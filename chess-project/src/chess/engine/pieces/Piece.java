package chess.engine.pieces;

import chess.ChessView;
import chess.PlayerColor;
import chess.PieceType;

public abstract class Piece implements ChessView.UserChoice {
    private final PlayerColor color;
    private final PieceType type;

    protected boolean collisionable;

    public Piece(PlayerColor color, PieceType type) {
        this.color = color;
        this.type = type;
        collisionable = true;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public void move(int x, int y) {
        if (x == 0 && y == 0) {
            throw new IllegalArgumentException("Not moving isn't a move!");
        }
    }

    public String textValue() {
        return getClass().getSimpleName();
    }

    public boolean canMove(int x, int y) {
        return false;
    }

    public boolean canAttack(int x, int y) {
        return canMove(x, y);
    }

    public boolean isCollisionable() {
        return collisionable;
    }
}
