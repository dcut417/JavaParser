package com.qualitascorpus.javaanalyser.demo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.qualitascorpus.text.TabularOutput;

public class ClassOrInterfaceDetails {
	private String _classOrInterfaceName;
	private List<FieldDetails> _detailsOfAllFields = new ArrayList<FieldDetails>();
	private FunctionalUnits _functionalUnits;
	private Kind _kind;
	public enum Kind {
		Class, Interface, Enum, Annotation;
	}
	public ClassOrInterfaceDetails(String name, boolean isInterface, boolean isEnum, boolean isAnnotation) {
		_classOrInterfaceName = name;
		_functionalUnits = new FunctionalUnits();
		_kind = Kind.Class;
		if (isInterface) {
			_kind = Kind.Interface;
		} else if (isEnum) {
			_kind = Kind.Enum;
		} else if (isAnnotation) {
			_kind = Kind.Annotation;
		}
//		System.out.println("ClassOrInterface:" + _classOrInterfaceName);
	}
	public void addFieldDetails(String name, String type) {
		FieldDetails details = new FieldDetails(name, type);
		_detailsOfAllFields.add(details);
		
	}
	public String getFQN() {
		return _classOrInterfaceName;
	}
	public void addMethodDetails(FunctionalUnitDetails methodDetails) {
		_functionalUnits.add(methodDetails);
//		System.out.println("    " + methodDetails.toString());
	}
    public static void statisticsHeader(TabularOutput output) {
    	output.newRow();
    	output.fillCurrentCellAsHeader("Unit name", true);
    	output.fillCurrentCellAsHeader("Num Fields", true);
    	output.fillCurrentCellAsHeader("Num Constructors", true);
    	output.fillCurrentCellAsHeader("Ave Statements/Constructor", true);
    	output.fillCurrentCellAsHeader("StdDev Statements/Constructor", true);
    	output.fillCurrentCellAsHeader("Constructor Statement Count...", true);
    	output.fillCurrentCellAsHeader("Num Methods", true);
    	output.fillCurrentCellAsHeader("Ave Statements/Method", true);
    	output.fillCurrentCellAsHeader("StdDevStatements/Method", true);
    	output.fillCurrentCellAsHeader("Method Statement Count...", true);
    	output.fillCurrentCellAsHeader("Total Statements", true);
    	output.fillCurrentCellAsHeader("Unit 'size'", true);
	}
	public void statistics(TabularOutput output) {
		long moduleSize = 1; // Class header
		long sumFunctionalUnitStatementCount = 0;
		for (FieldDetails details: _detailsOfAllFields) {
			moduleSize += 1; // Count each field once
		}
		String individualConstructorStatementCount = "";
		String individualMethodStatementCount = "";
		String cSep = "";
		String mSep = "";
		for (FunctionalUnitDetails details: _functionalUnits) {
			moduleSize += 1; // Method header
			moduleSize += details.getStatementCount();
			sumFunctionalUnitStatementCount += details.getStatementCount();
			if (details.isConstructor()) {
				individualConstructorStatementCount += cSep + details.getStatementCount();
				cSep = ", ";
			} else {
				individualMethodStatementCount += mSep + details.getStatementCount();
				mSep = ", ";
			}
		}
		output.newRow();
		SummaryStatistics methodStats = _functionalUnits.getMethodStatistics();
		SummaryStatistics constructorStats = _functionalUnits.getConstructorStatistics();
		output.fillCurrentCellContents(_classOrInterfaceName+"");
		output.fillCurrentCellContents(_detailsOfAllFields.size()+"");
		output.fillCurrentCellContents(constructorStats.getN()+"");
		output.fillCurrentCellContents(JavaParserStatementCount.tidyDouble(constructorStats.getMean()));
		output.fillCurrentCellContents(JavaParserStatementCount.tidyDouble(constructorStats.getStandardDeviation()));
		output.fillCurrentCellContents(individualConstructorStatementCount);
		output.fillCurrentCellContents(methodStats.getN()+"");
		output.fillCurrentCellContents(JavaParserStatementCount.tidyDouble(methodStats.getMean()));
		output.fillCurrentCellContents(JavaParserStatementCount.tidyDouble(methodStats.getStandardDeviation()));
		output.fillCurrentCellContents(individualMethodStatementCount);
		output.fillCurrentCellContents(sumFunctionalUnitStatementCount+"");
		output.fillCurrentCellContents(moduleSize+"");
	}
	

	@Override
	public String toString() {
		String result = _classOrInterfaceName + "\n";
		for (FieldDetails details: _detailsOfAllFields) {
			result += "    " + details.toString() + "\n";
		}
		for (FunctionalUnitDetails details: _functionalUnits) {
			result += "    " + details.toString() + "\n";
		}
		return result;
	}
	public long numberMethods() {
		return _functionalUnits.numberMethods();
	}
	public boolean isClass() {
		return _kind == Kind.Class;
	}
}
