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

All the GWT (UI and REST client) classes should stay in this package. GWT transpiles all the Java sources into JavaScript sources.

#### Shared

In this package you can put any classes which will be used from both sides: client and server. It is advisable to put *constants* and *endpoints* of the RESTful services so that they point to the same address. Also *DTO* (Data Transfer Objects) for RESTful services should be included in this package. GWT will transpile this package into JavaScript sources.

#### Server

All the Spring Boot classes should stay in this package. This package will __not be included__ in GWT transpiler.

#### Resource

All the themes for GWTBootstrap3 and general Bootstrap themes like Bootswatch should stay in this package. 

You can take a look the GWT [configuration file](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwt.gwt.xml) to see which packages will be included in GWT transpiler.

## Run the WebApp

### Server: Start the WebApp with Spring Boot

Just run the class *DemoGwtSpringbootApplication* or if you are using Spring Tool Suite just run it with Spring Boot Dashboard:

![STS Spring Boot Dashboard](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-boot-dashboard.png)

#### Tips

##### JRebel

- If you are using JRebel you need to put following parameter in VM Arguments, something like:

```java
-javaagent:C:\progjava\jrebel\jrebel.jar
```

![Spring Boot with JRebel parameter](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-boot-jrebel.png)

- You also have to comment out the dependency in pom.xml.

```java
        <!-- Use this Spring Tool for restarting the app automatically -->
		<!-- Only use this if you don't use JRebel! -->
		<!-- 
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
		</dependency>
		-->
```

- You need to compile the project with Maven profile *development*.

![Maven compile with Profile development](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-maven-development-profile.png)

##### Spring Boot Dev Tools

Spring Boot Dev Tools will restart the Spring Boot App automatically if codes have changed.
You have to deactivate JRebel if you want to use this tool. This dependency should be activated:

```java
        <!-- Use this Spring Tool for restarting the app automatically -->
		<!-- Only use this if you don't use JRebel! -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
		</dependency>
```

### Client: Start GWT SuperDev Mode transpiler

To be able to test quickly you can use GWT SuperDev Mode. With this tool you can just recompile the changes in GWT Java codes into JavaScript codes without restarting the process.

Follow following steps:

#### Starting GWT SuperDev Mode

Starting GWT SuperDev Mode Compiler from command line or within the development environment:

```java
mvn -P development process-classes gwt:run-codeserver
```

At the end you can see following message:

```java
...
[INFO] The code server is ready at http://localhost:9876/
...
```
Now you can go the given address and boomark the *Dev Mode On* through *drag and drop* into your bookmark menu.

![GWT SuperDev Mode](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-bookmarks.png)

That's it. You can just push *Dev Mode On* to run the transpiler directly and the WebApp will be reloaded automatically. 

![GWT SuperDev Mode](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-compiler.png)

### Call the WebApp demo-gwt-springboot from a web browser

Go to the application URL with a web browser:

```java
http://localhost:9014/demogwt/demogwt.html
```

### Logging

...

### Debugging GWT SuperDev Mode

...

## Unit and Integration Testing

### GWT Mockito

...

### Spring Test

...