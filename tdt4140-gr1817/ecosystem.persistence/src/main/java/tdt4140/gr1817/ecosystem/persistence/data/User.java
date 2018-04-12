package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    private String firstName;
    private String lastName;
    /** Height in centimeters. */
    private float height;
    private Date birthDate;
    private String username;
    private String password;
    private String email;
}
