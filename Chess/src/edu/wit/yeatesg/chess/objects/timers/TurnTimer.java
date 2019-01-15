package edu.wit.yeatesg.chess.objects.timers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import edu.wit.yeatesg.chess.objects.Player;
import edu.wit.yeatesg.chess.objects.gui.Board;

public abstract class TurnTimer extends Timer implements ActionListener
{		
	protected static final long serialVersionUID = -181704799419664359L;
	
	protected Player currentPlayer;
	
	protected Player p1;
	protected Player p2;
	
	protected Board board;
		
	public TurnTimer(Board b, int startTime, Player p1, Player p2)
	{
		super(1000, null);
		this.board = b;
		p1.setRemainingSeconds(startTime);
		p2.setRemainingSeconds(startTime);
		currentPlayer = p1;
		addActionListener(this);
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Player getOtherPlayer()
	{
		if (currentPlayer.equals(p1))
		{
			return p2;
		}
		else
		{
			return p1;
		}
	}
	
	public void onTurnSwitch()
	{
		if (currentPlayer.equals(p1))
		{
			currentPlayer = p2;
		}
		else
		{
			currentPlayer = p1;
		}
	}
	
	public void onTimeout(Player p)
	{
		this.stop();
		board.freeze();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		tick();
	}
	
	public abstract void tick();
	
}
