package ru.otus.l10.jdbc.dao;

import ru.otus.l10.core.annotations.Id;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Aleksey Ilyin
 * created on 05.04.20.
 */
public class SqlQueryBuilder<T> {
    private T objectForQueryBuild;
    private final Class<T> tClass;
    private final Field[] declaredFields;
    private String IdFieldName;                                // ID field name
    private Map<String, Object> fieldValues = new HashMap<>(); // key - name field, value - value object field
    private Boolean isSupportIdAnnotation;
    private final String nameTable;
    private Queue<String> paramsQueue = new LinkedList<>();

    public void setObjectForQueryBuild(T objectForQueryBuild) throws IllegalAccessException {
        this.objectForQueryBuild = objectForQueryBuild;
        refreshFieldValues();
    }

    public SqlQueryBuilder(Class<T> tClass ) {
        this.objectForQueryBuild = null;
        this.tClass = tClass;
        this.nameTable = tClass.getSimpleName().toLowerCase();
        declaredFields = tClass.getDeclaredFields();
        try {
            refreshFieldValues();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void refreshFieldValues() throws IllegalAccessException, SqlQueryBuilderNotSupported {
        this.fieldValues.clear();
        for (Field field : this.declaredFields) {
            field.setAccessible(true);
            final var idAnnotation = field.getAnnotation(Id.class);
            if (idAnnotation != null) {
                isSupportIdAnnotation = true;
                IdFieldName = field.getName();
            }
            fieldValues.put(field.getName(), objectForQueryBuild == null ? null : field.get(objectForQueryBuild));
        }
        if (!isSupportIdAnnotation) {
            throw new SqlQueryBuilderNotSupported("This class: " + tClass.getName() + " do not supported, please set annotation 'Id' on Id field");
        }
    }

    public String getInsertQuery() {
        paramsQueue.clear();
        StringBuilder queryBuilder = new StringBuilder("insert into ");
        queryBuilder.append(this.nameTable)
                .append("(");
        for (String s : fieldValues.keySet()) {
            queryBuilder.append(s).append(",");
            paramsQueue.offer(fieldValues.get(s).toString());
        }
        int maxIdxStrBuilder = queryBuilder.length() - 1;
        if (queryBuilder.charAt(maxIdxStrBuilder) == ',') {
            queryBuilder.deleteCharAt(maxIdxStrBuilder);
        }
        queryBuilder.append(") values (");
        for (String s : fieldValues.keySet()) {
            queryBuilder.append("?,");
        }
        maxIdxStrBuilder = queryBuilder.length() - 1;
        if (queryBuilder.charAt(maxIdxStrBuilder) == ',') {
            queryBuilder.deleteCharAt(maxIdxStrBuilder);
        }
        queryBuilder.append(')');
        return queryBuilder.toString();
    }

    public String getUpdateQuery() {
        paramsQueue.clear();
        StringBuilder queryBuilder = new StringBuilder("update ");
        queryBuilder.append(this.nameTable)
            .append(" set ");
        boolean isFirstKey = true;
        for (String s : fieldValues.keySet()) {
            if (!s.equals(this.IdFieldName)) {
                if (isFirstKey) {
                    isFirstKey = false;
                } else {
                    queryBuilder.append(',');
                }
                queryBuilder.append(s).append("=?");
                paramsQueue.offer(fieldValues.get(s).toString());
            }
        }

        queryBuilder.append(String.format(" where %s = ?", IdFieldName));
        paramsQueue.offer(fieldValues.get(IdFieldName).toString());
        return queryBuilder.toString();
    }

    public String getSelectQuery() {
        paramsQueue.clear();
        StringBuilder queryBuilder = new StringBuilder("select * from ");
        queryBuilder.append(this.nameTable);
        queryBuilder.append(String.format(" where %s = ?", IdFieldName));
        paramsQueue.offer(fieldValues.get(IdFieldName).toString());
        return queryBuilder.toString();
    }

    public Map<String, Object> getQueryValues() {
        return fieldValues;
    }

    public boolean isSupported() {
        return isSupportIdAnnotation;
    }

    public Queue<String> getParams() {
        return this.paramsQueue;
    }
}
