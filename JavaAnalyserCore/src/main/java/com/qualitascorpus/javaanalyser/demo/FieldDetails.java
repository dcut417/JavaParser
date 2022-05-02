package com.qualitascorpus.javaanalyser.demo;

public class FieldDetails {
	private String _fieldName;
	private String _type;
	public FieldDetails(String fieldName, String type) {
		_fieldName = fieldName;
		_type = type;
	}
	public String toString() {
		return _fieldName + ":" + _type;
	}
}
