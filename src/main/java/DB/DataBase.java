package DB;

import Exceptions.UnknownAccountException;

public interface DataBase {
    void getDB();
    boolean checkData(String name, String passwd) throws UnknownAccountException;
    void signUp(String name, String passwd);
}
