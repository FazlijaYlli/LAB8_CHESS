package chess;

import chess.engine.Position;
import chess.engine.pieces.*;

import java.lang.Math;

public class Controller implements ChessController {

    private Piece[][] board;
    private ChessView view;

    private Position whiteKingPos;
    private Position blackKingPos;
    private Position lastMovePos;

    private PlayerColor playerTurn;

    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    @Override
    public void newGame() {

        board = new Piece[8][8];
        playerTurn = PlayerColor.WHITE;

        // Back Pieces

        for (int i = 0; i < 4; ++i) {
            PlayerColor currentColor = i % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;

            board[7 * (i % 2)][7 * (i > 1 ? 1 : 0)] = new Rook(currentColor);
            board[7 * (i % 2)][1 + 5 * (i > 1 ? 1 : 0)] = new Knight(currentColor);
            board[7 * (i % 2)][2 + 3 * (i > 1 ? 1 : 0)] = new Bishop(currentColor);

            if (i > 1) {
                board[7 * (i % 2)][3] = new Queen(currentColor);
            } else {
                board[7 * (i % 2)][4] = new King(currentColor);

                switch (currentColor) {
                    case WHITE -> whiteKingPos = new Position(4, 0);
                    case BLACK -> blackKingPos = new Position(4, 7);
                }
            }
        }

        // Pawns

        for (int i = 0; i < 16; ++i) {
            PlayerColor currentColor = i % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
            board[1 + 5 * (i % 2)][i % 8 - (i % 2) + i / 8] = new Pawn(currentColor);
        }

        // Put pieces on view to be visible

        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null) {
                    view.putPiece(board[line][column].getType(), board[line][column].getColor(), column, line);
                }
            }
        }
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        Position from = new Position(fromX, fromY);
        Position to = new Position(toX, toY);

        // Not moving isn't a move
        if (from.equals(to))
            return false;

        // Can't move empty space
        if (board[fromY][fromX] == null)
            return false;

        // Wait for opponent move
        if (board[fromY][fromX].getColor() != playerTurn)
            return false;

        // Special moves

        if (castling(from, to)) {
            return true;
        }

        if (enPassant(from, to)) {
            return true;
        }

        // Normal move

        int relativeX = toX - fromX;
        int relativeY = toY - fromY;

        if (board[toY][toX] == null) {

            // Move
            if (!board[fromY][fromX].canMove(relativeX, relativeY))
                return false;
        } else {

            // Attack

            // Can't attack friends
            if (board[fromY][fromX].getColor() == board[toY][toX].getColor())
                return false;

            if (!board[fromY][fromX].canAttack(relativeX, relativeY))
                return false;
        }

        // Some pieces can't move over other pieces
        if (collision(from, to)) return false;

        // Simulation to prevent illegal moves
        if (board[fromY][fromX] instanceof King && isCellAttacked(getOpponent(), to)) {
            return false;
        } else {

            // Simulation to prevent self-check
            board[toY][toX] = board[fromY][fromX];
            board[fromY][fromX] = null;

            boolean check = isCellAttacked(getOpponent(), currentPlayerKingPos());

            board[fromY][fromX] = board[toY][toX];
            board[toY][toX] = null;

            if (check) return false;
        }

        endOfTurn(from, to);

        return true;
    }

    private PlayerColor getOpponent() {
        return playerTurn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    private Position currentPlayerKingPos() {
        return playerTurn == PlayerColor.WHITE ? whiteKingPos : blackKingPos;
    }

    private Position opponentPlayerKingPos() {
        return playerTurn == PlayerColor.WHITE ? blackKingPos : whiteKingPos;
    }

    private boolean collision(Position from, Position to) {

        if (board[from.getY()][from.getX()].isCollisionable()) {

            int relativeX = to.getX() - from.getX();
            int relativeY = to.getY() - from.getY();

            int directionX = relativeX == 0 ? 0 : relativeX / Math.abs(relativeX);
            int directionY = relativeY == 0 ? 0 : relativeY / Math.abs(relativeY);

            int x = from.getX() + directionX, y = from.getY() + directionY;

            while (!(x < 0 || x > 7 || y < 0 || y > 7) && !(x == to.getX() && y == to.getY())) {

                // Check every cell until destination

                if (board[y][x] != null)
                    return true;

                x += directionX;
                y += directionY;
            }
        }

        return false;
    }

    private boolean isCellAttacked(PlayerColor by, Position cell) {

        // Altough not "optimized" for a standard chess game,
        // it allows the creation of new pieces without any restraint
        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == by &&
                        board[line][column].canAttack(cell.getX() - column, cell.getY() - line)
                        && !collision(new Position(column, line), cell)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean castling(Position from, Position to) {

        if (!(to.getX() == 6 || to.getX() == 2))
            return false;

        boolean queenSide = to.getX() == 2;
        int rookFromX = queenSide ? 0 : 7;
        int rookToX = queenSide ? 3 : 5;

        if (!(board[from.getY()][from.getX()] instanceof King && board[from.getY()][rookFromX] instanceof Rook)
                || (((CastlingPiece) board[from.getY()][from.getX()]).getHasMoved()
                || ((CastlingPiece) board[from.getY()][rookFromX]).getHasMoved())) {
            return false;
        }

        if (isCellAttacked(getOpponent(), from))
            return false;

        for (int x = from.getX() + (queenSide ? -1 : 1); x != to.getX(); x += queenSide ? -1 : 1) {
            if (!(board[from.getY()][x] == null && !isCellAttacked(getOpponent(), new Position(x, from.getY()))))
                return false;
        }

        // Moving the rook, king is taken care of in endOfTurn() function
        view.putPiece(PieceType.ROOK, playerTurn, rookToX, to.getY());
        view.removePiece(rookFromX, from.getY());

        board[to.getY()][rookToX] = board[from.getY()][rookFromX];
        board[from.getY()][rookFromX] = null;

        ((CastlingPiece) board[to.getY()][rookToX]).moved();

        endOfTurn(from, to);

        return true;
    }

    private boolean enPassant(Position from, Position to) {

        if (lastMovePos == null) return false;

        if (board[lastMovePos.getY()][lastMovePos.getX()] instanceof Pawn
                && ((CountingMovePiece) board[lastMovePos.getY()][lastMovePos.getX()]).getNbMoves() == 1
                && lastMovePos.getY() == from.getY()
                && Math.abs(lastMovePos.getX() - from.getX()) == 1
                && board[from.getY()][from.getX()].canAttack(to.getX() - from.getX(), to.getY() - from.getY())) {

            // Simulation to prevent self-check

            int relativeY = playerTurn == PlayerColor.WHITE ? -1 : 1;

            board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
            board[from.getY()][from.getX()] = null;
            Piece deadPiece = board[to.getY() + relativeY][to.getX()];
            board[to.getY() + relativeY][to.getX()] = null;

            boolean check = isCellAttacked(getOpponent(), currentPlayerKingPos());

            board[from.getY()][from.getX()] = board[to.getY()][to.getX()];
            board[to.getY()][to.getX()] = null;

            if (check) {
                board[to.getY() + relativeY][to.getX()] = deadPiece;
                return false;
            }

            view.removePiece(to.getX(), to.getY() + relativeY);

            endOfTurn(from, to);

            return true;
        }

        return false;
    }

    private void promotion(Position to) {
        Piece promotedPiece = view.askUser("Promotion possible !", "Quelle pièce souhaitez-vous obtenir ?",
                new Rook(playerTurn), new Knight(playerTurn), new Bishop(playerTurn), new Queen(playerTurn));

        view.putPiece(promotedPiece.getType(), playerTurn, to.getX(), to.getY());
        board[to.getY()][to.getX()] = promotedPiece;
    }

    private void endOfTurn(Position from, Position to) {

        // Move the piece
        view.putPiece(board[from.getY()][from.getX()].getType(), playerTurn, to.getX(), to.getY());
        view.removePiece(from.getX(), from.getY());

        board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
        board[from.getY()][from.getX()] = null;

        // Promotion
        if (board[to.getY()][to.getX()] instanceof Pawn && to.getY() == (playerTurn == PlayerColor.WHITE ? 7 : 0)) {
            promotion(to);
        }

        // Update last move
        lastMovePos = to;

        // Update Kings positions
        if (from.equals(whiteKingPos)) whiteKingPos = to;
        if (from.equals(blackKingPos)) blackKingPos = to;

        // Check
        if (isCellAttacked(playerTurn, opponentPlayerKingPos())) {
            view.displayMessage("Check!");
        }

        // Increment moves where needed
        if (board[to.getY()][to.getX()] instanceof CastlingPiece)
            ((CastlingPiece) board[to.getY()][to.getX()]).moved();

        if (board[to.getY()][to.getX()] instanceof CountingMovePiece)
            ((CountingMovePiece) board[to.getY()][to.getX()]).incrementNbMoves();

        // Change turn
        playerTurn = getOpponent();
    }
}
