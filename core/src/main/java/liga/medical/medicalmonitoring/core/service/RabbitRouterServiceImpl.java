package liga.medical.medicalmonitoring.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import liga.medical.medicalmonitoring.core.api.RabbitRouterService;
import liga.medical.medicalmonitoring.core.api.RabbitSenderService;
import liga.medical.medicalmonitoring.core.config.ExchangeConfig;
import liga.medical.medicalmonitoring.core.model.MessageType;
import liga.medical.medicalmonitoring.core.model.QueueNames;
import liga.medical.medicalmonitoring.core.model.RabbitMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitRouterServiceImpl implements RabbitRouterService {

    private final ObjectMapper objectMapper;

    private final RabbitSenderService rabbitSenderService;

    private final RabbitTemplate rabbitTemplate;

    public RabbitRouterServiceImpl(ObjectMapper objectMapper, RabbitSenderService rabbitSenderService, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitSenderService = rabbitSenderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void routeMessage(String message) {
        try {
            RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);
            MessageType messageType = rabbitMessageDto.getType();

            switch (messageType) {
                case DAILY:
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.DAILY_QUEUE_NAME);
                    break;
                case ALERT:
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.ALERT_QUEUE_NAME);
                    break;
                default:
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.ERROR_QUEUE_NAME);
            }
        } catch (Exception exception) {
            rabbitSenderService.sendError(exception.getMessage());
        }
    }

    @Override
    public void routeMessageWithCustomExchange(String message) {
        rabbitTemplate.setExchange(ExchangeConfig.DIRECT_EXCHANGE_NAME);

        try {
            RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);
            rabbitTemplate.convertAndSend(rabbitMessageDto.getType().toString(), message);
            System.out.println("Роутер перенаправил сообщение при помощи обменника");
        } catch (Exception exception) {
            rabbitTemplate.convertAndSend(MessageType.ERROR.toString(), exception.getMessage());
            System.out.println("При перенаправлении сообщения произошла ошибка");

        }
    }
}
