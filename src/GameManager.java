import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;

public class GameManager extends JPanel implements ActionListener {
    private static JFrame game; // Main game JFrame
    private JPanel chessPanel; // Panel where the game is played
    private JButton[][] positionGrid; // JButton grid
    private Board myBoard;
    private AI Hal; // the AI
    private int playerColor;
    private int currentColor;
    private int xcoord1;
    private int ycoord1;
    private boolean isSinglePlayer; // boolean for if single player

    public GameManager(){
        xcoord1 = -1;
        ycoord1 = -1;
        currentColor = 2;
        isSinglePlayer = true;
        myBoard = new Board();

        game = new JFrame();
        chessPanel = new JPanel(new BorderLayout());
        chessPanel.setBackground(Color.gray);

        createBoard();

        game.add(chessPanel);
        game.setUndecorated(true);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setExtendedState(JFrame.MAXIMIZED_BOTH);
        game.pack();
    }

    private void createBoard(){
        JPanel boardPanel = new JPanel(new BorderLayout());
        JPanel Panel = new JPanel();
        positionGrid = new JButton[8][8];
        Panel.setLayout(new GridBagLayout());
        for(int y = 0; y < 8;y++){
            for(int x = 0; x < 8; x++){
                GridBagConstraints constraints = new GridBagConstraints();
                positionGrid[x][y] = new JButton("("+x+","+y+")");
                positionGrid[x][y].setFont(new Font("Arial", Font.PLAIN, 20));
                constraints.fill = GridBagConstraints.BOTH;
                constraints.gridx = x;
                constraints.gridy = y;
                constraints.weightx = 1;
                constraints.weighty = 1;
                positionGrid[x][y].addActionListener(this); // sets action listener
                Panel.add(positionGrid[x][y], constraints);

            }
        }
        colorBoard();
        Panel.setPreferredSize(new Dimension(800,800));
        Panel.setBackground(Color.gray);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(120,100,100,100));

        boardPanel.add(Panel,BorderLayout.CENTER);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(120,100,100,100));
        boardPanel.setBackground(Color.gray);
        chessPanel.add(boardPanel,BorderLayout.CENTER);
    }

    private void colorBoard(){
        boolean temp = true;
        for(int y = 0; y < 8; y++){
            temp = !temp;
            for(int x = 0; x < 8; x++){
                if(myBoard.colorHelper(x,y) == 1){
                    positionGrid[x][y].setText(myBoard.idHelper(x,y));
                    positionGrid[x][y].setBackground(Color.BLACK);
                    positionGrid[x][y].setForeground(Color.WHITE);
                } else if(myBoard.colorHelper(x,y) == 2) {
                    positionGrid[x][y].setText(myBoard.idHelper(x,y));
                    positionGrid[x][y].setBackground(Color.WHITE);
                    positionGrid[x][y].setForeground(Color.BLACK);
                } else {
                    if (temp){
                        positionGrid[x][y].setBackground(Color.DARK_GRAY);
                        positionGrid[x][y].setForeground(Color.WHITE);
                    } else {
                        positionGrid[x][y].setBackground(Color.LIGHT_GRAY);
                        positionGrid[x][y].setForeground(Color.BLACK);
                    }
                    positionGrid[x][y].setText("");

                }
                temp = !temp;
            }
        }
    }



    public void multiPlayer(){
        isSinglePlayer = false;
        game.setVisible(true);
        currentColor = 2;
    }

    public void singlePlayer(int playerColor) {
        isSinglePlayer = true;
        game.setVisible(true);
        this.playerColor = playerColor;
        Hal = new AI(myBoard, Board.getOpponent(playerColor));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSinglePlayer){
            singlePlayerAction(e);
        } else {
            multiPlayerAction(e);

        }

    }

    private void multiPlayerAction(ActionEvent e){
        int tempx = -1;
        int tempy = -1;
        int dispx; // separate variable for the display
        int dispy; // separate variable for the display
        if(currentColor == 0){
            return; // for switching
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (e.getSource() == positionGrid[x][y]) {
                    tempx = x;
                    tempy = y;

                }
            }
        }

        dispx = tempx;
        dispy = tempy;

        if(currentColor == 1){
            tempx = 7 - tempx;
            tempy = 7 - tempy;
        }
        if (tempx != -1) {
            if (xcoord1 == -1 && myBoard.checkPiece(tempx, tempy, currentColor)) {
                xcoord1 = tempx;
                ycoord1 = tempy;
                positionGrid[dispx][dispy].setBackground(Color.BLUE);
            } else if (myBoard.move(xcoord1, ycoord1, tempx, tempy, currentColor)) {
                int temp = Board.getOpponent(currentColor);
                currentColor = 0;
                xcoord1 = -1;
                ycoord1 = -1;
                if(myBoard.isCheckMate(temp)){
                    System.out.println("checkmate");
                    exit(0);
                } else if (myBoard.canMove(temp)){
                    currentColor = temp;
                } else {
                    currentColor = Board.getOpponent(temp);
                }

                if (currentColor == 1){
                    colorBlack();
                } else if(currentColor == 2) {
                    colorBoard();
                }

            } else if (xcoord1 != -1 && ycoord1 != -1){
                positionGrid[xcoord1][ycoord1].setBackground(Color.WHITE);
                xcoord1 = -1;
                ycoord1 = -1;
            }
        }
    }

    private void colorBlack(){
        boolean temp = true;
        int a = 7;
        int b = 7;
        for(int y = 0; y < 8; y++){
            temp = !temp;
            for(int x = 0; x < 8; x++){
                if(myBoard.colorHelper(x,y) == 1){
                    positionGrid[a][b].setText(myBoard.idHelper(x,y));
                    positionGrid[a][b].setBackground(Color.BLACK);
                    positionGrid[a][b].setForeground(Color.WHITE);
                } else if(myBoard.colorHelper(x,y) == 2) {
                    positionGrid[a][b].setText(myBoard.idHelper(x,y));
                    positionGrid[a][b].setBackground(Color.WHITE);
                    positionGrid[a][b].setForeground(Color.BLACK);
                } else {
                    if (temp){
                        positionGrid[a][b].setBackground(Color.DARK_GRAY);
                        positionGrid[a][b].setForeground(Color.WHITE);
                    } else {
                        positionGrid[a][b].setBackground(Color.LIGHT_GRAY);
                        positionGrid[a][b].setForeground(Color.BLACK);
                    }
                    positionGrid[a][b].setText("");
                }
                temp = !temp;
                a--;
                if(a < 0){
                    a = 7;
                }
            }
            b--;
            if(b < 0){
                b = 7;
            }
        }
    }

    private void singlePlayerAction(ActionEvent e){
        int tempx = -1;
        int tempy = -1;
        if(currentColor == playerColor) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (e.getSource() == positionGrid[x][y]) {
                        tempx = x;
                        tempy = y;
                    }
                }
            }
            if (tempx != -1) {
                if (xcoord1 == -1 && myBoard.checkPiece(tempx, tempy, playerColor)) {
                    xcoord1 = tempx;
                    ycoord1 = tempy;
                    positionGrid[tempx][tempy].setBackground(Color.BLUE);
                } else if (myBoard.move(xcoord1, ycoord1, tempx, tempy, playerColor)) {
                    currentColor = Board.getOpponent(playerColor);
                    xcoord1 = -1;
                    ycoord1 = -1;
                    colorBoard();
                    if(myBoard.isCheckMate(currentColor)){
                        System.out.println("checkmate black");
                        exit(0);
                    } else if (myBoard.canMove(currentColor)){
                        AIMove();
                    } else {
                        currentColor = playerColor;
                        System.out.println("white go again");
                    }
                } else if (xcoord1 != -1 && ycoord1 != -1){
                    positionGrid[xcoord1][ycoord1].setBackground(Color.WHITE);
                    xcoord1 = -1;
                    ycoord1 = -1;
                }
            }
        }
    }

    private void AIMove(){
        Hal.updateBoard(myBoard);
        int x;
        int y;
        int targetx;
        int targety;
        String coords = Hal.makeMove();
        x = coords.charAt(0) - '0';
        y = coords.charAt(1) - '0';
        targetx = coords.charAt(2) - '0';
        targety = coords.charAt(3) - '0';
        myBoard.AIMove(x, y, targetx, targety, currentColor);
        Hal.updateBoard(myBoard);
        colorBoard();
        if(myBoard.isCheckMate(playerColor)){
            System.out.println("checkmate white");
            exit(0);
        } else if(myBoard.canMove(playerColor)) {
            currentColor = playerColor;
        } else {
            AIMove();
        }
    }


}
