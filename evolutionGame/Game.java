package evolutionGame;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;


/**
 * A simulation of the Evolution game using threading
 * @author Eleanor Forrest
 */

public class Game {

    private static Scanner scanObject = new Scanner(System.in);

    /**
     * The executable main function to initialise and start the game
     * @param args - none
     */
    public static void main(String[] args){

        //Create a main log file
        createLog("game.txt");

        int numChildren = getNumberOfChildren();
        ArrayList<Child> children = new ArrayList<Child>();

        for (int childNo = 1; childNo <=numChildren; childNo++) {
            children.add(createChild(childNo));
        }

        Child.setChildren(children); //Set the list of children in the Child class

        System.out.println("Game starting");

        ArrayList<Thread> threads = new ArrayList<Thread>();
        //Start the game 
        for (Child child : children){
            Thread thread = new Thread(child);
            threads.add(thread);
            thread.start();
        }
        
        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("An error occured trying to join threads.");
                e.printStackTrace();
            }
        }

        for (Child child : children){
            child.writeLog("\nGame ended");
            child.writeLog("Final stage: " + child.getCurrentStage());
            if (child.getCurrentStage().equals("winner")){
                String winner = child.getName();
                System.out.println(String.format("The winner is %s!", winner));
            }
        }


    }


    /**
     * Ask the user to enter the number of children, verifying the input is a valid number
     * @return numChildren - the number of children playing the game. Between 6 and 100.
     */
    private static int getNumberOfChildren(){

        boolean validNumber = false;
        int numChildren = 0;
        while (!validNumber) {
            
            System.out.println("Enter the number of children:");

            try {
                numChildren = scanObject.nextInt();

                if (numChildren < 6) {
                    System.out.println("There must be at least six children to play this game.");
                }

                else if (numChildren > 100){
                    System.out.println("There cannot be more than 100 children playing this game.");
                }

                else {
                    validNumber = true;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a number.");
            }

            scanObject.nextLine(); //Clear the buffer
        }
        return numChildren;
            
    }

    /**
     * Ask the user to enter a name and create a new Child object and log file
     * @return child - the created child
     */
    private static Child createChild(int childNo){

        //Ask the user to enter a name
        
        System.out.println(String.format("Enter the name of child %d:", childNo));
        String name = scanObject.nextLine();
        

        //Create a log file for each child
        String filename = childNo + "_" + name + ".txt";
        createLog(filename);

        //Create a new child object
        Child child = new Child(childNo, name);
        return child;

    }

    /**
     * Create a new log file with the provided filename
     * @param filename - the name of the file to be created
     */
    private static void createLog(String filename){

        String pathname = "logs\\" + filename;
        try{
            File file = new File(pathname);
            file.createNewFile();
        }
        catch (IOException e) {
            System.out.println("An error occured trying to create a log file.");
            e.printStackTrace();
        } 
        

    }
    

}
