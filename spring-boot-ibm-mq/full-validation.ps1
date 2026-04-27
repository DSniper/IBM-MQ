#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Full validation of IBM MQ and Spring Boot integration
.DESCRIPTION
    Tests all endpoints and connectivity
#>

Clear-Host
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║       IBM MQ & Spring Boot Application Validation             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
Write-Host "Timestamp: $timestamp" -ForegroundColor Yellow

# ═════════════════════════════════════════════════════════════════════════════
# TEST 1: Port Connectivity
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "TEST 1: Port Connectivity" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Cyan

$ports = @{
    "8081 (Spring Boot)" = 8081
    "1414 (IBM MQ)" = 1414
    "9080 (MQ HTTP)" = 9080
    "9443 (MQ HTTPS)" = 9443
}

foreach ($port in $ports.GetEnumerator()) {
    $test = Test-NetConnection -ComputerName localhost -Port $port.Value -WarningAction SilentlyContinue
    if ($test.TcpTestSucceeded) {
        Write-Host "  ✅ Port $($port.Key) - LISTENING" -ForegroundColor Green
    } else {
        Write-Host "  ❌ Port $($port.Key) - NOT LISTENING" -ForegroundColor Red
    }
}

# ═════════════════════════════════════════════════════════════════════════════
# TEST 2: Spring Boot API Endpoints
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "TEST 2: Spring Boot API Endpoints" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Cyan

$endpoints = @(
    @{
        Name = "Queue Depth"
        Method = "Get"
        URL = "http://localhost:8081/api/v1/queue/depth/DEV.QUEUE.1"
    },
    @{
        Name = "Publish Message"
        Method = "Post"
        URL = "http://localhost:8081/api/v1/messages/publish/text?message=Test"
    },
    @{
        Name = "Consume Message"
        Method = "Get"
        URL = "http://localhost:8081/api/v1/messages/consume"
    }
)

foreach ($endpoint in $endpoints) {
    try {
        $response = Invoke-WebRequest -Uri $endpoint.URL -Method $endpoint.Method -SkipHttpErrorCheck -TimeoutSec 5
        if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 201) {
            Write-Host "  ✅ $($endpoint.Name) - HTTP $($response.StatusCode)" -ForegroundColor Green
            Write-Host "     URL: $($endpoint.URL)"
            Write-Host "     Response: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))"
        } else {
            Write-Host "  ⚠️  $($endpoint.Name) - HTTP $($response.StatusCode)" -ForegroundColor Yellow
            Write-Host "     URL: $($endpoint.URL)"
        }
    } catch {
        Write-Host "  ❌ $($endpoint.Name) - Connection Error" -ForegroundColor Red
        Write-Host "     Error: $($_.Exception.Message.Substring(0, 80))"
    }
}

# ═════════════════════════════════════════════════════════════════════════════
# TEST 3: IBM MQ Console Access
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "TEST 3: IBM MQ Console Access" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Cyan

# HTTP Console
try {
    $response = Invoke-WebRequest -Uri "http://localhost:9080" -Method Get -SkipHttpErrorCheck -TimeoutSec 5
    Write-Host "  ✅ MQ HTTP Console (9080) - HTTP $($response.StatusCode)" -ForegroundColor Green
    Write-Host "     Access: http://localhost:9080" -ForegroundColor Green
} catch {
    Write-Host "  ❌ MQ HTTP Console - Connection Error" -ForegroundColor Red
}

# HTTPS Console (ignore cert errors)
try {
    [System.Net.ServicePointManager]::ServerCertificateValidationCallback = {$true}
    $response = Invoke-WebRequest -Uri "https://localhost:9443/ibmmq/console" -Method Get -SkipHttpErrorCheck -TimeoutSec 5
    Write-Host "  ✅ MQ HTTPS Console (9443) - HTTP $($response.StatusCode)" -ForegroundColor Green
    Write-Host "     Access: https://localhost:9443/ibmmq/console" -ForegroundColor Green
    Write-Host "     Credentials: admin / passw0rd" -ForegroundColor Yellow
} catch {
    Write-Host "  ⚠️  MQ HTTPS Console - May require authentication" -ForegroundColor Yellow
}

# ═════════════════════════════════════════════════════════════════════════════
# TEST 4: Docker Container Status
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "TEST 4: Docker Container Status" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Cyan

try {
    $container = docker ps --filter "name=ibm-mq" --format "{{.Status}}" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ IBM MQ Container - $container" -ForegroundColor Green
    } else {
        Write-Host "  ❌ Docker not accessible" -ForegroundColor Red
    }
} catch {
    Write-Host "  ❌ Error checking Docker" -ForegroundColor Red
}

# ═════════════════════════════════════════════════════════════════════════════
# TEST 5: Java Process Status
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "TEST 5: Java Process Status" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Cyan

try {
    $javaProcess = Get-Process java -ErrorAction SilentlyContinue | Where-Object {$_.ProcessName -eq "java"}
    if ($javaProcess) {
        Write-Host "  ✅ Spring Boot Java Process Running" -ForegroundColor Green
        Write-Host "     PID: $($javaProcess.Id)"
        Write-Host "     Memory: $([math]::Round($javaProcess.WorkingSet / 1MB, 2)) MB"
        Write-Host "     CPU Time: $([math]::Round($javaProcess.TotalProcessorTime.TotalSeconds, 2)) seconds"
    } else {
        Write-Host "  ❌ Spring Boot Java Process Not Found" -ForegroundColor Red
    }
} catch {
    Write-Host "  ❌ Error checking Java process" -ForegroundColor Red
}

# ═════════════════════════════════════════════════════════════════════════════
# Summary
# ═════════════════════════════════════════════════════════════════════════════

Write-Host ""
Write-Host "═════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "QUICK ACCESS LINKS" -ForegroundColor Cyan
Write-Host "═════════════════════════════════════════════════════════════════" -ForegroundColor Cyan

Write-Host ""
Write-Host "Spring Boot Application:" -ForegroundColor Yellow
Write-Host "  📍 Base URL: http://localhost:8081/api" -ForegroundColor Green
Write-Host "  📍 Queue Depth: http://localhost:8081/api/v1/queue/depth/DEV.QUEUE.1" -ForegroundColor Green
Write-Host "  📍 Publish Message: http://localhost:8081/api/v1/messages/publish/text?message=YourMessage" -ForegroundColor Green

Write-Host ""
Write-Host "IBM MQ Console:" -ForegroundColor Yellow
Write-Host "  📍 HTTP: http://localhost:9080" -ForegroundColor Green
Write-Host "  📍 HTTPS: https://localhost:9443/ibmmq/console" -ForegroundColor Green
Write-Host "  📍 Username: admin" -ForegroundColor Cyan
Write-Host "  📍 Password: passw0rd" -ForegroundColor Cyan

Write-Host ""
Write-Host "IBM MQ Connection (for apps):" -ForegroundColor Yellow
Write-Host "  📍 Host: localhost" -ForegroundColor Green
Write-Host "  📍 Port: 1414" -ForegroundColor Green
Write-Host "  📍 Queue Manager: QM1" -ForegroundColor Green
Write-Host "  📍 Username: app" -ForegroundColor Cyan
Write-Host "  📍 Password: passw0rd" -ForegroundColor Cyan

Write-Host ""
Write-Host "═════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "✅ Validation Complete!" -ForegroundColor Green
Write-Host "═════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
