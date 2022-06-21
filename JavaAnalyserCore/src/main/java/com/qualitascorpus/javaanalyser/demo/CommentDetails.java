package com.qualitascorpus.javaanalyser.demo;

public class CommentDetails {

    private String _comment;

    private CommentType _type;

    public CommentDetails(String comment, CommentType type) {
        _comment = comment;
        _type = type;
    }

    public String toString() {
        return _comment;
    }

}
