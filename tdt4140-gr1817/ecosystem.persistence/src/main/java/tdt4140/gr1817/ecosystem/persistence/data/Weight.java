package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Represents a {@link User}'s weight measurement at a specific date.
 */
@Data
@Builder
public class Weight {
    private int id;
    private float currentWeight;
    private Date date;
    private User user;

    public Weight(int id, float currentWeight, Date date, User user) {
        validateWeight(currentWeight);
        this.id = id;
        this.currentWeight = currentWeight;
        this.date = date;
        this.user = user;
    }

    public void setCurrentWeight(float currentWeight) {
        validateWeight(currentWeight);
        this.currentWeight = currentWeight;
    }

    private static void validateWeight(float weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight can not be negative: " + weight);
        }
    }
}
