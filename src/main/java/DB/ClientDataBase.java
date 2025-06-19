package DB;
import Client.User;
import Server.ServerWindow;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBase implements DataBase{
    @Getter
    private static ArrayList<User> users = new ArrayList<>();
    private static int size = 0;

    public static User getClientByNickName(String name){
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getNickName().equals(name)){
                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public void pushDB(String name, String passwd) {
        try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/db.txt", true))){
            fw.write(new User(name, passwd).toString());
            fw.newLine();
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getDB() throws RuntimeException {
        String[] str;
        try( BufferedReader fr = new BufferedReader(new FileReader("src/main/java/db.txt"))){
            String line;
            while((line = fr.readLine()) != null){
                str = line.split(" ");
                if(str.length == 2){
                    users.add(new User(str[0], str[1]));
                    size++;
                }else throw new RuntimeException("error in db file" + str[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pushDB(User user){

    }

    public void signUp(User user) throws RuntimeException {
        if (getClientByNickName(user.getNickName()) == null) {
            users.add(user);
            pushDB(user.getNickName(), user.getPasswd());
        }else throw new RuntimeException("the account is already exist");
    }

}
