// is the node for the ML

public class Node {
    private boolean leaf;
    private String coords = "";
    private int wCount = -1;
    private int bCount = -1;
    private String parentCoords;

    public String getParentCoords() {
        return parentCoords;
    }

    public int getwCount() {
        return wCount;
    }

    public int getbCount() {
        return bCount;
    }


    public Node(Node other) {
        this.bCount = other.getbCount();
        this.wCount = other.getwCount();
        this.leaf = other.isLeaf();
        this.coords = other.getCoords();
        this.parentCoords = other.getParentCoords();
    }

    public Node(boolean leaf, String coords, int wCount, int bCount) {
        this.leaf = leaf;
        this.coords = coords;
        this.wCount = wCount;
        this.bCount = bCount;
    }

    public Node(boolean leaf, String coords, int wCount, int bCount, String parentCoords) {
        this.leaf = leaf;
        this.coords = coords;
        this.wCount = wCount;
        this.bCount = bCount;
        this.parentCoords = parentCoords;
    }

    public Node(int wCount, int bCount, boolean leaf) {
        this.wCount = wCount;
        this.bCount = bCount;
        this.leaf = leaf;
        this.parentCoords = "NA";
        this.coords = "NA";
    }



    public boolean isLeaf() {
        return leaf;
    }

    public String getCoords() {
        return coords;
    }
}
