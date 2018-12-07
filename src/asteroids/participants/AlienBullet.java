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
    private double xD;
    private double yD;
    private boolean small;
    public AlienBullet (double x, double y, boolean small, Controller controller, AlienShip alienShip, Ship ship)
    {

        this.small = small;
        setPosition(x, y);

        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(-1, 0);

        poly.lineTo(-1, 1);
        poly.lineTo(0, 1);
        poly.lineTo(0, 0);

        poly.closePath();
        outline = poly;
        startTimes = System.currentTimeMillis();
        if (!small)
        {

            this.setSpeed(BULLET_SPEED);
            this.setDirection(RANDOM.nextDouble() * 2 * Math.PI);
            
        }
        else if (small)
        {
            this.xD = ship.getX() - x;
            this.yD = ship.getY() - y;
            // System.out.println(this.getDirection());


            // System.out.println(Math.asin(yD/(Math.sqrt((Math.pow(-xD, 2) + Math.pow(yD, 2))))));

            this.setSpeed(BULLET_SPEED);
            this.setDirection(Math.toRadians(Math.toDegrees((Math.atan2(-yD, xD))) + (RANDOM.nextInt(11) - 5)));
            

        }
        this.controller = controller;
        this.alienShip = alienShip;
    }

    public void shoot ()
    {
        this.setSpeed(BULLET_SPEED);
        
        this.setVelocity(this.getSpeed(), -this.getDirection());
        
        if (controller.difficulty == 1)
        {
        new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 600);
        }
        if (controller.difficulty == 0)
        {
        new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION );
        }
        if (controller.difficulty == 2)
        {
        new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 800);
        }
        if (controller.difficulty == 3)
        {
        new ParticipantCountdownTimer(this, "travelTime", BULLET_DURATION + 800);
        new ParticipantCountdownTimer(this,"followyTime", 200);
        }
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

    public void aimAgain(double x , double y)
    {
        if (this.small) {
        this.xD = x - this.getX();
        this.yD = y - this.getY();
        this.setSpeed(BULLET_SPEED);
        this.setDirection(Math.toRadians(Math.toDegrees((Math.atan2(-yD, xD)))));
        this.setVelocity(this.getSpeed(), -this.getDirection());
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
        if (payload.equals("followyTime"))
                {
            aimAgain(controller.followX,controller.followY);
            new ParticipantCountdownTimer(this,"followyTime", 200);
                }
    }
}
