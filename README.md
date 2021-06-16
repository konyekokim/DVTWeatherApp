
# DVT Weather Application :package:

The purpose of this repo to demonstrate the use of a best practices to create a **Weather application** to display the current weather at the user’s location and a 5-day forecast.

Some of the practices used in this project include:

* App Modularization with **Kotlin DSL** and **Dagger**
* MVVM Design Pattern
* Unit Testing
* SOLID Principles
* Integration of **CircleCI** pipeline
* Code Coverage Integration: **Jacoco**
* Static code Analysis: **Detekt**

# Minimum requirements
* Gradle version: v6.x
* Android Studio: v4.0

# Libraries/APIs used in this project
* Retrofit
* GSON
* OkHttp
* Navigation
* Lifecycle
* Dagger
* Coroutines
* Room
* Google Material
* Jacoco
* Detekt
* Spotless
* Test dependencies: junit, mockk, rules

# Why Jacoco?
Jacoco is a tool to measure code coverage and currently the most widely used.

> Code coverage in simple terms means how much of the production code that you wrote is being executed at runtime when a particular test suite runs.

Here is the gradle task that needs to be run for Jacoco Report.
```
./gradlew jacocoTestReport
```
# Why Detekt?
Detekt is a static code analysis tool for the Kotlin programming language. It operates on the abstract syntax tree provided by the Kotlin compiler.

> Static code analysis  is a method of debugging by examining source code before a program is run. 
It's done by analyzing a set of code against a set (or multiple sets) of coding rules

Here is the gradle task that needs to be run for Detekt analysis
```
./gradlew detektCheck
```




