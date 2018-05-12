public class Meet {
    //class name should be changed to something better

    Match[] matches;

    int matchNum;

    Player playerA, playerB;

    int scoreSum[];

    Player selectedWinner;

    public Meet(Player a, Player b) {
        playerA = a;
        playerB = b;

        matchNum = Tournament.LEG_NUM;

        //generates matches for the meet
        matches = new Match[matchNum];
        for (int i = 0; i < matchNum; i++) {
            matches[i] = new Match(playerA, playerB);
        }

        scoreSum = new int[2];
        scoreSum[0] = 0;
        scoreSum[1] = 0;
    }

    /*public boolean firstLegOver() {
        return leg1.matchOver();
    }*/

    public boolean inProgress() {
        for (Match m: matches) {
            if (m.matchOver()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOver() {
        for (Match m: matches) {
            if (!m.matchOver()) {
                return false;
            }
        }
        return true;
    }

    public Player getWinner() {
        if (this.isOver()) {
            if (selectedWinner == null) {
                if (scoreSum[0] != scoreSum[1]) {
                    if (scoreSum[0] > scoreSum[1]) {
                        return playerA;
                    }
                    else {
                        return playerB;
                    }
                }
                else {
                    return new Player();
                }
            }
            else {
                return selectedWinner;
            }
        }
        return new Player(); //important for creating new round
    }

    public String printScores() {
        return "" + scoreSum[0] + "-" + scoreSum[1];
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

    public String toString() {
        String out = "Meet: ";
        for (int i = 0; i < matchNum; i++) {
            out += "\n  M"+ i +": " + matches[i].toString();
        }

        out += "\n Score Sum: " + scoreSum[0] + " - " + scoreSum[1];

        if (this.isOver()) {
            out += "\n Winner:" + this.getWinner();
        }

        return out;
    }

    public void updateScoreSum() {
        scoreSum[0] = 0;
        scoreSum[1] = 0;

        for (Match m: matches) {
            if (m.matchOver()) {
                scoreSum[0] += m.scores[0];
                scoreSum[1] += m.scores[1];
            }
        }
    }

    //updates the score of the match at index m
    public void updateScores(int[] s, int m) {
        if (playerA.playerID != -1 && playerB.playerID != -1) {
            matches[m].updateScores(s);

            updateScoreSum();
        }
    }

    public void updateScores(int[] s) {
        if (playerA.playerID != -1 && playerB.playerID != -1) {
            this.currentMatch().updateScores(s);

            updateScoreSum();
        }
    }

    public boolean isMatch(Match x) {
        for(Match m: matches) {
            if (m == x) {
                return true;
            }
        }
        return false;
    }

    public Match getMatch(int mI) {
        return matches[mI];
    }

    //returns latest unfinished match
    public Match currentMatch() {
        for (int i = 0; i < matchNum; i++) {
            if (!matches[i].matchOver()) {
                return matches[i];
            }
        }
        return null;
    }

    public void setWinner(Player w) {
        selectedWinner = w;
    }
}
