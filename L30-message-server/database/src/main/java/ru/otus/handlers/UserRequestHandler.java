package ru.otus.handlers;

import com.google.gson.Gson;
import ru.otus.Serializer;
import ru.otus.domain.User;
import ru.otus.domain.dao.DaoTemplate;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.List;
import java.util.Optional;

public class UserRequestHandler implements RequestHandler {
    private final DaoTemplate<User> userDao;
    private final Serializer serializer;

    public UserRequestHandler(DaoTemplate<User> userDao, Serializer serializer) {
        this.userDao = userDao;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        
        String dataForSerialized = null;
        
        switch (MessageType.getTypeMessageFromValue(msg.getType())) {
            
            case USER_DATA: {
                final List<User> all = userDao.findAll();
                dataForSerialized = new Gson().toJson(all);
                break;
            }
            
            case USER_CREATE: {
                final var userToSave = serializer.deserialize(msg.getPayload(), User.class);
                final var userId = userDao.saveObject(userToSave);
                dataForSerialized = userId.toString();
                break;
            }
        }
        
        return Optional.of(new Message(msg.getToClientType(), msg.getToClientId(), msg.getFromClientType(), msg.getFromClientId(), msg.getId(), msg.getType(), serializer.serialize(dataForSerialized)));
    }
}
