package DB;
import Client.Client;
import Server.ServerWindow;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBase {
    @Getter
    private static ArrayList<Client> clients = new ArrayList<>();
    private static int size = 0;

    public static Client getClientByNickName(String name){

        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getNickName().equals(name)){
                return clients.get(i);
            }
        }
        return null;
    }

    public static void getDB(){
        String[] str;
        try( BufferedReader fr = new BufferedReader(new FileReader("src/main/java/db.txt"))){
            String line;
            while((line = fr.readLine()) != null){
                str = line.split(" ");
                if(str.length == 2){
                    clients.add(new Client(str[0], str[1]));
                    size++;
                }else throw new RuntimeException("error in db file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pushDB(){
        if(clients.size() > size){
            for (int i = size; i < clients.size(); i++) {
                try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/db.txt", true))){
                    fw.write(clients.get(i).toString());
                    fw.newLine();
                    fw.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void signUp(Client client){
        if (getClientByNickName(client.getNickName()) == null) {
            clients.add(client);
            pushDB();
        }else throw new RuntimeException("the account is already exist");
    }

}
