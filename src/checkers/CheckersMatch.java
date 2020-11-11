package checkers;

import java.util.ArrayList;
import java.util.List;

import applications.Language;
import boardGame.Board;
import boardGame.Position;
import checkers.pieces.CommonPiece;

public class CheckersMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<Position> initialPositions = new ArrayList<>();
	private List<Position[]> spreePositions = new ArrayList<>();
	private List<boolean[][]> killedPieces = new ArrayList<>();

	public int getTurn() {
		return turn;
	}

	public List<Position> getInitialPositions() {
		return initialPositions;
	}

	public List<Position[]> getSpreePositions() {
		return spreePositions;
	}

	public List<boolean[][]> getKilledPieces() {
		return killedPieces;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public CheckersMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public GenericCheckersPiece[][] getPieces() {
		GenericCheckersPiece[][] mat = new GenericCheckersPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				mat[i][j] = (GenericCheckersPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	private void placeNewPiece(char column, int row, GenericCheckersPiece piece) {
		board.placePiece(piece, new CheckersPosition(column, row).toPosition());
	}

	public boolean[][] possibleMoves(CheckersPosition sourcePosition, Language language) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position, language);
		return board.piece(position).possibleMoves();
	}

	public boolean checkMandatoryMove() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				board.piece(new Position(i, j)).possibleMoves();
				if (((GenericCheckersPiece) board.piece(i, j)).getKillingSpree()
						&& ((GenericCheckersPiece) board.piece(i, j)).getColor() == currentPlayer)
					return true;
			}
		}
		return false;
	}

	public boolean[][] mandatoryMoves() {
		int lastSpreeSize = 0;
		boolean[][] possibleMoves = new boolean[board.getRows()][board.getColumns()];
		List<Position> initialPositions = new ArrayList<>();
		List<Position[]> spreePositions = new ArrayList<>();
		List<boolean[][]> killedPieces = new ArrayList<>();
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				GenericCheckersPiece p = (GenericCheckersPiece) board.piece(i, j);
				if (p.getKillingSpree() && p.getColor() == currentPlayer && p.getSpreeSize() > lastSpreeSize) {
					possibleMoves = p.possibleMoves();
					for (int k = 1; k <= p.getSpreePositions().size(); k++) {
						initialPositions.add(p.getCheckersPosition().toPosition());
					}
					spreePositions = p.getSpreePositions();
					killedPieces = p.getKilledPieces();
					lastSpreeSize = p.getSpreeSize();
				} else if (p.getKillingSpree() && p.getColor() == currentPlayer && p.getSpreeSize() == lastSpreeSize) {
					for (int k = 0; k < board.getRows(); k++) {
						for (int l = 0; l < board.getColumns(); l++) {
							if (!possibleMoves[k][l] && p.possibleMoves()[k][l])
								possibleMoves[k][l] = true;
						}

					}
					for (int k = 1; k <= p.getSpreePositions().size(); k++) {
						initialPositions.add(p.getCheckersPosition().toPosition());
						spreePositions.add(p.getSpreePositions().get(k));
						killedPieces.add(p.getKilledPieces().get(k));
					}
				}

			}
		}
		this.initialPositions = initialPositions;
		this.spreePositions = spreePositions;
		this.killedPieces = killedPieces;
		return possibleMoves;
	}

	public void performMandatoryMoves(int moveNumber) {
		Position initialPosition = initialPositions.get(moveNumber);
		GenericCheckersPiece p = (GenericCheckersPiece) board.removePiece(initialPosition);
		board.placePiece(p, spreePositions.get(moveNumber)[spreePositions.get(moveNumber).length - 1]);
		boolean[][] killedPieces = this.killedPieces.get(moveNumber);
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				if (killedPieces[i][j])
					board.removePiece(new Position(i, j));
			}
		}
	}

	public void performCheckersMove(CheckersPosition sourcePosition, CheckersPosition targetPosition,
			Language language) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source, language);
		validateTargetPosition(source, target, language);
		GenericCheckersPiece p=(GenericCheckersPiece)board.removePiece(source);
		board.placePiece(p, target);
	}

	private void validateSourcePosition(Position position, Language language) {
		if (!board.thereIsAPiece(position))
			throw new CheckersException(language.pieceInPosition());
		if (!board.piece(position).isThereAnyPossibleMove())
			throw new CheckersException(language.noPossibleMoves());
		if (currentPlayer != ((GenericCheckersPiece) board.piece(position)).getColor())
			throw new CheckersException(language.pieceDontBelong());
	}

	private void validateTargetPosition(Position source, Position target, Language language) {
		if (!board.piece(source).possibleMove(target))
			throw new CheckersException(language.invalidPosition());
	}

	private void initialSetup() {
		for (int j = 0; j <= 7; j++) {
			for (char i = 'a'; i <= 'h'; i++) {
				if ((j == 0 || j == 2) && i % 2 != 0)
					placeNewPiece(i, 2, new CommonPiece(board, Color.BLACK));
				else if (j == 1 && i % 2 == 0)
					placeNewPiece(i, 2, new CommonPiece(board, Color.BLACK));
				else if ((j == 6 || j == 8) && i % 2 == 0)
					placeNewPiece(i, 2, new CommonPiece(board, Color.WHITE));
				else if (j == 7 && i % 2 != 0)
					placeNewPiece(i, 2, new CommonPiece(board, Color.WHITE));
			}
		}
	}

	private void nextTurn() {
		turn += 1;
		if (currentPlayer == Color.WHITE)
			currentPlayer = Color.BLACK;
		else
			currentPlayer = Color.WHITE;
	}

}
