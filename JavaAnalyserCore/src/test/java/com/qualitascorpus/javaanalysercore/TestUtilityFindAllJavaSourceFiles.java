package com.qualitascorpus.javaanalysercore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Set;

import org.junit.Test;

import com.qualitascorpus.javaanalyser.core.Utility;

/**
 * 
 */
public class TestUtilityFindAllJavaSourceFiles 
{
    @Test
    public void noRootsSpecified() throws IOException {
    	Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots();
    	assertEquals(0, paths.size());
    }

    @Test
    public void nonExistentJavaSourceFile() throws IOException {
    	String root = "ThisFileDoesNotExist.java";
    	try {
    		Utility.findAllJavaSourceFilesFromRoots(root);
    		fail("Expected exception");
    	} catch (NoSuchFileException nsfex) {
    	}
    }
    @Test
    public void nonExistentDirectory() throws IOException {
    	String root = "a/b/c/ThisDirectoryDoesNotExist/";
    	try {
    		Utility.findAllJavaSourceFilesFromRoots(root);
    		fail("Expected exception");
    	} catch (NoSuchFileException nsfex) {
    	}
    }

    @Test
    public void singleJavaSource() throws IOException {
    	// Pretty sure this Java source file exists
    	Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots("src/test/java/com/qualitascorpus/javaanalysercore/TestUtilityFindAllJavaSourceFiles.java");
    	assertEquals(1, paths.size());
    }

    @Test
    public void singleNonEmptyDirectory() throws IOException {
    	// Pretty sure this directory exists. problem is, the number of files in it will change over time. But leave it as is for now
    	Set<Path> paths = Utility.findAllJavaSourceFilesFromRoots("src/test/java/com/qualitascorpus/javaanalysercore");
    	assertEquals(2, paths.size());
    }

}
