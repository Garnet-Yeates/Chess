package edu.wit.yeatesg.chess.objects.pieces;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.Tile;
import edu.wit.yeatesg.chess.objects.gui.Chess;
import edu.wit.yeatesg.chess.pathing.Path;
import edu.wit.yeatesg.chess.pathing.PathList;
import edu.wit.yeatesg.chess.pathing.Point;

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
			if (!t.hasPiece() || !t.getPiece().getColor().equals(color))
			{
				pathList.add(new Path(t, this));	
			}
		}
		return new PathList(pathList);
	}
	
	@SuppressWarnings("unchecked")
	public void examineCheckmate()
	{
		if (this.inCheck())
		{
			boolean checkMate = false;
			
			PathList thisPaths = this.getPaths();
			
			ArrayList<Piece> piecesThatCanKillKing = new ArrayList<>();
			
			for (Piece enemyPiece : this.getEnemyPieces())
			{
				for (Path enemyPath : enemyPiece.getPaths())
				{
					if (enemyPath.intercepts(new Path(this.getTile(), this)))
					{
						piecesThatCanKillKing.add(enemyPiece);
					}
				}
			}
			
			/* This block covers the possibility of the King's allies attacthis pieces that are threatening the this.
			 * However, this can only happen if it is this King's turn
			 */
			if (!board.getCurrentPlayer().getColor().equals(this.getColor()))
			{
				for (Piece enemyPiece : (ArrayList<Piece>) piecesThatCanKillKing.clone())
				{
					if (enemyPiece.isUnderAttack())
					{
						piecesThatCanKillKing.remove(enemyPiece);
					}
				}
			}
			
			if (!piecesThatCanKillKing.isEmpty())
			{
				checkMate = true;
			}
			
			/* Possibility of King moving himself out of check handeled below (again, it has to be the this's team's turn
			 * in order for this to be a possibility. Also, the possibility of an ally blocthis an enemy's path to the this
			 * is handeled below as well
			 */
			if (checkMate == true && !board.getCurrentPlayer().getColor().equals(this.getColor()))
			{									
				for (Path thisPath : (ArrayList<Path>) this.getPaths().clone())
				{
					for (Piece enemy : this.getEnemyPieces())
					{
						for (Path enemyPath : enemy.getPaths())
						{
							if (!thisPath.intercepts(enemyPath))
							{
								checkMate = false;
							}
						}
					}
				}
				
				if (checkMate)
				{
					ArrayList<Path> vulnerableKingPaths = new ArrayList<>();			
					
					for (Piece enemyPiece : this.getEnemyPieces())
					{
						for (Path enemyPath : enemyPiece.getPaths())
						{
							for (Path thisPath : thisPaths)
							{
								if (enemyPath.intercepts(thisPath))
								{
									vulnerableKingPaths.add(enemyPath);
								}
							}
						}
					}
					
					for (Path vulnerablePath : (ArrayList<Path>) vulnerableKingPaths.clone())
					{
						for (Piece allyPiece : this.getAllyPieces())
						{
							for (Path allyPath : allyPiece.getPaths())
							{
								if (allyPath.intercepts(vulnerablePath))
								{
									vulnerableKingPaths.remove(vulnerablePath);
								}
							}
						}
					}
					
					if (vulnerableKingPaths.isEmpty()) 
					{
						checkMate = false;
					}	
				}
			}
			
			if (checkMate)
			{
				String team = color.equals(Color.WHITE) ? "WHITE" : "BLACK";
				board.console.log("CHECKMATE FOR " + team + " KING");
			}
		}
	}
	
	public boolean castle(Rook rook)
	{
		if (!board.arePiecesBetweenHorizontal(getLocation(), rook.getLocation()))
		{
			if (!hasMoved() && !rook.hasMoved())
			{
				if (!inCheck())
				{
					// The two tiles that are between the Rook and the King cannot be under attack by the enemy team, so they are checked
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
						if (board.isUnderAttack(t.getLocation(), getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE))
						{
							board.console.log("Can't castle because the King has to move through check");
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
						board.console.log("rooks tile set to " + loc);
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
					board.console.log("Can't castle because the King is in check");
					return false;
				}	
			}
			else
			{
				board.console.log("Can't castle because this isn't the first move of the Rook/King");
				return false;
			}
		}
		else
		{
			board.console.log("Can't castle because there pieces between the King and Rook");
			return false;
		}
	}

	public boolean inCheck()
	{
		return isUnderAttack();
	}
}