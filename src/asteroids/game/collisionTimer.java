package asteroids.game;

import javax.swing.*;
import java.awt.event.*;

public class collisionTimer implements ActionListener
{

    private Timer timer;
    private ParticipantState pstate;
    private Object payload;
    private Participant p1;
    private Participant p2;

    public collisionTimer (int msecs, ParticipantState pstate, Participant p1, Participant p2)
    {
        this(msecs, "alien", pstate,p1,p2);

    }

    public collisionTimer (int msecs, Object payload, ParticipantState pstate, Participant p1, Participant p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.payload = payload;
        this.pstate = pstate;
        timer = new Timer(msecs, this);
        timer.start();
        p1.setCanCollides(p2);
        p2.setCanCollides(p1);

    }

    public void expire ()
    {
        timer.stop();
    }

    public void restart ()
    {
        timer.stop();
        timer.start();
    }

    public boolean running()
    {
        return timer.isRunning();
        
    }
    @Override
    public void actionPerformed (ActionEvent arg0)
    {
        // TODO Auto-generated method stub
        timer.stop();

        
        
        
    }

}
