package applications;

import java.util.Scanner;

import chess.ChessPiece;
import chess.Color;

public class Language {

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

	private boolean portuguese;

	public boolean getPortuguese() {
		return portuguese;
	}

	public void setLanguage(Scanner sc) {
		boolean loop = true;
		while (loop) {
			UI.clearScreen();
			System.out.println();
			System.out.println("     Digite (P) para jogar em " + ANSI_GREEN_BACKGROUND + " " + ANSI_YELLOW_BACKGROUND
					+ " " + ANSI_BLUE_BACKGROUND + " " + ANSI_RESET + " Pt-Br " + ANSI_BLUE_BACKGROUND + " "
					+ ANSI_YELLOW_BACKGROUND + " " + ANSI_GREEN_BACKGROUND + " " + ANSI_RESET);

			System.out.println("     Type (E) to play in " + ANSI_BLUE_BACKGROUND + " " + ANSI_WHITE_BACKGROUND + " "
					+ ANSI_RED_BACKGROUND + " " + ANSI_RESET + " English " + ANSI_RED_BACKGROUND + " "
					+ ANSI_WHITE_BACKGROUND + " " + ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);

			System.out.println();
			System.out.println();
			String language = sc.nextLine();
			if (language.equals("P") || language.equals("p")) {
				portuguese = true;
				loop = false;
			} else if (language.equals("E") || language.equals("e"))
				loop = false;
			else {
				System.out.println();
				System.out.println(ANSI_RED + "Invalid command! Please enter a valid character. (E/P)");
				System.out.println("Comando invalido! Por favor, digite um caractere valido. (E/P)" + ANSI_RESET);
				System.out.println();
				sc.nextLine();
			}

		}
	}

	protected String readChessPositionError() {
		if (portuguese)
			return ANSI_RED + "Erro lendo a posicao! As posicoes validas vao de a1 ate h8." + ANSI_RESET;
		else
			return ANSI_RED + "Error reading position! Valid values go from a1 to h8." + ANSI_RESET;
	}

	public String checkException() {
		if (portuguese)
			return ANSI_RED + "Voce nao pode se colocar em xeque!" + ANSI_RESET;
		else
			return ANSI_RED + "You can't put yourself in check!" + ANSI_RESET;
	}

	public String pieceInPosition() {
		if (portuguese)
			return ANSI_RED + "Nao ha uma peca sua na posicao selecionada!" + ANSI_RESET;
		else
			return ANSI_RED + "You can't move any pieces in the selected position!" + ANSI_RESET;
	}

	public String noPossibleMoves() {
		if (portuguese)
			return ANSI_RED + "Nao existem jogadas possiveis para a peca selecionada!" + ANSI_RESET;
		else
			return ANSI_RED + "There are no possible movements for the selected piece!" + ANSI_RESET;
	}

	public String pieceDontBelong() {
		if (portuguese)
			return ANSI_RED + "Esta peca nao pertence ao jogador atual!" + ANSI_RESET;
		else
			return ANSI_RED + "The piece don't belong to the current player!" + ANSI_RESET;
	}

	public String invalidPosition() {
		if (portuguese)
			return ANSI_RED + "A posicao selecionada e invalida para esta peca!" + ANSI_RESET;
		else
			return ANSI_RED + "Selected position is invalid for the choosen piece!" + ANSI_RESET;
	}

	public String piecePtEn(ChessPiece piece) {
		if (!portuguese || piece.toString().equals("B"))
			return piece.toString();
		else {
			if (piece.toString().equals("R"))
				return "T";
			else if (piece.toString().equals("k"))
				return "C";
			else if (piece.toString().equals("K"))
				return "R";
			else if (piece.toString().equals("Q"))
				return "r";
			else
				return "P";
		}
	}

	public String pieceArrayPtEn(String pieces) {
		String piece;
		String text = " ";

		for (int i = 0; i < pieces.length(); i++) {
			piece = String.valueOf(pieces.charAt(i));
			if (!portuguese || piece.equals("B") || piece.equals("P") || piece.equals("[") || piece.equals("]")
					|| piece.equals(" ")|| piece.equals(","))
				text += String.valueOf(pieces.charAt(i));
			else {
				if (piece.equals("R"))
					text += "T";
				else if (piece.equals("k"))
					text += "C";
				else if (piece.equals("K"))
					text += "R";
				else
					text += "r";
			}
		}
		return text;
	}

	public String translatePlayer(Color currentPlayer) {
		if (!portuguese) {
			if (currentPlayer.toString().equals("WHITE"))
				return "white";
			else
				return "black";
		} else {
			if (currentPlayer.toString().equals("WHITE"))
				return "branco";
			else
				return "preto";
		}
	}
}
