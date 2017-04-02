package cmpe202.umlparser;
 /**
  * Parser takes the input as per the given requirements
  * It depends on SeqDiagramGenerator.java to actually create Sequence Diagrams
  */

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class parser{

	String sourceFilePath;
	String destFilePath;
	String className;
	String methodName;

	ArrayList<CompilationUnit> compilationArray;

	parser(String sourcePath, String destPath, String className, String methodName){
		this.sourceFilePath = sourcePath;
		this.destFilePath = sourceFilePath + "\\" + destPath + ".jpeg";
		this.className = className;
		this.methodName = methodName;
	}

	public void startParsing() throws Exception{

		//Dummy method created

		compilationArray= getArray(sourceFilePath);

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
	 }

