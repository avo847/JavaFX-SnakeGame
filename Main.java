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
    private Scene scene;
    private BorderPane root;

    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button quitButton; 

    private int level;

    public void start(Stage stage) {

        // set up scene:
        level = 1;
        gameInstance = new SnakeGame(level);
        board = gameInstance.getBoard();

        HBox buttonBar = setUpButtonBar();

        root = new BorderPane(board);
        root.setBottom(buttonBar);

        scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            System.out.println("Keypress registered");
            gameInstance.keyTurn(e);
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Snake");
        stage.show();
    }

    public HBox setUpButtonBar() {
        startButton = new Button("START");
        pauseButton = new Button("PAUSE");
        resetButton = new Button("RESET");
        quitButton = new Button("QUIT");
        startButton.setOnAction(e -> {
            gameInstance.startAnimation();
            startButton.setDisable(true);
            pauseButton.setDisable(false);
        });
        pauseButton.setOnAction(e -> {
            gameInstance.stopAnimation();
            startButton.setDisable(false);
            pauseButton.setDisable(true);
        });
        resetButton.setOnAction(e -> {
            gameInstance.stopAnimation();
            gameInstance = new SnakeGame(1);
            board = gameInstance.getBoard();
            root.setCenter(board);
            startButton.setDisable(false);// initialize in paused state
            pauseButton.setDisable(true);
        });
        quitButton.setOnAction(e -> Platform.exit());

        // set initial disabled
        pauseButton.setDisable(true);

        // set focus traversable to false
        startButton.setFocusTraversable(false);
        pauseButton.setFocusTraversable(false);
        resetButton.setFocusTraversable(false);
        quitButton.setFocusTraversable(false);

        HBox buttonBar = new HBox(startButton, pauseButton, resetButton, quitButton);

        HBox.setHgrow(startButton, Priority.ALWAYS);
        HBox.setHgrow(pauseButton, Priority.ALWAYS);
        HBox.setHgrow(resetButton, Priority.ALWAYS);
        HBox.setHgrow(quitButton, Priority.ALWAYS);
        startButton.setMaxWidth(Double.POSITIVE_INFINITY);
        pauseButton.setMaxWidth(Double.POSITIVE_INFINITY);
        resetButton.setMaxWidth(Double.POSITIVE_INFINITY);
        quitButton.setMaxWidth(Double.POSITIVE_INFINITY);

        return buttonBar;
    }
}