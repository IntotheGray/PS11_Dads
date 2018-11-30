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
    private Controller controller;
    private Shape outline;
    final long startTime;
    private long endTime;

    public Debris(double x, double y, Controller controller)
    {
        this.controller = controller;
        setPosition(x,y);
        setPosition(x, y);
        setVelocity(1, RANDOM.nextDouble() * 2 * Math.PI);
        setRotation(2 * Math.PI * RANDOM.nextDouble());
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
    
    
    public void expireDebris()
    {
        Participant.expire(this);
    }
    
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
