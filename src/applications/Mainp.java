package applications;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import boardGame.Color;
import checkers.CheckersException;
import checkers.CheckersMatch;
import checkers.CheckersPosition;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Mainp {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Language language = new Language();
		language.setLanguage(sc);
		boolean game = UI.chooseGame(sc, language);

		if (game) {
			ChessMatch chessMatch = new ChessMatch();
			List<ChessPiece> captured = new ArrayList<>();

			while (!chessMatch.getCheckMate()) {
				try {
					UI.clearScreen();
					UI.printChessMatch(chessMatch, captured, language);
					System.out.println();
					if (!language.getPortuguese())
						System.out.print("Source: ");
					else
						System.out.print("Da casa: ");
					ChessPosition source = UI.readChessPosition(sc, language);

					boolean[][] possibleMoves = chessMatch.possibleMoves(source, language);
					UI.clearScreen();
					ChessPiece[][] piecesOnTheBoard = chessMatch.getPieces();
					UI.printChessBoard(piecesOnTheBoard, possibleMoves, chessMatch, language);

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
							UI.printChessBoard(piecesOnTheBoard, possibleMoves, chessMatch, language);
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
			UI.printChessMatch(chessMatch, captured, language);

		} else {
			CheckersMatch checkersMatch = new CheckersMatch();
			boolean winner = false;
			while (!winner) {
				try {
					UI.clearScreen();
					UI.printCheckersMatch(checkersMatch, language);
					System.out.println();
					if (checkersMatch.checkMandatoryMove()) {
						boolean[][] possibleMoves = checkersMatch.mandatoryMoves();
						UI.clearScreen();
						UI.printCheckersBoard(checkersMatch.getPieces(), possibleMoves, checkersMatch, language);
						if (!language.getPortuguese())
							System.out.print("Move number: ");
						else
							System.out.print("Numero da jogada: ");
						int moveNumber = sc.nextInt();
						checkersMatch.performMandatoryMoves(moveNumber, language);
					} else {
						if (!language.getPortuguese())
							System.out.print("Source: ");
						else
							System.out.print("Da casa: ");
						CheckersPosition source = UI.readCheckersPosition(sc, language);
						boolean[][] possibleMoves = checkersMatch.possibleMoves(source, language);
						UI.clearScreen();
						UI.printCheckersBoard(checkersMatch.getPieces(), possibleMoves, checkersMatch, language);
						System.out.println();
						if (!language.getPortuguese())
							System.out.print("Target: ");
						else
							System.out.print("Para a casa: ");
						CheckersPosition target = UI.readCheckersPosition(sc, language);
						checkersMatch.performCheckersMove(source, target, language);
					}

					checkersMatch.checkVictory();
					if (checkersMatch.getWinner() == Color.BLACK || checkersMatch.getWinner() == Color.WHITE)
						winner = true;

					checkersMatch.promote();

				} catch (CheckersException e) {
					System.out.println(e.getMessage());
					sc.nextLine();
				} catch (InputMismatchException e) {
					System.out.println(e.getMessage());
					sc.nextLine();
				}
			}
			UI.clearScreen();
			UI.printCheckersMatch(checkersMatch, language);
		}
	}

}
