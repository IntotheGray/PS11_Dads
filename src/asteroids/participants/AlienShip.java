package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.Random;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.ParticipantState;

public class AlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer, BulletDestroyer
{
    private Controller controller;
    private boolean small;
    private Shape outline;
    private double direction;
    private boolean nearbyShip;

    public AlienShip (boolean small, double x, double y, double direction, Controller controller)
    {
        new ParticipantCountdownTimer(this, "spawn", ALIEN_DELAY);

        this.controller = controller;
        this.small = small;
        this.direction = direction;
        setPosition(x, y);

        createAlienShipOutline(false);
        setDirection(direction);
        if (small)
        {
            setVelocity(10, this.getDirection());
        }
        else if (!small)
        {
            setVelocity(5, this.getDirection());
        }
        Random turny = new Random();
        new ParticipantCountdownTimer(this, "turn", turny.nextInt(500) + 1000);
        new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + 1000);
    }

    private void createAlienShipOutline (boolean small)
    {
        double scale = 1;
        Path2D.Double poly = new Path2D.Double();
        if (small)
        {
            scale = ALIENSHIP_SCALE[0];
        }
        if (!small)
        {
            scale = ALIENSHIP_SCALE[1];
        }
        poly.moveTo(10, 0);
        poly.lineTo(-10, 0);
        poly.lineTo(-20, -9);
        poly.lineTo(20, -9);
        poly.lineTo(-20, -9);
        poly.lineTo(-9, -18);
        poly.lineTo(9, -18);
        poly.lineTo(-9, -18);
        poly.lineTo(-5, -26);
        poly.lineTo(5, -26);
        poly.lineTo(10, -18);
        poly.lineTo(20, -9);
        poly.lineTo(10, 0);
        poly.closePath();
        outline = poly;

        poly.transform(AffineTransform.getScaleInstance(scale, scale));
    }

    public void nearShip (boolean nearbyShip)
    {
        this.nearbyShip = nearbyShip;

    }

    @Override
    protected Shape getOutline ()
    {

        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof AlienDestroyer)
        {
            Participant.expire(this);

            controller.alienDestroyed(getX(), getY());

        }
        // TODO Auto-generated method stub

    }

    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("turn"))
        {

            Random randomDirection = new Random();
            if (randomDirection.nextInt(3) == 0)
            {
                setVelocity(5, this.direction);
                setVelocity(5, this.getDirection() - Math.PI / 4);

            }
            else if (randomDirection.nextInt(3) == 1)
            {
                setVelocity(5, this.direction);
                setVelocity(5, this.getDirection());
            }
            else if (randomDirection.nextInt(3) == 2)
            {
                setVelocity(5, this.direction);
                setVelocity(5, this.getDirection() + Math.PI / 4);
            }
            createAlienShipOutline(false);

            Random turny = new Random();
            new ParticipantCountdownTimer(this, "turn", turny.nextInt(1000) + 500);
        }
        else if (payload.equals("shoot"))
        {
            if (nearbyShip)
            {
                controller.placeAlienBullet();
            }
            Random turny = new Random();

            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + 2000);

        }
    }
}
