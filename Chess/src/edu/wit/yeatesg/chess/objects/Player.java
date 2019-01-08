package edu.wit.yeatesg.chess.objects;

import java.awt.Color;
import java.util.ArrayList;

import edu.wit.yeatesg.chess.objects.pieces.King;
import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;

public class Player
{
	private Piece selectedPiece;
	private Color color;	
	private King king;
	private Board board;
	
	/**
	 * Constructs a new Player object
	 * @param team The color of this Player's pieces
	 */
	public Player(Color team, Board b)
	{
		board = b;
		color = team;
	}
	
	/**
	 * Determines whether or not this player has a piece selected
	 * on their mouse
	 * @return true if they have a selected piece
	 */
	public boolean hasSelectedPiece()
	{
		return selectedPiece != null;
	}

	/**
	 * Obtains the piece that the player currently has selected, if
	 * any
	 * @return null if the player doesnt have a selected {@link Piece}
	 */
	public Piece getSelectedPiece()
	{
		return selectedPiece;
	}
	
	/**
	 * Sets this Player's currently selected piece to the one specified in
	 * the parameter of this method
	 * @param p The piece to be selected
	 */
	public void selectPiece(Piece p)
	{
		selectedPiece = p;
	}
	
	/**
	 * Deselects the currently selected piece for this Player (sets the {@link #selectedPiece}
	 * field to null
	 */
	public void deselect()
	{
		selectedPiece = null;
	}
	
	/**
	 * Obtains the Color of this Player's pieces
	 * @return a clone of this Player's color
	 */
	public Color getColor()
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	/**
	 * Sets the reference to this Player's king piece
	 * @param k the new {@link King}
	 */
	public void setKing(King k)
	{
		king = k;
	}
	
	/**
	 * Obtains the reference to this Player's king piece
	 * @return this Player's {@link King}
	 */
	public King getKing()
	{
		return king;
	}

	@SuppressWarnings("unchecked")
	public void checkCheckmate()
	{
		if (king.inCheck())
		{
			boolean checkMate = false;
			
			PathList kingPaths = king.getPaths();
			
			ArrayList<Piece> piecesThatCanKillKing = new ArrayList<>();
			
			for (Piece enemyPiece : king.getEnemyPieces())
			{
				for (Path enemyPath : enemyPiece.getPaths())
				{
					if (enemyPath.intercepts(new Path(king.getTile(), king)))
					{
						piecesThatCanKillKing.add(enemyPiece);
					}
				}
			}
			
			for (Piece enemyPiece : (ArrayList<Piece>) piecesThatCanKillKing.clone())
			{
				if (enemyPiece.isUnderAttack())
				{
					piecesThatCanKillKing.remove(enemyPiece);
				}
			}
			
			if (!piecesThatCanKillKing.isEmpty())
			{
				checkMate = true;
			}
			
			// Possibility of King moving himself out of check handeled below
			
			if (checkMate == true)
			{	
				ArrayList<Path> vulnerableKingPaths = new ArrayList<>();			
				
				for (Piece enemyPiece : king.getEnemyPieces())
				{
					for (Path enemyPath : enemyPiece.getPaths())
					{
						for (Path kingPath : kingPaths)
						{
							if (enemyPath.intercepts(kingPath))
							{
								vulnerableKingPaths.add(enemyPath);
							}
						}
					}
				}
				
				for (Path vulnerablePath : (ArrayList<Path>) vulnerableKingPaths.clone())
				{
					for (Piece allyPiece : king.getAllyPieces())
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
				
				if (!vulnerableKingPaths.isEmpty()) 
				{
					String team = color.equals(Color.WHITE) ? "WHITE" : "BLACK";
					System.out.println("FUCKING CHECKMATE FOR THIS DUMB CUNT ON TEAM " + team);
				}
			}
		}
	}
}
