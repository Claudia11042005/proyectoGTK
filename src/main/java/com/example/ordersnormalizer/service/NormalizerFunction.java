package com.example.ordersnormalizer.service;

import com.example.ordersnormalizer.model.EventEnvelope;
import com.example.ordersnormalizer.model.Item;
import jakarta.validation.Validation;
import com.example.ordersnormalizer.model.OrderPayload; 
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
public class NormalizerFunction {

    @Autowired
    private StreamBridge streamBridge;

    private final Set<String> processedIds = Collections.synchronizedSet(new HashSet<>());
    private final Validator validator;

    public NormalizerFunction() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Bean
    public Function<EventEnvelope, EventEnvelope> normalize() {
        return envelope -> {
            if (processedIds.contains(envelope.getId())) {
                System.out.println("Duplicate message: " + envelope.getId());
                return null;
            }
            processedIds.add(envelope.getId());
            try {
                var violations = validator.validate(envelope.getPayload());
                if (!violations.isEmpty()) {
                    var first = violations.iterator().next();
                    sendError(envelope, "VALIDATION_ERROR",
                        "Campo inválido: " + first.getPropertyPath() + " - " + first.getMessage());
                    return null;
                }
                var payload = envelope.getPayload();
                payload.setAmount(payload.getAmount().setScale(2, RoundingMode.HALF_UP));
                payload.getItems().forEach(i -> i.setPrice(i.getPrice().setScale(2, RoundingMode.HALF_UP)));
                Map<String, Object> meta = envelope.getMeta() != null ? envelope.getMeta() : new HashMap<>();
                meta.put("initialValidation", true);
                meta.put("normalizedAt", Instant.now().toString());
                envelope.setMeta(meta);
                envelope.setType("ORDER.NORMALIZED");
                envelope.setSource("orders.normalizer");
                System.out.println("Normalized order: " + envelope.getId());
                return envelope;
            } catch (Exception e) {
                sendError(envelope, "UNKNOWN_ERROR", e.getMessage());
                return null;
            }
        };
    }

    private void sendError(EventEnvelope original, String type, String reason) {
        EventEnvelope errorEvent = new EventEnvelope();
        errorEvent.setVersion("1.0");
        errorEvent.setType("ORDER_ERROR"); 
        errorEvent.setSource("orders.normalizer");
        errorEvent.setId(UUID.randomUUID().toString());
        errorEvent.setTs(Instant.now().toString());

        // Crear payload mínimo
        OrderPayload errorPayload = new OrderPayload();
        if (original.getPayload() != null) {
            errorPayload.setOrderId(original.getPayload().getOrderId());
        } else {
            errorPayload.setOrderId("UNKNOWN");
        }
        errorEvent.setPayload(errorPayload);

        // Crear objeto de error
        Map<String, Object> err = new HashMap<>();
        err.put("type", type);
        err.put("reason", reason);
        err.put("step", "NORMALIZER");
        err.put("originalId", original.getId());

        errorEvent.setError(err);
        
        String errorTopic = System.getenv().getOrDefault("TOPIC_EVENTS_ERROR", "dev/events/error/v1");
        streamBridge.send(errorTopic, errorEvent);
        System.err.println("Error published: " + reason);
    }

    private boolean validatePayload(OrderPayload payload) {
        if (payload.getItems() == null || payload.getItems().isEmpty()) {
            return false;
        }
        
        for (Item item : payload.getItems()) {
            if (item.getQty() < 1) {
                return false;
            }
        }
        
        // Validar currency ISO-4217
        Set<String> validCurrencies = Set.of("USD", "GTQ", "MXN", "EUR");
        if (!validCurrencies.contains(payload.getCurrency())) {
            return false;
        }
        
        return true;
    }

}
