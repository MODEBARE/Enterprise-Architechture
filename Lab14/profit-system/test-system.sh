#!/bin/bash

# Profit System - Test Script

echo "Testing Profit System APIs..."
echo "============================"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to test an endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local expected_status=${3:-200}
    
    echo -e "${BLUE}Testing: $name${NC}"
    echo "URL: $url"
    
    response=$(curl -s -w "%{http_code}" -o /tmp/response.json "$url")
    status_code=${response: -3}
    
    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✅ Success (Status: $status_code)${NC}"
        if [ -s /tmp/response.json ]; then
            echo "Response: $(cat /tmp/response.json | head -c 200)..."
        fi
    else
        echo -e "${RED}❌ Failed (Status: $status_code)${NC}"
        if [ -s /tmp/response.json ]; then
            echo "Response: $(cat /tmp/response.json)"
        fi
    fi
    echo ""
}

echo "Waiting for services to be ready..."
sleep 5

echo ""
echo "1. Testing Health Endpoints"
echo "=========================="
test_endpoint "Profit Service Health" "http://localhost:8081/api/profit/health"
test_endpoint "Currency Service Health" "http://localhost:8082/api/currency/health" 
test_endpoint "Company Service Health" "http://localhost:8083/api/company/health"

echo ""
echo "2. Testing Profit Service"
echo "========================"
test_endpoint "Get January 2024 Profit" "http://localhost:8081/api/profit/month/2024-01"
test_endpoint "Get 2024 Total Profit" "http://localhost:8081/api/profit/year/2024/total"

echo ""
echo "3. Testing Currency Service"  
echo "=========================="
test_endpoint "Convert 1000 USD to EUR" "http://localhost:8082/api/currency/convert?amount=1000&from=USD&to=EUR"
test_endpoint "Get USD to GBP Rate" "http://localhost:8082/api/currency/rate?from=USD&to=GBP"

echo ""
echo "4. Testing Company Service (LLM with Tool Calling)"
echo "=================================================="
test_endpoint "Ask about January 2024 profit" "http://localhost:8083/api/company/query?question=What%20was%20the%20profit%20in%20January%202024?"
test_endpoint "Ask about best month in EUR" "http://localhost:8083/api/company/query?question=What%20was%20our%20best%20performing%20month?&currency=EUR"

echo ""
echo "5. Advanced LLM Queries"
echo "======================"
echo -e "${BLUE}Testing: Complex profit analysis${NC}"
curl -s -X POST http://localhost:8083/api/company/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Compare the profits of 2023 vs 2024 and tell me which year was better",
    "currency": "USD"
  }' | head -c 300
echo ""
echo ""

echo -e "${BLUE}Testing: Currency conversion query${NC}"
curl -s -X POST http://localhost:8083/api/company/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What was the profit in December 2024 converted to Japanese Yen?",
    "currency": "JPY"
  }' | head -c 300
echo ""
echo ""

echo "================================"
echo "✅ Test completed!"
echo ""
echo "To see full responses and HTTP request logs, check:"
echo "  • logs/company-service.log (for HTTP interceptor logs)"
echo "  • logs/profit-service.log"
echo "  • logs/currency-service.log"

# Clean up
rm -f /tmp/response.json