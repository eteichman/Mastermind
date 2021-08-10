package game;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PreviousGuesses extends JFrame{
	
	public PreviousGuesses(){
		
		super("Previous Guesses");

	}
	
	public JPanel displayEmptyBoardWithFrame(int levelSize, JPanel mainPanel){
		JPanel buttonPanel1 = new JPanel(new GridLayout(1, 2, 2, 2));
		JButton button = new JButton("Red Result   =   Right Number, Right Place");
		button.setBackground(new Color(150, 200, 200));
		buttonPanel1.add(button);
		button = new JButton("White Result   =   Right Number, Wrong Place");
		button.setBackground(new Color(150, 200, 200));
		buttonPanel1.add(button);
		
		mainPanel.add(buttonPanel1);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, levelSize + 2, 2, 2));
    	    	
    	for(int i = 0; i < levelSize; i++){
			button = new JButton("Position " + (i + 1));
			button.setBackground(new Color(200, 150, 200));
			buttonPanel.add(button);
		}

    	button = new JButton("Red Results");
		button.setBackground(new Color(200, 200, 150));
		buttonPanel.add(button);
		
		button = new JButton("White Results");
		button.setBackground(new Color(200, 200, 150));
		buttonPanel.add(button);

		mainPanel.add(buttonPanel);
		        
		getContentPane().add(mainPanel);
//      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(levelSize*100+375, 700);
      setVisible(true);
		
        return mainPanel;
	}
	
	public void closeBoard(){
		setVisible(false);
	}
	
	public JPanel addRow(Integer[] guessInt, JPanel mainPanel, String[] thisGuessReply){
		Color[] colors = new Color[guessInt.length];
		JPanel buttonPanel = new JPanel(new GridLayout(1, colors.length+2, 2, 2));
    	JButton button = new JButton();
    	    	
    	for(int i = 0; i<guessInt.length; i++){
			if (guessInt[i] == 1){
				colors[i] = new Color(235, 21, 21); //red
			} 
			else if (guessInt[i] == 2){
				colors[i] = new Color(240, 128, 16); //orange
			} 
			else if (guessInt[i] == 3){
				colors[i] = new Color(255, 255, 0); //yellow
			} 
			else if (guessInt[i] == 4){
				colors[i] = new Color(34, 255, 0); //green
			} 
			else if (guessInt[i] == 5){
				colors[i] = new Color(21, 56, 235); //blue
			} 
			else if (guessInt[i] == 6){
				colors[i] = new Color(142, 18, 236); //purple
			} 
			else if (guessInt[i] == 7){
				colors[i] = new Color(255, 0, 196); //pink
			}
			button = new JButton(guessInt[i].toString());
			button.setBackground(colors[i]);
			buttonPanel.add(button);
		}

		buttonPanel.add(new JButton(thisGuessReply[0]));
		buttonPanel.add(new JButton(thisGuessReply[1]));

		mainPanel.add(buttonPanel);
		
		
		getContentPane().add(mainPanel);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(colors.length*100+375, 700);
        setVisible(true);
        
        return mainPanel;
	}
	
}
