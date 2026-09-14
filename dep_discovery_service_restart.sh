docker compose -f docker-compose.yml down --rmi local -v

./mvnw -pl discovery-service -am clean package

docker compose -f docker-compose.yml build discovery-service

docker compose -f docker-compose.yml up -d discovery-service