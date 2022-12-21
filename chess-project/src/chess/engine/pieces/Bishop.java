package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.Move;
import chess.engine.Position;
import chess.engine.MoveType;

import java.util.ArrayList;

public class Bishop extends Piece{

    static final ArrayList<Move> moves;

    static {
        moves = new ArrayList<>();
        for (int pos = 1; pos < 8; ++pos) {
            moves.add(new Move(new Position(pos, pos), MoveType.BOTH));
            moves.add(new Move(new Position(pos, -pos), MoveType.BOTH));
            moves.add(new Move(new Position(-pos, pos), MoveType.BOTH));
            moves.add(new Move(new Position(-pos, -pos), MoveType.BOTH));
        }
    }

    public Bishop(PlayerColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }
}
