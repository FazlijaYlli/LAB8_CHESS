package chess.engine.pieces;

public abstract class castlingPiece extends Piece {
    boolean hasMoved;

    public castlingPiece(PlayerColor color, int maxX, int maxY, boolean excludeZero, int[] validMoveRatio) {
        super(color, maxX, maxY, excludeZero, validMoveRatio);
    }
}
