
/**
 * This class is essential for making cominations, accessible for the player and for
 * the computer. Class Master will combine its randomGenerator with this class. All
 * combinations are checked here (player combination and computer combination).
 * 
 * @author Wim Van Damme, Antoine Vandermeersch, Jorrit van der Mynsbrugge 
 * @version V4
 */



public class Combination
{
    // An object of type int to specify the size of combinations allowed, etc.
    private int aantal;
    // An Array object of type int, to set new combinations.
    private int[] combination;
    
    /**
     * Constructor for objects of class Combination.
     */
    public Combination(int aantal)
    {
        combination = new int[aantal];
        this.aantal = aantal;
    }

    /**
     * setCombination - Creates a new array containing numbers, specified by a int[] parameter
     * 
     * @param       inputCombination - type: int[],size: aantal, data to be stored
     * @return      void
     */
    public void setCombination(int[] inputCombination)
    {
        for(int x = 0; x<4; x++)
        {
            combination[x]=inputCombination[x];
        }
    }
    
    /**
     * getCombination - Returns the combination of the object.
     * 
     * @param       none
     * @return      combination - type: int[], size: aantal, returns stored (else <null>) data
     */
    public int[] getCombination()
    {
        return combination;
    }
    
    /**
     * compare - Compares the combination assigned to this object with a combination of
     * another object of class Combination. This method contains an important local variable
     * of type int[], called "aantalWitEnZwart". Index 0 contains a number, representing the
     * total of black pegs, whereas index 1 refers to the number of white pegs.
     * 
     * @param       teZoeken2 - type: Combination, size: aantal
     * @return      aantalWitEnZwart - type: int[], size: 2, contains the evaluation of the 2 combinations
     */
    public int[] compare(Combination teZoeken2)
    {
        int[] aantalWitEnZwart = new int[2];
        int[] teZoekenCopy = new int[aantal];
        
        // 2 copies are being made, so none of the original combinations get erased (internally).
        for(int x =0; x<aantal; x++)
        {        
        teZoekenCopy[x] = teZoeken2.getCombination()[x];
        }
        int[] combinationCopy = new int[aantal];
                
        for(int x =0; x<aantal; x++)
        {        
        combinationCopy[x] = combination[x];
        }
                
        int[] tellingTeZoeken = new int[5];
        int[] tellingCombination = new int[5];
        
        // First loop to check for possible black pegs
        for(int x=0; x<aantal; x++)
        {
            if(combinationCopy[x]==teZoekenCopy[x])
            {
                aantalWitEnZwart[0]++;
                combinationCopy[x]=-1;
                teZoekenCopy[x]=-1;
            }
        }
        
        // Second loop to check for possible white pegs.
        for(int x=0; x<aantal; x++)
        {
            for(int y=0; y<aantal; y++)
            {
                if(combinationCopy[x]==teZoekenCopy[y]&&combinationCopy[x]!=-1)
                {
                    aantalWitEnZwart[1]++;
                    combinationCopy[x]=-1;
                    teZoekenCopy[y]=-1;
                }
            }
        }
        return aantalWitEnZwart;
    }    
}
