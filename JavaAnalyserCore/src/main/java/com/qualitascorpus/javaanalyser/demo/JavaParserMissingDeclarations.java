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
 * With this version, one of the types (a.b.c.F) referred to by the code being analysed
 * is not on the classpath anywhere. The source files being analysed are separated into
 * two source directories because one of the source directories is used in other demos 
 * (and so can't have the file that refers to a.b.c.F or the other demos will fail)
 * 
 * Note this also demonstrates use of multiple source directories, in particular 
 * when using JavaParserTypeSolver.
 */
public class JavaParserMissingDeclarations {

	public static void main( String[] args ) throws Exception {
		String rootPath = "JavaAnalyserCore/src/main/resources/demo";
		String src1 = rootPath + "/src1";
		String src3 = rootPath + "/src3";
		Analyser analyser = new Analyser();
		analyser.addSourcePath(src1);
		analyser.addSourcePath(src3);
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
