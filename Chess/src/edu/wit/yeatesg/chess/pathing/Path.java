package edu.wit.yeatesg.chess.pathing;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Board;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.objects.pieces.Piece;

public class Path
{
	private static Board board;
	
	private ArrayList<Tile> tiles = new ArrayList<>();
	private Point specialKillSpot = null;
	private Piece killed;
	private Piece creator;
	
	public static void setBoard(Board b)
	{
		Path.board = b;
	}
	
	/**
	 * For Knights
	 * @param t
	 */
	public Path(Tile t, Piece creator)
	{
		tiles.add(t);
		this.creator = creator;
		if (t.hasPiece() && !t.getPiece().getColor().equals(creator.getColor()))
		{
			killed = t.getPiece();
		}
	}
	
	public Path(ArrayList<Tile> tiles, Piece creator)
	{
		this.creator = creator;
		this.tiles.addAll(tiles);
		for (Tile t : tiles)
		{
			if (t.hasPiece() && !t.getPiece().getColor().equals(creator.getColor()))
			{
				killed = t.getPiece();
			}
		}
	}
	
	public Path(Direction direction, Point point, Piece creator)
	{	
		Piece piece = board.pieceAt(point);
		this.creator = creator;
		Point p = point.clone();

		int Δx = 0;
		int Δy = 0;
		
		if (direction.toString().toLowerCase().contains("up"))
		{
			Δy = -1;
		}
		if (direction.toString().toLowerCase().contains("down"))
		{
			Δy = 1;
		}
		if (direction.toString().toLowerCase().contains("left"))
		{
			Δx = -1;
		}
		if (direction.toString().toLowerCase().contains("right"))
		{
			Δx = 1;
		}
		
		do
		{
			p.x += Δx;
			p.y += Δy;
			
			Tile t;
			if ((t = board.tileAt(p)) != null)
			{
				tiles.add(t);
				
				if (t.hasPiece() && !t.getPiece().getColor().equals(piece.getColor()))
				{
					killed = t.getPiece();
					break;
				}
				else if (t.hasPiece())
				{
					tiles.remove(t);
					break;
				}
			}
			else
			{
				break;
			}
		}
		while (true);
	}
	
	public boolean intercepts(Path other)
	{
		for (Tile t : other.getTiles())
		{
			if (this.contains(t))
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}
	
	public void setKillableEnemy(Piece killed)
	{
		this.killed = killed;
	}
	
	public boolean killsEnemy()
	{
		return killed != null;
	}
	
	public Piece getKilledPiece()
	{
		return killed;
	}
	
	public void setSpecialKillSpot(Point p)
	{
		this.specialKillSpot = p;
	}
	
	public boolean hasSpecialKillSpot()
	{
		return specialKillSpot != null;
	}
	
	public Point getSpecialKillSpot()
	{
		return specialKillSpot;
	}
	
	public boolean contains(Tile t)
	{
		return tiles.contains(t);
	}
	
	public Piece getCreator()
	{
		return creator;
	}
}