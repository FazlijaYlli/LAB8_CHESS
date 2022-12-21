package chess.engine;

public class Position {
    int x;
    int y;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

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
