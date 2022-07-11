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
            comment.print();
        }
    }
}
