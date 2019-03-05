package repo;

import domain.HasID;
import validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @param <ID> -type E must have an attribute of type ID
 * @param <E> -type of entities saved in repository
 */
public abstract class AbstractFileRepo<ID extends Comparable<ID>, E extends HasID<ID>> extends InMemoryRepo<ID, E> {
    protected String filename;

    /**
     * @param validator validator(validates entities)
     * @param filename filename associated with repo
     */
    AbstractFileRepo(Validator<E> validator, String filename) {
        super(validator);
        this.filename = filename;
        loadFromFile();
    }

    /**
     * @param line line containing entity fields
     * @return entity
     */
    protected abstract E getEntityFrom(String line);


    /**
     * add a new entity converted from a string to the repo
     * @param line entity content as a string
     */
    private void storeEntityFrom(String line){
        super.save(getEntityFrom(line));
    }

    /**
     *initialize repo contents from file
     */
    protected void loadFromFile(){
        try{
            List<String> lines = Files.readAllLines(Paths.get(filename));
            lines.removeIf(String::isEmpty);
            lines.forEach(this::storeEntityFrom);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * @param writer object used to write to file
     * @param entity the object which is being written to file
     */
    private void writeEntity(BufferedWriter writer, E entity){
        try{
            writer.write(entity.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *store the current repo state in a file
     */
    protected void storeToFile(){
        BiConsumer<BufferedWriter, E> operation = this::writeEntity;

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            super.findAll().forEach(entity->operation.accept(bw, entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * save an entity and record changes to file
     * @param entity - entity must be not null
     * @return null if the entity was stored to file
     */
    @Override
    public E save(E entity){
        E returnedEntity = super.save(entity);
        if(returnedEntity == null) storeToFile();
        return returnedEntity;
    }

    /**
     * remove an entity and record changes to file
     * @param id - id must be not null
     * @return the entity of the specified id, else null
     */
    @Override
    public E delete(ID id){
        E returnedEntity = super.delete(id);
        if(returnedEntity != null) storeToFile();
        return returnedEntity;
    }

    /**
     * update an entity and record changes to file
     * @param entity entity must not be null
     * @return null if entity was updated, else null
     */
    @Override
    public E update(E entity){
        E returnedEntity = super.update(entity);
        if(returnedEntity == null) storeToFile();
        return returnedEntity;
    }
}
