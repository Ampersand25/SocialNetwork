package main.repository.memory;

import main.domain.Entity;
import main.exceptions.RepositoryException;
import main.repository.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;

    /**
     * Constructor - creates the HashMap
     */
    public InMemoryRepository() {
        entities = new HashMap<>();
    }

    /**
     * Add an entity to the repository
     * @param new_entity the entity to be added to the repository
     * @throws RepositoryException if the entity (or an entity with the same ID) already exists in the repository
     * @throws IOException if the file cannot be found/opened (used in subclasses)
     */
    @Override
    public void save(E new_entity) throws RepositoryException, IOException {
        if (new_entity == null)
            throw new IllegalArgumentException("Entity must be not null.");
        if (entities.get(new_entity.getID()) != null)
            throw new RepositoryException("Entity with the same ID already exists in the repository.");
        for (Map.Entry<ID, E> entity : entities.entrySet()) {
            if (entity.getValue().equals(new_entity))
                throw new RepositoryException("This entity already exists in the repository.");
        }
        entities.put(new_entity.getID(), new_entity);
    }

    /**
     * Search for an entity in the repository by ID
     * @param ID entity ID
     * @return the entity, or null if there is no entity with that ID in the repository
     */
    @Override
    public E getOne(ID ID) {
        if (ID == null)
            throw new IllegalArgumentException("ID must be not null.");
        return entities.get(ID);
    }

    /**
     * Get a collection of all entities in the repository
     * @return a collection of all entities in the repository
     */
    @Override
    public Collection<E> getAll() {
        return entities.values();
    }

    /**
     * Updates an entity in the repository
     * @param entity the entity to be updated
     * @throws RepositoryException if there is no entity with the same ID in the repository
     */
    @Override
    public void update(E entity) throws RepositoryException {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null.");
        if (entities.get(entity.getID()) == null)
            throw new RepositoryException("There is no entity with the ID " + entity.getID() + " in the repository.");
        entities.put(entity.getID(), entity);
    }

    /**
     * Deletes an entity from the repository, by its ID
     * @param ID the ID of the entity to be deleted
     * @throws RepositoryException if there is no entity with that ID in the repository
     * @throws IOException if the file cannot be found/opened (used in subclasses)
     */
    @Override
    public void delete(ID ID) throws RepositoryException, IOException {
        if (ID == null)
            throw new IllegalArgumentException("ID must be not null.");
        if (entities.get(ID) == null)
            throw new RepositoryException("There is no entity having ID " + ID + " in the repository.");
        entities.remove(ID);
    }

    /**
     * Clears the repository (deletes all entities)
     */
    @Override
    public void clear() {
        entities.clear();
    }
}
