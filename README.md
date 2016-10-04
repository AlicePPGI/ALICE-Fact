# ALICE-Fact
Fact Revision Repository

This application aims to correct the alignment of ontologies expressed by first-order logic (FOL). Ontology alignment is a set of matching corresponding concepts between the involved ontologies. The here available application is able to do the revision of equivalences, where the concepts involved in the match are equals.

The application was developed in Java and applies to ontology networks compatible with the SWI Prolog, which must be installed on the computer. To use it follow these steps:

1) Import the repository for eclipse as general project.

2) Transform the project imported into a Java Project.

3) Add the jar jpl.jar to the project as an external jar.

4) Set the SWI_HOME_DIR environment variable to the root directory of SWI Prolog.

5) Add the directories %SWI_HOME_DIR%\bin and %JAVA_HOME%\jdk...\jre\bin\ server to the system PATH.

6) Add the directory %SWI_HOME_DIR%\lib to your CLASSPATH.
