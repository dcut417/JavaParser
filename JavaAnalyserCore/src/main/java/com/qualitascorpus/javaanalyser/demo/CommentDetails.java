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
        } else if (_comment.contains("FIXME")) {
            comment = _comment.substring(_comment.indexOf("FIXME") + 1).toLowerCase(Locale.ROOT);
        } else {
            comment = _comment.toLowerCase(Locale.ROOT);
        }
        if (comment.contains("add") || comment.contains("temporary") || comment.contains("support") || comment.contains("need")
                || comment.contains("also") || comment.contains("handle") || comment.contains("auto-generated")
                || comment.contains(" use ") || comment.contains("implement ") || comment.contains("accomodate")
                || comment.contains("introduce") || comment.contains("extend") || comment.contains("set up")
                || comment.contains("finish") || comment.contains("placeholder")) {
            _classifications.add(SATDClassification.REQUIREMENT);
        }
        if (comment.contains("comment") || comment.contains("description") || comment.contains("document")) {
            _classifications.add(SATDClassification.DOCUMENTATION);
        }
        if (comment.contains("move") || comment.contains("review") || comment.contains("change")
                || comment.contains("refactor") || comment.contains("obsolete") || comment.contains("do we")
                || comment.contains("lazy") || comment.contains("remove") || comment.contains("mess")
                || comment.contains("really") || comment.contains("why") || comment.contains("belong")
                || comment.contains("better") || comment.contains("hack") || comment.contains("improve")
                || comment.contains("nasty") || comment.contains("consider") || comment.contains("replace")
                || comment.contains("review") || comment.contains("clean") || comment.contains("check")
                || comment.contains("convenient") || comment.contains("link to") || comment.contains("structure")
                || comment.contains("expose") || comment.contains("delete") || comment.contains("shift")
                || comment.contains("make the") || comment.contains("make this") || comment.contains("rearrange")
                || comment.contains("encapsulate") || comment.contains("never") || comment.contains("best way")
                || comment.contains("awkward") || comment.contains("make") || comment.contains("ensure")
                || comment.contains("should we") || comment.contains("silly") || comment.contains("complex")
                || comment.contains("optimi") || comment.contains("provide") || comment.contains("avoid")
                || comment.contains("we may") || comment.contains("generalize") || comment.contains("approach")
                || comment.contains("maybe") || comment.contains("for now") || comment.contains("could be")
                || comment.contains("cast ") || comment.contains("organize") || comment.contains("convert")
                || comment.contains("clone") || comment.contains("duplicate") || comment.contains("complicated")) {
            _classifications.add(SATDClassification.DESIGN);
        }
        if (comment.contains("move") || comment.contains("review") || comment.contains("change")
                || comment.contains("refactor") || comment.contains("obsolete") || comment.contains("do we")
                || comment.contains("lazy") || comment.contains("remove") || comment.contains("mess")
                || comment.contains("really") || comment.contains("why") || comment.contains("belong")
                || comment.contains("better") || comment.contains("hack") || comment.contains("improve")
                || comment.contains("nasty") || comment.contains("consider") || comment.contains("replace")
                || comment.contains("review") || comment.contains("clean") || comment.contains("check")
                || comment.contains("convenient") || comment.contains("link to") || comment.contains("structure")
                || comment.contains("expose") || comment.contains("delete") || comment.contains("shift")
                || comment.contains("make the") || comment.contains("make this") || comment.contains("rearrange")
                || comment.contains("encapsulate") || comment.contains("never") || comment.contains("best way")
                || comment.contains("awkward") || comment.contains("make") || comment.contains("ensure")
                || comment.contains("should we") || comment.contains("silly")
                || comment.contains("optimi") || comment.contains("provide") || comment.contains("avoid")
                || comment.contains("we may") || comment.contains("generalize") || comment.contains("approach")
                || comment.contains("maybe") || comment.contains("for now") || comment.contains("could be")
                || comment.contains("cast ") || comment.contains("organize") || comment.contains("convert")) {
            _classifications.add(SATDClassification.ARCHITECTURAL);
        }
        if (comment.contains("fix") || comment.contains("correct") || comment.contains("right") || comment.contains("work")
                || comment.contains("should not") || comment.contains("issue") || comment.contains("fail")
                || comment.contains("error") || comment.contains("bug") || comment.contains("wrong")
                || comment.contains("problem") || comment.contains("do not") || comment.contains("verify")
                || comment.contains("relevant") || comment.contains("dont") || comment.contains("don't")) {
            _classifications.add(SATDClassification.DEFECT);
        }
        if (comment.contains("merge") || comment.contains("version")) {
            _classifications.add(SATDClassification.VERSIONING);
        }
        if (comment.contains("test") || comment.contains("junit")) {
            _classifications.add(SATDClassification.TEST);
        }
        if (comment.contains("clone") || comment.contains("duplicate") || comment.contains("remove")) {
            _classifications.add(SATDClassification.CODE);
        }
        if (comment.contains("refactor") || comment.contains("complex") || comment.contains("complicated")) {
            _classifications.add(SATDClassification.BUILD);
        }
        /*if (_comment.length() < 8) {
            _classifications.add(SATDClassification.UNCLEAR);
        }*/
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
