package com.example.insert_sort;

import com.example.insert_sort.DBHandler.SQLConnection;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController extends ExchangeData {

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    void loginButtonClick(ActionEvent event) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String login = loginField.getText();
        String password = passwordField.getText();
        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.activateConnection();
        ResultSet resultSet = sqlConnection.getUser(login, password);
        Integer integer = 1;
        int counter = 0;

        while (true) {
            try {
                if (!resultSet.next()){
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            counter++;
        }

        if (counter >= 1) {
            SceneLoader.load("main_window.fxml",false,false,"Сортировка вставками");
            ExchangeData.currentUser = login;

            ExchangeData.idUser = integer;
            System.out.println("Success!");
            loginField.getScene().getWindow().hide();

        }
    }

    @FXML
    void initialize(){
    }
}

