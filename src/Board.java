
public class Board {
    private Space[][] board;
    private Space[][] oldBoard;

    // constructor;
    public Board() {
        board = new Space[8][8];
        oldBoard = new Space[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y] = new Space("" + x + y);
                oldBoard[x][y] = new Space("" + x + y);
            }
        }
        GUISetup();
        backup();
    }

    private void GUISetup(){
        for (int i = 0; i < 8; i++) { // place pawns
            board[i][1].setPiece("P", 'p', 1);
            board[i][6].setPiece("P", 'p', 2);
        }

        board[4][7].setPiece("K", 'k', 2);
        board[4][0].setPiece("K", 'k', 1);

        board[3][7].setPiece("Q", 'q', 2);
        board[3][0].setPiece("Q", 'q', 1);

        board[2][7].setPiece("B", 'b', 2);
        board[5][7].setPiece("B", 'b', 2);
        board[2][0].setPiece("B", 'b', 1);
        board[5][0].setPiece("B", 'b', 1);

        board[6][7].setPiece("N", 'n', 2);
        board[1][7].setPiece("N", 'n', 2);
        board[6][0].setPiece("N", 'n', 1);
        board[1][0].setPiece("N", 'n', 1);

        board[0][7].setPiece("R", 'r', 2);
        board[7][7].setPiece("R", 'r', 2);
        board[0][0].setPiece("R", 'r', 1);
        board[7][0].setPiece("R", 'r', 1);
    }

    private void commandLineSetUp(){
        for (int i = 0; i < 8; i++) { // place pawns
            board[i][1].setPiece("P", 'p', 1);
            board[i][6].setPiece("W", 'p', 2);
        }

        board[4][7].setPiece("K", 'k', 2);
        board[4][0].setPiece("k", 'k', 1);

        board[3][7].setPiece("Q", 'q', 2);
        board[3][0].setPiece("q", 'q', 1);

        board[2][7].setPiece("B", 'b', 2);
        board[5][7].setPiece("B", 'b', 2);
        board[2][0].setPiece("b", 'b', 1);
        board[5][0].setPiece("b", 'b', 1);

        board[6][7].setPiece("N", 'n', 2);
        board[1][7].setPiece("N", 'n', 2);
        board[6][0].setPiece("n", 'n', 1);
        board[1][0].setPiece("n", 'n', 1);


        board[0][7].setPiece("R", 'r', 2);
        board[7][7].setPiece("R", 'r', 2);
        board[0][0].setPiece("r", 'r', 1);
        board[7][0].setPiece("r", 'r', 1);

    }

    // --public--
    // prints the selected piece
    public boolean checkPiece(int x, int y, int color) {
        if (checkInput(x, y)) {
            if (board[x][y].getColor() != color) {
                return false;
            }
        }

        return true;
    }

    public String idHelper(int x, int y){
        return board[x][y].getId();
    }

    public int colorHelper(int x, int y){
        return board[x][y].getColor();
    }

    // checks if color is in check
    public boolean isCheckMate(int color) {
        int[] xy = findKing(color, board);
        if(!isSafe(xy[0], xy[1], color, board)){
            //System.out.println("you are in check!!");
            if (kingEscape(xy[0], xy[1], color)) {
                return false;
            } else if (canBlock(color)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    // validates user input and checks for repeats
    public boolean move(int x, int y, int targetx, int targety, int currentColor) {
        if (x == targetx && y == targety) { // prevents repeats
            return false;
        }

        if ((checkInput(x, y)) && (checkInput(targetx, targety))) {
            if (board[x][y].getColor() != currentColor) {
                //System.out.println("Please select your own color");
                return false;
            } else if (board[x][y].getType() == 'k') {
                if (moveKing(x, y, targetx, targety, currentColor, board)) {
                    makeMove(x, y, targetx, targety, board);
                    backup();
                    return true; // can not inadvertently put a king in check while moving the king
                }

            } else if (board[targetx][targety].getColor() == currentColor) {
                //System.out.println("Do not target you own pieces unless castling");
                return false;
            } else if (board[x][y].getType() == 'p') {
                if (movePawn(x, y, targetx, targety, currentColor, board)) {
                    makeMove(x, y, targetx, targety, board);
                    if (checkKing(currentColor, board)) {
                        promote(targetx, targety, currentColor, board);
                        backup();
                        return true;
                    } else {
                        reset();
                    }
                }

            } else if (board[x][y].getType() == 'r') {
                if (moveRook(x, y, targetx, targety, board)) {
                    makeMove(x, y, targetx, targety, board);
                    if (checkKing(currentColor, board)) {
                        backup();
                        return true;
                    } else {
                        reset();
                    }
                }
            } else if (board[x][y].getType() == 'b') {
                if (moveBishop(x, y, targetx, targety, board)) {
                    makeMove(x, y, targetx, targety, board);
                    if (checkKing(currentColor, board)) {
                        backup();
                        return true;
                    } else {
                        reset();
                    }
                }
            } else if (board[x][y].getType() == 'n') {
                if (moveKnight(x, y, targetx, targety)) {
                    makeMove(x, y, targetx, targety, board);
                    if (checkKing(currentColor, board)) {
                        backup();
                        return true;
                    } else {
                        reset();
                    }
                }
            } else if (board[x][y].getType() == 'q') {
                if (moveQueen(x, y, targetx, targety, board)) {
                    makeMove(x, y, targetx, targety, board);
                    if (checkKing(currentColor, board)) {
                        backup();
                        return true;
                    } else {
                        reset();
                    }
                }
            }
        }
        return false;

    }

    // boards getter
    public Space[][] getBoard() {
        return board;
    }

    // prints the board
    public void printBoard() {
        System.out.println(" |0|1|2|3|4|5|6|7|");
        for (int y = 0; y < 8; y++) {
            System.out.print(y);
            for (int x = 0; x < 8; x++) {
                System.out.print("|" + board[x][y]);
            }
            System.out.println("|");
        }
    }

    // checks for valid moves
    public boolean canMove(int color) {
        int[] xy = findKing(color, board);
        if (kingEscape(xy[0], xy[1], color)) {
            return true;
        } else if (canBlock(color)) {
            return true;
        }
        //System.out.println("you cannot move right now");
        return false;
    }

    // The AI has already vetted its move so checking again would be redundant
    public void AIMove(int x, int y, int targetx, int targety, int currentColor) {
        if (board[x][y].getType() == 'k') {
            if (x == 4) {
                if (targetx == 7) {
                    if (currentColor == 1) {
                        board[5][0].setPiece("R", 'r', 1);
                    } else if (currentColor == 2) {
                        board[5][7].setPiece("R", 'r', 2);
                    }
                } else if (targetx == 0) {
                    if (currentColor == 1) {
                        board[3][0].setPiece("R", 'r', 1);
                    } else if (currentColor == 2) {
                        board[3][7].setPiece("R", 'r', 2);
                    }
                }
            }
        }

        board[targetx][targety].setPiece(board[x][y]);
        board[x][y].clearSpace();

        if(board[targetx][targety].getType() == 'p'){ // The AI will always give itself more queens
            promote(x, y, currentColor, board);
        }


        backup();
    }

    //--public statics--

    // validates AI input as the AI won't enter in index out of bounds errors
    public static boolean AICheck(int x, int y, int targetx, int targety, int currentColor, Space[][] board) {
        if (board[x][y].getType() == 'k') {
            if (moveKing(x, y, targetx, targety, currentColor, board)) {
                makeMove(x, y, targetx, targety, board);
                return true; // can not inadvertently put a king in check while moving the king
            }

        } else if (board[targetx][targety].getColor() == currentColor) {
            return false;
        } else if (board[x][y].getType() == 'p') {
            if (movePawn(x, y, targetx, targety, currentColor, board)) {
                makeMove(x, y, targetx, targety, board);
                if (checkKing(currentColor, board)) {
                    promote(x, y, currentColor, board);
                    return true;
                }
            }

        } else if (board[x][y].getType() == 'r') {
            if (moveRook(x, y, targetx, targety, board)) {
                makeMove(x, y, targetx, targety, board);
                if (checkKing(currentColor, board)) {
                    return true;
                }
            }
        } else if (board[x][y].getType() == 'b') {
            if (moveBishop(x, y, targetx, targety, board)) {
                makeMove(x, y, targetx, targety, board);
                if (checkKing(currentColor, board)) {
                    return true;
                }
            }
        } else if (board[x][y].getType() == 'n') {
            if (moveKnight(x, y, targetx, targety)) {
                makeMove(x, y, targetx, targety, board);
                if (checkKing(currentColor, board)) {
                    return true;
                }
            }
        } else if (board[x][y].getType() == 'q') {
            if (moveQueen(x, y, targetx, targety, board)) {
                makeMove(x, y, targetx, targety, board);
                if (checkKing(currentColor, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // utility method for getting the other players color
    public static int getOpponent(int color) {
        if (color == 1) {
            return 2;
        } else {
            return 1;
        }
    }


    public static int calcScore(Space[][] board){
        int count = 0;
        int multiple;
        for(int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y].getColor() != 0) {
                    if (board[x][y].getColor() == 2) { // changes the positive and negative sign;
                        multiple = 1;
                    } else {
                        multiple = -1;
                    }

                    if (board[x][y].getType() == 'p') {
                        count += multiple * 10;
                    } else if (board[x][y].getType() == 'b' || board[x][y].getType() == 'n') {
                        count += multiple * 30;
                    } else if (board[x][y].getType() == 'r') {
                        count += multiple * 50;
                    } else if (board[x][y].getType() == 'q') {
                        count += multiple * 90;
                    } else if (board[x][y].getType() == 'k') {
                        count += multiple * 900;
                    }
                }
            }
        }
        return count;
    }

    //--private--

    // checks if checkmate can be prevented by a piece other than the king
    private boolean canBlock(int color) {

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y].getColor() == color) {
                    if (board[x][y].getType() == 'q') {
                        if (scanStraight(x, y, color) || scanDiagonal(x, y, color)) {
                            return true;
                        }
                    } else if (board[x][y].getType() == 'b') {
                        if (scanDiagonal(x, y, color)) {
                            return true;
                        }
                    } else if (board[x][y].getType() == 'r') {
                        if (scanStraight(x, y, color)) {
                            return true;
                        }
                    } else if (board[x][y].getType() == 'n') {
                        if (scanKnight(x, y, color)) {
                            return true;
                        }
                    } else if (board[x][y].getType() == 'p') {
                        if (scanPawn(x, y, color)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // checks if a possible move will save the king
    private boolean moveTest(int x, int y, int targetx, int targety, int currentColor) {
        if (x == targetx && y == targety) { // prevents repeats
            return false;
        } else if (board[targetx][targety].getColor() == currentColor) {
            return false;
        } else if (board[x][y].getType() == 'p') {
            return movePawn(x, y, targetx, targety, currentColor, board);
        } else if (board[x][y].getType() == 'r') {
            return moveRook(x, y, targetx, targety, board);
        } else if (board[x][y].getType() == 'b') {
            return moveBishop(x, y, targetx, targety, board);
        } else if (board[x][y].getType() == 'n') {
            return moveKnight(x, y, targetx, targety);
        } else if (board[x][y].getType() == 'q') {
            return moveQueen(x, y, targetx, targety, board);
        }
        return false;
    }

    // checks if the king can escape
    private boolean kingEscape(int x, int y, int color) {
        for (int b = -1; b < 2; b++) {
            for (int a = -1; a < 2; a++) {
                if (checkInput(x + a, y + b)) {
                    if (board[x+a][y+b].getColor() != color) {
                        if (isSafe(x + a, y + b, color, board)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (x == 4) { // flee via castling
            if ((color == 1 && y == 0) || (color == 2 && y == 7)){
                if (canCastle(color, board)) {
                    return true;
                } else if (canQueenCastle(color, board)){
                    return true;
                }
            }
        }
        return false;
    }

    // updates oldBoard
    private void backup() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                oldBoard[x][y] = new Space(board[x][y]);
            }
        }
    }

    // resets the board
    private void reset() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y] = new Space(oldBoard[x][y]);
            }
        }
    }

    private boolean checkInput(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return true;
        } else {
            return false;
        }
    }

    // this little segment of code was repetative;
    private boolean scanHelper(int x, int y, int targetx, int targety, int color) {
        boolean result = false;
        if(moveTest(x, y , targetx, targety, color)){
            makeMove(x,y,targetx,targety,board);
            result = checkKing(color, board);
        }
        reset();
        return result;
    }

    private boolean scanStraight(int targetx, int targety, int color) {
        int hostile = getOpponent(color);
        int i;
        // vertical horizontal
        i = targetx + 1;
        while (i < 8 && board[i][targety].getColor() != color) {
            if (scanHelper(targetx, targety, i, targety, color)) {
                return true;
            } else if (board[i][targety].getColor() == hostile) {
                break;
            }
            i++;
        }

        i = targetx - 1;
        while (i >= 0 && board[i][targety].getColor() != color) {
            if (scanHelper(targetx, targety, i, targety, color)) {
                return true;
            } else if (board[i][targety].getColor() == hostile) {
                break;
            }
            i--;
        }

        i = targety + 1;
        while (i < 8 && board[targetx][i].getColor() != color) {
            if (scanHelper(targetx, targety, targetx, i, color)) {
                return true;
            } else if (board[targetx][i].getColor() == hostile) {
                break;
            }
            i++;
        }
        i = targety - 1;

        while (i >= 0 && board[targetx][i].getColor() != color) {
            if (scanHelper(targetx, targety, targetx, i, color)) {
                return true;
            } else if (board[targetx][i].getColor() == hostile) {
                break;
            }
            i--;
        }
        return false;
    }

    private boolean scanDiagonal(int targetx, int targety, int color) {
        int hostile = getOpponent(color);

        // diagonals
        int x = targetx + 1;
        int y = targety + 1;

        while (x < 8 && y < 8 && board[x][y].getColor() != color) {
            if (scanHelper(targetx, targety, x, y, color)) {
                return true;
            } else if (board[x][y].getColor() == hostile) {
                break;
            }
            x++;
            y++;
        }

        x = targetx - 1;
        y = targety - 1;

        while (x >= 0 && y >= 0 && board[x][y].getColor() != color) {
            if (scanHelper(targetx, targety, x, y, color)) {
                return true;
            } else if (board[x][y].getColor() == hostile) {
                break;
            }
            x--;
            y--;
        }


        x = targetx + 1;
        y = targety - 1;

        while (x < 8 && y >= 0 && board[x][y].getColor() != color) {
            if (scanHelper(targetx, targety, x, y, color)) {
                return true;
            } else if (board[x][y].getColor() == hostile) {
                break;
            }
            x++;
            y--;
        }


        x = targetx - 1;
        y = targety + 1;

        while (x >= 0 && y < 8 && board[x][y].getColor() != color) {
            if (scanHelper(targetx, targety, x, y, color)) {
                return true;
            } else if (board[x][y].getColor() == hostile) {
                break;
            }
            x--;
            y++;
        }
        return false;
    }

    private boolean scanKnight(int targetx, int targety, int color) {

        if (targetx + 2 < 8) {
            if (targety + 1 < 8 && board[targetx + 2][targety + 1].getColor() != color) {
                if (scanHelper(targetx, targety, targetx + 2, targety + 1, color)) {
                    return true;
                }
            }
            if (targety - 1 >= 0 && board[targetx + 2][targety - 1].getColor() != color) {
                if (scanHelper(targetx, targety, targetx + 2, targety - 1, color)) {
                    return true;
                }
            }
        }

        if (targetx - 2 > 0) {
            if (targety + 1 < 8 && board[targetx - 2][targety + 1].getColor() != color) {
                if (scanHelper(targetx, targety, targetx - 2, targety + 1, color)) {
                    return true;
                }
            }
            if (targety - 1 >= 0 && board[targetx - 2][targety - 1].getColor() != color) {
                if (scanHelper(targetx, targety, targetx - 2, targety - 1, color)) {
                    return true;
                }
            }
        }

        if (targetx + 1 < 8) {
            if (targety + 2 < 8 && board[targetx + 1][targety + 2].getColor() != color) {
                if (scanHelper(targetx, targety, targetx + 1, targety + 2, color)) {
                    return true;
                }
            }
            if (targety - 2 >= 0 && board[targetx + 1][targety - 2].getColor() != color) {
                if (scanHelper(targetx, targety, targetx + 1, targety - 2, color)) {
                    return true;
                }
            }
        }

        if (targetx - 1 > 0) {
            if (targety + 2 < 8 && board[targetx - 1][targety + 2].getColor() != color) {
                if (scanHelper(targetx, targety, targetx - 1, targety + 2, color)) {
                    return true;
                }
            }
            if (targety - 2 >= 0 && board[targetx - 1][targety - 2].getColor() != color) {
                if (scanHelper(targetx, targety, targetx - 1, targety - 2, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean scanPawn(int x, int y, int color) {
        int hostile;
        int i;
        if (color == 1) {
            hostile = 2;
            i = 1;
        } else {
            hostile = 1;
            i = -1;
        }

        if((color == 1 && y < 7) || (color == 2 && y > 0)){
            if (board[x][y + i].getColor() == 0) {
                if (scanHelper(x, y, x, y + i, color)) {
                    return true;
                }
            }

            if (x > 0 && board[x - 1][y + i].getColor() == hostile) {
                if (scanHelper(x, y, x - 1, y + i, color)) {
                    return true;
                }
            }

            if (x < 7 && board[x + 1][y + i].getColor() == hostile) {
                if (scanHelper(x, y, x + 1, y + i, color)) {
                    return true;
                }
            }
            if ((color == 1 && y == 1)) { // prevents index out of bounds
                if ((board[x][2].getColor() == 0 && board[x][3].getColor() == 0)) {
                    if (scanHelper(x, y, x, y + 2, color)) {
                        return true;
                    }
                }
            } else if (color == 2 && y == 6) {
                if ((board[x][5].getColor() == 0 && board[x][4].getColor() == 0)) {
                    if (scanHelper(x, y, x, y - 2, color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //--private statics--
    private static boolean checkKing(int color, Space[][] board){
        int[] xy = findKing(color, board);
        return isSafe(xy[0], xy[1], color, board);
    }

    // moves selected piece
    private static void makeMove(int x, int y, int targetx, int targety, Space[][] board) {
        board[targetx][targety].setPiece(board[x][y]);
        board[x][y].clearSpace();
    }

    private static boolean moveKnight(int x, int y, int targetx, int targety) {
        if (targetx == (x - 2) || targetx == (x + 2)) {
            if (targety == (y - 1) || targety == (y + 1)) {
                return true;
            }
        } else if (targetx == (x - 1) || targetx == (x + 1)) {
            if (targety == (y - 2) || targety == (y + 2)) {
                return true;
            }
        }

        return false;
    }

    private static boolean moveQueen(int x, int y, int targetx, int targety, Space[][] board) {
        if (targetx == x) {
            return checkVertical(y, targety, x, board);
        } else if (targety == y) {
            return checkHorizontal(x, targetx, y, board);
        }
        if (x < targetx && y < targety) {
            return checkDLR(x, y, targetx, targety, board);
        } else if (x > targetx && y < targety) {
            return checkDLL(x, y, targetx, targety, board);
        } else if (x > targetx && y > targety) {
            return checkDUL(x, y, targetx, targety, board);
        } else if (x < targetx && y > targety) {
            return checkDUR(x, y, targetx, targety, board);
        }
        return false;
    }

    private static boolean moveBishop(int x, int y, int targetx, int targety, Space[][] board) {
        if (x < targetx && y < targety) {
            return checkDLR(x, y, targetx, targety, board);
        } else if (x > targetx && y < targety) {
            return checkDLL(x, y, targetx, targety, board);
        } else if (x > targetx && y > targety) {
            return checkDUL(x, y, targetx, targety, board);
        } else if (x < targetx && y > targety) {
            return checkDUR(x, y, targetx, targety, board);
        }
        return false;
    }
    // method for improving AI efficiency
    private static boolean moveKing(int x, int y, int targetx, int targety, int color, Space[][] board) {
        if (board[targetx][targety].getColor() != color) { // checks for repeats
            if (x == targetx || x == targetx + 1 || x == targetx - 1) {
                if (y == targety || y == targety + 1 || y == targety - 1) {
                    if (isSafe(targetx, targety, color, board)) {

                        return true;
                    }
                }
            }
        } else if (x == 4) {
            if ((color == 1 && y == 0 && targety == 0) || (color == 2 && y == 7 && targety == 7)){
                if(targetx == 7 && canCastle(color, board)){
                    castle(color, board);
                    return true;
                } else if(targetx == 0 && canQueenCastle(color, board)) {
                    queenCastle(color, board);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean canQueenCastle(int color, Space[][] board) {
        if (color == 1) {
            if (board[0][1].isPawn(color) && board[1][1].isPawn(color) && board[2][1].isPawn(color) && board[0][0].isRook(color)) {
                if (board[1][0].getColor() == 0 && board[2][0].getColor() == 0 && board[3][0].getColor() == 0) {
                    return true;
                }
            }
        } else {
            if (board[0][6].isPawn(color) && board[0][6].isPawn(color) && board[0][6].isPawn(color) && board[0][7].isRook(color)) {
                if (board[1][7].getColor() == 0 && board[2][7].getColor() == 0 && board[3][7].getColor() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void queenCastle(int color, Space[][] board) {
        if (color == 1) {
            board[3][0].setPiece("R", 'r', 1);
        } else {
            board[3][7].setPiece("R", 'r', 2);
        }
    }

    // castles a piece
    private static void castle(int color, Space[][] board) {
        if (color == 1) {
            board[5][0].setPiece("R", 'r', 1);
        } else {
            board[5][7].setPiece("R", 'r', 2);
        }
    }

    private static boolean canCastle(int color, Space[][] board) {
        if (color == 1) {
            if (board[5][1].isPawn(color) && board[6][1].isPawn(color) && board[7][1].isPawn(color) && board[7][0].isRook(color)) {
                if (board[5][0].getColor() == 0 && board[6][0].getColor() == 0) {
                    return true;
                }
            }
        } else {
            if (board[5][6].isPawn(color) && board[6][6].isPawn(color) && board[7][6].isPawn(color) && board[7][7].isRook(color)) {
                if (board[5][7].getColor() == 0 && board[6][7].getColor() == 0) {
                    return true;
                }
            }
        }
        return false;

    }

    private static boolean moveRook(int x, int y, int targetx, int targety, Space[][] board) {
        if (targetx == x) {
            return checkVertical(y, targety, x, board);
        } else if (targety == y) {
            return checkHorizontal(x, targetx, y, board);
        } else {
            return false;
        }
    }

    // gets an x and y and looks for anything that can put the king in check;
    private static boolean isSafe(int targetx, int targety, int color, Space[][] board) {

        int hostile = getOpponent(color);
        int i;
        int x;
        int y;
        // vertical horizontal
        i = targetx + 1;
        while (i < 8 && board[i][targety].getColor() == 0) {
            i++;
        }
        if (i < 8 && board[i][targety].getColor() == hostile) {
            if (board[i][targety].getType() == 'r' || board[i][targety].getType() == 'q') {
                return false;
            } else if (board[i][targety].getType() == 'k' && i == targetx + 1) {
                return false;
            }
        }
        i = targetx - 1;

        while (i >= 0 && board[i][targety].getColor() == 0) {
            i--;
        }

        if (i >= 0 && board[i][targety].getColor() == hostile) {

            if (board[i][targety].getType() == 'r' || board[i][targety].getType() == 'q') {

                return false;
            } else if (board[i][targety].getType() == 'k' && i == targetx - 1) {
                return false;
            }
        }
        i = targety + 1;
        while (i < 8 && board[targetx][i].getColor() == 0) {
            i++;

        }
        if (i < 8 && board[targetx][i].getColor() == hostile) {
            if (board[targetx][i].getType() == 'r' || board[targetx][i].getType() == 'q') {
                return false;
            } else if (board[targetx][i].getType() == 'k' && i == targety + 1) {
                return false;
            }
        }
        i = targety - 1;

        while (i >= 0 && board[targetx][i].getColor() == 0) {
            i--;
        }
        if (i >= 0 && board[targetx][i].getColor() == hostile) {
            if (board[targetx][i].getType() == 'r' || board[targetx][i].getType() == 'q') {
                return false;
            } else if (board[targetx][i].getType() == 'k' && i == targety - 1) {
                return false;
            }
        }

        // diagonals
        x = targetx + 1;
        y = targety + 1;
        while (x < 8 && y < 8 && board[x][y].getColor() == 0) {
            x++;
            y++;
        }

        if (x < 8 && y < 8 && board[x][y].getColor() == hostile) {

            if (board[x][y].getType() == 'b' || board[x][y].getType() == 'q') {
                return false;
            } else if (board[x][y].getType() == 'k' && x == targetx + 1 && y == targety + 1) {
                return false;
            } else if (color == 1 && board[x][y].getType() == 'p' && x == targetx + 1 && y == targety + 1) {
                return false;
            }
        }

        x = targetx - 1;
        y = targety - 1;
        while (x >= 0 && y >= 0 && board[x][y].getColor() == 0) {
            x--;
            y--;
        }

        if (x >= 0 && y >= 0 && board[x][y].getColor() == hostile) {
            if (board[x][y].getType() == 'b' || board[x][y].getType() == 'q') {
                return false;
            } else if (board[x][y].getType() == 'k' && x == targetx - 1 && y == targety - 1) {
                return false;
            } else if (color == 2 && board[x][y].getType() == 'p' && x == targetx - 1 && y == targety - 1) {
                return false;
            }
        }

        x = targetx + 1;
        y = targety - 1;
        while (x < 8 && y >= 0 && board[x][y].getColor() == 0) {
            x++;
            y--;
        }

        if (x < 8 && y >= 0 && board[x][y].getColor() == hostile) {
            if (board[x][y].getType() == 'b' || board[x][y].getType() == 'q') {
                return false;
            } else if (board[x][y].getType() == 'k' && x == targetx + 1 && y == targety - 1) {
                return false;
            } else if (color == 2 && board[x][y].getType() == 'p' && x == targetx + 1 && y == targety - 1) {
                return false;
            }
        }

        x = targetx - 1;
        y = targety + 1;

        while (x >= 0 && y < 8 && board[x][y].getColor() == 0) {
            x--;
            y++;
        }

        if (x >= 0 && y < 8 && board[x][y].getColor() == hostile) {
            if (board[x][y].getType() == 'b' || board[x][y].getType() == 'q') {
                return false;
            } else if (board[x][y].getType() == 'k' && x == targetx - 1 && y == targety + 1) {
                return false;
            } else if (color == 1 && board[x][y].getType() == 'p' && x == targetx - 1 && y == targety + 1) {
                return false;
            }
        }
        // knights
        if (targetx + 2 < 8) {
            if (targety + 1 < 8 && board[targetx + 2][targety + 1].getColor() == hostile && board[targetx + 2][targety + 1].getType() == 'n') {
                return false;
            }
            if (targety - 1 >= 0 && board[targetx + 2][targety - 1].getColor() == hostile && board[targetx + 2][targety - 1].getType() == 'n') {
                return false;
            }
        }
        if (targetx - 2 > 0) {
            if (targety + 1 < 8 && board[targetx - 2][targety + 1].getColor() == hostile && board[targetx - 2][targety + 1].getType() == 'n') {
                return false;
            }
            if (targety - 1 >= 0 && board[targetx - 2][targety - 1].getColor() == hostile && board[targetx - 2][targety - 1].getType() == 'n') {
                return false;
            }
        }
        if (targetx + 1 < 8) {
            if (targety + 2 < 8 && board[targetx + 1][targety + 2].getColor() == hostile && board[targetx + 1][targety + 2].getType() == 'n') {
                return false;
            }
            if (targety - 2 >= 0 && board[targetx + 1][targety - 2].getColor() == hostile && board[targetx + 1][targety - 2].getType() == 'n') {
                return false;
            }
        }
        if (targetx - 1 > 0) {
            if (targety + 2 < 8 && board[targetx - 1][targety + 2].getColor() == hostile && board[targetx - 1][targety + 2].getType() == 'n') {
                return false;
            }
            if (targety - 2 >= 0 && board[targetx - 1][targety - 2].getColor() == hostile && board[targetx - 1][targety - 2].getType() == 'n') {
                return false;
            }
        }
        return true;
    }

    private static boolean checkHorizontal(int x, int targetx, int y, Space[][] board) {
        int z;
        int target;

        if (x < targetx) {
            z = x;
            target = targetx;
        } else {
            z = targetx;
            target = x;
        }
        z++; // move past first item
        while (z < target) {
            if (board[z][y].getColor() != 0) {
                return false;
            }
            z++;
        }
        return true;
    }

    private static boolean checkVertical(int y, int targety, int x, Space[][] board) {
        int z;
        int target;

        if (y < targety) {
            z = y;
            target = targety;
        } else {
            z = targety;
            target = y;
        }
        z++; // move past first item
        while (z < target) {
            if (board[x][z].getColor() != 0) {
                return false;
            }
            z++;

        }
        return true;
    }

    // Diagonal upper right
    // these four look in diagonal lines between two points and return true if the current function has nothing in
    // between them and if the line is straight;
    private static boolean checkDUR(int tempx, int tempy, int targetx, int targety, Space[][] board) {
        int x = tempx + 1;
        int y = tempy - 1;

        while (x < targetx && y > targety) {
            if (board[x][y].getColor() != 0) {
                return false;
            }
            x++;
            y--;
        }
        return x == targetx && y == targety;
    }

    private static boolean checkDUL(int tempx, int tempy, int targetx, int targety, Space[][] board) {
        int x = tempx - 1;
        int y = tempy - 1;

        while (x > targetx && y > targety) {
            if (board[x][y].getColor() != 0) {
                return false;
            }
            x--;
            y--;
        }
        return x == targetx && y == targety;
    }

    private static boolean checkDLR(int tempx, int tempy, int targetx, int targety, Space[][] board) {
        int x = tempx + 1;
        int y = tempy + 1;

        while (x < targetx && y < targety) {
            if (board[x][y].getColor() != 0) {
                return false;
            }
            x++;
            y++;
        }
        return x == targetx && y == targety;
    }

    private static boolean checkDLL(int tempx, int tempy, int targetx, int targety, Space[][] board) {
        int x = tempx - 1;
        int y = tempy + 1;

        while (x > targetx && y < targety) {
            if (board[x][y].getColor() != 0) {
                return false;
            }
            x--;
            y++;
        }
        return x == targetx && y == targety;
    }

    // moves pawn
    private static boolean movePawn(int x, int y, int targetx, int targety, int color, Space[][] board) {
        int hostile = getOpponent(color);
        if ((color == 1 && targety == (y + 1)) || (color == 2 && targety == (y - 1))) {
            if (x == targetx) { // move forward
                if ((board[targetx][targety].getColor() == 0)) {
                    return true;
                }
            } else if ((x == (targetx - 1)) || (x == (targetx + 1))) { // capture piece
                return board[targetx][targety].getColor() == hostile;
            }
        } else if (x == targetx) {
            if (color == 1 && y == 1 && targety == 3) {
                if ((board[targetx][2].getColor() == 0 && board[targetx][3].getColor() == 0)) {
                    return true;
                }
            } else if (color == 2 && y == 6 && targety == 4) {
                if (board[targetx][5].getColor() == 0 && board[targetx][4].getColor() == 0) {
                    return true;
                }
            }
        }

        return false;

    }

    // finds the king
    private static int[] findKing(int color, Space[][] board){
        int[] temp = new int[2];
        temp[0] = 0;
        temp[1] = 0;
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(board[x][y].isKing(color)){
                    temp[0] = x;
                    temp[1] = y;
                    return temp;
                }
            }
        }
        return temp;
    }


    // upgrades pawn
    private static void upgrade(int x, int y, char type, Space[][] board){
        board[x][y].setType(type);
        if(board[x][y].getColor() == 2){
            if(type == 'q'){
                board[x][y].setId("Q");
            } else if (type == 'n') {
                board[x][y].setId("N");
            } else if(type == 'r'){
                board[x][y].setId("R");
            } else if (type == 'b') {
                board[x][y].setId("B");
            }

        } else {
            if(type == 'q'){
                board[x][y].setId("q");
            } else if (type == 'n') {
                board[x][y].setId("n");
            } else if(type == 'r'){
                board[x][y].setId("r");
            } else if (type == 'b') {
                board[x][y].setId("b");
            }
        }
    }

    // method for promoting pawns
    private static void promote(int x, int y, int color, Space[][] board){ // will be expanded later
        if((color == 1 && y == 7) || (color == 2 && y == 0)){
            if(findQueen(color, board)) {
                upgrade(x, y, 'q', board);
            }
        }
    }

    // printout for testing
    public static void printBoard(Space[][] board){
        System.out.println(" |0|1|2|3|4|5|6|7|");
        for (int y = 0; y < 8; y++) {
            System.out.print(y);
            for (int x = 0; x < 8; x++) {
                System.out.print("|" + board[x][y]);
            }
            System.out.println("|");
        }
    }

    public static boolean findQueen(int color, Space[][] board){

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(board[x][y].getColor() == color && board[x][y].getType() == 'q'){
                    return false;
                }
            }
        }
        return true;
    }

}
