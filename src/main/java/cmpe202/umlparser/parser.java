package cmpe202.umlparser;
 /**
  * Parser takes the input as per the given requirements
  * It depends on SeqDiagramGenerator.java to actually create Sequence Diagrams
  */

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

public class parser{
	
	String sourceFilePath;
	String destFilePath;
	ArrayList<CompilationUnit> compilationArray;
	
	parser(String sourcePath, String destPath){
		this.sourceFilePath = sourcePath;
		this.destFilePath = sourceFilePath + "\\" + destPath + ".jpeg";
		
	}
	
	public void startParsing() throws Exception{
		
		//Dummy method created
		
		compilationArray= getArray(sourceFilePath);	
		
	}
	
	 private ArrayList<CompilationUnit> getArray(String inPath)
	            throws Throwable {
		 
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
	
}