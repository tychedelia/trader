.PHONY: docker-up
docker-up:
	COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --build

.PHONY: docker-up
docker-down:
	docker-compose down -v

.PHONY: migrate
migrate:
	lein run migrate
