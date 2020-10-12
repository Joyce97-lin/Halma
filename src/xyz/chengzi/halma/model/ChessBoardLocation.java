package xyz.chengzi.halma.model;

import java.util.Objects;

public class ChessBoardLocation {
    private int row, column;
    private ChessBoardLocation motherLocation;
    private int count;

    public ChessBoardLocation getMotherLocation() {
        return motherLocation;
    }

    public int getCount() {
        return count;
    }

    public void setMotherLocation(ChessBoardLocation motherLocation) {
        this.motherLocation = motherLocation;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ChessBoardLocation(int row, int column) {//设置方格，即棋子的行列数，来确定其在棋盘中的位置
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {//覆写的equals，可以直接比较ChessBoardLocation对象是否相等
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoardLocation location = (ChessBoardLocation) o;
        return row == location.row && column == location.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public ChessBoardLocation() {
        this.count = -1;
    }
}
