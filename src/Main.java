import java.util.Scanner;

/**
 *  main driver class
 */

public class Main {

    public static void main(String[] args) {
        Board myBoard = new Board();
        Scanner input = new Scanner(System.in);
        int x;
        int y;
        int targetx;
        int targety;
        int currentColor = 2; // white
        boolean checkMate = false;
        AI cortana = new AI(myBoard, 1);
        myBoard.printBoard();
        while(!checkMate){
            cortana.updateBoard(myBoard);
            if(myBoard.canMove(currentColor)){

                if (currentColor == 2) {
                    System.out.println("White's Turn");

                    System.out.print("enter x:");
                    x = input.nextInt();
                    System.out.print("enter y:");
                    y = input.nextInt();
                    System.out.println("");

                    if (myBoard.printPiece(x, y, currentColor)) {

                        System.out.print("enter target x:");
                        targetx = input.nextInt();
                        System.out.print("enter target y:");
                        targety = input.nextInt();

                        if (myBoard.move(x, y, targetx, targety, currentColor)) {
                            currentColor = 1;
                        } else {
                            System.out.println("illegal move try again!");
                        }
                    }

                } else {
                    System.out.println("Black's Turn");
                    String coords = cortana.makeMove();
                    System.out.println(coords);
                    x = coords.charAt(0) - '0';
                    y = coords.charAt(1) - '0';
                    targetx = coords.charAt(2) - '0';
                    targety = coords.charAt(3) - '0';
                    myBoard.AIMove(x, y, targetx, targety, currentColor);
                    currentColor = 2;
                }
            } else {
                currentColor = Board.getOpponent(currentColor);
                if (!myBoard.canMove(currentColor)) {
                    System.out.println("neither player can move draw");
                    checkMate = true;
                }
            }
            myBoard.printBoard();
            if (myBoard.isCheckMate(currentColor)) {
                checkMate = true;
                if(currentColor == 1){
                    System.out.println("white wins");
                } else {
                    System.out.println("black wins");
                }
            }
        }

        System.out.println("thank you for playing");

    }

    public static void fourMoveCheckMate(Board myBoard){
        myBoard.move(4,6, 4, 5, 2);
        myBoard.move(5,7, 2, 4, 2);
        myBoard.move(3,7, 5,5,2);
        myBoard.move(5, 5, 5,1, 2);

    }


}

           