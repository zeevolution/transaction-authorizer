# `transaction-authorizer`: a java application for banking transaction authorization

This is a `java` application for authorizing banking transaction based on a set predefined rules.

The program receives `json` lines as inputs that are read from `operations` file at
"/src/main/java/resources/operations". As output, the program provides `json` lines for each given input
— imagine this as a stream of events arriving at an authorizer consumer.

## Setup

#### Requirements

#### Build

#### Run

## Application Architecture

Let's talk about the structure or architecture of the application.
The project has been separated into the following layers:

1. <b>User interface (UI)</b> includes the use of terminal panel for displaying transaction outputs.
2. <b>Consumer</b> receives and handles transaction events.
3. <b>Services</b> process transaction events by applying a set of predefined rules,and produce an output for each event.
4. <b>Models</b> represent the domain that we're using within the system (e.g., `Account`, and `Transaction`).