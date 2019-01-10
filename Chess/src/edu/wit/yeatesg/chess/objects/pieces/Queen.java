package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.pathing.Direction;
import edu.wit.yeatesg.chess.pathing.PathList;

public class Queen extends Piece
{
	public Queen(Tile tile, Color color)
	{
		super(tile, color);
	}

	@Override
	public PathList getPaths()
	{
		return new PathList(Direction.getDiagonalsAndStraights(), getLocation(), this);
	}

}
