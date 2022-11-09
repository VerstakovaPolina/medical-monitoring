package liga.medical.medicalmonitoring.core.api;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RabbitRouterService {

    String routeMessage(String message) throws JsonProcessingException;

    void routeMessageWithCustomExchange(String message);
}
