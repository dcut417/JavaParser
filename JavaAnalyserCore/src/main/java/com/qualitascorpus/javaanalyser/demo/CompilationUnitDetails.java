package com.qualitascorpus.javaanalyser.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.qualitascorpus.text.TabularOutput;

/**
 * Represents all the details associated with a compilation unit (i.e. source file)
 * @author ewan
 * @date 20210923
 */
public class CompilationUnitDetails {
	private List<ClassOrInterfaceDetails> _details;
	private String _path;
	public CompilationUnitDetails(String path) {
		_path = path;
		_details = new ArrayList<ClassOrInterfaceDetails>();
	}
	public void add(ClassOrInterfaceDetails unit) {
		_details.add(unit);
	}
	public void statistics(TabularOutput output) {
		for (ClassOrInterfaceDetails details: _details) {
			details.statistics(output);
		}		
	}
	public long totalMethods() {
		long totalMethods = 0;
		for (ClassOrInterfaceDetails details: _details) {
			if (details.numberMethods() == 0) {
				System.out.println(">>>>" + details);
			}
			totalMethods += details.numberMethods();
		}
		return totalMethods;
	}
	public String toString() {
		return _path;
	}
	public void summaryStatistics(SummaryStatistics stats) {
		long totalMethods = 0;
		for (ClassOrInterfaceDetails details: _details) {
			if (details.isClass()) {
				stats.addValue(details.numberMethods());
			}
			/*
			if (details.numberMethods() == 0) {
				System.out.println(">>>>" + details);
			}
			*/
		}
	}
}
