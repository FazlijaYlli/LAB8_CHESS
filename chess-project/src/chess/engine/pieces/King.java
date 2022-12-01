package chess.engine.pieces;

import chess.PlayerColor;

public class King extends Piece{
    public King(PlayerColor color) {
        super(color, 1, 1, false, new int[]{0, 1});
    }
}
