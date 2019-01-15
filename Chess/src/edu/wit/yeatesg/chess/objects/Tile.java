package edu.wit.yeatesg.chess.objects;
import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.gui.Board;
import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.chess.pathing.Point;

public class Tile
{
	private Piece piece;
	private Board board;
	private Point location;
	private Color color;
	
	private Color[] blinkArray;
	private Color blinkColor = null;
	private Piece blinkPiece = null;
	
	private static ArrayList<Tile> tiles = new ArrayList<>();
	
	/**
	 * Constructs a new Tile object
	 * @param color The color of the tile (should be black or white)
	 * @param p The location of this tile
	 * @param b The board that this tile exists on
	 */
	public Tile(Color color, Point p, Board b)
	{
		tiles.add(this);
		this.location = p;
		this.color = color;
		this.board = b;
	}
	
	/**
	 * Obtains the {@link Board} that ths tile is on
	 * @return a reference to that board that this tile is on
	 */
	public Board getBoard()
	{
		return board;
	}
	
	/**
	 * Obtains the piece that is sitting on this tile, if any
	 * @return a reference to the piece that is on this tile, or null
	 * if there is no piece on this tile
	 */
	public Piece getPiece()
	{
		return piece;
	}
	
	/**
	 * Determines whether or not this Tile has a piece
	 * @return true if this tile has a piece, false otherwise
	 */
	public boolean hasPiece()
	{
		return piece != null;
	}
	
	/**
	 * Obtains the location of this piece
 	 * @return the (y,x) {@link Point} representation of where
 	 * this Tile is on its respective board
	 */
	public Point getLocation()
	{
		return location;
	}
	
	/**
	 * Obtains the Color of this tile. Dark tiles will always return black, even if
	 * they are painted green on the board
	 * @return a {@link Color} object representing the RGB values of this tile
	 */
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}	
	
	/**
	 * Sets this Tile's {@link Piece} to the one given in the parameter
	 * @param p The new {@link Piece} that will sit on this Tile
	 */
	public void setPiece(Piece p)
	{
		piece = p;
	}
	
	/**
	 * Obtains all 8 (or less, depending on whether it's an edge or corner tile) tiles
	 * that are adjacent to this tile
	 * @return {@link ArrayList} of Tiles that are adjacent to this tile on the same board
	 */
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
	
	/**
	 * Overriding {@link Object#equals(Object)}, this method determines whether
	 * or not this Tile is considered equivalent to another object. Tile equality
	 * means that both objects are tiles and have the same location
	 */
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
	
	private int blinkIndex = 0;
	
	public void onTick()
	{		
		if (blinkArray != null)
		{	
			if (blinkIndex < blinkArray.length)
			{
				blinkColor = blinkArray[blinkIndex];
				blinkIndex++;
			}
			else
			{
				blinkArray = null;
				blinkColor = null;
				blinkPiece = null;
				blinkIndex = 0;
			}
		}
	}
	
	public boolean hasBlinkColor()
	{
		return blinkColor != null;
	}
	
	public Color getBlinkColor()
	{
		return blinkColor;
	}
	
	public void setBlinkPiece(Piece p)
	{
		blinkPiece = p;
	}
	
	public boolean hasBlinkPiece()
	{
		return blinkPiece != null;
	}
	
	public Piece getBlinkPiece()
	{
		return blinkPiece;
	}

	public void blinkHighlight(Color highlight)
	{
		int size = 35;
		int gap = 5;
		
		double highlightWeight = 0.70;
				
		if (!hasPiece() && !hasBlinkPiece()) highlightWeight = 0.3;
		int r = (int) (highlightWeight * highlight.getRed()) + (int) ((1 - highlightWeight) * color.getRed());
		int g = (int) (highlightWeight * highlight.getGreen()) + (int) ((1 - highlightWeight) * color.getGreen());
		int b = (int) (highlightWeight * highlight.getBlue()) + (int) ((1 - highlightWeight) * color.getBlue());
		
		highlight = new Color(r, g, b);
	
		blinkArray = new Color[size];
		
		Color col = color;
		
		for (int i = 0; i < blinkArray.length; i++)
		{
			if (i % gap == 0)
			{
				col = col.equals(color) ? highlight : color;				
			}
			
			blinkArray[i] = col;	
		}
	}
	
	public static void doTicks()
	{
		for (Tile t : tiles)
		{
			t.onTick();
		}
	}

	public static boolean areTilesBlinking()
	{
		for (Tile t : tiles)
		{
			if (t.hasBlinkColor())
			{
				return true;
			}
		}
		return false;
	}
}
