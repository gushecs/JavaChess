package checkers.pieces;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Position;
import checkers.Color;
import checkers.GenericCheckersPiece;

public class CheckerPiece extends GenericCheckersPiece {

	public CheckerPiece(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "C";
	}

	private void checkKills(Position position, List<boolean[][]> killingSpreeList, List<Position[]> finalPosition) {

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
					boolean[][] piecesToIgnore = new boolean[getBoard().getRows()][getBoard().getColumns()];
					killingSpreeMat[p.getRow()][p.getColumn()] = true;
					piecesToIgnore[enemyPos.getRow()][enemyPos.getColumn()] = true;
					List<Position> finalPositionArray = new ArrayList<>();
					finalPositionArray.add(p);
					checkKillingSpree(piecesToIgnore, p, killingSpreeMat, killingSpreeList, finalPositionArray,
							finalPosition);
				}

			}

		}

	}

	@SuppressWarnings("null")
	private void checkKillingSpree(boolean[][] piecesToIgnore, Position position, boolean[][] killingSpreeMat,
			List<boolean[][]> killingSpreeList, List<Position> finalPositionArray, List<Position[]> finalPosition) {
		boolean[][] preservedkillingSpreeMat = killingSpreeMat;
		List<Position> preservedfinalPositionArray = finalPositionArray;
		int r = 1;
		int c = 1;
		Position p = new Position(position.getRow(), position.getColumn());
		Position enemyPos = new Position(0, 0);
		boolean foundEnemyPiece = false;
		boolean impossibleMove = false;
		for (int i = 1; i < 5; i++) {
			killingSpreeMat = preservedkillingSpreeMat;
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
						}
					} else {
						killingSpreeList.add(killingSpreeMat);
						Position[] posArray = new Position[finalPositionArray.size()];
						for (int j = 0; j < finalPositionArray.size(); j++) {
							posArray[j] = finalPositionArray.get(j);
						}
						finalPosition.add(posArray);
					}

					foundEnemyPiece = false;
					impossibleMove = true;
				}

				if (isThereOpponentPiece(p) && !foundEnemyPiece && !piecesToIgnore[p.getRow()][p.getColumn()]) {
					foundEnemyPiece = true;
					enemyPos = p;
				}

				if (!getBoard().thereIsAPiece(p) && foundEnemyPiece && !piecesToIgnore[p.getRow()][p.getColumn()]) {
					killingSpreeMat[p.getRow()][p.getColumn()] = true;
					piecesToIgnore[enemyPos.getRow()][enemyPos.getColumn()] = true;
					finalPositionArray.add(p);
					checkKillingSpree(piecesToIgnore, p, killingSpreeMat, killingSpreeList, finalPositionArray,
							finalPosition);
				}

			}

		}
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		List<boolean[][]> killingSpreeList = new ArrayList<>();
		List<Position[]> finalPosition = new ArrayList<>();
		killingSpree = false;

		checkKills(position, killingSpreeList, finalPosition);

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
					spreePositions = finalPosition;
				} else if (spreeSize == lastSpreeSize) {
					for (int j = 0; j < getBoard().getRows(); j++) {
						for (int k = 0; k < getBoard().getColumns(); k++) {
							if (!mat[j][k] && currentSpree[j][k])
								mat[j][k] = true;
						}
					}
					spreePositions.add(finalPosition.get(i));
				}
			}
		}

		return mat;
	}

}
