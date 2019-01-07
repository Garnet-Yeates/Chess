package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.pathing.Direction;
import edu.wit.yeatesg.pathing.PathList;

public class Bishop extends Piece
{
	public Bishop(Tile tile, Color color)
	{
		super(tile, color);
	}

	@Override
	public PathList getPaths()
	{
		return new PathList(Direction.getDiagonals(), getLocation());
	}

}
