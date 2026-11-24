@echo off
title Nexora Platform Launcher
echo ===================================================
echo            Starting Nexora Platform (Localhost)
echo ===================================================

echo [1/2] Starting Spring Boot Backend on port 8080...
start "Nexora Backend" cmd /k "mvnw.cmd spring-boot:run"

echo [2/2] Starting React Frontend on port 5173...
start "Nexora Frontend" cmd /k "cd frontend-react && npm run dev"

echo.
echo ===================================================
echo Nexora is starting!
echo Backend API: http://localhost:8080/api
echo Frontend UI: http://localhost:5173
echo ===================================================
ping 127.0.0.1 -n 6 >nul
start http://localhost:5173
