package cmpe202.umlparser;
 /**
  * Parser takes the input as per the given requirements
  * It depends on SeqDiagramGenerator.java to actually create Sequence Diagrams
  */

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

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

		compilationArray = getArray(sourceFilePath);
		createDiag(compilation)

	}

}