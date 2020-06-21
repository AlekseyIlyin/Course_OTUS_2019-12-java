package ru.otus.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@RequiredArgsConstructor
@Slf4j
public class MongoTemplateImpl implements DbTemplate {
    private static final TypeReference<Map<String, Object>> STR_OBJECT_MAP_TYPE_REF = new TypeReference<>() {};

    private final MongoCollection<Document> collection;
    private final ObjectMapper mapper;

    @Override
    public <T> ObjectId insert(T value) {
        val document = new Document(mapper.convertValue(value, STR_OBJECT_MAP_TYPE_REF));
        document.remove("_id");
        collection.insertOne(document);
        log.info("Object insert", value);
        return (ObjectId) document.get("_id");
    }

    @Override
    public <T> List<T> find(String fieldName, Object fieldValue, Class<T> tClass) {

        final Bson query;

        if (fieldName.isEmpty()) {
            query = Document.parse("{}");
        } else {
            query = eq(fieldName, fieldValue);
        }

        val documents = collection.find(query);

        val res = new ArrayList<T>();
        val cursor = documents.cursor();
        while (cursor.hasNext()) {
            val document = cursor.next();
            res.add(document2Object(document, tClass));
        }
        log.info("finding objects by query {}",query);
        return res;
    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) throws Exception {
        return find("", null, tClass);
    }

    @Override
    public void dropTable() {
        collection.drop();
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private <T> T document2Object(Document document, Class<T> tClass) {
        if (tClass.equals(Document.class)) {
            return (T) document;
        }

        val id = document.get("_id");
        document.put("_id", id.toString());
        return mapper.reader().forType(tClass).readValue(document.toJson());
    }

}