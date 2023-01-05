package chess;

import chess.engine.Position;
import chess.engine.pieces.*;

import java.lang.Math;
import java.util.Scanner;

public class Controller implements ChessController {

    private Piece[][] board;
    private ChessView view;

    private Position whiteKingPos;
    private Position blackKingPos;

    private int nbChecks;
    private Piece lastMove;
    private Position posLastMove;

    private PlayerColor playerTurn;

    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        Position from = new Position(fromX, fromY);
        Position to = new Position(toX, toY);

        // Not moving isn't a move
        if (from.equals(to))
            return false;

        // Movements spéciaux
        if (board[fromY][fromX] instanceof King) {
            if (castling(fromX, fromY, toX, toY))
                return true;
        }

        // Can't move empty space
        if (board[fromY][fromX] == null)
            return false;

        // Wait for opponent move
        if (board[fromY][fromX].getColor() != playerTurn)
            return false;

        int relativeX = toX - fromX;
        int relativeY = toY - fromY;

        /*if (nbChecks > 0) {

            boolean countered = false;
            Position currentKingPos = playerTurn == PlayerColor.WHITE ? whiteKingPos : blackKingPos;

            // Option 1 : Eating the piece that caused a check, which only possible if there's 1 checking piece
            if (nbChecks == 1) {
                // There are 3 scenarios for this option :
                // Scenario 1 : The piece that moved checked the King
                countered = lastMove.canAttack(relativeX, relativeY)
                        && lastMove.equals(board[toY][toX])
                        && board[fromY][fromX].canAttack(relativeX, relativeY);

                // Scenario 2 : The piece that moved created a discovered check
                if (!countered) {
                    int findX = currentKingPos.getX() - posLastMove.getX();
                    int findY = currentKingPos.getY() - posLastMove.getY();

                    int directionX = findX == 0 ? 0 : findX / Math.abs(findX);
                    int directionY = findY == 0 ? 0 : findY / Math.abs(findY);

                    int x = posLastMove.getX() + directionX, y = posLastMove.getY() + directionY;

                    while (!(x < 0 || x > 7 || y < 0 || y > 7) && board[y][x] == null) {

                        x += directionX;
                        y += directionY;
                    }

                    countered = board[y][x].canAttack(currentKingPos.getX() - x, currentKingPos.getY() - y);

                    // Scenario 3 : An En Passant created a discovered check via the eaten pawn
                    if (!countered) {
                        // TODO
                    }
                }

                // Option 2 : Blocking the move, also requires 1 checking piece
                if (lastMove.isCollisionable()) {

                }
            }

            // Option 3 : Moving the king out of harm
            if (!countered) {
                countered = from.equals(currentKingPos) && !isCellAttacked(getOpponent(), to);
            }

            if (!countered) return false;

        } else {*/

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
        //}

        // Some pieces can't move over other pieces
        if (collision(from, to)) return false;

        // Actually move piece
        view.removePiece(toX, toY);
        view.putPiece(board[fromY][fromX].getType(), playerTurn, toX, toY);
        view.removePiece(fromX, fromY);

        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        if (toY == (playerTurn == PlayerColor.WHITE ? 7 : 0) && board[toY][toX] instanceof Pawn) {
            promotion(toX, toY);
        }

        // Update last move
        lastMove = board[toY][toX];
        posLastMove = from;

        // Update King position
        if (from.equals(whiteKingPos)) whiteKingPos = to;
        if (from.equals(blackKingPos)) blackKingPos = to;

        // Check
        if ((nbChecks =
                countCellAttacked(playerTurn, playerTurn ==
                        PlayerColor.WHITE ? blackKingPos : whiteKingPos))
                > 0) {
            view.displayMessage("Check!");
        }

        // Change turn
        playerTurn = getOpponent();

        return true;
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

    PlayerColor getOpponent() {
        return playerTurn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    private void promotion(int toX, int toY) {
        Piece toPromote = view.askUser("Promotion disponible !", "Quelle pièce souhaitez-vous obtenir ?",
                new Rook(playerTurn), new Knight(playerTurn), new Bishop(playerTurn), new Queen(playerTurn));
        view.removePiece(toX, toY);
        view.putPiece(toPromote.getType(), playerTurn, toX, toY);
        board[toY][toX] = toPromote;
    }

    private boolean isCellAttacked(PlayerColor by, Position cell) {

        // Altough not "optimized" for a standard chess game,
        // it allows the creation of new pieces without any restraint
        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == by &&
                        board[line][column].canAttack(cell.getY() - line, cell.getX() - column)
                        && !collision(new Position(column, line), cell)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int countCellAttacked(PlayerColor by, Position cell) {

        // Uses same code as isCellAttacked above except it counts the number of attacks
        int counter = 0;

        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == by &&
                        board[line][column].canAttack(cell.getY() - line, cell.getX() - column)
                        && !collision(new Position(column, line), cell)) {
                    ++counter;
                }
            }
        }

        return counter;
    }

    boolean castling(int fromX, int fromY, int toX, int toY) {

        if (toY != fromY)
            return false;

        if (!(toX == 6 || toX == 1))
            return false;

        boolean bigCastle = toX == 1;
        int rookX = bigCastle ? 0 : 7;

        //check if pieces are castlelable and has already moved
        if (!(board[fromY][fromX] instanceof King
                && board[fromY][rookX] instanceof Rook)) {
            if (((SpecialMovePiece) board[fromY][fromX]).getHasMoved()
                    || ((SpecialMovePiece) board[fromY][rookX]).getHasMoved()) {
                return false;
            }
            return false;
        }

        for (int x = fromX + (bigCastle ? -1 : 1); x != toX; x += bigCastle ? -1 : 1) {
            if (!(board[fromY][x] == null && !isCellAttacked(getOpponent(), new Position(x, fromY))))
                return false;
        }

        // moving pieces
        //king
        view.putPiece(PieceType.KING, playerTurn, toX, toY);
        view.removePiece(fromX, fromY);

        //rook
        view.putPiece(PieceType.ROOK, playerTurn, bigCastle ? 2 : 5, toY);
        view.removePiece((bigCastle ? 0 : 7), fromY);

        //king
        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        //rook
        board[toY][bigCastle ? 2 : 5] = board[fromY][bigCastle ? 0 : 7];
        board[fromY][bigCastle ? 0 : 7] = null;

        playerTurn = getOpponent();
        return true;

    }

    @Override
    public void newGame() {

        board = new Piece[8][8];
        nbChecks = 0;
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
}
