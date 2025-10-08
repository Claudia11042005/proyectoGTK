package com.example.ordersnormalizer.controller;

import com.example.ordersnormalizer.model.EventEnvelope;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class TestController {

    private final StreamBridge streamBridge;

    public TestController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody EventEnvelope env) {
        streamBridge.send(System.getenv().getOrDefault("TOPIC_ORDERS_CREATED", "dev.orders.created.v1"), env);
        return ResponseEntity.accepted().build();
    }
}
