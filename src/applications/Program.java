package applications;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {

		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();
		Scanner sc = new Scanner(System.in);

		Language language = new Language();
		language.setLanguage(sc);

		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured, language);
				System.out.println();
				if (!language.getPortuguese())
					System.out.print("Source: ");
				else
					System.out.print("Da casa: ");
				ChessPosition source = UI.readChessPosition(sc, language);

				boolean[][] possibleMoves = chessMatch.possibleMoves(source, language);
				UI.clearScreen();
				ChessPiece[][] piecesOnTheBoard = chessMatch.getPieces();
				UI.printBoard(piecesOnTheBoard, possibleMoves, chessMatch, language);

				System.out.println();
				if (!language.getPortuguese())
					System.out.print("Target: ");
				else
					System.out.print("Para a casa: ");
				ChessPosition target = UI.readChessPosition(sc, language);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target, language);

				if (capturedPiece != null)
					captured.add(capturedPiece);

				while (chessMatch.getPromoted() != null) {
					try {
						System.out.println();
						if (!language.getPortuguese())
							System.out.print("Enter piece for promotion (B/k/R/Q) ");
						else
							System.out.print("Escolha a peca para promocao (B/C/T/r) ");
						String type = sc.nextLine();
						chessMatch.replacePromotedPiece(type, language);
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
						sc.nextLine();
						UI.clearScreen();
						UI.printBoard(piecesOnTheBoard, possibleMoves, chessMatch, language);
					}
				}

			} catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured, language);

	}

}
