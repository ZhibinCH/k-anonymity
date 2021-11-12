# k-anonymity


## Development environment
The main development of example is carried out using Eclipse (v 4.19.0) as an IDE. Please import this project as a “Maven project”. A `pom.xml` file for Maven is already provided, which declares all necessary dependencies, including
for instance,  the ARX library, `org.junit.jupiter 5.8.1` for running unit tests and  `net.jqwik 1.5.67` for running property-based tests. Moreover since the ARX library is also neccessary to run the example, please make sure that `libarx-3.9.0.jar` is located in `src/main/resources`.

## Source code files
Directory: `src/main/java/challenge`
```
Helper.java
KAnonymityExample.java
```
