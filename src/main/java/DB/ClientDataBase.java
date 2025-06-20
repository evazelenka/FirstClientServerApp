package DB;
import Client.User;
import Exceptions.UnknownAccountException;
import Server.ServerWindow;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBase implements DataBase{
    @Getter
    private static ArrayList<User> users = new ArrayList<>();
    private static int size = 0;

    private static User getClientByNickName(String name){
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getNickName().equals(name)){
                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean checkData(String name, String passwd) throws UnknownAccountException {
        for (int i = 0; i < users.size(); i++) {
            if(checkName(name) & checkPasswd(passwd)){
                return true;
            }
        }
        throw new UnknownAccountException("sign up first");
    }

    private boolean checkName(String name){
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getNickName().equals(name)){
                return true;
            }
        }
        throw new UnknownAccountException("sign up first");
    }

    private boolean checkPasswd(String passwd){
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getPasswd().equals(passwd)){
                return true;
            }
        }
        throw new UnknownAccountException("wrong password");
    }

    private void pushDB(User user) {
        try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/db.txt", true))){
            fw.write(user.toString());
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

    @Override
    public void signUp(String name, String passwd) throws UnknownAccountException {
        if (getClientByNickName(name) == null){
            User user = new User(name, passwd);
            users.add(user);
            pushDB(user);
        }else throw new UnknownAccountException("the account is already exist");
    }

}
