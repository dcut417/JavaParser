package com.qualitascorpus.javaanalyser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

/**
 * Currently this is just experimenting with JavaParser. A source I used was (among other things):
 * https://tomassetti.me/getting-started-with-javaparser-analyzing-java-code-programmatically/#chapter1
 * 
 * This shows examples of various uses of JavaParser where what is parsed is a String. It also shows
 * some resolution but only with respect to the standard API. 
 */
public class JavaParserSimpleString {
    public static void main( String[] args ) throws Exception {
    	String sourceCode = "package pkg; class A { } class B { }";
    	System.out.println("[[" + sourceCode + "]]");
    	listClasses(sourceCode);
    	listMethodInvocations(sourceCode);
    	sourceCode = "package x.y.z.z.y; class P { public void p(String s) { s.toString(); } } class Q { }";
    	System.out.println("[[" + sourceCode + "]]");
    	listClasses(sourceCode);
    	listMethodInvocations(sourceCode);
    }
    private static CompilationUnit getCompilationUnitForCode(String sourceCode) throws Exception {
    	/*
    	 * As a general rule, there will be multiple TypeSolvers, so get into the habit of using CombinedTypeSolver.
    	 * ReflectionTypeSolver will recognise any types on the classpath
    	 */
      TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
      ParserConfiguration configuration = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
      JavaParser parser = new JavaParser(configuration);
      ParseResult<CompilationUnit> parseResult = parser.parse(sourceCode);
      CompilationUnit compilationUnit = parseResult.getResult().get();
      return compilationUnit;
    }
    
    public static void listClasses(String sourceCode) throws Exception {
    	CompilationUnit compilationUnit = getCompilationUnitForCode(sourceCode);
    	VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
    		@Override
    		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
    			super.visit(n, arg);
    			System.out.println(" * " + n.getName());
    			System.out.println("   FQN:" + n.resolve().getQualifiedName());
    		};
    	};
    	System.out.println("CLASS DECLARATIONS");
    	visitor.visit(compilationUnit, null);
    }
    
    public static void listMethodInvocations(String sourceCode) throws Exception {
    	CompilationUnit compilationUnit = getCompilationUnitForCode(sourceCode);
        VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
        	@Override
        	public void visit(MethodCallExpr n, Object arg) {
        		super.visit(n, arg);
        		System.out.println(" method invoked " + n);
        		ResolvedMethodDeclaration rmd = n.resolve();
        		System.out.println("resolved:" + rmd);
        	};
        };
    	System.out.println("METHOD INVOCATIONS");
        visitor.visit(compilationUnit, null);
    }
}
