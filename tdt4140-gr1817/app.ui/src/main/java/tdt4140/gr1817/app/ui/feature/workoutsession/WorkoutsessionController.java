package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.core.feature.workoutsession.GetAllWorkoutSessions;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkoutsessionController {

    private int loggedBy;final ObservableList<SessionRow> sessionRowList = FXCollections
            .observableArrayList();
    private final UserSelectionService userSelectionService;
    private final Navigator navigator;
    private final Optional<UserSelectionService.UserId> selectedUser;
    private final ObservableList<WorkoutsessionController.SessionRow> sessionRowList = FXCollections.observableArrayList();
    private final Navigator navigator;
    private final Provider<GetAllWorkoutSessions> getAllWorkoutSessionsProvider;
    private final WorkoutSessionAdapter workoutSessionAdapter;

    @FXML
    private TableView<SessionRow> workoutTable;

    @FXML
    private TableColumn<SessionRow, String> dateCol;

    @FXML
    private TableColumn<SessionRow, Integer> intensityCol;

    @FXML
    private TableColumn<SessionRow, Integer> kCalCol;

    @FXML
    private TableColumn<SessionRow, Integer> heartrateCol;

    @FXML
    private TableColumn<SessionRow, Integer> maxHeartRateCol;

    @FXML
    private TableColumn<SessionRow, Integer> distanceCol;

    @FXML
    private TableColumn<SessionRow, Integer> durationCol;

    @FXML
    private TextField date1;

    @FXML
    private TextField date2;



    @Inject
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
        heartrateCol.setCellValueFactory(new PropertyValueFactory<>("heartrate"));
        maxHeartRateCol.setCellValueFactory(new PropertyValueFactory<>("maxHeartRate"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        if (selectedUser.isPresent()) {
            loggedBy = selectedUser.get().getId();
        }
        workoutTable.setItems(sessionRowList);
        loadWorkoutSessions();

    }

    public void loadWorkoutSessions() {
        final List<SessionRow> sessionRows = getAllWorkoutSessionsProvider.get().getAll(loggedBy)
                .stream().map(workoutSessionAdapter::adapt)
                .collect(Collectors.toList());
        sessionRowList.setAll(sessionRows);
    }

    @FXML
    public void goToViewUser() {
        navigator.navigate(Page.VIEW_USER);
    }

    //@FXML
    //public void loadWorkoutSessionsWithinDateRange() {
        //final List<SessionRow> sessionRows = getAllWorkoutSessionsProvider.get().getByDate()
    //}

}
