package tdt4140.gr1817.app.ui.feature.workout;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.converter.IntegerStringConverter;

import javax.inject.Inject;


/**
 * JavaFX controller for creating workouts.
 */
public class CreateWorkoutController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private final ObservableList<WorkoutRow> workoutRowList = FXCollections.observableArrayList();
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
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
    public CreateWorkoutController() {
    }

    public void changeWhatEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setWhat(edittedCell.getNewValue().toString());
    }

    public void changeTimeEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setTime(edittedCell.getNewValue().toString());
    }

    public void changeIntensityEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setIntensity(edittedCell.getNewValue().toString());
    }

    public void changeCommentEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setComment(edittedCell.getNewValue().toString());
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
        workoutTable.getStylesheets().add("tdt4140/gr1817/app/ui/stylesheets/tableview_stylesheet.css");

        addButton.setOnAction(addButtonHandler);
        deleteButton.setOnAction(deleteButtonHandler);

        dragRow();

        IntegerStringConverter converter = new IntegerStringConverter();

        whatCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        intensityCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        commentCol.setCellFactory(TextFieldTableCell.forTableColumn());
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
                    int selectedIndex = workoutTable.getSelectionModel().getSelectedIndex();
                    if (selectedIndex < workoutRowList.size() && selectedIndex > -1) {
                        workoutRowList.remove(selectedIndex);
                    }
                }
            };

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

