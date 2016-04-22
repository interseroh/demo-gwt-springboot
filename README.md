# demo-gwt-springboot

This is an example Maven project for following frameworks:

- User Interfaces: 
  - GWT
  - GWTBootstrap3 for the UI
  - RestyGWT for the RESTful access to backend services
  - GIN for Dependency Injection
  - GWT Event Binder for events
  - GWT Mockito for UI logic test
- Services: 
  - KissMDA
  - Spring Boot for Service logic
  - All the standard stuffs used by Spring Framework
- Domain Models: 
  - KissMDA
  - JPA Hibernate
  
Starting GWT SuperDev Mode Compiler
mvn -P development process-classes gwt:run-codeserver

Application URL
http://localhost:9014/demogwt/demogwt.html