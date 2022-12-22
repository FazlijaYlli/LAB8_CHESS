package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class King extends CastlingPiece{



    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }

    public boolean canMove(int x, int y){
        return (Math.abs(x) == 1 || Math.abs(y) == 1) && Math.abs(x*y) < 2;
    }

    //First Move : maxX = 2, maxY = 1, excludeZero = false, validMoveRatio = {0, 1}
    //For X = -2 or 2 to be valid, Rook on this side must not have moved
    //When moving if X = -2 or 2, initiate a Castle
    //A Check has to be done before trying to move (for a Castle too) that destination is not attacked
    //Castle : Check if 1)King not attacked, 2)Cell where Rook moves isn't attacked
    //      Then, move Rook next to King and move King to destination

    //Kings always start on X = 4 (5 on Chess Board)
}
