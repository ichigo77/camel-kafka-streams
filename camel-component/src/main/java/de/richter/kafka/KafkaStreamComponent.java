package de.richter.kafka;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

public class KafkaStreamComponent extends DefaultComponent
{
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new KafkaStreamEndpoint(uri,this);
        setProperties(endpoint,parameters);
        return endpoint;
    }
}
