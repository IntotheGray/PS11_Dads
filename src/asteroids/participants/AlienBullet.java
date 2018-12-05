package asteroids.participants;

import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Constants;
import static asteroids.game.Constants.*;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienBullet extends Participant implements ShipDestroyer, AsteroidDestroyer
{

    private double direction;
    private AlienShip alienShip;
    private Shape outline;
    final long startTimes;
    private long endTimes;
    private Controller controller;

    public AlienBullet (double x, double y, double Direction, Controller controller, AlienShip alienShip)
    {
        this.direction = Direction;
        this.controller = controller;
        this.alienShip = alienShip;

        setPosition(x, y);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);

        poly.lineTo(-1, 1);
        poly.lineTo(0, 1);
        poly.lineTo(0, 0);

        poly.closePath();
        outline = poly;
        startTimes = System.currentTimeMillis();
    }

    public void shoot ()
    {
        setSpeed(BULLET_SPEED);
        setDirection(direction);
        new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 600);
    }

    @Override
    protected Shape getOutline ()
    {
        // TODO Auto-generated method stub
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof BulletDestroyer)
        {
            Participant.expire(this);

            controller.alienBulletDestroyed();
        }

    }

    public long timeElapsed (Participant p)
    {
        endTimes = System.currentTimeMillis();
        long elapsed = endTimes - startTimes;
        return elapsed;
    }

    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("travelTime"))
        {
            Participant.expire(this);

            controller.alienBulletDestroyed();
        }
    }
}
