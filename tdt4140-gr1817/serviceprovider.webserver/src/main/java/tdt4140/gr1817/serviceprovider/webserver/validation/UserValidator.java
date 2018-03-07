package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Date;

public class UserValidator implements Validator {

    private final Gson gson;

    public UserValidator(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean validate(String json) {
        try {
            User user = gson.fromJson(json, User.class);

            return (isValidName(user.getFirstName())
                    && isValidName(user.getLastName())
                    && isValidEmail(user.getEmail())
                    && isValidBirthDate(user.getBirthDate())
                    && isValidHeight(user.getHeight())
                    && isValidID(user.getId()));

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks that the height is positive.
     *
     * @param height The height to be checked.
     * @return If the height is valid.
     */
    private boolean isValidHeight(float height) {
        return height > 0;
    }

    /**
     * Checks that the ID is positive.
     *
     * @param id The ID to be checked.
     * @return If the ID is valid.
     */
    private boolean isValidID(int id) {
        return id > 0;
    }

    /**
     * Checks that the date is in the past.
     *
     * @param date The date to be checked.
     * @return If the date is valid.
     */
    private boolean isValidBirthDate(Date date) {
        return date.before(new Date());
    }

    /**
     * Checks that the name consists only of letters, whitespace, hyphens and apostrophes.
     *
     * @param name The name to be checked.
     * @return If the name is valid.
     */
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\-'\\s]+");
    }

    /**
     * Checks that the email is in a valid format.
     * NOTE: This does not guarantee that the email exists.
     *
     * @param email The email to be checked.
     * @return If the email is valid.
     */
    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z]+)+");
    }
}
