import java.util.Scanner;

/**
 *  main driver class
 */

public class Main {

    public static void main(String[] args) {
        GameManager myGame = new GameManager();
        Scanner input = new Scanner(System.in);
        System.out.println("enter the number of players");
        int i = input.nextInt();
        if(i == 1){
            myGame.singlePlayer(2);
        } else {
            myGame.multiPlayer();
        }




    }






}

