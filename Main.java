import snake.Snake;

public class Main {
    public static void main(String[] args) {
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

    }
}