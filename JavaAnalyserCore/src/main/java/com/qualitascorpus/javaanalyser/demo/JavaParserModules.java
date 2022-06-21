package com.qualitascorpus.javaanalyser.demo;

import java.nio.file.Path;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;

/**
 * This lists all the names of "modules", that is, identifiers that are used
 * as a type name.
 * As with MissingDeclarations, some declarations for the modules are missing, meaning
 * they cannot be resolved.  
 */
public class JavaParserModules {

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
				public void visit(VariableDeclarator n, Object arg) {
					super.visit(n, arg);
					System.out.println("VariableDeclarator");
					System.out.println(" * " + n.getName());
					System.out.println(" - " + n);
					if (n.resolve().isType()) {
						System.out.println("   FQN (type):" + n.resolve().getType());
					} else {
						System.out.println("   FQN (not type):" + n.resolve().getName());
					}
				}
				@Override
				public void visit(VariableDeclarationExpr n, Object arg) {
					super.visit(n, arg);
					//JavaParserSimpleFile.listNodes(n);
					System.out.println("VariableDeclarationExpr");
					System.out.println(" - " + n);
				}
				@Override
				public void visit(PrimitiveType n, Object arg) {
					super.visit(n, arg);
					System.out.println("PrimitiveType: " + n.resolve());
				}
				@Override
				public void visit(ClassOrInterfaceType n, Object arg) {
					super.visit(n, arg);
					try {
						System.out.println("ClassOrInterfaceType: " + n.getName());
					} catch (UnsolvedSymbolException usex) {
						System.out.println("  -> Unrecognised:" + n.getNameAsString());
					}
				}
			};
			System.out.println("MODULES");
			visitor.visit(compilationUnit, null);

		}

	}
}
