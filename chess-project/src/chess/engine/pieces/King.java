package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class King extends CastlingPiece {
    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }

    public boolean canMove(int x, int y) {
        return (Math.abs(x) == 1 || Math.abs(y) == 1) && Math.abs(x * y) < 2;
    }
}
