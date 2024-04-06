package models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import utility.Config;

public class DbContext {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/itsm";
//    private static final String DB_USER = "";
//    private static final String DB_PASSWORD = "";

	private static final int MAX_CONNECTIONS = 10;

    private BlockingQueue<Connection> connectionPool;
    private static final DbContext instance = new DbContext();

    private DbContext() {
        connectionPool = new ArrayBlockingQueue<>(MAX_CONNECTIONS);
        initializePool();
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver");
        }
    }

    public static DbContext getInstance() {
        return instance;
    }

    private void initializePool() {
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                Connection connection = DriverManager.getConnection(Config.getDBUrl());
                connectionPool.offer(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void releaseConnection(Connection connection) {
        try {
            connectionPool.offer(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}