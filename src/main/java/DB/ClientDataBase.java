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

    public static void getDB() throws RuntimeException {
        String[] str;
        try( BufferedReader fr = new BufferedReader(new FileReader("src/main/java/db.txt"))){
            String line;
            while((line = fr.readLine()) != null){
                str = line.split(" ");
//                System.out.println(str.length);
                if(str.length == 2){
                    clients.add(new Client(str[0], str[1]));
                    size++;
                }else throw new RuntimeException("error in db file" + str[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pushDB(Client client){
        try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/db.txt", true))){
            fw.write(client.toString());
            fw.newLine();
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void signUp(Client client) throws RuntimeException {
        if (getClientByNickName(client.getNickName()) == null) {
            clients.add(client);
            pushDB(client);
        }else throw new RuntimeException("the account is already exist");
    }

}
