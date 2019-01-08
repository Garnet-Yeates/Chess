package edu.wit.yeatesg.pathing;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.pieces.Piece;

public class PathList extends ArrayList<Path>
{	
	private static final long serialVersionUID = 2239985954882086453L;

	private Piece piece;
	
	public PathList(Direction[] directions, Point startPoint, Piece creator)
	{
		for (Direction d : directions)
		{
			add(new Path(d, startPoint, piece));
		}
	}
	
	public PathList(ArrayList<Path> list)
	{
		addAll(list);
	}
	
}
