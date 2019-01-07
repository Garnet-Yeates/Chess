package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;
import edu.wit.yeatesg.pathing.Point;

public class Knight extends Piece
{
	public Knight(Tile tile, Color color)
	{
		super(tile, color);
	}

	@Override
	public PathList getPaths()
	{
		// TODO Auto-generated method stub
		ArrayList<Path> pathList = new ArrayList<>();
		Point loc = getLocation();
		Tile t;
		
		if ((t = board.tileAt(new Point(loc.y - 2, loc.x + 1))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y - 2, loc.x - 1))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y + 2, loc.x + 1))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y + 2, loc.x - 1))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y - 1, loc.x + 2))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y + 1, loc.x + 2))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y - 1, loc.x - 2))) != null)
		{
			pathList.add(new Path(t));
		}
		if ((t = board.tileAt(new Point(loc.y + 1, loc.x - 2))) != null)
		{
			pathList.add(new Path(t));
		}
		
		for (Path p : pathList)
		{
			t = p.getTiles().get(0);
			if (t.hasPiece() && !t.getPiece().getColor().equals(board.getCurrentPlayer().getColor()))
			{
				p.setKillableEnemy(t.getPiece());
			}
		}
		
		return new PathList(pathList);
	}

}
