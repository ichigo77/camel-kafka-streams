package de.richter.kafka;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class KafkaStreamConsumer extends DefaultConsumer {
    private Endpoint streamEndpoint;
    private Processor streamProcessor;
    private KafkaStreams streams;

    public KafkaStreamConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);

        this.streamEndpoint = endpoint;
        this.streamProcessor = processor;
    }

    @Override
    protected void doStop() throws Exception {
        log.info("Stopping kafka streams for topic");
        super.doStop();
        streams.close();
    }

    @Override
    protected void doStart() throws Exception {
        log.info("Starting kafka streams for topic");
        super.doStart();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"wordCount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"kafka:9091");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName());



        Serde<String> stringSerde = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> stream = builder.stream("test", Consumed.with(stringSerde,stringSerde));

        KTable<String,Long> result = stream.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+"))).groupBy((key,value)->value).count();
        result.toStream().to("test-result", Produced.with(stringSerde,longSerde));

        streams = new KafkaStreams(builder.build(),props);
        streams.start();
    }


}
