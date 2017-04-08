package cmpe202.umlparser;

/*
 * This contains implementation of sequence diagram generator
 */

import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

class SeqDiagramGenerator{

    String sourceFilePath; //Holds the path of the source file
    String destFilePath;   //Holds the path of the destination file
    String className;      //Holds the class Name of the source file
    String methodName;     //Holds the method Name of the source file

    SeqDiagramGenerator(String sourceFilePath, String destFilePath, String className, String methodName){
        this.sourceFilePath = sourceFilePath;
        this.destFilePath = sourceFilePath + "\\" + destFilePath + ".jpeg";
        this.className = className;
        this.methodName = methodName;

    }

    public void startParsing() throws Exception{

        //Dummy method created



    }

}