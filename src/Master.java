
/**
 * This class initiates and controls the game. It will generate a random combination for the player to find and enter it in a Combination 
 * class object. It will also handle all inputs given by the user, by means of another Combination class object in the case of the input 
 * being a set of four colors. Furthermore, it will evaluate these two combinations by using a method of the Combination class. 
 * This evaluation will then be displayed.
 * 
 * @author Wim Van Damme, Antoine Vandermeersch, Jorrit van der Mynsbrugge 
 * @version V4
 */
import java.util.Random;
import java.awt.Color;

public class Master
{
    // A Rooster object 'rooster' which will visualise the game.
    private Rooster rooster;
    // A Colors object 'colors' containing Color objects.
    private Colors colors;
    // A Combination object containing the combination the user will have to try and find.
    private Combination teZoeken;
    // A Combination object containing the combination the user enters.
    private Combination inputCombination;
    // The size of the combinations.
    private static final int aantal=4;
    // Contains the input of the user in int form.
    private int[] handleInput;
    // The X-position of the next circle to be displayed.
    private int xPos;
    // The Y-position of your current guess. User inputs will be displayed from yPos = 1 through yPos = 13.
    private int yPos;
    // Determines whether the game has ended, either by winning or losing.
    private boolean stop;
    
    
     /**
	 * Constructor for objects of class Master
	 */
    public Master()
    {
        rooster = new Rooster(8,13,30,Color.gray);
        colors = new Colors();     
        inputCombination = new Combination(aantal);
        teZoeken = new Combination(aantal);
        handleInput = new int[aantal];
        stop = false;
        createRooster();
        teZoeken.setCombination(generateCombination());
        createInput();        
    }
    


	/**
	 * createRooster - Creates a lay-out for the rooster object and (re)sets the position variables.
	 * 
	 * @param      none
	 * @return     void
	 */
    private void createRooster()
    {
        rooster.clear();
        rooster.setStatusText("Een spel werd begonnen.");
        rooster.drawVerticalSeparator(3,Color.black);
        for (int i = 1; i < 12; i++) 
        {
            rooster.drawHorizontalSeparator(i,Color.black);
        }
        String[] buttons = {"Nieuw", "Geef op", "Stoppen","R","B","Gr","Ge","Ma"};
        // Invoeren van de knoppen in knopFuncties.
        rooster.setButtons(buttons);
        xPos = 0;
        yPos = 1;
    }
    
      
	/**
	 * generateCombination - Creates a Random object and generates a random combination.
	 * 
	 * @param      none
	 * @return     randomCombination - type: int[], size = aantal, contains a set of 'aantal' random numbers.
	 */
    private int[] generateCombination()
    {
        Random randomGenerator = new Random();
        int[] randomCombination = new int[aantal];
        
        for(int x=0; x<4; x++)
        {
            randomCombination[x] = randomGenerator.nextInt(5);        
        }
                 
        return randomCombination;
    }
    
    
	/**
	 * createInput - While the game has not ended, this method will wait for a user to click a button, and then pass on the identifier of
	 * the button to the appropriate method. If the game has ended, it will wait for a user to click a button, but this time only the 
	 * 'Nieuw' and 'Stoppen' buttons will respond.
	 * 
	 * @param      none
	 * @return     void
	 */
    private void createInput()
    {
        while (!stop) 
        {
            
            int index = rooster.getNextButtonClick();
      
            if (index == 0) 
            {
                newGame();
            }
            if (index == 1) 
            {
                endGame(false);
            }
            if (index == 2) 
            {
                rooster.dispose();
            }
            if (index > 2) 
            {
                handleInput(index-3);    
                
            }
        }
        
        while(stop)
        {
            int index = rooster.getNextButtonClick();
            
           
            if (index == 0) 
            {
                newGame();
            }
            if (index == 2) 
            {
                rooster.dispose();
            }
        }
    }
    
	/**
	 * newGame - Processes inputs of the button 'Nieuw'. Reinitialises the variable stop, cleans rooster's lay-out, calls a method to 
	 * create a new random combination and continues to method createInput.
	 * 
	 * @param      none
	 * @return     void
	 */    
	private void newGame()
    {
        stop = false;        
        createRooster();        
        // posities op begin zetten
        teZoeken.setCombination(generateCombination());
        createInput();
    }
        
	/**
	 * endGame - Regulates the output at the end of a game, depending on whether the player won. Makes 'stop' true to show the game has 
	 * ended. Calls a method to display the correct combination.
	 * 
	 * @param      win - boolean showing whether the user won or lost
	 * @return     void
	 */
    private void endGame(boolean win)
    {
        if(!win)
        {
            rooster.setStatusText("YOU LOSE .. Better luck tomorrow!");
        }
        else
        {
            rooster.setStatusText("YOU WIN .. CHEATER ?!");
        }
        stop = true; 
        displayResult(createColorCombination(teZoeken));
    }
        
	/**
	 * displayResult - Displays the correct combination.
	 * 
	 * @param      colorCombination - type: Color[], size: aantal, contains a color combination
	 * @return     void
	 */
    private void displayResult(Color[] colorCombination)
    {
        for(int x=0; x<aantal; x++)
        {
            rooster.drawCell(x,0,colorCombination[x]);
        }
    }
    
    
	/**
	 * createColorCombination - transforms combinations in int form to combinations in Color form
	 * 
	 * @param      whatCombination - type: Combination, contains a combination in int form
	 * @return     colorcollection - type: Color[], contains a combination in Color form
	 */
    private Color[] createColorCombination(Combination whatCombination)
    {
        Color[] colorcollection = new Color[aantal];
        int[] digitCombination = whatCombination.getCombination();
        for(int x =0; x<aantal; x++)
        {
            colorcollection[x] = colors.getSingleColor(digitCombination[x]);
        }
        return colorcollection;
    }
           
	/**
	 * handleInput - processes and visualises user input from the color buttons, calls a method to evaluate a filled combination.
	 * 
	 * @param      index - identifies the button pressed by the player
	 * @return     void
	 */
    private void handleInput(int index)
    {
        handleInput[xPos] = index;
        rooster.drawCell(xPos,yPos,colors.getSingleColor(index));        
        if(xPos==aantal-1)
        {
            inputCombination.setCombination(handleInput);
            evaluate(inputCombination.compare(teZoeken));
            xPos=0;
            yPos++;
        }
        else 
        {
            xPos++;
        }
    }
    
	/**
	 * evaluate - Checks whether the game has ended by winning or by losing. 
	 * 
	 * @param      aantalWitEnZwart - type: int[], slot 0 contains the number of black circles to be displayed, 
	 * slot 1 contains the number of white circles to be displayed
	 * @return     void
	 */        
    private void evaluate(int[] aantalWitEnZwart)
    {
        displayFeedback(aantalWitEnZwart);
        
        if(aantalWitEnZwart[0]==4)
        {
            endGame(true);
        }        
        else
        {            
            if(yPos == 12)
            {
                 endGame(false);
            
            }
        
        }
     }
        
	/**
	 * displayFeedback - Draws white and black circles at their respective positions.
	 * 
	 * @param      aantalWitEnZwart - type: int[], slot 0 contains the number of black circles to be displayed, 
	 * slot 1 contains the number of white circles to be displayed
	 * @return     void
	 */
    private void displayFeedback(int[] aantalWitEnZwart)
    {
        xPos++;
        for(int x =0; x<aantalWitEnZwart[0]; x++)
        {
            rooster.drawCell(xPos,yPos,Color.black);
            xPos++;            
        }
        for(int y =0; y<aantalWitEnZwart[1]; y++)
        {
            rooster.drawCell(xPos,yPos,Color.white);
            xPos++;            
        }        
    }
            

    
}
