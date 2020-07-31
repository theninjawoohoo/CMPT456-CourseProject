/*
 * Author: Andy Wu
 * File Objective:
 * Create your own custom analyzer
 */

package org.apache.lucene.demo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;

// Code taken from the standard analyzer class with a few modifications
public class CMPT456Analyzer extends StopwordAnalyzerBase {

    /** An unmodifiable set containing some common English words that are not usually useful
     for searching.*/
    public static final CharArraySet ENGLISH_STOP_WORDS_SET;

    static {
        String path = "lucene/demo/src/java/org/apache/lucene/demo/stopwords.txt";
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<String> stopWords = lines;
        final CharArraySet stopSet = new CharArraySet(stopWords, false);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }

    /** Default maximum allowed token length */
    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

    /** An unmodifiable set containing some common English words that are usually not
     useful for searching. */
    public static final CharArraySet STOP_WORDS_SET = ENGLISH_STOP_WORDS_SET;

    /** Builds an analyzer with the given stop words.
     * @param stopWords stop words */
    public CMPT456Analyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    /** Builds an analyzer with the default stop words ({@link #STOP_WORDS_SET}).
     */
    public CMPT456Analyzer() {
        this(STOP_WORDS_SET);
    }

    /** Builds an analyzer with the stop words from the given reader.
     * @see WordlistLoader#getWordSet(Reader)
     * @param stopwords Reader to read stop words from */
    public CMPT456Analyzer(Reader stopwords) throws IOException {
        this(loadStopwordSet(stopwords));
    }

    /**
     * Set the max allowed token length.  Tokens larger than this will be chopped
     * up at this token length and emitted as multiple tokens.  If you need to
     * skip such large tokens, you could increase this max length, and then
     * use {@code LengthFilter} to remove long tokens.  The default is
     * {@link org.apache.lucene.analysis.standard.StandardAnalyzer#DEFAULT_MAX_TOKEN_LENGTH}.
     */
    public void setMaxTokenLength(int length) {
        maxTokenLength = length;
    }

    /** Returns the current maximum token length
     *
     *  @see #setMaxTokenLength */
    public int getMaxTokenLength() {
        return maxTokenLength;
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        src.setMaxTokenLength(maxTokenLength);
        TokenStream tok = new StandardFilter(src);
        tok = new LowerCaseFilter(tok);
        tok = new StopFilter(tok, stopwords);
        return new TokenStreamComponents(src, new PorterStemFilter(tok)) {
            @Override
            protected void setReader(final Reader reader) {
                // So that if maxTokenLength was changed, the change takes
                // effect next time tokenStream is called:
                src.setMaxTokenLength(CMPT456Analyzer.this.maxTokenLength);
                super.setReader(reader);
            }
        };
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        TokenStream result = new StandardFilter(in);
        result = new LowerCaseFilter(result);
        return result;
    }

    public static void main(String[] args) {
        boolean isRunning = true;
        while(isRunning) {
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter a query to tokenize and stem, results will be printed, q to quit");
            String userInput = in.nextLine();
            if(userInput.equals("q")) {
                isRunning = false;
            }
            else {
                List<String> result = new ArrayList<String>();
                CMPT456Analyzer myAnalyzer = new CMPT456Analyzer();
                try {
                    TokenStream tokenStream = myAnalyzer.tokenStream(null, new StringReader(userInput));
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        result.add(tokenStream.getAttribute(org.apache.lucene.analysis.tokenattributes.CharTermAttribute.class).toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for(String token : result) {
                    System.out.println(token);
                }
            }
        }
    }
}
