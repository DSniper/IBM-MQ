#!/bin/bash

# Spring Boot IBM MQ - Quick Start Script
# This script automates the setup process for local development

set -e  # Exit on error

echo "🚀 Spring Boot IBM MQ - Quick Start"
echo "===================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
echo "📋 Checking prerequisites..."

# Check Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker found${NC}"

# Check Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}✗ Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker Compose found${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java is not installed. Please install Java 17 or higher.${NC}"
    exit 1
fi
java_version=$(java -version 2>&1 | awk -F'"' '/version/ {print $2}')
echo -e "${GREEN}✓ Java $java_version found${NC}"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⚠ Maven is not found, will use Maven wrapper${NC}"
    MAVEN_CMD="./mvnw"
else
    MAVEN_CMD="mvn"
    echo -e "${GREEN}✓ Maven found${NC}"
fi

echo ""
echo "📦 Starting IBM MQ Docker container..."
docker-compose up -d

echo -e "${GREEN}✓ IBM MQ started${NC}"
echo ""
echo "⏳ Waiting for IBM MQ to be ready (30 seconds)..."
sleep 30

echo ""
echo "🔨 Building Spring Boot application..."
$MAVEN_CMD clean package -DskipTests

echo -e "${GREEN}✓ Application built successfully${NC}"
echo ""
echo "🎯 Starting Spring Boot application..."
echo ""
echo "========================================="
echo -e "${GREEN}Application is starting...${NC}"
echo "========================================="
echo ""
echo "📡 API Endpoints:"
echo "   - Health Check: http://localhost:8080/api/v1/messages/health"
echo "   - Publish Text: http://localhost:8080/api/v1/messages/publish/text?message=Hello"
echo "   - Publish JSON: POST http://localhost:8080/api/v1/messages/publish/json"
echo ""
echo "🔌 IBM MQ Ports:"
echo "   - Listener: localhost:1414"
echo "   - Console HTTPS: https://localhost:9443/ibm/console"
echo "   - Console HTTP: http://localhost:9080/ibm/console"
echo ""
echo "📝 Default Credentials:"
echo "   - Username: app"
echo "   - Password: passw0rd"
echo ""
echo "========================================="
echo ""

# Run the application
$MAVEN_CMD spring-boot:run
