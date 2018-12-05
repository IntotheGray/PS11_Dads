package asteroids.game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import asteroids.participants.AlienBullet;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;

/**
 * Keeps track of the Participants, their motions, and their collisions.
 */
public class ParticipantState
{
    
    /** The participants (asteroids, ships, etc.) that are involved in the game */
    private LinkedList<Participant> participants;

    /** Participants that are waiting to be added to the game */
    private Set<Participant> pendingAdds;

    /**
     * Creates an empty ParticipantState.
     */
    public ParticipantState ()
    {
        // No participants at the start
        participants = new LinkedList<Participant>();
        pendingAdds = new HashSet<Participant>();
    }

    /**
     * Clears out the state.
     */
    public void clear ()
    {
        pendingAdds.clear();
        for (Participant p : participants)
        {
            Participant.expire(p);
        }
        participants.clear();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pendingAdds.add(p);
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return participants.iterator();
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    public int countAsteroids ()
    {
        int count = 0;
        for (Participant p : participants)
        {
            if (p instanceof Asteroid && !p.isExpired())
            {
                count++;
            }
        }
        for (Participant p : pendingAdds)
        {
            if (p instanceof Asteroid && !p.isExpired())
            {
                count++;
            }
        }
        return count;
    }

    public int bulletCount ()
    {
        int count = 0;
        for (Participant p: participants)
            if (p instanceof Bullet && !p.isExpired() && !(p instanceof AlienBullet))
            {
                count++;
            }
            
        return count;
    }
    public boolean aliensExist ()
    {
        boolean existing = false;
        for (Participant p: participants)
        if(p instanceof AlienShip && !p.isExpired())
        {
            existing = true;
        }
        return existing;
    }
    public boolean aliensCanShoot ()
    {
        boolean nearbyShip = false;
        for (Participant p: participants)
            if (p instanceof AlienShip && !p.isExpired())
            {
                for (Participant q: participants)
                    if (q instanceof Ship && !q.isExpired())
                    {
                      if (((Math.sqrt(Math.pow(q.getX()- p.getX(), 2) + Math.pow(q.getY()-p.getY(), 2))) <= 250 + 100) || 
                              ((Math.sqrt(Math.pow(((750 - q.getX())+ (0 + p.getX())), 2) + Math.pow(q.getY()-p.getY(), 2)) <= 250 + 100)) ||
                              ((Math.sqrt(Math.pow(((750 - p.getX())+ (0 + q.getX())), 2) + Math.pow(q.getY()-p.getY(), 2)) <= 250 + 100)) ||
                              ((Math.sqrt(Math.pow(((750 - q.getY())+ (0 + p.getY())), 2) + Math.pow(q.getX()-p.getX(), 2)) <= 250 + 100)) ||
                              ((Math.sqrt(Math.pow(((750 - p.getY())+ (0 + q.getY())), 2) + Math.pow(q.getX()-p.getX(), 2)) <= 250 + 100)) ||
                              ((Math.sqrt(Math.pow(((750 - q.getX())+ (0 + p.getX())), 2) + Math.pow((0 + q.getY())+(750-p.getY()), 2))) <=250 + 100) || 
                              ((Math.sqrt(Math.pow(((750 - p.getX())+ (0 + q.getX())), 2) + Math.pow((0 + p.getY())+(750-q.getY()), 2)))) <= 250 + 100) 
                      {
                          nearbyShip =true;
                      }
                    }

            }
        return nearbyShip;
    }
    
    /**
     * Moves each of the active participants to simulate the passage of time.
     */
    public void moveParticipants ()
    {
        // Move all of the active participants
        for (Participant p : participants)
        {
            if (!p.isExpired())
            {
                p.move();
            }
        }

        // If there have been any collisions, deal with them. This may result
        // in new participants being added or old ones expiring. We save those
        // changes until after all of the collisions have been processed.
        checkForCollisions();

        // Deal with pending adds and expirations
        completeAddsAndRemoves();
        

    }

    /**
     * Completes any adds and removes that have been requested.
     */
    private void completeAddsAndRemoves ()
    {
        // Note: These updates are saved up and done later to avoid modifying
        // the participants list while it is being iterated over
        for (Participant p : pendingAdds)
        {
            participants.add(p);
        }
        pendingAdds.clear();

        Iterator<Participant> iter = participants.iterator();
        while (iter.hasNext())
        {
            Participant p = iter.next();
            if (p.isExpired())
            {
                iter.remove();
            }
        }
    }

    /**
     * Compares each pair of elements to detect collisions, then notifies all listeners of any found. Deals with each
     * pair only once. Never deals with (p1,p2) and then again with (p2,p1).
     */
    private void checkForCollisions ()
    {
        for (Participant p1 : participants)
        {
            if (!p1.isExpired())
            {
                Iterator<Participant> iter = participants.descendingIterator();
                while (iter.hasNext())
                {
                    Participant p2 = iter.next();
                    if (p1 == p2)
                        break;
                    if (!p2.isExpired() && p1.overlaps(p2))
                    {
                        p1.collidedWith(p2);
                        p2.collidedWith(p1);
                    }
                    if (p1.isExpired())
                        break;
                }
            }
        }
    }
    
}
