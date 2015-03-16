SWRLTab
=======

The SWRLTab is a [SWRLAPI](https://github.com/protegeproject/swrlapi/wiki)-based environment that provides a set of standalone graphical interfaces for managing SWRL rules. These interfaces are designed to be embeddable into third party Java applications.

Documentation can be found at the [SWRLTab Wiki](https://github.com/protegeproject/swrltab/wiki).

A [Protege Desktop Ontology Editor](http://protege.stanford.edu)-based [SWRLTab Plugin](https://github.com/protegeproject/swrltab-plugin/wiki) is also available.

#### Building Prerequisites

To build and run this plugin, you must have the following items installed:

+ Apache's [Maven](http://maven.apache.org/index.html).
+ A tool for checking out a [Git](http://git-scm.com/) repository.

#### Building

Get a copy of the latest code:

    git clone https://github.com/protegeproject/swrltab.git 

Change into the swrltab directory:

    cd swrltab

Build it with Maven:

    mvn clean install

On build completion, your local Maven repository will contain generated swrltab-${version}.jar and swrltab-${version}-jar-with-dependencies.jar files.
