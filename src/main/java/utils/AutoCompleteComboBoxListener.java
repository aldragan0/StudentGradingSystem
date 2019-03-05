package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private ComboBox<T> comboBox;
    private ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    /**
     * @param comboBox the comboBox on which the autoComplete is applied
     */
    public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(event -> comboBox.hide());
        this.comboBox.setOnKeyReleased(this);
    }

    /**
     * handles the keyReleased event
     * as the text is typed it moves the caret
     * and populates the comboBox with the items
     * that contain the string in the textField
     */
    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DELETE
                    || event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }

        String t = comboBox.getEditor().getText();
        ObservableList<T> list = getMatches(t.toLowerCase());

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        updateCaretMove(t, list.isEmpty());
    }

    /**
     * returns an observableList containing all the items where
     * a substring of them occurs
     * @param text substring to match the combo box items to
     * @return matches
     */
    private ObservableList<T> getMatches(String text){
        ObservableList<T> list = FXCollections.observableArrayList();
        for (T aData : data) {
            if(aData.toString().toLowerCase().contains(text)) {
                list.add(aData);
            }
        }
        return list;
    }

    /**
     * updates the position of the caret, updating the comboBox items
     * @param text current displaying text in the comboBox
     * @param isEmpty whether the list of items containing
     *                <@param>text</@param> is empty or not
     */
    private void updateCaretMove(String text, Boolean isEmpty){
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(text.length());
        if(!isEmpty) {
            comboBox.show();
        }
    }

    /**
     * moves the caret to a specified position
     * @param textLength position to move the caret to
     */
    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }

}