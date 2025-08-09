#!/bin/bash

# Profit System - Service Shutdown Script

echo "Stopping Profit System Services..."
echo "=================================="

# Function to stop a service
stop_service() {
    local service_name=$1
    
    if [ -f "logs/${service_name}.pid" ]; then
        local pid=$(cat logs/${service_name}.pid)
        echo "🛑 Stopping $service_name (PID: $pid)..."
        
        if kill -0 $pid 2>/dev/null; then
            kill $pid
            sleep 2
            
            # Force kill if still running
            if kill -0 $pid 2>/dev/null; then
                echo "   Force killing $service_name..."
                kill -9 $pid
            fi
            
            echo "   ✅ $service_name stopped"
        else
            echo "   ⚠️  $service_name was not running"
        fi
        
        rm -f logs/${service_name}.pid
    else
        echo "   ⚠️  No PID file found for $service_name"
    fi
}

# Stop all services
stop_service "company-service"
stop_service "currency-service" 
stop_service "profit-service"

echo ""
echo "✅ All services stopped!"
echo ""
echo "Log files are preserved in the logs/ directory"