docker compose -f docker-compose.yml down --rmi local -v

./mvnw -pl identity-service -am clean package

docker compose -f docker-compose.yml build --no-cache

docker compose -f docker-compose.yml up -d