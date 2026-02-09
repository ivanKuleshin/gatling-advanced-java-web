# Advanced Gatling for Stress Testing Web Applications - Java Edition

[![Gatling](https://img.shields.io/badge/Gatling-3.14.9-blue)](https://gatling.io/)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red)](https://maven.apache.org/)

## Description

This repository contains the source code accompanying the Udemy course **["Advanced Gatling for Stress Testing Web Applications - Java Edition"](https://www.udemy.com/course/advanced-gatling-java/)**. It provides comprehensive examples of advanced load testing techniques using Gatling's Java API to stress test web applications.

The project simulates user interactions with an e-commerce website (AceToys) and demonstrates various load patterns including instant users, ramped users, and constant users per second.

## Prerequisites

- **Java**: 21 or higher
- **Maven**: 3.6 or higher

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ivanKuleshin/gatling-advanced-java-web.git
   cd gatling-advanced-java-web
   ```

2. Compile the project:
   ```bash
   mvn clean compile
   ```

## Usage

### Running Simulations

Execute the Gatling tests using Maven:

```bash
mvn gatling:test
```

### Test Types

You can specify different load patterns using the `TEST_TYPE` system property:

- **INSTANT_USERS** (default): Injects users instantly
  ```bash
  mvn gatling:test -DTEST_TYPE=INSTANT_USERS
  ```

- **RAMP_USERS**: Gradually ramps up users over time
  ```bash
  mvn gatling:test -DTEST_TYPE=RAMP_USERS
  ```

- **USERS_PER_SECOND**: Maintains constant users per second with randomization
  ```bash
  mvn gatling:test -DTEST_TYPE=USERS_PER_SECOND
  ```

### Configuration

Customize load parameters using system properties:

- `USER_COUNT`: Number of users (default: 10)
- `RAMP_DURATION`: Ramp duration in seconds (default: 20)

Example:
```bash
mvn gatling:test -DUSER_COUNT=50 -DRAMP_DURATION=30
```

## Project Structure

```
src/
├── main/java/
│   ├── annotation/
│   └── enums/
└── test/
    ├── java/
    │   ├── acetoys/
    │   │   ├── AceToysMainSimulation.java    # Main simulation class
    │   │   ├── actions/                       # User action definitions
    │   │   ├── populations/                   # Population builders
    │   │   ├── scenarios/                     # Scenario definitions
    │   │   └── session/                       # Session management
    │   ├── Engine.java
    │   └── IDEPathHelper.java
    └── resources/
        ├── data/                              # Test data files
        ├── gatling.conf                       # Gatling configuration
        └── logback-test.xml                   # Logging configuration
```

## Key Features

- **Multiple Load Patterns**: Instant, ramped, and constant user injection
- **Realistic Scenarios**: Simulates e-commerce user journeys
- **Configurable Parameters**: Easily adjust user counts and durations
- **Comprehensive Actions**: Covers browsing, searching, purchasing workflows
- **Data-Driven Testing**: Uses CSV and JSON feeders for dynamic data

## License

This project is provided as-is for educational purposes.
