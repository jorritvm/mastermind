import java.awt.Color;

/**
 * The class Colors will determine the needed colors in MasterMind. The Master class will
 * make use of Colors to decode its combinations and player input. This way, int types will
 * refer to corresponding colors.
 * 
 * @author Wim Van Damme, Antoine Vandermeersch, Jorrit van der Mynsbrugge 
 * @version V4
 */
public class Colors
{
    // An Array object "colorCollection" to store the 5 predefined colors.
    private Color[] colorCollection;

    /**
     * Constructor for objects of class Colors.
     */
    public Colors()
    {
        colorCollection = new Color[5];
        colorCollection[0] = Color.red;
        colorCollection[1] = Color.blue;
        colorCollection[2] = Color.green;
        colorCollection[3] = Color.yellow;
        colorCollection[4] = Color.magenta;
    }



    /**
     * getSingleColor - returns a color when given a valid index (0-4).
     * 
     * @param       index - type: int
     * @return      getSingleColor - type: Color
     */
    public Color getSingleColor(int index)
    {
        Color getSingleColor = (Color) colorCollection[index];
        return getSingleColor;
    }
    
    /**
     * get SizeColorCollection - returns the size of the array colorCollection, which has been
     * made in the constructor.
     * 
     *@param        none
     *@return       getSizeColorCollection - type: Color[], size: 5, contains all the color objects
     */
    public int getSizeColorCollection()
    {
        return colorCollection.length;
    }
}