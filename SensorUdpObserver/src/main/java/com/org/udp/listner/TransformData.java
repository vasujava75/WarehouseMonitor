package com.org.udp.listner;

import reactor.core.publisher.Mono;
import reactor.netty.udp.UdpInbound;
import reactor.netty.udp.UdpOutbound;

public class TransformData {

     static Mono<Void> handleSensorData(UdpInbound inbound, UdpOutbound outbound, String sensorType) {
        return inbound.receive()
            .asString()
            .flatMap(data -> {
                System.out.println("Received " + sensorType + " data: " + data);
                return sendToCentralMonitoringService(data);
            })
            .then();
    }
    private static Mono<Void> sendToCentralMonitoringService(String data) {
        // Simulate sending data to Central Monitoring Service
        return Mono.fromRunnable(() -> System.out.println("Sending data to Central Monitoring Service: " + data));
    }
}
