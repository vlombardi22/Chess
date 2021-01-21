public class King {
    private int bx;
    private int by;
    private int wx;
    private int wy;
    private boolean bcheck;
    private boolean wcheck;

    public King(int bx, int by, int wx, int wy){
        this.bx = bx;
        this.by = by;
        this.wx = wx;
        this.wy = wy;
        bcheck = false;
        wcheck = false;
    }


    public void move(int x, int y, int color){
        if(color == 1){
            bx = x;
            by = y;
        } else {
            wx = x;
            wy = y;
        }
    }

    public boolean isCheck(int color){
        if(color == 1){
            return bcheck;
        } else {
            return wcheck;
        }
    }

    public int getX(int color) {
        if(color == 1){
            return bx;
        } else {
            return wx;
        }

    }

    public int getY(int color) {
        if(color == 1){
            return by;
        } else {
            return wy;
        }

    }

    public void setCheck(boolean temp, int color){
        if(color == 1){
            bcheck = temp;
        } else {
            wcheck = temp;
        }
    }

    public void clear(){
        bcheck = false;
        wcheck = false;
    }

}
