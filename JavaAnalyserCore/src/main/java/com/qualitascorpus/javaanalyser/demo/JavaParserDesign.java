package com.qualitascorpus.javaanalyser.demo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;

/**
 * This demo shows how to analyse one of the kalah designs. It produces a list of all the classes
 * (and interfaces, annotations, and enums) declared in the design, and the method signatures of
 * all methods in each. 
 * 
 * This also demonstrates the use of different visitors for different cases. This creates
 * a visitor for compilation units that only visits ClassOrInterface nodes. When it visits
 * these nodes it creates a ClassOrInterfaceVisitor that only visits ClassOrInterface nodes.
 * Those visitors only visit MethodDeclaration nodes using a separate visitor for MethodDeclaration nodes
 * 
 * One reason for having different visitors is that it means the different kinds of information
 * that needs to be collected can be kept separate using the "argument" parameter to the visitors.
 * 
 * The ClassOrInterfaceVisitor creates a ClassOrInterfaceDetails object for each node. 
 * ClassOrInterfaceDetails objects store information for all methods declared in the ClassOrInterface,
 * as well as the name of the class or interface. 
 * 
 * MethodDeclarationDetails objects get created for each method. They store the list of parameters
 * (and their types), the return type, and the return type.
 * 
 * Other uses of types (e.g. fields and local variables) can be discovered in a similar way
 * 
 * All that is done by this demo is print out information, so it doesn't really need to be stored
 * as it has been done (the commented out print systems suggest how this might be done), but this
 * way it demonstrates how information can be stored for later use.
 * 
 * CompilationUnit can have multiple ClassOrInterface declarations, and so really
 * an argument object should be used for that as well.
 * 
 * Note that this isn't necessarily the best use of the JavaParser API to extract the relevant information.
 */
public class JavaParserDesign {

	public static void main( String[] args ) throws Exception {
		String rootPath = "kalah_designs";
		String designToAnalyse = "design1001";
		String pathToAnalyse = rootPath + "/" + designToAnalyse + "/src";
		Analyser analyser = new Analyser();
		analyser.addSourcePath(pathToAnalyse);
		analyser.addJar("kalah_designs/lib/kalah20200717.jar");

		Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(pathToAnalyse);
		VoidVisitor<Object> compilationUnitVisitor = getCompilationUnitVisitor();
		for (Path path: paths) {
			CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
			compilationUnitVisitor.visit(compilationUnit, null);
		}
	}

	private static VoidVisitor<Object> getCompilationUnitVisitor() { 
		VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
			@Override
			public void visit(ClassOrInterfaceDeclaration n, Object arg) {
				super.visit(n, arg);
				VoidVisitor<ClassOrInterfaceDetails> visitor = getClassOrInterfaceVisitor();
				ClassOrInterfaceDetails classOrInterfaceDetails = new ClassOrInterfaceDetails(n.resolve().getQualifiedName().toString());
				visitor.visit(n, classOrInterfaceDetails);
				System.out.println(classOrInterfaceDetails);
			};
		};
		return visitor;
	}
	private static VoidVisitor<ClassOrInterfaceDetails> getClassOrInterfaceVisitor() {
		VoidVisitor<ClassOrInterfaceDetails> visitor = new VoidVisitorAdapter<ClassOrInterfaceDetails>() {
			@Override
			public void visit(MethodDeclaration n, ClassOrInterfaceDetails arg) {
				super.visit(n, arg);
//				System.out.println("  - method decl: " + n.getDeclarationAsString());
				VoidVisitor<MethodDetails> methodDeclarationVisitor = getMethodDeclarationVisitor();
				MethodDetails methodDetails = new MethodDetails(n.getNameAsString(), n.getTypeAsString());
				methodDeclarationVisitor.visit(n, methodDetails);
				arg.addMethodDetails(methodDetails);
//				System.out.println("Method> " + methodDetails.toString());
			};
		};
		return visitor;		
	}
	private static VoidVisitor<MethodDetails> getMethodDeclarationVisitor() { 
		VoidVisitor<MethodDetails> visitor = new VoidVisitorAdapter<MethodDetails>() {
			@Override
			public void visit(Parameter n, MethodDetails arg) {
				super.visit(n, arg);
				arg.addParameter(n.getNameAsString(), n.getType().resolve().describe());
			};
		};
		return visitor;
	}
	
	///// "argument" classes //////////////
	
	private static class ClassOrInterfaceDetails {
		private String _classOrInterfaceName;
		private List<MethodDetails> _detailsOfAllMethods = new ArrayList<MethodDetails>();
		public ClassOrInterfaceDetails(String name) {
			_classOrInterfaceName = name;
//			System.out.println("ClassOrInterface:" + _classOrInterfaceName);
		}
		public void addMethodDetails(MethodDetails methodDetails) {
			_detailsOfAllMethods.add(methodDetails);
//			System.out.println("    " + methodDetails.toString());
		}
		@Override
		public String toString() {
			String result = _classOrInterfaceName + "\n";
			for (MethodDetails details: _detailsOfAllMethods) {
				result += "    " + details.toString() + "\n";
			}
			return result;
		}
	}
	private static class MethodDetails {
		private String _methodName;
		private List<String> _parameterTypes = new ArrayList<String>();
		private List<String> _parameterNames= new ArrayList<String>();
		private String _returnType;
		public MethodDetails(String methodName, String returnType) {
			_methodName = methodName;
			_returnType = returnType;
		}
		public void addParameter(String name, String type) {
			_parameterNames.add(name);
			_parameterTypes.add(type);
		}
		@Override
		public String toString() {
			String parameters = "(";
			for (int i = 0; i < _parameterNames.size(); i++) {
				parameters += _parameterNames.get(i) + ":" + _parameterTypes.get(i);
			}
			parameters += ")";
			return _methodName + parameters + "->" + _returnType;
		}
	}
}
