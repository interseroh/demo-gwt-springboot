# demo-gwt-springboot

## Build Status

[![Build Status](https://travis-ci.org/interseroh/demo-gwt-springboot.svg?branch=master)](https://travis-ci.org/interseroh/demo-gwt-springboot)
[![Heroku](https://heroku-badge.herokuapp.com/?app=demo-gwt-springboot&root=demogwt/demogwt.html)](https://demo-gwt-springboot.herokuapp.com/demogwt/demogwt.html)

## Table of Contents

- [Introduction](#introduction)
- [Architecture](#architecture)
	- [Model for Services and Domains](#model-for-services-and-domains)
	- [Architecture](#architecture)
- [Run the WebApp for Development](#run-the-webapp-for-development)
	- [Server: Start the WebApp with Spring Boot](#server-start-the-webapp-with-spring-boot)
	- [Client: Start GWT SuperDev Mode transpiler](#client-start-gwt-superdev-mode-transpiler)
	- [Browser: Call the WebApp demo-gwt-springboot from a web browser](#browser-call-the-webapp-demo-gwt-springboot-from-a-web-browser)
	- [Heroku: Test the Webapp from Heroku](#heroku-test-the-webapp-from-heroku)
- [Logging](#logging)
	- [Server: Logging at the Spring Boot Console](#server-logging-at-the-spring-boot-console)
	- [Client: Logging at the Browser Console](#client-logging-at-the-browser-console)
- [Debugging](#debugging)
	- [Server: Debugging Spring Boot](#server-debugging-spring-boot)
	- [Client: Debugging with GWT SuperDev Mode](#client-gwt-debugging-with-gwt-superdev-mode)
	- [Client: Debugging with Eclipse SDBG](#client-gwt-debugging-with-eclipse-sdbg)
	- [Client: Debugging with IntelliJ IDEA](#client-gwt-debugging-with-intellij-idea)
- [Unit and Integration Testing](#unit-and-integration-testing)
    - [Server: Spring Test](#server-spring-test)
    - [Client: GWT Mockito](#client-gwt-mockito)

## Introduction

This is an example Maven project for following frameworks:

- User Interfaces (Client): 
  - GWT
  - GWTBootstrap3 for the UI
  - RestyGWT for the RESTful access to backend services
  - GIN for Dependency Injection
  - GWT Event Binder for event bus
  - GWT Mockito for UI logic test
- Controllers and Services (Server): 
  - KissMDA
  - Spring Boot for business logic implementations
  - All the standard stuffs used by Spring Framework
- Domains (Server): 
  - KissMDA
  - JPA with Hibernate
 
The idea of this project is to offer a simple application template 
for the mentioned frameworks above. If you need a more sophisticated GWT application
framework you can use following frameworks:
- ArcBees GWT-Platform: Model-View-Presenter Framework for GWT
- JBoss Errai Framework
- Sencha GXT

The development is based on Maven so this project can be used with Eclipse, IntelliJ or NetBeans.

## Architecture

### Model for Services and Domains

There are two services: *UserService* and *PersonService* and two Entities: *Person* and *Address*. Following diagram shows the structure of the services and the domains.

![Service and Domain Model](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/demo-gwt-springboot-model.jpg)

### Architecture

Following diagram shows the architecture of the **Microservice Demo**.
The naming of the packages *client*, *mock*, *server*, *shared* and *resource* (not shown in diagram) is based on this architecture.

![Architecture](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/demo-gwt-springboot-architecture.jpg)

#### Client

All the GWT (UI and REST client) classes should be located in this package. GWT transpiles all the Java sources into JavaScript sources.

#### Mock

The package consists of the mock implementation of the REST services at the client side (GWT). Instead of calling the real REST services 
it will create the mock data. For this purpose you can use the *development-mock* profile of Maven. It will compile the mock package 
and uses the mock implementation to handle the services. If you want to call the real REST services you can use *development* profile 
and GWT transpiler will remove the mock part.

#### Shared

In this package you can put any classes which will be used from both sides: client and server. It is advisable to put *constants* and *endpoints* of the RESTful services so that they point to the same address. Also *DTO* (Data Transfer Objects) for RESTful services should be included in this package. GWT transpiles this package into JavaScript sources.

#### Server

All the *controller*, *service*, *repository* and *domain* classes - based on Spring Framework - should reside in this package. This package will __not be included__ in GWT transpiler.

#### Resource

All the themes for GWTBootstrap3 and general Bootstrap themes like Bootswatch should be located in this package. 

You can take a look the GWT [configuration file](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwt.gwt.xml) to see which packages will be included in GWT transpiler.

## Run the WebApp for Development

### Server: Start the WebApp with Spring Boot

Just run the class *DemoGwtSpringbootApplication* or if you are using Spring Tool Suite just run it with Spring Boot Dashboard:

![STS Spring Boot Dashboard](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-boot-dashboard.png)

#### Tips and Tricks

##### JRebel

- If you are using JRebel you need to put following parameter in VM Arguments, something like:

```java
-javaagent:C:\progjava\jrebel\jrebel.jar
```
or the newer version of JRebel

```java
-agentpath:C:\progjava\jrebel\lib\jrebel64.dll
```

![Spring Boot with JRebel parameter](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-boot-jrebel.png)

- You also have to comment out the Spring Boot Dev Tools dependency in pom.xml.

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

- To be able to generate the *rebel.xml* you need to compile the project with Maven profile *development*.

![Maven compile with Profile development](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/sts-maven-development-profile.png)

##### Spring Boot Dev Tools

Spring Boot Dev Tools restarts the Spring Boot App automatically if your codes have changed.
You have to deactivate JRebel if you want to use this tool. This Spring Boot Dev Tools dependency should be activated:

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

Starting GWT SuperDev Mode Compiler from command line or within the development environment with Maven:

```java
mvn -P development gwt:run-codeserver
```

At the end you can see following message:

```java
...
[INFO] The code server is ready at http://localhost:9876/
...
```

#### Bookmark *Dev Mode On*

Now you can go to the given address and boomark the *Dev Mode On* through *drag and drop* into your bookmark menu.

![GWT SuperDev Mode](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-bookmarks.png)

That's it. You can just push *Dev Mode On* to run the transpiler directly and the WebApp will be reloaded automatically. 

![GWT SuperDev Mode](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-compiler.png)

### Browser: Call the WebApp demo-gwt-springboot from a web browser

Go to the application URL with a web browser:

```java
http://localhost:9014/demogwt/index.html
```

### Heroku: Test the Webapp from Heroku

The webapp is installed at Heroku PaaS and you can test it from this address: https://demo-gwt-springboot.herokuapp.com/demogwt/demogwt.html

## Logging

The GWT logging is activated (see [configuration file](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwt.gwt.xml)) at both sides: Client and Server.

### Server: Logging at the Spring Boot Console

![GWT Server Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-logging-server.png)

### Client: Logging at the Browser Console

![GWT Client Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/gwt-logging-client.png)

## Debugging

### Server: Debugging Spring Boot

Debugging the Spring Boot part can be achieved easily by starting the Spring Boot with Debug mode.

### Client GWT: Debugging with GWT SuperDev Mode

You need to update following file: [configuration file for development](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwtDevelopment.gwt.xml)

```java
    <!-- Compiler agent - we only need to compile for one web browser in development -->
    <!-- If you want to use SDBG for debugging you need to use Chrome == safari -->
    <set-property name="user.agent" value="safari" />  
```
For all purposes of debugging you need to use Google Chrome as your browser.

### Client GWT: Debugging with Eclipse SDBG

Debugging the GWT part with Eclipse should be done by using [SDBG](https://sdbg.github.io/). 

**Tips and Tricks for Optimizing Transpiler Speed**

There are two GWT configuration files: [_DemoGwtDevelopment.gwt.xml_](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwtDevelopment.gwt.xml) and [_DemoGwt.gwt.xml_](https://github.com/lofidewanto/demo-gwt-springboot/blob/master/src/main/resources/com/lofidewanto/demo/DemoGwt.gwt.xml).
- _DemoGwtDevelopment.gwt.xml_: this config will be used to make the GWT compiling process faster. This only compiles for one web browser and use INFO as logging output.
- _DemoGwt.gwt.xml_: this config will be used for production transpilling. This is optimized for many many production purposes.


### Client GWT: Debugging with IntelliJ IDEA

For debugging gwt with IntelliJ IDEA proceed the following stets. 

#### Prequesites

- JetBrains IntelliJ 2016 Ultimate (Community doesn't support it)
- Chrome browser
- [JetBrains IDE Support Chrome Browser Plugin](https://chrome.google.com/webstore/detail/jetbrains-ide-support/hmhgeddbohgjknpmjagkdomcpobmllji)
- Enabled GWT Plugin in IntelliJ

#### Overview
The following diagram shows the different parts of the setup:

![GWT Client Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-debugging-setup-diagram.png)

#### Step by step

##### Open Project in IntelliJ

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-import-project.png)

After this the project is loaded and the `DemoGwtSpringbootApplication` will be added to the `RunConfigurations` automatically.

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-import-project.png)

##### Configure Web Facet

Open in the `FileMenu` the `Project Structure`

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-open-project-structure.png)

Add add under `Facets` a `Web Facet` to the project

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-add-web-facet.png)

Add the facet to the `demo-gwt-springboot` module:

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-choose-module.png)

The path must be set to `src/main/resources/public` and the context must be `/demogwt`.

**Important**

Do not add the web.xml to git. Just ignore it.

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-do-not-add-web.xml.png)

###### Do not generate Artifacts

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-configure-web-facet-add-contextpath.png) 

Close the `Project Structure` with `Ok` and reopen it. Now the `Web Facet` can be selected in the GWT Module.

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-configure-web-facet-2.png) 

After this you should select only the GWT Module `DemoGwtDevelopment`

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-configure-web-facet.png) 

##### GWT Configuration
Add a new Run Configuration

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-open-run-configuration.png) 

And a GWT Configuration:

![Open Project in IntelliJ](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-add-gwt-run-configuration.png) 

After this you start the "Spring Boot Project" first and after this the "GWT-Project" in Debug mode.

##### Codeserver

Now you have to repeat the steps to configure the code server (see above).


##### Running the debugger with the IDE Support Plugin 

You should see the alert that the »JetBrains IDE Support« is running in debug mode. 

If you have any trouble connecting the browser with the idea, please check the ports of the browser plugin and Intellij. 

Right click on the Life Edit extension and choose Options:

![GWT Client Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-debugging-session-is-running.png)

The default port is `63342`.

![GWT Client Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-ide-support-options.png)

And check if the port in the Intellij IDEA debugger is configured on the same port.

![GWT Client Logging](https://raw.github.com/lofidewanto/demo-gwt-springboot/master/src/main/docs/idea-ide-support-preferences-configure-port.png)

## Unit and Integration Testing

### Server: Spring Test

Examples of unit test with POJO and Mockito:
- [PersonImplTest.java](https://github.com/interseroh/demo-gwt-springboot/blob/master/src/test/java/com/lofidewanto/demo/server/domain/PersonImplTest.java)
- [PersonServiceImplTest.java](https://github.com/interseroh/demo-gwt-springboot/blob/master/src/test/java/com/lofidewanto/demo/server/service/person/PersonServiceImplTest.java)

Examples of integration test with Spring and in memory database:
- [PersonServiceImplIT.java](https://github.com/interseroh/demo-gwt-springboot/blob/master/src/test/java/com/lofidewanto/demo/server/service/person/PersonServiceImplIT.java)

### Client: GWT Mockito

We use GWT Mockito for writing the GWT user interface unit test. Following is an example of GWT Mockito unit test:
- [MainPanelViewTest.java](https://github.com/interseroh/demo-gwt-springboot/blob/master/src/test/java/com/lofidewanto/demo/client/ui/main/MainPanelViewTest.java)
- [PersonPanelViewTest.java](https://github.com/interseroh/demo-gwt-springboot/blob/master/src/test/java/com/lofidewanto/demo/client/ui/person/PersonPanelViewTest.java)

