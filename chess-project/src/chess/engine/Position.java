package chess.engine;

public class Position {
    int x;
    int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position(int x, int y) {

        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new RuntimeException("Invalid position (x : " + x + ", y : " + y + ")!");
        }

        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        return o == this ||
                o != null && o.getClass() == getClass() &&
                        ((Position) o).x == this.x && ((Position) o).y == this.y;
    }
}
