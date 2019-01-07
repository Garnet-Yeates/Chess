package edu.wit.yeatesg.chess.objects;

import java.awt.Color;

import edu.wit.yeatesg.chess.objects.pieces.Piece;

public class Player
{
	private Piece selectedPiece;
	private Color color;	
	
	public Player(Color team)
	{
		color = team;
	}
	
	public boolean hasSelectedPiece()
	{
		return selectedPiece != null;
	}

	public Piece getSelectedPiece()
	{
		return selectedPiece;
	}
	
	public void selectPiece(Piece p)
	{
		selectedPiece = p;
	}
	
	public void deselect()
	{
		selectedPiece = null;
	}
	
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public boolean move()
	{
		return false;
	}
}
