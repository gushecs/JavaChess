package applications;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
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

	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured, Language language) {
		printBoard(chessMatch.getPieces(), chessMatch,language);
		System.out.println();
		printCapturedPieces(captured,language);
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
				System.out.println("Esperando o jogador " + language.translatePlayer(chessMatch.getCurrentPlayer()) + ".");
				if (chessMatch.getCheck())
					System.out.println("XEQUE!");
			} else {
				System.out.println("XEQUE-MATE!");
				System.out.println("Vencedor: " + language.translatePlayer(chessMatch.getCurrentPlayer()));
			}
		}
	}

	public static void printBoard(ChessPiece[][] pieces, ChessMatch chessMatch,Language language) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false, chessMatch, i, j,language);
			}
			System.out.println();
		}
		System.out.println("  A B C D E F G H");
	}

	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves, ChessMatch chessMatch,
			Language language) {
		castling = false;
		enPassant = false;
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j], chessMatch, i, j,language);
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

	private static void printPiece(ChessPiece piece, boolean background, ChessMatch chessMatch, int i, int j,Language language) {
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
