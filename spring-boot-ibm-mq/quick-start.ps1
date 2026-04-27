# Spring Boot IBM MQ - Quick Start Script (Windows)
# This script automates the setup process for local development on Windows

Write-Host "🚀 Spring Boot IBM MQ - Quick Start (Windows)" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green
Write-Host ""

# Colors
$GreenCheck = "✓"
$RedX = "✗"

# Function to check if command exists
function Test-CommandExists {
    param($command)
    $null = Get-Command $command -ErrorAction SilentlyContinue
    return $?
}

# Check prerequisites
Write-Host "📋 Checking prerequisites..." -ForegroundColor Yellow

# Check Docker
if (Test-CommandExists docker) {
    Write-Host "$GreenCheck Docker found" -ForegroundColor Green
} else {
    Write-Host "$RedX Docker is not installed. Please install Docker Desktop for Windows." -ForegroundColor Red
    exit 1
}

# Check Docker Compose
if (Test-CommandExists docker-compose) {
    Write-Host "$GreenCheck Docker Compose found" -ForegroundColor Green
} else {
    Write-Host "$RedX Docker Compose is not installed. Please install Docker Desktop for Windows." -ForegroundColor Red
    exit 1
}

# Check Java
if (Test-CommandExists java) {
    $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "$GreenCheck Java found: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "$RedX Java is not installed. Please install Java 17 or higher." -ForegroundColor Red
    exit 1
}

# Check Maven
if (Test-CommandExists mvn) {
    Write-Host "$GreenCheck Maven found" -ForegroundColor Green
    $MAVEN_CMD = "mvn"
} else {
    Write-Host "$GreenCheck Will use Maven wrapper" -ForegroundColor Green
    $MAVEN_CMD = ".\mvnw.cmd"
}

Write-Host ""
Write-Host "📦 Starting IBM MQ Docker container..." -ForegroundColor Yellow

# Start Docker container
docker-compose up -d

Write-Host "$GreenCheck IBM MQ started" -ForegroundColor Green
Write-Host ""
Write-Host "⏳ Waiting for IBM MQ to be ready (30 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host ""
Write-Host "🔨 Building Spring Boot application..." -ForegroundColor Yellow

# Build application
& $MAVEN_CMD clean package -DskipTests

Write-Host "$GreenCheck Application built successfully" -ForegroundColor Green
Write-Host ""
Write-Host "🎯 Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Application is starting..." -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "📡 API Endpoints:" -ForegroundColor Cyan
Write-Host "   - Health Check: http://localhost:8080/api/v1/messages/health"
Write-Host "   - Publish Text: http://localhost:8080/api/v1/messages/publish/text?message=Hello"
Write-Host "   - Publish JSON: POST http://localhost:8080/api/v1/messages/publish/json"
Write-Host ""

Write-Host "🔌 IBM MQ Ports:" -ForegroundColor Cyan
Write-Host "   - Listener: localhost:1414"
Write-Host "   - Console HTTPS: https://localhost:9443/ibm/console"
Write-Host "   - Console HTTP: http://localhost:9080/ibm/console"
Write-Host ""

Write-Host "📝 Default Credentials:" -ForegroundColor Cyan
Write-Host "   - Username: app"
Write-Host "   - Password: passw0rd"
Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Run the application
& $MAVEN_CMD spring-boot:run

# Cleanup on exit
Write-Host ""
Write-Host "Shutting down IBM MQ..." -ForegroundColor Yellow
docker-compose down

Write-Host "$GreenCheck IBM MQ stopped" -ForegroundColor Green
