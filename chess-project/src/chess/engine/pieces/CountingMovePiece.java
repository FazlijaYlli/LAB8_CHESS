package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class CountingMovePiece extends Piece {
    int nbrOfMoves;

    public CountingMovePiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.nbrOfMoves = 0;
    }

    public int getNbrOfMoves() {
        return nbrOfMoves;
    }
    public void incrementNbrOfMoves() {
        ++nbrOfMoves;
    }
}
