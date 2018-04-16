package tdt4140.gr1817.app.core.feature.user.vitaldata;

import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A series of {@link DataPoint DataPoints}.
 * Contains border values which are calculated during {@link #setData(List)},
 * such as {@link #getXLowerBound()} and {@link #getYUpperBound()}.
 *
 * @author Kristian Rekstad
 */
public class DataSeries {

    @Getter private long xLowerBound = Long.MAX_VALUE;
    @Getter private long xUpperBound = Long.MIN_VALUE;
    @Getter private float yLowerBound = Float.MAX_VALUE;
    @Getter private float yUpperBound = Float.MIN_VALUE;

    @Getter List<DataPoint> data = Collections.emptyList();

    public DataSeries() {
    }

    public void setData(List<DataPoint> data) {
        xLowerBound = Long.MAX_VALUE;
        xUpperBound = Long.MIN_VALUE;
        yLowerBound = Long.MAX_VALUE;
        yUpperBound = Long.MIN_VALUE;

        // Usually bad practice to store references to mutable lists. Use copies instead
        ArrayList<DataPoint> dataCopy = new ArrayList<>(data.size());

        for (DataPoint datum : data) {
            xLowerBound = Math.min(xLowerBound, datum.timestamp);
            xUpperBound = Math.max(xUpperBound, datum.timestamp);
            yLowerBound = Math.min(yLowerBound, datum.value);
            yUpperBound = Math.max(yUpperBound, datum.value);

            dataCopy.add(datum);
        }

        this.data = dataCopy;
    }

    @Value
    public static class DataPoint {
        long timestamp;
        float value;
    }
}
