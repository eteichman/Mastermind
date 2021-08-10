package game;
import javax.swing.*;
import javax.sound.sampled.*;

import java.awt.GridLayout;
import java.io.*;

public class MasterMind 
{
	//so we can pass in a mock object to test the arrays
	private IRandomValueGenerator rand;
	private Integer[] answer;
	private PreviousGuesses guesses;
	private JPanel mainPanel;
	
	//constructor; initialize the ArrayList to hold Strings of previous guesses
	public MasterMind(IRandomValueGenerator rand)
	{
		this.rand=rand;		
	}

	//***//***//The methods are presented below in the order that they are used://***//***//
	
		
	//play method that calls all methods so main does not have to
	public void play() throws IOException, LineUnavailableException
	{
		int choice;
		do{
			guesses = new PreviousGuesses();
			mainPanel = new JPanel(new GridLayout(12, 4, 2, 2));	
			String level=gameLevel();
			int levelLength = getLengthOfLevel(level);
			createArray(levelLength);
			boolean win=false;
			int tryNum=0;
			guesses.displayEmptyBoardWithFrame(levelLength, mainPanel);
			while(!win && tryNum<10)
			{
				Integer[] guessInt=getUserInput(level);
				tryNum++;
				win=checkGuess(guessInt);
				result(win, guessInt);
			}
			
			if(!win && tryNum==10)
			{
				playAudio("audio/sad_trombone.wav");
				JOptionPane.showMessageDialog(null, "Game over");
			}
			choice = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "MasterMind", JOptionPane.YES_OPTION);
			if (choice==JOptionPane.YES_OPTION){
				guesses.closeBoard();
			}
		}while(choice==JOptionPane.YES_OPTION);
	}
	
	//to let the user choose a game level
	protected String gameLevel()
	{
		JRadioButton easy=new JRadioButton("Easy");
			easy.setActionCommand("easy");
			easy.setSelected(true);
		JRadioButton medium=new JRadioButton("Medium");
			medium.setActionCommand("medium");
		JRadioButton hard=new JRadioButton("Hard");
			hard.setActionCommand("hard");
	
		final ButtonGroup group=new ButtonGroup();
			group.add(easy);
			group.add(medium);
			group.add(hard);

		final JPanel buttons=new JPanel();
		buttons.add(new JLabel("Pick Level: "));
			buttons.add(easy);
			buttons.add(medium);
			buttons.add(hard);
		
		int choice = JOptionPane.showConfirmDialog(null, buttons, "MasterMind", JOptionPane.PLAIN_MESSAGE);
		
		if (choice == JOptionPane.CLOSED_OPTION)
		{
			System.exit(0);
		}
					
		return group.getSelection().getActionCommand();
	}	
	
	protected int getLengthOfLevel(String level){
		int length;
		switch(level)
		{
		case "easy":
			length=3;
			break;
		case "medium":
			length=4;
			break;
		case "hard":
			length=5;
			break;
		default:
			length=0;
		}
		return length;
	}
	
	//call generateRandomArray() with length based on level
	protected Integer[] createArray(int levelLength)
	{
		answer=rand.generateRandomArray(levelLength);
		return answer;
	}
	
	//gets the user input and converts the String returned to an array
	protected Integer[] getUserInput(String level)
	{
		Integer[] guess=null;
		String guessString=null;
		boolean validGuess=false;
		
		do{		
			try {
				guessString=createTextFieldAndGetUserGuess(level); //calls the text-field method that is the right size
													//for that level, returns user's guess
				guess=stringToIntArr(guessString);	//copy the users' guess into an array to be able to
													//compare it to the answer
				validGuess=true;
			}
			catch(InvalidEntryException i)
			{
				JOptionPane.showMessageDialog(null, "Invalid entry. Guess must be an integer from 1-7");
				validGuess=false;
			}
			
		}while(!validGuess);
		
		return guess;
	}
	
	//call the text-field method that is the right size for that level, returns user's guess
	protected String createTextFieldAndGetUserGuess(String level)
	{
		String guessString=null;
		
		try {
			switch(level)
			{
				case "easy":
					guessString=createJComponent(3);	
					break;
				case "medium":
					guessString=createJComponent(4);
					break;
				case "hard":
					guessString=createJComponent(5);
					break;
			}
		}
		catch(InvalidEntryException i)
		{
			throw new InvalidEntryException();
		}
		
		return guessString;
	}
	
	//create a JComponent for user entry
	private String createJComponent(int levelLength) 
	{
		JTextField[] guessField=new JTextField[levelLength];
		
		for (int t = 0; t<guessField.length; t++) 
		{
		 	guessField[t] = new JTextField();
		 	guessField[t].setToolTipText("Enter guess here");
		}
		
		final JComponent[] inputs = new JComponent[1+(levelLength*2)]; 
		
		inputs[0] = new JLabel("Enter a guess from 1-7 into each slot:");
		
		int r=0;		
		for(int q=1; q<inputs.length;q+=2){
			inputs[q] = (new JLabel("#"+(r+1)+":"));
			inputs[q+1] = guessField[r];
			r++;
		}
		
		int choice = JOptionPane.showConfirmDialog(null, inputs, "MasterMind", JOptionPane.OK_CANCEL_OPTION);
		if(choice==JOptionPane.CANCEL_OPTION || choice==JOptionPane.CLOSED_OPTION)
		{
			System.exit(0);
		}
		
		try {
			return textFieldToString(guessField, levelLength);
		}
		catch(InvalidEntryException i)
		{
			throw new InvalidEntryException();
		}
	} 
	
	//convert the JTextField to a String in order to be able to use it and test it
	private String textFieldToString(JTextField[] field, int len) 
	{
		String s = "";
		String temp;
		for(int i=0; i<field.length; i++)
		{		
			temp = field[i].getText();
						
			if(temp.equals("8") || temp.equals("9"))
			{
				throw new InvalidEntryException();

			}
			
			s+=temp;
		}
	
		if(s.length()!=len)
		{
			throw new InvalidEntryException();
		}
		return s;
	}
	
	//converts the String to an array to be able to compare it to the answer
	protected Integer[] stringToIntArr(String guessString)
	{
		if(guessString.length()==0)
		{
			throw new InvalidEntryException();
		}
		Integer[] guessInt=new Integer[guessString.length()];
		
		for (int y=0; y<guessInt.length; y++)
		{
			try {
				if(Character.isDigit(guessString.charAt(y)))
				{
					guessInt[y]=Character.getNumericValue(guessString.charAt(y));
				}
				else
				{
					throw new InvalidEntryException();
				}
			}
			catch(NumberFormatException f)
			{
				throw new InvalidEntryException();
			}

			if(guessInt[y]<1)
			{
				throw new InvalidEntryException();
			}
		}
		
		return guessInt;
	}
	
	//check if the guess is 100% true
	protected boolean checkGuess(Integer[] guessInt)
	{

		for(int i=0; i<guessInt.length; i++)
		{
			if(guessInt[i]!=answer[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	//call displayWin() if user won and displayLoss() if didn't get it all right yet
	protected void result(boolean win, Integer[] guessInt) throws IOException, LineUnavailableException
	{				
		if(win)
		{
			displayWin(guessInt);
		}
		else
		{
			displayLoss(guessInt, checkHowManyRightPlace(guessInt), checkHowManyWrongPlace(guessInt));
			
		}
	}

	//call playAudio() and display "You Win!" message
	private void displayWin(Integer[] guessInt) throws IOException, LineUnavailableException
	{
		playAudio("audio/correct-answer.wav");
		String[] thisGuessReply = {"Red: "+guessInt.length, "White: 0"};
		guesses.addRow(guessInt, mainPanel, thisGuessReply);
		JOptionPane.showConfirmDialog(null, "You Win!", "MasterMind", JOptionPane.PLAIN_MESSAGE);		
	}
	
	//display how many were Right-num-Right-place and how many were Right-num-Wrong-place, call playAudio()
	private void displayLoss(Integer[] guessInt, int correct, int wrongPlace) throws IOException, LineUnavailableException
	{		
		String[] thisGuessReply = {"Red: " + correct, "White: " + wrongPlace};
		guesses.addRow(guessInt, mainPanel, thisGuessReply);
		playAudio("audio/pling.wav");
		
	}
	
	//to play the audio for a win or a loss
	private void playAudio(String filename) throws IOException, LineUnavailableException
	{
		File soundFile;
		AudioInputStream stream;
		SourceDataLine soundLine = null;
		int BUFFER_SIZE = 64*1024; 
		try {
			soundFile = new File(filename);	
			stream = AudioSystem.getAudioInputStream(soundFile);
			AudioFormat audioFormat = stream.getFormat();
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	        soundLine = (SourceDataLine) AudioSystem.getLine(info);
	        soundLine.open(audioFormat);
	        soundLine.start();
	        int nBytesRead = 0;
	        byte[] sampledData = new byte[BUFFER_SIZE];
	        while (nBytesRead != -1) 
	        {
	           nBytesRead = stream.read(sampledData, 0, sampledData.length);
	           if (nBytesRead >= 0) 
	           {
	              soundLine.write(sampledData, 0, nBytesRead);
	           }
	        }
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
			return;
		}
		catch(FileNotFoundException f)
		{
			System.err.println("File not found");
		}
	}
		
	//check how many were: Right number, Right place
	protected int checkHowManyRightPlace(Integer[] guessInt)
	{
		int total=0;	
		for(int i=0; i<guessInt.length; i++)
		{
			if(guessInt[i]==answer[i])
			{
				total++;
			}
		}
		
		return total;
	}
	
	//check how many were: Right number, Wrong place
	protected int checkHowManyWrongPlace(Integer[] guessInt)
    {
		int[] copyAns = new int[answer.length];
		int[] copyGuess = new int[guessInt.length];
		
		for (int r=0; r<answer.length; r++)
		{
			copyAns[r] = answer[r];
			
			copyGuess[r] = guessInt[r];
			
			if(copyGuess[r]==copyAns[r])
			{
				copyGuess[r] = -1; //change the one in the guess array to -1
				copyAns[r] = 0; //change the one in the answer array to 0
			}
		}
				
		int wrongPlace=0;
        for(int x=0; x<copyAns.length; x++)
        {
            for(int y=0; y<copyGuess.length; y++)
            {
                if(copyAns[x]==copyGuess[y])
                {
                	wrongPlace++;
                	copyAns[x] = 0; //change the one in the answer array to 0
                	copyGuess[y] = -1; //change the one in the guess array to -1
                }
            }
        }
        return wrongPlace;    
    }
		
	
	//main, creates a new MasterMind game and calls the play() method, the only public method 	
	public static void main(String[] args) throws IOException, LineUnavailableException
	{
		MasterMind game=new MasterMind(new RegRandom());
		game.play();
		
		System.exit(0);
	}
}
