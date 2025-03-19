package com.org.udp.listner;

import com.org.udp.model.EventsData;
import reactor.core.publisher.Mono;
import reactor.netty.udp.UdpInbound;
import reactor.netty.udp.UdpOutbound;

public class TransformData {

    public static Mono<Void> handleSensorData(UdpInbound inbound, UdpOutbound outbound, String sensorType) {
        return inbound.receive()
            .asString()
            .flatMap(data -> {
                System.out.println("Received " + sensorType + " data: " + data);
                String[] parts = data.split("; ");
                String sensorId = parts[0].split("=")[1];
                int value = Integer.parseInt(parts[1].split("=")[1]);
                return sendToCentralMonitoringService(new EventsData(sensorId,value));
            })
            .then();
    }
    private static Mono<Void> sendToCentralMonitoringService(EventsData data) {
        return Mono.fromRunnable(() -> System.out.println(data));
    }
}
