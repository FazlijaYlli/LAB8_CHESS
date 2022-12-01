package chess.engine.pieces;

import chess.PlayerColor;

public class Queen extends Piece{
    public Queen(PlayerColor color) {
        super(color, 7, 7, false, new int[]{0, 1, 4, 9, 16, 25, 36, 49});
    }
}
