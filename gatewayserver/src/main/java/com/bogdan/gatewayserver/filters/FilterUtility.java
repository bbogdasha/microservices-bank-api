package com.bogdan.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * getCorrelationId → шукає correlation ID у запиті, якщо він уже є.
 * <p>
 * setRequestHeader → створює нову копію запиту з додатковим або оновленим заголовком.
 * <p>
 * setCorrelationId → зручно встановлює саме correlation ID і ще кешує його в атрибутах.
 */

@Component
public class FilterUtility {

    /**
     * Correlation ID — це унікальний ідентифікатор, який допомагає відслідковувати один і той самий запит
     * через усі мікросервіси (корисно для логування та трейсингу).
     */
    public static final String CORRELATION_ID = "msbank-correlation-id";

    /**
     * Шукає Correlation ID у вхідних HTTP-заголовках запиту.
     *
     * @param requestHeaders — усі заголовки запиту
     * @return знайдений correlation ID або null, якщо його немає
     * <p>
     * Використання getFirst() дозволяє одразу отримати перше значення
     * без необхідності працювати зі списком рядків.
     */
    public String getCorrelationId(HttpHeaders requestHeaders) {
        return requestHeaders.getFirst(CORRELATION_ID);
    }

    /**
     * Додає або замінює HTTP-заголовок у поточному запиті.
     * <p>
     * Оскільки об’єкти Spring WebFlux (ServerHttpRequest, ServerWebExchange) є immutable
     * (незмінними), ми використовуємо builder (mutate) для створення копії з новим заголовком.
     *
     * @param exchange — поточний WebFlux обмін (запит + відповідь)
     * @param name — назва заголовка
     * @param value — значення заголовка
     * @return новий ServerWebExchange з оновленим запитом
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(name, value)
                .build();
        return exchange.mutate().request(mutatedRequest).build();
    }

    /**
     * Спеціальний метод для встановлення Correlation ID у запит.
     * <p>
     * 1. Використовує метод setRequestHeader(), щоб вставити заголовок "msbank-correlation-id".
     * 2. Додатково зберігає Correlation ID у attributes (атрибути ServerWebExchange),
     *    щоб його можна було швидко витягнути без повторного читання заголовків.
     *
     * @param exchange — поточний WebFlux обмін
     * @param correlationId — значення унікального Correlation ID
     * @return новий ServerWebExchange з оновленим заголовком і атрибутами
     */
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        ServerWebExchange mutated = setRequestHeader(exchange, CORRELATION_ID, correlationId);
        mutated.getAttributes().put(CORRELATION_ID, correlationId);
        return mutated;
    }

}