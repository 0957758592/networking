package com.ozzot.networking.constants;

public class Constants {

    public static final String URL = "jdbc:mysql://localhost:3306/testDB?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234567890QQQ";

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8080;

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String GET_ALL = "getAll";
    public static final String GET_BY_ID = "getById";

    public static final String QUERY = "SELECT id, name, dateOfBirth, salary, driverLicense, address FROM user;";
    public static final String QUERY_BY_ID = "SELECT id, name, dateOfBirth, salary, driverLicense, address FROM user WHERE ID = ?;";
}
