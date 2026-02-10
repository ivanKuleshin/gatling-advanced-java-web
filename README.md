Advanced Gatling for Stress Testing Web Applications - Java Edition
============================================

Source code to support the Advanced Gatling for Stress Testing Web Applications - Java Edition course on Udemy

## Prerequisites

- Java 21
- Maven (or use the included Maven wrapper)

## Running the Tests

To run the Gatling performance tests, use the following command:

```bash
mvn gatling:test -Dgatling.simulationClass=acetoys.AceToysMainSimulation
```

Or using the Maven wrapper:

```bash
./mvnw gatling:test -Dgatling.simulationClass=acetoys.AceToysMainSimulation
```

This will execute the `AceToysMainSimulation` test scenario and generate performance reports.
