package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;

import java.util.ArrayList;

public class King extends CastlingPiece{

    static final ArrayList<Move> moves;

    static {
        moves = new ArrayList<>();
        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                if (x == 0 && y == 0) continue;
                moves.add(new Move(new Position(x, y), MoveType.BOTH));
            }
        }
    }

    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }

    //First Move : maxX = 2, maxY = 1, excludeZero = false, validMoveRatio = {0, 1}
    //For X = -2 or 2 to be valid, Rook on this side must not have moved
    //When moving if X = -2 or 2, initiate a Castle
    //A Check has to be done before trying to move (for a Castle too) that destination is not attacked
    //Castle : Check if 1)King not attacked, 2)Cell where Rook moves isn't attacked
    //      Then, move Rook next to King and move King to destination

    //Kings always start on X = 4 (5 on Chess Board)
}
