/*
 * Author: Andy Wu
 * File Objective:
 * Parse HTML files and tokenize it
 */

package org.apache.lucene.demo;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.xml.sax.SAXException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.benchmark.byTask.feeds.DemoHTMLParser;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class HtmlIndexFiles {
    //Setter and getters


    //Method to index the data
    public static void indexer(Path docDir, IndexWriter writer) throws IOException {
        try (InputStream stream = Files.newInputStream(docDir)) {
            // Here is the directory we will index the tokens to.
            System.out.println("Adding... '" + docDir + "'...");
            Document doc = new Document();
            Field pathField = new StringField("path", docDir.toString(), Field.Store.YES);
            doc.add(pathField);
            try {
                DemoHTMLParser.Parser parser = new DemoHTMLParser.Parser(new InputStreamReader(stream, StandardCharsets.UTF_8));
                doc.add(new org.apache.lucene.document.TextField("Title", parser.title, Field.Store.YES));
                doc.add(new org.apache.lucene.document.TextField("contents", parser.body, Field.Store.YES));
            } catch (SAXException exception) {
                exception.printStackTrace();
            }
            writer.addDocument(doc);
        }
    }

    // Test driver for the HTMLIndexer
    public static void main(String[] args) throws IOException, SAXException {
        String docsPath = null;
        for (String arg : args) {
            System.out.println(arg);
        }

        for (int i = 0; i < args.length; i++) {
            if ("-docs".equals(args[i])) {
                docsPath = args[i + 1];
                i++;
            }
        }
        if (docsPath == null) {
            System.err.println("Docs Not Found");
            System.exit(1);
        }

        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }


        String indexPath = "index";
        Analyzer analyzer = new StandardAnalyzer();
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new org.apache.lucene.index.IndexWriter(dir, iwc);


        if (Files.isDirectory(docDir)) {
            Files.walkFileTree(docDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexer(file, writer);
                    } catch (IOException e) {
                        // don't index files that can't be read.
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexer(docDir, writer);
        }
        writer.close();
    }
}

