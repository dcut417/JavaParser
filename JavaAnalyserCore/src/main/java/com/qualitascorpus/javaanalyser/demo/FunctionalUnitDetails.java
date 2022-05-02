package com.qualitascorpus.javaanalyser.demo;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;

/**
 * Represents a unit that is like a function and is part of a module, e.g. methods, constructors, static functions,
 * initialisers
 * @author ewan
 *
 */
public class FunctionalUnitDetails {
	private String _functionalUnitName;
	private List<String> _parameterTypes = new ArrayList<String>();
	private List<String> _parameterNames= new ArrayList<String>();
	private String _returnType;
	private String _moduleName;
	private long _statementCount;
	private boolean _isConstructor;
	private boolean _isStatic;
	private boolean _isInitialiser;
	
	public FunctionalUnitDetails(String moduleName, String methodName, String returnType) {
		_moduleName = moduleName;
		_functionalUnitName = methodName;
		_returnType = returnType;
		_statementCount = 0;
		_isConstructor = false;
	}
	/**
	 * Create a functional unit that is a constructor, meaning the functional unit name is the same
	 * as the module name and there's no return type
	 * @param moduleName
	 */
	public FunctionalUnitDetails(String moduleName) {
		_moduleName = moduleName;
		_functionalUnitName = moduleName;
		_returnType = null;
		_isConstructor = true;
		_statementCount = 0; 
	}
	public void addParameter(String name, String type) {
		_parameterNames.add(name);
		_parameterTypes.add(type);
	}
	public long getStatementCount() {
		return _statementCount;
	}
	public void incrStatementCount(Node node) {
		_statementCount++;
	}
	public boolean isConstructor() {
		return _isConstructor;
	}
	@Override
	public String toString() {
		String parameters = "(";
		for (int i = 0; i < _parameterNames.size(); i++) {
			parameters += _parameterNames.get(i) + ":" + _parameterTypes.get(i);
		}
		parameters += ")";
		return _moduleName + "#" + _functionalUnitName + parameters + "->" + _returnType + " (" + _statementCount + ")";
		
	}
}

