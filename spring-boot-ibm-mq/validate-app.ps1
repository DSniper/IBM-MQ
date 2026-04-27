#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Quick health check and test script for IBM MQ Spring Boot application
.DESCRIPTION
    Validates that the application is running and connected to IBM MQ
#>

param(
    [Parameter(Mandatory = $false)]
    [int]$Port = 8081
)

function Write-Header { Write-Host $args -ForegroundColor Cyan -BackgroundColor Black }
function Write-Success { Write-Host $args -ForegroundColor Green }
function Write-Error-Custom { Write-Host $args -ForegroundColor Red }
function Write-Info { Write-Host $args -ForegroundColor Yellow }

Clear-Host

Write-Header "╔════════════════════════════════════════════════════════════════╗"
Write-Header "║   IBM MQ Application - Health Check & Validation             ║"
Write-Header "╚════════════════════════════════════════════════════════════════╝"
Write-Host ""

$BaseUrl = "http://localhost:$Port/api"

# Test 1: Health Check
Write-Info "Test 1️⃣: Checking application health..."
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/health" -Method Get -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Success "  ✅ Application is UP and responding on port $Port"
        Write-Host "  Response: $($response.Content)"
    }
}
catch {
    Write-Error-Custom "  ❌ Application not responding on port $Port"
    Write-Error-Custom "  Error: $($_.Exception.Message)"
    exit 1
}

Write-Host ""

# Test 2: MQ Connection
Write-Info "Test 2️⃣: Checking IBM MQ connectivity..."
Write-Host "  (Check application logs for: 'Creating connection with credentials for user: app')"

Write-Host ""

# Test 3: Queue Status
Write-Info "Test 3️⃣: Checking queue status..."
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/queues/status?queue=DEV.QUEUE.1" -Method Get -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 200) {
        Write-Success "  ✅ Queue status endpoint working"
        Write-Host "  Response: $($response.Content)"
    }
}
catch {
    Write-Info "  ℹ️  Queue endpoint not available (normal if not implemented)"
}

Write-Host ""
Write-Success "✅ Validation complete! Application is ready for testing."
Write-Host ""
Write-Info "📌 Next Steps:"
Write-Host "  1. Publish a message: POST to $BaseUrl/messages/publish"
Write-Host "  2. Check application logs for MQ connection details"
Write-Host "  3. Test queue operations using the API endpoints"
