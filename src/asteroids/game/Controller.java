package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import asteroids.participants.AlienBullet;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;
import asteroids.participants.Debris;
import javax.sound.sampled.*;
import java.util.TimerTask;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{
    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** The bullet (if one is active) */
    private Bullet bullet;
    /** Debris when something is destroyed */
    private Debris debris;
    /** Bullets for the alien ship */
    private AlienBullet alienBullet;
    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;
    /** True/False to keep track of user controls */
    private boolean turningRight;
    private boolean turningLeft;
    private boolean accelerating;
    private boolean firing;

    /** Keeps track of how many bullets the ship has fired */
    private int numBullets = 0;

    /** True when the alien ship is close to the ship */
    private boolean shipNearAlien;
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    /** The game display */
    private Display display;

    public boolean testMode;

    private controllerCountdownTimer alienTimes;
    /**
     * The alien ship if one is active
     */
    private AlienShip alienShip;
    /**
     * keep track of ship score
     */
    private int score;

    /** Current level */
    private int level;

    public boolean small;
    /**
     * all clips need for sounds
     */

    private Clip bangAlienShipClip;
    private Clip bangLargeClip;
    private Clip bangMediumClip;
    private Clip bangShipClip;
    private Clip bangSmallClip;
    private Clip beat1Clip;
    private Clip beat2Clip;
    private Clip fireClip;
    private Clip saucerBigClip;
    private Clip thrustClip;
    private Clip saucerSmallClip;
    
    /** Beat Delay*/
    private int beatDelay = 1000;
    
    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller (boolean testMode)
    {
        this.small = false;
        this.testMode = testMode;
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();

        // Set up all sounds

        fireClip = createClip("/sounds/fire.wav");
        saucerSmallClip = createClip("/sounds/saucerSmall.wav");
        bangAlienShipClip = createClip("/sounds/bangAlienShip.wav");
        bangLargeClip = createClip("/sounds/bangLarge.wav");
        bangMediumClip = createClip("/sounds/bangMedium.wav");
        bangShipClip = createClip("/sounds/bangShip.wav");
        bangSmallClip = createClip("/sounds/bangSmall.wav");
        beat1Clip = createClip("/sounds/beat1.wav");
        beat2Clip = createClip("/sounds/beat2.wav");
        saucerBigClip = createClip("/sounds/saucerBig.wav");
        thrustClip = createClip("/sounds/thrust.wav");

    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids(0);
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids (int level)
    {
        if (testMode == false)
        {

            if (alienTimes !=null)
            {
            alienTimes.restart();
            }
            addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
            addParticipant(new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET, 3, this));
            addParticipant(new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));
            addParticipant(new Asteroid(0, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));

            if (level > 1)
            {
                for (int i = 1; i < level; i++)
                {
                    if ((i % 2) != 0)
                    {
                        addParticipant(new Asteroid(0, 2, SIZE - (EDGE_OFFSET * 2), SIZE - EDGE_OFFSET, 3, this));
                    }
                    else
                    {
                        addParticipant(new Asteroid(0, 2, (EDGE_OFFSET * 2), EDGE_OFFSET, 3, this));
                    }
                }
            }
        }
    }

    private void placeBullet ()
    {
        if (pstate.bulletCount() < BULLET_LIMIT)
        {

            bullet = new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation(), this, ship);
            addParticipant(bullet);

        }

    }

    public void placeAlienBullet (boolean small)
    {
        if (small)
        {
            alienBullet = new AlienBullet(alienShip.getX(), alienShip.getY(), small, this, alienShip, ship);
        }
        if (!small)
        {
            alienBullet = new AlienBullet(alienShip.getX(), alienShip.getY(), small, this, alienShip, ship);
        }
        addParticipant(alienBullet);
        alienBullet.shoot();
    }

    public void placeAlienShip ()
    {
        if (level > 2)
        {
            this.small = true;
        }
        Random quarter = new Random();
        int whichSide = quarter.nextInt(2);
        if (whichSide == 0)
        {
            alienShip = new AlienShip(small, 0, quarter.nextInt(SIZE + 1), 0, this);
        }
        if (whichSide == 1)
        {
            alienShip = new AlienShip(small, SIZE, quarter.nextInt(SIZE + 1), Math.PI, this);
        }

        addParticipant(alienShip);
    }

    /**
     * Given xy place debris
     * 
     * @param x
     * @param y
     */
    private void placeDebris (boolean isShip, double x, double y)
    {
        addParticipant(new Debris(false, x, y, this));
        addParticipant(new Debris(false, x, y, this));
        addParticipant(new Debris(false, x, y, this));
        addParticipant(new Debris(false, x, y, this));
        addParticipant(new Debris(false, x, y, this));
        addParticipant(new Debris(false, x, y, this));

        if (isShip)
        {
            addParticipant(new Debris(true, x, y, this));
            addParticipant(new Debris(true, x, y, this));
        }
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        if (alienTimes != null)
        {
            alienTimes.expire();
        }
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(0);

        // Place the ship
        placeShip();

        // Reset statistics
        lives = 3;
        score = 0;
        level = 1;

        
        if (testMode)
        {
            new controllerCountdownTimer(RANDOM.nextInt(500), this);
        }
        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();

    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        thrustClip.stop();
        placeDebris(true, ship.getX(), ship.getY());
        bangShipClip.loop(1);
        // Null out the ship
        ship = null;

        // Ensure all booleans are false
        turningRight = false;
        turningLeft = false;
        accelerating = false;
        firing = false;

        // Decrement lives
        lives--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);

    }

    /**
     * An asteroid has been destroyed Grabs the size of this asteroid to add smaller asteroids if required Also needs
     * the location of the asteroid to create new ones
     */
    public void asteroidDestroyed (int size, double xVal, double yVal)
    {
        placeDebris(false, xVal, yVal);
        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0 && alienShip == null)
        {
            scheduleTransition(END_DELAY);
            level = level + 1;

        }

        if (size == 2)
        {
            bangLargeClip.loop(1);
            score = score + ASTEROID_SCORE[2];

            // Pick random speeds for new asteroids
            Random rand = new Random();
            int speedy = rand.nextInt(2) + 3;
            addParticipant(new Asteroid(1, 1, xVal, yVal, speedy, this));
            int speeder = rand.nextInt(2) + 3;
            addParticipant(new Asteroid(2, 1, xVal, yVal, speeder, this));
        }

        if (size == 1)
        {
            bangMediumClip.loop(1);
            score = score + ASTEROID_SCORE[1];

            // Pick random speed for asteroids
            Random rand = new Random();
            int speedy = rand.nextInt(3) + 5;
            addParticipant(new Asteroid(0, 0, xVal, yVal, speedy, this));
            int speeder = rand.nextInt(3) + 5;
            addParticipant(new Asteroid(1, 0, xVal, yVal, speeder, this));
        }
        if (size == 0)
        {
            bangSmallClip.loop(1);
            score = score + ASTEROID_SCORE[0];
        }
    }

    /**
     * A bullet has been destroyed
     */
    public void alienDestroyed (double x, double y, boolean small)
    {

        placeDebris(false, alienShip.getX(), alienShip.getY());
        bangAlienShipClip.loop(1);
        alienShip = null;
        noAliens();

        if (small)
        {
            score = score + ALIENSHIP_SCORE[0];
        }
        else
        {
        score = score + ALIENSHIP_SCORE[1];
        }

        // Test if a new level should start
        if (pstate.countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
            level = level + 1;
        }

    }

    public void bulletDestroyed ()
    {

    }

    public void alienBulletDestroyed ()
    {

    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
            playBeats(1);
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {

            if (turningRight == true && ship != null)
            {
                ship.turnRight();
            }
            if (turningLeft == true && ship != null)
            {
                ship.turnLeft();
            }
            if (accelerating == true && ship != null)
            {
                ship.accelerate();
            }
            if (firing == true && ship != null)
            {
                placeBullet();
                bullet.shoot();
            }

            // It may be time to make a game transition

            performTransition();
            if (level > 1 && (alienTimes == null))
            {
                controllerCountdownTimer alienTimes = new controllerCountdownTimer(RANDOM.nextInt(5000) + 5000, this);
                this.alienTimes = alienTimes;
            }

            // Move the participants to their new locations
            pstate.moveParticipants();

            shipNearAlien = pstate.aliensCanShoot();

            if (alienShip != null)
            {
                alienShip.nearShip(shipNearAlien);
                saucerBigClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {
                saucerBigClip.stop();
            }

            // Play background music

            // Refresh screen
            display.refresh();
        }
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }
            if (ship == null && lives > 0)
            {
                placeShip();
            }
            if (pstate.countAsteroids() == 0 && alienShip == null)
            {
                placeAsteroids(level);
            }
        }
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            // ship.turnRight();
            turningRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            // ship.turnLeft();
            turningLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            // ship.accelerate();

            accelerating = true;
            thrustClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && ship != null)
        {
            // placeBullet();
            // bullet.shoot();
            firing = true;
            fireClip.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            firing = true;
            fireClip.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            // ship.turnRight();
            turningRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            // ship.turnLeft();
            turningLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            // ship.accelerate();

            accelerating = true;
            thrustClip.loop(Clip.LOOP_CONTINUOUSLY);

        }
        if (e.getKeyCode() == KeyEvent.VK_S && ship != null)
        {
            // placeBullet();
            // bullet.shoot();
            firing = true;
            fireClip.start();

        }

    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            // ship.turnRight();
            turningRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            // ship.turnLeft();
            turningLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            // ship.accelerate();

            accelerating = false;
            thrustClip.stop();
            thrustClip.setFramePosition(0);

        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && ship != null)
        {
            // placeBullet();
            // bullet.shoot();
            firing = false;
            fireClip.stop();
            fireClip.setFramePosition(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            firing = false;
            fireClip.stop();
            fireClip.setFramePosition(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            // ship.turnRight();
            turningRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            // ship.turnLeft();
            turningLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            // ship.accelerate();

            accelerating = false;
            thrustClip.stop();
            thrustClip.setFramePosition(0);

        }
        if (e.getKeyCode() == KeyEvent.VK_S && ship != null)
        {
            // placeBullet();
            // bullet.shoot();
            firing = false;
            fireClip.stop();
            fireClip.setFramePosition(0);
        }
    }

    public void noAliens ()
    {
        alienTimes.restart();
    }

    public void countdownComplete ()
    {
        placeAlienShip();
    }

    /**
     * Called to start background beat
     */
    public void playBeats (int whatBeat)
    {
        if (lives > 0)
        {
            new BeatCountdownTimer(whatBeat, beatDelay, this);
            if (beatDelay > 200)
            {
                beatDelay = beatDelay - 10;
            }  
        }
        else
        {
            beat1Clip.stop();
            beat2Clip.stop();
            return;
        }
    }

    /**
     * Called from beatCountdownTimer to play first beat
     */
    public void beat1Countdown ()
    {
        beat2Clip.stop();
        beat2Clip.setFramePosition(0);
        beat1Clip.start();
        playBeats(2);
    }

    /**
     * Called from beatCountdownTimer to play second beat
     */
    public void beat2Countdown ()
    {
        beat1Clip.stop();
        beat1Clip.setFramePosition(0);
        beat2Clip.start();
        playBeats(1);
    }

    /**
     * returns the current score
     * 
     */
    public int getScore ()
    {
        return score;
    }

    /**
     * Returns the number of lives left
     */
    public int getLives ()
    {
        return lives;
    }

    /**
     * Return the level
     */
    public int getLevel ()
    {
        return level;
    }

    /**
     * Returns boolean accelerating
     */
    public boolean getAcc ()
    {
        return accelerating;
    }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }
}
