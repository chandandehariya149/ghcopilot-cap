---
description: "Use when designing or changing the eventual Java/Spring Boot application, including Java source, Maven/Gradle build files, configuration, and tests."
applyTo:
  - "**/*.java"
  - "pom.xml"
  - "**/pom.xml"
  - "build.gradle"
  - "build.gradle.kts"
  - "**/build.gradle"
  - "**/build.gradle.kts"
  - "**/src/**/resources/**"
---
# Java and Spring Boot guidance

- Follow the approved architecture and the repository's existing Java version, build tool, package layout, and framework conventions.
- If this is a new application, select a supported Java LTS and compatible Spring Boot release during architecture approval; pin versions and explain the choice. Do not guess versions during workflow setup.
- Prefer constructor injection, small cohesive types, explicit validation at boundaries, and clear error responses consistent with the API contract.
- Keep secrets out of source, logs, test fixtures, and generated documentation. Use externalized configuration and safe examples.
- Add focused tests for behavior and relevant edge cases. Use the test libraries already present unless the approved architecture authorizes a change.
- Avoid unnecessary dependencies and broad refactors. Document migrations, configuration changes, and operational implications.