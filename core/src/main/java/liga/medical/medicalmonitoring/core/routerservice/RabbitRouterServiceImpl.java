package liga.medical.medicalmonitoring.core.routerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liga.medical.medicalmonitoring.core.api.RabbitRouterService;
import liga.medical.medicalmonitoring.core.api.RabbitSenderService;
import liga.medical.medicalmonitoring.core.config.ExchangeConfig;
import liga.medical.medicalmonitoring.core.mapper.MessageMapper;
import liga.medical.medicalmonitoring.core.model.MessageType;
import liga.medical.medicalmonitoring.core.model.QueueNames;
import liga.medical.medicalmonitoring.core.model.RabbitMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitRouterServiceImpl implements RabbitRouterService {

    private final ObjectMapper objectMapper;

    private final MessageMapper messageMapper;

    private final RabbitSenderService rabbitSenderService;

    private final RabbitTemplate rabbitTemplate;

    public RabbitRouterServiceImpl(ObjectMapper objectMapper, MessageMapper messageMapper, RabbitSenderService rabbitSenderService, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.messageMapper = messageMapper;
        this.rabbitSenderService = rabbitSenderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public String routeMessage(String message) {
        String queue = null;
        try {
            RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);
            MessageType messageType = MessageType.valueOf(rabbitMessageDto.getType());

            switch (messageType) {
                case DAILY:
                    queue = QueueNames.DAILY_QUEUE_NAME;
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.DAILY_QUEUE_NAME);
                    messageMapper.addMessage(rabbitMessageDto);
                    break;
                case ALERT:
                    queue = QueueNames.ALERT_QUEUE_NAME;
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.ALERT_QUEUE_NAME);
                    messageMapper.addMessage(rabbitMessageDto);
                    break;
                default:
                    queue = QueueNames.ERROR_QUEUE_NAME;
                    rabbitSenderService.sendMessage(rabbitMessageDto, QueueNames.ERROR_QUEUE_NAME);
                    messageMapper.addMessage(rabbitMessageDto);
            }
        } catch (Exception exception) {
            queue = QueueNames.ERROR_QUEUE_NAME;
            try {
                messageMapper.addMessageWithError(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            rabbitSenderService.sendError(message);
        }
        return queue;
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
