package liga.medical.medicalmonitoring.core.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import liga.medical.medicalmonitoring.core.api.RabbitRouterService;
import liga.medical.medicalmonitoring.core.model.QueueNames;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitRouterListener {

    private final RabbitRouterService rabbitRouterService;

    public RabbitRouterListener(RabbitRouterService rabbitRouterService) {
        this.rabbitRouterService = rabbitRouterService;
    }

    @RabbitListener(queues = QueueNames.COMMON_MONITORING_QUEUE_NAME)
    public void receiveAndRedirectMessage(String message) {
        try {
            rabbitRouterService.routeMessage(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
