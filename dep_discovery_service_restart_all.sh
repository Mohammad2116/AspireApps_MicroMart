docker compose down --rmi local -v discovery-service
./mvnw -pl discovery-service -am clean package
docker compose build discovery-service
docker compose up -d discovery-service