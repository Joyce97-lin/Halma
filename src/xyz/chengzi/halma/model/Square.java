package xyz.chengzi.halma.model;

public class Square {//代表每一个方格。两个参数，ChessBoardLocation 相当于行列数，ChessPiece相当于棋子颜色
    private ChessBoardLocation location;
    private ChessPiece piece;

    public Square(ChessBoardLocation location) {
        this.location = location;
    }

    public ChessBoardLocation getLocation() {
        return location;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

}
