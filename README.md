# emmanage-api-war
Java web module with REST services built using Jersey/Jackson/Swagger/Spring

## Overview

A boilerplate of a Java web application module for building REST apis. It 
utilizes the following technologies

* Jersey JAX-RS
* Jackson serialization
* Swagger annotation, and automatically generated and Swagger UI of the REST APIs
* Spring for DI with Prgramatic Configuration


## Prerequisites
Maven3 is required is build the application. It cannot be built with Maven2!

## Key concepts
1. Application Initialization
1. Package layout
1. REST APIs
1. REST API Documentation
1. Swagger Integration
   * Runtime swagger generation
   * Swagger UI
1. WADL Generation

## Quick Start
Start web application
```
mvn3 -Dem.baseDirectory=c:\cst\em\product jetty:run
```
Open [http://localhost:8090/emmanage/api/v1](http://localhost:8090/emmanage/api/v1)

Create war file suitable for app server deployment
```
mvn3 clean package
```
see `target/*.war`
