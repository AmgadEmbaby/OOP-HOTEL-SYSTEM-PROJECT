import java.util.*;

public class Validator {

    public static <T> void checkNotNull(T obj, String outputMessage){
        if (obj == null){
            throw new IllegalArgumentException(outputMessage);
        }
    }

    public static void checkStringNotEmpty(String value, String fieldName){
        if (value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }

    public static <T> void checkDuplicates(List<T> list, T obj, String outputMessage){
        if (list.contains(obj)){
            throw new IllegalArgumentException(outputMessage);
        }
    }


}
