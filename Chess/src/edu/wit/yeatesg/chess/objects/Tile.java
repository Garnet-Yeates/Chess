package edu.wit.yeatesg.chess.objects;
import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.pathing.Point;

public class Tile
{
	private Piece piece;
	private Board board;
	private Point location;
	private Color color;
	
	public Tile(Color color, Point p, Board b)
	{
		this.location = p;
		this.color = color;
		this.board = b;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public boolean hasPiece()
	{
		return piece != null;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}	
	
	public void setPiece(Piece p)
	{
		piece = p;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Tile)
		{
			Tile o = (Tile) other;
			if (o.location.equals(location))
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Tile> getAdjacentTiles()
	{
		ArrayList<Tile> rawList = new ArrayList<>();
		Point loc = getLocation().clone();
		rawList.add(board.tileAt(new Point(loc.y + 1, loc.x)));
		rawList.add(board.tileAt(new Point(loc.y - 1, loc.x)));
		rawList.add(board.tileAt(new Point(loc.y, loc.x + 1)));
		rawList.add(board.tileAt(new Point(loc.y, loc.x - 1)));
		rawList.add(board.tileAt(new Point(loc.y + 1, loc.x + 1)));
		rawList.add(board.tileAt(new Point(loc.y - 1, loc.x - 1)));
		rawList.add(board.tileAt(new Point(loc.y + 1, loc.x - 1)));
		rawList.add(board.tileAt(new Point(loc.y - 1, loc.x + 1)));
		ArrayList<Tile> refinedList = new ArrayList<>();
		for (Tile t : rawList)
		{
			if (t != null)
			{
				refinedList.add(t);
			}
		}	
		return refinedList;
	}
}
