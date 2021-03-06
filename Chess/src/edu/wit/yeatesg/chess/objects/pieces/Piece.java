package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.wit.yeatesg.chess.objects.Player;
import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.objects.gui.Board;
import edu.wit.yeatesg.chess.objects.gui.Chess;
import edu.wit.yeatesg.chess.pathing.Path;
import edu.wit.yeatesg.chess.pathing.PathList;
import edu.wit.yeatesg.chess.pathing.Point;

public abstract class Piece
{
	public static Board board;

	protected Tile tile;
	protected Color color;

	protected int nextMoveNum;

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
		this.nextMoveNum = 1;
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
	 * Obtains the color of this piece
	 * @return a {@link Color} object representing this piece's color
	 */
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}	

	/**
	 * Obtains the color of the opponent's pieces
	 * @return a {@link Color} object representing the opponent's color
	 */
	public Color getEnemyColor()
	{
		return color == Color.WHITE ? Color.BLACK : Color.WHITE;
	}

	/**
	 * Returns the Player who owns this Piece
	 * @return the {@link Player} who owns this Piece
	 */
	public Player getOwner()
	{
		return board.getPlayer(color);
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
		boolean kills = false;
		for (Path p : paths)
		{
			if (p.contains(t))
			{
				if (p.killsEnemy())
				{	
					if (t.equals(p.getKilledPiece().getTile()) || p.hasSpecialKillSpot() && t.getLocation().equals(p.getSpecialKillSpot()))
					{	
						kills = true;
					}
				}

				if (preMove(t))
				{
					if (kills)
					{
						p.getKilledPiece().getTile().setPiece(null);
						p.getKilledPiece().setTile(null);
					}
					move(t);
					postMove(); // Actual movement just happened
					return;
				}
			}
		}

		// CONDITION can't move there...
	}

	/**
	 * Checks this {@link Piece}'s movement to the given {@link Tile}. If the ally king will be in
	 * be in checkmate as a result of this movement, an ArrayList of enemy {@link Path}'s that
	 * are capable of killing the {@link King} after the movement will be returned. Note that the
	 * movement never actually happens, this is merely to check if the king would die had the movement
	 * occured.
	 * @param t The tile that this "movement" is going to
	 * @return null if this is a safe movement
	 */
	public ArrayList<Path> getAttackingPathsForMove(Tile t)
	{
		ArrayList<Path> attacking = null;

		if (!t.hasPiece() || (t.hasPiece() && t.getPiece().getColor().equals(getEnemyColor())))
		{
			Tile lastTile = board.tileAt(getLocation());
			lastTile.setPiece(null);

			Piece removedPiece = t.getPiece();
			if (removedPiece != null)
			{
				removedPiece.setTile(null);
			}

			t.setPiece(this);
			setTile(t);		

			King king = getOwner().getKing();
			if (king.isUnderAttack())
			{
				attacking = board.getAttackingPaths(king.getLocation(), king.getEnemyColor());
			}

			lastTile.setPiece(this);
			setTile(lastTile);
			t.setPiece(removedPiece);
			if (removedPiece != null)
			{
				removedPiece.setTile(t);	
			}	
		}

		return attacking;
	}

	/**
	 * Checks if the movement of this piece to the given tile will cause this piece's King to be
	 * in check.
	 * @param t the tile involved with the movement that is being checked
	 * @return true if this movement will not cause this piece's {@link King} to be in check
	 * @see Piece#getAttackingPathsForMove(Tile)
	 */
	public boolean isMoveSafe(Tile t)
	{
		return getAttackingPathsForMove(t) == null;
	}

	/**
	 * Determines whether this piece is able to move at all (a piece can't move if it's movement will
	 * put the King into checkmate). Return true if any of this piece's movements will not cause the king
	 * to be in check
	 * @return true if there is a safe move
	 */
	public boolean hasAnySafeMove()
	{
		for (Path p : getPaths())
		{
			for (Tile t : p.getTiles())
			{
				if (isMoveSafe(t)) 
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method is called directly before {@link #move(Tile)}. It is used for doing last second
	 * checks before a piece moves that aren't otherwise handeled through paths and {@link #attemptMove(Tile)}
	 * (i.e determining whether or not the move will cause the king to be in check, and canceling movement if
	 * this is the case). This method also does things such as setting the {@link #lastLocation} field for pieces
	 * before they move. 
	 * <b><br> Important: If you override this method, ALWAYS call super.preMove(t) in the override method</b>
	 * @param 
	 * @return
	 */
	public boolean preMove(Tile t)
	{	
		setLastLocation(this.getLocation());

		ArrayList<Path> attackingPaths = getAttackingPathsForMove(t);

		boolean safeMove = attackingPaths == null;

		if (safeMove)
		{
			return true;
		}
		else
		{
			Chess.playSound("assets/Invalid_Move.wav");
			board.console.log("Invalid Move (you cannot put yourself into checkmate)");
			board.getCurrentPlayer().deselect();
			if (this instanceof King)
			{
				t.setBlinkPiece(getOwner().getKing());
				t.blinkHighlight(Color.RED);
			}
			else
			{
				getOwner().getKing().getTile().blinkHighlight(Color.RED);
			}

			for (Path p : attackingPaths)
			{
				p.getCreator().getTile().blinkHighlight(Color.RED);
			}

			return false;
		}		
	}

	/**
	 * Moves this Piece to the given Tile
	 * @param t the {@link Tile} to move to
	 */
	public final void move(Tile t)
	{
		getTile().setPiece(null);
		setTile(t);
		nextMoveNum++;
		t.setPiece(this);
	}

	/**
	 * Called directly after {@link #move(Tile)}, this method is mainly used for switching turns,
	 * repainting and deselecting. However, this method is overriden in some sub classes, such as
	 * {@link Pawn}, to have extra functionality (such as the en passant rule)
	 * <b><br> Important: If you override this method, ALWAYS call super.postMove(t) in the override method</b>
	 */
	public void postMove()
	{
		board.getCurrentPlayer().deselect();
		board.repaint();
		board.switchTurns();
		Chess.playSound("assets/Pickup_Place.wav");
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
	 * Gets the Location that this piece existed at before it moved to its current tile
	 * @return a {@link Point} representing this Piece's last location
	 */
	public Point getLastLocation()
	{
		return lastLocation;
	}

	/**
	 * Sets this Piece's last location to the given location
	 * @param loc the new {@link Point}s that this piece used to exist at
	 */
	public void setLastLocation(Point loc)
	{
		lastLocation = loc;
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