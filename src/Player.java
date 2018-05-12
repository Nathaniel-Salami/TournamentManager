import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class Player {
    static int PLAYER_ID = 0;

    int playerID;
    String name;
    String phone;
    String email;

    public Player(String n, String p, String e) {
        name = n;
        phone = p;
        email = e;
        playerID = PLAYER_ID++;
    }

    public Player(String playerString) {
        int c1 = playerString.indexOf(",");
        int c2 = playerString.indexOf(",", c1+1);

        name = playerString.substring(0, c1);
        phone = playerString.substring(c1+1, c2);
        email = playerString.substring(c2+1, playerString.length());

        playerID = PLAYER_ID++;
    }

    public Player() {
        name = "Fake Name";
        phone = "123-456-789";
        email = "fake_name@email.com";
        playerID = -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return ((Player) obj).playerID == this.playerID;
        }
        return false;
    }

    public String toString(){
        String out = "";

        out += " ID: " + playerID;
        out += " Name: " + name;
        out += " Phone: " + phone;
        out += " Email: " + email;

        return out;
    }

    public boolean isID(int searchID) {
        return searchID == playerID;
    }

    public boolean isName(String searchN) {
        if(searchN.contains(" ")) {
            return name.equalsIgnoreCase(searchN) ;
        }
        else {
            String fn = name.substring(0, name.indexOf(" "));
            String ln = name.substring(name.indexOf(" "), name.length());

            return fn.equalsIgnoreCase(searchN) || ln.equalsIgnoreCase(searchN);
        }
    }
}
