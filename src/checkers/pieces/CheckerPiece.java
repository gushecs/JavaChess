package checkers.pieces;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Color;
import boardGame.Position;
import checkers.GenericCheckersPiece;

public class CheckerPiece extends GenericCheckersPiece {

	private List<boolean[][]> killingSpreeList = new ArrayList<>();
	private List<boolean[][]> killedPiecesList = new ArrayList<>();
	private List<Position[]> finalPosition = new ArrayList<>();

	public CheckerPiece(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "C";
	}

	private void checkKills(Position position) {

		int r = 1;
		int c = 1;
		Position p = new Position(position.getRow(), position.getColumn());
		Position enemyPos = new Position(0, 0);
		boolean foundEnemyPiece = false;
		boolean impossibleMove = false;
		for (int i = 1; i < 5; i++) {
			if (i == 2) {
				c = -1;
			} else if (i == 3) {
				r = -1;
			} else if (i == 4) {
				c = 1;
			}
			p.setValues(position.getRow(), position.getColumn());
			impossibleMove = false;
			while (!impossibleMove) {
				p.setValues(p.getRow() + r, p.getColumn() + c);

				if ((!getBoard().thereIsAPiece(p) && foundEnemyPiece)
						|| (!getBoard().thereIsAPiece(p) && !isThereOpponentPiece(p))
						|| !getBoard().positionExists(p)) {
					foundEnemyPiece = false;
					impossibleMove = true;
				}

				if (isThereOpponentPiece(p) && !foundEnemyPiece) {
					foundEnemyPiece = true;
					enemyPos = p;
				}

				if (!getBoard().thereIsAPiece(p) && foundEnemyPiece) {
					killingSpree = true;
					boolean[][] killingSpreeMat = new boolean[getBoard().getRows()][getBoard().getColumns()];
					boolean[][] killedPieces = new boolean[getBoard().getRows()][getBoard().getColumns()];
					killingSpreeMat[p.getRow()][p.getColumn()] = true;
					killedPieces[enemyPos.getRow()][enemyPos.getColumn()] = true;
					List<Position> finalPositionArray = new ArrayList<>();
					Position pos = p;
					finalPositionArray.add(pos);
					checkKillingSpree(killedPieces, p, killingSpreeMat, finalPositionArray);
				}

			}

		}

	}

	@SuppressWarnings("null")
	private void checkKillingSpree(boolean[][] killedPieces, Position position, boolean[][] killingSpreeMat,
			List<Position> finalPositionArray) {
		boolean[][] preservedkillingSpreeMat = killingSpreeMat;
		boolean[][] preservedkilledPieces = killedPieces;
		List<Position> preservedfinalPositionArray = finalPositionArray;
		int r = 1;
		int c = 1;
		Position p = new Position(position.getRow(), position.getColumn());
		Position enemyPos = new Position(0, 0);
		boolean foundEnemyPiece = false;
		boolean impossibleMove = false;
		for (int i = 1; i < 5; i++) {
			killingSpreeMat = preservedkillingSpreeMat;
			killedPieces = preservedkilledPieces;
			finalPositionArray = preservedfinalPositionArray;
			if (i == 2) {
				c = -1;
			} else if (i == 3) {
				r = -1;
			} else if (i == 4) {
				c = 1;
			}

			p.setValues(position.getRow(), position.getColumn());
			impossibleMove = false;
			while (!impossibleMove) {
				p.setValues(p.getRow() + r, p.getColumn() + c);

				if ((!getBoard().thereIsAPiece(p) && foundEnemyPiece)
						|| (!getBoard().thereIsAPiece(p) && !isThereOpponentPiece(p))
						|| !getBoard().positionExists(p)) {

					boolean newKillingSpree = false;
					if (killingSpreeList != null) {
						for (int j = 0; j < killingSpreeList.size(); j++) {
							if (killingSpreeList.get(j) != killingSpreeMat)
								newKillingSpree = true;
						}
						if (newKillingSpree) {
							killingSpreeList.add(killingSpreeMat);
							Position[] posArray = new Position[finalPositionArray.size()];
							for (int j = 0; j < finalPositionArray.size(); j++) {
								posArray[j] = finalPositionArray.get(j);
							}
							finalPosition.add(posArray);
							killedPiecesList.add(killedPieces);
						}
					} else {
						killingSpreeList.add(killingSpreeMat);
						Position[] posArray = new Position[finalPositionArray.size()];
						for (int j = 0; j < finalPositionArray.size(); j++) {
							posArray[j] = finalPositionArray.get(j);
						}
						finalPosition.add(posArray);
						killedPiecesList.add(killedPieces);
					}

					foundEnemyPiece = false;
					impossibleMove = true;
				}

				if (isThereOpponentPiece(p) && !foundEnemyPiece && !killedPieces[p.getRow()][p.getColumn()]) {
					foundEnemyPiece = true;
					enemyPos = p;
				}

				if (!getBoard().thereIsAPiece(p) && foundEnemyPiece && !killedPieces[p.getRow()][p.getColumn()]) {
					killingSpreeMat[p.getRow()][p.getColumn()] = true;
					killedPieces[enemyPos.getRow()][enemyPos.getColumn()] = true;
					Position pos = p;
					finalPositionArray.add(pos);
					checkKillingSpree(killedPieces, p, killingSpreeMat, finalPositionArray);
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
				movePattern = -1;
			else
				movePattern = 1;

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
			for (int i = 1; i <= killingSpreeList.size(); i++) {
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
