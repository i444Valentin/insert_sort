package com.example.insert_sort;

import com.example.insert_sort.DBHandler.SQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainController extends ExchangeData {

    @FXML
    private AnchorPane messageBox;

    @FXML
    private Label messageText;

    @FXML
    private TextField sequenceField;

    @FXML
    private TextField sortedSequenceField;

    @FXML
    private Spinner<Integer> arraySizeSpinner;

    private static final Integer MAX_SEQUENCE = 1000000;
    private static final Integer MIN_SEQUENCE = 1;
    private final Integer initValue = 1;
    private ArrayList<Integer> array = new ArrayList<>();

    @FXML
    private TextField minNumberField;

    @FXML
    private TextField maxNumberField;

    @FXML
    void generateSequence(ActionEvent event) {
        if (isNullField(minNumberField)) {
            setVisibleMessage(true);
            messageText.setText("Не введено минимальное число");
            return;
        }
        if (isNullField(maxNumberField)) {
            setVisibleMessage(true);
            messageText.setText("Не введено максимальное число");
            return;
        }
        if (Integer.valueOf(minNumberField.getText()) > Integer.valueOf(maxNumberField.getText())){
            setVisibleMessage(true);
            messageText.setText("Минимальное число не может быть больше максимального");
            return;
        }

        setVisibleMessage(false);
        Integer min = Integer.valueOf(minNumberField.getText());
        Integer max = Integer.valueOf(maxNumberField.getText());
        Integer size = Integer.valueOf(arraySizeSpinner.getValue());
        array = arrayFromSize(min, max, size);
        String stringArray = toStringFromArray(array);
        sequenceField.setText(stringArray);
    }

    private String toStringFromArray(ArrayList<Integer> array) {
        StringBuilder buildString = new StringBuilder();
        for (Integer number : array) {
            buildString.append(number).append(", ");
        }
        buildString = new StringBuilder(buildString.substring(0, buildString.length() - 2));
        return buildString.toString();
    }

    private boolean isNullField(TextField field) {
        if (field == null || field.getText().equals("")) {
            return true;
        }
        return false;
    }

    private ArrayList<Integer> arrayFromSize(Integer minValue, Integer maxValue, Integer sizeArray) {
        ArrayList<Integer> sequence = new ArrayList<>();
        for (int i = 0; i < sizeArray; i++) {
            sequence.add(minValue + (int) (Math.random() * maxValue));
        }
        return sequence;
    }

    @FXML
    void sortSequence(ActionEvent event) throws SQLException {
        if (sequenceField.getText().isEmpty()){
            setVisibleMessage(true);
            messageText.setText("Сначала введите последовательность");
            return;
        }
        setVisibleMessage(false);
        String[] strNumbers = sequenceField.getText().split(", ");
        ArrayList<Integer> arrayInteger = getIntegerArray(new ArrayList<String>(Arrays.asList(strNumbers)));
        ArrayList<Integer> sorted = sortInsert(arrayInteger);

        String stringSorted = toStringFromArray(sorted);
        sortedSequenceField.setText(stringSorted);

        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.activateConnection();
        sqlConnection.setSequence(ExchangeData.idUser,sequenceField.getText(),sortedSequenceField.getText());
    }

    /**
     * Алгоритм, реализующий сортировку вставками.
     * Данный алгоритм работает с натуральными числами типа integer.
     * @param array - массив
     * @return - отсортированный массив
     */
    private ArrayList<Integer> sortInsert(ArrayList<Integer> array) {
        for (int i = 1; i < array.size(); i++) {
            int temp = array.get(i);
            int j = i;
            while (j > 0 && array.get(j - 1) >= temp) {
                array.set(j, array.get(j - 1));
                j--;
            }
            array.set(j, temp);
        }
        return array;
    }

    @FXML
    void initialize() throws SQLException {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory
                .IntegerSpinnerValueFactory(MIN_SEQUENCE, MAX_SEQUENCE, initValue);

        arraySizeSpinner.setValueFactory(valueFactory);
        setVisibleMessage(false);
        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.activateConnection();
        sqlConnection.setAudit(1);
    }

    void setVisibleMessage(boolean isVisible) {
        messageBox.setVisible(isVisible);
        messageText.setVisible(isVisible);
    }

    private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
            }
        }
        return result;
    }

}
