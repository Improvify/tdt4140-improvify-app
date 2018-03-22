package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;

import javax.inject.Inject;

public class GoalValidator implements Validator {

    private final Gson gson;

    @Inject
    public GoalValidator(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean validate(String json) {
        try {
            gson.fromJson(json, Goal.class);
            return true; //isValidID(goal.getId());

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
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
}
