Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "      Starting Nexora Platform on Localhost         " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

Write-Host "`n[1/2] Starting Spring Boot Backend (http://localhost:8080)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot'; .\mvnw.cmd spring-boot:run"

Write-Host "[2/2] Starting React Vite Frontend (http://localhost:5173)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\frontend-react'; npm run dev"

Start-Sleep -Seconds 6
Start-Process "http://localhost:5173"
Write-Host "`nNexora opened in browser! (http://localhost:5173)" -ForegroundColor Green
