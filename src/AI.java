import java.util.ArrayList;
import java.util.Hashtable;

public class AI {

    private int color;
    private Space [][] board;

    public AI (Board board, int color){
        this.board = new Space[8][8];
        this.color = color;
        Space[][] temp = board.getBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++ ) { // copy game board
                this.board[x][y] = new Space(temp[x][y]);
            }
        }
    }

    public String makeMove() {
        ArrayList<Node> moveList = new ArrayList<>();

        Space[][] tempBoard = new Space[8][8];
        // make a tempboard
        for (int b = 0; b < 8; b++) {
            for (int a = 0; a < 8; a++) {
                tempBoard[a][b] = new Space(board[a][b]);
            }
        }

        Hashtable<String, ArrayList<Node>> moveTree = new Hashtable<>(); // tree
        Node root = new Node(false, "r", 0, 0); // deep copy so we don't remove it
        moveTree.put("r", moveList);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y].getColor() == color) {
                    // 5 seconds usually works but we did not want to risk it as it is not one hundred percent
                    for (int d = 0; d < 8; d++){
                        for(int c = 0; c < 8; c++){
                            String parentcords = ""+ x + y + c + d;
                            if (moveHelper(x, y, c, d, color, 5, tempBoard, moveList, parentcords)) {
                                makeMoveDepth(tempBoard, x, y, c, d, color, 5, moveTree, parentcords);
                            }
                        }
                    }
                }
            }
        }
        return ab(moveTree, root, -1, -1, color, 6);
    }

    private void makeMoveDepth (Space[][] tempB, int x, int y, int targetx, int targety,  int myColor, int depth, Hashtable<String, ArrayList<Node>> moveTree, String parentCoords) {
        int target;
        Space[][] tempBoard = new Space[8][8];
        String coords = "" + x + y + targetx + targety;
        ArrayList<Node> moveList = new ArrayList<Node>();

        if (depth > 0) {
            target = Board.getOpponent(myColor);

            for (int b = 0; b < 8; b++) {
                for (int a = 0; a < 8; a++) { // create a temp board
                    tempBoard[a][b] = new Space(tempB[a][b]);
                }
            }
            // makes move
            mover(x,y, targetx, targety, myColor, tempBoard); // make possible move on temp board

            for (int b = 0; b < 8; b++) {
                for (int a = 0; a < 8; a++) {
                    if (tempBoard[a][b].getColor() == target) { // recursively look at all possible moves // this was board
                        for(int d = 0; d < 8; d++){
                            for(int c = 0; c < 8; c++){
                                if (moveHelper(a, b, c, d, target, depth - 1, tempBoard, moveList, parentCoords)) {
                                    makeMoveDepth(tempBoard, a, b, c, d, target, depth - 1, moveTree, parentCoords);
                                }
                            }
                        }
                    }
                }
            }
            moveTree.put(coords, moveList);
        }
    }

    public void updateBoard(Board board) {
        Space[][] temp = board.getBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++ ) {
                this.board[x][y] = new Space(temp[x][y]);
            }
        }
    }
    private int[] calcScore(Space[][] temp){
        int[] scores = new int[2];
        scores[0] = Board.getScore(1, temp);
        scores[1] = Board.getScore(2, temp);
        return scores;
    }

    private void mover(int x, int y, int targetx, int targety, int myColor, Space[][] tempBoard) {

        if(tempBoard[x][y].getType() == 'k') {
            if (x == 4 && targetx == 7) { // is safe is not called since a castle can't immediately put you in check
                if (myColor == 1) {
                    tempBoard[5][0].setPiece("r", 'r', 1);
                } else if (myColor == 2) {
                    tempBoard[5][7].setPiece("R", 'r', 2);
                }
            }
        }
        tempBoard[targetx][targety].setPiece(tempBoard[x][y]);
        tempBoard[x][y].clearSpace();
    }

    private boolean moveHelper(int x, int y, int targetx, int targety, int myColor, int depth, Space[][] tBoard,  ArrayList <Node> movelist, String parentCords) {
        if(x == targetx && y == targety){ // get rid of repeats before a loop is called
            return false;
        } else if(tBoard[targetx][targety].getColor() == myColor && tBoard[x][y].getType() != 'k'){
            return false;
        }

        boolean test = Board.AICheck(x,y,targetx,targety,myColor,tBoard);

        if (test) {
            int[] temp = calcScore(tBoard);
            String coords = "" + x + y + targetx + targety;
            boolean leaf = false;
            if (depth <= 0) { // takes care of leaf nodes
                leaf = true;
            }
            Node newMove = new Node(leaf, coords, temp[0], temp[1], parentCords);
            movelist.add(newMove);
        }
        return test;
    }

    /**
     * main alpha-beta prune method, similar to in project 9
     * instead of bestMin and bestMax, this ab-prune algorithm
     *     tries to maximize the white or black scores
     * @param graph dictionary where keys are moves (ex. "F5") and values are ArrayLists of connected Nodes
     * @param root root of graph ('S' in the Python minimax)
     * @param alpha alpha value
     * @param beta beta value
     * @param player which color the current player is
     * @param depthBound ab-prune depth bound (ex. 5)
     * @return string describing the best move (ex. "F5")
     */
    private String ab(Hashtable<String, ArrayList<Node>> graph, Node root, int alpha, int beta, int player, int depthBound) {
        Node bestMove;
        if (player == 1) {
            bestMove = bestWhite(graph, root, alpha, beta, depthBound, 0);
        } else {
            bestMove = bestBlack(graph, root, alpha, beta, depthBound, 0);
        }
        return bestMove.getParentCoords();
    }

    private Node bestWhite(Hashtable<String, ArrayList<Node>> graph, Node node, int alpha, int beta, int depthBound, int currDepth) {
        currDepth++;
        Node v = new Node(-1, -1, false);
        // checks depth
        if (currDepth < depthBound) {
            if (node.isLeaf()) {
                return node;
            }
            // node is not a leaf by this point so getLabel will work
            for (Node child : graph.get(node.getCoords())) {
                Node v1 = bestBlack(graph, child, alpha, beta, depthBound, currDepth);
                if (v1.getwCount() >= v.getwCount()) {
                    v = new Node(v1);
                }
                if (beta != -1) {
                    if (v1.getwCount() >= beta) {
                        return v;
                    }
                }
                if (alpha == -1 || v1.getwCount() > alpha) {
                    alpha = v1.getwCount();
                }
            }
        } else {
            return node;
        }
        return v;
    }

    private Node bestBlack(Hashtable<String, ArrayList<Node>> graph, Node node, int alpha, int beta, int depthBound, int currDepth) {
        currDepth++;
        Node v = new Node(-1, -1, false);
        // checks depth
        if (currDepth < depthBound) {
            if (node.isLeaf()) {
                return node;
            }
            // node is not a leaf by this point so getLabel will work
            for (Node child : graph.get(node.getCoords())) {

                Node v1 = bestWhite(graph, child, alpha, beta, depthBound, currDepth);
                if (v1.getbCount() >= v.getbCount()) {
                    v = new Node(v1);
                }
                if (alpha != -1) {
                    if (v1.getbCount() >= alpha) {
                        return v;
                    }
                }
                if (beta == -1 || v1.getbCount() > beta) {
                    beta = v1.getbCount();
                }
            }
        } else {
            return node;
        }
        return v;
    }
}

