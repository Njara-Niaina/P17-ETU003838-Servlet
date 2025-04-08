package main.java.com.helloWorld.dao;

import main.java.com.helloWorld.connection.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDaoSql<T extends BaseModelDao> implements InterfaceDao<T> {
    private final Class<T> entityClass;

    public GenericDaoSql(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Connection getConnection() throws SQLException {
        return Dbconnect.getConnection(); // Corrigé pour utiliser Dbconnect.getConn()
    }

    private String getTableName() {
        return entityClass.getSimpleName().toLowerCase(); // Consistent with your previous code
    }

    private Field[] getEntityFields() {
        return entityClass.getDeclaredFields();
    }

    // Original methods
    @Override
    public void save(T toSave) throws SQLException {
        try (Connection conn = getConnection()) {
            save(conn, toSave);
        }
    }

    @Override
    public T[] findAll() throws SQLException {
        try (Connection conn = getConnection()) {
            return findAll(conn);
        }
    }

    @Override
    public T[] findById(int id) throws SQLException {
        try (Connection conn = getConnection()) {
            return findById(conn, id);
        }
    }

    @Override
    public void update(T toUpdate) throws SQLException {
        try (Connection conn = getConnection()) {
            update(conn, toUpdate);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = getConnection()) {
            delete(conn, id);
        }
    }

    @Override
    public T[] findWithPagination(int indexFrom, int limit) throws SQLException {
        try (Connection conn = getConnection()) {
            return findWithPagination(conn, indexFrom, limit);
        }
    }

    // Transaction-supporting methods
    @Override
    public void save(Connection conn, T toSave) throws SQLException {
        if (!entityClass.isInstance(toSave)) {
            throw new SQLException("Entity type mismatch. Expected: " + entityClass.getName() +
                    ", Got: " + toSave.getClass().getName());
        }

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Field field : getEntityFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(toSave);
                if (field.getName().equals("id")) continue;

                if (columns.length() > 0) {
                    columns.append(", ");
                    placeholders.append(", ");
                }

                columns.append(field.getName());
                placeholders.append("?");
                values.add(value);
            } catch (IllegalAccessException e) {
                throw new SQLException("Failed to access field: " + field.getName(), e);
            }
        }

        String sql = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, values);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    toSave.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public T[] findAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM " + getTableName();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
            return results.toArray(createArray(results.size()));
        }
    }

    @Override
    public T[] findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                if (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
                return results.toArray(createArray(results.size()));
            }
        }
    }

    @Override
    public void update(Connection conn, T toUpdate) throws SQLException {
        if (!entityClass.isInstance(toUpdate)) {
            throw new SQLException("Entity type mismatch. Expected: " + entityClass.getName() +
                    ", Got: " + toUpdate.getClass().getName());
        }

        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Field field : getEntityFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(toUpdate);
                if (field.getName().equals("id")) continue;

                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(field.getName()).append(" = ?");
                values.add(value);
            } catch (IllegalAccessException e) {
                throw new SQLException("Failed to access field: " + field.getName(), e);
            }
        }

        String sql = "UPDATE " + getTableName() + " SET " + setClause + " WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, values);
            stmt.setInt(values.size() + 1, toUpdate.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public T[] findWithPagination(Connection conn, int indexFrom, int limit) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, indexFrom);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
                return results.toArray(createArray(results.size()));
            }
        }
    }

    private void setParameters(PreparedStatement stmt, List<Object> values) throws SQLException {
        int paramIndex = 1;
        for (Object value : values) {
            stmt.setObject(paramIndex++, value);
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setId(rs.getInt("id"));

            for (Field field : getEntityFields()) {
                if (field.getName().equals("id")) continue;

                field.setAccessible(true);
                try {
                    Class<?> fieldType = field.getType();
                    Object value = getTypedValue(rs, field.getName(), fieldType);
                    if (!rs.wasNull()) {
                        field.set(entity, value);
                    }
                } catch (SQLException e) {
                    // Column might not exist, ignore
                }
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new SQLException("Failed to create entity instance", e);
        }
    }

    private Object getTypedValue(ResultSet rs, String columnName, Class<?> fieldType) throws SQLException {
        if (fieldType == int.class || fieldType == Integer.class) {
            return rs.getInt(columnName);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return rs.getDouble(columnName);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return rs.getBoolean(columnName);
        } else if (fieldType == String.class) {
            return rs.getString(columnName);
        } else if (fieldType == java.util.Date.class) {
            return rs.getDate(columnName);  
        }
        return rs.getObject(columnName);
    }

    public T[] createInstanceFromQuery(String sqlQuery) throws SQLException {
        try (Connection conn = getConnection()) {
            return createInstanceFromQuery(conn, sqlQuery);
        }
    }
    
    public T[] createInstanceFromQuery(Connection conn, String sqlQuery) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                T obj = mapResultSetToEntity(rs);
                if (obj != null) {
                    results.add(obj);
                }
            }
        }
        return results.toArray(createArray(results.size()));
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int size) {
        return (T[]) java.lang.reflect.Array.newInstance(entityClass, size);
    }
}