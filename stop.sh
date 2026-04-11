#!/bin/bash

# Stop script for Bookstore application containers
# Uses Podman Compose to stop and remove Kafka and Apicurio Registry

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Stopping Bookstore containers..."

# Check if podman-compose is available
if ! command -v podman-compose &> /dev/null; then
    echo "Error: podman-compose is not installed or not in PATH"
    exit 1
fi

# Stop containers
podman-compose down

echo ""
echo "Containers stopped successfully!"
echo ""
