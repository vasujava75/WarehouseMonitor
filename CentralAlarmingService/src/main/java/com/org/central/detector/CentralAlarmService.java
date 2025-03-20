package com.org.central.detector;

import com.org.central.detector.config.KafkaConsumerConfig;
import com.org.central.detector.utility.EventsData;
import com.org.central.detector.utility.EventsParserUtility;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

public class CentralAlarmService {

    public static void main(String[] args) {
        KafkaReceiver<String, String> receiver = KafkaConsumerConfig.createReceiver();

        Flux<ReceiverRecord<String, String>> kafkaFlux = receiver.receive();

        kafkaFlux
            .doOnNext(record -> {
                EventsData eventsData = EventsParserUtility.parseSensorData(record.value());
                System.out.println("Parsed EventsData: " + eventsData);
                record.receiverOffset().acknowledge();
            })
            .doOnError(e -> System.err.printf("Error occurred: %s%n", e.getMessage()))
            .subscribe();
    }
}
