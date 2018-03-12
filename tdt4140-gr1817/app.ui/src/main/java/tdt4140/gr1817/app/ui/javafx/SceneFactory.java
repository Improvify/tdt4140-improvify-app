package tdt4140.gr1817.app.ui.javafx;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Creates a {@link Scene} instance.
 *
 * <p>
 * Needed because the scene object can not be instantiated with an invalid root {@link Parent}.
 * Therefore, a unit test must return a mocked scene here, instead of creating a scene with a mocked root.
 * </p>
 */
public class SceneFactory {

    public Scene create(Parent root) {
        return new Scene(root);
    }
}
