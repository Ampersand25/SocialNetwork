package main.repository;

import main.domain.Entity;
import main.exceptions.RepositoryException;

import java.io.IOException;

/**
 * @param <ID> the type of entities ID (Integer, Long)
 * @param <E> the type of entities that are saved in the repository
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Add an entity to the repository
     * @param entity the entity to be added to the repository
     * @throws RepositoryException if the entity (or an entity with the same ID) already exists in the repository
     * @throws IOException if the file cannot be found/opened (used in subclasses)
     */
    void save(E entity) throws RepositoryException, IOException;

    /**
     * Search for an entity in the repository by ID
     * @param ID entity ID
     * @return the entity, or null if there is no entity with that ID in the repository
     */
    E getOne(ID ID) throws RepositoryException;

    /**
     * Get a collection of all entities in the repository
     * @return a collection of all entities in the repository
     */
    Iterable<E> getAll() throws RepositoryException;

    /**
     * Updates an entity in the repository
     * @param entity the entity to be updated
     * @throws RepositoryException if there is no entity with the same ID in the repository
     */
    void update(E entity) throws RepositoryException, IOException;

    /**
     * Deletes an entity from the repository, by its ID
     * @param ID the ID of the entity to be deleted
     * @throws RepositoryException if there is no entity with that ID in the repository
     * @throws IOException if the file cannot be found/opened (used in subclasses)
     */
    void delete(ID ID) throws RepositoryException, IOException;

    /**
     * Clears the repository (deletes all entities)
     */
    void clear();
}
