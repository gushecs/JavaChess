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

		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);

				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				ChessPiece[][] piecesOnTheBoard = chessMatch.getPieces();
				UI.printBoard(piecesOnTheBoard, possibleMoves,chessMatch);

				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

				if (capturedPiece != null)
					captured.add(capturedPiece);

				while (chessMatch.getPromoted() != null) {
					try {
						System.out.println();
						System.out.print("Enter piece for promotion (B/k/R/Q) ");
						String type = sc.nextLine();
						chessMatch.replacePromotedPiece(type);
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
						sc.nextLine();
						UI.clearScreen();
						UI.printBoard(piecesOnTheBoard, possibleMoves,chessMatch);
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
		UI.printMatch(chessMatch, captured);

	}

}
