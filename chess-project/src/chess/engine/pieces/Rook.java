package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;

import java.util.ArrayList;

public class Rook extends CastlingPiece {

    //First Move : Castle is a King move, no need to check on the Rook side
    static final ArrayList<Move> moves;

    static {
        moves = new ArrayList<>();
        for (int pos = 1; pos < 8; pos++) {
            moves.add(new Move(new Position(pos, 0), MoveType.BOTH));
            moves.add(new Move(new Position(-pos, 0), MoveType.BOTH));
            moves.add(new Move(new Position(0, pos), MoveType.BOTH));
            moves.add(new Move(new Position(0, -pos), MoveType.BOTH));
        }
    }

    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }
}
