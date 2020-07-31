/*
 * Author: Andy Wu
 * File Objective:
 * Simple Metrics to check counts of various words
 */

package org.apache.lucene.demo;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class SimpleMetrics {
    public SimpleMetrics() {

    }

    public static void printCount(Term term) {
        try {
            // Using index reader on to the index
            long docFreq = 0;
            long termFreq = 0;
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            termFreq = indexReader.totalTermFreq(term);
            docFreq = indexReader.docFreq(term);
            System.out.printf("Doc Freq:  %,d\n", docFreq);
            System.out.printf("Term Freq:  %,d\n", termFreq);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void userEntersWord(boolean keepRunning) {
        while(keepRunning) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter another word or press q to quit...");
            String userInput = in.nextLine();
            Term loopedTerm = new Term("contents", userInput);
            if(userInput.equals("q")) {
                keepRunning = false;
            }
            else {
                printCount(loopedTerm);
            }
        }
    }

    public static void main(String[] args) {
        String termValue = null;
        for (int i = 0; i < args.length; i++) {
            if ("-words".equals(args[i])) {
                termValue = args[i + 1];
                i++;
            }
        }
        boolean keepRunning = true;

        if(termValue == null) {
            userEntersWord(keepRunning);
        } else {
            Term newTerm = new Term("contents", termValue);
            printCount(newTerm);
            userEntersWord(keepRunning);
        }
    }
}
