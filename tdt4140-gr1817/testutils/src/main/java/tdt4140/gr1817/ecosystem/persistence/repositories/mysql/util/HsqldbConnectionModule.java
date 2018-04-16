package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.sql.Connection;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class HsqldbConnectionModule extends AbstractModule {

    private final HsqldbRule hsqldbRule;

    public HsqldbConnectionModule(HsqldbRule hsqldbRule) {
        this.hsqldbRule = hsqldbRule;
    }

    @Override
    protected void configure() {

    }

    @Provides
    public Connection provideDataSource() {
        return hsqldbRule.getConnection();
    }
}

