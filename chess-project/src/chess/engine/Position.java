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
