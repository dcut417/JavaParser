package com.qualitascorpus.javaanalyser.demo;

import java.io.File;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.qualitascorpus.javaanalyser.core.Analyser;

/**
 * This shows examples of various uses of JavaParser where what is parsed is a File. It contains
 * the same code examples as JavaParserSimpleString, but there are also comments in the files,
 * and what this does provides more details about the code and comments (e.g. positions) (see listNodes())
 */
public class JavaParserSimpleFile {
    public static void main( String[] args ) throws Exception {
    	File sourceFile = new File("JavaAnalyserCore/src/main/resources/demo/A.java");
    	System.out.println("[[" + sourceFile.getCanonicalPath() + "]]");
    	listClasses(sourceFile);
    	//listMethodInvocations(sourceFile);
    	listNodes(sourceFile);
    	sourceFile = new File("JavaAnalyserCore/src/main/resources/demo/P.java");
    	System.out.println("[[" + sourceFile.getCanonicalPath()+ "]]");
    	listClasses(sourceFile);
    	//listMethodInvocations(sourceFile);
    	listNodes(sourceFile);
    }
    private static CompilationUnit getCompilationUnitForCode(File sourceFile) throws Exception {
      Analyser analyser = new Analyser();
      CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(sourceFile.toPath());
      return compilationUnit;
    }
    
    public static void listClasses(File sourceFile) throws Exception {
    	CompilationUnit compilationUnit = getCompilationUnitForCode(sourceFile);
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
    
    public static void listMethodInvocations(File sourceFile) throws Exception {
    	CompilationUnit compilationUnit = getCompilationUnitForCode(sourceFile);
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

    public static void listNodes(File sourceFile) throws Exception {
    	CompilationUnit compilationUnit = getCompilationUnitForCode(sourceFile);
    	listNodes(compilationUnit);
    }
    
    /**
     * This prints every node in the AST, which includes every little details (such as each individual part of 
     * a package name), so isn't really useful. This this provides a way to see what an AST looks like, and
     * what information we have available to us.
     * @param parent
     * @throws Exception
     */
    public static void listNodes(Node parent) {
		if (parent.getComment().isPresent()) {
			System.out.println("Parent:" + parent.getClass());
			System.out.println("comment:" + parent.getComment());
			System.out.println("position:" + parent.getBegin() + "-" + parent.getEnd());
			System.out.println();
		}
    	for (Node child: parent.getChildNodes()) {
    		listNodes(child);
    	}
    }
}
