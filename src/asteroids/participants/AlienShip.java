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
    private int health = 1;
    private int velo;
    private int interval;
    public AlienShip (boolean small, double x, double y, double direction, Controller controller)
    {
        new ParticipantCountdownTimer(this, "spawn", ALIEN_DELAY);

        this.controller = controller;
        this.small = small;
        this.direction = direction;
        setPosition(x, y);

        createAlienShipOutline(small);
        setDirection(direction);
        if (this.small)
        {
            
            if (controller.difficulty == 1)
            {
                velo = 10;
            this.health = 1;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 0)
            {this.health = 1;
            velo = 8;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 2)
            {this.health = 1;
            velo = 11;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 3)
            {this.health = 1;
            velo = 12;
            setVelocity(velo , this.getDirection());
            }
        }
        else if (!this.small )
        {
            if (controller.difficulty == 0)
            {
            this.health = 1;
            this .velo = 3;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 2)
            {
            this.health = 2;
            this.velo = 5;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 3)
            {
            this.health = 2;
            this.velo = 7;
            setVelocity(velo, this.getDirection());
            }
            if (controller.difficulty == 1)
            {
            this.health = 1;
            velo = 5;
            setVelocity(velo, this.getDirection());
            }
        }
        Random turny = new Random();
        new ParticipantCountdownTimer(this, "turn", turny.nextInt(500) + 1000);
        if (controller.difficulty == 2)
        {
            interval = 500;
            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + interval);
        }
        if (controller.difficulty == 0)
        {
            interval = 1500;
            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) +interval);
        }
        if (controller.difficulty == 3)
        {
            interval = 0;
            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + interval);
        }
        if (controller.difficulty == 1)
        {
            interval = 1000;
            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + interval);
        }
    }

    private void createAlienShipOutline (boolean small)
    {

        Path2D.Double poly = new Path2D.Double();

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

        double scale;
        if (small)
        {
            scale = ALIENSHIP_SCALE[0];
        }
        else
        {
            scale = ALIENSHIP_SCALE[1];
        }
        poly.transform(AffineTransform.getScaleInstance(scale, scale));
        outline = poly;

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
            this.health =this.health -1;
            if (health <= 0)
            {
            Participant.expire(this);

            controller.alienDestroyed(getX(), getY(), small);
            }

        }
        // TODO Auto-generated method stub

    }

    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("turn"))
        {

            if (!small)
            {
                Random randomDirection = new Random();
                if (randomDirection.nextInt(3) == 0)
                {

                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection() - Math.PI / 4);

                }
                else if (randomDirection.nextInt(3) == 1)
                {
                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection());
                }
                else if (randomDirection.nextInt(3) == 2)
                {
                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection() + Math.PI / 4);
                }
            }
            if (small)
            {
                Random randomDirection = new Random();
                if (randomDirection.nextInt(3) == 0)
                {

                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection() - Math.PI / 4);

                }
                else if (randomDirection.nextInt(3) == 1)
                {
                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection());
                }
                else if (randomDirection.nextInt(3) == 2)
                {
                    setVelocity(velo, this.direction);
                    setVelocity(velo, this.getDirection() + Math.PI / 4);
                }
            }
            createAlienShipOutline(small);

            Random turny = new Random();
            new ParticipantCountdownTimer(this, "turn", turny.nextInt(1000) + 500);
        }
        else if (payload.equals("shoot"))
        {
            if (nearbyShip)
            {
                controller.placeAlienBullet(small);
            }

            Random turny = new Random();
            new ParticipantCountdownTimer(this, "shoot", turny.nextInt(1500) + interval);

        }
    }
}
