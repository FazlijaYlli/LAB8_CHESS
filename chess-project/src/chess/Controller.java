package chess;

import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;
import chess.engine.pieces.*;

import java.util.ArrayList;
import java.lang.Math;

public class Controller implements ChessController {

    Piece[][] board;
    int[][] attackBoard;
    ChessView view;

    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        if (toX == fromX && toY == fromY)
            return false;

        if (board[fromY][fromX] == null)
            return false;

        ArrayList<Move> possibleMoves = board[fromY][fromX].getMoves();

        for (Move move : possibleMoves) {
            Position truePosition = new Position(
                    move.getDestination().getX() + fromX,
                    move.getDestination().getY() + fromY
            );
            if (truePosition.equals(new Position(toX, toY))) {

                //Empêcher de bouger s'y a une pièce sur le chemin
                if (truePosition.getX() != 1 && truePosition.getY() != 1) {

                    boolean pieceOnTheWay = false;
                    boolean isXnull = move.getDestination().getX() == 0;
                    boolean isXnegative = move.getDestination().getX() < 0;
                    boolean isYnull = move.getDestination().getY() == 0;
                    boolean isYnegative = move.getDestination().getY() < 0;

                    for (int i = 1; i < Math.max(
                            Math.abs(move.getDestination().getX()),
                            Math.abs(move.getDestination().getY()));
                         ++i) {

                        if (board[fromY + i * (isYnull ? 0 : isYnegative ? -1 : 1)]
                                [fromX + i * (isXnull ? 0 : isXnegative ? -1 : 1)] != null) {
                            pieceOnTheWay = true;
                            break;
                        }
                    }

                    if (pieceOnTheWay) {
                        continue;
                    }
                }

                if (move.getType() != MoveType.MOVE) {
                    view.removePiece(toX, toY);
                }

                view.putPiece(board[fromY][fromX].getType(), board[fromY][fromX].getColor(), toX, toY);
                view.removePiece(fromX, fromY);
                board[toY][toX] = board[fromY][fromX];
                board[fromY][fromX] = null;
                return true;
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
            }
            else {
                board[7 * (i % 2)][4] = new King(currentColor);
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

        // Setup attacks board
        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                Piece p = board[line][column];
                if (p != null) {

                }
            }
        }
    }
}
