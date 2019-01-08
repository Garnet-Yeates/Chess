package edu.wit.yeatesg.chess.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import edu.wit.yeatesg.chess.objects.Board;

public class Chess
{
	public static void main(String[] args)
	{	
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame gameFrame = new JFrame();
				Board gamePanel = new Board(gameFrame);
				//gameFrame.addKeyListener(gamePanel);
				gamePanel.setPreferredSize(gamePanel.getPreferredSize());
				gameFrame.getContentPane().add(gamePanel);
				gameFrame.pack();
				gameFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
				gameFrame.setResizable(false);
				gameFrame.setVisible(true);
				gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameFrame.add(gamePanel);
			}
		});
	}

	/**
	 * Plays the sound file at the given path, if there is any
	 * @param path the location of the file
	 */
	public static void playSound(String path)
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
