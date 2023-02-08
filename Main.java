import snake.Snake;

public class Main {
    public static void main(String[] args) {
        Snake snake = new Snake(10, 10, Snake.Direction.UP);

        snake.testSnake();
    }
}