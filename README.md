# README 

### What is Junder?

Junder helps you identify dependency problems in your code design.  For example, if you have a Java package design and you intend to prevent circular dependencies between those packages, the Junder might be the tool you are looking for.  

### How do I get set up?

Junder is a visualisation tool.  In order to work with it, you have to create a property file (text).  You give it only your jars, not the jars of third parties.  This means the analysis is done on your code only, not on all the jars of the application.  Instead of jars you can also give it class files.

The name of the property in the input file 'inputPaths'. Separate entries using a comma.  Here is an example of the context of a file name `my-junder.properties`
~~~
inputPaths=lib/some-jar.jar,lib/other-jar.jar,~/my-app/classes
~~~

Using this name you can now do:
~~~
java -jar junder.jar my-junder.properties 
~~~

More information on what you are looking at can be obtained from the [User Guide](https://bitbucket.org/codespear/junder/wiki/User_Guide).
