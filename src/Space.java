/**
class for each space on the board and its respective piece
*/
public class Space {
    private int color; // the color of the piece on the board
    private char type; // type of piece
    private String id; // how the piece is displayed
    private String coords; // for debugging displays coordinates as a string

    // constructor
    public Space(String coords) {
        this.color = 0;
        this.type = 'e';
        this.id = " ";
        this.coords = coords;
    }

    public void setType(char type){
        this.type = type;
    }

    public void setId(String id){
        this.id = id;
    }


    // copy constructor
    Space(Space space){
        this.color = space.getColor();
        this.type = space.getType();
        this.id = space.getId();
        this.coords = space.getCoords();
    }

    // colors get method
    public int getColor() {
        return color;
    }

    // returns space's coordinates
    public String getCoords(){
        return coords;
    }

    // clears the space
    public void clearSpace(){
        color = 0;
        type = 'e';
        id = " ";
    }

    // simple call for checking if a piece is a rook used in castling
    public boolean isRook(int color){
        return type == 'r' && this.color == color;
    }

    // simple call for if a piece is a pawn used in castling
    public boolean isPawn(int color){
        return type == 'p' && this.color == color;
    }

    // simple call for if a piece is a king
    public boolean isKing(int color){
        return type == 'k' && this.color == color;
    }

    // sets the piece in one call
    public void setPiece(String id, char type, int color){
        this.id = id;
        this.type = type;
        this.color = color;
    }

    // is piece's overwrite method
    public void setPiece(Space space){
        id = space.getId();
        type = space.getType();
        color = space.getColor();
    }

    // returns the type
    public char getType() {
        return type;
    }

    // returns the id
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
