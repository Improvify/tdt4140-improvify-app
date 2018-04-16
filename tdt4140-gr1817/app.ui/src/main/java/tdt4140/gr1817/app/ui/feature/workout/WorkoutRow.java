package tdt4140.gr1817.app.ui.feature.workout;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WorkoutRow {
    public SimpleStringProperty what;
    public SimpleIntegerProperty time;
    public SimpleStringProperty intensity;
    public SimpleStringProperty comment;

    public WorkoutRow(String what, int time, String intensity, String comment) {
        this.what = new SimpleStringProperty(what);
        this.time = new SimpleIntegerProperty(time);
        this.intensity = new SimpleStringProperty(intensity);
        this.comment = new SimpleStringProperty(comment);
    }

    public WorkoutRow() {
    }

    public void setWhat(String w) {
        this.what = new SimpleStringProperty(w);
    }

    public String getWhat() {
        return what.get();
    }

    public SimpleStringProperty whatProperty() {
        return what;
    }

    public void setTime(String s) {
        int t = Integer.parseInt(s);
        this.time = new SimpleIntegerProperty(t);
    }

    public int getTime() {
        return time.get();
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    public void setIntensity(String s) {
        this.intensity = new SimpleStringProperty(s);
    }

    public String getIntensity() {
        return intensity.get();
    }

    public SimpleStringProperty intensityProperty() {
        return intensity;
    }

    public void setComment(String c) {
        this.comment = new SimpleStringProperty(c);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }
}
