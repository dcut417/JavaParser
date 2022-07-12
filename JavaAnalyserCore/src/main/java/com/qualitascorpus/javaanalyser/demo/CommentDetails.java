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
        String comment = _comment.toLowerCase(Locale.ROOT);
        if (comment.contains("add") || comment.contains("temporary") || comment.contains("support") || comment.contains("needs")) {
            _classifications.add(SATDClassification.REQUIREMENT);
        }
        if (comment.contains("comment")) {
            _classifications.add(SATDClassification.DOCUMENTATION);
        }
        if (comment.contains("move") || comment.contains("review")) {
            _classifications.add(SATDClassification.DESIGN);
            _classifications.add(SATDClassification.ARCHITECTURAL);
        }
        if (comment.contains("fix") || comment.contains("correct")) {
            _classifications.add(SATDClassification.DEFECT);
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
