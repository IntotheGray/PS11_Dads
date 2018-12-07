package asteroids.game;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The main class for the application.
 * 
 * @author Eric J. Marsh, Jack Ronnie Skate Fast...
 */
public class Asteroids
{
    /**
     * Launches a dialog that lets the user choose between a classic and an enhanced game of Asteroids.
     */
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater( () -> chooseVersion());
    }

    /**
     * Interacts with the user to determine whether to run classic Asteroids or enhanced Asteroids.
     */
    private static void chooseVersion ()
    {
        String[] options = { "Classic", "Enhanced" };
        int choice = JOptionPane.showOptionDialog(null, "Which version would you like to run?", "Choose a Version",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0)
        {
            new Controller(1);
        }
        else if (choice == 1)
        {
            String[] options2 = { "Easy", "Hard", "D̮͚̝̬e̡a̖̣̙͎̲͞ͅt͍͉͓̰͠h͏͚ͅ" };
            int choice2 = JOptionPane.showOptionDialog(null, "Choose your level", "Don't pick option 3",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
                if (choice2 == 0)
                {
                    new Controller(0);
                }
                else if (choice2 == 1)
                {
                    new Controller(2);
                }
                else if (choice2 == 2)
                {
                    String[] option3 = {"thanks"};
                    JOptionPane.showOptionDialog(null, "Welcome to hell amigo", "oh no",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option3, 0);
                    
                    new Controller(3);
                }


            
        }
    }
}
