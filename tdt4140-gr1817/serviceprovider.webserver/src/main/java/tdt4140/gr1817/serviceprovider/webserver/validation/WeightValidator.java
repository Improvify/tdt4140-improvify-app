package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import javax.inject.Inject;
import java.util.Date;

public class WeightValidator implements Validator {

    private final Gson gson;

    @Inject
    public WeightValidator(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean validate(String json) {
        try {
            Weight weight = gson.fromJson(json, Weight.class);

            return (isValidWeight(weight.getCurrentWeight())
                    && isValidDate(weight.getDate())
            );

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks that the weight is a positive number.
     *
     * @param weight The weight to be checked.
     * @return If the weight is valid.
     */
    private boolean isValidWeight(float weight) {
        return weight > 0;
    }

    /**
     * Checks that the date is in the past.
     *
     * @param date The date to be checked.
     * @return If the date is valid.
     */
    private boolean isValidDate(Date date) {
        return date.before(new Date());
    }
}
