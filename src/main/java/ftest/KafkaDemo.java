package ftest;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;


public class KafkaDemo
{
    private static final ObjectMapper OM = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(KafkaDemo.class);
    public static void main(String[] args) throws Exception
    {
        final StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();



        Properties p = new Properties();
        p.setProperty("bootstrap.servers", "localhost:9092");

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>("kd-sample-topic", new SimpleStringSchema(), p);
        DataStream<String> stream = env.addSource(kafkaSource);

        KeyedStream<Customer, String> customerPerCountryStream = stream.map(data -> {
            try {
                LOG.info("reading data: " + data);
                return OM.readValue(data, Customer.class);
            } catch (Exception e) {
                LOG.info("exception reading data: " + data);
                return null;
            }
        }).filter(Objects::nonNull).keyBy(Customer::getCountry);


        DataStream<Tuple2<String, Long>> result = customerPerCountryStream.timeWindow(Time.seconds(5))
                .aggregate(new CustomerAggregatorByCountry());

        result.print();

        env.execute("CustomerRegistrationApp");


    }

}


