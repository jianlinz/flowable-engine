Flowable (V6)
=============

[Maven Central:  
    ![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.flowable/flowable-engine/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.flowable/flowable-engine)

[Travis CI:  
	![build status badge](https://travis-ci.org/flowable/flowable-engine.svg?branch=master)](https://travis-ci.org/flowable/flowable-engine)

[License:  
	![license](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/flowable/flowable-engine/blob/master/LICENSE)


Homepage: http://flowable.org/

## flowable / flowəb(ə)l /
* a compact and highly efficient workflow and Business Process Management (BPM) platform for developers, system admins and business users.
* a lightning fast, tried and tested BPMN 2 process engine written in Java.  It is Apache 2.0 licensed open source, with a committed community.
* can run embedded in a Java application, or as a service on a server, a cluster, and in the cloud.  It integrates perfectly with Spring.  With a rich Java and REST API, it is the ideal engine for orchestrating human or system activities.

## Introduction

### License

Flowable is distributed under the Apache V2 license (http://www.apache.org/licenses/LICENSE-2.0.html).

### Download

The Flowable downloads can be found on http://flowable.org/downloads.html.

### Sources

The distribution contains most of the sources as jar files. The source code of Flowable can be found on https://github.com/flowable/flowable-engine.

### JDK 7+

Flowable runs on a JDK higher than or equal to version 7. Go to http://www.oracle.com/technetwork/java/javase/downloads/index.html and click on button "Download JDK".  There are installation instructions on that page as well. To verify that your installation was successful, run "java -version" on the command line.  That should print the installed version of your JDK.

### Contributing

Contributing to Flowable: https://github.com/flowable/flowable-engine/wiki.

### Reporting problems

Every self-respecting developer should have read this link on how to ask smart questions: http://www.catb.org/~esr/faqs/smart-questions.html.

After you've done that you can post questions and comments on http://forum.flowable.org and create issues in https://github.com/flowable/flowable-engine/issues.

### QA server

There's a Jenkins server running the Flowable unit tests on http://qa.flowable.org.


Proper Soft Branch
==================

Pack custom version Flowable
----------------------------

### How to pack

`mvn -DskipTests clean package deploy -P proper`

Pack web designer in jar
------------------------

Use `Gradle` build system to pack Flowable front-end static resources from `flowable-ui-modeler-app` module into `flowable-web-designer-xxx.jar` as `Flowable Web Designer`.

Static resources are packed under `META-INF/resources/` in the jar, which could be used as a dependency of the main web app.

### How to pack

    # get a clean package
    $ ./gradlew clean pack
    # and then upload jar
    $ ./gradlew upload

and you could find the `flowable-web-designer-xxx.jar` at `modules/flowable-ui-modeler/flowable-ui-modeler-app/build/libs`

Current version
---------------

Now this branch has merged [flowable-6.2.1](https://github.com/flowable/flowable-engine/tree/flowable-6.2.1) tag, and you can find diff [here](https://github.com/flowable/flowable-engine/compare/flowable-6.2.1...propersoft-cn:proper-6.2.1)
