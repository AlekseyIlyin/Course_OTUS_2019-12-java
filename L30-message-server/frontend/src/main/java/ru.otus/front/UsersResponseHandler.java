package ru.otus.front;

import lombok.extern.slf4j.Slf4j;
import ru.otus.Serializer;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UsersResponseHandler implements RequestHandler {
    private final Serializer serializer;
    private final FrontendService frontendService;

    public UsersResponseHandler(Serializer serializer, FrontendService frontendService) {
        this.serializer = serializer;
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("new message:{}", msg);

        try {
            String userData = serializer.deserialize(msg.getPayload(), String.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userData));

        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
