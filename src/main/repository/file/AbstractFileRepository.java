package main.repository.file;

import main.domain.Entity;
import main.exceptions.RepositoryException;
import main.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String fileName;

    /**
     * Constructor
     * @param fileName the name of the file that contains the entities
     * @throws IOException if the file cannot be found/opened
     * @throws RepositoryException if the entities in the file cannot compose a valid repository
     */
    public AbstractFileRepository(String fileName) throws IOException, RepositoryException {
        this.fileName = fileName;
        loadData();
    }

    /**
     * Loads the data from the file into the memory repository
     * @throws IOException if the file cannot be found/opened
     * @throws RepositoryException if the entities in the file cannot compose a valid repository
     */
    private void loadData() throws IOException, RepositoryException {
        super.clear();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            String[] parts = data.split(";");
            E entity = extractEntity(Arrays.asList(parts));
            super.save(entity);
        }
    }

    /**
     * Adds information about an entity to the file
     * @param entity the entity whose information will be added to the file
     * @throws IOException if the file cannot be found/opened
     */
    private void writeToFile(E entity) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
        bufferedWriter.write(createEntityAsString(entity));
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    /**
     * Write data from memory to file
     * @throws IOException if the file cannot be found/opened
     */
    private void writeAllToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        Collection<E> entities = super.getAll();
        for (E entity : entities) {
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /**
     * Add a new entity to the repository
     * @param new_entity the entity to be added to the repository
     * @throws RepositoryException if the entity cannot be added to the repository
     * @throws IOException if the file cannot be found/opened
     */
    @Override
    public void save(E new_entity) throws RepositoryException, IOException {
        loadData();
        super.save(new_entity);
        writeToFile(new_entity);
    }

    /**
     * Delete an entity from the repository
     * @param ID entity ID
     * @throws RepositoryException if this entity does not exist in the repository or entity's ID is null
     * @throws IOException if the file cannot be found/opened
     */
    @Override
    public void delete(ID ID) throws RepositoryException, IOException {
        loadData();
        super.delete(ID);
        writeAllToFile();
    }

    /**
     * Converts a string to an entity
     * @param attributes the list of substrings to be transformed into an entity
     * @return the entity corresponding to the string
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Converts an entity to a string
     * @param entity the entity to be transformed into a string
     * @return the string corresponding to the entity
     */
    public abstract String createEntityAsString(E entity);
}
