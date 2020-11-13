package applications;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import boardGame.Color;
import checkers.CheckersMatch;
import checkers.CheckersPosition;
import checkers.GenericCheckersPiece;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.pieces.King;
import chess.pieces.Pawn;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	private static boolean castling;
	private static boolean enPassant;

	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static boolean chooseGame(Scanner sc, Language language) {
		boolean selected = false;
		while (language.getPortuguese()) {
			clearScreen();
			System.out.println();
			System.out.println();
			System.out.println("Escolha o jogo:");
			System.out.println("1-Xadrez");
			System.out.println("2-Damas");
			System.out.println();
			int jogo = sc.nextInt();
			sc.nextLine();
			if (jogo == 1)
				return true;
			if (jogo == 2)
				return false;
		}
		while (!language.getPortuguese()) {
			clearScreen();
			System.out.println();
			System.out.println();
			System.out.println("Choose the game:");
			System.out.println("1-Chess");
			System.out.println("2-Checkers");
			System.out.println();
			int jogo = sc.nextInt();
			sc.nextLine();
			if (jogo == 1)
				return true;
			if (jogo == 2)
				return false;
		}
		return selected;
	}

	public static void printChessMatch(ChessMatch chessMatch, List<ChessPiece> captured, Language language) {
		printChessBoard(chessMatch.getPieces(), chessMatch, language);
		System.out.println();
		printCapturedPieces(captured, language);
		System.out.println();
		if (!language.getPortuguese()) {
			System.out.println("Turn: " + chessMatch.getTurn());
			if (!chessMatch.getCheckMate()) {
				System.out.println("Waiting " + language.translatePlayer(chessMatch.getCurrentPlayer()) + " player.");
				if (chessMatch.getCheck())
					System.out.println("CHECK!");
			} else {
				System.out.println("CHECKMATE!");
				System.out.println("Winner: " + language.translatePlayer(chessMatch.getCurrentPlayer()));
			}
		} else {
			System.out.println("Turno: " + chessMatch.getTurn());
			if (!chessMatch.getCheckMate()) {
				System.out.println(
						"Esperando o jogador " + language.translatePlayer(chessMatch.getCurrentPlayer()) + ".");
				if (chessMatch.getCheck())
					System.out.println("XEQUE!");
			} else {
				System.out.println("XEQUE-MATE!");
				System.out.println("Vencedor: " + language.translatePlayer(chessMatch.getCurrentPlayer()));
			}
		}
	}

	public static void printCheckersMatch(CheckersMatch checkersMatch, Language language) {
		printCheckersBoard(checkersMatch.getPieces(), checkersMatch, language);
		System.out.println();
		checkersMatch.checkVictory();
		if (!language.getPortuguese()) {
			if (checkersMatch.checkWinner()) {
				System.out.println("Winner: " + language.translatePlayer(checkersMatch.getWinner()));
			} else {
				System.out.println("Turn: " + checkersMatch.getTurn());
				System.out
						.println("Waiting " + language.translatePlayer(checkersMatch.getCurrentPlayer()) + " player.");
			}
		} else {
			if (checkersMatch.checkWinner()) {
				System.out.println("Vencedor: " + language.translatePlayer(checkersMatch.getWinner()));
			} else {
				System.out.println("Turno: " + checkersMatch.getTurn());
				System.out.println(
						"Esperando o jogador " + language.translatePlayer(checkersMatch.getCurrentPlayer()) + ".");
			}
		}
	}

	public static void printChessBoard(ChessPiece[][] pieces, ChessMatch chessMatch, Language language) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printChessPiece(pieces[i][j], false, chessMatch, i, j, language);
			}
			System.out.println();
		}
		System.out.println("  A B C D E F G H");
	}

	public static void printCheckersBoard(GenericCheckersPiece[][] pieces, CheckersMatch checkersMatch,
			Language language) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printCheckersPiece(pieces[i][j], false, false, checkersMatch, language);
			}
			System.out.println();
		}
		System.out.println("  A B C D E F G H");
	}

	public static void printChessBoard(ChessPiece[][] pieces, boolean[][] possibleMoves, ChessMatch chessMatch,
			Language language) {
		castling = false;
		enPassant = false;
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printChessPiece(pieces[i][j], possibleMoves[i][j], chessMatch, i, j, language);
			}
			System.out.println();
		}
		System.out.println("  A B C D E F G H");
		if (enPassant) {
			System.out.println();
			System.out.println(ANSI_GREEN_BACKGROUND + " " + ANSI_RESET + ANSI_GREEN + " En Passant." + ANSI_RESET);
		}
		if (castling) {
			System.out.println();
			if (!language.getPortuguese())
				System.out.println(ANSI_GREEN_BACKGROUND + " " + ANSI_RESET + ANSI_GREEN + " Castling." + ANSI_RESET);
			else
				System.out.println(ANSI_GREEN_BACKGROUND + " " + ANSI_RESET + ANSI_GREEN + " Roque." + ANSI_RESET);
		}
	}

	public static void printCheckersBoard(GenericCheckersPiece[][] pieces, boolean[][] possibleMoves,
			CheckersMatch checkersMatch, Language language) {
		boolean[][] finalPosition = new boolean[pieces.length][pieces.length];
		if (checkersMatch.checkMandatoryMove()) {
			for (int k = 0; k < checkersMatch.getInitialPositions().size(); k++) {
				int positions = checkersMatch.getSpreePositions().get(k).length;
				for (int i = 0; i < positions; i++) {
					if (i == positions - 1) {
						finalPosition[checkersMatch.getSpreePositions().get(k)[i]
								.getRow()][checkersMatch.getSpreePositions().get(k)[i].getColumn()] = true;
						possibleMoves[checkersMatch.getSpreePositions().get(k)[i]
								.getRow()][checkersMatch.getSpreePositions().get(k)[i].getColumn()] = false;
					}
				}
			}
		}
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printCheckersPiece(pieces[i][j], possibleMoves[i][j], finalPosition[i][j], checkersMatch, language);
			}
			System.out.println();
		}
		System.out.println("  A B C D E F G H");
		if (checkersMatch.checkMandatoryMove()) {
			System.out.println();
			System.out.println();
			if (!language.getPortuguese()) {
				System.out.println("Possible Moves:");
				for (int k = 1; k - 1 < checkersMatch.getInitialPositions().size(); k++) {
					int positions = checkersMatch.getSpreePositions().get(k - 1).length;
					System.out.print("play #" + k + ": "
							+ CheckersPosition.fromPosition(checkersMatch.getInitialPositions().get(k - 1)) + "->");
					for (int i = 0; i < positions; i++) {
						if (i == positions - 1)
							System.out.println(
									CheckersPosition.fromPosition(checkersMatch.getSpreePositions().get(k - 1)[i])
											+ ".");
						else
							System.out.print(
									CheckersPosition.fromPosition(checkersMatch.getSpreePositions().get(k - 1)[i])
											+ "->");
					}
					System.out.println();
				}
			} else {
				System.out.println("Jogadas possiveis:");
				for (int k = 1; k - 1 < checkersMatch.getInitialPositions().size(); k++) {
					int positions = checkersMatch.getSpreePositions().get(k - 1).length;
					System.out.print("Jogada #" + k + ": "
							+ CheckersPosition.fromPosition(checkersMatch.getInitialPositions().get(k - 1)) + "->");
					for (int i = 0; i < positions; i++) {
						if (i == positions - 1)
							System.out.println(
									CheckersPosition.fromPosition(checkersMatch.getSpreePositions().get(k - 1)[i])
											+ ".");
						else
							System.out.print(
									CheckersPosition.fromPosition(checkersMatch.getSpreePositions().get(k - 1)[i])
											+ "->");
					}
					System.out.println();
				}
			}
		}
	}

	private static void printChessPiece(ChessPiece piece, boolean background, ChessMatch chessMatch, int i, int j,
			Language language) {
		if (background) {
			ChessPiece movingPiece = chessMatch.getMovingPiece();
			int row = chessMatch.getMovingPieceRow();
			int column = chessMatch.getMovingPieceColumn();
			if (movingPiece instanceof Pawn && (i == row + 1 || i == row - 1) && (j == column + 1 || j == column - 1)
					&& piece == null) {
				// print en passant
				System.out.print(ANSI_GREEN_BACKGROUND);
				enPassant = true;
			} else if (movingPiece instanceof King && (j == column + 2 || j == column - 2)) {
				// print castling
				System.out.print(ANSI_GREEN_BACKGROUND);
				castling = true;
			} else
				System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (piece == null)

		{
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + language.piecePtEn(piece) + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + language.piecePtEn(piece) + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}

	private static void printCheckersPiece(GenericCheckersPiece piece, boolean background, boolean finalPosition,
			CheckersMatch checkersMatch, Language language) {
		if (background)
			System.out.print(ANSI_BLUE_BACKGROUND);
		
		if (finalPosition)
			System.out.print(ANSI_GREEN_BACKGROUND);

		if (piece == null)
			System.out.print("-" + ANSI_RESET);
		
		else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + language.piecePtEn(piece) + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + language.piecePtEn(piece) + ANSI_RESET);
			}
		}
		
		System.out.print(" ");
	}

	public static ChessPosition readChessPosition(Scanner sc, Language language) {
		try {

			String s = sc.nextLine();
			char column = s.charAt(0);
			column = convertUpperLower(column);
			int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);
		} catch (RuntimeException exc) {
			throw new InputMismatchException(language.readChessPositionError());
		}
	}

	public static CheckersPosition readCheckersPosition(Scanner sc, Language language) {
		try {

			String s = sc.nextLine();
			char column = s.charAt(0);
			column = convertUpperLower(column);
			int row = Integer.parseInt(s.substring(1));
			return new CheckersPosition(column, row);
		} catch (RuntimeException exc) {
			throw new InputMismatchException(language.readChessPositionError());
		}
	}

	private static char convertUpperLower(char column) {
		if (column == 'A')
			return 'a';
		else if (column == 'B')
			return 'b';
		else if (column == 'C')
			return 'c';
		else if (column == 'D')
			return 'd';
		else if (column == 'E')
			return 'e';
		else if (column == 'F')
			return 'f';
		else if (column == 'G')
			return 'g';
		else if (column == 'H')
			return 'h';
		else
			return column;
	}

	private static void printCapturedPieces(List<ChessPiece> captured, Language language) {
		List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE)
				.collect(Collectors.toList());
		List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK)
				.collect(Collectors.toList());
		if (!language.getPortuguese()) {
			System.out.println("Captured pieces:");
			System.out.print("White: ");
		} else {
			System.out.println("Capturados:");
			System.out.print("Brancos: ");
		}
		System.out.print(ANSI_WHITE);
		System.out.println(language.pieceArrayPtEn(Arrays.toString(white.toArray())));
		System.out.print(ANSI_RESET);
		if (!language.getPortuguese())
			System.out.print("Black: ");
		else
			System.out.print("Pretos: ");
		System.out.print(ANSI_YELLOW);
		System.out.println(language.pieceArrayPtEn(Arrays.toString(black.toArray())));
		System.out.print(ANSI_RESET);
	}
}
