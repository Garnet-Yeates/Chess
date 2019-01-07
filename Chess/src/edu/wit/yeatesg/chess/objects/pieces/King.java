package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.main.Chess;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;
import edu.wit.yeatesg.pathing.Point;

public class King extends Piece
{	
	public King(Tile tile, Color color)
	{
		super(tile, color);
	}

	@Override
	public PathList getPaths()
	{
		ArrayList<Path> pathList = new ArrayList<Path>();
		for (Tile t : getTile().getAdjacentTiles())
		{
			pathList.add(new Path(t));
		}
		return new PathList(pathList);
	}
	
	public boolean castle(Rook rook)
	{
		if (!board.arePiecesBetweenHorizontal(getLocation(), rook.getLocation()))
		{
			if (!hasMoved() && !rook.hasMoved())
			{
				if (!inCheck())
				{
					ArrayList<Tile> tilesToCheck = new ArrayList<>();
					if (rook.isLeftOf(this))
					{
						tilesToCheck.add(board.tileAt(new Point(getLocation().y, getLocation().x - 1)));
						tilesToCheck.add(board.tileAt(new Point(getLocation().y, getLocation().x - 2)));
					}
					else
					{
						tilesToCheck.add(board.tileAt(new Point(getLocation().y, getLocation().x + 1)));
						tilesToCheck.add(board.tileAt(new Point(getLocation().y, getLocation().x + 2)));
					}
					for (Tile t : tilesToCheck)
					{
						if (checkLocation(t.getLocation(), getColor()))
						{
							System.out.println("Can't castle because one of the between tiles will put the king in check");
							return false;
						}
					}
					
					// If it didn't return false yet here, then castling is possible					
					Tile originalRookTile = rook.getTile();
					Tile originalKingTile = this.getTile();
		
					if (rook.isLeftOf(this))
					{
						Point loc = new Point(getLocation().y, getLocation().x - 2);
						board.tileAt(loc).setPiece(this);
						this.setTile(board.tileAt(loc));
						
						loc = loc.clone();
						loc.x += 1;
						
						board.tileAt(loc).setPiece(rook);
						rook.setTile(board.tileAt(loc));
						System.out.println("rooks tile set to " + loc);
					}
					else
					{
						Point loc = new Point(getLocation().y, getLocation().x + 2);
						board.tileAt(loc).setPiece(this);
						this.setTile(board.tileAt(loc));
						
						loc = loc.clone();
						loc.x -= 1;
						
						board.tileAt(loc).setPiece(rook);
						rook.setTile(board.tileAt(loc));
						System.out.println("rooks tile set to " + loc);
					}
					
					originalKingTile.setPiece(null);
					originalRookTile.setPiece(null);
					
					board.getCurrentPlayer().deselect();
					board.switchTurns();
					Chess.playSound("assets/Pickup_Place.wav");
					return true;
				}
				else
				{
					System.out.println("Can't castle because king is in check");
					return false;
				}	
			}
			else
			{
				System.out.println("Can't castle because this isn't the first move of the rook/king");
				return false;
			}
		}
		else
		{
			System.out.println("Cant castle because pieces between");
			return false;
		}
	}
	
	public boolean checkLocation(Point point, Color color)
	{
		Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
		ArrayList<Piece> pieces = board.getLivingPieces();
		for (Piece p : pieces)
		{
			if (p.getColor().equals(enemyColor))
			{
				PathList paths = p.getPaths();
				for (Path path : paths)
				{
					if (path.contains(board.tileAt(point)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean inCheck()
	{
		return checkLocation(getLocation(), getColor());
	}

}
