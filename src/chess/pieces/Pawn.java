package chess.pieces;

import boardGame.Board;
import boardGame.Color;
import boardGame.Position;
import chess.ChessMatch;
import chess.ChessPiece;

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		int i = 1;

		if (getColor() == Color.WHITE)
			i = -1;

		p.setValues(position.getRow() + i, position.getColumn());

		// move 1 position
		if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
			mat[p.getRow()][p.getColumn()] = true;

		// move 2 positions
		if (getBoard().positionExists(new Position(p.getRow() + i, p.getColumn()))
				&& !getBoard().thereIsAPiece(new Position(p.getRow() + i, p.getColumn()))
				&& mat[p.getRow()][p.getColumn()] == true && getMoveCount() == 0)
			mat[p.getRow() + i][p.getColumn()] = true;

		// diagonal left
		p.setValues(position.getRow() + i, position.getColumn() - 1);
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			mat[p.getRow()][p.getColumn()] = true;

		// diagonal right
		p.setValues(position.getRow() + i, position.getColumn() + 1);
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			mat[p.getRow()][p.getColumn()] = true;

		// en passant
		if ((getColor() == Color.WHITE && position.getRow() == 3)
				|| (getColor() == Color.BLACK && position.getRow() == 4)) {
			p.setValues(position.getRow(), position.getColumn());
			Position p1 = new Position(position.getRow(), position.getColumn() - 1);
			Position p2 = new Position(position.getRow(), position.getColumn() + 1);
			if (getBoard().positionExists(p1) && isThereOpponentPiece(p1)) {
				ChessPiece enPassantPieceLeft = (ChessPiece) getBoard().piece(p1);
				if (enPassantPieceLeft == chessMatch.getEnPassantVulnerable())
					mat[p1.getRow() + i][p1.getColumn()] = true;
			}
			if (getBoard().positionExists(p2) && isThereOpponentPiece(p2)) {
				ChessPiece enPassantPieceRight = (ChessPiece) getBoard().piece(p2);
				if (enPassantPieceRight == chessMatch.getEnPassantVulnerable())
					mat[p2.getRow() + i][p2.getColumn()] = true;
			}
		}

		return mat;
	}

}
