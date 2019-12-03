package finalProject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;


public class MouseClickClass extends JFrame implements ActionListener
{
    private final String rightClick = "X";

    private final int MAX_ROUNDS = 5;
    private int numBoardButtons = 64;

    private int clicked = 0;
    private int misses = 0;
    private int round = 0;
    private int delay = 1500;
    private String wrongClick = " ";


    private JPanel panel = new JPanel();
    private JButton[] boardButtons = new JButton[numBoardButtons];
    private JLabel instructions;
    private Timer timer;
    private JPanel buttonsPanel = new JPanel();
    
    private JButton start = new JButton("Start");
    private JButton quit = new JButton("Quit");
    private JButton reset = new JButton("Reset");
    private JButton pause = new JButton("Pause");
    private JButton resume = new JButton("Resume");
    
    int percentage = 0;
    boolean caught = false;
    boolean notCaught = false;
    boolean gameOver = false;

    
    JMenuBar bar = new JMenuBar();
    JMenu difficulty = new JMenu("Change Difficulty");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");
    JMenu changeButtons = new JMenu("Change Buttons");
    JMenuItem noButton = new JMenuItem("No button");
    JMenuItem button1 = new JMenuItem("0");
    JMenuItem button2 = new JMenuItem("8");


    /**
     * Constructor.
     **/
    public MouseClickClass()
    {
        // Setup the JFrame...
        super("Mouse Clicker");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Game instructions and components.
        instructions = new JLabel("");
        Font myFont = new Font("Courier New", Font.BOLD, 16);
        JPanel game = new JPanel();
        

        // Padding around edges.
        game.setLayout(new GridLayout(8, 8));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setJMenuBar(bar);

        // Create each button.
        for (int i = 0; i < numBoardButtons; ++i)
        {
            boardButtons[i] = new JButton(wrongClick);
            boardButtons[i].setFont(myFont);
            boardButtons[i].addActionListener(this);
            boardButtons[i].setOpaque(true);
            boardButtons[i].setBorderPainted(false);
            boardButtons[i].setBackground(Color.WHITE);
            boardButtons[i].setForeground(Color.BLACK);
            boardButtons[i].setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            game.add(boardButtons[i]);
        }

        // Setup timer.
        timer = new Timer(delay, this);
        timer.setInitialDelay(delay);
        timer.stop();
        
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


        // Add elements.
        panel.add(instructions, BorderLayout.NORTH);
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
        //moveMouse();
        updateInstructions();     
        resume.setVisible(false);
    }

    /**
     * Returns a random index to be used with the boardButtons array.
     */
    public int getRandomPlacement()
    {
        Random random = new Random();
        return random.nextInt(numBoardButtons);
    }
        	
    /**
     * Using the random index, determine which button will be the "mouse".
     */
    public void moveMouse()
    {    	
        timer.restart();
        int index = getRandomPlacement();
        for (int i = 0; i < numBoardButtons; ++i)
        {
            boardButtons[i].setText(wrongClick);
            if (i == index)
            {
                boardButtons[i].setText(rightClick);
                boardButtons[i].setBackground(Color.WHITE);
                boardButtons[i].setForeground(Color.BLACK);
            }
        }
    }
        
    /**
     * Resets the core variables so the player can play again.
     */
    private void resetGame()
    {
        clicked = 0;
        misses = 0;
        round = 0;
        gameOver = false;
        moveMouse();
    }

    /**
     * Prints the current round to the user.
     */
    private void updateInstructions()
    {
        String roundStr = "<html>Click the ''X''<br/>Round: " + round + " of " + MAX_ROUNDS + "<br/>Misses:<html/> " 
        		+ misses + "<br>Delay: " + delay + "ms";
        instructions.setText(roundStr);
        if(round >= MAX_ROUNDS-1) {
        	gameOver = true;
        }
    }

    /**
     * Event handling.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
        // The timer finishes. Move the mouse!
        if (source == timer)
        {
            moveMouse();
            ++misses;
            round++;
            updateInstructions();
        }
        else if(source == start) {
        	timer.start();
        	moveMouse();
        }
        else if(source == reset) {
        	resetGame();
        	updateInstructions();
        	timer.stop();
        }
        else if(source == quit) {
        	super.dispose();
        }
        else if(source == pause) {
        	timer.stop();
        	resume.setVisible(true);     	        	
        }
        else if(source == resume) {
        	timer.start();
        	resume.setVisible(false);
        }
        else if(source == easy) {
        	timer.setInitialDelay(1500);
        	delay = 1500;

        }
        else if(source == medium) {
        	timer.setInitialDelay(1300);
        	delay = 1300;

        }
        else if(source == hard) {
        	timer.setInitialDelay(700);
        	delay = 700;

        }
        else if(source == noButton) {
        	wrongClick = " ";
        }
        else if(source == button1) {
        	wrongClick = "0";
        }
        else if(source == button2) {
        	wrongClick = "8";
        }

        // A button was clicked...
        else 
        {
            JButton button = (JButton)e.getSource();
            
            int percentage = 0;
            boolean caught = false;
            boolean notCaught = false;
			
            // ...the user clicked on the mouse!
            if (button.getText().equals(rightClick))
            {
                ++clicked;
                round++;
                for (int i = 0; i < numBoardButtons; ++i)
                {
                    boardButtons[i].setBackground(Color.WHITE);
                    boardButtons[i].setForeground(Color.BLACK);
                }
                caught = true;
                notCaught = false;
            }

            // ...the user clicked on something else. Add a miss to their score.
            else if(button.getText().equals(wrongClick))
            {	
            	round++;
            	++misses; 
                notCaught = true;
            }            
            // The game is finished. Display the score to the user and reset the game.
        	if (gameOver == true)
            {	timer.stop();
                percentage = (int) Math.round(clicked / (double) (misses + clicked) * 100);
                JOptionPane.showMessageDialog(null, "Finished! \nYou caught the mouse " + percentage + "% of the time!\nMisses: " + misses + "\n");
            }


            // The game is not finished. Move the mouse.
            
            else if (caught == true)
            {            	
                //button.setBackground(Color.GREEN);
                //button.setForeground(Color.WHITE);
                updateInstructions();
                moveMouse();
            }
            else if(notCaught == true) {           	
            	button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
                updateInstructions();
                moveMouse();
            }
                        
        }
    }
}
