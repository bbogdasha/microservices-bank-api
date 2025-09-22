package com.bogdan.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    /**
     * Головний метод глобального фільтра, який перехоплює кожен запит до Gateway.
     * <p>
     * Логіка:
     * 1. Перевіряє, чи є Correlation ID у вхідних заголовках запиту.
     *    - Якщо є → логує, що ідентифікатор знайдено.
     *    - Якщо немає → генерує новий Correlation ID (UUID), додає його у запит
     *      через FilterUtility і логує створення.
     * <p>
     * 2. Передає обробку далі по ланцюгу (chain.filter(exchange)),
     *    щоб наступні фільтри та мікросервіси бачили оновлений запит.
     * <p>
     * @param exchange — поточний об’єкт, що містить запит і відповідь
     * @param chain — ланцюг фільтрів Gateway
     * @return реактивний Mono<Void>, який сигналізує про завершення обробки
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = filterUtility.getCorrelationId(exchange.getRequest().getHeaders());
        if (correlationId != null) {
            logger.debug("Correlation id found: {}", correlationId);
        } else {
            correlationId = UUID.randomUUID().toString();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug("Generated correlation id: {}", correlationId);
        }
        return chain.filter(exchange);
    }
}