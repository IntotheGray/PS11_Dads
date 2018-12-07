package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Iterator;
import javax.swing.*;
import asteroids.game.Controller;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;

    /** Game controller */
    private Controller controller;
    
    /** Boolean to keep track of if Enhanced*/
    private boolean isEnhanced = false;

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    /**
     * Set Enhanced
     */
    public void setEnhanced ()
    {
        isEnhanced = true;
    }
    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw each participant in its proper place
        Iterator<Participant> iter = controller.getParticipants();
        while (iter.hasNext())
        {
            iter.next().draw(g);
        }

        // Draw the legend across the middle of the panel
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);
        // Draw Score
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        FontMetrics fm = g2.getFontMetrics();
        int height = fm.getHeight();
        g2.drawString(Integer.toString(controller.getScore()), height, height);
        g2.drawString(Integer.toString(controller.getLevel()), (750 - height), height);

        
        Graphics2D g3 = (Graphics2D) graphics;
        
        g3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        
        
        
        
        int lives = controller.getLives();
        
        if (lives == 3)
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(30, 70);
            poly.lineTo(42, 110);
            poly.lineTo(40, 102);
            poly.lineTo(20, 102);
            poly.lineTo(18, 110);
            poly.closePath();
            Shape outline = poly;
            g.draw(outline);
            
            Path2D.Double poly1 = new Path2D.Double();
            poly1.moveTo(60, 70);
            poly1.lineTo(72, 110);
            poly1.lineTo(70, 102);
            poly1.lineTo(50, 102);
            poly1.lineTo(48, 110);
            poly1.closePath();
            Shape outline1 = poly1;
            g.draw(outline1);
            
            Path2D.Double poly2 = new Path2D.Double();
            poly2.moveTo(90, 70);
            poly2.lineTo(102, 110);
            poly2.lineTo(100, 102);
            poly2.lineTo(80, 102);
            poly2.lineTo(78, 110);
            poly2.closePath();
            Shape outline2 = poly2;
            g.draw(outline2);
        }
        
        if (lives == 2)
        {
            
            
            Path2D.Double poly1 = new Path2D.Double();
            poly1.moveTo(60, 70);
            poly1.lineTo(72, 110);
            poly1.lineTo(70, 102);
            poly1.lineTo(50, 102);
            poly1.lineTo(48, 110);
            poly1.closePath();
            Shape outline1 = poly1;
            g.draw(outline1);
            
            Path2D.Double poly2 = new Path2D.Double();
            poly2.moveTo(90, 70);
            poly2.lineTo(102, 110);
            poly2.lineTo(100, 102);
            poly2.lineTo(80, 102);
            poly2.lineTo(78, 110);
            poly2.closePath();
            Shape outline2 = poly2;
            g.draw(outline2);
        }
        
        if (lives == 1)
        {
            
            
            Path2D.Double poly1 = new Path2D.Double();
            poly1.moveTo(60, 70);
            poly1.lineTo(72, 110);
            poly1.lineTo(70, 102);
            poly1.lineTo(50, 102);
            poly1.lineTo(48, 110);
            poly1.closePath();
            Shape outline1 = poly1;
            g.draw(outline1);
            
            
        }
    }

}
