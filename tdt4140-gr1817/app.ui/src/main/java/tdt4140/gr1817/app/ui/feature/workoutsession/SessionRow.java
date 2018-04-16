package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SessionRow {
    private SimpleStringProperty date;
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty intensity;
    private SimpleIntegerProperty kCal;
    private SimpleIntegerProperty heartrate;
    private SimpleIntegerProperty maxHeartRate;
    private SimpleIntegerProperty distance;
    private SimpleIntegerProperty duration;
    private SimpleIntegerProperty loggedBy;

    public SessionRow(String date, int id, int intensity, int kCal, int heartrate,
                      int maxHeartRate, int distance, int duration, int loggedBy) {
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleIntegerProperty(id);
        this.intensity = new SimpleIntegerProperty(intensity);
        this.kCal = new SimpleIntegerProperty(kCal);
        this.heartrate = new SimpleIntegerProperty(heartrate);
        this.maxHeartRate = new SimpleIntegerProperty(maxHeartRate);
        this.distance = new SimpleIntegerProperty(distance);
        this.duration = new SimpleIntegerProperty(duration);
        this.loggedBy = new SimpleIntegerProperty(loggedBy);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public int getID() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getIntensity() {
        return intensity.get();
    }

    public SimpleIntegerProperty intensityProperty() {
        return intensity;
    }

    public int getKCal() {
        return kCal.get();
    }

    public SimpleIntegerProperty kCalProperty() {
        return kCal;
    }

    public int getHeartrate() {
        return heartrate.get();
    }

    public SimpleIntegerProperty heartrateProperty() {
        return heartrate;
    }

    public int getmaxHeartRate() {
        return maxHeartRate.get();
    }

    public SimpleIntegerProperty maxHeartRateProperty() {
        return maxHeartRate;
    }

    public int getDistance() {
        return distance.get();
    }

    public SimpleIntegerProperty distanceProperty() {
        return distance;
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }
    public int getLoggedBy() {
        return loggedBy.get();
    }
    public SimpleIntegerProperty loggedByProperty() {
        return loggedBy;
    }
}
