package de.richter.kafka;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultEndpoint;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class KafkaStreamEndpoint extends DefaultEndpoint {

    public KafkaStreamEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    public KafkaStreamEndpoint() {
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new RuntimeCamelException("This is a test");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new KafkaStreamConsumer(this,processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
