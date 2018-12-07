package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Bullet extends Participant implements AsteroidDestroyer, AlienDestroyer
{

    private Controller controller;
    private Ship ship;
    private Shape outline;
    private double direction;
    private double XPosition, YPosition;
    final long startTime;
    private long endTime;

    public Bullet (double x, double y, double Direction, Controller controller, Ship ship)
    {

        this.direction = Direction;
        this.controller = controller;
        this.ship = ship;
        XPosition = x;
        YPosition = y;
        setPosition(x, y);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);

        poly.lineTo(-1, 1);
        poly.lineTo(0, 1);
        poly.lineTo(0, 0);

        poly.closePath();
        outline = poly;
        startTime = System.currentTimeMillis();

    }

    public void expireB ()
    {
        Participant.expire(this);
        controller.bulletDestroyed();
    }

    public void shoot ()
    {
        if (BULLET_SPEED + ship.getSpeed() <= SPEED_LIMIT)
        {
            setSpeed(BULLET_SPEED + ship.getSpeed());
            setDirection(direction);
        }
        else
        {
            setSpeed(SPEED_LIMIT);
            setDirection(direction);
        }
        if (controller.bullets)
        {
            setSpeed(30);
            setDirection(direction);
            
        }
        
        if (!controller.bullets)
        {
            new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 600);
        }
        if (controller.bullets)
        {
            new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 1600);
        }
        // accelerate(SHIP_ACCELERATION);

        // move();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * when a bullet collides with a bullet destroyer
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof BulletDestroyer)
        {
            expireB();
        }

    }

    public long timeElapsed (Participant p)
    {
        endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        return elapsed;
    }

    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("travelTime"))
        {
            expireB();
        }
    }

}
