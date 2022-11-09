package liga.medical.medicalmonitoring.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liga.medical.medicalmonitoring.core.model.MessageType;
import liga.medical.medicalmonitoring.core.model.RabbitMessageDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface MessageMapper {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Insert("insert into rabbit_message_dto values(#{id},#{type},#{description});")
    void addMessage(RabbitMessageDto messageDto);

    @Insert("insert into rabbit_message_dto values(#{id},#{type},#{description});")
    default void addMessageWithError(String message) throws JsonProcessingException {
        RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);
        String messageType = rabbitMessageDto.getType();
        String description = rabbitMessageDto.getDescription();
        Long id = rabbitMessageDto.getId();
        RabbitMessageDto messageDto = new RabbitMessageDto(id,messageType,description);
        addMessage(messageDto);
    }
}
