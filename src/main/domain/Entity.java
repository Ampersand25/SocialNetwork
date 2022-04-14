package main.domain;

public interface Entity<ID> {
    /**
     * Getter for entity ID
     * @return entity ID
     */
    ID getID();
}
