# Orders Normalizer Service

Microservicio que normaliza órdenes (`ORDER.CREATED` → `ORDER.NORMALIZED`) y publica errores (`ORDER.ERROR`) usando Solace PubSub+.

## Variables de entorno

| Variable | Descripción |
|-----------|--------------|
| SOLACE_HOST | Host TCP/TCPS del broker (ej: `tcps://mr-xyz.messaging.solace.cloud:55443`) |
| SOLACE_VPN | VPN de tu servicio |
| SOLACE_USERNAME | Usuario |
| SOLACE_PASSWORD | Contraseña |
| TOPIC_ORDERS_CREATED | Tópico de entrada (default: `dev.orders.created.v1`) |
| TOPIC_ORDERS_NORMALIZED | Tópico de salida (default: `dev.orders.normalized.v1`) |
| TOPIC_EVENTS_ERROR | Tópico de errores (default: `dev.events.error.v1`) |

## Ejecutar localmente

```bash
mvn spring-boot:run
```

## Docker

```bash
docker build -t orders-normalizer .
docker run -e SOLACE_HOST=tcps://... -e SOLACE_VPN=... -e SOLACE_USERNAME=... -e SOLACE_PASSWORD=... orders-normalizer
```

## Prueba de evento

```bash
curl -X POST http://localhost:8080/internal/send -H "Content-Type: application/json" -d '{
  "version":"1.0",
  "type":"ORDER.CREATED",
  "source":"producer.orders",
  "id":"111",
  "ts":"2025-10-07T10:00:00Z",
  "payload":{
    "orderId":"ORD-1",
    "customerId":"CUST-1",
    "currency":"USD",
    "amount":100.456,
    "items":[{"sku":"A1","qty":1,"price":50.567,"category":"electronics"}],
    "paymentMethod":"CARD",
    "country":"US",
    "createdAt":"2025-10-07T10:00:00Z",
    "shippingAddress":{"line1":"Main 123","city":"NY","postalCode":"10001","country":"US"}
  }
}'
```
