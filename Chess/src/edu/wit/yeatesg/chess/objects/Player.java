package edu.wit.yeatesg.chess.objects;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;

import edu.wit.yeatesg.chess.objects.gui.Board;
import edu.wit.yeatesg.chess.objects.pieces.King;
import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.chess.pathing.Point;

public class Player
{
	private Piece selectedPiece;
	private Color color;	
	private King king;
	private Board board;
	
	private JLabel timerLabel;
	private int remainingSeconds;
	
	/**
	 * Constructs a new Player object
	 * @param team The color of this Player's pieces
	 */
	public Player(Color team, Board b)
	{
		board = b;
		color = team;
	}
	
	public JLabel getTimerLabel()
	{
		return timerLabel;
	}
	
	public void setTimerLabel(JLabel label)
	{
		timerLabel = label;
	}
	
	public void setRemainingSeconds(int ms)
	{
		remainingSeconds = ms;
	}
	
	public int getRemainingSeconds()
	{
		return remainingSeconds;
	}
	
	public void modifyRemainingSeconds(int num)
	{
		timerLabel.setText(Board.secondsToString(remainingSeconds));
		remainingSeconds += num;
	}
	
	/**
	 * Determines whether or not this player has a piece selected
	 * on their mouse
	 * @return true if they have a selected piece
	 */
	public boolean hasSelectedPiece()
	{
		return selectedPiece != null;
	}

	/**
	 * Obtains the piece that the player currently has selected, if
	 * any
	 * @return null if the player doesnt have a selected {@link Piece}
	 */
	public Piece getSelectedPiece()
	{
		return selectedPiece;
	}
	
	/**
	 * Sets this Player's currently selected piece to the one specified in
	 * the parameter of this method
	 * @param p The piece to be selected
	 */
	public void selectPiece(Piece p)
	{
		selectedPiece = p;
	}
	
	/**
	 * Deselects the currently selected piece for this Player (sets the {@link #selectedPiece}
	 * field to null
	 */
	public void deselect()
	{
		selectedPiece = null;
	}
	
	/**
	 * Obtains the Color of this Player's pieces
	 * @return a clone of this Player's color
	 */
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	/**
	 * Sets the reference to this Player's king piece
	 * @param k the new {@link King}
	 */
	public void setKing(King k)
	{
		king = k;
	}
	
	/**
	 * Obtains the reference to this Player's king piece
	 * @return this Player's {@link King}
	 */
	public King getKing()
	{
		return king;
	}
	
	public boolean canMove()
	{
		for (Piece allyPiece : getPieces())
		{
			if (allyPiece.hasAnySafeMove())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkStalemate()
	{
		if (!king.inCheck() && !canMove())
		{
			board.finishGame(null);
			return true;
		}
		return false;
	}
	
	public ArrayList<Piece> getPieces()
	{
		ArrayList<Piece> list = new ArrayList<>();
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Point loc = new Point(y, x);
				Piece p;
				if ((p = board.pieceAt(loc)) != null && p.getColor().equals(getColor()))
				{
					list.add(p);
				}
			}
		}
		return list;	}

	@SuppressWarnings("unchecked")
	public void checkCheckmate()
	{
		king.examineCheckmate();
	}
	
	
}
