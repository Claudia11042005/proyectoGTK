package com.example.ordersnormalizer.model;

import java.util.Map;

public class EventEnvelope {
    private String version;
    private String type;
    private String source;
    private String id;
    private String ts;
    private OrderPayload payload;
    private Map<String, Object> meta;
    private Map<String, Object> error;

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTs() { return ts; }
    public void setTs(String ts) { this.ts = ts; }
    public OrderPayload getPayload() { return payload; }
    public void setPayload(OrderPayload payload) { this.payload = payload; }
    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }
    public Map<String, Object> getError() { return error; }
    public void setError(Map<String, Object> error) { this.error = error; }
}
