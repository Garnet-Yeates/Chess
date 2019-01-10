package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.wit.yeatesg.chess.main.Chess;
import edu.wit.yeatesg.chess.objects.Board;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.pathing.Path;
import edu.wit.yeatesg.chess.pathing.PathList;
import edu.wit.yeatesg.chess.pathing.Point;

public abstract class Piece
{
	public static Board board;

	protected Tile tile;
	protected Color color;

	protected int nextMoveNum = 1;

	protected ImageIcon icon;
	protected ImageIcon outlineIcon;
	
	protected Point lastLocation;

	/**
	 * Sets the class field {@link Piece#board}. This board will be used for all pieces, whether it
	 * be for removing them, moving their location, or obtaining their location
	 * @param b
	 */
	public static void setBoard(Board b)
	{
		Piece.board = b;
	}

	/**
	 * Default constructor which is used for all sub classes of Piece
	 * @param tile The {@link Tile} that this piece will exist on
	 * @param color the {@link Color} of this piece
	 */
	public Piece(Tile tile, Color color)
	{
		if (tile != null)
		{
			this.tile = tile;
			this.color = color;
			tile.setPiece(this);	
		}

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

	/**
	 * Attempts to move this piece to the given Tile. This method makes sure that the movement
	 * that this piece is attempting is a legal moves. Legal moves are defined as movements to {@link Tile}'s
	 * that exist in this piece's path list (obtained via {@link Piece#getPaths()}). It also sets certain fields
	 * during/after movement, depending on what piece is moving (for example, when a Pawn moves, it
	 * calls {@link Pawn#postMove()} after the movement occurs, which is a special method used only for Pawns)
	 * @param t the {@link Tile} to attempt to move to
	 * @return false if the movement failed
	 */
	public void attemptMove(Tile t)
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
						
						if (p.getKilledPiece() instanceof King)
						{
							board.finishGame(color);
							board.getCurrentPlayer().getSelectedPiece().getTile().setPiece(null);
							t.setPiece(board.getCurrentPlayer().getSelectedPiece());
							board.getCurrentPlayer().getSelectedPiece().setTile(t);
							board.getCurrentPlayer().deselect();
							board.repaint();
							nextMoveNum++;
							Chess.playSound("assets/Victory.wav");
							return;
						}
						
					}
				}
				
				if (preMove(t))
				{
					move(t);
					postMove(); // Actual movement just happened
					return;
				}
			}
		}

		// CONDITION can't move there...
		Chess.playSound("assets/Invalid_Move.wav");
	}
	
	public boolean preMove(Tile t)
	{	
		setLastLocation(this.getLocation());
		return true;
	}
	
	public void move(Tile t)
	{
		getTile().setPiece(null);
		setTile(t);
		nextMoveNum++;
		t.setPiece(this);
	}
	
	public void postMove()
	{
		board.getCurrentPlayer().deselect();
		board.repaint();
		board.switchTurns();
		Chess.playSound("assets/Pickup_Place.wav");
	}


	/**
	 * This method is implemented in all subclasses of {@link Piece}. These subclasses include
	 * {@link Pawn}, {@link Rook}, {@link Knight}, {@link Bishop}, {@link King}, and {@link Queen}.
	 * Every sub class of {@link Piece} has it's own paths that it can move to, and they are all
	 * dependent on many factors such as if it is their first move, what type of piece they are,
	 * if they (the king) is in check etc
	 * @return a {@link PathList} containing all of the {@link Path}'s that this piece can move to
	 */
	public abstract PathList getPaths();

	/**
	 * Obtains the ImageIcon for this piece. Depending on what piece it is, this ImageIcon will be different.
	 * This icon will also change depending on the {@link Color} of the Piece
	 * @return this piece's ImageIcon
	 */
	public ImageIcon getImageIcon()
	{
		return icon;
	}

	/**
	 * Obtains the ImageIcon for the outline of this piece. Depending on what piece it is, the
	 * outline will look different (a rook outline is in the shape of a rook)
	 * @return this piece's outline ImageIcon
	 */
	public ImageIcon getOutlineIcon()
	{
		return outlineIcon;
	}

	/**
	 * Obtains the Tile that this piece exists on
	 * @return the {@link Tile} object that this piece is bound to
	 */
	public Tile getTile()
	{
		return tile;
	}

	/**
	 * Changes the Tile that this piece exists on
	 * @param t The new {@link Tile} for this piece
	 */
	public void setTile(Tile t)
	{
		tile = t;
	}

	/**
	 * Obtains the {@link Point} location of this piece. This location is the same location of the tile
	 * that this piece exists on, so it uses that tile to obtain the location
	 * @return a {@link Point} containing the y,x value of this piece's location
	 */
	public Point getLocation()
	{
		return tile.getLocation() == null ? null : tile.getLocation();
	}

	/**
	 * Obtains the color of this piece
	 * @return a {@link Color} object representing this piece's color
	 */
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}	

	/**
	 * Determines whether or not this Piece is to the left of another piece
	 * @param other the other piece
	 * @return true if this piece is to the left of other
	 */
	public boolean isLeftOf(Piece other)
	{
		if (getLocation().x < other.getLocation().x)
		{
			return true;
		}
		else return false;
	}

	/**
	 * Determines whether or not this Piece is to the right of another piece
	 * @param other the other piece
	 * @return true if this piece is to the right of other
	 */
	public boolean isRightOf(Piece other)
	{
		if (getLocation().x > other.getLocation().x)
		{
			return true;
		}
		else return false;
	}

	/**
	 * Obtains the piece directly to the left of this piece
	 * @return null if no such piece exists
	 */
	public Piece getPieceToLeft()
	{
		Point p = getLocation().clone();
		p.x -= 1;
		return board.pieceAt(p);
	}

	/**
	 * Obtains the piece directly to the right of this piece
	 * @return null if no such piece exists
	 */
	public Piece getPieceToRight()
	{
		Point p = getLocation().clone();
		p.x += 1;
		return (board.pieceAt(p) == null) ? null : board.pieceAt(p);
	}

	/**
	 * Determines whether or not this piece has moved. It does this by checking
	 * if the {@link Piece#nextMoveNum} field is greater than one and returns true
	 * if that is the case
	 * @return true if this piece has moved
	 */
	public boolean hasMoved()
	{
		return nextMoveNum > 1;
	}
	
	public Point getLastLocation()
	{
		return lastLocation;
	}
	
	public void setLastLocation(Point loc)
	{
		lastLocation = loc;
	}
	
	
	/**
	 * Checks to see if this piece is under attack (able to be attacked by enemies)
	 * @return true if this piece is under attack
	 */
	public boolean isUnderAttack()
	{
		return board.isUnderAttack(getLocation(), getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE);
	}
	
	/**
	 * Obtains a list of all of the pieces on the board that are on the same team
	 * as this piece
	 * @return an {@link ArrayList} of the ally pieces
	 */
	public ArrayList<Piece> getAllyPieces()
	{
		ArrayList<Piece> list = new ArrayList<>();
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Point loc = new Point(y, x);
				Piece p;
				if ((p = board.pieceAt(loc)) != null && p.getColor().equals(getColor()))
				{
					list.add(p);
				}
			}
		}
		list.remove(this);
		return list;
	}
	
	/**
	 * Obtains a list of all of the pieces on the board that aren't on the same team
	 * as this piece
	 * @return an {@link ArrayList} of the enemy pieces
	 */
	public ArrayList<Piece> getEnemyPieces()
	{
		ArrayList<Piece> list = new ArrayList<>();
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Point loc = new Point(y, x);
				Piece p;
				if ((p = board.pieceAt(loc)) != null && !p.getColor().equals(getColor()))
				{
					list.add(p);
				}
			}
		}
		return list;
	}

	/**
	 * Overriding {@link Object#equals(Object)}, this method determines whether
	 * or not this Piece is considered equivalent to another object. Piece equality
	 * means that both objects are pieces and have the same location and the same tile
	 */
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
}