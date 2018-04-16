package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DataFormat;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

import javax.inject.Provider;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

//import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.
// specification.improvify.GetWorkoutSessionByLoggedBySpecification;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class WorkoutsessionController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

private final ObservableList<WorkoutsessionController.SessionRow> sessionRowList = FXCollections
            .observableArrayList();
    private final UserSelectionService userSelectionService;
    private final Navigator navigator;
    private final Optional<UserSelectionService.UserId> selectedUser;    private final ObservableList<WorkoutsessionController.SessionRow> sessionRowList = FXCollections.observableArrayList();private final UserSelectionService userSelectionService;
    private final Navigator navigator;
    private final Optional<UserSelectionService.UserId> selectedUser;
    private final Provider<Connection> connection;

    @FXML
    private TableView<WorkoutsessionController.SessionRow> workoutTable;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, String> dateCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> idCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> intensityCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> kCalCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> heartrateCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> maxHRCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> distanceCol;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, Integer> durationCol;

    public WorkoutsessionController(UserSelectionService userSelectionService,
                                    Navigator navigator, Provider<Connection> connection) {
        this.userSelectionService = userSelectionService;
        this.navigator = navigator;
        selectedUser = userSelectionService.getSelectedUserId();
        this.connection = connection;

    }

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        intensityCol.setCellValueFactory(new PropertyValueFactory<>("Intensity"));
        kCalCol.setCellValueFactory(new PropertyValueFactory<>("KCal"));
        heartrateCol.setCellValueFactory(new PropertyValueFactory<>("Heartrate"));
        maxHRCol.setCellValueFactory(new PropertyValueFactory<>("MaxHR"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        //int loggedBy = selectedUser.get().getId();


        /*try {
            GetWorkoutSessionByLoggedBySpecification loggedBySpecification =
                    new GetWorkoutSessionByLoggedBySpecification(loggedBy);
            ResultSet rs = loggedBySpecification.toStatement(this.connection);
            while (rs.next()) {
                sessionRowList.add(new SessionRow(rs.getDate("date"), rs.getInt("id"),
                        rs.getInt("intensity"), rs.getInt("kCal"),
                        rs.getInt("heartrate"),
                        rs.getInt("maxHR"),
                        rs.getInt("duration"),
                        rs.getInt("distance")));
            }
            workoutTable.setItems(sessionRowList);
        } catch (SQLException e) {
            Logger.getLogger(WorkoutsessionController.class.getName()).log(Level.SEVERE, null, e);
        }*/
    }

    @FXML
    public void goToViewUser() {
        navigator.navigate(Page.VIEW_USER);
    }

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


    public static class SessionRow {
        private SimpleDateFormat date;
        private SimpleIntegerProperty id;
        private SimpleIntegerProperty intensity;
        private SimpleIntegerProperty kCal;
        private SimpleIntegerProperty heartrate;
        private SimpleIntegerProperty maxHR;
        private SimpleIntegerProperty distance;
        private SimpleIntegerProperty duration;

        public SessionRow(Date date, int id, int intensity, int kCal, int heartrate, int maxHR, int distance,
                          int duration) {
            //this.date = new SimpleDateFormat("").format(date);
            this.id = new SimpleIntegerProperty(id);
            this.intensity = new SimpleIntegerProperty(intensity);
            this.kCal = new SimpleIntegerProperty(kCal);
            this.heartrate = new SimpleIntegerProperty(heartrate);
            this.maxHR = new SimpleIntegerProperty(maxHR);
            this.distance = new SimpleIntegerProperty(distance);
            this.duration = new SimpleIntegerProperty(duration);
        }

        //date
        public void setDate(String w) {
            this.date = new SimpleDateFormat(w);
        }

        /*public Date getDate() {
            return date;
        }*/

        public SimpleDateFormat dateProperty() {
            return date;
        }


        //ID
        public void setID(String s) {
            int i = Integer.parseInt(s);
            this.id = new SimpleIntegerProperty(i);
        }

        public int getID() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }


        //Intensity
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


        //KCAL
        public void setKCal(String w) {
            int i = Integer.parseInt(w);
            this.kCal = new SimpleIntegerProperty(i);
        }

        public int getKCal() {
            return kCal.get();
        }

        public SimpleIntegerProperty kCalProperty() {
            return kCal;
        }


        //Heartrate
        public void setHeartrate(String s) {
            int i = Integer.parseInt(s);
            this.heartrate = new SimpleIntegerProperty(i);
        }

        public int getHeartrate() {
            return heartrate.get();
        }

        public SimpleIntegerProperty heartrateProperty() {
            return heartrate;
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
            this.distance = new SimpleIntegerProperty(i);
        }

        public int getDistance() {
            return distance.get();
        }

        public SimpleIntegerProperty distanceProperty() {
            return distance;
        }


        //duration
        public void setDuration(String s) {
            int i = Integer.parseInt(s);
            this.duration = new SimpleIntegerProperty(i);
        }

        public int getDuration() {
            return duration.get();
        }

        public SimpleIntegerProperty durationProperty() {
            return duration;
        }
    }
}
