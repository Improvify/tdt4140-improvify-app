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
            Goal goal = gson.fromJson(json, Goal.class);
            return isValidDescription(goal.getDescription());

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks that the description is not null.
     *
     * @param description The description to be checked.
     * @return If the description is valid.
     */
    private boolean isValidDescription(String description) {
        return description != null;
    }
}
