package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.objects.gui.PromotionWindow;
import edu.wit.yeatesg.chess.pathing.Path;
import edu.wit.yeatesg.chess.pathing.PathList;
import edu.wit.yeatesg.chess.pathing.Point;

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

	@SuppressWarnings("unchecked")
	@Override
	public PathList getPaths()
	{
		Point loc = getLocation().clone();
		ArrayList<Tile> tiles = new ArrayList<>();
		
		Tile front1 = board.tileAt(new Point(loc.y + direction, loc.x));
		Tile front2 = board.tileAt(new Point(loc.y + direction + direction, loc.x));
		tiles.add(front1);
		tiles.add(front2);
		
		if (front1 != null && front1.hasPiece()) // If the tile in front has a piece, it can't move to either front1 or front2 because it is blocked via front1
		{
			tiles.remove(front1);
			tiles.remove(front2);
		}
		else if (front2 != null && front2.hasPiece()) // If the tile that is 2 tiles in front has a piece, it can't move there
		{
			tiles.remove(front2);
		}
		
		if (hasMoved() || front2 == null) // If this Pawn has moved before it can't jump 2 tiles
		{
			tiles.remove(front2);
		}
		
		ArrayList<Path> pathList = new ArrayList<Path>();
		
		for (Tile t : (ArrayList<Tile>) tiles.clone())
		{
			if (t == null)
			{
				tiles.remove(t);
			}
		}
		
		if (!tiles.isEmpty())
		{
			pathList.add(new Path(tiles, this));	
		}
		
		ArrayList<Tile> diagTiles = new ArrayList<Tile>();
		diagTiles.add(board.tileAt(new Point(loc.y + direction, loc.x - 1)));
		diagTiles.add(board.tileAt(new Point(loc.y + direction, loc.x + 1)));
		
		for (Tile diagTile : diagTiles)
		{
			Path p;
			if (diagTile != null && diagTile.hasPiece() && !diagTile.getPiece().getColor().equals(color))
			{
				pathList.add(p = new Path(diagTile, this));
			}
			else if (diagTile != null && !diagTile.hasPiece())
			{
				if (passantJumpLocation != null && passantJumpLocation.equals(diagTile.getLocation()))
				{
					pathList.add(p = new Path(diagTile, this));
					p.setKillableEnemy(passant);
					p.setSpecialKillSpot(passantJumpLocation);
				}
			}
		}
		
		// Remove nulls, if any
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
	
	@Override
	public void postMove()
	{ 
		super.postMove();

		if (this.getLastLocation().getVerticalDistance(this.getLocation()) > 1)
		{
			setJumpedTwiceOnFirstMove(true);
		}
				
		/*(if (tile.getLocation().equals(getPassantLocation()))
		{
			getPassant().getTile().setPiece(null);
			getPassant().setTile(null);
		}*/
		
		if ((direction == 1 && getLocation().y == 7) || (direction == -1 && getLocation().y == 0))
		{
			new PromotionWindow(this, board);
		}
		
		if (nextMoveNum == 2) // After the first movement is done, the move number is going to be 2, which is the only spot where en passant can occur
		{
			Piece[] leftAndRight = new Piece[2];
			leftAndRight[0] = getPieceToLeft();
			leftAndRight[1] = getPieceToRight();
			for (Piece adjacentPiece : leftAndRight)
			{
				if (adjacentPiece != null)
				{
					if (adjacentPiece instanceof Pawn && !((Pawn) adjacentPiece).getColor().equals(color) && ((Pawn) adjacentPiece).jumpedTwiceOnFirstMove && nextMoveNum < 3)
					{ 
						Point passantLocation = this.getLocation().clone();
						passantLocation.y += direction*-1;
						((Pawn) adjacentPiece).passant = this;
						((Pawn) adjacentPiece).passantJumpLocation = passantLocation;
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
	
	public Piece getPassant()
	{
		return passant;
	}
	
	public Point getPassantLocation()
	{
		return passantJumpLocation;
	}
}