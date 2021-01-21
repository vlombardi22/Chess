import java.util.Scanner;

/**
 *  main driver class
 */

// TODO list
    // test kings coordinates
    // improve kings coordinates
    // fix playing as black in single player
    // comment code
    // test AI
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

