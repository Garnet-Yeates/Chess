package edu.wit.yeatesg.chess.pathing;
public enum Direction
{
	UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
	
	public static Direction[] getDiagonals()
	{
		return new Direction[] { UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
	}
	
	public static Direction[] getStraights()
	{
		return new Direction[] { UP, DOWN, LEFT, RIGHT };
	}
	
	public static Direction[] getDiagonalsAndStraights()
	{
		return values();
	}
	

}