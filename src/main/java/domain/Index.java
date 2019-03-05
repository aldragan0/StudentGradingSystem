package domain;

/**
 * class for managing indexes operation on student id assign
 */
public class Index {
    private Index(){}
    private static long startIndex = 1000;
    public static void nextIndex(){
        ++startIndex;
    }

    /**
     * resets the value of the index
     */
    public static void resetIndex(){
        Index.startIndex = 1000;
    }

    public static long getIndex(){
        return Index.startIndex;
    }

    /**
     * returns the index of an id, if the id is a generated one
     * @param id string to get the index from
     * @return index from id, if the id is a generated one
     *         -1, otherwise
     */
    public static long getIndexId(String id){
        long index = -1;
        if(id.matches("[a-z]{2}[0-9]{4,}")){
            index = Long.parseLong(id.substring(2));
        }
        return index;
    }

    public static void setIndex(long startIndex){
        Index.startIndex = startIndex;
    }
}
