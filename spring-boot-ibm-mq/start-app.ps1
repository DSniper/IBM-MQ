#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Complete automation script to start IBM MQ, build, and run Spring Boot application
.DESCRIPTION
    This script handles all steps needed to get the application running:
    1. Starts IBM MQ Docker container
    2. Waits for IBM MQ to be ready
    3. Builds Spring Boot application
    4. Runs the application with proper credentials
.AUTHOR
    IBM MQ Spring Boot Integration Team
#>

param(
    [Parameter(Mandatory = $false)]
    [int]$Port = 8081,
    
    [Parameter(Mandatory = $false)]
    [switch]$SkipMQ = $false,
    
    [Parameter(Mandatory = $false)]
    [switch]$BuildOnly = $false
)

# Color output functions
function Write-Header { Write-Host $args -ForegroundColor Cyan -BackgroundColor Black }
function Write-Success { Write-Host $args -ForegroundColor Green }
function Write-Error-Custom { Write-Host $args -ForegroundColor Red }
function Write-Info { Write-Host $args -ForegroundColor Yellow }

Clear-Host

Write-Header "╔════════════════════════════════════════════════════════════════╗"
Write-Header "║   IBM MQ Spring Boot Integration - Complete Setup Script      ║"
Write-Header "║   Version: 1.0.0                                              ║"
Write-Header "╚════════════════════════════════════════════════════════════════╝"
Write-Host ""

# Script configuration
$RootPath = "d:\Softwares\MQ\IBM MQ"
$ProjectPath = "$RootPath\spring-boot-ibm-mq"
$JarPath = "$ProjectPath\target\spring-boot-ibm-mq-1.0.0.jar"

# Credentials
$MQUser = "app"
$MQPassword = "passw0rd"
$MQQueueManager = "QM1"
$MQChannel = "DEV.APP.SVRCONN"
$MQConnection = "localhost(1414)"

Write-Header "═══════════════════════════════════════════════════════════════"
Write-Info "📋 Configuration Summary:"
Write-Host "  Root Directory:    $RootPath"
Write-Host "  Project Directory: $ProjectPath"
Write-Host "  MQ Username:       $MQUser"
Write-Host "  MQ Queue Manager:  $MQQueueManager"
Write-Host "  MQ Channel:        $MQChannel"
Write-Host "  Application Port:  $Port"
Write-Header "═══════════════════════════════════════════════════════════════"
Write-Host ""

# ═════════════════════════════════════════════════════════════════════════════
# STEP 1: Start IBM MQ Container
# ═════════════════════════════════════════════════════════════════════════════

if (-not $SkipMQ) {
    Write-Header "STEP 1: Starting IBM MQ Docker Container..."
    Write-Host ""
    
    try {
        Push-Location $RootPath
        
        # Check if docker-compose is available
        $dcVersion = docker-compose --version 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Error-Custom "❌ Docker Compose not found. Please install Docker Desktop."
            exit 1
        }
        
        Write-Info "  🐳 Docker Compose version: $dcVersion"
        
        # Start containers
        Write-Info "  ▶️  Starting IBM MQ container..."
        docker-compose up -d 2>&1 | ForEach-Object { Write-Host "     $_" }
        
        if ($LASTEXITCODE -ne 0) {
            Write-Error-Custom "❌ Failed to start Docker containers"
            exit 1
        }
        
        Write-Info "  ⏳ Waiting for IBM MQ to be ready (this takes 30-60 seconds)..."
        
        # Wait for MQ to be ready
        $maxRetries = 60
        $retryCount = 0
        $isMQReady = $false
        
        while ($retryCount -lt $maxRetries) {
            $retryCount++
            $portOpen = Test-NetConnection -ComputerName localhost -Port 1414 -WarningAction SilentlyContinue
            
            if ($portOpen.TcpTestSucceeded) {
                $isMQReady = $true
                break
            }
            
            Write-Host -NoNewline "."
            Start-Sleep -Seconds 1
        }
        
        Write-Host ""
        
        if ($isMQReady) {
            Write-Success "  ✅ IBM MQ is ready on port 1414"
        }
        else {
            Write-Error-Custom "  ❌ IBM MQ failed to start within timeout"
            docker-compose logs
            exit 1
        }
        
        Pop-Location
    }
    catch {
        Write-Error-Custom "❌ Error starting IBM MQ: $_"
        exit 1
    }
}
else {
    Write-Info "⏭️  Skipping IBM MQ startup (as requested)"
}

Write-Host ""

# ═════════════════════════════════════════════════════════════════════════════
# STEP 2: Build Spring Boot Application
# ═════════════════════════════════════════════════════════════════════════════

Write-Header "STEP 2: Building Spring Boot Application..."
Write-Host ""

try {
    Push-Location $ProjectPath
    
    Write-Info "  🔨 Running Maven clean package..."
    Write-Host "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    $buildOutput = mvn clean package -DskipTests 2>&1
    
    # Check for build success
    if ($buildOutput | Select-String "BUILD SUCCESS") {
        Write-Host $buildOutput
        Write-Success "  ✅ Build completed successfully"
    }
    else {
        Write-Error-Custom "  ❌ Build failed"
        Write-Host $buildOutput
        exit 1
    }
    
    # Verify JAR exists
    if (Test-Path $JarPath) {
        $jarSize = (Get-Item $JarPath).Length / 1MB
        Write-Success "  ✅ JAR file created: spring-boot-ibm-mq-1.0.0.jar ($([math]::Round($jarSize, 2)) MB)"
    }
    else {
        Write-Error-Custom "  ❌ JAR file not found at: $JarPath"
        exit 1
    }
    
    Pop-Location
}
catch {
    Write-Error-Custom "❌ Error during build: $_"
    exit 1
}

Write-Host ""

# ═════════════════════════════════════════════════════════════════════════════
# STEP 3: Run Application (if not build-only)
# ═════════════════════════════════════════════════════════════════════════════

if ($BuildOnly) {
    Write-Success "✅ Build completed successfully!"
    Write-Info "📌 To run the application, execute:"
    Write-Host "   java -jar $JarPath --server.port=$Port"
    exit 0
}

Write-Header "STEP 3: Starting Spring Boot Application..."
Write-Host ""

Write-Info "🚀 Launching application with credentials..."
Write-Host "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "  Credentials:"
Write-Host "    Username: $MQUser"
Write-Host "    Password: (***hidden***)"
Write-Host "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host ""

try {
    # Prepare Java command with credentials
    $javaArgs = @(
        "-jar",
        $JarPath,
        "--server.port=$Port",
        "--mq.user=$MQUser",
        "--mq.password=$MQPassword",
        "--mq.queue-manager=$MQQueueManager",
        "--mq.channel=$MQChannel",
        "--mq.connection-name=$MQConnection"
    )
    
    Write-Info "📝 Waiting for application to start..."
    Write-Info "   Health endpoint: http://localhost:$Port/api/health"
    Write-Info "   Press Ctrl+C to stop the application"
    Write-Host ""
    
    # Run the application
    & java @javaArgs
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error-Custom "❌ Application exited with error code: $LASTEXITCODE"
        exit $LASTEXITCODE
    }
}
catch {
    Write-Error-Custom "❌ Error running application: $_"
    exit 1
}
