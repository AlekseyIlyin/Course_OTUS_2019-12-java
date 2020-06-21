package ru.otus.messagesystem;

public enum MessageType {

  TECHNICAL("Technical"),
  CONNECT("Connect"),
  USER_DATA("UserData"),
  USER_CREATE("UserCreate"),
  STOP("STOP");

  private final String value;

  public String getValue() {
    return value;
  }

  public static MessageType getTypeMessageFromValue(String value) {

    switch (value) {
      case "Technical" : {
        return TECHNICAL;
      }
      case "Connect" : {
        return CONNECT;
      }
      case "UserData" : {
        return USER_DATA;
      }
      case "UserCreate" : {
        return USER_CREATE;
      }
      default: {
        return STOP;
      }
    }

  }

  MessageType(String value) {
    this.value = value;
  }

}
