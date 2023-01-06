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

    private boolean gameEnded;
    private int piecesCounter;

    /**
     * Start the view according to the view
     *
     * @param view The view to use
     */
    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    /**
     * Initialise a new chess game
     */
    @Override
    public void newGame() {

        board = new Piece[8][8];
        playerTurn = PlayerColor.WHITE;
        gameEnded = false;
        piecesCounter = 0;

        // Back Pieces

        for (int i = 0; i < 4; ++i) {
            PlayerColor currentColor = i % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;

            board[7 * (i % 2)][7 * (i > 1 ? 1 : 0)] = new Rook(currentColor);
            ++piecesCounter;
            board[7 * (i % 2)][1 + 5 * (i > 1 ? 1 : 0)] = new Knight(currentColor);
            ++piecesCounter;
            board[7 * (i % 2)][2 + 3 * (i > 1 ? 1 : 0)] = new Bishop(currentColor);
            ++piecesCounter;

            if (i > 1) {
                board[7 * (i % 2)][3] = new Queen(currentColor);
                ++piecesCounter;
            } else {
                board[7 * (i % 2)][4] = new King(currentColor);
                ++piecesCounter;

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
            ++piecesCounter;
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

    /**
     * Methode to check if the input move is correct. If it's correct, it moves the piece.
     *
     * @param fromX x coordinate where the piece is
     * @param fromY y coordinate where the piece is
     * @param toX x coordinate where the piece want to go
     * @param toY y coordinate where the piece want to go
     * @return true if the move is correct
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        if (gameEnded) return false;

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
            boolean check;

            Piece attackedPiece = board[toY][toX];
            board[toY][toX] = board[fromY][fromX];
            board[fromY][fromX] = null;

            if (board[toY][toX] instanceof King) {
                check = isCellAttacked(getOpponent(), to);
            } else {
                check = isCellAttacked(getOpponent(), currentPlayerKingPos());
            }

            board[fromY][fromX] = board[toY][toX];
            board[toY][toX] = attackedPiece;

            if (check) return false;
        }

        endOfTurn(from, to);

        return true;
    }

    /**
     * @return The color of the opponent
     */
    private PlayerColor getOpponent() {
        return playerTurn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    /**
     * @return The current player's King's position
     */
    private Position currentPlayerKingPos() {
        return playerTurn == PlayerColor.WHITE ? whiteKingPos : blackKingPos;
    }

    /**
     * Check if there is no collision between from to to
     *
     * @param from Position of the start piece
     * @param to Position of the end piece
     * @return true if the piece can go at the to position without colliding with anothere piece
     */
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

    /**
     * Check if the cell is attacked by a piece of the attacker
     *
     * @param by Color of the attacker
     * @param cell Position of the piece to attack
     * @return true if the cell is attacked by at least one piece
     */
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

    /**
     * Check conditions to "Castle" and do it if check pass
     *
     * @param from Position of the start piece
     * @param to Position of the end piece
     * @return true if the castle can happen
     * @link <a href="https://en.wikipedia.org/wiki/castling">Wikipedia : Castling</a>
     */
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

    /**
     * Check conditions to do the "En Passant" and do it if check pass
     *
     * @param from Position of the start piece
     * @param to Position of the end piece
     * @return true if the "En Passant" can happen
     * @link <a href="https://en.wikipedia.org/wiki/En_passant">Wikipedia : En Passant</a>
     */
    private boolean enPassant(Position from, Position to) {

        if (lastMovePos == null) return false;

        if (board[from.getY()][from.getX()] instanceof Pawn
                && board[lastMovePos.getY()][lastMovePos.getX()] instanceof Pawn
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
            --piecesCounter;

            endOfTurn(from, to);

            return true;
        }

        return false;
    }

    /**
     * Promote a Pawn to a Rook, a Knight, a Bishop or a Queen
     *
     * @param to Position where the Pawn is going
     */
    private void promotion(Position to) {
        Piece promotedPiece = view.askUser("Promotion possible !", "Quelle pièce souhaitez-vous obtenir ?",
                new Rook(playerTurn), new Knight(playerTurn), new Bishop(playerTurn), new Queen(playerTurn));

        view.putPiece(promotedPiece.getType(), playerTurn, to.getX(), to.getY());
        board[to.getY()][to.getX()] = promotedPiece;
    }

    /**
     * Do all the action required to end the turn
     *
     * @param from Position of the start piece
     * @param to Position of the end piece
     */
    private void endOfTurn(Position from, Position to) {

        // Move the piece
        if (board[to.getY()][to.getX()] != null) --piecesCounter;
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

        // Increment moves where needed
        if (board[to.getY()][to.getX()] instanceof CastlingPiece)
            ((CastlingPiece) board[to.getY()][to.getX()]).moved();

        if (board[to.getY()][to.getX()] instanceof CountingMovePiece)
            ((CountingMovePiece) board[to.getY()][to.getX()]).incrementNbMoves();

        // Change turn
        playerTurn = getOpponent();

        // Check
        boolean check;
        if (check = isCellAttacked(getOpponent(), currentPlayerKingPos())) {
            view.displayMessage("Check!");
        }

        // End conditions
        boolean stalemateByPat = false;
        boolean stalemateByMoves = false;
        boolean checkmate = false;

        // Stalemate : Pat
        if (piecesCounter < 5) {
            // No need to look for the 2 Kings
            Piece[] livingPieces = new Piece[2];

            int i = 0;
            int cell = -1;
            for (int line = 0; line < 8; ++line) {
                for (int column = 0; column < 8; ++column) {
                    if (board[line][column] != null && !(board[line][column] instanceof King)) {

                        livingPieces[i] = board[line][column];

                        ++i;

                        // Stalemate by Pat's last case requires 2 bishops on opposite color cells
                        if (livingPieces[i - 1] instanceof Bishop) {
                            if (cell < 0) {
                                cell = (line + column) % 2;
                            } else {
                                if (cell == (line + column) % 2) {
                                    ++i; //By putting i on 3, it can't be Pat
                                }
                            }
                        }
                    }

                    if (i >= 2) break;
                }
                if (i >= 2) break;
            }

            stalemateByPat = i == 0 // King vs King
                    // King vs King + 1
                    || (i == 1
                    // +1 is either Bishop or Knight
                    && (livingPieces[0] instanceof Bishop || livingPieces[0] instanceof Knight))
                    // King + 1 vs King + 1
                    || (i == 2
                    // Both +1 must be Bishops that are on opposite color cells
                    // Opposite color cells are checked in the above loops
                    && livingPieces[0] instanceof Bishop && livingPieces[1] instanceof Bishop);
        }

        // Stalemate : Moves available ?
        // There is no verification for Castling since it's an impossible move when there's a possibility of stalemate
        boolean moveFound = false;
        for (int line = 0; line < 8; ++line) {
            if (stalemateByPat) break;

            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == playerTurn) {

                    // Simulation

                    for (int toY = 0; toY < 8; ++toY) {
                        for (int toX = 0; toX < 8; ++toX) {

                            // Not on self
                            if (toY == line && toX == column) continue;

                            // Piece on destination
                            boolean enPassantFound = false;
                            if (board[toY][toX] != null) {

                                // Cannot attack ally
                                if (board[toY][toX].getColor() == playerTurn) continue;

                                // Attack on opponent cell
                                moveFound = board[line][column].canAttack(toX - column, toY - line)
                                        && !collision(new Position(column, line), new Position(toX, toY));
                            } else {

                                // Move on empty cell
                                moveFound = board[line][column].canMove(toX - column, toY - line)
                                        && !collision(new Position(column, line), new Position(toX, toY));

                                // En Passant possibility
                                if (!moveFound) moveFound = enPassantFound = toY - line == 1
                                        && toX == lastMovePos.getX()
                                        && board[line][column] instanceof Pawn
                                        && board[lastMovePos.getY()][lastMovePos.getX()] instanceof Pawn
                                        && lastMovePos.getY() == line
                                        && Math.abs(lastMovePos.getX() - column) == 1;
                            }

                            if (moveFound) {
                                // Self-check prevention
                                Piece enPassantPiece = null;
                                if (enPassantFound) {
                                    enPassantPiece = board[lastMovePos.getY()][lastMovePos.getX()];
                                    board[lastMovePos.getY()][lastMovePos.getX()] = null;
                                }

                                Piece attackedPiece = board[toY][toX];
                                board[toY][toX] = board[line][column];
                                board[line][column] = null;

                                if (board[toY][toX] instanceof King) {
                                    moveFound = !isCellAttacked(getOpponent(), new Position(toX, toY));
                                } else {
                                    moveFound = !isCellAttacked(getOpponent(), currentPlayerKingPos());
                                }

                                // Reset simulation
                                if (enPassantFound) {
                                    board[lastMovePos.getY()][lastMovePos.getX()] = enPassantPiece;
                                }

                                board[line][column] = board[toY][toX];
                                board[toY][toX] = attackedPiece;
                            }

                            if (moveFound) break;
                        }
                        if (moveFound) break;
                    }
                }
                if (moveFound) break;
            }
            if (moveFound) break;
        }
        stalemateByMoves = !moveFound;

        // Checkmate
        if (checkmate = stalemateByMoves && check) {
            view.displayMessage("Checkmate!");
        } else if (stalemateByMoves || stalemateByPat) {
            view.displayMessage("Stalemate!");
        }

        gameEnded = checkmate || stalemateByMoves || stalemateByPat;

        System.out.printf("%d pieces remaining\n", piecesCounter);
    }
}
