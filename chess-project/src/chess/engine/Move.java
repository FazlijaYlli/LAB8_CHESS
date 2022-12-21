package chess.engine;

public class Move {
    private final Position destination;
    private final MoveType type;

    public Move(Position destination, MoveType type)
    {
        this.destination = destination;
        this.type = type;
    }

    public Position getDestination() {
        return destination;
    }

    public MoveType getType() {
        return type;
    }
}
