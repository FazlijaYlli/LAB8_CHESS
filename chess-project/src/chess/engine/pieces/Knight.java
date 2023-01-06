package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends Piece {

    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
        collisionable = false;
    }

    public boolean canMove(int x, int y) {
        return Math.abs(x * y) == 2;
    }
}
