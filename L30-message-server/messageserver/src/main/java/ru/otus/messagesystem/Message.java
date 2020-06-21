package ru.otus.messagesystem;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Message {
    static final Message VOID_MESSAGE = new Message();

    private final UUID id = UUID.randomUUID();
    private final String fromClientType;
    private final UUID fromClientId;
    private final String toClientType;
    private UUID toClientId;
    private final UUID sourceMessageId;
    private final String type;
    private final int payloadLength;
    private final byte[] payload;


    private Message() {
        this.fromClientType = null;
        this.fromClientId = null;
        this.toClientType = null;
        this.toClientId = null;
        this.sourceMessageId = null;
        this.type = MessageType.TECHNICAL.getValue();
        this.payload = new byte[1];
        this.payloadLength = payload.length;
    }

    public Message(String fromClientType, UUID fromClientId, String toClientType, UUID toClientId, UUID sourceMessageId, String type, byte[] payload) {
        this.fromClientType = fromClientType;
        this.fromClientId = fromClientId;
        this.toClientType = toClientType;
        this.toClientId = toClientId;
        this.sourceMessageId = sourceMessageId;
        this.type = type;
        this.payloadLength = payload.length;
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                "id=" + id +
                ", from='" + fromClientType + '-' + fromClientId + '\'' +
                ", to='" + toClientType + '-' + toClientId + '\'' +
                ", sourceMessageId=" + sourceMessageId +
                ", type='" + type + '\'' +
                ", payloadLength=" + payloadLength +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public String getFromClientType() {
        return fromClientType;
    }

    public UUID getFromClientId() {
        return fromClientId;
    }

    public String getToClientType() {
        return toClientType;
    }

    public UUID getToClientId() {
        return toClientId;
    }

    public void setToClientId(UUID id) {
        toClientId = id;
    }

    public String getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public Optional<UUID> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }

}
