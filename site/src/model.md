---
title: Junder Model module
layout: page.njk
---

Junder model converts Java class files into a list of [Prolog facts](https://en.wikipedia.org/wiki/Prolog).  The idea is that one could then use these facts to reason about the Java classes.

## How does it work?

The input is a [Java properties file](https://docs.oracle.com/javase/tutorial/essential/environment/properties.html) with these entries:
  - *outputFilename*: The name of the file to which the facts must be written.  
  - *inputPaths*: A ',' separated list of paths to search.  Each path is searched recursively for *jar* and *class* files. A path entry can also point to a specific '.jar' or '.class' file. Note that path separator should be '/'.

Command to run the application:
```JunderMain <propertiesFile>```

## What libraries did we use?
  - For parsing the class files: [Apache Commons BCEL](http://commons.apache.org/proper/commons-bcel/)
  - For writing unit tests: [JUnit](http://junit.org/junit4/)

## Interesting Java features used:
 - [Optional values](http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html)
 - [not null checks](https://dzone.com/articles/checking-null-values-java)
 - [Java Stream](http://blog.hartveld.com/2013/03/jdk-8-33-stream-api.html)
 - [convert arrays to streams](http://stackoverflow.com/questions/24390463/java-8-stream-and-operation-on-arrays)
 - [Java lambda expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
 
## Questions 
  - Why use streams?
    - [David Hartveld](http://blog.hartveld.com/2013/03/jdk-8-33-stream-api.html) says it brings a lot of expressiveness, but there are shotcomings when compared to other languages. 
    - The fact that streams cannot be reuse is a bit sad.
    - There are [subtle mistakes](https://blog.jooq.org/2014/06/13/java-8-friday-10-subtle-mistakes-when-using-the-streams-api/) to look out for while using streams
    - [Angelika Langer](https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html) says streams are slower.
