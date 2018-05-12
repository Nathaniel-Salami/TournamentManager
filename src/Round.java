public class Round {

    Player[] leftPlayers, rightPlayers;


    int totalP; //total number of players
    int totalM; //total number of meets

    //int roundID;

    Meet[] leftMeets, rightMeets;
    Meet finalMeet;

    int legNum;


    public Round(Player[] leftP, Player[] rightP) {
        //legNum = Tournament.LEG_NUM;

        leftPlayers = leftP;
        rightPlayers = rightP;

        totalP = leftPlayers.length + rightPlayers.length;
        totalM = totalP/2;

        generateMeets();
    }

    //fills left and right match arrays with matches for players in left/right player arrays
    private void generateMeets() {
        if (!this.isFinalRound()) {
            leftMeets = new Meet[totalM/2];
            rightMeets = new Meet[totalM/2];

            int mCount = 0;
            for (int i = 0; i < totalP/2; i+=2) {
                leftMeets[mCount] = new Meet(leftPlayers[i], leftPlayers[i+1]);
                rightMeets[mCount] = new Meet(rightPlayers[i], rightPlayers[i+1]);
                mCount++;
            }
        }
        else {
            finalMeet = new Meet(leftPlayers[0], rightPlayers[0]);
        }
    }

    public Round(Round prevR) {
        this(prevR.getWinners("left"), prevR.getWinners("right"));
    }

    public Player[] getWinners(String side) {
        Player[] playerOut;
        if (!this.isFinalRound()) {
            Meet[] meetArr;

            if (side.equalsIgnoreCase("left")){
                meetArr = leftMeets;
            }
            else/* if (side.equalsIgnoreCase("right")) */{
                meetArr = rightMeets;
            }

            playerOut = new Player[totalM/2];

            for (int i = 0; i < totalM/2; i++) {
                playerOut[i] = meetArr[i].getWinner();
            }
        }
        else { //final match mode
            playerOut = new Player[1];

            playerOut[0] = finalMeet.getWinner();
        }

        return playerOut;
    }

    public boolean isFinalRound() {
        return totalM == 1 && totalP == 2;
    }

    public Meet getMeet(Player x) {
        if (!this.isFinalRound()) {
            for (Meet m: leftMeets) {
                if (m.isPlaying(x)) {
                    return m;
                }
            }
            for (Meet m: rightMeets) {
                if (m.isPlaying(x)) {
                    return m;
                }
            }
        }
        else {
            if (finalMeet.isPlaying(x)) {
                return finalMeet;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String out = "";

        if (!this.isFinalRound()) {
            out += " Left Meets \n";

            for (Meet m: leftMeets) {
                out += m.toString();
                out += "\n";
            }

            out += "\n Right Meets \n";

            for (Meet m: rightMeets) {
                out += m.toString();
                out += "\n";
            }

//            out += "\nStats: \n";
//            out += "Left: Matches: "+leftMatches.length+", Players: "+leftPlayers.length+"\n";
//            out += "Right: Matches: "+rightMatches.length+", Players: "+rightPlayers.length;
        }
        else {
            out += " Final Meet \n";
            out += finalMeet.toString();
        }

        return out += "\n\n";
    }

    public void update(Round prevR) {
        leftPlayers = prevR.getWinners("left");
        rightPlayers = prevR.getWinners("right");

        if (!this.isFinalRound()) {
            //rightMatches = new Match[totalM/2];

            int mCount = 0;
            for (int i = 0; i < totalP/2; i+=2) {
                if (!leftMeets[mCount].inProgress()) {
                    leftMeets[mCount] = new Meet(leftPlayers[i], leftPlayers[i+1]);
                }

                if (!rightMeets[mCount].inProgress()) {
                    rightMeets[mCount] = new Meet(rightPlayers[i], rightPlayers[i+1]);
                }
                mCount++;
            }
        }
        else {
            if (!finalMeet.inProgress()) {
                finalMeet = new Meet(leftPlayers[0], rightPlayers[0]);
            }
        }
    }
}
