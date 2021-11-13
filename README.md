# k-anonymity
The aim of [k-anonymity](https://en.wikipedia.org/wiki/K-anonymity) is to protect datasets from re-identification. A dataset is k-anonymous if each record cannot be distinguished from at least k-1 other records. [ARX](https://arx.deidentifier.org) is an open source tool for anonymizing structured (i.e. tabular) sensitive personal data. It supports a wide variety of (1) privacy and risk models, (2) methods for transforming data and (3) methods for analyzing the usefulness of output data. This demo provides a brief overview of running k-anonymity as the selected privacy model via ARX [API](https://arx.deidentifier.org/development/api/).

## Development environment
The main development of example is carried out using Eclipse (v 4.19.0) as an IDE. Please import this project as a “Maven project”. A `pom.xml` file for Maven is already provided, which declares all necessary dependencies, including
for instance,  the ARX library, `org.junit.jupiter 5.8.1` for running unit tests and  `net.jqwik 1.5.67` for running property-based tests. Moreover since the ARX library is also neccessary to run the example, please make sure that `libarx-3.9.0.jar` is located in `src/main/resources`.

## Source code files
Directory: `src/main/java/challenge`
```
Helper.java
KAnonymityExample.java
```
