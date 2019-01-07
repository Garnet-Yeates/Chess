package edu.wit.yeatesg.pathing;
import java.util.ArrayList;

public class PathList extends ArrayList<Path>
{	
	private static final long serialVersionUID = 2239985954882086453L;

	public PathList(Direction[] directions, Point startPoint)
	{
		for (Direction d : directions)
		{
			add(new Path(d, startPoint));
		}
	}
	
	public PathList(ArrayList<Path> list)
	{
		addAll(list);
	}
	
}
