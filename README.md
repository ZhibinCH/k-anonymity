# k-anonymity
The aim of [k-anonymity](https://en.wikipedia.org/wiki/K-anonymity) is to protect datasets from re-identification. A dataset is k-anonymous if each record cannot be distinguished from at least k-1 other records. [ARX](https://arx.deidentifier.org) is an open source tool for anonymizing structured (i.e. tabular) sensitive personal data. It supports a wide variety of (1) privacy and risk models, (2) methods for transforming data and (3) methods for analyzing the usefulness of output data. This demo provides a brief overview of running k-anonymity as the selected privacy model via ARX [API](https://arx.deidentifier.org/development/api/).

## Development environment
The main development of example is carried out using Eclipse (v 4.19.0) as an IDE. Please import this project as a “Maven project”. A `pom.xml` file for Maven is already provided, which declares all necessary dependencies, including
for instance,  the ARX library, `org.junit.jupiter 5.8.1` for running unit tests and  `net.jqwik 1.5.67` for running property-based tests. Moreover since the ARX library is also neccessary to run the example, please make sure that `libarx-3.9.0.jar` is located in `src/main/resources`.

## Source code files
Directory: `src/main/java/challenge/`
```
Helper.java
KAnonymityExample.java
```
## Data files
Directory: `data/`
```
test.csv  
test_hierarchy_age.csv
test_hierarchy_gender.csv
test_hierarchy_zipcode.csv
```
To note, an output .csv file will be saved in this directory `data`, please make sure path is authorized.
## Library
Directory: `src/main/resources`
```
libarx-3.9.0.jar
```
## Workflow of implementation
1. Defining input data
The class `Data` offers different ways to provide data to the ARX framework. An intuitive approach is to define the input data manually. However, it is not feasible when data size is large and any change in the dataset requires modification in source code. Therefore in this example, the `test.csv` file is provided for defining input data.
2. Adding a generalization hierarchy to the data definition
Similar to step 1, we can define the generalization hierarchy manually or by loading a hierarchy file, e.g. `test_hierarchy_age.csv`. Moreover, one can also create functional hierarches in the ARX framework. In this example, thoese above mentioned ways are also included.
3. Defining privacy models and transformation rules
The k-Anonymity privacy model is selected and some releted fields in configuration can be specified by user, e.g. we can specify the suppression limit which allows for a certain threshold  (in percentage) of outliers.
4. Executing the anonymization algorithm
5. Accessing and comparing data
6. Analyzing re-identification risks
7. Writing data
