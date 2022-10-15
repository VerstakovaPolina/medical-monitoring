package liga.medical.medicalmonitoring.core.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import liga.medical.medicalmonitoring.core.model.RabbitMessageDto;

public interface RabbitSenderService {

    void sendMessage(RabbitMessageDto messageDto, String queue) throws JsonProcessingException;

    void sendError(String message);

}
