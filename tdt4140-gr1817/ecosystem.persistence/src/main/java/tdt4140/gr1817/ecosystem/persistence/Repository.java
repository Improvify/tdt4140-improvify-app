package tdt4140.gr1817.ecosystem.persistence;

import java.util.List;

/**
 * A general repository interface, specifying common actions for a repository.
 * <p>
 * Implement this interface for each data class that is persisted.
 *
 *
 * @param <T> The data class that the repository handles
 * @see Specification
 */
public interface Repository<T> {

    // For help in the repository pattern, check out https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006

    /**
     * Add an item to the repository
     * @param item the item to be added
     */
    void add(T item);

    /**
     * Add all items to the repository, ignoring duplicates
     * @param items items to be added
     */
    void add(Iterable<T> items);

    /**
     * Update the repository with a matching identifyer as {@code item} to
     * contain the new values in {@code item}.
     * @param item the item to update
     */
    void update(T item);

    /**
     * Remove an item that matches the identifyer in {@code item}.
     * @param item
     */
    void remove(T item);

    /**
     * Remove all items in {@code items} from persistence
     * @param items
     */
    void remove(Iterable<T> items);

    /**
     * Remove all items that match the {@code specification} from persistence
     * @param specification
     */
    void remove(Specification specification);

    /**
     * Gets all items that match the specification
     * @param specification
     * @return
     */
    List<T> query(Specification specification);
}
