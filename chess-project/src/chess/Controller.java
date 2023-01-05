package chess;

import chess.engine.Position;
import chess.engine.pieces.*;

import java.lang.Math;

public class Controller implements ChessController {

    private Piece[][] board;
    private ChessView view;

    Position whiteKingPos;
    Position blackKingPos;

    private PlayerColor playerTurn = PlayerColor.WHITE;

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

        // Can't move empty space
        if (board[fromY][fromX] == null)
            return false;

        // Wait for opponent move
        if (board[fromY][fromX].getColor() != playerTurn)
            return false;

        Position relative = new Position(toX - fromX, toY - fromY);

        if (board[toY][toX] == null) {

            // Move
            if (!board[fromY][fromX].canMove(relative.getX(), relative.getY()))
                return false;
        } else {

            // Attack

            // Can't attack friends
            if (board[fromY][fromX].getColor() == board[toY][toX].getColor())
                return false;

            if (!board[fromY][fromX].canAttack(relative.getX(), relative.getY()))
                return false;
        }

        // Some pieces can't move over other pieces
        if (board[fromY][fromX].isCollisionable()) {

            int directionX = relative.getX() == 0 ? 0 : relative.getX() / Math.abs(relative.getX());
            int directionY = relative.getY() == 0 ? 0 : relative.getY() / Math.abs(relative.getY());

            int x = fromX + directionX, y = fromY + directionY;

            while (!(x == toX && y == toY)) {

                // Check every cell until destination

                if (board[y][x] != null)
                    return false;

                x += directionX;
                y += directionY;
            }
        }

        // Actually move piece

        view.removePiece(toX, toY);
        view.putPiece(board[fromY][fromX].getType(), board[fromY][fromX].getColor(), toX, toY);
        view.removePiece(fromX, fromY);

        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        // Update King position
        if (from.equals(whiteKingPos)) whiteKingPos = to;
        if (from.equals(blackKingPos)) blackKingPos = to;

        // Change turn
        playerTurn = playerTurn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

        return true;
    }

    public boolean isCellAttacked(PlayerColor by, int x, int y) {

        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new RuntimeException("Invalid cell : x(" + x + "), y(" + y + ")!");
        }

        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == by &&
                        board[line][column].canAttack(y - line, x - column)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void newGame() {

        board = new Piece[8][8];

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
