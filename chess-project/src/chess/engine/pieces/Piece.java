package chess.engine.pieces;

import chess.ChessView;
import chess.PlayerColor;
import chess.PieceType;

public abstract class Piece implements ChessView.UserChoice {
    private final PlayerColor color;
    private final PieceType type;

    protected boolean collisionable;

    /**
     * Create a chess piece
     *
     * @param color White or Black piece
     * @param type The type of piece
     */
    public Piece(PlayerColor color, PieceType type) {
        this.color = color;
        this.type = type;
        collisionable = true;
    }

    /**
     * @return The color of the piece
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * @return The type of the piece
     */
    public PieceType getType() {
        return type;
    }

    /**
     * @return The class name of the piece
     */
    public String textValue() {
        return getClass().getSimpleName();
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the piece
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the piece can move there
     */
    public boolean canMove(int x, int y) {
        return false;
    }

    /**
     * Check if the (x,y) relative coordinate is a valid attack for the piece
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the piece can move there
     */
    public boolean canAttack(int x, int y) {
        return canMove(x, y);
    }

    /**
     * @return if the piece need to check collision or not
     */
    public boolean isCollisionable() {
        return collisionable;
    }
}
