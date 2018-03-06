package validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;

public class GoalValidator implements Validator {

    private Gson gson;

    public GoalValidator() {
        this.gson = new Gson();
    }

    @Override
    public boolean validate(String json) {
        try {
            Goal goal = gson.fromJson(json, Goal.class);

            return isValidID(goal.getId());

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
