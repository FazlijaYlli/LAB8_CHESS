package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class CountingMovePiece extends Piece {
    private int nbMoves;

    /**
     * Create a piece that need to keep track of it's number of moves
     *
     * @param color White or Black piece
     * @param type The type of piece
     */
    public CountingMovePiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.nbMoves = 0;
    }

    /**
     * @return Number of move the piece made so far
     */
    public int getNbMoves() {
        return nbMoves;
    }

    public void incrementNbMoves() {
        ++nbMoves;
    }
}
