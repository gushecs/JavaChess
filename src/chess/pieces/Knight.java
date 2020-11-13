package chess.pieces;

import boardGame.Board;
import boardGame.Color;
import boardGame.Position;
import chess.ChessPiece;

public class Knight extends ChessPiece{
	
	public Knight(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "k";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		//above
		p.setValues(position.getRow()-2, position.getColumn()-1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		p.setColumn(position.getColumn()+1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;

		//left
		p.setValues(position.getRow()-1, position.getColumn()-2);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		p.setRow(position.getRow()+1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//down
		p.setValues(position.getRow()+2, position.getColumn()-1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		p.setColumn(position.getColumn()+1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;

		//right
		p.setValues(position.getRow()-1, position.getColumn()+2);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		p.setRow(position.getRow()+1);
		if (getBoard().positionExists(p) && canMove(p))
			mat[p.getRow()][p.getColumn()] = true;

		return mat;
	}

}
