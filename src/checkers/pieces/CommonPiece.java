package checkers.pieces;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Color;
import boardGame.Position;
import checkers.GenericCheckersPiece;

public class CommonPiece extends GenericCheckersPiece {

	private List<boolean[][]> killingSpreeList = new ArrayList<>();
	private List<boolean[][]> killedPiecesList = new ArrayList<>();
	private List<Position[]> finalPosition = new ArrayList<>();

	public CommonPiece(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "O";
	}

	private void checkKills(Position position) {

		for (int i = 1; i < 5; i++) {
			Position p1 = new Position(position.getRow() + 1, position.getColumn() + 1);
			Position p2 = new Position(position.getRow() + 2, position.getColumn() + 2);
			if (i == 2) {
				p1.setRow(position.getRow() + 1);
				p1.setColumn(position.getColumn() - 1);
				p2.setRow(position.getRow() + 2);
				p2.setColumn(position.getColumn() - 2);
			} else if (i == 3) {
				p1.setRow(position.getRow() - 1);
				p1.setColumn(position.getColumn() - 1);
				p2.setRow(position.getRow() - 2);
				p2.setColumn(position.getColumn() - 2);
			} else if (i == 4) {
				p1.setRow(position.getRow() - 1);
				p1.setColumn(position.getColumn() + 1);
				p2.setRow(position.getRow() - 2);
				p2.setColumn(position.getColumn() + 2);
			}
			if (getBoard().positionExists(p1) && isThereOpponentPiece(p1) && getBoard().positionExists(p2)
					&& !getBoard().thereIsAPiece(p2)) {
				killingSpree = true;
				boolean[][] killingSpreeMat = new boolean[getBoard().getRows()][getBoard().getColumns()];
				boolean[][] killedPieces = new boolean[getBoard().getRows()][getBoard().getColumns()];
				List<Position> finalPositionArray = new ArrayList<>();
				killedPieces[p1.getRow()][p1.getColumn()] = true;
				killingSpreeMat[p2.getRow()][p2.getColumn()] = true;
				finalPositionArray.add(p2);
				checkKillingSpree(killedPieces, p2, killingSpreeMat, finalPositionArray);
			}
		}

	}

	@SuppressWarnings("null")
	private void checkKillingSpree(boolean[][] killedPieces, Position position, boolean[][] killingSpreeMat,
			List<Position> finalPositionArray) {
		boolean[][] preservedkillingSpreeMat = killingSpreeMat;
		boolean[][] preservedkilledPieces = killedPieces;
		List<Position> preservedfinalPositionArray = finalPositionArray;
		for (int i = 1; i < 5; i++) {
			Position p1 = new Position(position.getRow() + 1, position.getColumn() + 1);
			Position p2 = new Position(position.getRow() + 2, position.getColumn() + 2);
			killingSpreeMat = preservedkillingSpreeMat;
			killedPieces = preservedkilledPieces;
			finalPositionArray = preservedfinalPositionArray;
			if (i == 2) {
				p1.setRow(position.getRow() + 1);
				p1.setColumn(position.getColumn() - 1);
				p2.setRow(position.getRow() + 2);
				p2.setColumn(position.getColumn() - 2);
			} else if (i == 3) {
				p1.setRow(position.getRow() - 1);
				p1.setColumn(position.getColumn() - 1);
				p2.setRow(position.getRow() - 2);
				p2.setColumn(position.getColumn() - 2);
			} else if (i == 4) {
				p1.setRow(position.getRow() - 1);
				p1.setColumn(position.getColumn() + 1);
				p2.setRow(position.getRow() - 2);
				p2.setColumn(position.getColumn() + 2);
			}
			if (getBoard().positionExists(p1) && isThereOpponentPiece(p1) && getBoard().positionExists(p2)
					&& !getBoard().thereIsAPiece(p2) && !killedPieces[p1.getRow()][p1.getColumn()]) {
				finalPositionArray.add(p2);
				killedPieces[p1.getRow()][p1.getColumn()] = true;
				killingSpreeMat[p2.getRow()][p2.getColumn()] = true;
				checkKillingSpree(killedPieces, p2, killingSpreeMat, finalPositionArray);
			} else {
				boolean newKillingSpree = true;
				if (killingSpreeList.size() != 0) {
					boolean[] equals = new boolean[killingSpreeList.size()];
					for (int j = 0; j < killingSpreeList.size(); j++) {
						for (int k = 0; k < getBoard().getRows(); k++) {
							for (int l = 0; l < getBoard().getColumns(); l++) {
								if (killingSpreeList.get(j)[k][l] != killingSpreeMat[k][l])
									equals[j] = true;
							}
						}
						for (int k=0;k<equals.length;k++) {
							if(!equals[k])
								newKillingSpree=false;
						}
					}
					if (newKillingSpree) {
						boolean[][] addToListSpree = new boolean[getBoard().getRows()][getBoard().getColumns()];
						boolean[][] addToListKilled = new boolean[getBoard().getRows()][getBoard().getColumns()];
						for (int k = 0; k < getBoard().getRows(); k++) {
							for (int j = 0; j < getBoard().getColumns(); j++) {
								if (!addToListSpree[k][j] && killingSpreeMat[k][j])
									addToListSpree[k][j] = true;
								if (!addToListKilled[k][j] && killedPieces[k][j])
									addToListKilled[k][j] = true;
							}
						}
						killingSpreeList.add(addToListSpree);
						Position[] posArray = new Position[finalPositionArray.size()];
						for (int j = 0; j < finalPositionArray.size(); j++) {
							posArray[j] = new Position(finalPositionArray.get(j).getRow(),
									finalPositionArray.get(j).getColumn());
						}
						finalPosition.add(posArray);
						killedPiecesList.add(addToListKilled);
					}
				} else {
					boolean[][] addToListSpree = new boolean[getBoard().getRows()][getBoard().getColumns()];
					boolean[][] addToListKilled = new boolean[getBoard().getRows()][getBoard().getColumns()];
					for (int k = 0; k < getBoard().getRows(); k++) {
						for (int j = 0; j < getBoard().getColumns(); j++) {
							if (!addToListSpree[k][j] && killingSpreeMat[k][j])
								addToListSpree[k][j] = true;
							if (!addToListKilled[k][j] && killedPieces[k][j])
								addToListKilled[k][j] = true;
						}
					}
					killingSpreeList.add(addToListSpree);
					Position[] posArray = new Position[finalPositionArray.size()];
					for (int j = 0; j < finalPositionArray.size(); j++) {
						posArray[j] = new Position(finalPositionArray.get(j).getRow(),
								finalPositionArray.get(j).getColumn());
					}
					finalPosition.add(posArray);
					killedPiecesList.add(addToListKilled);
				}
			}
		}
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		killingSpreeList = new ArrayList<>();
		killedPiecesList = new ArrayList<>();
		finalPosition = new ArrayList<>();
		killingSpree = false;

		checkKills(position);

		if (!killingSpree) {
			int movePattern;
			Position p = new Position(0, 0);

			if (getColor() == Color.BLACK)
				movePattern = 1;
			else
				movePattern = -1;

			// north-east
			p.setValues(position.getRow() + movePattern, position.getColumn() + movePattern);
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

			// north-west
			p.setValues(position.getRow() + movePattern, position.getColumn() - movePattern);
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

		} else {
			int spreeSize;
			int lastSpreeSize = 0;
			boolean[][] currentSpree = new boolean[getBoard().getRows()][getBoard().getColumns()];
			for (int i = 0; i < killingSpreeList.size(); i++) {
				spreeSize = 0;
				currentSpree = killingSpreeList.get(i);
				for (int j = 0; j < getBoard().getRows(); j++) {
					for (int k = 0; k < getBoard().getColumns(); k++) {
						if (currentSpree[j][k])
							spreeSize++;
					}
				}
				if (spreeSize > lastSpreeSize) {
					mat = currentSpree;
					lastSpreeSize = spreeSize;
					this.spreeSize = spreeSize;
					List<Position[]> tempFinalPosition = new ArrayList<>();
					tempFinalPosition.add(finalPosition.get(i));
					spreePositions = tempFinalPosition;
					List<boolean[][]> tempKilledPieces = new ArrayList<>();
					tempKilledPieces.add(killedPiecesList.get(i));
					killedPieces = tempKilledPieces;
				} else if (spreeSize == lastSpreeSize) {
					for (int j = 0; j < getBoard().getRows(); j++) {
						for (int k = 0; k < getBoard().getColumns(); k++) {
							if (!mat[j][k] && currentSpree[j][k])
								mat[j][k] = true;
						}
					}
					spreePositions.add(finalPosition.get(i));
					killedPieces.add(killedPiecesList.get(i));
				}
			}
		}

		return mat;
	}

}
