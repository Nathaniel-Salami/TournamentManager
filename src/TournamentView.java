
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

public class TournamentView extends StackPane {
    //view is to be in full screen for best results

    Tournament model;

    //view layers
    GridPane overlay; //view for buttons and text boxes
    Canvas underlay; //for drawing lines and other graphics
    Pane backGround;

    //overlay
    int BUTTON_W = 80;
    int BUTTON_L = 30; //each button on the first round is seperated by this length as well
    int HGAP = BUTTON_W;
    Font overlayButtonFont = Font.font("Calibri", 12);
    Button[][] tournamentButtons; //all active view buttons

    //every button is a winner unless it is a loser
    Color winnerC = Color.BLACK;
    Color loserC = Color.GRAY;

    //score input dialog
    int S_BUTTON_W = 40;
    int S_BUTTON_L = 30;
    Font diagFont = Font.font("Calibri", 16);

    //canvas line and score drawing
    GraphicsContext aPen; //used for drawing
    Color lineC = Color.WHITE;
    Color textC = Color.WHITE;
    Double lineThickness = 2.5;
    Font scoreFont = Font.font("Calibri", 18);
    double connectP = 0; //percentage of the line where the join happens

    //stores the screen size
    double WIN_X;
    double WIN_Y;

    //menu
    MenuItem loadPlayers;
    MenuItem findPlayer;


    public TournamentView(Tournament tournament, double screenX, double screenY) {

        WIN_X = screenX;
        WIN_Y = screenY;

        model = tournament;


        //setting up underlay and pen for drawing
        underlay = new Canvas(WIN_X, WIN_Y);
        aPen = underlay.getGraphicsContext2D();
        aPen.setStroke(lineC);
        aPen.setFill(textC);
        aPen.setLineWidth(lineThickness);
        aPen.setFont(scoreFont);


        //setting up background background
        backGround = new Pane();
        if (this.getBackGround() != null) {
            backGround.setBackground(this.getBackGround());
        }

        //setting up overlay
        overlay = new GridPane();

        if (model.started()) { //loaded
            tournamentButtons = new Button[model.numRounds*2][];

            for (int i=0; i< model.numRounds; i++) {
                Round currRound = model.rounds[i];

                Button[] leftButtons = new Button[currRound.leftPlayers.length];
                Button[] rightButtons = new Button[currRound.rightPlayers.length];

                //testing
                //System.out.println(currRound);

                VBox leftVbox = new VBox();
                VBox rightVbox = new VBox();

                if (currRound.isFinalRound()) {
                    Button newLB = new Button("");
                    Button newRB = new Button("");

                    newLB.setMinSize(BUTTON_W, BUTTON_L);
                    newLB.setMaxSize(BUTTON_W, BUTTON_L);

                    newRB.setMinSize(BUTTON_W, BUTTON_L);
                    newRB.setMaxSize(BUTTON_W, BUTTON_L);

                    newLB.setFont(overlayButtonFont);
                    newRB.setFont(overlayButtonFont);

                    leftButtons[0] = newLB;
                    rightButtons[0] = newRB;

                    //leftVbox.setPadding(new Insets(BUTTON_L*lScale, BUTTON_W*wScale,BUTTON_L*lScale ,BUTTON_W*wScale));
                    leftVbox.getChildren().add(newLB);

                    //rightVbox.setPadding(new Insets(BUTTON_L*lScale, BUTTON_W*wScale,BUTTON_L*lScale ,BUTTON_W*wScale));
                    rightVbox.getChildren().add(newRB);
                }
                else {
                    //adding buttons to vboxes and arrays
                    for (int j=0; j < currRound.leftPlayers.length; j++) { //same as right players
                        //spacing formatting
                        if ((j != 0) ) {
                            for (int x=0; x<model.playersBetween(i+1); x++){
                                Label lPSpace = new Label("");
                                lPSpace.setMinSize(BUTTON_W, BUTTON_L);
                                lPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                                leftVbox.getChildren().add(lPSpace);

                                Label rPSpace = new Label("");
                                rPSpace.setMinSize(BUTTON_W, BUTTON_L);
                                rPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                                rightVbox.getChildren().add(rPSpace);
                            }
                            Label lPSpace = new Label("");
                            lPSpace.setMinSize(BUTTON_W, BUTTON_L);
                            lPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                            leftVbox.getChildren().add(lPSpace);

                            Label rPSpace = new Label("");
                            rPSpace.setMinSize(BUTTON_W, BUTTON_L);
                            rPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                            rightVbox.getChildren().add(rPSpace);
                        }

                        //left buttons
                        Button newLB = new Button("");
                        newLB.setMinSize(BUTTON_W, BUTTON_L);
                        newLB.setMaxSize(BUTTON_W, BUTTON_L);

                        newLB.setFont(overlayButtonFont);

                        leftButtons[j] = newLB;
                        leftVbox.getChildren().add(newLB);


                        //right buttons
                        Button newRB = new Button("");
                        newRB.setMinSize(BUTTON_W, BUTTON_L);
                        newRB.setMaxSize(BUTTON_W, BUTTON_L);

                        newRB.setFont(overlayButtonFont);

                        rightButtons[j] = newRB;
                        rightVbox.getChildren().add(newRB);
                    }
                }
                //centering buttons in the middle of the vbox
                leftVbox.setAlignment(Pos.CENTER);
                rightVbox.setAlignment(Pos.CENTER);

                //adding left and right vbox to grid pane
                overlay.add(leftVbox, i, 0, 1, 1);
                overlay.add(rightVbox, (model.numRounds*2-1) - i, 0, 1, 1);

                //adding button arrays  to 2D array
                tournamentButtons[i] = leftButtons;
                tournamentButtons[(model.numRounds*2-1) - i] = rightButtons;
            }
        }

        overlay.setHgap(HGAP);
        overlay.setAlignment(Pos.CENTER);


        //create menu
        MenuBar menuBar = new MenuBar();
        Menu tournieMenu = new Menu(this.model.tournamentName);

        loadPlayers = new MenuItem("Load Players");
        findPlayer = new MenuItem("Find Player");

        tournieMenu.getItems().add(loadPlayers);
        tournieMenu.getItems().add(findPlayer);

        //disable find player until players are loaded
        findPlayer.setDisable(true);

        menuBar.getMenus().add(tournieMenu);


        //adding the view layers
        this.getChildren().add(backGround);
        this.getChildren().add(underlay);
        this.getChildren().add(overlay);
        this.getChildren().add(menuBar);

        //setting layer alignments
        this.setAlignment(backGround, Pos.CENTER);
        this.setAlignment(underlay, Pos.CENTER);
        this.setAlignment(overlay, Pos.CENTER);
        this.setAlignment(menuBar, Pos.TOP_CENTER);
    }

    public Background getBackGround() {
        if (getClass().getResourceAsStream("background.jpg") == null) {
            return null;
        }
        else {
            Image srcImage = new Image(getClass().getResourceAsStream("background.jpg"));

            BackgroundSize backSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
            BackgroundImage backImg = new BackgroundImage(srcImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backSize);

            Background pBackG = new Background(backImg);

            return pBackG;
        }
    }

    public Meet getMeet(int pX, int pY) {
        Round pRound = model.getViewRound(pX);
        Player player;

        if(pX < model.rounds.length) {
            player = model.getViewPlayer(pRound, pY, "left");
        }
        else {
            player = model.getViewPlayer(pRound, pY, "right");
        }

        return model.getMeet(player, pRound);
    }

    public void drawFinalMatchConnection() {
        //player A x,y index for final match
        int pAXI = tournamentButtons.length/2 - 1;
        int pAYI = 0;

        //player B x,y index for final match
        int pBXI = tournamentButtons.length/2;
        int pBYI = 0;

        Point2D startVal = this.getScenePos(pAXI, pAYI);
        Point2D endVal = this.getScenePos(pBXI, pBYI);

        double[] drawVals = new double[4];
        drawVals[0] = startVal.getX();
        drawVals[1] = startVal.getY();
        drawVals[2] = endVal.getX();
        drawVals[3] = endVal.getY();

        drawLines(aPen, drawVals);

        //draw score for valid (final scores are set) meet
        Meet currMeet = this.getMeet(pAXI, pAYI);
        if (currMeet.playerA.playerID != -1
                && currMeet.playerB.playerID != -1
                && currMeet.isOver())
        {

            String scores = this.getMeet(pAXI, pAYI).printScores();
            aPen.fillText(scores, WIN_X/2 - 15, WIN_Y/2 - 5);
        }
    }

    public void drawConnection(Point2D[] trio, TrioIndex tIndex) {

        double tx = trio[0].getX();
        double ty = trio[0].getY();
        double bx = trio[1].getX();
        double by = trio[1].getY();
        double mx = trio[2].getX();
        double my = trio[2].getY();

        //GraphicsContext aPen = underlay.getGraphicsContext2D();

        double x1, y1, x2, y2;

        //top line
        x1 = tx;
        y1 = ty;
        x2 = pXCut(tx, mx, connectP);
        y2 = ty;

        double [] topLine = {x1, y1, x2, y2};

        //bottom line
        x1 = bx;
        y1 = by;
        x2 = pXCut(bx, mx, connectP);
        y2 = by;

        double [] bottomLine = {x1, y1, x2, y2};

        //connect line
        x1 = pXCut(tx, mx, connectP);
        y1 = ty;
        x2 = pXCut(bx, mx, connectP);
        y2 = by;

        double [] connectLine = {x1, y1, x2, y2};

        //middle line
        x1 = pXCut(tx, mx, connectP); //should also work with bx
        y1 = my;
        x2 = mx;
        y2 = my;
        double [] midLine = {x1, y1, x2, y2};

        //drawing lines
        drawLines(aPen, topLine);
        drawLines(aPen, bottomLine);
        drawLines(aPen, connectLine);
        drawLines(aPen, midLine);

        //adding score
        Meet currMeet = this.getMeet(tIndex.topIndex[0], tIndex.topIndex[1]); //should work with (bx, by)

        if (currMeet.playerA.playerID != -1
                && currMeet.playerB.playerID != -1
                && currMeet.isOver())
        {
            String scores = currMeet.printScores();
            double lScoreShift = BUTTON_W/2 + this.getScoreWidth(scores) + 5;
            double rScoreShift = BUTTON_W/2 + 5;

            if (mx > tx) { //left side
                aPen.fillText(scores, midLine[2] - lScoreShift, midLine[3] - lineThickness*2);
            }
            else { //right
                aPen.fillText(scores, midLine[2] + rScoreShift, midLine[3] - lineThickness*2);
            }
        }
    }

    public double getScoreWidth(String sc) {
        final Text text = new Text(sc);
        text.setFont(scoreFont);

        return text.getLayoutBounds().getWidth();
    }

    public void drawLines(GraphicsContext aPen, double[] vals) { //vals: x1, y1, x2, y2
        aPen.strokeLine(vals[0], vals[1], vals[2], vals[3]);
    }

    public double pXCut(double x1, double x2, double cut) { //cut in percentage
        double diff = x2 - x1;
        double cutLen = diff * (cut/100);
        double out = (x1 + cutLen);

        //System.out.println("testMenu: " + cut/100);

        return out;
    }

    //trio: the three buttons/players used for the line connection
    public TrioIndex[][] getTrioIndexes() {
        int totalTrios = this.tournamentButtons.length - 2; //(-2) for the final match buttons/players
        TrioIndex[][] out = new TrioIndex[totalTrios][];

        //loop through view tournament buttons //round level
        //count for array in front
        //for every two players increase count to get middle for pair

        //left side, therefore first half of array
        for (int x = 0; x < totalTrios/2; x++) {
            //players in round
            int totalRP = this.tournamentButtons[x].length;
            TrioIndex[] roundTrioIndices = new TrioIndex[totalRP/2];
            int midCount = 0;
            //for each player in current round/array
            //for every second player form a trio with player in next round at count
            for (int y = 0; y < totalRP; y++) {
                if (y % 2 != 0) {
                    int tx = x;
                    int ty = y-1;
                    int bx = x;
                    int by = y;
                    int mx = x+1;
                    int my = midCount;

                    TrioIndex newTrioIndex = new TrioIndex(tx, ty, bx, by, mx, my);

                    roundTrioIndices[midCount] = newTrioIndex;

                    midCount++;
                }
            }
            out[x] = roundTrioIndices;
        }

        //starting from back of tournament buttons rounds
        int newRStart = this.tournamentButtons.length;

        //right side
        for (int x = newRStart-1; x > newRStart/2; x--) {
            //players in round
            int totalRP = this.tournamentButtons[x].length;
            TrioIndex[] roundTrioIndices = new TrioIndex[totalRP/2];
            int midCount = 0;
            //for each player in current round/array
            //for every second player form a trio with player in next round at count
            for (int y = 0; y < totalRP; y++) {
                if (y % 2 != 0) {
                    int tx = x;
                    int ty = y-1;
                    int bx = x;
                    int by = y;
                    int mx = x-1;
                    int my = midCount;

                    TrioIndex newTrioIndex = new TrioIndex(tx, ty, bx, by, mx, my);

                    roundTrioIndices[midCount] = newTrioIndex;

                    midCount++;
                }
            }
            out[x-2] = roundTrioIndices;
        }

        return out;
    }

    public void drawAllLines() {
        //System.out.println("DRAWING LINES!!!");

        if (model.started()) {
            //clear canvas
            this.underlay.getGraphicsContext2D().clearRect(0, 0, this.WIN_X, this.WIN_Y);

            TrioIndex[][] TrioIndeces = getTrioIndexes();

            for (TrioIndex[] tIA: TrioIndeces) {
                for (TrioIndex tI: tIA) {
                    this.drawConnection(tI.getTrioP2D(this), tI);
                }
            }

            //draw final match lines
            drawFinalMatchConnection();
        }
    }

    public void updateButtons() {
        for (int i=0; i< model.numRounds; i++) {
            Round currRound = model.rounds[i];

            //testing
            //System.out.println(currRound);

            if (currRound.isFinalRound()) {
                //left
                if (currRound.finalMeet.playerA.playerID != -1) {
                    String lBText = "["+currRound.finalMeet.playerA.playerID+"]";
                    String lFN = currRound.finalMeet.playerA.name.substring(0, currRound.finalMeet.playerA.name.indexOf(" "));
                    lBText += " " + lFN;

                    tournamentButtons[i][0].setText(lBText);
                    tournamentButtons[i][0].setTextFill(winnerC);

                    if (this.getMeet(i, 0).isOver()) {
                        if (this.getMeet(i, 0).getWinner() != model.getViewPlayer(currRound, 0, "left")) { //loser
                            tournamentButtons[i][0].setTextFill(loserC);
                        }
                    }
                }

                //right
                if (currRound.finalMeet.playerB.playerID != -1) {
                    String rBText = "["+currRound.finalMeet.playerB.playerID+"]";
                    String rFN = currRound.finalMeet.playerB.name.substring(0, currRound.finalMeet.playerB.name.indexOf(" "));
                    rBText += " " + rFN;

                    tournamentButtons[(model.numRounds*2-1) - i][0].setText(rBText);
                    tournamentButtons[(model.numRounds*2-1) - i][0].setTextFill(winnerC);

                    //System.out.println(this.getMatch((model.numRounds*2-1) - i, 0));
                    if (this.getMeet((model.numRounds*2-1) - i, 0).isOver()) {
                        if (this.getMeet((model.numRounds*2-1) - i, 0).getWinner() != model.getViewPlayer(currRound, 0, "right")) { //loser
                            tournamentButtons[(model.numRounds*2-1) - i][0].setTextFill(loserC);
                        }
                    }
                }

                //font
                tournamentButtons[i][0].setFont(overlayButtonFont);
                tournamentButtons[(model.numRounds*2-1) - i][0].setFont(overlayButtonFont);
            }
            else {

                //adding buttons to vboxes and arrays
                for (int j=0; j < currRound.leftPlayers.length; j++) { //same as right players

                    //left
                    if (currRound.leftPlayers[j].playerID != -1) {
                        String lBText = "["+currRound.leftPlayers[j].playerID+"]";
                        String lFName = currRound.leftPlayers[j].name.substring(0, currRound.leftPlayers[j].name.indexOf(" "));
                        lBText += " " + lFName;

                        tournamentButtons[i][j].setText(lBText);
                        tournamentButtons[i][j].setTextFill(winnerC);

                        if (this.getMeet(i, j).isOver()) {
                            if (this.getMeet(i, j).getWinner() != model.getViewPlayer(currRound, j, "left")) { //loser
                                tournamentButtons[i][j].setTextFill(loserC);
                            }
                        }
                    }

                    //right
                    if (currRound.rightPlayers[j].playerID != -1) {
                        String rBText = "["+currRound.rightPlayers[j].playerID+"]";
                        String rFName = currRound.rightPlayers[j].name.substring(0, currRound.rightPlayers[j].name.indexOf(" "));
                        rBText += " " + rFName;

                        tournamentButtons[(model.numRounds*2-1) - i][j].setText(rBText);
                        tournamentButtons[(model.numRounds*2-1) - i][j].setTextFill(winnerC);

                        if (this.getMeet((model.numRounds*2-1) - i, j).isOver()) {
                            if (this.getMeet((model.numRounds*2-1) - i, j).getWinner() != model.getViewPlayer(currRound, j, "right")) { //loser
                                tournamentButtons[(model.numRounds*2-1) - i][j].setTextFill(loserC);
                            }
                        }
                    }

                    //font
                    tournamentButtons[i][j].setFont(overlayButtonFont);
                    tournamentButtons[(model.numRounds*2-1) - i][j].setFont(overlayButtonFont);
                }
            }
        }
    }

    public void updateView() {
        if (model.started()) {
            model.update();
            this.updateButtons();
            this.drawAllLines();
        }
    }

    //gets the point2D pos of a button, appropriately translated for drawing lines
    public Point2D getScenePos(int xIndex, int yIndex) {
        double transX;
        double transY;

        double startX = this.tournamentButtons[xIndex][yIndex].localToScene(0.0, 0.0).getX();
        double startY = this.tournamentButtons[xIndex][yIndex].localToScene(0.0, 0.0).getY();

        transX = startX + this.BUTTON_W/2;
        transY = startY + this.BUTTON_L/2;

        return new Point2D(transX, transY);
    }

    public void loadModel(Tournament newModel) {
        //clear overlay and underlay
        overlay.getChildren().clear();
        //this.underlay.getGraphicsContext2D().clearRect(0, 0, this.WIN_X, this.WIN_Y);

        model = newModel;

        tournamentButtons = new Button[model.numRounds*2][];

        for (int i=0; i< model.numRounds; i++) {
            Round currRound = model.rounds[i];

            Button[] leftButtons = new Button[currRound.leftPlayers.length];
            Button[] rightButtons = new Button[currRound.rightPlayers.length];

            //testing
            //System.out.println(currRound);

            VBox leftVbox = new VBox();
            VBox rightVbox = new VBox();

            if (currRound.isFinalRound()) {
                Button newLB = new Button("");
                Button newRB = new Button("");

                newLB.setMinSize(BUTTON_W, BUTTON_L);
                newLB.setMaxSize(BUTTON_W, BUTTON_L);

                newRB.setMinSize(BUTTON_W, BUTTON_L);
                newRB.setMaxSize(BUTTON_W, BUTTON_L);

                leftButtons[0] = newLB;
                rightButtons[0] = newRB;

                //leftVbox.setPadding(new Insets(BUTTON_L*lScale, BUTTON_W*wScale,BUTTON_L*lScale ,BUTTON_W*wScale));
                leftVbox.getChildren().add(newLB);

                //rightVbox.setPadding(new Insets(BUTTON_L*lScale, BUTTON_W*wScale,BUTTON_L*lScale ,BUTTON_W*wScale));
                rightVbox.getChildren().add(newRB);
            }
            else {
                //adding buttons to vboxes and arrays
                for (int j=0; j < currRound.leftPlayers.length; j++) { //same as right players
                    //spacing formatting
                    if ((j != 0) ) {
                        for (int x=0; x<model.playersBetween(i+1); x++){
                            Label lPSpace = new Label("");
                            lPSpace.setMinSize(BUTTON_W, BUTTON_L);
                            lPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                            leftVbox.getChildren().add(lPSpace);

                            Label rPSpace = new Label("");
                            rPSpace.setMinSize(BUTTON_W, BUTTON_L);
                            rPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                            rightVbox.getChildren().add(rPSpace);
                        }
                        Label lPSpace = new Label("");
                        lPSpace.setMinSize(BUTTON_W, BUTTON_L);
                        lPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                        leftVbox.getChildren().add(lPSpace);

                        Label rPSpace = new Label("");
                        rPSpace.setMinSize(BUTTON_W, BUTTON_L);
                        rPSpace.setMaxSize(BUTTON_W, BUTTON_L);
                        rightVbox.getChildren().add(rPSpace);
                    }

                    //left buttons
                    Button newLB = new Button("");
                    newLB.setMinSize(BUTTON_W, BUTTON_L);
                    newLB.setMaxSize(BUTTON_W, BUTTON_L);

                    leftButtons[j] = newLB;
                    leftVbox.getChildren().add(newLB);


                    //right buttons
                    Button newRB = new Button("");
                    newRB.setMinSize(BUTTON_W, BUTTON_L);
                    newRB.setMaxSize(BUTTON_W, BUTTON_L);

                    rightButtons[j] = newRB;
                    rightVbox.getChildren().add(newRB);
                }
            }
            //centering buttons in the middle of the vbox
            leftVbox.setAlignment(Pos.CENTER);
            rightVbox.setAlignment(Pos.CENTER);

            //adding left and right vbox to grid pane
            overlay.add(leftVbox, i, 0, 1, 1);
            overlay.add(rightVbox, (model.numRounds*2-1) - i, 0, 1, 1);

            //adding button arrays  to 2D array
            tournamentButtons[i] = leftButtons;
            tournamentButtons[(model.numRounds*2-1) - i] = rightButtons;
        }

        //this.updateButtons();
        //this.updateView();
    }

    public void updateScorePopUp(Meet currMeet, Stage stage) {
        Font connectorFont = new Font("Times", 20); //the line between the buttons

        Dialog dialog = new Dialog();
        dialog.setTitle("Update Match Scores");
        dialog.setHeaderText(null);

        GridPane dGridPane = new GridPane();
        dGridPane.setAlignment(Pos.CENTER);
        //dGridPane.setBackground(this.getBackground());

        //adding name to top
        String pAText = currMeet.playerA.name.substring(0, currMeet.playerA.name.indexOf(" "));
        String pBText = currMeet.playerB.name.substring(0, currMeet.playerB.name.indexOf(" "));

        Label pALabel = new Label(pAText);
        Label pBLabel = new Label(pBText);

        if (currMeet.getWinner() == currMeet.playerA) { //winner
            pALabel.setTextFill(winnerC);
        }
        else {
            pALabel.setTextFill(loserC);
        }
        if (currMeet.getWinner() == currMeet.playerB) { //winner
            pBLabel.setTextFill(winnerC);
        }
        else {
            pBLabel.setTextFill(loserC);
        }
        if (!currMeet.isOver()) {
            pALabel.setTextFill(winnerC);
            pBLabel.setTextFill(winnerC);
        }

        pALabel.setFont(diagFont);
        pBLabel.setFont(diagFont);

        dGridPane.add(pALabel, 0, 0);
        dGridPane.add(pBLabel, 2, 0);

        TextField[][] scoreTextFields = new TextField[currMeet.matchNum][2];

        for (int i = 0; i < currMeet.matchNum; i++) {
            TextField pAScore = new TextField();
            pAScore.setMinSize(S_BUTTON_W, S_BUTTON_L);
            pAScore.setMaxSize(S_BUTTON_W, S_BUTTON_L);

            TextField pBScore = new TextField();
            pBScore.setMinSize(S_BUTTON_W, S_BUTTON_L);
            pBScore.setMaxSize(S_BUTTON_W, S_BUTTON_L);

            //if the match at this index is over print its scores in score box
            if (currMeet.matches[i].matchOver()) {
                pAScore.setText(""+currMeet.matches[i].scores[0]);
                pBScore.setText(""+currMeet.matches[i].scores[1]);
            }
            else {
                pAScore.setText("");
                pBScore.setText("");
            }

            Label connector = new Label("―");
            connector.setFont(connectorFont);

            //adds score text fields to arr
            scoreTextFields[i][0] = pAScore;
            scoreTextFields[i][1] = pBScore;

            dGridPane.add(pAScore, 0, i+1);
            dGridPane.add(connector, 1, i+1);
            dGridPane.add(pBScore, 2, i+1);
        }


        //placing total scores at bottom of gridpane
        Label scoreTotal = new Label("Total: " + currMeet.scoreSum[0] +" - "+ currMeet.scoreSum[1]);
        scoreTotal.setFont(diagFont);

        dGridPane.add(scoreTotal, 0, currMeet.matchNum+1, 3, 1);


        //rest of formatting and adding gridpane to dialog
        dGridPane.setAlignment(Pos.CENTER);
        dGridPane.setVgap(S_BUTTON_L/3);
        dGridPane.setHgap(S_BUTTON_W/3);
        dGridPane.setMinWidth(S_BUTTON_W*2.5);

        dialog.getDialogPane().setContent(dGridPane);
        dialog.initOwner(stage); //places the dialog over the main window even in full screen


        //submits scores
        ButtonType submitButton = new ButtonType("Submit", ButtonBar.ButtonData.APPLY);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.FINISH);

        dialog.getDialogPane().getButtonTypes().addAll(submitButton, closeButton);


        while (true) {
            Optional<ButtonType> opt = dialog.showAndWait();

            if (opt.isPresent() && opt.get().getButtonData() == ButtonBar.ButtonData.APPLY) { //submit
                // updates scores displays
                for (int i = 0; i < currMeet.matchNum; i++) {
                    TextField[] scorePair = scoreTextFields[i];

                    if ((scorePair[0].getText().length() != 0) && (scorePair[1].getText().length() != 0)) {
                        if (scorePair[0].getText().matches("[0-9]+") &&  scorePair[1].getText().matches("[0-9]+")) {
                            int newPAS = Integer.parseInt(scorePair[0].getText());
                            int newPBS = Integer.parseInt(scorePair[1].getText());

                            if (newPAS >= 0 && newPBS >= 0) {
                                int[] newScores = {newPAS, newPBS};
                                currMeet.updateScores(newScores, i);
                            }
                        }
                    }

                    if (currMeet.matches[i].matchOver()) {
                        scorePair[0].setText(""+currMeet.matches[i].scores[0]);
                        scorePair[1].setText(""+currMeet.matches[i].scores[1]);
                    }
                    else {
                        scorePair[0].setText("");
                        scorePair[1].setText("");
                    }
                }

                String newTotal ="Total: " + currMeet.scoreSum[0] +" - "+ currMeet.scoreSum[1];
                scoreTotal.setFont(diagFont);
                scoreTotal.setText(newTotal);

                //select winner in case of draw
                if (currMeet.scoreSum[0] == currMeet.scoreSum[1] && currMeet.isOver()) {
                    Dialog selectWinner = new Dialog();
                    selectWinner.setTitle("Select Winner");
                    selectWinner.setHeaderText(null);
                    selectWinner.initOwner(dialog.getOwner());

                    GridPane gridPane = new GridPane();

                    Label textLabel = new Label("Select Winner");
                    textLabel.setFont(diagFont);

                    ButtonType pAButton = new ButtonType(pAText, ButtonBar.ButtonData.FINISH);
                    ButtonType pBButton = new ButtonType(pBText, ButtonBar.ButtonData.APPLY);

                    gridPane.add(textLabel, 0, 0, 2, 1);

                    gridPane.setVgap(10);
                    gridPane.setHgap(10);

                    selectWinner.getDialogPane().setContent(gridPane);

                    selectWinner.getDialogPane().getButtonTypes().addAll(pAButton, pBButton);

                    Optional<ButtonType> winner = selectWinner.showAndWait();
                    if (winner.isPresent() && winner.get().getButtonData() == ButtonBar.ButtonData.FINISH) { //playerA
                        //setting winner as playerA
                        currMeet.setWinner(currMeet.playerA);
                    }
                    if (winner.isPresent() && winner.get().getButtonData() == ButtonBar.ButtonData.APPLY) { //playerB
                        //setting winner as playerB
                        currMeet.setWinner(currMeet.playerB);
                    }
                }

                //updating player labels color
                if (currMeet.getWinner() == currMeet.playerA) { //winner
                    pALabel.setTextFill(winnerC);
                }
                else {
                    pALabel.setTextFill(loserC);
                }
                if (currMeet.getWinner() == currMeet.playerB) { //winner
                    pBLabel.setTextFill(winnerC);
                }
                else {
                    pBLabel.setTextFill(loserC);
                }
                if (!currMeet.isOver()) { //match isnt over
                    pALabel.setTextFill(winnerC);
                    pBLabel.setTextFill(winnerC);
                }

                this.updateView();
            }

            if (opt.isPresent() && opt.get().getButtonData() == ButtonBar.ButtonData.FINISH) { //done
                // closes menu accepting changes
                dialog.close();
                break;
            }
        }
    }

    public void addButtonHandlers(Stage myStage, TournamentView myView) {
        //player buttons
        if (myView.model.started()) {
            for (int x=0; x<myView.tournamentButtons.length; x++) {
                for (int y=0; y<myView.tournamentButtons[x].length; y++) {

                    final int tempX = x;
                    final int tempY = y;

                    myView.tournamentButtons[x][y].setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            Meet currMeet = myView.getMeet(tempX, tempY);
                            if (currMeet.playerA.playerID != -1 && currMeet.playerB.playerID != -1) {
                                myView.updateScorePopUp(currMeet, myStage);
                            }
                            myView.updateView();
                        }
                    });
                }
            }
        }
    }

    public void findPlayerPopUp(Stage stage) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Find Player");
        dialog.setHeaderText(null);

        GridPane dGridPane = new GridPane();

        TextField searchText = new TextField();
        searchText.setPromptText("Name or ID");
        searchText.setMinSize(S_BUTTON_W*5, S_BUTTON_L);

        //dGridPane.add(new Label("Enter Player Name or ID"), 0, 0);
        dGridPane.add(searchText, 0, 1);

        dialog.getDialogPane().setContent(dGridPane);

        ButtonType searchButton = new ButtonType("Find", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(searchButton, ButtonType.CANCEL);

        dialog.initOwner(stage); //places the dialog over the main window even in full screen

        Optional<ButtonType> opt = dialog.showAndWait();

        if (opt.isPresent() && opt.get().getButtonData() == ButtonBar.ButtonData.OK_DONE && searchText.getText().length() != 0) { //submit


            if (searchText.getText().matches("[0-9]+")) { //text must be an ID
                System.out.println("SEARCHING FOR PLAYER ID: " + searchText.getText() + "...");
            }
            else { //must be a name
                System.out.println("SEARCHING FOR PLAYER NAME: " + searchText.getText() + "...");
            }
        }
    }
}
