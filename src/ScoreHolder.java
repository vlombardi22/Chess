/**
 This class acts as a wrapper that holds a score and a boolean
 The score represents the possible score for a move while the boolean represents if it is a valid move
 This class dramatically improves the efficiency of the AI algorithm by preventing boards from being generated for most invalid moves
 The scoring for pawns and the king changes halfway through the game to reward turning pawns into Queens while protecting the king.
 */
public class ScoreHolder {
    private int score;
    private boolean valid;

    private static final int[][] Pawn =  {{60,  60,  60,  60,  60,  60,  60,  60},
                                    {50, 50, 50, 50, 50, 50, 50, 50},
                                    {10, 10, 20, 30, 30, 20, 10, 10},
                                    {5,  5, 10, 25, 25, 10,  5,  5},
                                    {0,  0,  0, 20, 20,  0,  0,  0},
                                    {5, -5,-10,  0,  0,-10, -5,  5},
                                    {5, 10, 10,-20,-20, 10, 10,  5},
                                    {0,  0,  0,  0,  0,  0,  0,  0}};

    private static final int[][] EPawn =  {{880,  890,  890,  895,  895,  890, 890, 880},
                                    {90, 90, 90, 90, 90, 90, 90, 90},
                                    {85, 85, 85, 85, 85, 85, 85, 85},
                                    {80, 80, 80, 80, 80, 80, 80, 80},
                                    {0,  0,  0, 20, 20,  0,  0,  0},
                                    {5, -5,-10,  0,  0,-10, -5,  5},
                                    {5, 10, 10,-20,-20, 10, 10,  5},
                                    {0,  0,  0,  0,  0,  0,  0,  0}};


    private static final int[][] Knight ={{-50,-40,-30,-30,-30,-30,-40,-50},
                                    {-40,-20,  0,  0,  0,  0,-20,-40},
                                    {-30,  0, 10, 15, 15, 10,  0,-30},
                                    {-30,  5, 15, 20, 20, 15,  5,-30},
                                    {-30,  0, 15, 20, 20, 15,  0,-30},
                                    {-30,  5, 10, 15, 15, 10,  5,-30},
                                    {-40,-20,  0,  5,  5,  0,-20,-40},
                                    {-50,-40,-30,-30,-30,-30,-40,-50}};

    private static final int[][] Bishop = {{-20,-10,-10,-10,-10,-10,-10,-20},
                                    {-10,  0,  0,  0,  0,  0,  0,-10},
                                    {-10,  0,  5, 10, 10,  5,  0,-10},
                                    {-10,  5,  5, 10, 10,  5,  5,-10},
                                    {-10,  0, 10, 10, 10, 10,  0,-10},
                                    {-10, 10, 10, 10, 10, 10, 10,-10},
                                    {-10,  5,  0,  0,  0,  0,  5,-10},
                                    {-20,-10,-10,-10,-10,-10,-10,-20}};

    private static final int[][] Rook = {{0,  0,  0,  0,  0,  0,  0,  0},
                                    {5, 10, 10, 10, 10, 10, 10,  5},
                                    {-5,  0,  0,  0,  0,  0,  0, -5},
                                    {-5,  0,  0,  0,  0,  0,  0, -5},
                                    {-5,  0,  0,  0,  0,  0,  0, -5},
                                    {-5,  0,  0,  0,  0,  0,  0, -5},
                                    {-5,  0,  0,  0,  0,  0,  0, -5},
                                    {-5,  0,  0,  5,  5,  0,  0,  -5}};

    private static final int[][] Queen = {{-20,-10,-10, -5, -5,-10,-10,-20},
                                    {-10,  0,  0,  0,  0,  0,  0,-10},
                                    {-10,  0,  5,  5,  5,  5,  0,-10},
                                    {-5,  0,  5,  5,  5,  5,  0, -5},
                                    {0,  0,  5,  5,  5,  5,  0, -5},
                                    {-10,  5,  5,  5,  5,  5,  0,-10},
                                    {-10,  0,  5,  0,  0,  0,  0,-10},
                                    {-20,-10,-10, -5, -5,-10,-10,-20}};



    private static final int[][] King = {{-30, -40, -40, -50, -50, -40, -40, -30},
                                    {-30, -40, -40, -50, -50, -40, -40, -30},
                                    {-30, -40, -40, -50, -50, -40, -40, -30},
                                    {-30, -40, -40, -50, -50, -40, -40, -30},
                                    {-20, -30, -30, -40, -40, -30, -30, -20},
                                    {-10, -20, -20, -20, -20, -20, -20, -10},
                                    {20, 20,  0,  0,  0,  0, 20, 20},
                                    {20, 30, 10,  0,  0, 10, 30, 20}};

    private static final int[][] EKing = {{-50,-40,-30,-20,-20,-30,-40,-50},
                                    {-30,-20,-10,  0,  0,-10,-20,-30},
                                    {-30,-10, 20, 30, 30, 20,-10,-30},
                                    {-30,-10, 30, 40, 40, 30,-10,-30},
                                    {-30,-10, 30, 40, 40, 30,-10,-30},
                                    {-30,-10, 20, 30, 30, 20,-10,-30},
                                    {-30,-30,  0,  0,  0,  0,-30,-30},
                                    {-50,-30,-30,-30,-30,-30,-30,-50}};


    // constructor for valid moves
    public ScoreHolder(Space[][] board, int score){
        this.score = score;
        this.score += calcScore(board);
        valid = true;
    }

    // constructor for invalid moves
    public ScoreHolder(){
        score = 0;
        valid = false;
    }

    // returns score
    public int getScore() {
        return score;
    }

    // is valid move
    public boolean isValid() {
        return valid;
    }

    // calculates the score of a given board based on position
    private static int calcScore(Space[][] board){
        int count = 0; // score variable
        int wcount = 0; // number of white pieces
        int bcount = 0; // number of black pieces
        int multiple; // determines if a piece is scored negatively or positively based on color
        int pwscore = 0; // pawn score
        int pwescore = 0; // pawn end score
        int pbscore = 0; // pawn score
        int pbescore = 0; // pawn end score
        int wx = -1;
        int wy = -1;
        int bx = -1;
        int by = -1;

        for(int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y].getColor() != 0) {
                    if (board[x][y].getColor() == 2) { // changes the positive and negative sign
                        multiple = 1;
                        wcount++;
                    } else {
                        multiple = -1;
                        bcount++;
                    }

                    if (board[x][y].getType() == 'p') {
                        count += multiple * 100;
                        if (multiple == 1) {
                            pwscore += Pawn[y][x];
                            pwescore += EPawn[y][x];
                        } else {
                            pbscore += -1 * Pawn[7 - y][x];
                            pbescore += -1 * EPawn[7 - y][x];
                        }
                    } else if (board[x][y].getType() == 'b') {

                        count += multiple * 330;
                        if (multiple == 1) {
                            count += Bishop[y][x];

                        } else {
                            count += -1 * Bishop[7 - y][x];
                        }
                    } else if (board[x][y].getType() == 'n') {
                        count += multiple * 320;
                        if (multiple == 1) {
                            count += Knight[7 - y][x];
                        } else {
                            count += -1 * Knight[7 - y][x];
                        }

                    } else if (board[x][y].getType() == 'r') {
                        count += multiple * 500;
                        if (multiple == 1) {
                            count += Rook[7 - y][x];
                        } else {
                            count += -1 * Rook[7 - y][x];
                        }

                    } else if (board[x][y].getType() == 'q') {
                        count += multiple * 900;
                        if (multiple == 1) {
                            count += Queen[y][x];
                        } else {
                            count += -1 * Queen[7 - y][x];
                        }

                    } else if (board[x][y].getType() == 'k') {
                        count += multiple * 2000;

                        if (board[x][y].getColor() == 2) {
                            wx = x; // TODO fix the x y problem
                            wy = y;
                        } else {
                            bx = x;
                            by = y;
                        }
                    }
                }
            }
        }

        if(wcount < 8 || bcount < 8){ // tried to minimize the loops
            if(wx != -1) {
                count += EKing[wy][wx];
            }
            if(bx != -1) {
                count += -1 * EKing[7-by][bx];
            }
        } else {
            if(wx != -1) {
                count += King[wy][wx];
            }
            if(bx != -1) {
                count += -1 * King[7-by][bx];
            }
        }

        if(wcount < 8){ // score pawns differently based on number of remaining pieces to encourage getting queens
            count += pbescore;
        } else {
            count += pbscore;
        }

        if(bcount < 8){
            count += pwescore;
        } else {
            count += pwscore;
        }


        return count;

    }
}
