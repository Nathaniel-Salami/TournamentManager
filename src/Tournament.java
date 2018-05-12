import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tournament {

    public static int LEG_NUM;

    String tournamentName;

    Player[] totalPlayers;
    Round[] rounds;

    int numPlayers;
    int numRounds;

    public Tournament(String fileName, int ln) {
        Player.PLAYER_ID = 0;

        LEG_NUM = ln;

        totalPlayers = getPlayersFromFile(fileName);

        numPlayers = totalPlayers.length;
        numRounds = calcNumRounds(numPlayers);

        Player[][] startingLR = randomizeLeftRight(totalPlayers);

        rounds = new Round[numRounds];

        for (int i = 0; i < rounds.length; i++) {
            if (i == 0) {
                rounds[i] = new Round(startingLR[0], startingLR[1]);
            }
            else {
                rounds[i] = new Round(rounds[i-1]);
            }
        }
    }

    public Tournament(String tn) {
        tournamentName = tn;

        Player.PLAYER_ID = 0;

        numPlayers = 0;
        numRounds = 0;

        totalPlayers = new Player[0];
        rounds = new Round[0];
    }

    public boolean started() {
        return numRounds != 0 && numPlayers != 0;
    }

    //for testing
    public Tournament(int np, int ln) {
        //reset player id count
        Player.PLAYER_ID = 0;
        LEG_NUM = ln;

        numPlayers = np;
        numRounds = calcNumRounds(numPlayers);

        totalPlayers = makeFakePlayers(np);

        Player[][] startingLR = randomizeLeftRight(totalPlayers);

        rounds = new Round[numRounds];

        for (int i = 0; i < rounds.length; i++) {
            if (i == 0) {
                rounds[i] = new Round(startingLR[0], startingLR[1]);
            }
            else {
                rounds[i] = new Round(rounds[i-1]);
            }
        }

        //this.update();
    }

    public void loadPlayers(Player[] newPlayers, int ln) {
        Player.PLAYER_ID = 0;
        LEG_NUM = ln;

        totalPlayers = newPlayers;

        numPlayers = totalPlayers.length;
        numRounds = calcNumRounds(numPlayers);

        Player[][] startingLR = randomizeLeftRight(totalPlayers);

        rounds = new Round[numRounds];

        for (int i = 0; i < rounds.length; i++) {
            if (i == 0) {
                rounds[i] = new Round(startingLR[0], startingLR[1]);
            }
            else {
                rounds[i] = new Round(rounds[i-1]);
            }
        }

    }

    @Override
    public String toString() {
        String out = "Tournament Stats: ";
        out += "Players("+numPlayers+"), ";
        out += "Rounds("+numRounds+")";

        return out;
    }

    public Player getViewPlayer(Round pRound, int viewPlayerIndex, String side) {
        if (side.equalsIgnoreCase("right")) { //right
            return pRound.rightPlayers[viewPlayerIndex];
        }
        else { //left
            return pRound.leftPlayers[viewPlayerIndex];
        }
    }

    public Meet getMeet(Player player, Round pRound) {
        return pRound.getMeet(player);
    }

    public Match getMatch(Player player, Round pRound, int mI) {
        return pRound.getMeet(player).getMatch(mI);
    }

    public Match currentMatch(Player player, Round pRound) {
        return pRound.getMeet(player).currentMatch();
    }

    public Round getViewRound(int viewRoundIndex) {
        if (viewRoundIndex >= rounds.length) { //right
            //translation array
            int[] transArr = new int[rounds.length*2];
            for (int i = 0; i < rounds.length; i++) { //first half
                transArr[i] = i;
            }

            int count = rounds.length-1;
            for (int i = rounds.length; i < rounds.length*2; i++) { //second half
                transArr[i] = count--;
            }

            return rounds[transArr[viewRoundIndex]];
        }
        else { //left
            return rounds[viewRoundIndex];
        }
    }

    public int calcNumRounds(int numP) {
        int count = 1;
        int temp = 2;
        while (temp != numP) {
            temp = temp * 2;
            count++;
        }

        return count;
    }

    public Player[] makeFakePlayers(int num) {
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUMBERS = "0123456789";
        java.util.Random rand = new java.util.Random();

        Player[] out = new Player[num];

        for (int i=0; i < out.length; i++) {
            String fName = "";
            String fEmail = "";
            String fPhone = "";

            while (fPhone.length() != 10) {
                fPhone += NUMBERS.charAt(rand.nextInt(NUMBERS.length()));
            }

            while (fName.length() != 10) {
                fName += LETTERS.charAt(rand.nextInt(LETTERS.length()));
            }

            fEmail += fName + "@fmail.ca";

            fName = fName.substring(0, 5) + " " + fName.substring(5, fName.length());

            Player fPlayer = new Player(fName, fPhone, fEmail);

            out[i] = fPlayer;
        }

        return out;
    }

    public void update() {
        if (numRounds != 0 && numPlayers != 0) {
            for (int i = 1; i < rounds.length; i++) {
                //rounds[i] = new Round(rounds[i-1]);
                rounds[i].update(rounds[i-1]);
            }
        }
    }

    public Player[][] randomizeLeftRight(Player[] totalP) {
        Player[] copy = totalP;
        Random rand = new java.util.Random();

        HashSet<Integer> randIndexes = new HashSet<>();

        //adding unique indices for left array
        while (randIndexes.size() != copy.length/2) {
            randIndexes.add(rand.nextInt(copy.length));
        }

        ArrayList<Player> leftArr = new ArrayList<>();
        ArrayList<Player> rightArr = new ArrayList<>();

        for (Integer i: randIndexes) {
            leftArr.add(copy[i]);
            copy[i] = null;
        }

        for (int i = 0; i < copy.length; i++) {
            if (copy[i] != null) {
                rightArr.add(copy[i]);
            }
        }


        Player[][] out = new Player[2][totalP.length/2];

        out[0] = leftArr.toArray(new Player[leftArr.size()]);
        out[1] = rightArr.toArray(new Player[rightArr.size()]);

        return  out;
    }

    //calculates how many players are between each player of a specific round
    //used for view formatting
    public int playersBetween(int roundN) {
        if (roundN == 1) {
            return 0;
        }
        else if (roundN == 2) {
            return 2;
        }
        else {
            return 2+playersBetween(roundN-1) + playersBetween(roundN-1);
        }
    }

    public static Player[] getPlayersFromFile(String fileName) {
        Player[] out;

        ArrayList<Player> temp = new ArrayList<>();


        try {
            BufferedReader in;
            if (fileName.length() == 0) {
                in = new BufferedReader(new FileReader("TournamentPlayers.txt"));
            }
            else {
                in = new BufferedReader(new FileReader(fileName));
            }


            while(in.ready()) {
                String playerString = in.readLine();

                Player newP = new Player(playerString);

                //System.out.println(newP);

                temp.add(newP);
            }

            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for reading");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }

        //List<T> list = new ArrayList<T>();

        out = temp.toArray(new Player[temp.size()]);


        return out;
    }

    public static boolean isValidNum(int num) {
        int temp = 1;

        while (temp != num) {
            if (temp <= num) {
                temp *= 2;
            }
            else {
                return false;
            }
        }

        return true;
    }
}
