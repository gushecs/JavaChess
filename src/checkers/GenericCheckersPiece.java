package checkers;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Color;
import boardGame.Piece;
import boardGame.Position;

public abstract class GenericCheckersPiece extends Piece{

	private Color color;
	protected boolean killingSpree;
	protected int spreeSize;
	protected List<Position[]> spreePositions = new ArrayList<>();
	protected List<boolean[][]> killedPieces = new ArrayList<>();

	public boolean getKillingSpree() {
		return killingSpree;
	}

	public int getSpreeSize() {
		return spreeSize;
	}

	public List<Position[]> getSpreePositions() {
		return spreePositions;
	}
	
	public List<boolean[][]> getKilledPieces() {
		return killedPieces;
	}

	public GenericCheckersPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public CheckersPosition getCheckersPosition() {
		return CheckersPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		GenericCheckersPiece p=(GenericCheckersPiece)getBoard().piece(position);
		return p != null && p.getColor() != color;
	}
	
}
