package evolutionGame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**A Child object representing one individual who plays the game
 * @author Eleanor Forrest
 */
public class Child implements Runnable{
    

    //Attributes
    public boolean running = true;
    private int childNo;
    String name;
    private String currentStage;
    public boolean currentlyPlaying;
    static ArrayList<Child> children;


    public Child(int childNo, String name) {
        this.childNo = childNo;
        this.name = name;
        this.currentStage = "egg";
        this.currentlyPlaying = false;
        writeLog("Child : "+ name + "\n");
        writeLog("Initial stage: egg \n");
        writeGameLog("Child created: " + name + "\n");
    }

    @Override
    public void run() {
        //Play until interrupted or this child becomes a winner
        while (running && !currentStage.equals("winner")){
            findOpponent();
        }
        
        //Exit correctly
        if (currentStage.equals("winner")){
            //Interrupt the other children
            for (Child child : children){
                if (!child.equals(this)){
                    child.running = false;
                }
            }

            try {
                Thread.sleep(1000); //Wait for other threads to finish
            } catch (InterruptedException e) {
                System.out.println("An unexpected error occured while waiting.");
                e.printStackTrace();
            } 

            //Update logs
            writeGameLog("\nWinner : " + name);
            writeGameLog("End of game");
                
        }

    }


    /**
     * Interrupt the thread and update logs when another child has won
     */
    public void interrupt(){
        writeLog("Interrupted");
        writeLog("Final stage: " + currentStage);
        running = false;
    }

    /**
     * Attempt to find another child of the same stage
     * @param children - the list of children to search
     */
    private void findOpponent() {
        
        Random random = new Random();
        int randomIndex = random.nextInt(children.size());

        if (randomIndex + 1 != childNo){

            Child otherChild = children.get(randomIndex);

            if (otherChild.getCurrentStage().equals(currentStage)){

                if (!otherChild.currentlyPlaying && !currentlyPlaying){
                    currentlyPlaying = true;
                    otherChild.currentlyPlaying = true;
                   
                    //Play rock paper scissors
                    playRockPaperScissors(otherChild);
                    
                    currentlyPlaying = false;
                    otherChild.currentlyPlaying = false;
                }
                
            }


        }
        //The method ends if contact was unsuccessful, so a new child can try
    }


  
    /**
     * Play a game of Rock Paper Scissors and update logs accordingly
     * @param otherChild - the opponent
     */
    private void playRockPaperScissors(Child otherChild) {
        //Update logs at the start of the game
        writeLog("Started playing with : " + otherChild.name);
        otherChild.writeLog("Started playing with : " + name);

        Random random = new Random();
        String[] moves = {"rock", "paper", "scissors"};
        Boolean winner = false; //keep playing until one child wins
        while (!winner){
            String move = moves[random.nextInt(3)];
            writeLog("Used move: " + move);
            String otherMove = moves[random.nextInt(3)];
            otherChild.writeLog("Used move: " + otherMove);

            //Compare the results
            if (move.equals(otherMove)){
                writeLog("Draw");
                otherChild.writeLog("Draw");
            }
            else if (move.equals("rock") && otherMove.equals("scissors") || move.equals("paper") && otherMove.equals("rock") || move.equals("scissors") && otherMove.equals("paper")){
                writeLog("Win");
                otherChild.writeLog("Lose");
                winner = true;
                evolve(true);
                otherChild.evolve(false);
            }
            else {
                writeLog("Lose");
                otherChild.writeLog("Win");
                winner = true;
                evolve(false);
                otherChild.evolve(true);
            }


        }

    }

    /**
    * Evolve or devolve by one stage
    * @param win - True if this child won the rock paper scissors game and so should evolve, False if they should devolve
    */
    public void evolve(Boolean win){
        if (win) {
            switch (currentStage) {
                case "egg":
                    currentStage = "chicken";
                    writeLog("Evolved into : chicken");
                    writeGameLog(name + " evolved into : chicken");
                    break;
                case "chicken":
                    currentStage = "eagle";
                    writeLog("Evolved into : eagle");
                    writeGameLog(name + " evolved into : eagle");
                    break;
                case "eagle":
                    currentStage = "t-rex";
                    writeLog("Evolved into : t-rex");
                    writeGameLog(name + " evolved into : t-rex");
                    break;
                case "t-rex":
                    currentStage = "dragon";
                    writeLog("Evolved into : dragon");
                    writeGameLog(name + " evolved into : dragon");
                    break;
                case "dragon":
                    currentStage = "winner";
                    writeLog("Became the winner");
                    writeGameLog(name + " became the winner");
                    break;
                case "winner":
                    throw new IllegalStateException("Cannot evolve from winner");
            }
        }
        else {
            switch (currentStage) {
                case "egg":
                    writeLog("Cannot devolve from egg");
                    writeGameLog(name + " cannot devolve from egg");
                    break;
                case "chicken":
                    currentStage = "egg";
                    writeLog("Devolved into : egg");
                    writeGameLog(name + " devolved into : egg");
                    break;
                case "eagle":
                    currentStage = "chicken";
                    writeLog("Devolved into : chicken");
                    writeGameLog(name + " devolved into : chicken");
                    break;
                case "t-rex":
                    currentStage = "eagle";
                    writeLog("Devolved into : eagle");
                    writeGameLog(name + " devolved into : eagle");
                    break;
                case "dragon":
                    currentStage = "t-rex";
                    writeLog("Devolved into : t-rex");
                    writeGameLog(name + " devolved into : t-rex");
                    break;
                case "winner":
                    throw new IllegalStateException("Cannot devolve from winner");
            }
        }

    }
    
    /**
    * Write a message to the child's log file
    * @param message - the message to be written
    */
    public void writeLog(String message) {
        //Work out the log path
        String pathname = ".\\logs\\" +childNo + "_" + name + ".txt";

        try{
            FileWriter writer = new FileWriter(pathname, true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to the child log file.");
            e.printStackTrace();
        }
    }

    /**
     * Write a message to the game log file
     * @param message - the message to be written
     */
    public void writeGameLog(String message) {
        //Work out the log path
        String pathname = ".\\logs\\game.txt";

        try{
            FileWriter writer = new FileWriter(pathname, true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to the game log file.");
            e.printStackTrace();
        }
    }

    /**
    * Get the current stage of the child (like asking a child what stage they are at)
    * @return currentStage - the current stage of the child
    */
    public String getCurrentStage() {
        return currentStage;
    }

    /**
     * Get the name of the child
     * @return name - the name of the child
     */
    public String getName() {
        return name;
    }

    /**
     * Make the list of children known to each child
     * @param children - the list of children
     */
    public static void setChildren(ArrayList<Child> children) {
        Child.children = children;
    }
}
