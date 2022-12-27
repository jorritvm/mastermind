
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * Een object van de klasse Rooster stelt een scherm voor waarop
 * bolletjes getekend kunnen worden volgens een rooster-patroon.
 */
public class Rooster extends Frame {
    
    /*
     * Enkele private attributen:
     */
    /*
     * - width, height: breedte/hoogte van het rooster
     * - thickness: dikte van de afstand tussen elk hokje
     * - size: grootte (in pixels) van elk hokje
     */
    private int width;
    private int height;
    private int thickness;
    private int size;
    
    /*
     * - totalWidth, totalHeight: echte hoogte en breedte van het scherm
     */
    private int totalWidth;
    private int totalHeight;
    
    /*
     * - backgroundColor: achtergrond kleur
     * - img, canvas: hulpvariabelen die voor het beheer van het
     *   scherm dienen.
     */
    private Color backgroundColor;
    private Image image;
    private Canvas canvas;
    private TextField statusBar;
    private Panel buttonsPanel;
    private LinkedList buttonClickQueue = new LinkedList();
    
    /*
     * Enkele private methodes
     */
    private synchronized void waitForImage() {
        while (image == null){
            try {
                wait();
            } catch(InterruptedException e) {};
        }
    }
    
    private int xconvert(int x){
        return x;
    }
    
    private int yconvert(int y){
        return (height - 1) - y;
    }
    private int scaled(int pos) {
        return pos*(size+thickness) + thickness;
    }
    
    /**
     * Maakt een rooster aan.
     *
     * Parameters:
     *   width:
     *     het aantal vakjes dat men wenst in horizontale richting
     * 	   (breedte). Dit moet groter zijn dan 0.
     * 	 	    
     *   height:
     *     het aantal vakjes dat men wenst in verticale richting
     *     (hoogte). Dit moet groter zijn dan 0.
     *     
     *   size:
     *     de lengte van een zijde van een vakje (in pixels)
     *     
     *   background:
     *     de gewenste achtergrond-kleur
     *     bv.: new java.awt.Color(192,192,192)
     */
    public Rooster(int width, int height, int size, Color background) {
        // Maak het venster met in de titel de afmetingen van het venster.
        super("Rooster grootte " + width + " x " + height);
        this.width = width; this.height = height;
        this.thickness = size/5; this.size = size;
        this.backgroundColor = background;
        
        totalWidth = width * (size + thickness) + thickness;
        totalHeight = height * (size + thickness) + thickness;
        setSize(totalWidth, totalHeight);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                Insets insets = getInsets();
                setSize(totalWidth + insets.left + insets.right,
                totalHeight + insets.top + insets.bottom);
                Image tmpImage = createImage(totalWidth, totalHeight);
                Graphics g = tmpImage.getGraphics();
                g.setColor(backgroundColor);
                g.fillRect(0,0,totalWidth,totalHeight);
                g.dispose();
                add(canvas = new Canvas() {
                    public void update(Graphics g) {
                        paint(g);
                    }
                    public void paint(Graphics g) {
                        waitForImage();
                        g.drawImage(image, (getWidth() - totalWidth) / 2, (getHeight() - totalHeight) / 2, this);
                    }
                });
                Panel bottomPanel = new Panel(new BorderLayout());
                buttonsPanel = new Panel();
                bottomPanel.add(buttonsPanel);
                statusBar = new TextField();
                statusBar.setEditable(false);
                bottomPanel.add(statusBar, BorderLayout.SOUTH);
                add(bottomPanel, BorderLayout.SOUTH);
                canvas.setSize(totalWidth, totalHeight);
                pack();
                synchronized(Rooster.this) {
                    image = tmpImage;
                    Rooster.this.notifyAll();
                }
            }
        });
        setVisible(true);
    }
    
    /**
     * Retourneert de index van de ingedrukte knop.
     * Deze methode wacht tot er een knop ingedrukt wordt.
     */
    public int getNextButtonClick() {
        try {
            synchronized (buttonClickQueue) {
                while (buttonClickQueue.isEmpty()) {
                    buttonClickQueue.wait();
                }
                return ((Integer)buttonClickQueue.removeFirst()).intValue();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Stel de tekst in die weergegeven wordt onderaan het rooster.
     */
    public void setStatusText(String text) {
        statusBar.setText(text);
    }
    
    /**
     * Geef op welke knoppen weergegeven moeten worden.
     * 
     * Parameters:
     * 
     * buttons:
     * De labels van de knoppen.
     * Bv.: new String[] {"Ja", "Nee"}
     */
    public void setButtons(final String[] buttons) {
        if (EventQueue.isDispatchThread()) {
            buttonsPanel.removeAll();
            for (int i = 0; i < buttons.length; i++) {
                 final int index = i;
                 Button button = new Button(buttons[i]);
                 button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        synchronized (buttonClickQueue)
                        {
                            buttonClickQueue.add(new Integer(index));
                            buttonClickQueue.notifyAll();
                        }
                    }
                });
                buttonsPanel.add(button);
            }
            buttonClickQueue.clear();
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        setButtons(buttons);
                        pack();
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Vult de cel op de gegeven positie met een gegeven kleur.
     * 
     * Bemerk dat de coordinaat (0,0) overeenstemt met het
     * meest linksonder gelegen vakje. Het meest linksboven gelegen
     * vakje komt overeen met de coordinaten (breedte-1, hoogte-1).
     */
    public void drawCell(int xpos, int ypos, Color kleur) {
        xpos = scaled(xconvert(xpos));
        ypos = scaled(yconvert(ypos));
        waitForImage();
        Graphics g = image.getGraphics();
        g.setColor(kleur);
        g.fillArc(xpos,ypos,size,size, 0, 360);
        g.dispose();
        canvas.repaint();
    }
    
    /**
     * Teken een vertikale lijn tussen 2 kolommen van het rooster.
     * Hierbij geef je de x-positie aan van de cel waarachter de lijn
     * getekend moet worden.
     */
    public void drawVerticalSeparator(int xpos, Color kleur){
        int x = scaled(xconvert(xpos + 1));
        waitForImage();
        Graphics g = image.getGraphics();
        g.setColor(kleur);
        g.fillRect(x - (thickness / 2), 0, 1, totalHeight);
        g.dispose();
        canvas.repaint();
    }
    
    /**
     * Teken een horizontale lijn tussen 2 rijen van het rooster.
     * Hierbij geef je de y-positie aan van de cel waarachter de lijn
     * getekend moet worden.
     */
    public void drawHorizontalSeparator(int ypos, Color kleur){
        int y = scaled(yconvert(ypos));
        waitForImage();
        Graphics g = image.getGraphics();
        g.setColor(kleur);
        g.fillRect(0, y - (thickness / 2), totalWidth, 1);
        g.dispose();
        canvas.repaint();
    }
    
    /**
     * Wis het scherm.
     */
    public void clear(){
        waitForImage();
        Graphics g = image.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, totalWidth, totalHeight);
        g.dispose();
        canvas.repaint();
    }
    
    /**
     * Sluit het rooster-venster.
     */
    public void dispose() {
        super.dispose();
    }
    
}
