package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import java.util.Random;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, AlienDestroyer
{
    /** The outline of the ship */
    private Shape outline;
    
    /** The outline of ship when accelerating*/
    private Shape outlineAcc;
    
    /** Game controller */
    private Controller controller;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;

        //Outline for when accelerating
        Path2D.Double poly2 = new Path2D.Double();
        poly2.moveTo(21, 0);
        poly2.lineTo(-21, 12);
        poly2.lineTo(-14, 10);
        poly2.lineTo(-14, 5);
        poly2.lineTo(-23, 0);
        poly2.lineTo(-14, -5);
        poly2.lineTo(-14, -10);
        poly2.lineTo(-21, -12);
        poly2.closePath();
        outlineAcc = poly2;
        
        // Schedule an acceleration in two seconds
        new ParticipantCountdownTimer(this, "move", 2000);
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }
    
    /**
     * Returns the outline of ship
     */
    @Override
    protected Shape getOutline ()
    {
        if (controller.getAcc())
        {
            //Used to simulate the flicker when accelerating shown on demo
            Random rand = new Random();
            int n = rand.nextInt(2) + 1;
            if (n == 1)
            {
                return outlineAcc;
            }
            else
            {
                return outline;
            }
        }
        else
        {
            return outline; 
        }
        
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        /*
         * if (payload.equals("move")) { accelerate(); new ParticipantCountdownTimer(this, "move", 200); }
         */
    }

}
