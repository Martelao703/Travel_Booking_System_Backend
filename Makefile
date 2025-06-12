.PHONY: dev

dev:
	docker compose down -v
	docker compose up -d
	mvn spring-boot:run -Denv=dev