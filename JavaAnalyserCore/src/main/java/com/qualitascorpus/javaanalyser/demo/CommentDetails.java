package com.qualitascorpus.javaanalyser.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommentDetails {

    private String _comment;

    private CommentType _type;

    private String _position;

    private String _fullyQualifiedName;

    private List<SATDClassification> _classifications = new ArrayList<SATDClassification>();

    public CommentDetails(String comment, CommentType type, String position, String fqn) {
        _comment = comment;
        _type = type;
        _position = position;
        _fullyQualifiedName = fqn;
        classify();
    }

    private void classify() {
        String comment;
        if (_comment.contains("TODO")) {
            comment = _comment.substring(_comment.indexOf("TODO")).toLowerCase(Locale.ROOT);
        } else {
            comment = _comment.toLowerCase(Locale.ROOT);
        }
        if (comment.contains("add") || comment.contains("temporary") || comment.contains("support") || comment.contains("need")
                || comment.contains("also") || comment.contains("handle") || comment.contains("auto-generated")
                || comment.contains("use ") || comment.contains("implement ") || comment.contains("accomodate")
                || comment.contains("introduce") || comment.contains("extend")) {
            _classifications.add(SATDClassification.REQUIREMENT);
        }
        if (comment.contains("comment") || comment.contains("description")) {
            _classifications.add(SATDClassification.DOCUMENTATION);
        }
        if (comment.contains("move") || comment.contains("review") || comment.contains("change")
                || comment.contains("refactor") || comment.contains("obsolete") || comment.contains("do we")
                || comment.contains("lazy") || comment.contains("remove") || comment.contains("mess")
                || comment.contains("really") || comment.contains("why") || comment.contains("belong")
                || comment.contains("better") || comment.contains("hack") || comment.contains("improve")
                || comment.contains("nasty") || comment.contains("consider") || comment.contains("replace")
                || comment.contains("review") || comment.contains("clean") || comment.contains("check")) {
            _classifications.add(SATDClassification.DESIGN);
            _classifications.add(SATDClassification.ARCHITECTURAL);
        }
        if (comment.contains("fix") || comment.contains("correct") || comment.contains("right") || comment.contains("work")
                || comment.contains("should not") || comment.contains("issue") || comment.contains("fail")
                || comment.contains("error")) {
            _classifications.add(SATDClassification.DEFECT);
        }
        if (comment.contains("merge") || comment.contains("version")) {
            _classifications.add(SATDClassification.VERSIONING);
        }
        if (comment.contains("test")) {
            _classifications.add(SATDClassification.TEST);
        }
        if (comment.contains("clone") || comment.contains("duplicate")) {
            _classifications.add(SATDClassification.CODE);
        }
        if (comment.contains("refactor")) {
            _classifications.add(SATDClassification.BUILD);
        }
    }

    public String getComment() {
        return _comment;
    }

    public CommentType getType() {
        return _type;
    }

    public String getPosition() {
        return _position;
    }

    public String getFQN() {
        return _fullyQualifiedName;
    }

    public List<SATDClassification> getClassifications() {
        return _classifications;
    }

    public String toString() {
        return _comment;
    }

    public void print() {
        System.out.println("Class: " + _fullyQualifiedName);
        System.out.println("Type: " + _type);
        System.out.println("Comment: " + _comment);
        System.out.println("Position: " + _position);
        System.out.println("Classification(s): " + classificationsToString());
        System.out.println();
    }

    private String classificationsToString() {
        if (_classifications.isEmpty()) {
            return "None";
        } else {
            String all = "";
            for (SATDClassification classification: _classifications) {
                all = all.concat(classification.toString() + ", ");
            }
            return all;
        }
    }

}
