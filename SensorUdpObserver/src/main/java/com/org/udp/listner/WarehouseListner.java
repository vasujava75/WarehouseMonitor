package com.org.udp.listner;

import reactor.netty.udp.UdpServer;

public class WarehouseListner {

    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        udpSocketListner(3344, "Temperature");
        // Create and start UDP server for humidity sensors
        udpSocketListner(3355, "Humidity");
    }

    private static void udpSocketListner(int port, String Temperature) {
        // Create and start UDP server for temperature sensors
        UdpServer.create()
            .host(HOST)
            .port(port)
            .handle((inbound, outbound) -> handleSensorData(inbound, outbound, Temperature))
            .bindNow()
            .onDispose()
            .subscribe();
    }



}
