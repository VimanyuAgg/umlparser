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

    ArrayList<CompilationUnit> compilationArray;
    String plantUml;

    SeqDiagramGenerator(String sourceFilePath, String destFilePath, String className, String methodName){
        this.sourceFilePath = sourceFilePath;
        this.destFilePath = sourceFilePath + "\\" + destFilePath + ".jpeg";
        this.className = className;
        this.methodName = methodName;

    }

    public void startParsing() throws Exception{

        //Dummy method created
        compilationArray = getArray(sourceFilePath);
            createMappings();
            plantUml += "actor user #black\n";
            plantUml += "user" + " -> " + className + " : " + methodName + "\n";

            parsingHandler(methodName);
            plantUml += "@enduml";
            //generateDiagram(plantUml);
            System.out.println("Plant UML Code:\n" + plantUml);



    }

    private ArrayList<CompilationUnit> getArray(String inPath)
            throws Exception {

     File folder = new File(inPath);
        ArrayList<CompilationUnit> compilationArray = new ArrayList<CompilationUnit>();
        for (final File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                FileInputStream inStream = new FileInputStream(file);
                CompilationUnit cu;
                try {
                    cu = JavaParser.parse(inStream);
                    compilationArray.add(cu);
                } finally {
                    inStream.close();
                }
            }
        }
        return compilationArray;
    }

    private void createMappings(){
        //Dummy method
    }

}