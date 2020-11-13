package checkers;

import boardGame.Position;

public class CheckersPosition {

	public char column;
	public int row;
	
	public CheckersPosition(char column, int row) {
		if (row>8 || row<1 || column>'h' || column<'a')
			throw new CheckersException("Position out of bounds.");
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}
	public int getRow() {
		return row;
	}
	
	public Position toPosition() {
		return new Position (8-row,column-'a');
	}
	
	public static CheckersPosition fromPosition (Position position) {
		return new CheckersPosition((char)('a'+position.getColumn()),8-position.getRow());
	}
	
	@Override
	public String toString() {
		return ""+column+row;
	}
}
