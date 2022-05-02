package com.qualitascorpus.javaanalyser.demo;

import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.LocalRecordDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.YieldStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.qualitascorpus.javaanalyser.core.Analyser;
import com.qualitascorpus.javaanalyser.core.Utility;
import com.qualitascorpus.text.TabularOutput;

/**
 * 
 * @author ewan
 * @date 20210922
 * Started from JavaParserDesign
 */
public class JavaParserStatementCount {

	private static final boolean DEBUG = false;
	private static final String DEFAULT_ROOT_PATH = "kalah_designs";
	private static final String DEFAULT_DESIGN_TO_ANALYSE = "design1001";
	private static final String DEFAULT_PATH_TO_KALAH_JAR = "kalah_designs/lib/kalah20200717.jar";
	private static final int OVERALL_STATS_ROW = 2;
	private static final int[] NUM_UNITS_COORDINATE = { OVERALL_STATS_ROW, 1 };
	private static final int[] AVERGE_METHODS_PER_UNIT_COORDINATE = { OVERALL_STATS_ROW, 2 };
	private static final int[] STDDEV_METHODS_PER_UNIT_COORDINATE = { OVERALL_STATS_ROW, 3 };
	private static final int[] RANGE_METHODS_PER_UNIT_COORDINATE = { OVERALL_STATS_ROW, 4 };
	
	public static void main( String[] args ) throws Exception {
		String pathToAnalyse = determinePathToAnalyse(args);
		Analyser analyser = new Analyser();
		analyser.addSourcePath(pathToAnalyse);
		analyser.addJar(determinePathToKalahJar(args));

		Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots(pathToAnalyse);
		TabularOutput output = new TabularOutput(false);
		output.newRow();
		output.fillCurrentCellContents(pathToAnalyse);
		output.newRow();
		output.fillCurrentCellAsHeader("Design", false);
		output.fillCurrentCellAsHeader("Number of Units", true);
		output.fillCurrentCellAsHeader("Average Methods", true);
		output.fillCurrentCellAsHeader("StdDev Methods", true);
		output.fillCurrentCellAsHeader("Range Methods", true);
		output.newRow();
		output.fillCurrentCellContents(determineDesignID(args));
		
		output.newRow();
		ClassOrInterfaceDetails.statisticsHeader(output);
		VoidVisitor<CompilationUnitDetails> compilationUnitVisitor = getCompilationUnitVisitor();
		List<CompilationUnitDetails> compilationUnitDetails = new ArrayList<CompilationUnitDetails>();
		for (Path path: paths) {
			//System.out.println("Analysing " + path);
			CompilationUnit compilationUnit = null;
			try {
				compilationUnit = analyser.getCompilationUnitForPath(path);
			} catch (NoSuchElementException nse) {
				System.err.println(path + "------------>" + nse + " Aborting");
				continue;
			}
			CompilationUnitDetails details = new CompilationUnitDetails(path.toString());
			compilationUnitDetails.add(details);
			try {
				compilationUnitVisitor.visit(compilationUnit, details);
			} catch (UnsupportedOperationException unsup) {
				// TODO This looks like when generic type parameters are encountered
				System.err.println(path + "------------>" + unsup + " Aborting");
				continue;
			}
			details.statistics(output);
		}
		SummaryStatistics stats = new SummaryStatistics();
		for (CompilationUnitDetails cuDetails: compilationUnitDetails) {
			cuDetails.summaryStatistics(stats);
			//stats.addValue(cuDetails.totalMethods());
		}
		
		output.appendCellContents(NUM_UNITS_COORDINATE[0], NUM_UNITS_COORDINATE[1], "" + stats.getN());
		output.appendCellContents(AVERGE_METHODS_PER_UNIT_COORDINATE[0], AVERGE_METHODS_PER_UNIT_COORDINATE[1], 
				tidyDouble(stats.getMean())); 
		output.appendCellContents(STDDEV_METHODS_PER_UNIT_COORDINATE[0], STDDEV_METHODS_PER_UNIT_COORDINATE[1], 
				tidyDouble(stats.getStandardDeviation()));
		output.appendCellContents(RANGE_METHODS_PER_UNIT_COORDINATE[0], RANGE_METHODS_PER_UNIT_COORDINATE[1], 
				((int)stats.getMin()) + "-" + ((int)stats.getMax()));
		output.newRow(); // Spacer
		output.newRow(); // Spacer
		output.printAsTSV();
	}
	private static String determineDesignID(String[] args) {
		if (args.length == 0) {
			return DEFAULT_DESIGN_TO_ANALYSE;
		}
		return args[1];		
	}
	private static String determinePathToKalahJar(String[] args) {
		if (args.length == 0) {
			return DEFAULT_PATH_TO_KALAH_JAR;
		}
		return args[2];
	}
	private static String determinePathToAnalyse(String[] args) {
		if (args.length == 0) {
			return DEFAULT_ROOT_PATH + "/" + DEFAULT_DESIGN_TO_ANALYSE + "/src";
		}
		if (args.length != 3) {
			System.out.println("Usage: $0 [<root path> <design to analyse> <path to kalah jar>]");
			System.out.println("\tE.g. $0 kalah_designs design1001 kalah_designs/lib/kalah20200717.jar");
			System.exit(1);
		}
		return args[0] + "/" + args[1] + "/src";
	}
	private static void printChildren(Node node) {
		System.out.println("PARENT:"  + node.getClass());
		Node.DirectChildrenIterator iterator = new Node.DirectChildrenIterator(node); 
		while (iterator.hasNext()) {
			Node child = iterator.next();
			System.out.println("CHILD\t" + child.getClass());
		}
	}

	private static VoidVisitor<CompilationUnitDetails> getCompilationUnitVisitor() { 
		VoidVisitor<CompilationUnitDetails> visitor = new VoidVisitorAdapter<CompilationUnitDetails>() {
			@Override
			public void visit(ClassOrInterfaceDeclaration n, CompilationUnitDetails arg) {
				super.visit(n, arg);
				VoidVisitor<ClassOrInterfaceDetails> visitor = getClassOrInterfaceVisitor();
				ClassOrInterfaceDetails classOrInterfaceDetails = new ClassOrInterfaceDetails(n.resolve().getQualifiedName().toString(),
						n.isInterface(), n.isEnumDeclaration(), n.isAnnotationDeclaration());
				visitor.visit(n, classOrInterfaceDetails);
				if (DEBUG) {
					System.out.println(classOrInterfaceDetails);
				}
				arg.add(classOrInterfaceDetails);
			};
		};
		return visitor;
	}
	private static VoidVisitor<ClassOrInterfaceDetails> getClassOrInterfaceVisitor() {
		VoidVisitor<ClassOrInterfaceDetails> visitor = new VoidVisitorAdapter<ClassOrInterfaceDetails>() {
			@Override
			public void visit(ConstructorDeclaration node, ClassOrInterfaceDetails visitorArg) {
				super.visit(node, visitorArg);
				FunctionalUnitDetails constructorDetails = new FunctionalUnitDetails(visitorArg.getFQN());
				VoidVisitor<FunctionalUnitDetails> methodDeclarationVisitor = getMethodDeclarationVisitor();
				methodDeclarationVisitor.visit(node, constructorDetails);
				visitorArg.addMethodDetails(constructorDetails);
			}
			@Override
			public void visit(MethodDeclaration n, ClassOrInterfaceDetails arg) {
				super.visit(n, arg);
				//printChildren(n);
				VoidVisitor<FunctionalUnitDetails> methodDeclarationVisitor = getMethodDeclarationVisitor();
				FunctionalUnitDetails methodDetails = new FunctionalUnitDetails(arg.getFQN(), n.getNameAsString(), n.getTypeAsString());
				methodDeclarationVisitor.visit(n, methodDetails);
				arg.addMethodDetails(methodDetails);
				//System.out.println("Method> " + methodDetails.toString());
			};
			@Override
			public void visit(FieldDeclaration node, ClassOrInterfaceDetails arg) {
				super.visit(node, arg);
				//System.out.println("Visiting field " + node.toString());
				for (Node child: node.getChildNodes()) {
					//System.out.println(child + " " + child.getClass());
					if (child instanceof VariableDeclarator) { // TODO ok need to tidy this up but not entirely sure how - surely not another visitor!
						arg.addFieldDetails(((VariableDeclarator)child).resolve().getName(), ((VariableDeclarator)child).resolve().getType().toString());
					}
				}
			}
			
		};
		return visitor;		
	}
	private static VoidVisitor<FieldDetails> getFieldDeclarationVisitor() {
		VoidVisitor<FieldDetails> visitor = new VoidVisitorAdapter<FieldDetails>() {
		};
		return visitor;
	}
	private static VoidVisitor<FunctionalUnitDetails> getMethodDeclarationVisitor() { 
		VoidVisitor<FunctionalUnitDetails> visitor = new VoidVisitorAdapter<FunctionalUnitDetails>() {
			@Override
			public void visit(Parameter n, FunctionalUnitDetails arg) {
				super.visit(n, arg);
				String type = null;
				try {
					type = n.getType().resolve().describe();
				} catch (UnsupportedOperationException unsup) {
					// This happens at least when the type is from a type parameter. No idea
					// if it can happen for other reasons. There seems to be no way to determine
					// whether we're in this case other than catching the exception. n.getType().isTypeParameter() is asking a different question
					type = n.getTypeAsString();
				}
				arg.addParameter(n.getNameAsString(), type);
			};
		    @Override
		    public void visit(AssertStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
	    		v.incrStatementCount(n);
		    }
		    /**
		     * Do not count block statements. This method is only here for documentation purposes
		     */
		    @Override
		    public void visit(BlockStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    }

		    @Override
		    public void visit (BreakStmt n, FunctionalUnitDetails v) { 
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ContinueStmt  n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (DoStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (EmptyStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ExplicitConstructorInvocationStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ExpressionStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ForEachStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ForStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (IfStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (LabeledStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (LocalClassDeclarationStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
			
		    @Override
		    public void visit (LocalRecordDeclarationStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ReturnStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (SwitchStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (SynchronizedStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (ThrowStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (TryStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (UnparsableStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (WhileStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }
		    @Override
		    public void visit (YieldStmt n, FunctionalUnitDetails v) {
				super.visit(n, v);
		    	v.incrStatementCount(n);
		    }			
		};
		return visitor;
	}
	public static String tidyDouble(Double d) {
	    DecimalFormat formatter = new DecimalFormat("#0.000");
		if (d.isNaN()) {
			return "n/a";
		} else {
			return formatter.format(d);
		}
	}
	
}
