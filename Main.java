import snake.Snake;
import snake.SnakeGame;
import snake.FoodStuffs;

import mosaic.MosaicCanvas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // fields
    private SnakeGame gameInstance;
    private MosaicCanvas board;

    public void start(Stage stage) {

        // set up scene:
        gameInstance = new SnakeGame();
        board = gameInstance.getBoard();

        HBox buttonBar = setUpButtonBar();

        BorderPane root = new BorderPane(board);
        root.setBottom(buttonBar);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> gameInstance.keyTurn(e));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Snake");
        stage.show();
    }

    public HBox setUpButtonBar() {
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");
        Button quitButton = new Button("Quit");
        startButton.setOnAction(e -> {
            gameInstance.startAnimation();
            startButton.setDisable(true);
            stopButton.setDisable(false);
        });
        stopButton.setOnAction(e -> {
            gameInstance.stopAnimation();
            startButton.setDisable(false);
            stopButton.setDisable(true);
        });
        resetButton.setOnAction(e -> gameInstance.resetAnimation());
        quitButton.setOnAction(e -> Platform.exit());

        // set initial values
        stopButton.setDisable(true);
        resetButton.setDisable(true);

        HBox buttonBar = new HBox(startButton, stopButton, resetButton, quitButton);

        HBox.setHgrow(startButton, Priority.ALWAYS);
        HBox.setHgrow(stopButton, Priority.ALWAYS);
        HBox.setHgrow(resetButton, Priority.ALWAYS);
        HBox.setHgrow(quitButton, Priority.ALWAYS);
        startButton.setMaxWidth(Double.POSITIVE_INFINITY);
        stopButton.setMaxWidth(Double.POSITIVE_INFINITY);
        resetButton.setMaxWidth(Double.POSITIVE_INFINITY);
        quitButton.setMaxWidth(Double.POSITIVE_INFINITY);

        return buttonBar;
    }
}