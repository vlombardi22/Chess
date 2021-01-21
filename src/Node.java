/**
This class is the node for the ML
*/

public class Node {
    private boolean leaf;
    private String coords;
    private String parentCoords;
    private int score;

    // copy constructor
    public Node(Node other) {
        this.leaf = other.isLeaf();
        this.coords = other.getCoords();
        this.parentCoords = other.getParentCoords();
        this.score = other.getScore();
    }

    // root constructor
    public Node(boolean leaf, String coords, int score){
        this.leaf = leaf;
        this.coords = coords;
        this.score = score;
    }


    public Node(boolean leaf, String coords, int score, String parentCoords) {
        this.leaf = leaf;
        this.coords = coords;
        this.score = score;
        this.parentCoords = parentCoords;
    }

    // constructor from best black or best white
    public Node(int score, boolean leaf){
        this.score = score;
        this.leaf = leaf;
        parentCoords = "9999";
        coords = "9999";

    }


    public String getParentCoords() {
        return parentCoords;
    }

    public int getScore() { return score; }

    public boolean isLeaf() {
        return leaf;
    }

    public String getCoords() {
        return coords;
    }


}
