import java.util.Objects;

public class Match {

    //static private int MATCH ID

    static final int NO_GOAL = -1;

    Player playerA, playerB;
     //Match srcMatchA, srcMatchB;

    //boolean active; //only active when both players are present

    int[] scores; //in the form {playerA, playerB}

    public Match (Player a, Player b) {
        playerA = a;
        playerB = b;

        scores = new int[2];
        scores[0] = NO_GOAL;
        scores[1] = NO_GOAL;

    }

    public Player getOpponent(Player aPlayer) {
        if (aPlayer == playerA) {
            return playerB;
        }
        else if (aPlayer == playerB) {
            return playerA;
        }
        return null;
    }

    public Player getWinner() {
        if (scores[0] == NO_GOAL && scores[1] == NO_GOAL) {
            //return null;
            return new Player(); //important for creating new rounds
        }
        else {
            if (scores[0] > scores[1]) {
                return playerA;
            }
            else {
                return playerB;
            }
        }
    }

    public String printScores() {
        return "" + scores[0] + "-" + scores[1];
    }

    public void updateScores(int[] s) {
        if (playerA.playerID != -1 && playerB.playerID != -1) {
            if (s[0] >= 0 && s[1] >= 0) {
                scores[0] = s[0];
                scores[1] = s[1];
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        //System.out.println("custom .equals for Match class");
        if (obj instanceof Match) {
            return  ((Match) obj).playerA.equals(this.playerA) &&
                    ((Match) obj).playerB.equals(this.playerB);
        }
        return false;
    }

    public boolean isPlaying(Player x) {
        return playerA.equals(x) || playerB.equals(x);
    }

    public boolean matchOver() {
        return scores[0] != NO_GOAL && scores[1] != NO_GOAL;
    }

    public String toString() {
        String out = "Match: ";
        out += "("+playerA.playerID+")"+playerA.name +" ["+scores[0]+"]" + " Vs. ";
        out += "("+playerB.playerID+")"+playerB.name + " ["+scores[1]+"]";

        return out;
    }
}
