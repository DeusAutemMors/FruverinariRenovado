# Ejecutar Fruverinari sin PostgreSQL (H2 en memoria)
Set-Location $PSScriptRoot
Write-Host "Iniciando Fruverinari (perfil: local, H2 en memoria)..." -ForegroundColor Green
Write-Host "URL: http://localhost:8080/Login" -ForegroundColor Cyan
Write-Host "Usuario admin: admin / 1234 | Cajero: cajero / 1234" -ForegroundColor Yellow
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
