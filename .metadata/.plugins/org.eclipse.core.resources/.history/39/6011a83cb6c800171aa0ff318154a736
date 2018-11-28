package mobiles1;

import javax.swing.*;
import static mobiles1.Mobile.*;
import java.awt.*;

/**
 * A simple Swing application for visualizing Mobiles.
 */
@SuppressWarnings("serial")
public class MobileViewer extends JFrame
{
    // Number of MobileViewers that have been opened
    private static int windowCount = 0;
    
    /**
     * Creates a visualization of m.  
     */
    public MobileViewer(Mobile m, String title)
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setLocation(windowCount * 50, windowCount * 50);
        windowCount++;
        setTitle(title);
        MobilePanel panel = new MobilePanel(m);
        setContentPane(panel);
        pack();
        setVisible(true);
    }
}

/**
 * JPanel that can display a Mobile rendering
 */
@SuppressWarnings("serial")
class MobilePanel extends JPanel
{
    // The Mobile being displayed
    private Mobile mobile;
    
    /**
     * Creates a MobilePanel to display m.
     */
    public MobilePanel (Mobile m)
    {
        mobile = m;
    }
    
    /**
     * Draws the Mobile on g.
     */
    @Override
    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        mobile.display(g2, getWidth()/2.0, Mobile.TOP);
        FontMetrics fm = g2.getFontMetrics();
        int height = fm.getHeight();
        g2.drawString("Weight: " + mobile.weight(), (int) GAP, height);
        g2.drawString("IsBalanced: " + mobile.isBalanced(), (int) GAP, 2*height);
        g2.drawString("Depth: " + mobile.depth(), (int) GAP, 3*height);
        g2.drawString("BobCount: " + mobile.bobCount(), (int) GAP, 4*height);
        g2.drawString("RodCount: " + mobile.rodCount(), (int) GAP, 5*height);
        g2.drawString("HeaviestBob: " + mobile.heaviestBob(), (int) GAP, 6*height);
        g2.drawString("LongestRod: " + mobile.longestRod(), (int) GAP, 7*height);
    }
}
