package edu.wit.yeatesg.chess.objects.timers;

import edu.wit.yeatesg.chess.objects.Player;
import edu.wit.yeatesg.chess.objects.gui.Board;

public class BlitzTimer extends TurnTimer
{
	private static final long serialVersionUID = 5209026512513626357L;

	public BlitzTimer(Board b, int startTime, Player p1, Player p2)
	{
		super(b, startTime, p1, p2);
	}

	@Override
	public void tick()
	{
		getOtherPlayer().modifyRemainingSeconds(1);
		currentPlayer.modifyRemainingSeconds(-1);
		
		if (currentPlayer.getRemainingSeconds() < 0)
		{
			onTimeout(currentPlayer);
		}
		
	}


}
