package asteroids.game;

import javax.swing.*;
import java.awt.event.*;

public class controllerCountdownTimer implements ActionListener
{

    private Timer timer;
    private Controller controller;

    public controllerCountdownTimer (int msecs, Controller controller)
    {
        this.controller = controller;
        timer = new Timer(msecs, this);
        timer.start();

    }

    @Override
    public void actionPerformed (ActionEvent arg0)
    {
        // TODO Auto-generated method stub
        timer.stop();
        controller.countdownComplete();
    }

}
