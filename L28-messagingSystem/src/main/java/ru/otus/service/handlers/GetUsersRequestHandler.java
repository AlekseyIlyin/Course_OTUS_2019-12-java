package ru.otus.service.handlers;


import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.otus.Serializers;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.repository.UserRepository;

import java.util.Optional;

@Component
public class GetUsersRequestHandler implements RequestHandler {
    private final UserRepository userRepository;
    private final Serializers serializer;

    public GetUsersRequestHandler(UserRepository userRepository, Serializers serializer) {
        this.userRepository = userRepository;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var all = userRepository.findAll();

        String data = new Gson().toJson(all);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), serializer.serialize(data)));
    }
}
