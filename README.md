# JavaAnalyserCore

This project (currently) provides an exploration of how to use the Java
parsing system [JavaParser](https://javaparser.org).

Instructions for running the code can be found in the project compendium report.

The template code in this project was provided by supervisor Ewan Tempero. I (Daniel Cutfield) built upon these to provide the functionality
that is most useful to the project. The files are located in the
`com.qualitascorpus.javaanalyser.demo` package (under
`JavaAnalyserCore/src/main/java`). They demonstrate slightly different things at different levels of complexity. 
To understand JavaParser, the most useful order to look at them is described in the [package](JavaAnalyserCore/src/main/java/com/qualitascorpus/javaanalyser/demo/package.html) file.
The main file for my project is JavaParserSimpleDir.java (File Provided by Ewan Tempero, Edited by Daniel Cutfield). 
Other files that are used in this project are
- Analyser.java (Provided by Ewan Tempero, slightly edited by Daniel Cutfield)
- Utility.java (Provided by Ewan Tempero)
- CommentDetails.java (Written by Daniel Cutfield)
- Comments.java (Written by Daniel Cutfield)
- CommentType.java (Written by Daniel Cutfield)
- CompilationUnitDetails.java (Provided by Ewan Tempero)
- SATDClassification.java (Written by Daniel Cutfield)
- All other files were provided by Ewan Tempero

### Project Contents

* `JavaAnalyserCore` - the source for a basic use of JavaParser, including demo programs
* `README.md` - this file (markdown)
* `kalah_designs` - provides some more realistic (but still small) code bases used by one of the demo programs (more details below)
* `lib` - contains the `jar` files needed for JavaParser (see **Download JavaParser `jar` files** below)

### 'kalah_designs`

The contents are:

* `build-kalah.xml` - ant script for compiling and executing tests on designs (not needed but provided for completeness)
* `design1001` - kalah design 
* `design1019` - kalah design 
* `design1020` - kalah design 
* `design1025` - kalah design 
* `design1036` - kalah design 
* `design1042` - kalah design 
* `lib` - `jar` files needed for compiling and executing tests. `kalah20200712.jar` will be needed to analyse the designs (see the `JavaParserDesign` demo). `junit-3.8.2.jar` is provided only for completeness

#### Design details

Each design directory contains the source code for the design, plus some measurements for various metrics for each design (in `METADATA`). Provided for completeness.

### JavaParser Documentation

The documentation is accessible from the [JavaParser](https://javaparser.org) website, but here are some useful shortcuts.

* [javaparser-core API JavaDoc](https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/index.html) - Note that this is for the latest version of JavaDoc and so may not match the particular version of JavaParser you have installed
* [javaparser-symbol-solver-core API JavaDoc](https://www.javadoc.io/doc/com.github.javaparser/javaparser-symbol-solver-core/latest/index.html)
* [All JavaParser JavaDocs](https://www.javadoc.io/doc/com.github.javaparser)

### Download JavaParser `jar` files

The `jar` files used by JavaParser are included in the project (in `lib`) but this documents how they got there.

It seems the only way to get the libraries is to use Maven. This project doesn't really use Maven but it includes a `pom.xml` file that provides a way to get the libraries. It is somewhat of a hack, but does the job.

This is what you need to do.

1. Install Maven if you don't have it already
1. From the command line do the following in the directory that contains the file `pom.xml`

    ```mvn package```

1. This will cause Maven to download all of the "dependencies" that are specified in the `pom.xml` file. This includes those needed for JavaParser. The results are stored by Maven in a directory it uses. The default is `.m2` in your home directory.
JavaParser depends on other projects, but Maven will handled downloading them. The full list of the needed `jar` files is:

    * `.m2/repository/com/github/javaparser/javaparser-symbol-solver-core/3.18.0/javaparser-symbol-solver-core-3.18.0.jar`
    * `.m2/repository/com/github/javaparser/javaparser-core/3.18.0/javaparser-core-3.18.0.jar`
    * `.m2/repository/com/google/guava/guava/30.0-jre/guava-30.0-jre.jar`
    * `.m2/repository/com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar`
    * `.m2/repository/com/google/guava/failureaccess/1.0.1/failureaccess-1.0.1.jar`
    * `.m2/repository/org/javassist/javassist/3.27.0-GA/javassist-3.27.0-GA.jar`


### Build JavaAnalyserCore

1. You will need to add JUnit to your project (most IDEs provide a simple way to do this).

1. You will need to add the `jar` files listed to to your classpath. You may want to first copy them from then `.m2` location to somewhere more convenient.

1. Most IDEs will support importing Maven projects. That saves having to specify the `jar` files. It might work to just import the JavaAnalyserCore project, but this hasn't been tested (feel free to use the provided `pom.xml` file as a starting point your own project if you intend to use Maven.

