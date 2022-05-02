package com.qualitascorpus.javaanalyser.demo;

import java.nio.file.Path;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;

public class JavaParserSimpleDir {

	public static void main( String[] args ) throws Exception {
		String rootPath = "JavaAnalyserCore/src/main/resources/demo/src1";
		Analyser analyser = new Analyser();
		analyser.addSourcePath(rootPath);
		Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(rootPath);
		for (Path path: paths) {
			CompilationUnit compilationUnit = analyser.getCompilationUnitForPath(path);
			VoidVisitor<Object> visitor = new VoidVisitorAdapter<Object>() {
				@Override
				public void visit(ClassOrInterfaceDeclaration n, Object arg) {
					super.visit(n, arg);
					System.out.println(" * " + n.getName());
					System.out.println("   FQN:" + n.resolve().getQualifiedName());
				};
				@Override
				public void visit(MethodCallExpr n, Object arg) {
					super.visit(n, arg);
					System.out.println(" method invoked " + n);
					ResolvedMethodDeclaration rmd = n.resolve();
					System.out.println("resolved:" + rmd);
				};
			};
			System.out.println("CLASS DECLARATIONS");
			visitor.visit(compilationUnit, null);
		}
	}
}
