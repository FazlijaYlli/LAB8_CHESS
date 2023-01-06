package chess.engine;

public class Position {
    int x;
    int y;

    /**
     * @return The x coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * @return The y coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * Create a new position using a X axis and a Y axis
     *
     * @param x The x coordinate of the position
     * @param y The y coordinate of the position
     */
    public Position(int x, int y) {

        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new RuntimeException("Invalid position (x : " + x + ", y : " + y + ")!");
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Check if the object is equivalent to this position
     *
     * @param o Object to compare
     * @return true if the two Object are equivalent
     */
    @Override
    public boolean equals(Object o) {
        return o == this ||
                o != null && o.getClass() == getClass() &&
                        ((Position) o).x == this.x && ((Position) o).y == this.y;
    }
}
