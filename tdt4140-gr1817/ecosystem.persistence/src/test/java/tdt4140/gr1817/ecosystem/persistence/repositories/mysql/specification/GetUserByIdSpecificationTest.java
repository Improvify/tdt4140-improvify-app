package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetUserByIdSpecificationTest { }
/*
    @Test
    public void shouldGetUserWithCorrectId() {
        GetUserByIdSpecification getUserByIdSpecification = new GetUserByIdSpecification(1);

        MySqlUserRepository repository = new MySqlUserRepository();

        // When
        List<User> users = repository.query(getUserByIdSpecification);

        // Then
        assertThat(users, hasSize(1));
        assertThat(users.get(0).getId(), is(1));
    }
} */