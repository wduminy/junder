---
title: Junder User Guide
layout: page.njk
---

## Overview

Junder is a static code analysis tool that works on JVM class files. 

You give it a list of folders or a list of jars that you would like to analyse. The idea is that you give it only your jars, not the jars of third parties.  This means the analysis is done on your code only, not the entire application.

Most likely you would use the **classes folder** that is used by your build  or development tool as input. In such a set-up, you would run Junder after an Eclipse build. 

You give the input in a [properties file](https://en.wikipedia.org/wiki/.properties).  The name of the property is `inputPaths`.  The file itself can contain other properties as well; so you can add an entry to an existing properties file if you like.

## A quick tutorial

For this quick tutorial, we run Junder using itself as input.  So, you would have to clone the [repository](https://github.com/wduminy/junder). 

```
git clone https://github.com/wduminy/junder
cd junder
```

Now we compile the source to get some `.class` files for Junder to use.

```
mvn compile
```

This command builds class files, but also an unstable version of Junder.  For the tutorial we will use the unstable version, but for your is may be better to get the latest stable version of Junder from [downloads](https://bitbucket.org/codespear/junder/downloads/).

Now you create a properties file for junder to read.  The Junder source code already has such a file, it is named `junder.properties`.  It contains something like this:
```
inputPaths=junder.model/target/classes,junder.swing/target/classes
```

This properties file contains a comma separated list of entries.  If the entry is a path, the path is searched recursively for `.jar` and `.class` files. An entry can also point to a specific '.jar' or '.class' file.  An entry must use forward slashes ('/') to construct a path.

Now you are ready to run Junder:

```
 java -jar /path-to-your-jar/junder-0-3.jar junder.properties
```

What you will see is a view with a matrix on the left hand side and an info panel on the right.  Something like this:

![MainPanel](/images/snap_main.png)

If you click on a coloured cell,  you see some details of the association between the row and column package in the info panel.

The matrix shows the number of times the row package uses the column package.  The info panel shows which classes from these packages are involved in the usage.

The primary use case of the application is to identify the usages that cause dependency loops.  Dependencies that appear above the diagonal line in the matrix participate in loops.  If there are no coloured block above the line, then there are no dependencies.   

## Using the sub-matrix

The sub-matrix view idea is handy when you have a large number of packages and it is difficult to see what is going on.  A sub-matrix eliminates all the packages from the view that is not connected (directly or indirectly) to the package for which the sub-matrix is created.  You can zoom in to create a sub-matrix from a sub-matrix, until you have the view you are looking for.

The sub-matrix is for a specific java package. The cell has a row package and a column package.  Choose the appropriate action under the `View` menu to create a sub-matrix from the selected cell.  The `Back to parent matrix` action zooms you back out.

A good reason to use the sub-matrix view is when you want to zone into the packages that participate in a cycle.  To do this you would select a coloured cell above the diagonal and create a sub-matrix from it.

## That's all folks

Propose your ideas and log bugs on the [issue list](https://github.com/wduminy/junder/issues).