package com.qualitascorpus.javaanalyser.demo;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;

public class JavaParserSimpleDir {

	private Comments comments = new Comments();

	public static void main( String[] args ) throws Exception {
		String rootPath = "QualitasCorpus-20130901r/Systems/argouml";
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
					System.out.println("   FQN: " + n.resolve().getQualifiedName());
				};
				/*@Override
				public void visit(MethodCallExpr n, Object arg) {
					super.visit(n, arg);
					System.out.println(" method invoked " + n);
					ResolvedMethodDeclaration rmd = n.resolve();
					System.out.println("resolved:" + rmd);
				};*/
			};
			System.out.println("CLASS DECLARATIONS");
			visitor.visit(compilationUnit, null);
			listNodes(compilationUnit);
		}
	}

	/**
	 * This prints every node in the AST, which includes every little details (such as each individual part of
	 * a package name), so isn't really useful. This this provides a way to see what an AST looks like, and
	 * what information we have available to us.
	 * @param parent
	 * @throws Exception
	 */
	public static void listNodes(Node parent) {
		if (parent.getComment().isPresent() && parent.getComment().get().getContent().contains("TODO")) {
			System.out.println("Parent: " + parent.getClass());
			String commentType;
			if (parent.getComment().get().isBlockComment()) {
				commentType = "Block";
			} else if (parent.getComment().get().isJavadocComment()) {
				commentType = "Javadoc";
			} else {
				commentType = "Line";
			}
			System.out.println("Type: " + commentType);
			System.out.println("Comment: " + parent.getComment().get().getContent());
			System.out.println("Position: " + parent.getBegin().get() + "-" + parent.getEnd().get());
			System.out.println();
		}
		for (Node child: parent.getChildNodes()) {
			listNodes(child);
		}
	}
}
