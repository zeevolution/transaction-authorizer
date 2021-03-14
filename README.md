# `transaction-authorizer`: a java application for banking transaction authorization

This is a `java` application for authorizing banking transaction based on a set predefined rules.

The program receives `json` lines as inputs that are read from `operations` file at
"/src/main/java/resources/operations". As output, the program provides `json` lines for each given input
— imagine this as a stream of events arriving at an authorizer consumer.

## Local Setup

#### 1. Requirements

- Java (OpenJDK 15+) ([instruction](https://www.oracle.com/br/java/technologies/javase-downloads.html))
- Maven (3.6.3+) ([instructions](https://maven.apache.org/install.html))
- Your favorite IDE (the recommended one is [IntelliJ Community Edition](https://www.jetbrains.com/pt-br/idea/download/)). 

#### 2. Build via terminal
In this project's root directory, run:

```bash
mvn clean install
```

#### 3. Run via terminal
`AuthorizerApplication` is the project's main file. 
So, in order to run application, execute:

```bash
mvn exec:java -Dexec.mainClass=transaction.authorizer.AuthorizerApplication
```

The application reads all transaction inputs inside `"/src/main/java/resources/operations"`
, and produces a `output` file in the project's root directory.

If you want to test application against other transaction inputs, feel free to change 
content inside `"/src/main/java/resources/operations"`, and re-run application.

## Application Architecture

Let's talk about the structure or architecture of the application.
The project has been separated into the following layers:

1. <b>Main Application</b> (AuthorizerApplication) starts application, and setups input and output files.
2. <b>Consumers</b> receive and handle transaction events.
3. <b>Services</b> process transaction events by applying a set of predefined rules, and producing an output for each event.
4. <b>Models</b> represent the domain that we're using within the application (e.g., `Account`, and `Transaction`).