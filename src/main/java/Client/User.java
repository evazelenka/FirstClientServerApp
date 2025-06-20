package Client;


import lombok.Data;
import lombok.Getter;

@Data
public class User {
    private String nickName;
    @Getter
    private String passwd;
    private boolean isOnline = false;

    public User(String nickName, String passwd){
        this.nickName = nickName;
        this.passwd = passwd;
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
