# demo-gwt-springboot

## Build Status

[![Build Status](https://travis-ci.org/lofidewanto/demo-gwt-springboot.svg?branch=master)](https://travis-ci.org/lofidewanto/demo-gwt-springboot)

## Introduction

This is an example Maven project for following frameworks:

- User Interfaces: 
  - GWT
  - GWTBootstrap3 for the UI
  - RestyGWT for the RESTful access to backend services
  - GIN for Dependency Injection
  - GWT Event Binder for event bus
  - GWT Mockito for UI logic test
- Controllers and Services: 
  - KissMDA
  - Spring Boot for business logic implementations
  - All the standard stuffs used by Spring Framework
- Domains: 
  - KissMDA
  - JPA with Hibernate
 
The idea of this project is to offer a simple application template 
for the mentioned frameworks above.

The development is based on Eclipse or IDEAJ and Maven.

## Architecture

...  

## Start the WebApp with Spring Boot

...

## Start GWT SuperDev Mode compiler

Starting GWT SuperDev Mode Compiler:

```java
mvn -P development process-classes gwt:run-codeserver
```

## Run the WebApp demo-gwt-springboot

Application URL:

```java
http://localhost:9014/demogwt/demogwt.html
```