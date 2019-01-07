package edu.wit.yeatesg.chess.objects;

import java.awt.Color;

import edu.wit.yeatesg.chess.objects.pieces.Piece;

public class Player
{
	private Piece selectedPiece;
	private Color color;	
	
	/**
	 * Constructs a new Player object
	 * @param team The color of this Player's pieces
	 */
	public Player(Color team)
	{
		color = team;
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
}
