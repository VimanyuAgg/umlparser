# umlparser
Personal Project for CMPE202

## Associated Waffle Board link:
http://waffle.io/VimanyuAgg/umlparser

## Description:
This project creats a Parser which converts Java Source Code into a UML Class Diagram as well as Sequence Diagram.

## Project - Pre-requisites:
** All Java source files to be parsed should be in the "default" package.
** No subdirectories should be present inside the default package.


## Requirements:
* The parser must be executable on the command line with the following format:
```
   umlparser <source folder> <output file name>
** <source folder> is a folder name where all the .java source files will be
** <output file name> is the name of the output image file you program will generate ( .jpg, .png or .pdf format)
```
* All the Java source files will be in the "default" package.  That is, there will be no sub-directories in the source folder.

## Instructions for umlparser (Sequence Diagrams)
In order to generate sequence diagram add the < source folder location in quotes> < output file name> for sequence diagram in the argument.
So that the entire query with class diagram and sequence diagram becomes:

```
umlparser <source folder location in quotes--for class diagram> <output file name--for class diagram> <source folder location in quotes--for sequence diagram> <output file name--for sequence diagram>
```
Please note that
* source folder for sequence diagram should contain a Main.java with test sequence being in a main static method
* output file will be a PNG and user does not need to add .png in < output file name>
* source folder should contain java files to be parsed (with no subdirectories)


## Project scope:
**Static and Abstract Notation**:  Static and Abstract notation in UML are usually denoted as "underline" and "italic", but rarely used in practice. Thus, parsing this is not included in the scope of this project

**Relationships Between Interfaces**:  Although conceptually possible in UML, relationships between Interfaces (i.e. inheritance, dependencies) are rarely thought of in practice and generally bad practice.  As such, this parser does not include detection of these situations.

***Only Public Methods***: (private, package and protected scope methods are ignored in the Class diagram)



