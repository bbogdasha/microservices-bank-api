package com.bogdan.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    /**
     * Створює глобальний фільтр для обробки вихідних відповідей (post filter).
     * <p>
     * - Цей фільтр виконується вже після того, як запит пройшов через gateway і отримав відповідь
     *   від потрібного мікросервісу.
     * - Його завдання — гарантувати, що у кожній відповіді клієнту буде доданий заголовок
     *   з correlationId (унікальним ідентифікатором запиту).
     * <p>
     * Логіка роботи:
     * 1. Виконуємо основний ланцюг фільтрів (`chain.filter(exchange)`), щоб запит був
     *    оброблений і gateway отримав відповідь від сервісу.
     * 2. Використовуємо `.then(Mono.fromRunnable(...))`, щоб додати додаткову дію
     *    після завершення основної обробки.
     * 3. Перш за все намагаємось отримати correlationId з `exchange.getAttributes()`,
     *    куди його записав `RequestTraceFilter`.
     * 4. Якщо там його немає (наприклад, якщо вхідний фільтр не спрацював),
     *    тоді беремо його з вхідних headers.
     * 5. Якщо correlationId знайдений — додаємо його у вихідні headers відповіді,
     *    щоб клієнт міг отримати цей унікальний ідентифікатор разом із відповіддю.
     * 6. Якщо ж correlationId відсутній навіть тут — логіруємо попередження.
     */
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            String correlationId = (String) exchange.getAttributes().get(FilterUtility.CORRELATION_ID);

            if (correlationId == null) {
                correlationId = filterUtility.getCorrelationId(exchange.getRequest().getHeaders());
            }

            if (correlationId != null) {
                exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
            } else {
                logger.warn("CorrelationId is empty!");
            }
        }));
    }
}
