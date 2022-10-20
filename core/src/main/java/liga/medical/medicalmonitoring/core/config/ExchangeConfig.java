package liga.medical.medicalmonitoring.core.config;

import liga.medical.medicalmonitoring.core.model.MessageType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    private final Queue dailyQueue;

    private final Queue alertQueue;

    private final Queue errorQueue;

    public ExchangeConfig(@Qualifier("dailyQueue") Queue dailyQueue, @Qualifier("alertQueue") Queue alertQueue, @Qualifier("errorQueue") Queue errorQueue) {
        this.dailyQueue = dailyQueue;
        this.alertQueue = alertQueue;
        this.errorQueue = errorQueue;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingDailyMsg() {
        return BindingBuilder.bind(dailyQueue).to(directExchange()).with(MessageType.DAILY.toString());
    }

    @Bean
    public Binding bindingAlertMsg() {
        return BindingBuilder.bind(alertQueue).to(directExchange()).with(MessageType.ALERT.toString());
    }

    @Bean
    public Binding bindingErrorMsg() {
        return BindingBuilder.bind(errorQueue).to(directExchange()).with(MessageType.ERROR.toString());
    }
}
