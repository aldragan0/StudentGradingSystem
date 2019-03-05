package utils;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.Service;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GuiUtils {
    /**
     * loads the items from the service into the comboBox's list
     * @param service the source of items, must implement the Service<> interface
     * @param getter function to map an object to one of its fields
     * @param comboBoxItems observable list defined for the combo box
     * @param <T> type of the object's field
     * @param <E> type of the elements in the service
     * @param <S> name of the class implementing the service
     */
    public static<T, S extends Service<E>, E>
    void loadComboBoxItems(S service, Function<E, T> getter, ObservableList<T> comboBoxItems){
        comboBoxItems.setAll(service.getAll().stream()
                .map(getter).collect(Collectors.toList()));
    }

    /**
     * initializes the items in a comboBox
     * @param items items to be loaded into the comboBox
     * @param comboBox comboBox to load the items into
     * @param isAutoComplete if comboBox is an AutoComplete
     * @param <T> comboBox elem type
     */
    public static <T> void initComboBox(ObservableList<T> items, ComboBox<T> comboBox,
                                        boolean isAutoComplete){
        comboBox.setItems(items);
        if(isAutoComplete) new AutoCompleteComboBoxListener<>(comboBox);
    }

    /**
     * create and show a new file chooser with the implicit fileName
     * and return a file with the path chosen in the file chooser
     * @param currentFileName file name of the file to be saved
     * @param dirPath implicit path of the directory where
                      the file will be saved
     * @param fileChooserType type of file chooser dialog box
     * @param stage stage to bind the file chooser to
     * @param extensions extension filters to populate the file chooser
     * @return file with the path chosen in the file chooser
     */

    public static File showFileChooser(String currentFileName, String dirPath,
                                       FileChooserType fileChooserType, Stage stage,
                                       FileChooser.ExtensionFilter... extensions){

        FileChooser fileChooser = new FileChooser();
        File currentDirectory = new File(dirPath);
        fileChooser.setInitialDirectory(currentDirectory);
        fileChooser.setInitialFileName(currentFileName);
        fileChooser.getExtensionFilters().addAll(extensions);
        switch (fileChooserType){
            case OPEN:
                return fileChooser.showOpenDialog(stage);
            case SAVE:
                return fileChooser.showSaveDialog(stage);
            default:
                System.out.println("Invalid file chooser type");
                return null;
        }
    }

    /**
     * create and show a new file chooser and return the file with the path
     * chosen in the file chooser
     * @param dirPath implicit path of the directory where
                      the file will be saved
     * @param fileChooserType type of file chooser dialog box
     * @param extensions extension filters
     * @param stage stage to bind the file chooser to
     * @return file with the path chosen in the file chooser
     */
    public static File showFileChooser(String dirPath, FileChooserType fileChooserType,
                                       Stage stage, FileChooser.ExtensionFilter... extensions){
        return showFileChooser("", dirPath, fileChooserType, stage, extensions);
    }

    /**
     * set the style of the items of a combo box
     * @param comboBox object to set the style to
     * @param <T> item type of the combo box
     */
    public static <T> void setComboBoxItemsStyle(ComboBox<T> comboBox){
        comboBox.setCellFactory(param -> new ComboBoxListCell<T>(){{
            setTextFill(Color.valueOf("#E5F0F7"));
            final Background blackBackground = new Background(new BackgroundFill(
                    Color.valueOf("#2E2E2E"),
                    null,
                    null));
            final Background blueBackground = new Background(new BackgroundFill(
            Color.valueOf("#0051A8"),
                    null,
                    null));
            setBackground(blackBackground);
            setOnMouseEntered(event-> setBackground(blueBackground));
            setOnMouseExited(event -> setBackground(blackBackground));
        }});
    }
}
