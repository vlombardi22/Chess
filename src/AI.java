import java.util.ArrayList;
import java.util.Hashtable;

/**
This class is the AI
 */

public class AI {
    private final int maxScore = Integer.MAX_VALUE;
    private final int minScore = Integer.MIN_VALUE;
    private int color;
    private Space [][] board; // game board object
    private int wCount; // number of white pieces
    private int bCount; // number of black pieces
    private int DepthBound;



    public AI (Board board, int color, int depthBound){
        this.board = new Space[8][8];
        this.color = color;
        DepthBound = depthBound;
        wCount = 16;
        bCount = 16;
        Space[][] temp = board.getBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) { // copy game board
                this.board[x][y] = new Space(temp[x][y]);
            }
        }
    }

    public String makeMove() {
        ArrayList<Node> moveList = new ArrayList<>();
        int depthBound = DepthBound;

        if (bCount < 8 || wCount < 8) { // increase depth bound near end of the game to improve checkmate ability
            depthBound += 2;
            if((bCount < 7 && color == 2) || (wCount < 7 && color == 1)){
                depthBound += 1;
            }
        }

        Hashtable<String, ArrayList<Node>> moveTree = new Hashtable<>(); // tree
        Node root;
        if(color == 1){
            root = new Node(false, "r", maxScore); // deep copy so we don't remove it
        } else {
            root = new Node(false, "r", minScore); // deep copy so we don't remove it
        }
        moveTree.put("r", moveList);
        int count = getCount(color);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y].getColor() == color) {
                    for (int d = 0; d < 8; d++){
                        for(int c = 0; c < 8; c++){
                            String parentcords = ""+ x + y + c + d;
                            if (moveHelper(x, y, c, d, color, depthBound - 1, board, moveList, parentcords)) {
                                makeMoveDepth(board, x, y, c, d, color, depthBound - 1, moveTree, parentcords);
                            }
                        }
                    }
                    count--;
                    if(count == 0){ // this is an effort to speed things up
                        return ab(moveTree, root, color, depthBound);
                    }
                }
            }
        }
        return ab(moveTree, root, color, depthBound);
    }

    private void makeMoveDepth (Space[][] tempB, int x, int y, int targetx, int targety,  int myColor, int depth, Hashtable<String, ArrayList<Node>> moveTree, String parentCoords) {
        int target;
        String coords = "" + x + y + targetx + targety;
        ArrayList<Node> moveList = new ArrayList<Node>();

        if (depth > 0) {
            target = Board.getOpponent(myColor);
            Space temp1 = new Space(tempB[x][y]);
            Space temp2 = new Space(tempB[targetx][targety]);
            // makes move
            mover(x,y,targetx, targety, myColor, tempB);
            int count = getCount(myColor);
            for (int b = 0; b < 8; b++) {
                for (int a = 0; a < 8; a++) {
                    if (tempB[a][b].getColor() == target) { // recursively look at all possible moves
                        for(int d = 0; d < 8; d++){
                            for(int c = 0; c < 8; c++){
                                if (moveHelper(a, b, c, d, target, depth - 1, tempB, moveList, parentCoords)) {
                                    makeMoveDepth(tempB, a, b, c, d, target, depth - 1, moveTree, parentCoords);
                                }
                            }
                        }
                        count--;
                        if(count == 0){ // this is an effort to speed things up
                            a = 8; // kind of like a break
                            b = 8;
                        }
                    }
                }
            }
            tempB[x][y] = new Space(temp1);
            tempB[targetx][targety] = new Space(temp2);
            moveTree.put(coords, moveList);
        }
    }

    // updates AI board
    public void updateBoard(Board board) {
        Space[][] temp = board.getBoard();
        wCount = board.getwCount();
        bCount = board.getbCount();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++ ) {
                this.board[x][y] = new Space(temp[x][y]);
            }
        }
    }

    private void mover(int x, int y, int targetx, int targety, int myColor, Space[][] tempBoard) {

        if(tempBoard[x][y].getType() == 'k') {
            if (x == 4) {
                if (targetx == 7) { // is safe is not called since a castle can't immediately put you in check
                    if (myColor == 1) {
                        tempBoard[5][0].setPiece("R", 'r', 1);
                    } else if (myColor == 2) {
                        tempBoard[5][7].setPiece("R", 'r', 2);
                    }
                } else if (targetx == 0){
                    if (myColor == 1) {
                        tempBoard[3][0].setPiece("R", 'r', 1);
                    } else if (myColor == 2) {
                        tempBoard[3][7].setPiece("R", 'r', 2);
                    }
                }
            }
        }
        tempBoard[targetx][targety].setPiece(tempBoard[x][y]);
        tempBoard[x][y].clearSpace();

        if(tempBoard[targetx][targety].getType() == 'p'){ // The AI will always give itself more queens
            if((myColor == 1 && targety == 7) || (myColor == 2 && targety == 0)) {
                tempBoard[targetx][targety].setType('q');
                tempBoard[targetx][targety].setId("Q");
            }
        }
    }

    private boolean moveHelper(int x, int y, int targetx, int targety, int myColor, int depth, Space[][] tBoard,  ArrayList <Node> movelist, String parentCords) {
        if(x == targetx && y == targety){ // get rid of repeats before a loop is called
            return false;
        } else if (tBoard[targetx][targety].getColor() == myColor) {
            if (!(tBoard[x][y].isKing(myColor) && tBoard[targetx][targety].isRook(myColor))) {
                return false;
            }
        }

        ScoreHolder scoreHolder = Board.AICheck(x,y,targetx,targety,myColor,tBoard);

        if(scoreHolder.isValid()){
            String coords = "" + x + y + targetx + targety;
            boolean leaf = false;
            if (depth <= 0) { // takes care of leaf nodes
                leaf = true;
            }

            Node newMove = new Node(leaf, coords, scoreHolder.getScore(), parentCords);
            movelist.add(newMove);

        }
        return scoreHolder.isValid();
    }

    private String ab(Hashtable<String, ArrayList<Node>> graph, Node root, int player, int depthBound) {
        Node bestMove;

        if (player == 2) {
            bestMove = bestWhite(graph, root, minScore, maxScore, depthBound, 0);
        } else {
            bestMove = bestBlack(graph, root, minScore, maxScore, depthBound, 0);
        }

        return bestMove.getParentCoords();
    }


    private Node bestWhite(Hashtable<String, ArrayList<Node>> graph, Node node, int talpha, int beta, int depthBound, int currDepth) {
        currDepth++;
        Node wNode = new Node(minScore, false);
        boolean childless = true;
        int alpha = talpha;
        // checks depth
        if (currDepth < depthBound) {
            if (node.isLeaf()) {
                return node;
            }
            for (Node child : graph.get(node.getCoords())) {
                Node bestBNode = bestBlack(graph, child, alpha, beta, depthBound, currDepth);
                childless = false;
                if (bestBNode.getScore() >= wNode.getScore()) {
                    wNode = new Node(bestBNode);
                }
                if(alpha < wNode.getScore()){
                    alpha = wNode.getScore();
                }
                if(beta <= alpha){
                   return wNode;
                }
            }
            if(childless){ // I think this happens when the node is not the bottom of the tree but there are no moves afterwords
                return node;
            }
        } else {
            return node;
        }
        return wNode;
    }

    private Node bestBlack(Hashtable<String, ArrayList<Node>> graph, Node node, int alpha, int tbeta, int depthBound, int currDepth) {
        currDepth++;
        Node bNode = new Node(maxScore, false);
        int beta = tbeta;
        boolean childless = true;
        // checks depth
        if (currDepth < depthBound) {
            if (node.isLeaf()) {
                return node;
            }
            for (Node child : graph.get(node.getCoords())) {
                Node bestWNode = bestWhite(graph, child, alpha, beta, depthBound, currDepth);
                childless = false;
                if (bestWNode.getScore() <= bNode.getScore()) {
                    bNode = new Node(bestWNode);
                }
                if(beta > bNode.getScore()){
                    beta = bNode.getScore();
                }
                if(beta <= alpha){
                    return bNode;
                }
            }
            if(childless){ // I think this happens when the node is not the bottom of the tree but there are no moves afterwords
                return node;
            }
        } else {
            return node;
        }
        return bNode;
    }

    private int getCount(int color) {
        if(color == 2){
            return wCount;
        } else {
            return bCount;
        }
    }
}

