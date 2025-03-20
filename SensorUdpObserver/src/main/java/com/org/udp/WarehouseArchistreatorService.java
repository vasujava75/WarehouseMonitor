package com.org.udp;

import com.org.udp.kafka.producer.KafkaProducerConfig;
import com.org.udp.model.EventsData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Mono;
import reactor.netty.udp.UdpServer;
import reactor.netty.udp.UdpInbound;
import reactor.netty.udp.UdpOutbound;

import java.util.concurrent.CountDownLatch;

public class WarehouseArchistreatorService {

    private static final String TOPIC = "sensor-data";
    private KafkaProducer<String, String> kafkaProducer;

    public WarehouseArchistreatorService() {
        this.kafkaProducer = KafkaProducerConfig.createProducer();
    }

    public static void main(String[] args) throws InterruptedException {
        WarehouseArchistreatorService service = new WarehouseArchistreatorService();
        service.startServers();
    }

    public void startServers() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Create and start UDP server for temperature sensors
        UdpServer.create()
            .host("127.0.0.1")
            .port(3344)
            .handle((inbound, outbound) -> handleSensorData(inbound, outbound, "Temperature"))
            .bindNow()
            .onDispose()
            .subscribe();

        // Create and start UDP server for humidity sensors
        UdpServer.create()
            .host("127.0.0.1")
            .port(3355)
            .handle((inbound, outbound) -> handleSensorData(inbound, outbound, "Humidity"))
            .bindNow()
            .onDispose()
            .subscribe();

        // Keep the main thread alive
        latch.await();
    }

    private Mono<Void> handleSensorData(UdpInbound inbound, UdpOutbound outbound, String sensorType) {
        return inbound.receive()
            .asString()
            .flatMap(data -> {
                System.out.println("Received " + sensorType + " data: " + data);
                String[] parts = data.split("; ");
                String sensorId = parts[0].split("=")[1];
                int value = Integer.parseInt(parts[1].split("=")[1]);
                EventsData eventsData = new EventsData(sensorId, value);
                sendToKafka(eventsData);
                return Mono.empty();
            })
            .then();
    }

    private void sendToKafka(EventsData eventsData) {
        String message = "sensor_id=" + eventsData.sensorId() + "; value=" + eventsData.data();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, eventsData.sensorId(), message);
        kafkaProducer.send(record);
        System.out.println("Sent data to Kafka: " + message);
    }
}
