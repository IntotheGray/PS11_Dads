package asteroids.participants;

import java.awt.geom.Path2D;
import java.awt.Shape;
import asteroids.destroyers.BulletDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

public class Debris extends Participant
{
    //To keep track of debris mainly to move and remove the participants
    private Controller controller;
    //shape of debris
    private Shape outline;
    //To keep track of how long to show debris
    final long startTime;
    private long endTime;

    /**
     * Given an x and y location create a random moving debris
     * @param x
     * @param y
     * @param controller
     */
    public Debris(boolean isShip, double x, double y, Controller controller)
    {
        this.controller = controller;
        setPosition(x,y);
        setVelocity(1, RANDOM.nextDouble() * 2 * Math.PI);
        setRotation(2 * Math.PI * RANDOM.nextDouble());
        if (isShip)
        {
            
        }
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1,0);
        poly.lineTo(-1, 1);
        poly.lineTo(1,1);
        poly.lineTo(1, -1);
        poly.lineTo(-1, -1);
        poly.lineTo(-1, 1);
        poly.closePath();
        outline = poly;
        startTime = System.currentTimeMillis();
        new ParticipantCountdownTimer(this, "travelTime", 1000);
    }
    
    /**
     * Removes debris
     */
    public void expireDebris()
    {
        Participant.expire(this);
    }
    
    /**
     * Required to display debris from participants
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    
    @Override
    public void collidedWith (Participant p)
    {
        
    }
    
    public long timeElapsed (Participant p)
    {
        endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        return elapsed;
    }
    
    @Override 
    public void countdownComplete(Object payload)
    {
        if (payload.equals("travelTime"))
        {
            expireDebris();
        }
    }
}
