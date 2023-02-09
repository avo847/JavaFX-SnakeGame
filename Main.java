import snake.Snake;
import snake.SnakeGame;

import mosaic.MosaicCanvas;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
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
import javafx.scene.paint.Color;

public class Main extends Application {
    public static void main(String[] args) {
        /*
        Snake snake = new Snake(10, 10, Snake.Direction.UP);
        snake.testSnake(); 

        snake.updatePosForNewFrame();
        snake.testSnake();
        snake.updatePosForNewFrame();
        snake.testSnake();
        snake.changeDirection(Snake.Direction.LEFT);
        System.out.println("Turned left...");
        snake.updatePosForNewFrame();
        snake.testSnake();
        snake.grow();
        snake.testSnake();
        snake.updatePosForNewFrame();
        snake.testSnake();
        snake.updatePosForNewFrame();
        snake.testSnake();
        */
        launch(args);
    }

    // fields
    private SnakeGame gameInstance;
    private MosaicCanvas board;

    public void start(Stage stage) {

        // set up scene:
        gameInstance = new SnakeGame();
        board = gameInstance.getBoard();


        BorderPane root = new BorderPane(board);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> gameInstance.keyHandle(e));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Snake");
        stage.show();
    }
}