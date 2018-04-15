package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DataFormat;

public class WorkoutsessionController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private final ObservableList<WorkoutsessionController.SessionRow> SessionRowList = FXCollections.observableArrayList();

    @FXML
    private TableView<WorkoutsessionController.SessionRow> workoutTable;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, String> dateCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> IDCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> IntensityCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> KCalCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> HeartrateCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> maxHRCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> distanceCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> durationCol;


    public void changeDateEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setDate(edittedCell.getNewValue().toString());
    }

    public void changeIDEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setID(edittedCell.getNewValue().toString());
    }

    public void changeIntensityEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setIntensity(edittedCell.getNewValue().toString());
    }

    public void changeKCalEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setKCal(edittedCell.getNewValue().toString());
    }

    public void changeHeartrateEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setHeartrate(edittedCell.getNewValue().toString());
    }

    public void changemaxHREvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setMaxHR(edittedCell.getNewValue().toString());
    }

    public void changeDistanceEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setDistance(edittedCell.getNewValue().toString());
    }

    public void changeDurationEvent(TableColumn.CellEditEvent edittedCell) {
        WorkoutsessionController.SessionRow workWork = workoutTable.getSelectionModel().getSelectedItem();
        workWork.setDuration(edittedCell.getNewValue().toString());
    }

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        IntensityCol.setCellValueFactory(new PropertyValueFactory<>("Intensity"));
        KCalCol.setCellValueFactory(new PropertyValueFactory<>("KCal"));
        HeartrateCol.setCellValueFactory(new PropertyValueFactory<>("Heartrate"));
        maxHRCol.setCellValueFactory(new PropertyValueFactory<>("MaxHR"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));

    }

    public static class SessionRow {
        private SimpleStringProperty date;
        private SimpleIntegerProperty ID;
        private SimpleIntegerProperty Intensity;
        private SimpleIntegerProperty KCal;
        private SimpleIntegerProperty Heartrate;
        private SimpleIntegerProperty maxHR;
        private SimpleIntegerProperty Distance;
        private SimpleIntegerProperty Duration;

        public SessionRow(String date, int ID, int Intensity, int KCal, int Heartrate, int maxHR, int Distance, int Duration) {
            this.date = new SimpleStringProperty(date);
            this.ID = new SimpleIntegerProperty(ID);
            this.Intensity = new SimpleIntegerProperty(Intensity);
            this.KCal = new SimpleIntegerProperty(KCal);
            this.Heartrate = new SimpleIntegerProperty(Heartrate);
            this.maxHR = new SimpleIntegerProperty(maxHR);
            this.Distance = new SimpleIntegerProperty(Distance);
            this.Duration = new SimpleIntegerProperty(Duration);
        }

        //date
        public void setDate(String w) {
            this.date = new SimpleStringProperty(w);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }


        //ID
        public void setID(String s) {
            int i = Integer.parseInt(s);
            this.ID = new SimpleIntegerProperty(i);
        }

        public int getID() {
            return ID.get();
        }

        public SimpleIntegerProperty IDProperty() {
            return ID;
        }


        //Intensity
        public void setIntensity(String s) {
            int i = Integer.parseInt(s);
            this.Intensity = new SimpleIntegerProperty(i);
        }

        public int getIntensity() {
            return Intensity.get();
        }

        public SimpleIntegerProperty intensityProperty() {
            return Intensity;
        }


        //KCAL
        public void setKCal(String w) {
            int i = Integer.parseInt(w);
            this.KCal = new SimpleIntegerProperty(i);
        }

        public int getKCal() {
            return KCal.get();
        }

        public SimpleIntegerProperty KCalProperty() {
            return KCal;
        }


        //Heartrate
        public void setHeartrate(String s) {
            int i = Integer.parseInt(s);
            this.Heartrate = new SimpleIntegerProperty(i);
        }

        public int getHeartrate() {
            return Heartrate.get();
        }

        public SimpleIntegerProperty HeartrateProperty() {
            return Heartrate;
        }


        //maxhr
        public void setMaxHR(String s) {
            int i = Integer.parseInt(s);
            this.maxHR = new SimpleIntegerProperty(i);
        }

        public int getmaxHR() {
            return maxHR.get();
        }

        public SimpleIntegerProperty maxHRProperty() {
            return maxHR;
        }


        //distance
        public void setDistance(String s) {
            int i = Integer.parseInt(s);
            this.Distance = new SimpleIntegerProperty(i);
        }

        public int getDistance() {
            return Distance.get();
        }

        public SimpleIntegerProperty distanceProperty() {
            return Distance;
        }


        //duration
        public void setDuration(String s) {
            int i = Integer.parseInt(s);
            this.Duration = new SimpleIntegerProperty(i);
        }

        public int getDuration() {
            return Duration.get();
        }

        public SimpleIntegerProperty durationProperty() {
            return Duration;
        }
    }
}
