package com.qualitascorpus.javaanalyser.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * Represents a set of functional units
 * @author ewan
 * @date 2021-09-23
 */
public class FunctionalUnits implements Iterable<FunctionalUnitDetails> {
	private List<FunctionalUnitDetails> _details;
	public FunctionalUnits() {
		 _details = new ArrayList<FunctionalUnitDetails>();
	}
	public void add(FunctionalUnitDetails fu) {
		_details.add(fu);
	}
	public SummaryStatistics getMethodStatistics() {
		SummaryStatistics stats = new SummaryStatistics();
		for (FunctionalUnitDetails fu: _details) {
			if (!fu.isConstructor()) {
				stats.addValue(fu.getStatementCount());
			}
		}
		return stats;
	}

	public SummaryStatistics getConstructorStatistics() {
		SummaryStatistics stats = new SummaryStatistics();
		for (FunctionalUnitDetails fu: _details) {
			if (fu.isConstructor()) {
				stats.addValue(fu.getStatementCount());
			}
		}
		return stats;
	}
	public double averageConstructorStatements() {
		SummaryStatistics stats = new SummaryStatistics();
		for (FunctionalUnitDetails fu: _details) {
			if (fu.isConstructor()) {
				stats.addValue(fu.getStatementCount());
			}
		}
		return stats.getMean();
	}
	public double averageMethodsStatements() {
		SummaryStatistics stats = new SummaryStatistics();
		for (FunctionalUnitDetails fu: _details) {
			if (!fu.isConstructor()) {
				stats.addValue(fu.getStatementCount());
			}
		}
		return stats.getMean();
	}
	public long numberConstructors() {
		long count = 0;
		for (FunctionalUnitDetails fu: _details) {
			if (fu.isConstructor()) {
				count++;
			}
		}
		return count;
	}
	public long numberMethods() {
		long count = 0;
		for (FunctionalUnitDetails fu: _details) {
			if (!fu.isConstructor()) {
				count++;
			}
		}
		return count;
	}
	@Override
	public Iterator<FunctionalUnitDetails> iterator() {
		return _details.iterator();
	}
}
