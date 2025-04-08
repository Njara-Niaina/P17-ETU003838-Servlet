package main.java.com.helloWorld.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface InterfaceDao<T extends BaseModelDao> {
    // Original methods
    void save(T toSave) throws SQLException;
    T[] findAll() throws SQLException;
    T[] findById(int id) throws SQLException;
    void update(T toUpdate) throws SQLException;
    void delete(int id) throws SQLException;
    T[] findWithPagination(int indexFrom, int limit) throws SQLException;

    // New transaction-supporting methods
    void save(Connection conn, T toSave) throws SQLException;
    T[] findAll(Connection conn) throws SQLException;
    T[] findById(Connection conn, int id) throws SQLException;
    void update(Connection conn, T toUpdate) throws SQLException;
    void delete(Connection conn, int id) throws SQLException;
    T[] findWithPagination(Connection conn, int indexFrom, int limit) throws SQLException;

    // Add to InterfaceDao interface
    T[] createInstanceFromQuery(String sqlQuery) throws SQLException;
    T[] createInstanceFromQuery(Connection conn, String sqlQuery) throws SQLException;
}