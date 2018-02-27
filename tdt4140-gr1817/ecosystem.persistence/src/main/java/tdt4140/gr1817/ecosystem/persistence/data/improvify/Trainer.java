package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;

/**
 * A trainer that works at Improvify.
 *
 * <p>This trainer is the person that eventually will use the javaFX interface
 * for the Improvify service provider.</p>
 */
@Data
public class Trainer {
    private int id;
    private String name;
}
