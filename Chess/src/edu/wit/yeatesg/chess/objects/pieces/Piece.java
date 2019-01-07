package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;

import javax.swing.ImageIcon;

import edu.wit.yeatesg.chess.main.Chess;
import edu.wit.yeatesg.chess.objects.Board;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;
import edu.wit.yeatesg.pathing.Point;

public abstract class Piece
{
	public static Board board;
	
	protected Tile tile;
	protected Color color;
	
	protected int moveNum = 1;
	
	private ImageIcon icon;
	private ImageIcon outlineIcon;
	
	public Piece(Tile tile, Color color)
	{
		this.tile = tile;
		this.color = color;
		tile.setPiece(this);
		
		outlineIcon = new ImageIcon("assets/" + getClass().getSimpleName() + "_Outline.png");
		
		if (color.equals(Color.WHITE))
		{
			icon = new ImageIcon("assets/" + getClass().getSimpleName() + "_White.png");
		}
		else
		{
			icon = new ImageIcon("assets/" + getClass().getSimpleName() + "_Black.png");
		}
	}
	
	public abstract PathList getPaths();
	
	public ImageIcon getImageIcon()
	{
		return icon;
	}
	
	public ImageIcon getOutlineIcon()
	{
		return outlineIcon;
	}
		
	public static void setBoard(Board b)
	{
		Piece.board = b;
	}
	
	public Tile getTile()
	{
		return tile;
	}
	
	public Point getLocation()
	{
		return tile.getLocation() == null ? null : tile.getLocation();
	}
	
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}	
	
	@Override
	public boolean equals(Object other)
	{
		if (super.equals(other))
		{
			if (other instanceof Piece)
			{
				Piece o = (Piece) other;
				if (o.color.equals(color) && o.getLocation().equals(getLocation()) && o.tile.equals(tile))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void setTile(Tile t)
	{
		tile = t;
	}
	
	public boolean attemptMove(Tile t)
	{
		PathList paths = getPaths();
		for (Path p : paths)
		{
			if (p.contains(t))
			{
				if (p.killsEnemy())
				{
					if (t.equals(p.getKilledPiece().getTile()))
					{
						p.getKilledPiece().setTile(null);
					}
				}
				
				Piece selectedPiece = board.getCurrentPlayer().getSelectedPiece();
				
				if (selectedPiece instanceof Pawn)
				{
					if (t.getLocation().getVerticalDistance(selectedPiece.getLocation()) > 1)
					{
						((Pawn) selectedPiece).jumpedTwiceOnFirstMove = true;
						((Pawn) selectedPiece).setLastLocation(selectedPiece.getLocation());
					}
				}
				
				// ACTUAL MOVEMENT JUST HAPPENED
				selectedPiece.getTile().setPiece(null);
				selectedPiece.setTile(t);
				t.setPiece(selectedPiece);

				if (selectedPiece instanceof Pawn)
				{
					Pawn pawn = (Pawn) selectedPiece;

					if (t.getLocation().equals(pawn.getPassantLocation()))
					{
						pawn.getPassant().getTile().setPiece(null);
						pawn.getPassant().setTile(null);
					}
					
					pawn.postMove();
					
				/*	if (pawn.isFirstMove() == true)
					{
						pawn.setMoved(true);						
					}*/
					
				}

				// CONDITION: MOVED
				moveNum++;
				board.getCurrentPlayer().deselect();
				board.repaint();
				board.switchTurns();
				Chess.playSound("assets/Pickup_Place.wav");
				
				return true;
			}
			else
			{
				// CONDITION Can't move there...........................
			}
		}
		
		Chess.playSound("assets/Invalid_Move.wav");
		return false;
	}

	public void unbindTile()
	{
		tile = null;
	}
	
	public boolean isLeftOf(Piece other)
	{
		if (getLocation().x < other.getLocation().x)
		{
			return true;
		}
		else return false;
	}
	
	public boolean isRightOf(Piece other)
	{
		if (getLocation().x > other.getLocation().x)
		{
			return true;
		}
		else return false;
	}
	
	public Piece getPieceToLeft()
	{
		Point p = getLocation().clone();
		p.x -= 1;
		return board.pieceAt(p);
	}
	
	public Piece getPieceToRight()
	{
		Point p = getLocation().clone();
		p.x += 1;
		return (board.pieceAt(p) == null) ? null : board.pieceAt(p);
	}
	
	public boolean hasMoved()
	{
		return moveNum > 1;
	}
	

}