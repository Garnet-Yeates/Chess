package edu.wit.yeatesg.chess.objects.gui;

import javax.swing.JLabel;

public class Console extends JLabel
{
	private static final long serialVersionUID = 2877167275636168459L;

	public Console(String text)
	{
		super(text);
	}
	
	public void log(String message)
	{
		setText(message);
	}
}
