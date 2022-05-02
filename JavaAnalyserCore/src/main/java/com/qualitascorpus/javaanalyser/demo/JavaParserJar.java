package com.qualitascorpus.javaanalyser.demo;

import java.nio.file.Path;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;

/**
 * With this version, one of the classes refers to a class in an external library (JUnit).
 * This shows how to include the jar file for the library when using JavaParser.
 */
public class JavaParserJar {

	public static void main( String[] args ) throws Exception {
		String rootPath = "JavaAnalyserCore/src/main/resources/demo";
		String src1 = rootPath + "/src1";
		String src4 = rootPath + "/src4";
		Analyser analyser = new Analyser();
		analyser.addSourcePath(src1);
		analyser.addSourcePath(src4);
		
		analyser.addJar("JavaAnalyserCore/src/main/resources/demo/junit-4.12.jar");
		Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(rootPath);
		for (Path path: paths) {
			CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
			VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
				@Override
				public void visit(ClassOrInterfaceDeclaration n, Object arg) {
					super.visit(n, arg);
					System.out.println("  - ClassOrInterface name: " + n.getName());
					System.out.println("  - FQN:" + n.resolve().getQualifiedName());
				};
				@Override
				public void visit(MethodCallExpr n, Object arg) {
					super.visit(n, arg);
					System.out.println("  - method invoked " + n);
					try {
						ResolvedMethodDeclaration rmd = n.resolve();
						System.out.println("  - method resolved:" + rmd.getQualifiedSignature());
						System.out.println("  - declaring type: " + rmd.declaringType().getQualifiedName());
					} catch (UnsolvedSymbolException usex) {
						System.out.println(">>>Cannot resolve " + n);
					}
				};
			};
			System.out.println("OUTPUT FOR " + path);
			visitor.visit(compilationUnit, null);
		}
	}
}
