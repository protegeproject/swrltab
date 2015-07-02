SWRLTab
=======

The SWRLTab is a [SWRLAPI](https://github.com/protegeproject/swrlapi/wiki)-based environment that provides a set of standalone graphical interfaces for managing SWRL rules and SQWRL queries. 

Documentation can be found at the [SWRLTab Wiki](https://github.com/protegeproject/swrltab/wiki).

A [Protégé Desktop Ontology Editor](http://protege.stanford.edu)-based [SWRLTab Plugin](https://github.com/protegeproject/swrltab-plugin/wiki) is also available.

### Building Prerequisites

To build and run this project you must have the following items installed:

+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ Apache's [Maven](http://maven.apache.org/index.html).

### Building

Get a copy of the latest code:

    git clone https://github.com/protegeproject/swrltab.git 

Change into the swrltab directory:

    cd swrltab

Build it with Maven:

    mvn clean install

On build completion, your local Maven repository will contain generated swrltab-${version}.jar and swrltab-${version}-jar-with-dependencies.jar files.
The ./target directory will also contain these JARs.

### Running

You can run the standalone SQWRLTab as follows:

    mvn exec:java

You can run the standalone SWRLTab as follows:

    java -cp swrltab-${version}-jar-with-dependencies.jar org.swrltab.ui.SWRLTab 


