package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class CastlingPiece extends Piece {
    private boolean hasMoved;


    /**
     * Create a piece that can use the "Castling Move"
     *
     * @param color White or Black piece
     * @param type The type of piece
     */
    public CastlingPiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.hasMoved = false;
    }

    /**
     * @return true if piece already made a move during the game
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    public void moved() {
        hasMoved = true;
    }
}