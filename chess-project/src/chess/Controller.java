package chess;

import chess.engine.Position;
import chess.engine.pieces.*;

import java.lang.Math;
import java.util.Scanner;

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

        // Actually move piece

        view.removePiece(toX, toY);
        view.putPiece(board[fromY][fromX].getType(), board[fromY][fromX].getColor(), toX, toY);
        view.removePiece(fromX, fromY);

        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        if (toY == (playerTurn == PlayerColor.WHITE ? 7 : 0) && board[toY][toX].getType() == PieceType.PAWN) {
            promotion(toX, toY);
        }

        // Update King position
        if (from.equals(whiteKingPos)) whiteKingPos = to;
        if (from.equals(blackKingPos)) blackKingPos = to;

        // Check
        if (isCellAttacked(playerTurn, playerTurn == PlayerColor.WHITE ? blackKingPos : whiteKingPos)) {
            view.displayMessage("Check!");
        }

        // Change turn
        playerTurn = ;

        return true;
    }

    private boolean collision(Position from, Position to) {

        if (board[from.getY()][from.getX()].isCollisionable()) {

            int relativeX = to.getX() - from.getX();
            int relativeY = to.getY() - from.getY();

            int directionX = relativeX == 0 ? 0 : relativeX / Math.abs(relativeX);
            int directionY = relativeY == 0 ? 0 : relativeY / Math.abs(relativeY);

            int x = from.getX() + directionX, y = from.getY() + directionY;

            while (!(x == to.getX() && y == to.getY())) {

                // Check every cell until destination

                if (board[y][x] != null)
                    return true;

                x += directionX;
                y += directionY;
            }
        }

        return false;
    }

    private void promotion(int toX, int toY) {
        Piece toPromote = view.askUser("Promotion disponible !", "Quelle pièce souhaitez-vous obtenir ?",
                new Rook(playerTurn), new Knight(playerTurn), new Bishop(playerTurn), new Queen(playerTurn));
        view.removePiece(toX,toY);
        view.putPiece(toPromote.getType(),playerTurn,toX,toY);
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
