import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;


public class MouseClickClass extends JFrame implements ActionListener
{
    private final String NOT_MOUSE_CHAR = " ";
    private final String MOUSE_CHAR = "X";

    private final int MAX_ROUNDS = 5;
    private int NUM_boardButtons = 64;

    private int clicked = 0;
    private int misses = 0;
    private int round = 0;
    private int delay = 1500;

    private JPanel panel = new JPanel();
    private JButton[] boardButtons = new JButton[NUM_boardButtons];
    private JLabel instructions;
    private Timer timer;
    private JPanel buttonsPanel = new JPanel();
    
    private JButton start = new JButton("Start");
    private JButton quit = new JButton("Quit");
    private JButton reset = new JButton("Reset");
    private JButton pause = new JButton("Pause");
    
    int percentage = 0;
    boolean caught = false;
    boolean notCaught = false;
    boolean gameOver = false;

    
    JMenuBar bar = new JMenuBar();
    JMenu difficulty = new JMenu("Change Difficulty");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");

    /**
     * Constructor.
     **/
    public MouseClickClass()
    {
        // Setup the JFrame...
        super("Catch The Mouse!");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Game instructions and components.
        instructions = new JLabel("");
        Font myFont = new Font("Courier New", Font.BOLD, 16);
        JPanel game = new JPanel();
        

        // Padding around edges.
        game.setLayout(new GridLayout(8, 6));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setJMenuBar(bar);

        // Create each button.
        for (int i = 0; i < NUM_boardButtons; ++i)
        {
            boardButtons[i] = new JButton(NOT_MOUSE_CHAR);
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
        easy.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);

        // Add elements.
        panel.add(instructions, BorderLayout.NORTH);
        panel.add(game, BorderLayout.CENTER);
        buttonsPanel.add(start);
        buttonsPanel.add(quit);
        buttonsPanel.add(reset);
        buttonsPanel.add(pause);
        add(panel);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        bar.add(difficulty);
        difficulty.add(easy);
        difficulty.add(medium);
        difficulty.add(hard);
        //moveMouse();
        updateInstructions();     
    }

    /**
     * Returns a random index to be used with the boardButtons array.
     */
    public int getRandomPlacement()
    {
        Random random = new Random();
        return random.nextInt(NUM_boardButtons);
    }
        	
    /**
     * Using the random index, determine which button will be the "mouse".
     */
    public void moveMouse()
    {    	
        timer.restart();
        int index = getRandomPlacement();
        for (int i = 0; i < NUM_boardButtons; ++i)
        {
            boardButtons[i].setText(NOT_MOUSE_CHAR);
            if (i == index)
            {
                boardButtons[i].setText(MOUSE_CHAR);
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
        		+ misses + "<br>Delay: " + delay;
        instructions.setText(roundStr);
        if(round >= MAX_ROUNDS-1) {
        	timer.stop();
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
        }
        else if(source == quit) {
        	super.dispose();
        }
        else if(source == pause) {
        	timer.stop();
        	pause.setText("Resume");        	        	
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
        // A button was clicked...
        else 
        {
            JButton button = (JButton)e.getSource();
            
            int percentage = 0;
            boolean caught = false;
            boolean notCaught = false;
			
            // ...the user clicked on the mouse!
            if (button.getText().equals(MOUSE_CHAR))
            {
                ++clicked;
                round++;
                for (int i = 0; i < NUM_boardButtons; ++i)
                {
                    boardButtons[i].setBackground(Color.WHITE);
                    boardButtons[i].setForeground(Color.BLACK);
                }
                caught = true;
                notCaught = false;
            }

            // ...the user clicked on something else. Add a miss to their score.
            else if(button.getText().equals(NOT_MOUSE_CHAR))
            {	
            	round++;
            	++misses; 
                notCaught = true;
            }            
            // The game is finished. Display the score to the user and reset the game.
        	if (gameOver == true)
            {
                percentage = (int) Math.round(clicked / (double) (misses + clicked) * 100);
                JOptionPane.showMessageDialog(null, "Finished! \nYou caught the mouse " + percentage + "% of the time!\nMisses: " + misses + "\n");
            }


            // The game is not finished. Move the mouse.
            
            else if (caught == true)
            {            	
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
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

