package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	public Pawn(Board board, Color color) {
		super(board, color);
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

		return mat;
	}

}
