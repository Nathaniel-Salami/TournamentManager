import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;


public class TournamentApp extends Application {


    private TournamentView view;
    //private TournamentApp surrogate = this;

    //private Tournament tournie; //


    private Stage myStage;

    private Scene myScene;

    // Initialize things before the app starts (optional)
    public void init() {
        String tName = "18-Tournie";

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        view = new TournamentView(new Tournament(tName), screenSize.width, screenSize.height);

        System.out.println("---Starting---");
    }

    // Clean up things just before the app stops (optional)
    public void stop() {
        System.out.println("---Stopping---");
    }


    public void start(Stage primaryStage) {

        myStage = primaryStage;

        //event handlers
        //load players
        view.loadPlayers.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("LOADING PLAYERS");

                int numMatch = 1; //number of matches in each meet

                //view.model.loadPlayers(view.model.makeFakePlayers(4), numMatch);

                view.model.loadPlayers(Tournament.getPlayersFromFile(""), numMatch);

                view.loadModel(view.model);
                view.addButtonHandlers(myStage, view);

                view.findPlayer.setDisable(false);

                view.updateView();
            }
        });

        //load players
        view.findPlayer.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (view.model.started()) {
                    view.findPlayerPopUp(myStage);
                }

            }
        });

        //this.addButtonHandlers();

        myScene = new Scene(view, view.WIN_X, view.WIN_Y);

        myStage.setTitle(view.model.tournamentName);
        myStage.setScene(myScene);
        myStage.setFullScreen(true);
        myStage.show();

        myStage.setWidth(view.WIN_X);
        myStage.setHeight(view.WIN_Y);

        myStage.setResizable(true);
    }


    public static void main(String[] args) {
        launch(args);
    }


}
