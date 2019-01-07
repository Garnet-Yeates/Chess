package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;
import edu.wit.yeatesg.pathing.Point;

public class Pawn extends Piece
{
	private int direction;
	private boolean jumpedTwiceOnFirstMove;
	
	private Piece passant;
	private Point passantJumpLocation;
	
	public static ArrayList<Pawn> p1_Pawns = new ArrayList<Pawn>();
	public static ArrayList<Pawn> p2_Pawns = new ArrayList<Pawn>();
	
	public Pawn(Tile tile, Color color)
	{
		super(tile, color);
		direction = color.equals(Color.WHITE) ? -1 : 1;
	}

	@Override
	public PathList getPaths()
	{
		Point loc = getLocation().clone();
		ArrayList<Tile> tiles = new ArrayList<>();
		
		Tile front1 = board.tileAt(new Point(loc.y + direction, loc.x));
		Tile front2 = board.tileAt(new Point(loc.y + direction + direction, loc.x));
		tiles.add(front1);
		tiles.add(front2);
		
		if (front1.hasPiece())
		{
			tiles.remove(front1);
			tiles.remove(front2);
		}
		else if (front2 != null && front2.hasPiece())
		{
			tiles.remove(front2);
		}
		
		if (hasMoved() || front2 == null)
		{
			tiles.remove(front2);
		}
		
		ArrayList<Path> pathList = new ArrayList<Path>();
		if (!tiles.isEmpty())
		{
			pathList.add(new Path(tiles));	
		}
		
		Tile diagLeft = board.tileAt(new Point(loc.y + direction, loc.x - 1));
		Tile diagRight = board.tileAt(new Point(loc.y + direction, loc.x + 1));
		
		if (diagLeft != null && diagLeft.hasPiece() && !diagLeft.getPiece().getColor().equals(color))
		{
			pathList.add(new Path(diagLeft));
		}
		else if (diagLeft != null && !diagLeft.hasPiece())
		{
			if (passantJumpLocation != null && passantJumpLocation.equals(diagLeft.getLocation()))
			{
				pathList.add(new Path(diagLeft));
			}
		}
		if (diagRight != null && diagRight.hasPiece() && !diagRight.getPiece().getColor().equals(color))
		{
			pathList.add(new Path(diagRight));
		}
		else if (diagRight != null && !diagRight.hasPiece())
		{
			if (passantJumpLocation != null && passantJumpLocation.equals(diagRight.getLocation()))
			{
				pathList.add(new Path(diagRight));
			}
		}
	
		for (Tile t : tiles)
		{
			if (t == null)
			{
				tiles.remove(t);
			}
		}
		
		return new PathList(pathList);
	}
	
	public boolean jumpedTwiceOnFirstMove()
	{
		return jumpedTwiceOnFirstMove;
	}
	
	public void setJumpedTwiceOnFirstMove(boolean twice)
	{
		jumpedTwiceOnFirstMove = twice;
	}
	
	public void postMove()
	{ 
		if (!hasMoved())
		{
			Piece[] leftAndRight = new Piece[2];
			leftAndRight[0] = getPieceToLeft();
			leftAndRight[1] = getPieceToRight();
			for (Piece p : leftAndRight)
			{
				if (p != null)
				{
					if (p instanceof Pawn && !((Pawn) p).getColor().equals(color) && ((Pawn) p).jumpedTwiceOnFirstMove && moveNum < 3)
					{ 
						Point passantLocation = this.getLocation().clone();
						passantLocation.y += direction*-1;
						((Pawn) p).passant = this;
						((Pawn) p).passantJumpLocation = passantLocation;
					}
				}
			}
		}
		
		if (p1_Pawns.contains(this))
		{
			for (Pawn p : p1_Pawns)
			{
				p.passant = null;
				p.passantJumpLocation = null;
			}
		}
		else if (p2_Pawns.contains(this))
		{
			for (Pawn p : p2_Pawns)
			{
				p.passant = null;
				p.passantJumpLocation = null;
			}
		}
	}
	
	private Point lastLocation;

	public Point getLastLocation()
	{
		return lastLocation;
	}
	
	public void setLastLocation(Point loc)
	{
		lastLocation = loc;
	}
	
	public Piece getPassant()
	{
		return passant;
	}
	
	public Point getPassantLocation()
	{
		return passantJumpLocation;
	}
}
