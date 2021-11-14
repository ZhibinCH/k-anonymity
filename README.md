# k-anonymity
## Introduction
Our society places great demand on the sharing data collections containing person-specific information. As more and more public information becomes electronically accessible,  a person can be more easily re-identified by directly linking these data, even explicit identifiers such as name, address and telephone number are removed.  Therefore we need a solution to release person-specific data while individuals who are the subjects of the data cannot be re-identified. An an attempt to solve this problem, a formal protection model named [k-anonymity](https://en.wikipedia.org/wiki/K-anonymity)  and a set of accompanying policies for deployment were introduced [[1]](#1). A dataset is called k-anonymous if each record cannot be distinguished from at least k-1 other records. Operationally, one is  expected to identify all attributes in person-specific data which could be used for linking to other information. Such attributes can be the above mentioned explicit identifiers and also a combination of attributes that can uniquely identify individuals such as birth date and gender. These attributes are called quasi-identifier and  the ability to link with external information using the quasi-identifier should be limited. Therefore, in a k-anonymous dataset, if there are at least k records sharing the same value for each combination of the quasi-identifiers, individuals cannot be uniquely identified by linking attacks.<br/>

[ARX](https://arx.deidentifier.org) is an open source tool for anonymizing structured (i.e. tabular) sensitive personal data. It supports a wide variety of (1) privacy and risk models, (2) methods for transforming data and (3) methods for analyzing the usefulness of output data. This example provides a brief overview of running k-anonymity as the selected privacy model via ARX [API](https://arx.deidentifier.org/development/api/). Furthermore, this example including data is implemented based on and inspired by materials on the [ARX Github](https://github.com/arx-deidentifier/arx) repository.

## Development environment
The main development of example is carried out using Eclipse (v 4.19.0) as an IDE. Please import this project as a “Maven project”. A `pom.xml` file for Maven is already provided, which declares all necessary dependencies, including
for instance,  the ARX library, `org.junit.jupiter 5.8.1` for running unit tests and  `net.jqwik 1.5.67` for running property-based tests. Moreover since the ARX library is also neccessary to run the example, please make sure that `libarx-3.9.0.jar` is located in `src/main/resources`.

## Source code files
Directory: `src/main/java/challenge/`
```
Helper.java
KAnonymityExample.java
```
Test directory: `src/test/java/challenge/`
```
TestKAnonymityExample.java
```

## Data files
Directory: `data/`
```
test.csv  
test_hierarchy_age.csv
test_hierarchy_gender.csv
test_hierarchy_zipcode.csv
```
The above files are originally from [here](https://github.com/arx-deidentifier/arx/tree/master/data). To note, an output .csv file will be saved in this directory `data`, please make sure path is authorized.
## Library
Directory: `src/main/resources`
```
libarx-3.9.0.jar
```
To download this Java library, please refer to the [official site](https://arx.deidentifier.org/downloads/) of ARX.
## Workflow of implementation
1. Defining input data<br/>
The class `Data` offers different ways to provide data to the ARX framework. An intuitive approach is to define the input data manually. However, it is not feasible when data size is large and any change in the dataset requires modification in source code. Therefore in this example, the `test.csv` file is provided for defining input data.
2. Adding a generalization hierarchy to the data definition<br/>
Similar to step 1, we can define the generalization hierarchy manually or by loading a hierarchy file, e.g. `test_hierarchy_age.csv`. Moreover, one can also create functional hierarchies in the ARX framework. In this example, the above mentioned ways are also included.
3. Defining privacy models and transformation rules<br/>
The k-Anonymity privacy model is selected and some related fields in configuration can be specified by user, e.g. we can specify the suppression limit which allows for a certain threshold  (in percentage) of outliers.
4. Executing the anonymization algorithm
5. Accessing and comparing data<br/>
The input and output are printed out in order to compare them easily. Intuitively, if more attributes are available, the risk of a person gets re-identified is higher. Based on [[2]](#2), we can use distinction and separation to qualify quasi-identifiers of input, which can in combination be used for re-identification attacks.<br/>
(1) An &alpha; - distinct quasi-identifier is a subset of attributes which becomes a key in the table remaining after the removal of at most a 1 − &alpha; fraction of tuples in the original table.<br/>
(2) A subset of attributes separates a pair of tuples x and y if x and y have different values on at least one attribute in the subset. An &alpha;-separation quasi-identifier is a subset of attributes which separates at least a α fraction of all possible tuple pairs.<br/>
<div align="center">

| | age | sex | state|
|------ | ------ | ------ | ------ |
|1|20|f|CA|
|2|30|f|CA|
|3|40|f|TX|
|4|20|m|NY|
|5|40|m|CA|

</div>

The above table has 3 attributes. The attribute _age_ is a 0.6-distinct quasi-identifier because it has 3 distinct values in a total of 5 tuples; it is a 0.8-separation quasi-identifier because there are 10 distinct pairs of tuples (select 2 tuples out of 5 tuples, there are 10 possible cominations) and 8 pairs can be separated by _age_.

6. Analyzing re-identification risks <br/>
In this step some associated risks with respect to re-identification are printed out. The following table illustrates the definition of each term. <br/>

| term| definition | 
|------| ------ | 
|Average risk |the average re-identification risk|
|Lowest risk |the lowest re-identification risk of any records in the data set|
|Tuples affected <br> by the lowest risk|the fraction of records affected by the lowest re-identification risk|
|Highest risk  |the highest re-identification risk of any records in the data set|
|Tuples affected <br> by the highest risk|the fraction of records affected by the highest re-identification risk|
|Sample uniqueness|the fraction of records affected by the highest re-identification risk|
|Population uniqueness <br> (Zayatz)|estimated number of unique tuples in the population according to the Zayatz model |


7. Writing data

## References
<a id="1">[1]</a>
Sweeney, L. (2002). k-anonymity: A model for protecting privacy. International Journal of Uncertainty, Fuzziness and Knowledge-Based Systems, 10(05), 557-570.<br/>
<a id="2">[2]</a> 
Motwani, R., & Xu, Y. (2007, September). Efficient algorithms for masking and finding quasi-identifiers. In Proceedings of the Conference on Very Large Data Bases (VLDB) (pp. 83-93).<br/>
<a id="3">[3]</a> 
Samaratiy, P., & Sweeney, L. Protecting Privacy when Disclosing Information: k-Anonymity and Its Enforcement through Generalization and Suppression.
