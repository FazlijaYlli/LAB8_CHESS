package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Queen extends Piece{
    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    //Queens always start on X = 3 (4 on Chess board)
}
