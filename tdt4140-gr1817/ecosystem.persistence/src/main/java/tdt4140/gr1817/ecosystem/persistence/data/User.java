package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private float height;
    private Date birthDate;
    private String username;
    private String password;
    private String email;
}
