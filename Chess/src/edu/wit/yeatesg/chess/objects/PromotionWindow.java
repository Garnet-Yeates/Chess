package edu.wit.yeatesg.chess.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.wit.yeatesg.chess.main.Chess;
import edu.wit.yeatesg.chess.objects.pieces.Bishop;
import edu.wit.yeatesg.chess.objects.pieces.Knight;
import edu.wit.yeatesg.chess.objects.pieces.Piece;
import edu.wit.yeatesg.chess.objects.pieces.Queen;
import edu.wit.yeatesg.chess.objects.pieces.Rook;
import edu.wit.yeatesg.pathing.Point;

public class PromotionWindow extends JFrame
{
	private static final long serialVersionUID = 92705488934126508L;

	private Color team;
	private Piece piece;
	private Board b;
	
	public PromotionWindow(Piece toPromote, Board b)
	{
		this.b = b;
		b.freeze();
		b.transferFocus();
		this.requestFocus();
		this.setLocation(192, 336);
		this.setAutoRequestFocus(true);
		this.setTitle("Click A Piece To Promote Your Pawn!");
		this.piece = toPromote;
		this.team = toPromote.getColor();
		PromotionBoard panel = this.new PromotionBoard();
		panel.setPreferredSize(panel.getPreferredSize());
		this.getContentPane().add(panel);
		this.setAlwaysOnTop(true);
		this.pack();
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.add(panel);
	}
	
	class PromotionBoard extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1422271377529974455L;
		
		Tile[] tiles = new Tile[4];
		
		public PromotionBoard()
		{
			addMouseListener(this);
			
			tiles[0] = new Tile(new Color(236, 238, 212), new Point(-1, 0), null);
			tiles[1] = new Tile(new Color(181, 136, 99), new Point(-1, 1), null);
			tiles[2] = new Tile(new Color(236, 238, 212), new Point(-1, 2), null);
			tiles[3] = new Tile(new Color(181, 136, 99), new Point(-1, 3), null);

			new Rook(tiles[0], team);
			new Knight(tiles[1], team);
			new Bishop(tiles[2], team);
			new Queen(tiles[3], team);
		}
		
		@Override
		public void paint(Graphics g)
		{
			int xPos = 0;
			for (int x = 0; x < 4; x++)
			{
				Tile t = tiles[x];
				g.setColor(t.getColor());
				g.fillRect(xPos, 0, 96, 96);
				xPos += 96;
			}
			
			xPos = 0;
			for (int x = 0; x < 4; x++)
			{
				Piece p = tiles[x].getPiece();
				p.getImageIcon().paintIcon(this, g, xPos, 0);
				xPos += 96;
			}
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(384 - 10, 96 - 10);
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			int xPos = e.getX() / 96;
			
			Tile tile = piece.getTile();
			piece.getTile().setPiece(null);
			piece.setTile(null);
						
			switch (xPos)
			{
			case 0: // Rook
				new Rook(tile, piece.getColor());
				break;
			case 1: // Knight
				new Knight(tile, piece.getColor());
				break;
			case 2: // Bishop
				new Bishop(tile, piece.getColor());
				break;
			case 3: // Queen
				new Queen(tile, piece.getColor());
				break;
			}
			
			PromotionWindow.this.dispose();
			Chess.playSound("assets/Promote.wav");
			b.unFreeze();
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{ }

		@Override
		public void mouseExited(MouseEvent e)
		{ }

		@Override
		public void mousePressed(MouseEvent e)
		{ }

		@Override
		public void mouseReleased(MouseEvent e)
		{ }
	}
}
