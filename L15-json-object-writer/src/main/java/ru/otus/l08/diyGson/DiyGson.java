package ru.otus.l08.diyGson;

import ru.otus.l08.ObjectForTesting;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class DiyGson {

    public String toJson(ObjectForTesting object) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try {
            collectValuesInBuilder(jsonObjectBuilder, object, getObjectFields(object)).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return jsonObjectBuilder.build().toString();
    }

    private JsonObjectBuilder collectValuesInBuilder(JsonObjectBuilder jsonObjectBuilder, Object object, Set<Field> fields) {
        for (Field field : fields) {
            try {
                JsonValue jsonValue = getJsonValue(field.get(object));
                if (jsonValue != null) {
                    jsonObjectBuilder.add(field.getName(), jsonValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectBuilder;
    }

    private Set<Field> getObjectFields(Object object) throws IllegalAccessException{
        Set<Field> fields = new HashSet<>();
        Class<?> objClass = object.getClass();
        final Field[] declaredFields = objClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            final var modifiers = field.getModifiers();
            final var valueOfField = field.get(object);
            if (valueOfField != null && !Modifier.isStatic(modifiers)) { //!Modifier.isFinal(modifiers) &&
                fields.add(field);
            }
        }
        return fields;
    }

    private JsonValue getJsonValue(Object object) {
        JsonValue jsonValue = JsonValue.NULL;
        JsonArrayBuilder jsonArrayBuilder;

        switch (getSerializedType(object)) {

            case PRIMITIVE:
                if (object instanceof String) {
                    jsonValue = Json.createValue ((String) object);
                } else if (object instanceof Integer) {
                    jsonValue = Json.createValue ((Integer) object);
                } else if (object instanceof Character) {
                    jsonValue = Json.createValue ((Character) object);
                } else if (object instanceof Byte) {
                    jsonValue = Json.createValue ((Byte) object);
                } else if (object instanceof Short ) {
                    jsonValue = Json.createValue ((Short) object);
                } else if (object instanceof Boolean) {
                    jsonValue = (Boolean) object ? JsonValue.TRUE : JsonValue.FALSE;
                } else if (object instanceof Long) {
                    jsonValue = Json.createValue ((Long) object);
                } else if (object instanceof Float) {
                    jsonValue = Json.createValue ((Float) object);
                } else if (object instanceof Double) {
                    jsonValue = Json.createValue((Double) object);
                }
                break;
            case ARRAY:
                jsonArrayBuilder = Json.createArrayBuilder();
                try {
                    int lengthArray = Array.getLength(object);
                    for (int i = 0; i < lengthArray; i++) {
                        var arrEl = Array.get(object, i);
                        if (arrEl == null) {
                            jsonArrayBuilder.add(JsonValue.NULL);
                        } else {
                            jsonArrayBuilder.add(getJsonValue(arrEl));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                jsonValue = jsonArrayBuilder.build();
                break;
            case COLLECTION:
                jsonArrayBuilder = Json.createArrayBuilder();
                try {
                    for (Object objItem : (Collection) object) {
                        if (objItem == null) {
                            jsonArrayBuilder.add(JsonValue.NULL);
                        } else {
                            jsonArrayBuilder.add(getJsonValue(objItem));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                jsonValue = jsonArrayBuilder.build();
                break;
            case MAP:
                JsonObjectBuilder jsonMapBuilder = Json.createObjectBuilder((Map<String, Object>) object);
                try {
                    for (Map.Entry<String,Object> mapEntry : ((Map<String, Object>) object).entrySet()) {
                        jsonMapBuilder.add(mapEntry.getKey(), getJsonValue(mapEntry.getValue()));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                jsonValue = jsonMapBuilder.build();
                break;
            case NULL:
                System.out.println("Unknown type value: " + object);
        }
        return jsonValue;
    }

    private SerializedTypes getSerializedType(Object object) {
        if (object == null) {
            return SerializedTypes.NULL;
        } else if (object instanceof Collection) {
            return SerializedTypes.COLLECTION;
        } else if (object instanceof Map) {
            return SerializedTypes.MAP;
        } else if (object instanceof Boolean || object instanceof Character || object instanceof String
                || object instanceof Byte || object instanceof Short || object instanceof Integer
                || object instanceof Long || object instanceof Float || object instanceof Double) {
            return SerializedTypes.PRIMITIVE;
        } else {
            Class<?> classObject = object.getClass();
            if (classObject.isPrimitive()) {
                return SerializedTypes.PRIMITIVE;
            } else if (classObject.isArray()) {
                return SerializedTypes.ARRAY;
            } else {
                return SerializedTypes.NULL;
            }
        }
    }
}
