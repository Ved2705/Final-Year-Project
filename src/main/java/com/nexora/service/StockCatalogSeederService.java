package com.nexora.service;

import com.nexora.entity.*;
import com.nexora.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockCatalogSeederService {

        private final UserRepository userRepository;
        private final StartupRepository startupRepository;
        private final StockRepository stockRepository;
        private final PriceHistoryRepository priceHistoryRepository;
        private final InstrumentRepository instrumentRepository;
        private final MarketDataSnapshotRepository marketDataSnapshotRepository;

        private static record StockDef(String name, String symbol, String desc, String industry, String stage,
                        double val,
                        int emp, double price) {
        }

        private static final List<StockDef> CATALOG = List.of(
                        new StockDef("NeuralEdge Tech", "NURAI", "Edge AI chipsets and embedded neural inference.",
                                        "AI", "SEED",
                                        120000000.0, 15, 145.2),
                        new StockDef("CognitiveBotics", "CGBOT", "Conversational AI and autonomous enterprise agents.",
                                        "AI",
                                        "SERIES_A", 650000000.0, 55, 320.0),
                        new StockDef("VisionX AI", "VISX", "Computer vision and spatial tracking for smart factories.",
                                        "AI",
                                        "SERIES_B", 1400000000.0, 110, 510.5),
                        new StockDef("DeepSynth Audio", "DSYNT",
                                        "Generative speech synthesis and multilingual voice AI.", "AI",
                                        "SEED", 180000000.0, 20, 88.4),
                        new StockDef("AutoML Foundry", "AUTOML",
                                        "Low-code AutoML platform for predictive risk scoring.", "AI",
                                        "GROWTH", 2800000000.0, 190, 740.0),
                        new StockDef("Sentient Labs", "SENTI", "Natural language sentiment and market signal analysis.",
                                        "AI",
                                        "SERIES_A", 550000000.0, 45, 215.3),
                        new StockDef("HyperGen Media", "HYGEN",
                                        "Real-time AI generative video and 3D avatar rendering.", "AI",
                                        "SEED", 150000000.0, 18, 112.0),
                        new StockDef("NexusBrain", "NXBRN", "Enterprise knowledge graph and LLM orchestration.", "AI",
                                        "SERIES_B",
                                        1800000000.0, 140, 430.75),
                        new StockDef("RoboMind Systems", "RBMND",
                                        "Autonomous robotics control systems and SLAM algorithms.", "AI",
                                        "GROWTH", 3200000000.0, 220, 890.0),
                        new StockDef("PromptFlow", "PMTFL", "Developer tooling for LLM observability and evaluation.",
                                        "AI", "SEED",
                                        220000000.0, 25, 165.5),
                        new StockDef("BioIntelligence", "BIOAI",
                                        "Protein folding prediction and de novo drug design AI.", "AI",
                                        "SERIES_A", 700000000.0, 60, 290.0),
                        new StockDef("AegisAI Security", "AGSAI",
                                        "AI-driven threat intelligence and autonomous pentesting.", "AI",
                                        "SERIES_B", 1600000000.0, 130, 465.0),
                        new StockDef("OmniSearch AI", "OMNIS", "Semantic enterprise multimodal neural search engines.",
                                        "AI",
                                        "GROWTH", 2400000000.0, 175, 615.2),
                        new StockDef("CodeCraft AI", "CDCRF", "AI pair programmer for legacy mainframe modernization.",
                                        "AI",
                                        "SEED", 250000000.0, 22, 180.0),
                        new StockDef("DataMorph AI", "DMORP",
                                        "Synthetic tabular data generation and privacy preservation.", "AI",
                                        "SERIES_A", 850000000.0, 70, 340.5),
                        new StockDef("KoreSemantic", "KORES", "Domain-specific LLM fine-tuning for legal workflows.",
                                        "AI", "SEED",
                                        140000000.0, 14, 95.0),
                        new StockDef("AuraVision", "AURAV", "Drone-based aerial vision analytics for power lines.",
                                        "AI",
                                        "SERIES_B", 1950000000.0, 150, 525.0),
                        new StockDef("SpeechMatrix", "SPMTX", "Voice biometric authentication and deepfake defense.",
                                        "AI",
                                        "GROWTH", 3100000000.0, 210, 780.0),
                        new StockDef("InferenceFast", "INFST", "GPU acceleration compiler for micro-quantized models.",
                                        "AI",
                                        "SERIES_A", 900000000.0, 65, 310.25),
                        new StockDef("AetherLogic", "AETHL",
                                        "Autonomous negotiation bot for global supply chain procurement.",
                                        "AI", "SEED", 300000000.0, 28, 198.0),
                        new StockDef("CloudScale Systems", "CLDSC",
                                        "Multi-cloud Kubernetes management and FinOps optimizer.",
                                        "TECH", "GROWTH", 4200000000.0, 250, 850.0),
                        new StockDef("CyberFortress", "CYBFR",
                                        "Zero-trust network access (ZTNA) and identity management.", "TECH",
                                        "SERIES_B", 2100000000.0, 160, 475.5),
                        new StockDef("DevPulse Software", "DVPUL",
                                        "Continuous code delivery and developer telemetry tooling.",
                                        "TECH", "SERIES_A", 750000000.0, 55, 280.0),
                        new StockDef("ByteStream Data", "BYSTR",
                                        "High-throughput distributed event streaming platform.", "TECH",
                                        "GROWTH", 3500000000.0, 210, 690.0),
                        new StockDef("QuantumLogic", "QNTLG", "Quantum simulation and quantum-safe cryptography.",
                                        "TECH", "SEED",
                                        350000000.0, 30, 160.0),
                        new StockDef("ApexInfra Cloud", "APXIN",
                                        "Bare-metal server orchestration and edge computing mesh.", "TECH",
                                        "SERIES_B", 1900000000.0, 145, 415.0),
                        new StockDef("LogixStack", "LGXST",
                                        "Unified application performance monitoring and log analytics.", "TECH",
                                        "SERIES_A", 600000000.0, 50, 230.5),
                        new StockDef("SaaSFlow Tech", "SSFLW", "Subscription billing and revenue operations engine.",
                                        "TECH",
                                        "SEED", 200000000.0, 20, 125.0),
                        new StockDef("Veloce Networking", "VELOC",
                                        "Software-defined WAN and private 5G mesh networking.", "TECH",
                                        "SERIES_B", 1750000000.0, 135, 380.0),
                        new StockDef("MicroMesh Systems", "MMESH",
                                        "Serverless service mesh and microservice observability.",
                                        "TECH", "SEED", 280000000.0, 26, 170.0),
                        new StockDef("SecureVault", "SCVLT", "Hardware-encrypted key management and secrets vault.",
                                        "TECH",
                                        "GROWTH", 2900000000.0, 180, 640.0),
                        new StockDef("DataWeave Analytics", "DTWVE",
                                        "Real-time ETL pipeline and data transformation engine.",
                                        "TECH", "SERIES_A", 800000000.0, 68, 295.0),
                        new StockDef("GridLink EnergyTech", "GRDLK",
                                        "Smart electricity grid automation and IoT gateways.", "TECH",
                                        "SERIES_A", 950000000.0, 75, 335.0),
                        new StockDef("StackGuard DevOps", "STKGD",
                                        "Infrastructure-as-code security scanner and policy engine.",
                                        "TECH", "SEED", 180000000.0, 18, 110.0),
                        new StockDef("PolyGlot DB", "PLYDB", "Distributed hybrid transactional/analytical database.",
                                        "TECH",
                                        "SERIES_B", 2300000000.0, 165, 510.0),
                        new StockDef("EdgeNode Networks", "EDGND", "Global CDN and serverless compute at the edge.",
                                        "TECH",
                                        "GROWTH", 3800000000.0, 230, 720.0),
                        new StockDef("SyncSpace Collab", "SYSPC",
                                        "Local-first collaboration engine and real-time CRDT sync.",
                                        "TECH", "SEED", 240000000.0, 22, 145.0),
                        new StockDef("AeroCloud Systems", "ARCLD", "Aviation fleet maintenance and telematics SaaS.",
                                        "TECH",
                                        "SERIES_A", 880000000.0, 70, 310.0),
                        new StockDef("FirmShield IoT", "FRMSH",
                                        "Embedded firmware vulnerability detection for connected devices.",
                                        "TECH", "SEED", 160000000.0, 16, 98.0),
                        new StockDef("CoreMetric SaaS", "CRMET", "Customer retention and churn prediction analytics.",
                                        "TECH",
                                        "SERIES_B", 1650000000.0, 120, 395.0),
                        new StockDef("PayZen Direct", "PYZEN",
                                        "UPI switch infrastructure and cross-border remittances.", "FINTECH",
                                        "GROWTH", 5500000000.0, 320, 920.0),
                        new StockDef("WealthCraft India", "WLTCF",
                                        "Algorithmic portfolio management and tax harvesting.",
                                        "FINTECH", "SERIES_B", 2200000000.0, 160, 480.0),
                        new StockDef("CrediLens Analytics", "CRDLN", "Alternate data credit underwriting for MSMEs.",
                                        "FINTECH",
                                        "SERIES_A", 900000000.0, 75, 315.0),
                        new StockDef("InsurX Digital", "INSRX",
                                        "Parametric micro-insurance and instant claims processing.",
                                        "FINTECH", "SERIES_B", 1800000000.0, 130, 390.0),
                        new StockDef("KuberNeo Bank", "KUBER", "Digital-first commercial neobank for small businesses.",
                                        "FINTECH",
                                        "GROWTH", 4100000000.0, 240, 760.0),
                        new StockDef("LoanSprint", "LNSPR", "3-minute collateral-free working capital loan platform.",
                                        "FINTECH",
                                        "SERIES_A", 650000000.0, 52, 245.0),
                        new StockDef("AlgoTrade Pro", "ALTRD",
                                        "Ultra-low latency algorithmic trading execution platform.",
                                        "FINTECH", "SERIES_B", 1950000000.0, 145, 430.0),
                        new StockDef("RupyaPay Gateways", "RPYPY",
                                        "Offline soundbox and QR sound verification hardware.",
                                        "FINTECH", "SEED", 320000000.0, 30, 175.0),
                        new StockDef("LedgerTrust Escrow", "LDGTR",
                                        "Smart escrow accounts for real estate and B2B contracts.",
                                        "FINTECH", "SEED", 200000000.0, 19, 130.0),
                        new StockDef("FinShield FraudTech", "FNSHD",
                                        "Real-time card fraud and money laundering prevention.",
                                        "FINTECH", "SERIES_A", 850000000.0, 68, 290.0),
                        new StockDef("TaxBot Pro", "TXBOT",
                                        "Automated GST reconciliation and input tax credit optimizer.",
                                        "FINTECH", "SEED", 150000000.0, 15, 95.0),
                        new StockDef("PennyWise Micro", "PNYWS",
                                        "Spare change micro-savings and digital gold accumulation.",
                                        "FINTECH", "SEED", 250000000.0, 24, 150.0),
                        new StockDef("BimaSmart", "BMSMT", "Telematics-based pay-as-you-drive auto insurance broker.",
                                        "FINTECH",
                                        "SERIES_A", 700000000.0, 58, 260.0),
                        new StockDef("SettleFast", "STLFS", "T+0 instant interbank settlement and treasury management.",
                                        "FINTECH",
                                        "GROWTH", 3400000000.0, 195, 680.0),
                        new StockDef("ChitFunds Digital", "CHTFD",
                                        "Government-licensed digital chit fund bidding platform.",
                                        "FINTECH", "SEED", 180000000.0, 18, 115.0),
                        new StockDef("GlobalFx Hub", "GLBFX", "Multi-currency hedging and forward contract exchange.",
                                        "FINTECH",
                                        "SERIES_B", 2500000000.0, 170, 540.0),
                        new StockDef("BillPayr Systems", "BLPYR",
                                        "Automated utility bill discounting and invoice factoring.",
                                        "FINTECH", "SERIES_A", 600000000.0, 48, 225.0),
                        new StockDef("SmartCap Solutions", "SMRTC",
                                        "Secondary liquidity marketplace for unlisted startup shares.",
                                        "FINTECH", "GROWTH", 4600000000.0, 260, 810.0),
                        new StockDef("VyaparScore", "VYPRS", "Merchant point-of-sale credit rating aggregator.",
                                        "FINTECH", "SEED",
                                        280000000.0, 26, 165.0),
                        new StockDef("SafeCustody Assets", "SFCST",
                                        "Institutional custodian for tokenized digital bonds.",
                                        "FINTECH", "SERIES_B", 2100000000.0, 150, 455.0),
                        new StockDef("GeneCure Therapeutics", "GNCUR",
                                        "mRNA-based targeted oncology therapies and biosimilars.",
                                        "HEALTHCARE", "GROWTH", 4800000000.0, 280, 870.0),
                        new StockDef("NeuroPulse Diagnostics", "NRPUL",
                                        "Portable EEG headsets and stroke early-warning sensors.",
                                        "HEALTHCARE", "SERIES_B", 2100000000.0, 150, 460.0),
                        new StockDef("CardioWave Health", "CRDWV",
                                        "Continuous cardiac telemetry and arrhythmia screening.",
                                        "HEALTHCARE", "SERIES_A", 950000000.0, 72, 330.0),
                        new StockDef("PharmaSupply Cloud", "PHRSC",
                                        "Temperature-monitored cold chain pharmaceutical logistics.",
                                        "HEALTHCARE", "GROWTH", 3600000000.0, 210, 710.0),
                        new StockDef("DiabeTech Solutions", "DIBTC",
                                        "Non-invasive continuous glucose monitoring patches.",
                                        "HEALTHCARE", "SERIES_B", 1750000000.0, 130, 385.0),
                        new StockDef("TeleDoc Rural", "TLDOC",
                                        "Low-bandwidth telemedicine clinics for Tier-3/4 villages.",
                                        "HEALTHCARE", "SERIES_A", 550000000.0, 45, 210.0),
                        new StockDef("BioGenome Labs", "BGNOM",
                                        "Preventive whole-exome sequencing and rare disease tests.",
                                        "HEALTHCARE", "SEED", 300000000.0, 28, 160.0),
                        new StockDef("OrthoPrint 3D", "ORTPR",
                                        "Custom 3D-printed titanium implants for knee surgeries.",
                                        "HEALTHCARE", "SEED", 220000000.0, 20, 135.0),
                        new StockDef("AyuHerb Organics", "AYHRB",
                                        "Standardized clinically-proven herbal formulation extracts.",
                                        "HEALTHCARE", "SERIES_A", 800000000.0, 65, 275.0),
                        new StockDef("PathoScan AI", "PTHSC",
                                        "Automated digital pathology slide scanner and cell counter.",
                                        "HEALTHCARE", "SERIES_B", 1900000000.0, 140, 420.0),
                        new StockDef("CareCompanion", "CRCPN", "Remote eldercare monitoring and fall-detection radar.",
                                        "HEALTHCARE", "SEED", 180000000.0, 18, 115.0),
                        new StockDef("VisionEye Devices", "VSNEY",
                                        "AI fundus camera for diabetic retinopathy screening.",
                                        "HEALTHCARE", "SERIES_A", 650000000.0, 52, 240.0),
                        new StockDef("NanoMed Delivery", "NNOMD",
                                        "Targeted nanoparticle drug delivery for liver ailments.",
                                        "HEALTHCARE", "SEED", 350000000.0, 32, 180.0),
                        new StockDef("DentalAlign Pro", "DNTAL", "Clear dental aligners and intra-oral 3D scanning.",
                                        "HEALTHCARE",
                                        "SERIES_B", 1600000000.0, 115, 365.0),
                        new StockDef("HospitaLink BedOS", "HSPLK",
                                        "Hospital bed occupancy and ICU workflow management SaaS.",
                                        "HEALTHCARE", "SEED", 160000000.0, 15, 95.0),
                        new StockDef("SurgeAssist Robotics", "SRGAS",
                                        "Laparoscopic surgical robot with haptic feedback.",
                                        "HEALTHCARE", "GROWTH", 5200000000.0, 300, 940.0),
                        new StockDef("DermaScan AI", "DRMSC",
                                        "Smartphone skin lesion and melanoma screening application.",
                                        "HEALTHCARE", "SEED", 200000000.0, 19, 125.0),
                        new StockDef("BioRehab Systems", "BRHAB",
                                        "Exoskeleton robotic rehabilitation for paralysis patients.",
                                        "HEALTHCARE", "SERIES_A", 720000000.0, 58, 255.0),
                        new StockDef("PureWater Biotech", "PRWTR",
                                        "Microbial water purification filters for dialysis centers.",
                                        "HEALTHCARE", "SEED", 260000000.0, 24, 148.0),
                        new StockDef("StemCell Genesis", "STMCL",
                                        "Cord blood stem cell preservation and regenerative therapy.",
                                        "HEALTHCARE", "GROWTH", 3900000000.0, 230, 750.0),
                        new StockDef("SkillSprint Academy", "SKLSP",
                                        "Full-stack bootcamp with guaranteed hiring placement.",
                                        "EDTECH", "GROWTH", 3700000000.0, 220, 690.0),
                        new StockDef("VirtualLab VR", "VRTLB",
                                        "Immersive VR physics and chemistry laboratory simulations.",
                                        "EDTECH", "SERIES_B", 1800000000.0, 135, 395.0),
                        new StockDef("CodeCamp Junior", "CCMPJ", "Game-based coding curriculum for K-12 students.",
                                        "EDTECH",
                                        "SERIES_A", 850000000.0, 68, 290.0),
                        new StockDef("ExamCracker AI", "EXMCR",
                                        "Adaptive test preparation engine for JEE, NEET and UPSC.",
                                        "EDTECH", "SERIES_B", 2200000000.0, 160, 475.0),
                        new StockDef("LinguaBridge", "LNGBR", "Vernacular speech-to-English language learning app.",
                                        "EDTECH",
                                        "SEED", 280000000.0, 26, 165.0),
                        new StockDef("TutorConnect Pro", "TTRCN",
                                        "On-demand 1-on-1 expert tutor marketplace in 60 seconds.",
                                        "EDTECH", "SERIES_A", 600000000.0, 48, 230.0),
                        new StockDef("StemBox Learning", "STMBO",
                                        "Hands-on robotics and electronics DIY subscription kits.",
                                        "EDTECH", "SEED", 190000000.0, 18, 120.0),
                        new StockDef("EduFinance Credit", "EDFNC",
                                        "Zero-cost study-now-pay-later student education loans.",
                                        "EDTECH", "SERIES_B", 1650000000.0, 120, 370.0),
                        new StockDef("CampusERP Cloud", "CMERP",
                                        "Unified university management, attendance and grading ERP.",
                                        "EDTECH", "GROWTH", 2900000000.0, 175, 580.0),
                        new StockDef("MicroLearn Bites", "MCRLN",
                                        "3-minute corporate byte-sized micro-learning modules.", "EDTECH",
                                        "SEED", 150000000.0, 14, 98.0),
                        new StockDef("AeroEdu Simulators", "AREDU",
                                        "Flight simulation and aeronautical training software.",
                                        "EDTECH", "SEED", 240000000.0, 22, 145.0),
                        new StockDef("UpSkill AI", "UPSKL", "Corporate workforce upskilling and AI readiness badges.",
                                        "EDTECH",
                                        "SERIES_A", 950000000.0, 76, 325.0),
                        new StockDef("KiddoStory Interactive", "KDSTR",
                                        "Interactive animated moral stories and phonics reader.",
                                        "EDTECH", "SEED", 120000000.0, 12, 82.0),
                        new StockDef("GradPass Global", "GRDPS",
                                        "Study abroad admissions, visa assistance and test prep.",
                                        "EDTECH", "SERIES_B", 2100000000.0, 155, 440.0),
                        new StockDef("MathWhiz Interactive", "MTHWZ",
                                        "Visual mental math and gamified arithmetic drills.",
                                        "EDTECH", "SERIES_A", 700000000.0, 56, 250.0),
                        new StockDef("CyberSec Academy", "CYBAC",
                                        "Hands-on capture-the-flag cybersecurity training range.",
                                        "EDTECH", "SEED", 320000000.0, 30, 175.0),
                        new StockDef("MusicNotes Online", "MSCNT",
                                        "Real-time acoustic pitch correction and instrument learning.",
                                        "EDTECH", "SEED", 160000000.0, 15, 105.0),
                        new StockDef("ScholarHub Net", "SCHLR",
                                        "Aggregator for international research scholarships and grants.",
                                        "EDTECH", "SERIES_A", 500000000.0, 42, 195.0),
                        new StockDef("LogicGate Academy", "LGCGT", "Gate exam preparation and mock test benchmarking.",
                                        "EDTECH",
                                        "SEED", 220000000.0, 20, 138.0),
                        new StockDef("CareerPath Compass", "CRPTH",
                                        "AI-driven aptitude profiling and college stream advisor.",
                                        "EDTECH", "GROWTH", 3100000000.0, 190, 630.0),
                        new StockDef("QuickDrop Logistics", "QKDRP",
                                        "10-minute dark-store grocery delivery infrastructure.",
                                        "ECOMMERCE", "GROWTH", 6200000000.0, 360, 980.0),
                        new StockDef("KisanBazaar Direct", "KSNBZ",
                                        "Farm-to-fork direct agricultural procurement marketplace.",
                                        "ECOMMERCE", "SERIES_B", 2400000000.0, 170, 510.0),
                        new StockDef("GlamourBox D2C", "GLMBX",
                                        "Clean beauty and organic cosmetics direct-to-consumer brand.",
                                        "ECOMMERCE", "SERIES_A", 900000000.0, 72, 315.0),
                        new StockDef("UrbanFit Wear", "UBNFT", "Sustainable athleisure apparel and fitness activewear.",
                                        "ECOMMERCE", "SERIES_B", 1750000000.0, 125, 390.0),
                        new StockDef("ReturnRocket Tech", "RTRKT",
                                        "Automated e-commerce returns, exchanges and refund API.",
                                        "ECOMMERCE", "SEED", 250000000.0, 24, 150.0),
                        new StockDef("SnackRoots Foods", "SNKRT",
                                        "Traditional Indian regional snacks and healthy roasted millets.",
                                        "ECOMMERCE", "SERIES_A", 650000000.0, 54, 240.0),
                        new StockDef("DropShip Central", "DRPSH",
                                        "Automated supplier catalog synchronization for online stores.",
                                        "ECOMMERCE", "SEED", 200000000.0, 18, 128.0),
                        new StockDef("FleetTrack ColdChain", "FLTTR", "IoT-connected refrigerated reefer trucking.",
                                        "ECOMMERCE",
                                        "GROWTH", 3500000000.0, 210, 680.0),
                        new StockDef("CustomPrint Hub", "CSTPR",
                                        "On-demand merchandise printing and automated fulfillment.",
                                        "ECOMMERCE", "SEED", 180000000.0, 16, 115.0),
                        new StockDef("LuxeLiving Furniture", "LXLVN",
                                        "Modular flat-pack designer furniture for urban apartments.",
                                        "ECOMMERCE", "SERIES_B", 1950000000.0, 140, 430.0),
                        new StockDef("PetPals Direct", "PTPAL",
                                        "Premium organic pet nutrition and veterinary telemedicine.",
                                        "ECOMMERCE", "SERIES_A", 750000000.0, 60, 270.0),
                        new StockDef("ShopVernacular", "SHPVN",
                                        "Voice-assisted social commerce app for Tier-2/3 shoppers.",
                                        "ECOMMERCE", "SEED", 300000000.0, 28, 168.0),
                        new StockDef("EcoPack Solutions", "ECPCK",
                                        "100% biodegradable cassava starch packaging materials.",
                                        "ECOMMERCE", "SEED", 220000000.0, 20, 135.0),
                        new StockDef("ArtisanCraft India", "ARTCR",
                                        "Global export marketplace for certified GI-tag handlooms.",
                                        "ECOMMERCE", "SERIES_A", 550000000.0, 44, 210.0),
                        new StockDef("WareHousePro Cloud", "WRHPR",
                                        "Automated warehouse sorting robots and barcode AGVs.",
                                        "ECOMMERCE", "GROWTH", 4100000000.0, 240, 760.0),
                        new StockDef("RefurbGadgets Exch", "RFBGD",
                                        "45-point inspected certified refurbished smartphone portal.",
                                        "ECOMMERCE", "SERIES_B", 2300000000.0, 160, 490.0),
                        new StockDef("LastMile Electric", "LSTML",
                                        "Electric 2-wheeler and 3-wheeler delivery fleet as a service.",
                                        "ECOMMERCE", "GROWTH", 4800000000.0, 290, 840.0),
                        new StockDef("ChaiBrew Direct", "CHYBR",
                                        "Single-estate orthodox Darjeeling and Assam tea subscription.",
                                        "ECOMMERCE", "SEED", 160000000.0, 15, 98.0),
                        new StockDef("B2B WholesaleDirect", "B2BWD",
                                        "Bulk raw material procurement platform for manufacturing units.", "ECOMMERCE",
                                        "GROWTH",
                                        5100000000.0, 310, 890.0),
                        new StockDef("ClickNCollect Retail", "CLKNK",
                                        "Kirana store digitization and neighborhood store pickup.",
                                        "ECOMMERCE", "SEED", 280000000.0, 25, 160.0));

        @Transactional
        public int seedAllCatalogStocks() {
                log.info("Starting Java JPA automated stock catalog seeding (120 stocks across 6 markets)...");

                // Find or create default founders
                User founder = userRepository.findByEmail("founder1@nexora.com")
                                .or(() -> userRepository.findAll().stream()
                                                .filter(u -> u.getUserType() == User.UserType.STARTUP_FOUNDER)
                                                .findFirst())
                                .or(() -> userRepository.findAll().stream().findFirst())
                                .orElse(null);

                if (founder == null) {
                        log.warn("No user found to assign startups. Please seed users first.");
                        return 0;
                }

                LocalDate asOfDate = LocalDate.now().minusDays(1);
                int seededCount = 0;

                for (StockDef def : CATALOG) {
                        if (stockRepository.existsBySymbol(def.symbol())) {
                                continue;
                        }

                        // 1. Startup
                        Startup startup = startupRepository.findByName(def.name()).orElse(null);
                        if (startup == null) {
                                startup = startupRepository.save(Startup.builder()
                                                .founder(founder)
                                                .name(def.name())
                                                .description(def.desc())
                                                .industry(def.industry())
                                                .stage(Startup.Stage.valueOf(def.stage()))
                                                .valuation(BigDecimal.valueOf(def.val()))
                                                .foundedDate(LocalDate.now().minusYears(3))
                                                .employeeCount(def.emp())
                                                .revenue(BigDecimal.valueOf(def.val() * 0.12))
                                                .status(Startup.StartupStatus.ACTIVE)
                                                .build());
                        }

                        // 2. Stock
                        BigDecimal currentPrice = BigDecimal.valueOf(def.price());
                        long totalShares = (long) (def.val() / def.price());
                        long circulatingShares = (long) (totalShares * 0.75);
                        BigDecimal dayHigh = currentPrice.multiply(BigDecimal.valueOf(1.03)).setScale(2,
                                        RoundingMode.HALF_UP);
                        BigDecimal dayLow = currentPrice.multiply(BigDecimal.valueOf(0.97)).setScale(2,
                                        RoundingMode.HALF_UP);
                        BigDecimal yearHigh = currentPrice.multiply(BigDecimal.valueOf(1.35)).setScale(2,
                                        RoundingMode.HALF_UP);
                        BigDecimal yearLow = currentPrice.multiply(BigDecimal.valueOf(0.70)).setScale(2,
                                        RoundingMode.HALF_UP);

                        Stock stock = stockRepository.save(Stock.builder()
                                        .startup(startup)
                                        .symbol(def.symbol())
                                        .currentPrice(currentPrice)
                                        .totalShares(totalShares)
                                        .circulatingShares(circulatingShares)
                                        .dayHigh(dayHigh)
                                        .dayLow(dayLow)
                                        .yearHigh(yearHigh)
                                        .yearLow(yearLow)
                                        .dayVolume(120000L)
                                        .status(Stock.StockStatus.ACTIVE)
                                        .build());

                        // 3. Price History (10 days)
                        for (int i = 10; i >= 1; i--) {
                                BigDecimal closeP = currentPrice.multiply(BigDecimal.valueOf(1.0 - (i * 0.006)))
                                                .setScale(2,
                                                                RoundingMode.HALF_UP);
                                priceHistoryRepository.save(PriceHistory.builder()
                                                .stock(stock)
                                                .date(LocalDate.now().minusDays(i))
                                                .openPrice(closeP.multiply(BigDecimal.valueOf(0.99)).setScale(2,
                                                                RoundingMode.HALF_UP))
                                                .highPrice(closeP.multiply(BigDecimal.valueOf(1.02)).setScale(2,
                                                                RoundingMode.HALF_UP))
                                                .lowPrice(closeP.multiply(BigDecimal.valueOf(0.98)).setScale(2,
                                                                RoundingMode.HALF_UP))
                                                .closePrice(closeP)
                                                .volume(75000L)
                                                .build());
                        }

                        // 4. Instrument for AI Discovery (strictly exchange = NEXORA)
                        Instrument inst = instrumentRepository.findBySymbol(def.symbol()).orElse(null);
                        if (inst == null) {
                                inst = instrumentRepository.save(Instrument.builder()
                                                .symbol(def.symbol())
                                                .exchange("NEXORA")
                                                .companyName(def.name())
                                                .sector(def.industry())
                                                .industry(def.industry())
                                                .description(def.desc())
                                                .currentPrice(currentPrice)
                                                .marketCap(BigDecimal.valueOf(def.val()))
                                                .instrumentStatus(Instrument.InstrumentStatus.ACTIVE)
                                                .dataAsOf(asOfDate)
                                                .build());

                                // 5. MarketDataSnapshots (clean JPA objects - zero FK errors!)
                                double peVal = Math.max(14.0, def.val() / (def.val() * 0.12 * 0.20));
                                saveSnapshot(inst, "ROE", new BigDecimal("24.50"), "%", asOfDate);
                                saveSnapshot(inst, "PE_RATIO",
                                                BigDecimal.valueOf(peVal).setScale(2, RoundingMode.HALF_UP), "x",
                                                asOfDate);
                                saveSnapshot(inst, "DEBT_TO_EQUITY", new BigDecimal("0.22"), "x", asOfDate);
                                saveSnapshot(inst, "OPERATING_MARGIN", new BigDecimal("21.00"), "%", asOfDate);
                                saveSnapshot(inst, "REV_GROWTH_YOY", new BigDecimal("28.50"), "%", asOfDate);
                                saveSnapshot(inst, "VOLATILITY_BETA", new BigDecimal("0.98"), "x", asOfDate);
                        }

                        seededCount++;
                }

                log.info("Finished automated seeding. Successfully inserted {} new stocks!", seededCount);
                return seededCount;
        }

        private void saveSnapshot(Instrument inst, String key, BigDecimal value, String unit, LocalDate asOf) {
                marketDataSnapshotRepository.save(MarketDataSnapshot.builder()
                                .instrument(inst)
                                .metricKey(key)
                                .metricValue(value)
                                .unit(unit)
                                .currency("INR")
                                .source("NEXORA_PLATFORM_DISCLOSURE")
                                .asOf(asOf)
                                .retrievedAt(LocalDateTime.now())
                                .build());
        }
}
