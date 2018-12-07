package asteroids.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class BeatCountdownTimer implements ActionListener
{
    //Timer
    private Timer timer;
    //Brought in from controller class
    private Controller controller;
    //Instance variable to determine what beat called next
    private int whatBeats;

    
    public BeatCountdownTimer (int whatBeat, int msecs, Controller controller)
    {
        this.controller = controller;
        timer = new Timer(msecs, this);
        timer.start();

        whatBeats = whatBeat;
    }
    
    /**
     * called when timer is complete
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        //Calls countdown depending on what beat is played next
        timer.stop();
        if (whatBeats == 1)
        {
            controller.beat1Countdown();
        }
        else
        {
            controller.beat2Countdown();
        }
    }
}
