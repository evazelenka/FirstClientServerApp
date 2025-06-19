package Client;


import lombok.Data;

@Data
public class User {
    private String nickName;
    private char[] passwd;
    private boolean isOnline = false;

    public User(String nickName, char[] passwd){
        this.nickName = nickName;
        this.passwd = passwd;
    }

    public User(String nickName, String passwd){
        this.nickName = nickName;
        this.passwd = passwd.toCharArray();
    }

    public String getPasswd() {
        StringBuilder p = new StringBuilder();
        for(char c : passwd){
           p.append(c);
        }
        return p.toString();
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
