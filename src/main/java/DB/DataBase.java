package DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface DataBase {
//    ArrayList<Object> objects = new ArrayList<>();
//    void pushDB(String name, String passwd);
    void getDB();
    boolean checkData(String name, String passwd);
    void signUp(String name, String passwd);
}
