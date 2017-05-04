package umlparser.cmpe202;

import net.sourceforge.plantuml.SourceStringReader;

import java.util.*;
import java.io.*;


import com.github.javaparser.ast.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;


public class ParserSeqDiagram {
	
    private String sourceFilePath;
    private String destFilePath;
    ArrayList<CompilationUnit> compilationUnitArray;
    private String className;
    private String methodName;
    
    private String planUmlSyntaxCode = "@startuml\n";
    HashMap<String, String> methodClassMappings = new HashMap<String, String>();
    HashMap<String, ArrayList<MethodCallExpr>> methodCallMapping = new HashMap<String, ArrayList<MethodCallExpr>>();
        
    

    ParserSeqDiagram(String sourceFilePath, String className, String methodName, String destFilePath) {
    	this.className = className;
        this.methodName = methodName;
    	this.sourceFilePath = sourceFilePath;
        this.destFilePath = sourceFilePath + "/" + destFilePath + ".png";        
    }
    
    private ArrayList<CompilationUnit> getAllcompilationUnitList(String sourcePath)
            throws Exception {
    	ArrayList<CompilationUnit> compilationUnitArray = new ArrayList<CompilationUnit>();
    	File currDirectory = new File(sourcePath);
        for ( File file : currDirectory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".java")) {
            	CompilationUnit compilationUnit;
            	FileInputStream inputStream = new FileInputStream(file);                
                try {
                    compilationUnit = JavaParser.parse(inputStream);
                    compilationUnitArray.add(compilationUnit);
                }
                catch (Exception e){
                	e.printStackTrace();
                }
                finally {
                    inputStream.close();
                }
            }
            else{
            System.out.println(file.getName() + " is not a java file");	
            }
        }
        return compilationUnitArray;
    }

    public void startParsing() throws Exception {
        compilationUnitArray = getAllcompilationUnitList(sourceFilePath);
        
        createMappings();
        planUmlSyntaxCode += "actor client #green\n client -> " + className + " : " + methodName + "\nactivate " + methodClassMappings.get(methodName) + "\n";
        parseAST(methodName);
        planUmlSyntaxCode += "@enduml";
        System.out.println("Generating Sequence Diagram....");
        
        
        drawDiagram(planUmlSyntaxCode);
        System.out.println("Intermediate plantUml Code: " + planUmlSyntaxCode);
    }

    private void parseAST(String methodCall) {
        for (MethodCallExpr methodCallExpression : methodCallMapping.get(methodCall)) {
        	
        	String calledMethodName = methodCallExpression.getName();
        	String calledClassName = methodClassMappings.get(calledMethodName);
        	
        	String callerClassName = methodClassMappings.get(methodCall);
            
            
            if (methodClassMappings.containsKey(calledMethodName)) {
                planUmlSyntaxCode += callerClassName + " -> " + calledClassName + " : " + methodCallExpression.toStringWithoutComments() + "\nactivate " + calledClassName + "\n";
                //Recursively calling the called method
                parseAST(calledMethodName);
             
                planUmlSyntaxCode += calledClassName + " -->> " + callerClassName + "\n" + "deactivate " + calledClassName + "\n";
             
            }
        }
    }
    
    private String drawDiagram(String src) throws IOException {

        OutputStream outputStream = new FileOutputStream(destFilePath);
        return new SourceStringReader(src).generateImage(outputStream);

    }

    private void createMappings() {
        for (CompilationUnit compilationUnit : compilationUnitArray) {
            String shortClassName = "";
            
            List<TypeDeclaration> compilationUnitTypeList = compilationUnit.getTypes();
           
            for (int i=0;i<compilationUnitTypeList.size();i++) {
            	
                ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) compilationUnitTypeList.get(i);
                shortClassName = classOrInterface.getName();
                
                for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) classOrInterface).getMembers()) {
                    boolean validBodyDeclaration = bodyDeclaration instanceof MethodDeclaration;
                	if (validBodyDeclaration) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                        ArrayList<MethodCallExpr> methodCallExprArr = new ArrayList<MethodCallExpr>();
                      
                        for (Object methodDeclarationChildren : methodDeclaration.getChildrenNodes()) {
                            boolean validmethodDeclarationChildren = methodDeclarationChildren instanceof BlockStmt;
                        	if (validmethodDeclarationChildren) {
                            
                        		for (Object expressionStatement : ((Node) methodDeclarationChildren).getChildrenNodes()) {
                        			boolean validExpressionStatement = expressionStatement instanceof ExpressionStmt;
                        			if (validExpressionStatement) {
                                    
                        				if (((ExpressionStmt) (expressionStatement)).getExpression() instanceof MethodCallExpr) {
                                        
                        					methodCallExprArr.add((MethodCallExpr) (((ExpressionStmt) (expressionStatement)).getExpression()));
                                        }
                        				else{
                        					System.out.println(((ExpressionStmt) (expressionStatement)).getExpression()+ "not instance of ExpressionStmt");
                        				}
                                    }
                        			else{
                                		System.out.println( expressionStatement+" not instance of ExpressionStmt");
                                	}
                                }
                            }
                        	else{
                        		System.out.println(methodDeclarationChildren+" not instance of BlockStatement");
                        	}
                        }
                        methodClassMappings.put(methodDeclaration.getName(), shortClassName);
                        methodCallMapping.put(methodDeclaration.getName(), methodCallExprArr);
                        
                        
                    }
                }
            }
        }
    }

    

    
}
