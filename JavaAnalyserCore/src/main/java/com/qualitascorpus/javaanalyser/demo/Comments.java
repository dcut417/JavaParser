package com.qualitascorpus.javaanalyser.demo;

import java.util.ArrayList;
import java.util.List;

public class Comments {

    private List<CommentDetails> commentList = new ArrayList<CommentDetails>();

    public Comments() {}

    public void add(CommentDetails comment) {
        commentList.add(comment);
    }

    public void remove(CommentDetails comment) {
        commentList.remove(comment);
    }

    public void print() {
        for (CommentDetails comment : commentList) {
            System.out.println(commentList.indexOf(comment));
            comment.print();
        }
        System.out.println("Total SATD Comments: " + commentList.size());
        printClassificationCount();
    }

    private void printClassificationCount() {
        int designCount = 0;
        int defectCount = 0;
        int documentationCount = 0;
        int requirementCount = 0;
        int testCount = 0;
        int architecturalCount = 0;
        int codeCount = 0;
        int buildCount = 0;
        int infrastructureCount = 0;
        int versioningCount = 0;
        int unclearCount = 0;
        int noneCount = 0;
        int designArchitectural = 0;
        int designBuild = 0;
        int designCode = 0;
        for (CommentDetails comment : commentList) {
            List<SATDClassification> classList = comment.getClassifications();
            if (classList.contains(SATDClassification.DESIGN)) {
                designCount++;
            }
            if (classList.contains(SATDClassification.DEFECT)) {
                defectCount++;
            }
            if (classList.contains(SATDClassification.DOCUMENTATION)) {
                documentationCount++;
            }
            if (classList.contains(SATDClassification.REQUIREMENT)) {
                requirementCount++;
            }
            if (classList.contains(SATDClassification.TEST)) {
                testCount++;
            }
            if (classList.contains(SATDClassification.ARCHITECTURAL)) {
                architecturalCount++;
            }
            if (classList.contains(SATDClassification.CODE)) {
                codeCount++;
            }
            if (classList.contains(SATDClassification.BUILD)) {
                buildCount++;
            }
            if (classList.contains(SATDClassification.INFRASTRUCTURE)) {
                infrastructureCount++;
            }
            if (classList.contains(SATDClassification.VERSIONING)) {
                versioningCount++;
            }
            if (classList.contains(SATDClassification.UNCLEAR)) {
                unclearCount++;
            }
            if (classList.isEmpty()) {
                noneCount++;
            }
            if (classList.contains(SATDClassification.DESIGN) && classList.contains(SATDClassification.ARCHITECTURAL)) {
                designArchitectural++;
            }
            if (classList.contains(SATDClassification.DESIGN) && classList.contains(SATDClassification.BUILD)) {
                designBuild++;
            }
            if (classList.contains(SATDClassification.DESIGN) && classList.contains(SATDClassification.CODE)) {
                designCode++;
            }
        }
        System.out.println("Design: " + designCount);
        System.out.println("Defect: " + defectCount);
        System.out.println("Documentation: " + documentationCount);
        System.out.println("Requirement: " + requirementCount);
        System.out.println("Test: " + testCount);
        System.out.println("Architectural: " + architecturalCount);
        System.out.println("Build: " + buildCount);
        System.out.println("Code: " + codeCount);
        System.out.println("Infrastructure: " + infrastructureCount);
        System.out.println("Versioning: " + versioningCount);
        System.out.println("Unclear: " + unclearCount);
        System.out.println("Unclassified: " + noneCount);

        System.out.println("Design + Architectural: " + designArchitectural);
        System.out.println("Design + Build: " + designBuild);
        System.out.println("Design + Code: " + designCode);
    }

    public int getSize() {
        return commentList.size();
    }
}
