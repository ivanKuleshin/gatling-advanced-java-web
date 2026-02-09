# TestNG Tests

This directory contains TestNG-based integration tests for the Gatling project, specifically focused on Xray plugin integration.

## Test Classes

- **XrayTest.java**: Basic test for Xray plugin integration using random number generation.
- **XrayAnotherTest.java**: Another test for Xray integration.
- **XrayAnotherRandomTest.java**: Contains two additional tests for random number validation.

## Workflow

The test execution workflow is configured in the project's `pom.xml` and involves the following steps:

1. **Test Execution**: TestNG tests are run using the Maven Surefire plugin, which generates results in `target/surefire-reports/testng-results.xml`.

2. **Xray Import**: The Xray Maven plugin automatically imports the TestNG results into Xray Cloud for test management and reporting. The plugin is configured with:
   - Cloud instance
   - Client ID and Secret for authentication
   - Project Key: XSP
   - Test Execution Key: XSP-69
   - Report format: TestNG

3. **Groovy Validation**: A Groovy script (via the Groovy Maven plugin) parses the TestNG results file to check for failed tests. If any failures are detected, it throws a RuntimeException to halt the build.

## Running the Tests

To run these TestNG tests, use the following Maven command from the project root:

```bash
mvn test
```

These tests are part of the overall test suite and can be executed alongside other tests.

## Purpose

These tests ensure that the Xray plugin is properly integrated and functioning within the TestNG framework for test management and reporting.
