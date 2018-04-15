package tdt4140.gr1817.app.ui.feature.workout;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import tdt4140.gr1817.app.core.feature.user.GetAllUsers;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.stream.Collectors;


/**
 * JavaFX controller for creating workouts.
 * <p>
 * Created by Fredrik Jenssen and Pål Fossnes.
 *
 * @author Fredrik Jenssen
 * @author Pål Fossnes
 */
public class CreateWorkoutController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private final ObservableList<WorkoutRow> workoutRowList = FXCollections.observableArrayList();
    final ObservableList<UserListItem> userListItems = FXCollections.observableArrayList();
    private Provider<GetAllUsers> getAllUsersProvider;
    private UserListItemAdapter userListItemAdapter;

    @FXML
    private ComboBox userDropdown;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField titleField;
    @FXML
    private TableView<WorkoutRow> workoutTable;
    @FXML
    private TableColumn<WorkoutRow, String> whatCol;
    @FXML
    private TableColumn<WorkoutRow, Integer> timeCol;
    @FXML
    private TableColumn<WorkoutRow, Integer> intensityCol;
    @FXML
    private TableColumn<WorkoutRow, String> commentCol;

    @Inject
    public CreateWorkoutController(Provider<GetAllUsers> getAllUsersProvider,
                                   UserListItemAdapter userListItemAdapter) {
        this.getAllUsersProvider = getAllUsersProvider;
        this.userListItemAdapter = userListItemAdapter;
    }

    public void changeWhatEvent(TableColumn.CellEditEvent editedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setWhat(editedCell.getNewValue().toString());
    }

    public void changeTimeEvent(TableColumn.CellEditEvent editedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setTime(editedCell.getNewValue().toString());
    }

    public void changeIntensityEvent(TableColumn.CellEditEvent editedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setIntensity(editedCell.getNewValue().toString());
    }

    public void changeCommentEvent(TableColumn.CellEditEvent editedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setComment(editedCell.getNewValue().toString());
    }

    @FXML
    public void initialize() {
        whatCol.setCellValueFactory(new PropertyValueFactory<>("what"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        intensityCol.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        //Test data
        WorkoutRow test1 = new WorkoutRow("bla", 2, 3, "bla");
        WorkoutRow test2 = new WorkoutRow("biatch", 1, 1, "haha");
        WorkoutRow test3 = new WorkoutRow("blå", 4, 10, "nigguh");
        workoutRowList.add(test1);
        workoutRowList.add(test2);
        workoutRowList.add(test3);

        workoutTable.setItems(workoutRowList);
        workoutTable.setEditable(true);
        workoutTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        workoutTable.getStylesheets().add("tdt4140/gr1817/app/ui/stylesheets/tableview_stylesheet.css");

        workoutTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (workoutTable.isFocused()) {
                    deleteButton.setDisable(false);
                }
            }
        });

        userDropdown.setItems(userListItems);

        addButton.setOnAction(addButtonHandler);
        deleteButton.setOnAction(deleteButtonHandler);
        deleteButton.setDisable(true);
        saveButton.setOnAction(saveButtonHandler);

        dragRow();
        loadUsers();

        IntegerStringConverter converter = new IntegerStringConverter();

        whatCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        intensityCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        commentCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userDropdown.setCellFactory((comboBox) -> {
            return new ListCell<UserListItem>() {
                @Override
                protected void updateItem(UserListItem userListItem, boolean empty) {
                    super.updateItem(userListItem, empty);

                    if (userListItem == null || empty) {
                        setText(null);
                    } else {
                        setText(userListItem.getUsername());
                    }
                }
            };
        });

        userDropdown.setConverter(new StringConverter<UserListItem>() {
            @Override
            public String toString(UserListItem userListItem) {
                if (userListItem == null) {
                    return null;
                } else {
                    return userListItem.getUsername();
                }
            }

            @Override
            public UserListItem fromString(String string) {
                return null;
            }
        });
    }


    EventHandler<ActionEvent> addButtonHandler =
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    WorkoutRow testRow = new WorkoutRow();
                    int selectedIndex = workoutTable.getSelectionModel().getSelectedIndex();
                    workoutTable.getSelectionModel().getSelectedItem();
                    workoutRowList.add(selectedIndex + 1, testRow);
                }
            };

    EventHandler<ActionEvent> deleteButtonHandler =
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    workoutRowList.removeAll(workoutTable.getSelectionModel().getSelectedItems());
                    workoutTable.getSelectionModel().clearSelection();
                    deleteButton.setDisable(true);
                }
            };

    EventHandler<ActionEvent> saveButtonHandler =
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (isValid()) {
                        int i = 0;
                    }
                }
            };

    private boolean isValid() {
        Alert emptyTitleAlert
                = new Alert(Alert.AlertType.WARNING, "Missing workout plan title", ButtonType.OK);
        Alert emptyWorkoutPlan
                = new Alert(Alert.AlertType.WARNING, "Your workout plan must contain at least 1 row", ButtonType.OK);
        Alert missingCellInformation
                = new Alert(Alert.AlertType.WARNING, "One or more fields are empty", ButtonType.OK);
        Alert userNotSelected
                = new Alert(Alert.AlertType.WARNING, "Please select a user", ButtonType.OK);

        //Checks if title is missing
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            emptyTitleAlert.showAndWait();
            return false;
        }
        //Checks if rows are missing
        if (workoutRowList.isEmpty()) {
            emptyWorkoutPlan.showAndWait();
            return false;
        }
        //Checks if user is selected
        if (userDropdown.getSelectionModel().isEmpty()) {
            userNotSelected.showAndWait();
            return false;
        }

        //Iterate over table and see if there are any missing fields
        for (WorkoutRow row : workoutRowList) {
            if (row.what == null) {
                missingCellInformation.showAndWait();
                return false;
            } else {
                String temp = row.what.toString().trim().substring(23, row.what.toString().length() - 1);
                if (row.intensity == null || row.time == null
                        || row.what == null || temp.isEmpty() || temp.trim().isEmpty()) {
                    missingCellInformation.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    public void dragRow() {
        workoutTable.setRowFactory(tv -> {
            TableRow<WorkoutRow> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    WorkoutRow draggedWorkoutRow = workoutTable.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = workoutTable.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    workoutTable.getItems().add(dropIndex, draggedWorkoutRow);

                    event.setDropCompleted(true);
                    workoutTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row;
        });

    }

    public void loadUsers() {
        final List<UserListItem> userItems = getAllUsersProvider.get().getAll()
                .stream()
                .map(userListItemAdapter::adapt)
                .collect(Collectors.toList());

        userListItems.setAll(userItems);
    }

    public static class WorkoutRow {
        private SimpleStringProperty what;
        private SimpleIntegerProperty time;
        private SimpleIntegerProperty intensity;
        private SimpleStringProperty comment;

        public WorkoutRow(String what, int time, int intensity, String comment) {
            this.what = new SimpleStringProperty(what);
            this.time = new SimpleIntegerProperty(time);
            this.intensity = new SimpleIntegerProperty(intensity);
            this.comment = new SimpleStringProperty(comment);

        }

        public WorkoutRow() {
        }

        public void setWhat(String w) {
            this.what = new SimpleStringProperty(w);
        }

        public String getWhat() {
            return what.get();
        }

        public SimpleStringProperty whatProperty() {
            return what;
        }

        public void setTime(String s) {
            int t = Integer.parseInt(s);
            this.time = new SimpleIntegerProperty(t);
        }

        public int getTime() {
            return time.get();
        }

        public SimpleIntegerProperty timeProperty() {
            return time;
        }

        public void setIntensity(String s) {
            int i = Integer.parseInt(s);
            this.intensity = new SimpleIntegerProperty(i);
        }

        public int getIntensity() {
            return intensity.get();
        }

        public SimpleIntegerProperty intensityProperty() {
            return intensity;
        }

        public void setComment(String c) {
            this.comment = new SimpleStringProperty(c);
        }

        public String getComment() {
            return comment.get();
        }

        public SimpleStringProperty commentProperty() {
            return comment;
        }
    }
}

