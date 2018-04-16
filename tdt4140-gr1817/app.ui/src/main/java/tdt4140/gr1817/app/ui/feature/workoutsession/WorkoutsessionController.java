package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.core.feature.workoutsession.GetAllWorkoutSessions;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

import javax.inject.Provider;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import java.sql.ResultSet;

public class WorkoutsessionController {


    private final ObservableList<WorkoutsessionController.SessionRow> sessionRowList = FXCollections
            .observableArrayList();
    private final UserSelectionService userSelectionService;
    private final Navigator navigator;
    private final Optional<UserSelectionService.UserId> selectedUser;
    private final ObservableList<WorkoutsessionController.SessionRow> sessionRowList = FXCollections.observableArrayList();
    private final Navigator navigator;
    private final Provider<GetAllWorkoutSessions> getAllWorkoutSessionsProvider;
    private final WorkoutSessionAdapter workoutSessionAdapter;

    @FXML
    private TableView<WorkoutsessionController.SessionRow> workoutTable;

    @FXML
    private TableColumn<WorkoutsessionController.SessionRow, String> dateCol;

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
                                    Provider<GetAllWorkoutSessions> getAllWorkoutSessionsProvider,
                                    WorkoutSessionAdapter workoutSessionAdapter,
                                    Navigator navigator) {
        this.userSelectionService = userSelectionService;
        selectedUser = userSelectionService.getSelectedUserId();
        this.getAllWorkoutSessionsProvider = getAllWorkoutSessionsProvider;
        this.workoutSessionAdapter = workoutSessionAdapter;
        this.navigator = navigator;
    }

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        intensityCol.setCellValueFactory(new PropertyValueFactory<>("Intensity"));
        kCalCol.setCellValueFactory(new PropertyValueFactory<>("KCal"));
        heartrateCol.setCellValueFactory(new PropertyValueFactory<>("Heartrate"));
        maxHRCol.setCellValueFactory(new PropertyValueFactory<>("MaxHR"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        workoutTable.setItems(sessionRowList);
        loadWorkoutSessions();

    }

    public void loadWorkoutSessions() {
        int loggedBy = selectedUser.get().getId();
        final List<SessionRow> sessionRows = getAllWorkoutSessionsProvider.get().getAll(loggedBy)
                .stream().map(workoutSessionAdapter::adapt)
                .collect(Collectors.toList());

        sessionRowList.setAll(sessionRows);
    }

    @FXML
    public void goToViewUser() {
        navigator.navigate(Page.VIEW_USER);
    }

    public static class SessionRow {
        private Date date;
        private SimpleIntegerProperty id;
        private SimpleIntegerProperty intensity;
        private SimpleFloatProperty kCal;
        private SimpleFloatProperty heartrate;
        private SimpleFloatProperty maxHR;
        private SimpleFloatProperty distance;
        private SimpleIntegerProperty duration;

        public SessionRow(Date date, int id, int intensity, float kCal, float heartrate, float maxHR, float distance,
                          int duration) {
            this.date = date;
            this.id = new SimpleIntegerProperty(id);
            this.intensity = new SimpleIntegerProperty(intensity);
            this.kCal = new SimpleFloatProperty(kCal);
            this.heartrate = new SimpleFloatProperty(heartrate);
            this.maxHR = new SimpleFloatProperty(maxHR);
            this.distance = new SimpleFloatProperty(distance);
            this.duration = new SimpleIntegerProperty(duration);
        }

        public Date getDate() {
            return date;
        }

        public int getID() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public int getIntensity() {
            return intensity.get();
        }

        public SimpleIntegerProperty intensityProperty() {
            return intensity;
        }

        public float getKCal() {
            return kCal.get();
        }

        public SimpleFloatProperty kCalProperty() {
            return kCal;
        }

        public float getHeartrate() {
            return heartrate.get();
        }

        public SimpleFloatProperty heartrateProperty() {
            return heartrate;
        }

        public float getmaxHR() {
            return maxHR.get();
        }

        public SimpleFloatProperty maxHRProperty() {
            return maxHR;
        }

        public float getDistance() {
            return distance.get();
        }

        public SimpleFloatProperty distanceProperty() {
            return distance;
        }

        public int getDuration() {
            return duration.get();
        }

        public SimpleIntegerProperty durationProperty() {
            return duration;
        }
    }
}
