/*
 * Austin Hoang
 * 12/2/2019
 * Final Project
 * A mouse clicker game to see how many times a user can hit a random moving X.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;

public class MouseClickClass extends JFrame implements ActionListener {
	//Initializing the correct mark for user to click
    private final String rightClick = "X";
    //Initializing number of rounds and number of board buttons.
    private final int MAX_ROUNDS = 20;
    private int numBoardButtons = 64;
    //Initializing game counters(# clicked, missed, rounds, time delay, setting 
    //wrongClick to an empty string.
    private int clicked = 0;
    private int misses = 0;
    private int round = 0;
    private int delay = 1500;
    private int percentage = 0;
    private String wrongClick = " ";
    //Initializing board/frame properties
    private JPanel panel = new JPanel();
    private JButton[] boardButtons = new JButton[numBoardButtons];
    private JLabel gameText;
    private Timer timer;
    private JPanel buttonsPanel = new JPanel();
    //Initializing buttons
    private JButton start = new JButton("Start");
    private JButton quit = new JButton("Quit");
    private JButton reset = new JButton("Reset");
    private JButton pause = new JButton("Pause");
    private JButton resume = new JButton("Resume");
    //Initializing booleans for caught/notCaught and game over.
    private boolean caught = false;
    private boolean notCaught = false;
    private boolean gameOver = false;
    //Initializing frame components
    JMenuBar bar = new JMenuBar();
    JMenu difficulty = new JMenu("Change Difficulty");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    JMenu changeButtons = new JMenu("Change Board Marks");
    JMenuItem noButton = new JMenuItem("No marks");
    JMenuItem button1 = new JMenuItem("0");
    JMenuItem button2 = new JMenuItem("8");

    public MouseClickClass() {
        // Setting up the JFrame.
        super("Mouse Clicker");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //Setting up game instructions with font and game panel.
        gameText = new JLabel("");
        Font myFont = new Font("Courier New", Font.BOLD, 16);
        JPanel game = new JPanel();
        

        //Setting layouts
        game.setLayout(new GridLayout(8, 8));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setJMenuBar(bar);

        //Creating each button of the grid layout.
        for (int i = 0; i < numBoardButtons; ++i) {
            boardButtons[i] = new JButton(wrongClick);
            boardButtons[i].setFont(myFont);
            boardButtons[i].addActionListener(this);
            boardButtons[i].setOpaque(true);
            boardButtons[i].setBorderPainted(false);
            boardButtons[i].setBackground(Color.WHITE);
            boardButtons[i].setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            game.add(boardButtons[i]);
        }

        //Setting up the timer.
        timer = new Timer(delay, this);
        timer.setInitialDelay(delay);
        timer.stop();
        //Adding action listeners to all buttons
        start.addActionListener(this);
        quit.addActionListener(this);
        reset.addActionListener(this);
        pause.addActionListener(this);
        resume.addActionListener(this);
        easy.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);
        noButton.addActionListener(this);
        button1.addActionListener(this);
        button2.addActionListener(this);

        //Adding elements elements.
        panel.add(gameText, BorderLayout.NORTH);
        panel.add(game, BorderLayout.CENTER);
        buttonsPanel.add(start);
        buttonsPanel.add(quit);
        buttonsPanel.add(reset);
        buttonsPanel.add(pause);
        buttonsPanel.add(resume);
        add(panel);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        bar.add(difficulty);
        bar.add(changeButtons);
        difficulty.add(easy);
        difficulty.add(medium);
        difficulty.add(hard);
        changeButtons.add(noButton);
        changeButtons.add(button1);
        changeButtons.add(button2);
        updateInstructions();     
        resume.setVisible(false);
    }

    //Returns a random index where the X will move
    public int getRandomPlacement() {
        Random random = new Random();
        return random.nextInt(numBoardButtons);
    }
        	
    //Using random index to move X
    public void moveMark() {    	
        timer.restart();
        int index = getRandomPlacement();
        for (int i = 0; i < numBoardButtons; ++i) {
            boardButtons[i].setText(wrongClick);
            if (i == index) {
                boardButtons[i].setText(rightClick);
                boardButtons[i].setBackground(Color.WHITE);
                boardButtons[i].setForeground(Color.BLACK);
            }
        }
    }
        
    //Resetting game variables
    private void resetGame() {
        clicked = 0;
        misses = 0;
        round = 0;
        gameOver = false;
        moveMark();
    }

    //Instructions updated to show current round, misses, and delay
    private void updateInstructions() {
        String roundStr = "<html>Click the ''X''<br/>Round: " + round + " of " + MAX_ROUNDS + "<br/>Misses:<html/> " 
        		+ misses + "<br>Delay: " + delay + "ms";
        gameText.setText(roundStr);
        //If the rounds reach the limit, set gameOver to true
        if(round >= MAX_ROUNDS-1) {
        	gameOver = true;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
        //The timer finishes, move the X.
        if (source == timer) {
            moveMark();
            ++misses;
            round++;
            updateInstructions();
        }
        //Starts the game.
        else if(source == start) {
        	timer.start();
        	moveMark();
        }
        //Calls resetGame() and resets the game.
        else if(source == reset) {
        	resetGame();
        	updateInstructions();
        	timer.stop();
        }
        //Closes the window
        else if(source == quit) {
        	super.dispose();
        }
        //Pauses the game by stopping the timer
        else if(source == pause) {
        	timer.stop();
        	//Setting resume visibility to true 
        	resume.setVisible(true);     	        	
        }
        //Restarts the game from a pause
        else if(source == resume) {
        	timer.start();
        	resume.setVisible(false);
        }
        //Sets the time delay to 1500 (easy)
        else if(source == easy) {
        	timer.setInitialDelay(1500);
        	delay = 1500;

        }
        //Sets the time delay to 1100 (medium)
        else if(source == medium) {
        	timer.setInitialDelay(1100);
        	delay = 1100;
        }
        //Sets the time delay to 800 (hard)
        else if(source == hard) {
        	timer.setInitialDelay(800);
        	delay = 800;
        }
        //Sets the game board buttons to empty (no marks, just X)
        else if(source == noButton) {
        	wrongClick = " ";
        }
        //Sets game board buttons to 0
        else if(source == button1) {
        	wrongClick = "0";
        }
        //Sets game board buttons to 8
        else if(source == button2) {
        	wrongClick = "8";
        }

        //Else if a button was clicked on the board.
        else {
            JButton button = (JButton)e.getSource();
            			
            //User clicked on the X
            if (button.getText().equals(rightClick)) {
                ++clicked;
                round++;
                for (int i = 0; i < numBoardButtons; ++i) {
                    boardButtons[i].setBackground(Color.WHITE);
                    boardButtons[i].setForeground(Color.BLACK);
                }
                caught = true;
                notCaught = false;
            }

            //User clicked something else on the board
            else if(button.getText().equals(wrongClick)) {	
            	round++;
            	++misses; 
                notCaught = true;
            }            
            //Game is finished, displays score and misses.
        	if (gameOver == true) {	
        		timer.stop();
                percentage = (int) Math.round(clicked / (double) (misses + clicked) * 100);
                JOptionPane.showMessageDialog(null, "Finished! \nYou correctly clicked " + percentage + 
                		"% of the time!\nMisses: " + misses + "\n");
            }
        	//Else the game is not finished.
            else if (caught == true) {            	
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                updateInstructions();
                moveMark();
            }
            else if(notCaught == true) {           	
            	button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
                updateInstructions();
                moveMark();
            }                        
        }
    }
}
