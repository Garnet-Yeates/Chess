package edu.wit.yeatesg.chess.objects;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import edu.wit.yeatesg.chess.main.Chess;
import edu.wit.yeatesg.chess.objects.pieces.Bishop;
import edu.wit.yeatesg.chess.objects.pieces.King;
import edu.wit.yeatesg.chess.objects.pieces.Knight;
import edu.wit.yeatesg.chess.objects.pieces.Pawn;
import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.chess.objects.pieces.Queen;
import edu.wit.yeatesg.chess.objects.pieces.Rook;
import edu.wit.yeatesg.pathing.Path;
import edu.wit.yeatesg.pathing.PathList;
import edu.wit.yeatesg.pathing.Point;

public class Board extends JPanel
{
	private static final long serialVersionUID = -180155589480308568L;

	@SuppressWarnings("unused")
	private JFrame container;

	private int tileSize = 96;
	private int numTiles = 8;

	private Tile[][] board = new Tile[numTiles][numTiles];

	private Player p1 = new Player(Color.WHITE, this);
	private Player p2 = new Player(Color.BLACK, this);

	private Player currentPlayer = p1;

	public Board(JFrame container)
	{
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new java.awt.Point(0, 0), "blank cursor");
		setCursor(blankCursor);

		this.container = container;

		Path.setBoard(this);
		Piece.setBoard(this);

		initTiles();
		initPieces();

		timer.start();

		BoardMouseListener mouseListener = new BoardMouseListener();
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);		
	}
	
	/**
	 * Initializes the pieces on the board. They are set up like regular old chess pieces
	 */
	public void initPieces()
	{
		new Rook(board[0][0], Color.BLACK);
		new Rook(board[0][7], Color.BLACK);

		new Rook(board[7][0], Color.WHITE);
		new Rook(board[7][7], Color.WHITE);

		new Knight(board[0][1], Color.BLACK);
		new Knight(board[0][6], Color.BLACK);

		new Knight(board[7][1], Color.WHITE);
		new Knight(board[7][6], Color.WHITE);
		
		new Bishop(board[0][2], Color.BLACK);
		new Bishop(board[0][5], Color.BLACK);

		new Bishop(board[7][2], Color.WHITE);
		new Bishop(board[7][5], Color.WHITE);

		new Queen(board[0][3], Color.BLACK);
		
		new Queen(board[7][3], Color.WHITE);
		
		p2.setKing(new King(board[0][4], Color.BLACK));
		
		p1.setKing(new King(board[7][4], Color.WHITE));

		for (int x = 0; x < numTiles; x++)
		{
			Pawn.p2_Pawns.add(new Pawn(board[1][x], Color.BLACK));
			Pawn.p1_Pawns.add(new Pawn(board[6][x], Color.WHITE));
		}

		repaint();				
	}

	/**
	 * Initializes all of the tiles on the board, with the top left tile being white. The
	 * board is designed to have tiles of alternating colors
	 */
	public void initTiles()
	{
		boolean white = true;
		for (int y = 0; y < numTiles; y++)
		{
			for (int x = 0; x < numTiles; x++, white = ((white) ? false : true))
			{
				Point loc = new Point(y, x);
				Tile t = new Tile(((white == true) ? new Color(236, 238, 212) : new Color(116, 150, 84)), loc, this);
				board[y][x] = t;
				if (x == numTiles - 1)
				{
					white = ((white) ? false : true);
				}
			}
		}
	}
	
	private ImageIcon cursorSelectIcon = new ImageIcon("assets/Corsor.png");
	private ImageIcon whiteCursor = new ImageIcon("assets/Cursor_White.png");
	private ImageIcon blackCursor = new ImageIcon("assets/Cursor_Black.png");
	
	/**
	 * This method is called every game tick and continuously updates the graphics that are displayed
	 * on the board. The graphics that are updated include the special cursors, all of the pieces and
	 * the tiles
	 */
	@Override
	public void paint(Graphics g)
	{	
		// BLOCK: Draw Tiles
		{
			int yPos = 0;
			int xPos = 0;
			for (int y = 0; y < numTiles; y++)
			{
				xPos = 0;
				for (int x = 0; x < numTiles; x++)
				{
					Point p = new Point(y, x);
					Tile t = tileAt(p);
					g.setColor(t.getColor());
					g.fillRect(xPos, yPos, tileSize, tileSize);
					xPos += tileSize;
				}
				yPos += tileSize;
			}
		}

		// BLOCK: Draw Pieces
		{
			for (int y = 0; y < numTiles; y++)
			{
				for (int x = 0; x < numTiles; x++)
				{
					Point loc = new Point(y, x);
					Tile t = tileAt(loc);
					if (t.hasPiece())
					{
						Piece p = t.getPiece();
						int xPos = loc.x * tileSize;
						int yPos = loc.y * tileSize;

						if (currentPlayer.hasSelectedPiece())
						{
							if (currentPlayer.getSelectedPiece().getTile() != null && currentPlayer.getSelectedPiece().getTile().equals(t))
							{
								p.getOutlineIcon().paintIcon(this, g.create(), xPos, yPos);
								continue;
							}
						}

						p.getImageIcon().paintIcon(this, g.create(), xPos, yPos);
					}
				}
			}
		}

		// BLOCK: Draw Selected Piece On Mouse
		{
			if (currentPlayer.hasSelectedPiece())
			{
				Piece p = currentPlayer.getSelectedPiece();
				p.getImageIcon().paintIcon(this, g, mouseX + 25, mouseY + 25);
			} 
		}

		// BLOCK: Draw Mouse Cursor
		{
			if (currentPlayer.hasSelectedPiece())
			{
				cursorSelectIcon.paintIcon(this, g, mouseX - 30, mouseY - 30);
			}
			else
			{
				if (currentPlayer.getColor().equals(Color.WHITE))
				{
					whiteCursor.paintIcon(this, g, mouseX, mouseY);
				}
				else
				{
					blackCursor.paintIcon(this, g, mouseX, mouseY);
				}
			}
		}
	}

	/**
	 * Obtains the Tile at exists at the specified point
	 * @param p the (y,x) location to look for a tile at
	 * @return null if there is no tile here (out of bounds)
	 */
	public Tile tileAt(Point p)
	{
		try
		{
			return board[p.y][p.x];
		} 
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}

	/**
	 * Obtains the Piece that exists at the specified point, if there is any
	 * @param p the point to search for a piece at
	 * @return null if there is no Piece here
	 */
	public Piece pieceAt(Point p)
	{
		return (tileAt(p) == null) ? null : tileAt(p).getPiece();
	}

	/**
	 * Given that point a and point b have the same y value, this method
	 * will check to see if there are any pieces in between them
	 * @param a the first point
	 * @param b the second point
	 * @return true if there is any piece between these two points
	 */
	public boolean arePiecesBetweenHorizontal(Point a, Point b)
	{
		Point temp;
		if (a.x > b.x) // Swap variables so a is less than b
		{
			temp = a;
			a = b;
			b = temp;
		}
				
		for (int x = a.x + 1; x < b.x; x++)
		{
			if (pieceAt(new Point(a.y, x)) != null)
			{
				return true;
			}
		}
		
		return false;		
	}

	/**
	 * Returns all the Pieces that currently exist on this Board object
	 * @return an ArrayList containing all pieces on the board
	 */
	public ArrayList<Piece> getLivingPieces()
	{
		ArrayList<Piece> list = new ArrayList<>();
		for (int y = 0; y < numTiles; y++)
		{
			for (int x = 0; x < numTiles; x++)
			{
				Point loc = new Point(y, x);
				Tile t = tileAt(loc);
				if (t.hasPiece())
				{
					list.add(t.getPiece());
				}
			}
		}
		return list;
	}
	
	public boolean isUnderAttack(Point point, Color color)
	{
		Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
		ArrayList<Piece> pieces = getLivingPieces();
		for (Piece p : pieces)
		{
			if (p.getColor().equals(enemyColor))
			{
				PathList paths = p.getPaths();
				for (Path path : paths)
				{
					if (path.contains(tileAt(point)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Who's turn is it? Obtains the player who is currently allowed to select
	 * and move pieces of their respective color around the board
	 * @return the player whose turn it is
	 */
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	/**
	 * This method is called after each player makes a move, and hands over
	 * selecting and moving rights to the other player
	 */
	public void switchTurns()
	{
		p1.checkCheckmate();
		p2.checkCheckmate();
		if (p1.equals(currentPlayer))
		{
			currentPlayer = p2;
		}
		else
		{
			currentPlayer = p1;
		}
	}
	
	boolean frozen = false;
	
	public void freeze()
	{
		frozen = true;
	}
	
	public void unFreeze()
	{
		frozen = false;
	}
	
	private int preferredSize = numTiles * tileSize - 10;
	
	/**
	 * Overrides the {@link #getPreferredSize()} method in Component to obtain
	 * a preferred size that makes sense for this chess board
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(preferredSize, preferredSize);
	}
	
	private Timer timer = new Timer(13, this.new BoardTimer());
	
	/**
	 * The BoardTimer class is an ActionListener designed to handle repainting this component.
	 * Every time the timer ticks, the board is repainted using this class
	 * @author yeatesg
	 * 
	 */
	class BoardTimer implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			repaint();
		}
	}

	private int mouseX = 0;
	private int mouseY = 0;

	/**
	 * The BoardMouseListener class is a MouseListener designed to capture all mouse events that occur on
	 * this component. This class detects clicks and decides whether the current player is selecting a piece,
	 * de-selecting a piece, or moving a piece. By using a MouseMotionListener, it also tracks the location of
	 * their cursor so that the pieces that they have selected will appear next to the mouse.
	 * @author yeatesg
	 *
	 */
	class BoardMouseListener implements MouseListener, MouseMotionListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (!frozen)
			{
				int x = e.getX() / (tileSize);
				int y = e.getY() / (tileSize);
				Tile clickedTile = tileAt(new Point(y, x));
				if (!currentPlayer.hasSelectedPiece()) // If the current player doesn't have a selected piece, they must be trying to select
				{
					if ((clickedTile.hasPiece() && clickedTile.getPiece().getColor().equals(currentPlayer.getColor())))
					{ 
						currentPlayer.selectPiece(clickedTile.getPiece());
						Chess.playSound("assets/Pickup_Place.wav");
					}
					else // In this condition, the player tried to select one of their opponent's pieces
					{
						System.out.println("OPP");
						Chess.playSound("assets/Invalid_Click.wav");
					}
				}
				else if (currentPlayer.getSelectedPiece().getTile().equals(clickedTile))
				{ // If the player clicks on the tile that their selected piece originated from, they must be de-selecting
					currentPlayer.deselect();
					Chess.playSound("assets/Pickup_Place.wav");
				}
				else if (!clickedTile.hasPiece() || !clickedTile.getPiece().getColor().equals(currentPlayer.getSelectedPiece().getColor()))
				{
					currentPlayer.getSelectedPiece().attemptMove(clickedTile); // In this condition, they clicked an empty tile so they must be trying to move
				}
				else // The user has a selected piece but didn't de-select their piece
				{
					if (clickedTile.hasPiece() && clickedTile.getPiece().getColor().equals(currentPlayer.getSelectedPiece().getColor()))
					{
						Piece selected = currentPlayer.getSelectedPiece();
						if (selected instanceof King && clickedTile.getPiece() instanceof Rook)
						{
							((King) selected).castle((Rook) clickedTile.getPiece());
						}
						else
						{
							Chess.playSound("assets/Invalid_Move.wav");
						}
					}
				}
			}
			else
			{
				Chess.playSound("assets/Invalid_Move.wav");
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			mouseX = e.getX();
			mouseY = e.getY();
		}
		
		// Unused Implemented Methods

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		public void mouseDragged(MouseEvent arg0) {}
	}

	public void finishGame(Color winner)
	{
		freeze();
		System.out.println((winner.equals(Color.WHITE) ? "BLACK" : "WHITE") + " Team Lost!");
	}
}
