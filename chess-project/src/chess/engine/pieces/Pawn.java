package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends CountingMovePiece {

    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }

    public boolean canMove(int x, int y) {
        if (getNbMoves() == 0 && y == (getColor() == PlayerColor.WHITE ? 2 : -2) && x == 0) {
            return true;
        } else {
            return y == (getColor() == PlayerColor.WHITE ? 1 : -1) && x == 0;
        }
    }

    public boolean canAttack(int x, int y) {
        return y == (getColor() == PlayerColor.WHITE ? 1 : -1) && Math.abs(x) == 1;
    }
}
