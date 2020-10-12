/*
package ftest;
*/
/* java imports *//*

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;
import org.apache.flink.util.Collector;

import java.util.Properties;

*/
/* flink imports *//*

*/
/* parser imports *//*

*/
/* flink streaming twittter imports *//*


public class TwitterData
{
    public static void main(String[] args) throws Exception
    {
        final StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();

        Properties twitterCredentials = new Properties();
        twitterCredentials.setProperty(TwitterSource.CONSUMER_KEY, "IOWjXjTgze8W6");
        twitterCredentials.setProperty(TwitterSource.CONSUMER_SECRET, "fLXizGTfhs5ToARABKnJUNBDLT7N35");
        twitterCredentials.setProperty(TwitterSource.TOKEN, "40PyXZrRfPQ5JhU0KlxF0ySbfS");
        twitterCredentials.setProperty(TwitterSource.TOKEN_SECRET, "CWoDEjE4jQUC9aRx8KkE");

        System.out.println("fetching data");
        DataStream<String> twitterData = env.addSource(new TwitterSource(twitterCredentials));


        twitterData.flatMap(new TweetParser()).writeAsText("/Users/z002x5k/Downloads/flink/tweet.txt");

        env.execute("Twitter Example");
    }

    public static class TweetParser	implements FlatMapFunction<String, Tuple2<String, Integer>>
    {

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception
        {
            System.out.println("Inside mapper");
            ObjectMapper jsonParser = new ObjectMapper();
            JsonNode node = jsonParser.readValue(value, JsonNode.class);

            boolean isEnglish =
                    node.has("user") &&
                            node.get("user").has("lang") &&
                            node.get("user").get("lang").asText().equals("en");

            boolean hasText = node.has("text");

            if (isEnglish && hasText)
            {
                String tweet = node.get("text").asText();

                out.collect(new Tuple2<String, Integer>(tweet, 1));
            }
        }    }}

*/
