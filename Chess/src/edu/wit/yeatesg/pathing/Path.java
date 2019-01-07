package edu.wit.yeatesg.pathing;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Board;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.objects.pieces.Piece;

public class Path
{
	private ArrayList<Tile> tiles = new ArrayList<>();
	private Piece killed;
	
	private static Board board;
	
	public static void setBoard(Board b)
	{
		Path.board = b;
	}
	
	/**
	 * For Knights
	 * @param t
	 */
	public Path(Tile t)
	{
		tiles.add(t);
	}
	
	public Path(ArrayList<Tile> tiles)
	{
		this.tiles.addAll(tiles);
	}
	
	public Path(Direction direction, Point point)
	{	
		Piece piece = board.pieceAt(point);
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
	
	public boolean contains(Tile t)
	{
		return tiles.contains(t);
	}
}