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
import javafx.scene.control.Label;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    private Button nextLevelButton;
    private Button retryButton;
    private Button quitButton; 

    private int level;
    private int livesRemaining = 3;

    private Label levelLabel;
    private Label livesLabel;

    public void start(Stage stage) {

        HBox buttonBar = setUpButtonBar();
        // set up scene:
        level = 1;
        livesRemaining = 3;
        gameInstance = new SnakeGame(level, nextLevelButton);
        board = gameInstance.getBoard();

        
        GridPane displayBar = setUpDisplayBar();


        root = new BorderPane(board);
        root.setBottom(buttonBar);

        root.setTop(displayBar);

        scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
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
        nextLevelButton = new Button("NEXT LEVEL");
        retryButton = new Button("RETRY");
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
        nextLevelButton.setOnAction(e -> {
            gameInstance = new SnakeGame(++level, nextLevelButton);
            board = gameInstance.getBoard();
            root.setCenter(board);
            updateLabels();
            pauseButton.setDisable(true);
            startButton.setDisable(false);
            nextLevelButton.setDisable(true);
        });
        retryButton.setOnAction(e -> {
            if (gameInstance.gameStatus == SnakeGame.GameStatus.LOST){
                livesRemaining--;
                updateLabels();
            }
            gameInstance = new SnakeGame(level, nextLevelButton);
            board = gameInstance.getBoard();
            root.setCenter(board);
            startButton.setDisable(false);// initialize in paused state
            pauseButton.setDisable(true);
        });
        quitButton.setOnAction(e -> Platform.exit());

        // set initial disabled
        pauseButton.setDisable(true);
        nextLevelButton.setDisable(true);

        // set focus traversable to false
        startButton.setFocusTraversable(false);
        pauseButton.setFocusTraversable(false);
        nextLevelButton.setFocusTraversable(false);
        retryButton.setFocusTraversable(false);
        quitButton.setFocusTraversable(false);

        HBox buttonBar = new HBox(startButton, pauseButton, nextLevelButton, retryButton, quitButton);

        HBox.setHgrow(startButton, Priority.ALWAYS);
        HBox.setHgrow(pauseButton, Priority.ALWAYS);
        HBox.setHgrow(nextLevelButton, Priority.ALWAYS);
        HBox.setHgrow(retryButton, Priority.ALWAYS);
        HBox.setHgrow(quitButton, Priority.ALWAYS);
        startButton.setMaxWidth(Double.POSITIVE_INFINITY);
        pauseButton.setMaxWidth(Double.POSITIVE_INFINITY);
        nextLevelButton.setMaxWidth(Double.POSITIVE_INFINITY);
        retryButton.setMaxWidth(Double.POSITIVE_INFINITY);
        quitButton.setMaxWidth(Double.POSITIVE_INFINITY);

        return buttonBar;
    }

    GridPane setUpDisplayBar() {
        GridPane displayBar = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        displayBar.getColumnConstraints().addAll(col1, col2);

        levelLabel = new Label("Level: " + level);
        livesLabel = new Label("Lives Remaining: " + livesRemaining);

        displayBar.setStyle("-fx-padding:5px;" +
                      "-fx-border-color:darkblue;" +
                      "-fx-border-width:2px;" +
                      "-fx-background-color:#DDF");

        levelLabel.setStyle("-fx-font-size: 20;" +
                            "-fx-font-weight: bold;");
        livesLabel.setStyle("-fx-font-size: 20;" +
                            "-fx-font-weight: bold;");

        displayBar.add(levelLabel, 0, 0);
        displayBar.add(livesLabel, 1, 0);

        return displayBar;
    }

    private void updateLabels() {
        levelLabel.setText("Level " + level);
        livesLabel = new Label("Lives Remaining: " + livesRemaining);
    }
}