package edu.wit.yeatesg.chess.pathing;

public class Point
{
	public int x;
	public int y;
	
	public Point(int y, int x)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Point)
		{
			Point o = (Point) other;
			if (o.x == x && o.y == y)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "( " +  y + " , " + x + " )";
	}
	
	public Point clone()
	{
		return new Point(y, x);
	}

	public int getVerticalDistance(Point other)
	{
		return (Math.abs(other.y - y));
	}
	
	public int getHorizontalDistance(Point other)
	{
		return (Math.abs(other.x - x));
	}
	
	public int getDistance(Point other)
	{
		return (Math.abs(other.x - x) + Math.abs(other.y - y));
	}
}
