docker compose -f infrastructure-docker-compose.yml down --rmi local -v

docker compose -f infrastructure-docker-compose.yml build --no-cache

docker compose -f infrastructure-docker-compose.yml up -d