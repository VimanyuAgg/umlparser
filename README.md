# umlparser
Personal Project for CMPE202

## Associated Waffle Board link: 
http://waffle.io/VimanyuAgg/umlparser

## Description:
This project creats a Parser which converts Java Source Code into a UML Class Diagram as well as Sequence Diagram.

## Project - Pre-requisites:
All Java source files to be parsed should be in the "default" package. There should be no subdirectories.

## Requirements:
* The parser must be executable on the command line with the following format:
```
   umlparser <source folder> <output file name>
** <source folder> is a folder name where all the .java source files will be
** <output file name> is the name of the output image file you program will generate ( .jpg, .png or .pdf format)
```
* All the Java source files will be in the "default" package.  That is, there will be no sub-directories in the source folder.

## Project scope:
**Static and Abstract Notation**:  Static and Abstract notation in UML are usually denoted as "underline" and "italic", but rarely used in practice. Thus, parsing this is not included in the scope of this project

**Relationships Between Interfaces**:  Although conceptually possible in UML, relationships between Interfaces (i.e. inheritance, dependencies) are rarely thought of in practice and generally bad practice.  As such, this project does not expect parser to detect these situations.  

***Only Public Methods***: (private, package and protected scope methods are ignored in the Class diagram)



