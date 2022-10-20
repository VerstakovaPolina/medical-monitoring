package liga.medical.medicalmonitoring.core.model;

import lombok.Data;

@Data
public class RabbitMessageDto {

    private MessageType type;

    private String content;
}
