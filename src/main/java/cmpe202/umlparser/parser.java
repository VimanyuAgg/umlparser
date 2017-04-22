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
		 String res = "";
	        String class = "";
	        String classNameShort = "";
	        String methods = "";
	        String fields = "";
	        String additions = ",";

	        ArrayList<String> makeFieldPublic = new ArrayList<String>();
	        List<TypeDeclaration> ltd = cu.getTypes();
	        Node node = ltd.get(0); // assuming no nested classes

	        // Get class
	        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
	        if (coi.isInterface()) {
	            class = "[" + "<<interface>>;";
	        } else {
	            class = "[";
	        }
	        class += coi.getName();
	        classNameShort = coi.getName();

	         // Parse Methods
        boolean nextParam = false;
        for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
            // Getting Methods
            if (bd instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
                if (cd.getDeclarationAsString().startsWith("public")
                        && !coi.isInterface()) {
                    if (nextParam)
                        methods += ";";
                    methods += "+ " + cd.getName() + "(";
                    for (Object gcn : cd.getChildrenNodes()) {
                        if (gcn instanceof Parameter) {
                            Parameter paramCast = (Parameter) gcn;
                            String paramClass = paramCast.getType().toString();
                            String paramName = paramCast.getChildrenNodes()
                                    .get(0).toString();
                            methods += paramName + " : " + paramClass;
                            if (map.containsKey(paramClass)
                                    && !map.get(classShortName)) {
                                additions += "[" + classShortName
                                        + "] uses -.->";
                                if (map.get(paramClass))
                                    additions += "[<<interface>>;" + paramClass
                                            + "]";
                                else
                                    additions += "[" + paramClass + "]";
                            }
                            additions += ",";
                        }
                    }
                    methods += ")";
                    nextParam = true;
                }
            }
        }
		 return result;
	 }

	 private String modifyBrackets(String foo) {
	        foo = foo.replace("[", "(");
	        foo = foo.replace("]", ")");
	        foo = foo.replace("<", "(");
	        foo = foo.replace(">", ")");
	        return foo;
	    }

	   private String parseSumOperation() {
	        String result = "";
	        Set<String> keys = mapClassConn.keySet();
	        for (String i : keys) {
	            String[] classes = i.split("-");
	            if (m.get(classes[0]))
	                result += "[<<interface>>;" + classes[0] + "]";
	            else
	                result += "[" + classes[0] + "]";
	            result += mapClassConn.get(i); // Add connection
	            if (m.get(classes[1]))
	                result += "[<<interface>>;" + classes[1] + "]";
	            else
	                result += "[" + classes[1] + "]";
	            result += ",";
	        }
	        return result;
	    }

	     private String makeyumlCodeUnique(String blob) {
	        String[] codeSplitter = blob.split(",");
	        String[] uniqueCodeLines = new LinkedHashSet<String>(
	                Arrays.asList(codeSplitter)).toArray(new String[0]);
	        String result = String.join(",", uniqueCodeLines);
	        return result;
	    }
}




