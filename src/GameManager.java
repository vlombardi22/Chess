import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

/**
 UI class
 To run the game the player clicks on the piece they want to move and the space they want to move it too.
 The original coordinates are stored in xcoord1 and ycoord1.
 */

public class GameManager extends JPanel implements ActionListener {
    private static JFrame game; // Main game JFrame
    private JPanel chessPanel; // Panel where the game is played
    private JButton[][] positionGrid; // JButton grid
    private Board myBoard = new Board();
    private AI Hal; // the AI
    private int playerColor;
    private int currentColor;
    private int xcoord1; // x coordinate of the piece you want to move
    private int ycoord1; // y coordinate of the piece you want to move
    private Font font;
    private JButton exitButton;
    private JLabel label1;
    private JLabel label2;
    private boolean isSinglePlayer; // boolean for if single player
    private JButton queen;
    private JButton rook;
    private JButton knight;
    private JButton bishop;
    private boolean promoting;


    public GameManager(){
        reset();
        isSinglePlayer = true;
        currentColor = 2;
        exitButton = new JButton();
        queen = new JButton();
        knight = new JButton();
        bishop = new JButton();
        rook = new JButton();
        font = new Font("Arial", Font.PLAIN, 15);

        game = new JFrame();
        chessPanel = new JPanel(new BorderLayout());
        chessPanel.setBackground(Color.gray);

        createBoard();
        createMessage();
        addButtons();

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
                positionGrid[x][y].setFont(font);
                constraints.fill = GridBagConstraints.BOTH;
                constraints.gridx = x;
                constraints.gridy = y;
                constraints.weightx = 1;
                constraints.weighty = 1;
                positionGrid[x][y].addActionListener(this); // sets action listener
                Panel.add(positionGrid[x][y], constraints);

            }
        }
        colorWhite();
        Panel.setPreferredSize(new Dimension(600,600));
        Panel.setBackground(Color.gray);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(120,100,20,20));
        boardPanel.add(Panel,BorderLayout.NORTH);

        boardPanel.setBorder(BorderFactory.createEmptyBorder(120,100,20,20));
        boardPanel.setBackground(Color.gray);
        chessPanel.add(boardPanel,BorderLayout.CENTER);
    }

    // this flips the board for black in multiplayer
    private void colorBlack(){
        boolean pattern = true; // this boolean is used to make the checker board pattern on unoccupied spaces
        int px = 7; // position grid x coordinate
        int py = 7; // position grid y coordinate
        for(int y = 0; y < 8; y++){
            pattern = !pattern;
            for(int x = 0; x < 8; x++){
                if(myBoard.colorHelper(x,y) == 1){
                    positionGrid[px][py].setText(myBoard.idHelper(x,y));
                    positionGrid[px][py].setBackground(Color.BLACK);
                    positionGrid[px][py].setForeground(Color.WHITE);
                } else if(myBoard.colorHelper(x,y) == 2) {
                    positionGrid[px][py].setText(myBoard.idHelper(x,y));
                    positionGrid[px][py].setBackground(Color.WHITE);
                    positionGrid[px][py].setForeground(Color.BLACK);
                } else {
                    if (pattern){
                        positionGrid[px][py].setBackground(Color.DARK_GRAY);
                        positionGrid[px][py].setForeground(Color.WHITE);
                    } else {
                        positionGrid[px][py].setBackground(Color.LIGHT_GRAY);
                        positionGrid[px][py].setForeground(Color.BLACK);
                    }
                    positionGrid[px][py].setText("");
                }
                pattern = !pattern;
                px--;
                if(px < 0){
                    px = 7;
                }
            }
            py--;
            if(py < 0){
                py = 7;
            }
        }
    }

    // color white is the default color
    private void colorWhite(){
        boolean pattern = true; // this boolean is used to make the checker board pattern on unoccupied spaces
        int wcount = 0;
        int bcount = 0;

        for(int y = 0; y < 8; y++){
            pattern = !pattern;
            for(int x = 0; x < 8; x++){
                if(myBoard.colorHelper(x,y) == 1){
                    positionGrid[x][y].setText(myBoard.idHelper(x,y));
                    positionGrid[x][y].setBackground(Color.BLACK);
                    positionGrid[x][y].setForeground(Color.WHITE);
                    bcount++;
                } else if(myBoard.colorHelper(x,y) == 2) {
                    positionGrid[x][y].setText(myBoard.idHelper(x,y));
                    positionGrid[x][y].setBackground(Color.WHITE);
                    positionGrid[x][y].setForeground(Color.BLACK);
                    wcount++;
                } else {
                    if (pattern){
                        positionGrid[x][y].setBackground(Color.DARK_GRAY);
                        positionGrid[x][y].setForeground(Color.WHITE);
                    } else {
                        positionGrid[x][y].setBackground(Color.LIGHT_GRAY);
                        positionGrid[x][y].setForeground(Color.BLACK);
                    }
                    positionGrid[x][y].setText("--"); // makes it easier to see

                }
                pattern = !pattern;
            }
        }
        myBoard.setbCount(bcount);
        myBoard.setwCount(wcount);
    }

    private void colorBoard(int color){
        if(color == 1){
            colorBlack();
        } else {
            colorWhite();
        }
    }

    public void multiPlayer(){
        isSinglePlayer = false;
        game.setVisible(true);

    }

    public void AIDuel() throws InterruptedException {
        AI white = new AI(myBoard, 2, 3);
        AI black = new AI(myBoard, 1, 4);
        game.setVisible(true);

        for(int y = 0; y < 8;y++) {
            for (int x = 0; x < 8; x++) {
                positionGrid[x][y].setEnabled(false);
            }
        }
        boolean freeze = false;
        boolean checkMate = false;
        while (!checkMate) {
            int x;
            int y;
            int targetx;
            int targety;
            String coords;

            if (currentColor == 2) {
                label1.setText("White's turn");
                coords = white.makeMove();
            } else {
                label1.setText("Black's turn");
                coords = black.makeMove();
            }

            if (coords.equals("9999")) {// checks for bad moves
                label1.setText("AI has issues");
                currentColor = Board.getOpponent(currentColor);
                if (freeze) {
                    checkMate = true;
                    label2.setText("alert");
                } else {
                    freeze = true;
                }
            } else {
                x = coords.charAt(0) - '0';
                y = coords.charAt(1) - '0';
                targetx = coords.charAt(2) - '0';
                targety = coords.charAt(3) - '0';
                myBoard.AIMove(x, y, targetx, targety, currentColor);
                freeze = false;
                black.updateBoard(myBoard);
                white.updateBoard(myBoard);
                colorWhite();
                if (myBoard.isCheck(currentColor)) {
                    label2.setText("check");
                } else {
                    label2.setText("");
                }
                currentColor = Board.getOpponent(currentColor);
                if (myBoard.isCheckMate(currentColor)) {
                    label2.setText("checkmate");
                    checkMate = true;
                } else if (!myBoard.canMove(currentColor)) {
                    currentColor = Board.getOpponent(currentColor);
                    if (!myBoard.canMove(currentColor)){
                        label2.setText("cats");
                        checkMate = true;
                    }
                }

            }
            TimeUnit.SECONDS.sleep(1);
        }

    }

    public void singlePlayer(int playerColor) {
        isSinglePlayer = true;
        game.setVisible(true);
        if(playerColor == 1 || playerColor == 2) {
            this.playerColor = playerColor;
        } else {
            this.playerColor = 2;
        }
        Hal = new AI(myBoard, Board.getOpponent(playerColor), 3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int tempx = -1;
        int tempy = -1;

        if (e.getSource() == exitButton) {
            System.out.println("goodbye");
            exit(0);
        } else if (promoting) {
            if (e.getSource() == queen) {
                myBoard.upgrade(xcoord1, ycoord1, 'q');
                hideButtons();
            } else if (e.getSource() == rook) {
                myBoard.upgrade(xcoord1, ycoord1, 'r');
                hideButtons();
            } else if (e.getSource() == bishop) {
                myBoard.upgrade(xcoord1, ycoord1, 'b');
                hideButtons();
            } else if (e.getSource() == knight) {
                myBoard.upgrade(xcoord1, ycoord1, 'n');
                hideButtons();
            }
            if (!promoting) {
                if (isSinglePlayer) {
                    singlePlayerHelper();
                } else {
                    multiPlayerHelper();
                }
            }

        } else {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (e.getSource() == positionGrid[x][y]) {
                        tempx = x;
                        tempy = y;
                    }
                }
            }
            if(tempx != -1) {
                if (isSinglePlayer) {
                    singlePlayerAction(tempx, tempy);
                } else {
                    multiPlayerAction(tempx, tempy);
                }
            }
        }
    }

    // short hand for resetting piece selection
    private void reset(){
        xcoord1 = -1;
        ycoord1 = -1;
    }

    // multiplayer action handler
    private void multiPlayerAction(int x, int y){
        int tempx;
        int tempy;

        if(currentColor == 0){ // locks the board during a process
            return;
        } else if(currentColor == 1){
            tempx = 7 - x;
            tempy = 7 - y;
        } else {
            tempx = x;
            tempy = y;
        }

        if (xcoord1 == -1 && myBoard.checkPiece(tempx, tempy, currentColor)) {
            xcoord1 = tempx;
            ycoord1 = tempy;
            positionGrid[x][y].setBackground(Color.BLUE);
        } else if (myBoard.move(xcoord1, ycoord1, tempx, tempy, currentColor)) {
            if(myBoard.canPromote(tempx,tempy, currentColor)){
                showButtons();
                xcoord1 = tempx;
                ycoord1 = tempy;
            } else {
                multiPlayerHelper();
            }
        } else if (xcoord1 != -1 && ycoord1 != -1){
            if(currentColor == 2) {
                positionGrid[xcoord1][ycoord1].setBackground(Color.WHITE);
            }else{
                positionGrid[xcoord1][ycoord1].setBackground(Color.BLACK);
            }
            reset();
        }
    }

    // single player action handler
    private void singlePlayerAction(int tempx, int tempy){
        if(currentColor == playerColor) {
            if (xcoord1 == -1 && myBoard.checkPiece(tempx, tempy, playerColor)) {
                xcoord1 = tempx;
                ycoord1 = tempy;
                positionGrid[tempx][tempy].setBackground(Color.BLUE);
            } else if (myBoard.move(xcoord1, ycoord1, tempx, tempy, playerColor)) {
                if(myBoard.canPromote(tempx,tempy, playerColor)) {
                    showButtons();
                    xcoord1 = tempx;
                    ycoord1 = tempy;
                } else {
                    singlePlayerHelper();
                }
            } else if (xcoord1 != -1 && ycoord1 != -1){
                positionGrid[xcoord1][ycoord1].setBackground(Color.WHITE);
                reset();
            }

        }
    }

    // this needed to be called in multiple places
    private void multiPlayerHelper(){
        int temp = Board.getOpponent(currentColor);
        currentColor = 0;
        reset();
        if(myBoard.isCheckMate(temp)){
            label2.setText("Checkmate");
        } else if (myBoard.canMove(temp)){
            currentColor = temp;
        } else {
            currentColor = Board.getOpponent(temp);
        }
        if (currentColor == 1){
            label1.setText("Black's Turn");
            colorBlack();
        } else if(currentColor == 2) {
            label1.setText("White's Turn");
            colorWhite();
        }
        updateLabel2(currentColor);
    }

    // this section of code was part of singleplayer action
    private void singlePlayerHelper(){
        currentColor = Board.getOpponent(playerColor);
        reset();
        colorBoard(playerColor);

        if (myBoard.isCheckMate(currentColor)) {
            label2.setText("checkmate");
        } else if (myBoard.canMove(currentColor)) {
            updateLabel2(currentColor);
            AIMove();
        } else {
            currentColor = playerColor;
            updateLabel2(currentColor);
        }
    }

    // Method for moving the AI
    private void AIMove(){
        Hal.updateBoard(myBoard);
        int x;
        int y;
        int targetx;
        int targety;
        String coords = Hal.makeMove();
        if(coords.equals("9999")){ // Part of a bug this is included for debugging
            label1.setText("White's turn AI has issues");
            currentColor = playerColor;
            return;
        }
        x = coords.charAt(0) - '0';
        y = coords.charAt(1) - '0';
        targetx = coords.charAt(2) - '0';
        targety = coords.charAt(3) - '0';
        myBoard.AIMove(x, y, targetx, targety, currentColor);
        Hal.updateBoard(myBoard);
        colorBoard(playerColor);
        if(myBoard.isCheckMate(playerColor)){
            label2.setText("checkmate");
        } else if(myBoard.canMove(playerColor)) {
            label1.setText("White's turn");
            currentColor = playerColor;
            updateLabel2(currentColor);
        } else {
            label1.setText("Black's turn");
            AIMove();
        }
    }

    // updates the second message
    private void updateLabel2(int color){
        if(color == 0){
            return;
        }
        if(myBoard.isCheck(1)){
            label2.setText("Black is in check");
        } else if (myBoard.isCheck(2)){
            label2.setText("White is in check");
        } else {
            label2.setText("");
        }
    }

    private void hideButtons(){
        promoting = false;
        rook.setVisible(false);
        knight.setVisible(false);
        bishop.setVisible(false);
        queen.setVisible(false);
    }

    private void showButtons(){
        promoting = true;
        rook.setVisible(true);
        knight.setVisible(true);
        bishop.setVisible(true);
        queen.setVisible(true);
    }

    private void addButtons(){
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.gray);

        exitButton.addActionListener(this);
        exitButton.setPreferredSize(new Dimension(200,75));
        exitButton.setFont(font);
        exitButton.setBackground(Color.gray);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        exitButton.setForeground(Color.BLACK);
        exitButton.setText("EXIT");

        queen.addActionListener(this);
        queen.setPreferredSize(new Dimension(200,75));
        queen.setFont(font);
        queen.setBackground(Color.gray);
        queen.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        queen.setForeground(Color.BLACK);
        queen.setText("QUEEN");

        rook.addActionListener(this);
        rook.setPreferredSize(new Dimension(200,75));
        rook.setFont(font);
        rook.setBackground(Color.gray);
        rook.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        rook.setForeground(Color.BLACK);
        rook.setText("ROOK");

        bishop.addActionListener(this);
        bishop.setPreferredSize(new Dimension(200,75));
        bishop.setFont(font);
        bishop.setBackground(Color.gray);
        bishop.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        bishop.setForeground(Color.BLACK);
        bishop.setText("BISHOP");

        knight.addActionListener(this);
        knight.setPreferredSize(new Dimension(200,75));
        knight.setFont(font);
        knight.setBackground(Color.gray);
        knight.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        knight.setForeground(Color.BLACK);
        knight.setText("KNIGHT");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;

        buttonPanel.add(exitButton, BorderLayout.LINE_END);
        panel.add(queen,constraints);
        panel.add(rook, constraints);
        panel.add(bishop,constraints);
        panel.add(knight, constraints);
        panel.setBackground(Color.gray);
        buttonPanel.add(panel, BorderLayout.LINE_START);
        hideButtons();
        chessPanel.add(buttonPanel,BorderLayout.SOUTH);
    }

    private void createMessage(){
        JPanel boardPanel = new JPanel(new BorderLayout());
        JPanel Panel = new JPanel();

        label1 = new JLabel("White's turn");
        label2 = new JLabel("");
        label1.setFont(font);
        label1.setForeground(Color.BLACK);
        label2.setFont(font);
        label2.setForeground(Color.BLACK);
        Panel.add(label1);
        Panel.add(label2, BorderLayout.CENTER);
        Panel.setPreferredSize(new Dimension(400,400));
        Panel.setBackground(Color.lightGray);
        boardPanel.add(Panel,BorderLayout.NORTH);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(70,50,50,50));
        boardPanel.setBackground(Color.gray);
        chessPanel.add(boardPanel,BorderLayout.LINE_END);
    }



}
