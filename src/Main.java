import java.util.Scanner;

/**
 *  main driver class takes command line input for multiplayer single player or making the AI's fight each other
 */

public class Main {


    public static void main(String[] args) throws InterruptedException {
        GameManager myGame = new GameManager();
        Scanner input = new Scanner(System.in);
        System.out.println("enter the number of players");


        int i = input.nextInt();
        if(i == 1){
            myGame.singlePlayer(2);
        } else if (i == 2) {
            myGame.multiPlayer();
        } else {
            myGame.AIDuel();
        }
    }



}

