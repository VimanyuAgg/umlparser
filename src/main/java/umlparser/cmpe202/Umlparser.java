package umlparser.cmpe202;

import java.util.*;
import java.lang.*;
import java.io.*;

import com.github.javaparser.ast.*;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.*;
import com.github.javaparser.ast.body.*;

public class Umlparser {
	private String yumlSyntax = "";
	private HashMap<String, Boolean> classMap= new HashMap<String, Boolean>();
	private HashMap<String, String> classTypeMap = new HashMap<String, String>();
	private String YUML_INTERFACE = "<<interface>>;";
    
	private ArrayList<CompilationUnit> allcompilationUnitList;
	private String sourceFilePath;
	private String destFilePath;
	
    Umlparser(String sourceFilePath, String destFilePath) {
    	this.destFilePath = sourceFilePath + "/" + destFilePath + ".png";
    	this.sourceFilePath = sourceFilePath;
    }

    public void startParsing() throws Exception {
        allcompilationUnitList = getAllcompilationUnitList(sourceFilePath);
        createMappings(allcompilationUnitList);
        
        for (int i = 0; i<allcompilationUnitList.size();i++){
        	
        	yumlSyntax += parseAST(allcompilationUnitList.get(i));
        }
            
        yumlSyntax += parseClassConnections();
        yumlSyntax = makeYumlCodeUnique(yumlSyntax);
        System.out.println("Generating PNG...");
        DrawDiagram.drawImage(yumlSyntax, destFilePath);
        System.out.println("Created: "+destFilePath);
        System.out.println("Intermediate yuml Code: " + yumlSyntax);
    }
    
    private void createMappings(ArrayList<CompilationUnit> compilationUnitList) {
        for (CompilationUnit compilationUnit : compilationUnitList) {
            List<TypeDeclaration> compilationTypeList = compilationUnit.getTypes();
            for (Node node : compilationTypeList) {
                ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) node;
                classMap.put(classOrInterface.getName(), classOrInterface.isInterface());
                
            }
        }
    }

    private ArrayList<CompilationUnit> getAllcompilationUnitList(String sourceFilePath)throws Exception {
    	ArrayList<CompilationUnit> getcompilationUnitList = new ArrayList<CompilationUnit>();
    	File currentFolder = new File(sourceFilePath);        
        
        for (File file : currentFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".java")) {
            	
                FileInputStream inputStream = new FileInputStream(file);
                CompilationUnit compilationUnit;
                try {
                    compilationUnit = JavaParser.parse(inputStream);
                    getcompilationUnitList.add(compilationUnit);
                } finally {
                    inputStream.close();
                }
            }
        }
        return getcompilationUnitList;
    }
    

    private String parseClassConnections() {
        String result = "";
        Set<String> allKeys = classTypeMap.keySet(); // get all keys
        Iterator<String> iter = allKeys.iterator();
        while (iter.hasNext()) {
        	String current = iter.next();
            
        	String[] classArray = current.split("-");
            if (classMap.get(classArray[0])){
                result += "["+YUML_INTERFACE + classArray[0] + "]";
            }
            else{
                result += "[" + classArray[0] + "]";
            }
            
            result += classTypeMap.get(current); 
            if (classMap.get(classArray[1])){
                result += "["+YUML_INTERFACE + classArray[1] + "]";
            }
            else{
                result += "[" + classArray[1] + "]";
            }
            result += ",";
        }
        return result;
    }
    
    private String[] parseConstructorDeclaration(Node node,ClassOrInterfaceDeclaration classOrInterface, boolean hasNextParam,String yumlSyntaxMethods,
    											 String yumlSyntaxClassRelations, String className){
    	
        for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) node).getMembers()) {         // Parsing Methods
           
        	if (bodyDeclaration instanceof ConstructorDeclaration) {
                ConstructorDeclaration constructorDeclaration = ((ConstructorDeclaration) bodyDeclaration);
                
                if (constructorDeclaration.getDeclarationAsString().startsWith("public")&& !classOrInterface.isInterface()) {
                    if (hasNextParam){
                        yumlSyntaxMethods += ";";
                    }
                    
                    yumlSyntaxMethods += "+ " + constructorDeclaration.getName() + "(";
                    
                    for (Object childrenNodes : constructorDeclaration.getChildrenNodes()) {
                        if (childrenNodes instanceof Parameter) {
                            Parameter parameter = (Parameter) childrenNodes;
                            String parameterClass = parameter.getType().toString();
                            String parameterName = parameter.getChildrenNodes().get(0).toString();
                            //Creating yuml syntax for methods
                            yumlSyntaxMethods += parameterName + " : " + parameterClass;
                            if (classMap.containsKey(parameterClass)&& !classMap.get(className)) {
                                yumlSyntaxClassRelations += "[" + className + "] uses -.->";
                                if (classMap.get(parameterClass))
                                    yumlSyntaxClassRelations += "["+YUML_INTERFACE + parameterClass + "]";
                                else
                                    yumlSyntaxClassRelations += "[" + parameterClass + "]";
                            }
                            yumlSyntaxClassRelations += ",";
                        }
                    }
                    yumlSyntaxMethods += ")";
                    hasNextParam = true;
                }
            }
        }
        String hasNextParamString;
        if(hasNextParam){
        	hasNextParamString ="true";
        }
        else{
        	hasNextParamString ="false";
        }
        
        String[] constructorDeclarationResult = new String[3];
        constructorDeclarationResult[0] = hasNextParamString;
        constructorDeclarationResult[1] = yumlSyntaxMethods;
        constructorDeclarationResult[2] = yumlSyntaxClassRelations;
        
        return constructorDeclarationResult;
    }
    
    private ArrayList<ArrayList<String>> parseMethodDeclaration(Node node,ClassOrInterfaceDeclaration classOrInterface, ArrayList<String> publicMethodNames,
    										String yumlSyntaxMethods, String yumlSyntaxClassRelations, String className, boolean hasNextParam){
    	ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    	for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) node).getMembers()) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = ((MethodDeclaration) bodyDeclaration);
                
                if (methodDeclaration.getDeclarationAsString().startsWith("public") && !classOrInterface.isInterface()) { // Getting only public methods
                	//publicMethodNames;
                	
                	// Setters and Getters
                    if (methodDeclaration.getName().startsWith("get")|| methodDeclaration.getName().startsWith("set")) {
                        String variableNames = methodDeclaration.getName().substring(3);
                        publicMethodNames.add(variableNames.toLowerCase());
                    } 
                    else {
                        if (hasNextParam){
                            yumlSyntaxMethods += ";";
                        }
                        yumlSyntaxMethods += "+ " + methodDeclaration.getName() + "(";
                        for (Object childrenNodes : methodDeclaration.getChildrenNodes()) {
                        	
                            if (childrenNodes instanceof Parameter) {
                                Parameter parameter = (Parameter) childrenNodes;
                                String parameterType = parameter.getType().toString();
                                String parameterName = parameter.getChildrenNodes().get(0).toString();
                                yumlSyntaxMethods += parameterName + " : " + parameterType;
                                if (classMap.containsKey(parameterType)&& !classMap.get(className)) {
                                    yumlSyntaxClassRelations += "[" + className + "] uses -.->";
                                    if (classMap.get(parameterType))
                                        yumlSyntaxClassRelations += "["+YUML_INTERFACE+ parameterType + "]";
                                    else
                                        yumlSyntaxClassRelations += "[" + parameterType + "]";
                                }
                                yumlSyntaxClassRelations += ",";
                            } 
                            else {
                                String body[] = childrenNodes.toString().split(" ");
                                for (String str : body) {
                                    if (classMap.containsKey(str)&& !classMap.get(className)) {
                                        yumlSyntaxClassRelations += "[" + className + "] uses -.->";
                                        if (classMap.get(str)) yumlSyntaxClassRelations += "["+YUML_INTERFACE + str+ "]";
                                        else yumlSyntaxClassRelations += "[" + str + "]";
                                        yumlSyntaxClassRelations += ",";
                                    }
                                }
                            }
                        }
                        hasNextParam = true;
                        yumlSyntaxMethods += ") : " + methodDeclaration.getType();
                        
                    }
                }
            }
        }
    	
    	result.add(publicMethodNames);
    	ArrayList<String> yumlSyntaxArrays = new ArrayList<String>();
    	yumlSyntaxArrays.add(yumlSyntaxMethods);
    	yumlSyntaxArrays.add(yumlSyntaxClassRelations);
    	String hasNextParamString;
    	if(hasNextParam){
    		hasNextParamString = "true";
    	}
    	else{
    		hasNextParamString = "false";
    	}
    	yumlSyntaxArrays.add(hasNextParamString);
    	result.add(yumlSyntaxArrays);
    	
    	return result;
    }
    

    private String parseAST(CompilationUnit compilationUnit) {
        
        String yumlSyntaxMethods = "";
        String yumlSyntaxClassName = "";
        String yumlSyntaxClassAttributes = "";
        String yumlSyntaxClassRelations = ",";
        
        String yumlOutput = "";
        String className = "";
        
        List<TypeDeclaration> typeDeclarationList = compilationUnit.getTypes();
     // assuming classes are not nested
        Node node = typeDeclarationList.get(0); 
        ArrayList<String> publicMethodNames = new ArrayList<String>();
        
        
        ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) node; // Get className
        
        if (classOrInterface.isInterface()) {
            yumlSyntaxClassName = "["+YUML_INTERFACE;
        } 
        else {
            yumlSyntaxClassName = "[";
        }
        yumlSyntaxClassName += classOrInterface.getName();
        className = classOrInterface.getName();

        
        boolean hasNextParam = false;
    
        String[] parseConstructorDeclarationResult = new String[3];
        
        parseConstructorDeclarationResult = parseConstructorDeclaration(node,classOrInterface, hasNextParam,yumlSyntaxMethods,
    											 yumlSyntaxClassRelations, className);
        
        String hasNextParamString = parseConstructorDeclarationResult[0];
        yumlSyntaxMethods = parseConstructorDeclarationResult[1];
        yumlSyntaxClassRelations = parseConstructorDeclarationResult[2];
        
        if(hasNextParamString.equals("false")){
        	hasNextParam = false;
        }
        else{
        	hasNextParam = true;
        }
        
        ArrayList<ArrayList<String>> parseMethodDeclarationResult = parseMethodDeclaration(node,classOrInterface, publicMethodNames,
				 yumlSyntaxMethods, yumlSyntaxClassRelations, className,  hasNextParam);
        
        //ArrayList<String> parsedPublicMethodNames
        publicMethodNames = parseMethodDeclarationResult.get(0);
        
        yumlSyntaxMethods = parseMethodDeclarationResult.get(1).get(0);
        yumlSyntaxClassRelations = parseMethodDeclarationResult.get(1).get(1);
    	hasNextParamString= parseMethodDeclarationResult.get(1).get(2);
        
    	if (hasNextParamString.equals("true")){
    		hasNextParam = true;
    	}
    	else{
    		hasNextParam = false;
    	}
        // Parsing Fields
    	
    	 ArrayList<ArrayList<String>> parseClassAttributesResult = parseClassAttributes(node, publicMethodNames, yumlSyntaxClassAttributes, className );
    	 publicMethodNames = parseClassAttributesResult.get(0);
    	 yumlSyntaxClassAttributes = parseClassAttributesResult.get(1).get(0); 
        
    	    	
        // Check relationships
        if (classOrInterface.getExtends() != null) {
            yumlSyntaxClassRelations += "[" + className + "] " + "-^ " + classOrInterface.getExtends();
            yumlSyntaxClassRelations += ",";
        }
        if (classOrInterface.getImplements() != null) {
            List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) classOrInterface.getImplements();
            for (ClassOrInterfaceType ifList : interfaceList) {
                yumlSyntaxClassRelations += "[" + className + "] -.-^ ["+YUML_INTERFACE + ifList + "],";

            }
        }
        // Combining class names, methods and attributes into result
        yumlOutput += yumlSyntaxClassName;
        if (!yumlSyntaxClassAttributes.isEmpty()) {
            yumlOutput += "|" + changeBrackets(yumlSyntaxClassAttributes);
        }
        if (!yumlSyntaxMethods.isEmpty()) {
            yumlOutput += "|" + changeBrackets(yumlSyntaxMethods);
        }
        yumlOutput += "]";
        yumlOutput += yumlSyntaxClassRelations;
        return yumlOutput;
    }
    
    private ArrayList<ArrayList<String>> parseClassAttributes(Node node, ArrayList<String>publicMethodNames, String yumlSyntaxClassAttributes, String className ){
    	boolean nextField = false;
    	ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    	for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) node).getMembers()) {
            if (bodyDeclaration instanceof FieldDeclaration) {
                FieldDeclaration fieldDescriptor = ((FieldDeclaration) bodyDeclaration);
                String scopeAccessor = changeAccessorsToUmlSymbols(bodyDeclaration.toStringWithoutComments().substring(0,
                                bodyDeclaration.toStringWithoutComments().indexOf(" ")));
                String attributeClassName = changeBrackets(fieldDescriptor.getType().toString());
                String attributeName = fieldDescriptor.getChildrenNodes().get(1).toString();
                if (attributeName.contains("=")){
                    attributeName = fieldDescriptor.getChildrenNodes().get(1).toString().substring(0, fieldDescriptor.getChildrenNodes().get(1).toString().indexOf("=") - 1);
                }
               
                if (scopeAccessor.equals("-")
                        && publicMethodNames.contains(attributeName.toLowerCase())) {
                    scopeAccessor = "+";
                }
                String markDependencies = "";
                boolean hasMultipleDependencies = false;
                if (attributeClassName.contains("(")) {
                    markDependencies = attributeClassName.substring(attributeClassName.indexOf("(") + 1,
                            attributeClassName.indexOf(")"));
                    hasMultipleDependencies = true;
                } else if (classMap.containsKey(attributeClassName)) {
                    markDependencies = attributeClassName;
                }
                if (markDependencies.length() > 0 && classMap.containsKey(markDependencies)) {
                    String realizations = "-";

                    if (classTypeMap.containsKey(markDependencies + "-" + className)) {
                        realizations = classTypeMap.get(markDependencies + "-" + className);
                        if (hasMultipleDependencies)
                            realizations = "*" + realizations;
                        classTypeMap.put(markDependencies + "-" + className,
                                realizations);
                    } else {
                        if (hasMultipleDependencies)
                            realizations += "*";
                        classTypeMap.put(className + "-" + markDependencies,realizations);
                    }
                }
                if (scopeAccessor == "+" || scopeAccessor == "-") {
                    if (nextField)
                        yumlSyntaxClassAttributes += "; ";
                    yumlSyntaxClassAttributes += scopeAccessor + " " + attributeName + " : " + attributeClassName;
                    nextField = true;
                }
            }

        }
    	
    	result.add(publicMethodNames);
    	ArrayList<String> yumlSyntaxArrays = new ArrayList<String>();
    	yumlSyntaxArrays.add(yumlSyntaxClassAttributes);
    	
    	
    	result.add(yumlSyntaxArrays);
    	
    	return result;
    }
    private String makeYumlCodeUnique(String code) {
        String[] codeLines = code.split(",");
        String[] uniqueCodeLines = new LinkedHashSet<String>(
                Arrays.asList(codeLines)).toArray(new String[0]);
        String result = String.join(",", uniqueCodeLines);
        return result;
    }

    private String changeBrackets(String str) {
    	str = str.replace("]", ")");        
        str = str.replace("[", "(");
        
        str = str.replace(">", ")");
        str = str.replace("<", "(");
        return str;
    }

    

   
    
    private String changeAccessorsToUmlSymbols(String accessor) {
        
    	if (accessor.equals("public")){
    		return "+";
    	}
    	else if(accessor.equals("private")){
    		return "-";
    	}
    	else{
    		return "";
    	}
    	
    }


}
