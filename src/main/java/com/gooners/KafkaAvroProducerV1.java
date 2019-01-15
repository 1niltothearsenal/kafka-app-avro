package com.gooners;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaAvroProducerV1 {

    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.ACKS_CONFIG,"1");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG,"10");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.setProperty(SCHEMA_REGISTRY_URL,"http://127.0.0.1:8081");

        KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<String, Customer>(properties);
        String topic = "customer-avro";

        Customer customer = Customer.newBuilder()
                .setFirstName("JOhn")
                .setLastName("Doe")
                .setAge(26)
                .setHeight(185.5f)
                .setWeight(85.6f)
                .setAutomatedEmail(false)
                .build();

        ProducerRecord<String,Customer> producerRecord = new ProducerRecord<String, Customer>(
                topic,customer
        );

        kafkaProducer.send(producerRecord, (recordMetadata,exception) ->
        {
            if(exception == null){
                System.out.println("SUCCESS!");
                System.out.println(recordMetadata.toString());
            }else {
                exception.printStackTrace();
            }

        });

        //To make sure the record is indeed sent
        kafkaProducer.flush();

        kafkaProducer.close();

    }

}
