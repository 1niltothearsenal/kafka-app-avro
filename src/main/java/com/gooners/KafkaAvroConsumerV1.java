package com.gooners;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaAvroConsumerV1 {

    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
    private static final String SPECIFIC_AVRO_READER = "specific.avro.reader";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"my-avro-consumer");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.setProperty(SCHEMA_REGISTRY_URL,"http://127.0.0.1:8080");
        properties.setProperty(SPECIFIC_AVRO_READER,"true");

        KafkaConsumer<String, Customer> consumer = new KafkaConsumer<String, Customer>(properties);
        String topic = "customer-avro";

        consumer.subscribe(Collections.singleton(topic));

        System.out.println("Waiting for data...");

        while (true){
            ConsumerRecords<String, Customer> records = consumer.poll(500);
            for(ConsumerRecord<String,Customer> record:records){
                Customer customer = record.value();
                System.out.println(customer);

            }
            consumer.commitSync();
            consumer.close();
        }

    }
}
