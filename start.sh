#!/bin/bash

# Start script for Bookstore application containers
# Uses Podman Compose to start Kafka and Apicurio Registry

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Starting Bookstore containers..."

# Check if podman-compose is available
if ! command -v podman-compose &> /dev/null; then
    echo "Error: podman-compose is not installed or not in PATH"
    exit 1
fi

# Pull images if not present (optional, uncomment to enable)
# echo "Pulling container images..."
# podman-compose pull

# Start containers
podman-compose up -d

echo ""
echo "Containers started successfully!"
echo ""
echo "Services:"
echo "  - Kafka:        localhost:9092"
echo "  - Apicurio UI:  http://localhost:8080/ui"
echo ""
echo "To check container status:"
echo "  podman ps"
echo ""
echo "To view logs:"
echo "  podman-compose logs -f"
