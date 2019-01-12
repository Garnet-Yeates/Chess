package edu.wit.yeatesg.chess.objects;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AlternatativeFrame extends JFrame
{
	private static final long serialVersionUID = 2354095733328681014L;
	private JPanel contentPane;
	private Board board = new Board(this);

	JLabel p1TimerLabel = new JLabel(" --- ");
	JLabel p2TimerLabel = new JLabel(" --- ");
	JLabel whoseTurnLabel = new JLabel("");
	private final JLabel lblNewLabel = new JLabel("New label");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AlternatativeFrame frame = new AlternatativeFrame();
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AlternatativeFrame()
	{
		addKeyListener(this.new BoardKeyListener());
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBorder(new LineBorder(Color.WHITE, 3, true));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 932);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.getContentPane().setBackground(new Color(36, 37, 38));
		this.setBackground(Color.DARK_GRAY);
		board.setBackground(Color.BLACK);
		board.setPreferredSize(board.getPreferredSize());
		p1TimerLabel.setBackground(Color.WHITE);
		p1TimerLabel.setForeground(Color.WHITE);
		
		p1TimerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		p1TimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		p1TimerLabel.setBorder(new LineBorder(Color.WHITE, 3, true));
		p2TimerLabel.setForeground(Color.WHITE);
		
		p2TimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		p2TimerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		p2TimerLabel.setBorder(new LineBorder(Color.WHITE, 3, true));
		whoseTurnLabel.setForeground(Color.WHITE);
		
		whoseTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		whoseTurnLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		whoseTurnLabel.setBorder(new LineBorder(Color.WHITE, 3, true));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(board, GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(p1TimerLabel, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(whoseTurnLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(p2TimerLabel, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(board, GroupLayout.PREFERRED_SIZE, 768, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(p1TimerLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addComponent(p2TimerLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addComponent(whoseTurnLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		board.setBorder(new LineBorder(Color.BLACK));
		System.out.println(board.getBorder());
		
		repaint();
	}
	
	public void updateClocks()
	{
		p1TimerLabel.setText(board.getP1BlitzClockString());
		p2TimerLabel.setText(board.getP2BlitzClockString());
		whoseTurnLabel.setText(board.getCurrentPlayerString());
	}
	
	public class BoardKeyListener implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			board.onKeyPress(e);
		}

		@Override
		public void keyReleased(KeyEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0)
		{


		}

	}
}
