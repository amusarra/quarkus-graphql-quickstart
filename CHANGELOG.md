# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [1.2.0] - 2025-05-30

### Changed
- update the Quarkus version to 3.23

## [1.1.0] - 2025-04-17

### Changed
- Changed: update the Quarkus version to 3.21.3

## [1.0.1] - 2025-02-17

### Fixed
- Fixed: field dependency injection should be avoided java:S6813 (#6)
- Fixed: resolve the SonarQube maintainability issues (#8)
- Fixed: resolve the SonarQube maintainability issues (two round) (#11)

## [1.0.0] - 2025-02-17

### Added
- Initial release of the Quarkus GraphQL Quickstart project.
- Integration with MinIO as an Object Store S3.
- GraphQL API for managing authors and books.
- REST API for health checks and application info.
- Database integration with PostgreSQL and H2 for testing.
- Unit and integration tests using JUnit 5 and RestAssured.
- Support for native image generation with GraalVM.
- Docker support for containerized deployment.
- OpenAPI and Swagger UI for API documentation.
- Health checks and metrics with SmallRye Health.
- SSL configuration with Keytool Maven Plugin.
- License management with License Maven Plugin.
- Code coverage reporting with JaCoCo and SonarCloud integration.
