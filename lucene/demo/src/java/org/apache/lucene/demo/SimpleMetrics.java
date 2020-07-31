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

    public static void main(String[] args) {
        String termValue = null;
        for (int i = 0; i < args.length; i++) {
            if ("-words".equals(args[i])) {
                termValue = args[i + 1];
                i++;
            }
        }
        boolean keepRunning = true;

        long docFreq = 0;
        long termFreq = 0;
        System.out.println(termValue);
        Term newTerm = new Term("contents", termValue);
        try {
            // Using index reader on to the index
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            termFreq = indexReader.totalTermFreq(newTerm);
            docFreq = indexReader.docFreq(newTerm);
            System.out.printf("Doc Freq:  %,d\n", docFreq);
            System.out.printf("Term Freq:  %,d\n", termFreq);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        while(keepRunning) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter another word or press q to quit...");
            String userInput = in.nextLine();
            Term loopedTerm = new Term("contents", userInput);
            if(userInput.equals("q")) {
                keepRunning = false;
            }
            else {
                try {
                    // Using index reader on to the index
                    IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
                    termFreq = indexReader.totalTermFreq(loopedTerm);
                    docFreq = indexReader.docFreq(loopedTerm);
                    System.out.printf("Doc Freq:  %,d\n", docFreq);
                    System.out.printf("Term Freq:  %,d\n", termFreq);
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }
}
