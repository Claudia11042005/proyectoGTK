package com.example.ordersnormalizer.controller;

import com.example.ordersnormalizer.model.EventEnvelope;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

@RestController
@RequestMapping("/internal")
public class TestController {

    private final StreamBridge streamBridge;

    public TestController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody EventEnvelope env) {
        // 👇 Se usa "env" (el parámetro correcto) y se añade un encabezado opcional
        var topic = System.getenv().getOrDefault("TOPIC_ORDERS_CREATED", "dev/orders/created/v1");
        var message = MessageBuilder.withPayload(env)
                .setHeaderIfAbsent("contentType", MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
        streamBridge.send(topic, message);
        return ResponseEntity.accepted().build();
    }
}
