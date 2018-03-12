package tdt4140.gr1817.app.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

@Slf4j
public class Main extends Application {


    private Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        injector = Guice.createInjector(new JavaFxModule(primaryStage));

        Navigator navigator = injector.getInstance(Navigator.class);
        navigator.navigate(getInitialPage());
    }

    protected Page getInitialPage() {
        return Page.CREATE_WORKOUT;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
