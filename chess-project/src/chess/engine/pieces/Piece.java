package chess.engine.pieces;

import chess.PlayerColor;

public abstract class Piece {
    // TODO : Compléter les attributs pour premier mouvement.
    private final PlayerColor color;
    private final int maxX;
    private final int maxY;
    private final boolean exludeZero;
    // TODO : trouver meilleur nom.
    private final int[] validMoveRatio;

    public Piece(PlayerColor color, int maxX, int maxY, boolean exludeZero, int[] validMoveRatio) {
        this.color = color;
        this.maxX = maxX;
        this.maxY = maxY;
        this.exludeZero = exludeZero;
        this.validMoveRatio = validMoveRatio;
    }

    public void move(int x, int y) {
        if (x == 0 && y == 0) {
            throw new IllegalArgumentException("Not moving isn't a move!");
        }
    }
}
