package chess.engine.pieces;

import chess.PlayerColor;

public class Rook extends Piece{
    public Rook(PlayerColor color) {
        super(color, 7, 7, false, new int[]{0});
    }
}
