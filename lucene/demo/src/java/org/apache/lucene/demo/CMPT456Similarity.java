/*
 * Author: Andy Wu
 * File Objective:
 * Similarity checking using tf and idf
 */

package org.apache.lucene.demo;
import org.apache.lucene.search.similarities.ClassicSimilarity;

// Class similarity extending Classic Simality
public class CMPT456Similarity extends ClassicSimilarity {
    @Override
    public float tf(float freq) {
        return (float)Math.sqrt(1 + freq);
    }

    @Override
    public float idf(long docFreq, long docCount) {
        return (float)(Math.log((docCount+2)/(double)(docFreq+2)) + 1.0);
    }
}
