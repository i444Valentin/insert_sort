package com.example.insert_sort.DBHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class SQLConnection {
    private final String url;
    private final String login;
    private final String password;
    private final String database;
    private final String parameters;
    private Connection connection;

    private SQLConnection(String url, String login, String password, String database, String parameters) {
        this.url = url;
        this.login = login;
        this.password = password;
        this.database = database;
        this.parameters = parameters;
    }

    public SQLConnection(){

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties").toAbsolutePath())) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String database = props.getProperty("database");
        String parameters = props.getProperty("parameters");

        SQLConnection sqlConnection = new SQLConnection(url, username, password, database, parameters);
        this.url = sqlConnection.url;
        this.login = sqlConnection.login;
        this.password = sqlConnection.password;
        this.database = sqlConnection.database;
        this.parameters = sqlConnection.parameters;
    }

    public void activateConnection() throws SQLException {
        String fullUrl = getFullUrl();
        connection = DriverManager.getConnection(fullUrl,login,password);
    }

    private String getFullUrl() {
        return url + "/" + database + "?" + parameters;
    }

    public void connect() throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection conn = connection;
            conn.close();
    }

    public ResultSet getUser(String login, String password){
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + "user" + " WHERE " +
                "login" + "=? AND " + "password" + "=?";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(select);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрошенную команду.");
        }
        return resultSet;
    }

    public void setAudit(Integer idUser){
        String insert = "INSERT INTO audit (date,iduser) VALUE (?,?)";
        PreparedStatement preparedStatement;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate = formatter.format(date);
        try {
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, strDate);
            preparedStatement.setInt(2, idUser);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрошенную команду.");
        }
    }

    public void setSequence (Integer idUser, String beginSequence, String sortedSequence){
        String insert = "INSERT INTO done_sequence (initsequence,sortedsequence,iduser) VALUE (?,?,?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, beginSequence);
            preparedStatement.setString(2, sortedSequence);
            preparedStatement.setInt(3, idUser);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрошенную команду.");
        }
    }


}
