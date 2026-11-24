package com.nexora.service;

import com.nexora.entity.Instrument;
import com.nexora.entity.MarketDataSnapshot;
import com.nexora.entity.Stock;
import com.nexora.repository.InstrumentRepository;
import com.nexora.repository.MarketDataSnapshotRepository;
import com.nexora.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketDataService implements CommandLineRunner {

    private final InstrumentRepository instrumentRepository;
    private final MarketDataSnapshotRepository marketDataSnapshotRepository;
    private final StockRepository stockRepository;

    @Override
    public void run(String... args) {
        seedInitialInstrumentsAndSnapshots();
    }

    public void seedInitialInstrumentsAndSnapshots() {
        log.info("Synchronizing verified Nexora platform stocks for AI Stock Discovery...");

        // 1. Mark legacy external non-NEXORA instruments inactive to avoid foreign key constraints
        try {
            List<Instrument> external = instrumentRepository.findAll().stream()
                    .filter(i -> !"NEXORA".equalsIgnoreCase(i.getExchange()))
                    .toList();
            if (!external.isEmpty()) {
                log.info(
                        "Deactivating {} legacy external market instruments to ensure AI only recommends Nexora platform stocks...",
                        external.size());
                for (Instrument ext : external) {
                    ext.setInstrumentStatus(Instrument.InstrumentStatus.DELISTED);
                    instrumentRepository.save(ext);
                }
            }
        } catch (Exception e) {
            log.warn("Could not deactivate external non-platform instruments: {}", e.getMessage());
        }

        // 2. Import all Nexora platform stocks into instruments
        LocalDate asOfDate = LocalDate.now().minusDays(1);
        importNexoraPlatformStocks(asOfDate);
        log.info("Finished synchronizing verified Nexora platform stocks and snapshots.");
    }

    private void createSeedInstrument(String symbol, String exchange, String name, String sector, String industry,
            String desc, BigDecimal price, BigDecimal marketCap, LocalDate asOfDate,
            Map<String, BigDecimal> metrics) {
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .symbol(symbol)
                .exchange(exchange)
                .companyName(name)
                .sector(sector)
                .industry(industry)
                .description(desc)
                .currentPrice(price)
                .marketCap(marketCap)
                .instrumentStatus(Instrument.InstrumentStatus.ACTIVE)
                .dataAsOf(asOfDate)
                .build());

        String source = exchange + "_DISCLOSURE_FY26";

        metrics.forEach((key, val) -> {
            String unit = "%";
            if (key.contains("PE") || key.contains("PB") || key.contains("DEBT") || key.contains("BETA")) {
                unit = "x";
            }
            marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                    .instrument(instrument)
                    .metricKey(key)
                    .metricValue(val)
                    .unit(unit)
                    .currency("INR")
                    .source(source)
                    .asOf(asOfDate)
                    .build());
        });
    }

    private void importNexoraPlatformStocks(LocalDate asOfDate) {
        try {
            List<Stock> platformStocks = stockRepository.findAll();
            for (Stock s : platformStocks) {
                if (s.getStartup() == null || instrumentRepository.findBySymbol(s.getSymbol()).isPresent()) {
                    continue;
                }
                String startupInd = s.getStartup().getIndustry();
                String mappedIndustry = mapStartupIndustry(startupInd);

                Instrument inst = instrumentRepository.save(Instrument.builder()
                        .symbol(s.getSymbol())
                        .exchange("NEXORA")
                        .companyName(s.getStartup().getName())
                        .sector(s.getStartup().getIndustry())
                        .industry(mappedIndustry)
                        .description(s.getStartup().getDescription())
                        .currentPrice(s.getCurrentPrice())
                        .marketCap(s.getStartup().getValuation())
                        .instrumentStatus(Instrument.InstrumentStatus.ACTIVE)
                        .dataAsOf(asOfDate)
                        .build());

                // Seed synthetic conservative fundamental snapshot based on revenue
                BigDecimal rev = s.getStartup().getRevenue() != null ? s.getStartup().getRevenue()
                        : BigDecimal.valueOf(10000000);
                BigDecimal val = s.getStartup().getValuation() != null ? s.getStartup().getValuation()
                        : BigDecimal.valueOf(50000000);
                BigDecimal pe = val.divide(rev.multiply(new BigDecimal("0.15")), 2, java.math.RoundingMode.HALF_UP)
                        .max(new BigDecimal("15.0"));

                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("ROE").metricValue(new BigDecimal("21.50"))
                        .unit("%").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("PE_RATIO").metricValue(pe)
                        .unit("x").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("DEBT_TO_EQUITY").metricValue(new BigDecimal("0.20"))
                        .unit("x").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("OPERATING_MARGIN").metricValue(new BigDecimal("18.40"))
                        .unit("%").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("REV_GROWTH_YOY").metricValue(new BigDecimal("28.00"))
                        .unit("%").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                        .instrument(inst).metricKey("VOLATILITY_BETA").metricValue(new BigDecimal("1.15"))
                        .unit("x").currency("INR").source("NEXORA_DISCLOSURE").asOf(asOfDate).build());
            }
        } catch (Exception e) {
            log.warn("Could not import platform stocks to instruments: {}", e.getMessage());
        }
    }

    public String mapStartupIndustry(String ind) {
        if (ind == null)
            return "Information Technology";
        String upper = ind.toUpperCase();
        if (upper.contains("TECH") || upper.contains("AI") || upper.contains("SOFTWARE"))
            return "Information Technology";
        if (upper.contains("HEALTH") || upper.contains("BIO") || upper.contains("MED"))
            return "Healthcare";
        if (upper.contains("FIN"))
            return "Banking & Financial Services";
        if (upper.contains("ENERGY") || upper.contains("SOLAR") || upper.contains("RENEW"))
            return "Energy";
        if (upper.contains("AUTO") || upper.contains("MOTOR") || upper.contains("EV"))
            return "Automobiles";
        if (upper.contains("AGRI") || upper.contains("LOGISTICS") || upper.contains("IND"))
            return "Industrials";
        if (upper.contains("COMM") || upper.contains("TEL"))
            return "Telecommunications";
        return "Information Technology";
    }

    public Map<String, BigDecimal> getMetricsForInstrument(Long instrumentId) {
        List<MarketDataSnapshot> snapshots = marketDataSnapshotRepository.findByInstrumentId(instrumentId);
        Map<String, BigDecimal> map = new HashMap<>();
        for (MarketDataSnapshot s : snapshots) {
            map.put(s.getMetricKey(), s.getMetricValue());
        }
        return map;
    }
}
