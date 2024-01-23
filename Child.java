/**A Child object representing one individual who plays the game
 * @author Eleanor Forrest
 */
public class Child implements Runnable{
    

    //Attributes
    String name;
    String currentStage;


    public Child(String name) {
        this.name = name;
        this.currentStage = "egg";
    }

    public void run() {

    }

    /**
    * Evolve or devolve by one stage
    * @param win - True if this child won the rock paper scissors game and so should evolve, False if they should devolve
    */
    private void evolve(Boolean win){
        


    }
    

}
