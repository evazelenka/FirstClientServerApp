package Client;


import lombok.Data;

import java.util.Arrays;

@Data
public class Client {
    private String nickName;
    private char[] passwd;
    private boolean isOnline = false;

    public Client(String nickName, char[] passwd){
        this.nickName = nickName;
        this.passwd = passwd;
    }

    public String getPasswd() {
        StringBuilder p = new StringBuilder();
        for(char c : passwd){
           p.append(c);
        }
        return p.toString();
    }

    public Client(String nickName, String passwd){
        this.nickName = nickName;
        this.passwd = passwd.toCharArray();
    }

    public void setOnline(){
        isOnline = true;
    }

    public void setOffline(){
        isOnline = false;
    }

    public boolean isOnline(){
        return isOnline;
    }

    @Override
    public String toString(){
        return nickName + " " + getPasswd();
    }
}
