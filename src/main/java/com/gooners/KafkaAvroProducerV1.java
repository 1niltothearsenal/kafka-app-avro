package com.gooners;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaAvroProducerV1 {

    private static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    private static final String ACKS = "acks";
    private static final String RETRIES = "retries";
    private static final String KEY_SERIALIZER = "key.serializer";
    private static final String VALUE_SERIALIZER = "value.serializer";
    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";



    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS,"127.0.0.1:9092");
        properties.setProperty(ACKS,"1");
        properties.setProperty(RETRIES,"10");
        properties.setProperty(KEY_SERIALIZER, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER, KafkaAvroDeserializer.class.getName());
        properties.setProperty(SCHEMA_REGISTRY_URL,"http://127.0.0.1:8081");


//        KafkaProducer<String,Customer> kafkaProducer = new KafkaProducer<String, Customer>(properties);
//        String topic = "customer-avro";
//
//
//        Customer customer = ??;
//
//        ProducerRecord<String,Customer> producerRecord = new ProducerRecord<String, Customer>(
//                topic,customer
//        );


    }

}
