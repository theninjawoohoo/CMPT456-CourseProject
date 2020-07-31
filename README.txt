Apache Lucene/Solr

lucene/ is a search engine library
solr/ is a search engine server that uses lucene

To compile the sources run 'ant compile'
To run all the tests run 'ant test'
To setup your ide run 'ant idea', 'ant netbeans', or 'ant eclipse'
For Maven info, see dev-tools/maven/README.maven

For more information on how to contribute see:
http://wiki.apache.org/lucene-java/HowToContribute
http://wiki.apache.org/solr/HowToContribute

run image (stop container first)
docker run -t -d --name "cmpt456"
docker cp ./lucene/demo/src/java/org/apache/lucene/demo/HtmlIndexFiles.java 0dc269eda6bf:/lucene-solr/lucene/demo/src/java/org/apache/lucene/demo
docker cp ./lucene/demo/build.xml 0dc269eda6bf:/lucene-solr/lucene/demo/build.xml
docker cp ./lucene/demo/src/java/org/apache/lucene/demo/CMPT456Analyzer.java 0dc269eda6bf:/lucene-solr/lucene/demo/src/java/org/apache/lucene/demo
docker cp ./lucene/demo/build.xml 0dc269eda6bf:/lucene-solr/lucene/demo/build.xml
docker cp ./lucene/demo/src/java/org/apache/lucene/demo/SimpleMetrics.java 0dc269eda6bf:/lucene-solr/lucene/demo/src/java/org/apache/lucene/demo
docker cp ./lucene/demo/build.xml 0dc269eda6bf:/lucene-solr/lucene/demo/build.xml
docker exec -it new-cmpt-456 /bin/bash



ant -f lucene/core/build.xml; ant -f lucene/demo/build.xml


to restart a container
docker start id

docker run --name new-cmpt-456 -it -d \
-v $(pwd):/lucene-solr \
cmpt456-lucene-solr:6.6.7

new-cmpt-456

