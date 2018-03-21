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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import javax.inject.Inject;


/**
 * JavaFX controller for creating workouts.
 */
public class CreateWorkoutController {

    private final ObservableList<WorkoutRow> workoutRowList = FXCollections.observableArrayList();
    @FXML
    private Button addButton;
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

        workoutTable.setItems(workoutRowList);
        addButton.setOnAction(btnNewHandler);
        workoutTable.setEditable(true);

        IntegerStringConverter converter = new IntegerStringConverter();

        whatCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        intensityCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        commentCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }


    EventHandler<ActionEvent> btnNewHandler =
            new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    WorkoutRow testRow = new WorkoutRow();
                    workoutRowList.add(testRow);
                }
            };

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
        public WorkoutRow(){ }

        public void setWhat(String w) {
            this.what = new SimpleStringProperty(w);
        }

        public String getWhat() {
            return what.get();
        }

        public SimpleStringProperty whatProperty() {
            return what;
        }

        public void setTime(String s){
            int t = Integer.parseInt(s);
            this.time = new SimpleIntegerProperty(t);
        }

        public int getTime() {
            return time.get();
        }

        public SimpleIntegerProperty timeProperty() {
            return time;
        }

        public void setIntensity(String s){
            int i = Integer.parseInt(s);
            this.intensity = new SimpleIntegerProperty(i);
        }
        public int getIntensity() {
            return intensity.get();
        }

        public SimpleIntegerProperty intensityProperty() {
            return intensity;
        }

        public void setComment(String c){
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

