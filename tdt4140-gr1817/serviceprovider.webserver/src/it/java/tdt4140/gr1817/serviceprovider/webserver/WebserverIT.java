package tdt4140.gr1817.serviceprovider.webserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class WebserverIT {

    private App app;

    @Before
    public void setUp() throws Exception {
        final Injector injector = Guice.createInjector();
        app = new App(2222, injector);
        app.start();
    }

    //TODO: Write web server integration tests
}