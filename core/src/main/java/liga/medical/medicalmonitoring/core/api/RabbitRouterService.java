package liga.medical.medicalmonitoring.core.api;

public interface RabbitRouterService {

    void routeMessage(String message);

    void routeMessageWithCustomExchange(String message);
}
