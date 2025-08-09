#!/bin/bash

# Profit System - Service Startup Script

echo "Starting Profit System Services..."
echo "=================================="

# Check if OPENAI_API_KEY is set
if [ -z "$OPENAI_API_KEY" ]; then
    echo "⚠️  WARNING: OPENAI_API_KEY environment variable is not set!"
    echo "   Please set it with: export OPENAI_API_KEY=your_api_key_here"
    echo "   The Company Service (LLM) will not work without it."
    echo ""
fi

# Function to start a service in background
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    echo "🚀 Starting $service_name on port $port..."
    cd $service_dir
    mvn spring-boot:run > ../logs/${service_name}.log 2>&1 &
    local pid=$!
    echo "   PID: $pid"
    echo $pid > ../logs/${service_name}.pid
    cd ..
}

# Create logs directory
mkdir -p logs

echo "Starting services in background..."
echo ""

# Start Profit Service
start_service "profit-service" "profit-service" "8081"

# Wait a moment for profit service to start
echo "⏳ Waiting for Profit Service to initialize..."
sleep 10

# Start Currency Service
start_service "currency-service" "currency-service" "8082"

# Wait a moment for currency service to start
echo "⏳ Waiting for Currency Service to initialize..."
sleep 10

# Start Company Service
start_service "company-service" "company-service" "8083"

echo ""
echo "✅ All services started!"
echo ""
echo "Service URLs:"
echo "  • Profit Service:  http://localhost:8081/api/profit/health"
echo "  • Currency Service: http://localhost:8082/api/currency/health"  
echo "  • Company Service:  http://localhost:8083/api/company/health"
echo ""
echo "H2 Database Console: http://localhost:8081/h2-console"
echo "  JDBC URL: jdbc:h2:mem:profitdb"
echo "  Username: sa"
echo "  Password: (empty)"
echo ""
echo "Example Company Service Query:"
echo "  curl \"http://localhost:8083/api/company/query?question=What was the profit in January 2024?\""
echo ""
echo "Logs are written to:"
echo "  • logs/profit-service.log"
echo "  • logs/currency-service.log"
echo "  • logs/company-service.log"
echo ""
echo "To stop all services, run: ./stop-services.sh"