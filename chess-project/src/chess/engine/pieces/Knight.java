package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends Piece {

    /**
     * Create a Knight piece
     *
     * @param color White or Black piece
     */
    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
        collisionable = false;
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the Knight
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Knight can move there
     */
    public boolean canMove(int x, int y) {
        return Math.abs(x * y) == 2;
    }
}
