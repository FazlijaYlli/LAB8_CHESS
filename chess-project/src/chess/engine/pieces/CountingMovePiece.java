package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class CountingMovePiece extends Piece {
    private int nbMoves;

    public CountingMovePiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.nbMoves = 0;
    }

    public int getNbMoves() {
        return nbMoves;
    }

    public void incrementNbMoves() {
        ++nbMoves;
    }
}
