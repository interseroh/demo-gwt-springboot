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
for the mentioned frameworks above. If you need a more sophisticated GWT application
framework you can use following frameworks:
- ArcBees GWT-Platform: Model-View-Presenter Framework for GWT
- JBoss Errai Framework
- Sencha GXT

The development is based on Maven so this project can be used by Eclipse, IntelliJ or NetBeans.

## Architecture

### Model for Services and Domains

There are two services: *UserService* and *PersonService* and two Entities: *Person* and *Address*. Following diagram shows the structure of the services and the domains.

![Service and Domain Model](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/demo-gwt-springboot-model.jpg)

### Architecture

Following diagram shows the architecture of the Microservice Demo.
The naming of the packages *client*, *server*, *shared* and *resource* (not shown in diagram) based on this architecture.

![Architecture](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/demo-gwt-springboot-architecture.jpg)

#### Client

All the GWT classes should stay in this package. GWT transpiles the Java source into JavaScript source.

#### Shared

In this package you can put any classes which will be used from both sides: client and server. It is advisable to put *constants* and *endpoints* of the RESTful services so that they point to the same address. Also *DTO* (Data Transfer Objects) for RESTful services should be included in this package. GWT will transpile this package into JavaScript.

#### Server

All the Spring Boot classes should stay in this package. This package will __not be included__ in GWT transpiler.

#### Resource

All the themes for GWTBootstrap3 should stay in this package.

## Run the WebApp

### Start the WebApp with Spring Boot

...

### Start GWT SuperDev Mode compiler

Starting GWT SuperDev Mode Compiler:

```java
mvn -P development process-classes gwt:run-codeserver
```



### Call the WebApp demo-gwt-springboot from a web browser

Application URL:

```java
http://localhost:9014/demogwt/demogwt.html
```

## Unit and Integration Testing

### GWT Mockito

...

### Spring Test

...