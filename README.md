SWRLTab
=======

[![Build Status](https://travis-ci.org/protegeproject/swrltab.svg?branch=master)](https://travis-ci.org/protegeproject/swrltab)

The SWRLTab is a [SWRLAPI](https://github.com/protegeproject/swrlapi/wiki)-based environment that provides a set of standalone graphical interfaces for managing SWRL rules and SQWRL queries. 

Documentation can be found at the [SWRLTab Wiki](https://github.com/protegeproject/swrltab/wiki).

A [Protégé Desktop Ontology Editor](http://protege.stanford.edu)-based [SWRLTab Plugin](https://github.com/protegeproject/swrltab-plugin/wiki) is also available.

### Downloading

You can get a copy of the latests JAR from the [project's GitHub Release area](https://github.com/protegeproject/swrltab/releases).

This JAR will have a name of the form:

    swrltab-${version}-jar-with-dependencies.jar
 
### Running

To execute this JAR you must have [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed.

The SWRLTab can be started as follows:

    java -cp swrltab-${version}-jar-with-dependencies.jar org.swrltab.ui.SWRLTab 

The SQWRLTab can be started as follows:

    java -cp swrltab-${version}-jar-with-dependencies.jar org.swrltab.ui.SQWRLTab 

### Building

To build and run this project you must have the following items installed:

+ [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ A tool for checking out a [Git](http://git-scm.com/) repository
+ Apache's [Maven](http://maven.apache.org/index.html)

Get a copy of the latest code:

    git clone https://github.com/protegeproject/swrltab.git 

Change into the swrltab directory:

    cd swrltab

Build it with Maven:

    mvn clean install

On build completion, your local Maven repository will contain generated swrltab-${version}.jar and swrltab-${version}-jar-with-dependencies.jar files.
The ./target directory will also contain these JARs.

You can then run the standalone SQWRLTab as follows:

    mvn exec:java

You can run the standalone SWRLTab as follows:

    java -cp ./target/swrltab-${version}-jar-with-dependencies.jar org.swrltab.ui.SWRLTab 

#### License

The software is licensed under the [BSD 2-clause License](https://github.com/protegeproject/swrltab/blob/master/license.txt).

#### Questions

If you have questions about this library, please go to the main
Protégé website and subscribe to the [Protégé Developer Support
mailing list](http://protege.stanford.edu/support.php#mailingListSupport).
After subscribing, send messages to protege-dev at lists.stanford.edu.
