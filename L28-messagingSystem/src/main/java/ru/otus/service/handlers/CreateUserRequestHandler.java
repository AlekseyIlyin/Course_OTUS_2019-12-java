package ru.otus.service.handlers;

import ru.otus.Serializers;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.repository.UserRepository;

import java.util.Optional;

public class CreateUserRequestHandler implements RequestHandler {
    private final UserRepository userRepository;
    private final Serializers serializer;

    public CreateUserRequestHandler(UserRepository userRepository, Serializers serializer) {
        this.userRepository = userRepository;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var userToSave = serializer.deserialize(msg.getPayload(), User.class);
        final var userId = userRepository.create(userToSave);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.CREATE_USER.getValue(), serializer.serialize(userId.toString())));
    }
}
