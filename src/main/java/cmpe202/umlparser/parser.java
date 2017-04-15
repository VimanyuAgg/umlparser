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

	String sourceFilePath; //Holds the path of the source file
	String destFilePath;   //Holds the path of the destination file
	String yumlParse;
	HashMap<String, Boolean> m;

	ArrayList<CompilationUnit> compilationArray;

	parser(String sourcePath, String destPath){
		this.sourceFilePath = sourcePath;
		this.destFilePath = sourceFilePath + "\\" + destPath + ".jpeg";

	}

	public void startParsing() throws Exception{

		//Dummy method created

		compilationArray= getArray(sourceFilePath);
        createMap(compilationArray);
        for (CompilationUnit c : compilationArray)
            yumlParse += parsingHandler(c);


        System.out.println("Unique Code: " + yumlParse);


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

	 private void createMap(ArrayList<CompilationUnit> cu){
		 for (CompilationUnit c : cu) {
	            List<TypeDeclaration<?>> cl = c.getTypes();
	            for (Node n : cl) {
	                ClassOrInterfaceDeclaration classInterfaceDeclaration = (ClassOrInterfaceDeclaration) n;
	                m.put(classInterfaceDeclaration.getName().toString(), classInterfaceDeclaration.isInterface());

	            }
	        }

	 }

	 private String parsingHandler(CompilationUnit c){
		 String result="";

		 //Dummy method
		 return result;
	 }


}




