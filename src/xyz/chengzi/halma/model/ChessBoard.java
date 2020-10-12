package xyz.chengzi.halma.model;

import xyz.chengzi.halma.listener.GameListener;
import xyz.chengzi.halma.listener.Listenable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard implements Listenable<GameListener> {
    private List<GameListener> listenerList = new ArrayList<>();
    private Square[][] grid;// Square的二维数组，用于存放棋子，并用索引值代表棋子的位置 grid！！！
    private int dimension;//用来设置整个棋盘的行列数！
    ArrayList<ChessBoardLocation> AllValidLocation = new ArrayList<>();//存储有效位置的ArrayList
    private int number;
    ArrayList<ChessBoardLocation> queue = new ArrayList<>();
    ArrayList<ChessBoardLocation> wayToDest=new ArrayList<>();

    public ArrayList<ChessBoardLocation> getQueue() {
        return queue;
    }

    public ArrayList<ChessBoardLocation> getWayToDest() {
        return wayToDest;
    }

    public void setQueue(ArrayList<ChessBoardLocation> queue) {
        this.queue = queue;
    }

    public void setWayToDest(ArrayList<ChessBoardLocation> wayToDest) {
        this.wayToDest = wayToDest;
    }

    public ChessBoard(int dimension) {
        this.grid = new Square[dimension][dimension];
        this.dimension = dimension;

        initGrid();//初始化grid，即棋子数组
    }

    private void initGrid() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = new Square(new ChessBoardLocation(i, j));
            }
        }
    }

    public void loadInititalPieces() {//加载棋盘的时候用到的，将所有的piece都初始化，然后将棋盘导入
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j].setPiece(null);
            }
        }
        listenerList.forEach(listener -> listener.onChessBoardReload(this));
    }

    public void loadOver() {
        listenerList.forEach(listener -> listener.onChessBoardReload(this));
    }//注意两个都添加了监听器//调用了chessboardcomponent中的onchessroardload

    public void setColorPieceInGrid(int row, int colunm, Color color) {
        grid[row][colunm].setPiece(new ChessPiece(color));
    }

    public void placeInitialPieces(int num1) {//初始化棋子的颜色和位置，即显示了棋子。同时在restart过程中初始化棋子。
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j].setPiece(null);
            }
        }
        number = num1;
        // TODO: This is only a demo implementation
        if (num1 == 2) {
            grid[0][0].setPiece(new ChessPiece(Color.RED));
            grid[0][1].setPiece(new ChessPiece(Color.RED));
            grid[0][2].setPiece(new ChessPiece(Color.RED));
            grid[0][3].setPiece(new ChessPiece(Color.RED));
            grid[0][4].setPiece(new ChessPiece(Color.RED));
            grid[1][0].setPiece(new ChessPiece(Color.RED));
            grid[1][1].setPiece(new ChessPiece(Color.RED));
            grid[1][2].setPiece(new ChessPiece(Color.RED));
            grid[1][3].setPiece(new ChessPiece(Color.RED));
            grid[1][4].setPiece(new ChessPiece(Color.RED));
            grid[2][0].setPiece(new ChessPiece(Color.RED));
            grid[2][1].setPiece(new ChessPiece(Color.RED));
            grid[2][2].setPiece(new ChessPiece(Color.RED));
            grid[2][3].setPiece(new ChessPiece(Color.RED));
            grid[3][0].setPiece(new ChessPiece(Color.RED));
            grid[3][1].setPiece(new ChessPiece(Color.RED));
            grid[3][2].setPiece(new ChessPiece(Color.RED));
            grid[4][0].setPiece(new ChessPiece(Color.RED));
            grid[4][1].setPiece(new ChessPiece(Color.RED));
            grid[dimension - 1][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 4].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 5].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 4].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 5].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 4].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 4][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 4][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 4][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 5][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 5][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
        } else {
            grid[0][0].setPiece(new ChessPiece(Color.RED));
            grid[0][1].setPiece(new ChessPiece(Color.RED));
            grid[0][2].setPiece(new ChessPiece(Color.RED));
            grid[0][3].setPiece(new ChessPiece(Color.RED));
            grid[1][0].setPiece(new ChessPiece(Color.RED));
            grid[1][1].setPiece(new ChessPiece(Color.RED));
            grid[1][2].setPiece(new ChessPiece(Color.RED));
            grid[1][3].setPiece(new ChessPiece(Color.RED));
            grid[2][0].setPiece(new ChessPiece(Color.RED));
            grid[2][1].setPiece(new ChessPiece(Color.RED));
            grid[2][2].setPiece(new ChessPiece(Color.RED));
            grid[3][0].setPiece(new ChessPiece(Color.RED));
            grid[3][1].setPiece(new ChessPiece(Color.RED));
            grid[dimension - 1][0].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 1][1].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 1][2].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 1][3].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 2][0].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 2][1].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 2][2].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 2][3].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 3][0].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 3][1].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 3][2].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 4][0].setPiece(new ChessPiece(Color.BLACK));
            grid[dimension - 4][1].setPiece(new ChessPiece(Color.BLACK));
            grid[0][dimension - 1].setPiece(new ChessPiece(Color.BLUE));
            grid[0][dimension - 2].setPiece(new ChessPiece(Color.BLUE));
            grid[0][dimension - 3].setPiece(new ChessPiece(Color.BLUE));
            grid[0][dimension - 4].setPiece(new ChessPiece(Color.BLUE));
            grid[1][dimension - 1].setPiece(new ChessPiece(Color.BLUE));
            grid[1][dimension - 2].setPiece(new ChessPiece(Color.BLUE));
            grid[1][dimension - 3].setPiece(new ChessPiece(Color.BLUE));
            grid[1][dimension - 4].setPiece(new ChessPiece(Color.BLUE));
            grid[2][dimension - 1].setPiece(new ChessPiece(Color.BLUE));
            grid[2][dimension - 2].setPiece(new ChessPiece(Color.BLUE));
            grid[2][dimension - 3].setPiece(new ChessPiece(Color.BLUE));
            grid[3][dimension - 1].setPiece(new ChessPiece(Color.BLUE));
            grid[3][dimension - 2].setPiece(new ChessPiece(Color.BLUE));
            grid[dimension - 1][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 1][dimension - 4].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 2][dimension - 4].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 2].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 3][dimension - 3].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 4][dimension - 1].setPiece(new ChessPiece(Color.GREEN));
            grid[dimension - 4][dimension - 2].setPiece(new ChessPiece(Color.GREEN));


        }
        listenerList.forEach(listener -> listener.onChessBoardReload(this));
    }

    public Square getGridAt(ChessBoardLocation location) {
        return grid[location.getRow()][location.getColumn()];
    }//传入一个chessBoardLocation对象，利用其记录的行列数，返回一个grid记录的square对象，即通过位置找到对应棋子

    public Square getGridAt(int row, int column) {
        return grid[row][column];
    }

    public ChessPiece getChessPieceAt(ChessBoardLocation location) {
        return getGridAt(location).getPiece();
    }//返回对应位置的棋子的颜色，通过前面的getGridAt来实现

    public ChessPiece getChessPieceAt(int row, int column) {
        return getGridAt(row, column).getPiece();
    }

    public void setChessPieceAt(ChessBoardLocation location, ChessPiece piece) {
        getGridAt(location).setPiece(piece);
        listenerList.forEach(listener -> listener.onChessPiecePlace(location, piece));
    }//设置对应位置的棋子的颜色，通过前面的getGridAt来实现

    public ChessPiece removeChessPieceAt(ChessBoardLocation location) {//移除对应位置的棋子，并返回移除棋子的颜色
        ChessPiece piece = getGridAt(location).getPiece();//获取对应位置的棋子颜色
        getGridAt(location).setPiece(null);//将对应位置的棋子颜色设置为空
        listenerList.forEach(listener -> listener.onChessPieceRemove(location));
        return piece;
    }

    public void moveChessPiece(ChessBoardLocation src, ChessBoardLocation dest) {//移动对应位置的棋子到目的位置的方法
        if (!isValidMove(src, dest)) {//如果不可以移动，报错
            throw new IllegalArgumentException("Illegal halma move");
        }
        setChessPieceAt(dest, removeChessPieceAt(src));//前面写的方法，在对应位置（dest）上设置对应颜色（remove方法的返回值）
    }

    public int getDimension() {
        return dimension;
    }

    public boolean EveryIsValidMove(ChessBoardLocation src, ChessBoardLocation dest) {//src是起始位置，dest是目标位置（没有棋子在上面才可）  ！！其实光这一个方法就可以实现不转弯的连跳了
        int srcRow = src.getRow(), srcCol = src.getColumn(), destRow = dest.getRow(), destCol = dest.getColumn();
        int rowDistance = destRow - srcRow, colDistance = destCol - srcCol;
        if(getGrid(src.getRow(), src.getColumn()).getPiece()!=null) {
            if (number == 2) {
                if (red2(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.GREEN)) {
                    if (!red2(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
                if (green2(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.RED)) {
                    if (!green2(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
            } else {
                if (red4(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.GREEN)) {
                    if (!red4(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
                if (green4(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.RED)) {
                    if (!green4(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
                if (black(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.BLUE)) {
                    if (!black(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
                if (blue(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.BLACK)) {
                    if (!blue(dest.getRow(), dest.getColumn())) {
                        return false;
                    }
                }
            }
        }
        if (Math.abs(colDistance) == 1 || Math.abs(rowDistance) == 1) {
            if (getChessPieceAt(src) == null && getChessPieceAt(dest) == null) {
                return false;
            }
        }
        if (getChessPieceAt(dest) != null) {
            return false;
        }
        if (rowDistance != 0 && colDistance != 0 && Math.abs((double) rowDistance / colDistance) != 1.0) {//判断是否是左右上下移动且横纵坐标移动是否是相等
            return false;
        }
        if (Math.abs(rowDistance) == Math.abs(colDistance)) {
            if (Math.abs(rowDistance) == 2 && Math.abs(colDistance) == 2) {
                if (getChessPieceAt(src.getRow() + rowDistance / 2, src.getColumn() + colDistance / 2) == null || getChessPieceAt(dest) != null) {
                    return false;
                }
            } else if (Math.abs(rowDistance) > 2 && Math.abs(colDistance) > 2) {//对角隔一个棋子跳一步
                for (int i = 2; i <= Math.abs(rowDistance); i = i + 2) {
                    if (getChessPieceAt(src.getRow() + i * rowDistance / Math.abs(rowDistance), src.getColumn() + i * colDistance / Math.abs(colDistance)) != null
                            || getChessPieceAt(src.getRow() + (i - 1) * rowDistance / Math.abs(rowDistance), src.getColumn() + (i - 1) * colDistance / Math.abs(colDistance)) == null) {
                        return false;
                    }
                    if (i + 1 <= Math.abs(rowDistance) && getChessPieceAt(src.getRow() + (i + 1) * rowDistance / Math.abs(rowDistance), src.getColumn() + (i + 1) * colDistance / Math.abs(colDistance)) == null) {
                        return false;
                    }
                }
            }
        }//对角连跳

        if (Math.abs(rowDistance) == 0 && Math.abs(colDistance) != 0) {
            if (Math.abs(colDistance) == 2) {
                if (getChessPieceAt(src.getRow(), src.getColumn() + colDistance / 2) == null || getChessPieceAt(dest) != null) {
                    return false;//左右隔一个棋子跳一步
                }
            } else if (Math.abs(colDistance) > 2) {
                for (int i = 2; i <= Math.abs(colDistance); i = i + 2) {
                    if (getChessPieceAt(src.getRow(), src.getColumn() + i * colDistance / Math.abs(colDistance)) != null
                            || getChessPieceAt(src.getRow(), src.getColumn() + (i - 1) * colDistance / Math.abs(colDistance)) == null) {
                        return false;
                    }
                    if (i + 1 <= Math.abs(colDistance) && getChessPieceAt(src.getRow(), src.getColumn() + (i + 1) * colDistance / Math.abs(colDistance)) == null) {
                        return false;
                    }
                }
            }//左右连跳
        }
        if (Math.abs(colDistance) == 0 && Math.abs(rowDistance) != 0) {
            if (Math.abs(rowDistance) == 2) {
                if (getChessPieceAt(src.getRow() + rowDistance / 2, src.getColumn()) == null || getChessPieceAt(dest) != null) {
                    return false;//上下隔一个棋子跳一步
                }
            } else if (Math.abs(rowDistance) > 2) {
                for (int i = 2; i <= Math.abs(rowDistance); i = i + 2) {
                    if (getChessPieceAt(src.getRow() + i * rowDistance / Math.abs(rowDistance), src.getColumn()) != null
                            || getChessPieceAt(src.getRow() + (i - 1) * rowDistance / Math.abs(rowDistance), src.getColumn()) == null) {
                        return false;
                    }
                    if (i + 1 <= Math.abs(rowDistance) && getChessPieceAt(src.getRow() + (i + 1) * rowDistance / Math.abs(rowDistance), src.getColumn()) == null) {
                        return false;
                    }
                }//上下连跳
            }
        }
        if (Math.abs(colDistance) != Math.abs(rowDistance) && Math.abs(rowDistance) > 0 && Math.abs(colDistance) > 0) {
            return false;
        }
        return true;
    }
    public void isValidLocation (ChessBoardLocation src){//这个方法相当于遍历了当前起始位置周围的位置，设了对象，判断是不是符合跳的条件，符合就加到ArrayList里面
        ChessBoardLocation[] rightMove = new ChessBoardLocation[dimension - src.getColumn()];//向右
        for (int i = 1; i < dimension - src.getColumn(); i++) {
            rightMove[i] = new ChessBoardLocation(src.getRow(), src.getColumn() + i);
            if (EveryIsValidMove(src, rightMove[i])) {
                if (!AllValidLocation.contains(rightMove[i])) {
                    AllValidLocation.add(rightMove[i]);
                }
            } else {
                if (i > 1) {//这个是我意识到，如果起点右边两步的地方不能跳的话，这一行都不用测了，下面一样（想减少占用内存啊qwq）
                    break;
                }
            }
            rightMove[i] = null;//我想把不用了的对象释放掉来着，感觉没啥用qwq
        }
        ChessBoardLocation[] leftMove = new ChessBoardLocation[src.getColumn() + 1];//向左
        for (int i = 1; i < src.getColumn() + 1; i++) {
            leftMove[i] = new ChessBoardLocation(src.getRow(), src.getColumn() - i);
            if (EveryIsValidMove(src, leftMove[i])) {
                if (!AllValidLocation.contains(leftMove[i])) {
                    AllValidLocation.add(leftMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            leftMove[i] = null;
        }
        ChessBoardLocation[] upMove = new ChessBoardLocation[src.getRow() + 1];//向上
        for (int i = 1; i <= src.getRow(); i++) {
            upMove[i] = new ChessBoardLocation(src.getRow() - i, src.getColumn());
            if (EveryIsValidMove(src, upMove[i])) {
                if (!AllValidLocation.contains(upMove[i])) {
                    AllValidLocation.add(upMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            upMove[i] = null;
        }
        ChessBoardLocation[] downMove = new ChessBoardLocation[dimension - src.getRow()];//向下
        for (int i = 1; i < dimension - src.getRow(); i++) {
            downMove[i] = new ChessBoardLocation(src.getRow() + i, src.getColumn());
            if (EveryIsValidMove(src, downMove[i])) {
                if (!AllValidLocation.contains(downMove[i])) {
                    AllValidLocation.add(downMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            downMove[i] = null;
        }
        ChessBoardLocation[] rightUpMove = new ChessBoardLocation[Math.min(dimension - src.getColumn(), src.getRow()) + 1];//右上
        for (int i = 1; i < Math.min(dimension - src.getColumn(), src.getRow() + 1); i++) {
            rightUpMove[i] = new ChessBoardLocation(src.getRow() - i, src.getColumn() + i);
            if (EveryIsValidMove(src, rightUpMove[i])) {
                if (!AllValidLocation.contains(rightUpMove[i])) {
                    AllValidLocation.add(rightUpMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            rightUpMove[i] = null;
        }
        ChessBoardLocation[] rightDownMove = new ChessBoardLocation[Math.min(dimension - src.getColumn(), dimension - src.getRow())];//右下
        for (int i = 1; i < Math.min(dimension - src.getColumn(), dimension - src.getRow()); i++) {
            rightDownMove[i] = new ChessBoardLocation(src.getRow() + i, src.getColumn() + i);
            if (EveryIsValidMove(src, rightDownMove[i])) {
                if (!AllValidLocation.contains(rightDownMove[i])) {
                    AllValidLocation.add(rightDownMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            rightDownMove[i] = null;
        }
        ChessBoardLocation[] leftUpMove = new ChessBoardLocation[Math.min(src.getColumn() + 1, src.getRow()) + 1];//左上
        for (int i = 1; i <= Math.min(src.getColumn(), src.getRow()); i++) {
            leftUpMove[i] = new ChessBoardLocation(src.getRow() - i, src.getColumn() - i);
            if (EveryIsValidMove(src, leftUpMove[i])) {
                if (!AllValidLocation.contains(leftUpMove[i])) {
                    AllValidLocation.add(leftUpMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            leftUpMove[i] = null;
        }
        ChessBoardLocation[] leftDownMove = new ChessBoardLocation[Math.min(src.getColumn() + 1, dimension - src.getRow())];//左下
        for (int i = 1; i < Math.min(src.getColumn() + 1, dimension - src.getRow()); i++) {
            leftDownMove[i] = new ChessBoardLocation(src.getRow() + i, src.getColumn() - i);
            if (EveryIsValidMove(src, leftDownMove[i])) {
                if (!AllValidLocation.contains(leftDownMove[i])) {
                    AllValidLocation.add(leftDownMove[i]);
                }
            } else {
                if (i > 1) {
                    break;
                }
            }
            leftDownMove[i] = null;
        }
    }
    public boolean isValidMove (ChessBoardLocation src, ChessBoardLocation dest)
    {//这个就是拿最终的目标位置跟ArrayList里面的比，看在不在里面，这里有个把起点周围的周围一直往下面测的循环
        if (EveryIsValidMove(src, dest)) {
            return true;
        }
        if (AllValidLocation.contains(dest)) {
            return true;
        }
        return false;
    }
    public void allValidLocation (ChessBoardLocation src){
        AllValidLocation.clear();//避免滚雪球
        isValidLocation(src);//第一层先搞进去
        int count = 0;//用来看每一层有多少个有效位置，避免重复判断
        int loop = 0;//用来判断是不是已经没有对象往ArrayList里面存了
        while (loop != AllValidLocation.size()) {//第二层及后面
            loop = AllValidLocation.size();
            if (count != -1) {
                for (int i = count; i < AllValidLocation.size(); i++) {
                    if ((Math.abs(src.getColumn() - AllValidLocation.get(i).getColumn()) != 0 || Math.abs(src.getRow() - AllValidLocation.get(i).getRow()) != 1) &&
                            (Math.abs(src.getColumn() - AllValidLocation.get(i).getColumn()) != 1 || Math.abs(src.getRow() - AllValidLocation.get(i).getRow()) != 0) &&
                            (Math.abs(src.getColumn() - AllValidLocation.get(i).getColumn()) != 1 || Math.abs(src.getRow() - AllValidLocation.get(i).getRow()) != 1)) {
                        isValidLocation(AllValidLocation.get(i));//第二层及以后的判断
                    }
                }
                count = AllValidLocation.size() - 1;
            }
                if(getGrid(src.getRow(), src.getColumn()).getPiece()!=null) {
                    if (number == 2) {
                        if (red2(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.GREEN)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!red2(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                        if (green2(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.RED)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!green2(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                    } else {
                        if (red4(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.GREEN)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!red4(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                        if (green4(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.RED)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!green4(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                        if (black(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.BLUE)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!black(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                        if (blue(src.getRow(), src.getColumn()) && getGrid(src.getRow(), src.getColumn()).getPiece().getColor().equals(Color.BLACK)) {
                            for (int i = 0; i <AllValidLocation.size() ; i++) {
                                if(!blue(AllValidLocation.get(i).getRow(), AllValidLocation.get(i).getColumn())){
                                    AllValidLocation.remove(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    public Square getGrid ( int row, int column){
        return grid[row][column];
    }

    public Square[][] getGrid () {
        return grid;
    }

    @Override
    public void registerListener (GameListener listener){
        listenerList.add(listener);
    }

    @Override
    public void unregisterListener (GameListener listener){
        listenerList.remove(listener);
    }

    public ArrayList<ChessBoardLocation> getAllValidLocation () {
        return AllValidLocation;
    }

    public void setAllValidLocation (ArrayList < ChessBoardLocation > allValidLocation) {
        AllValidLocation = allValidLocation;
    }
    public boolean red2 ( int row, int column){
        return ((row == 0 && column == 0) || (row == 0 && column == 1) || (row == 0 && column == 2) || (row == 0 && column == 3) || (row == 0 && column == 4)
                || (row == 1 && column == 0) || (row == 1 && column == 1) || (row == 1 && column == 2) || (row == 1 && column == 3) || (row == 1 && column == 4)
                || (row == 2 && column == 0) || (row == 2 && column == 1) || (row == 2 && column == 2) || (row == 2 && column == 3)
                || (row == 3 && column == 0) || (row == 3 && column == 1) || (row == 3 && column == 2)
                || (row == 4 && column == 0) || (row == 4 && column == 1));
    }
    public boolean green2 ( int row, int column){
        return ((row == dimension-1 && column == dimension-1) || (row == dimension-1 && column == dimension-2) || (row == dimension-1 && column == dimension-3) || (row == dimension-1 && column == dimension-4) || (row == dimension-1 && column == dimension-5)
                || (row == dimension-2 && column == dimension-1) || (row ==dimension-2  && column == dimension-2) || (row == dimension-2 && column == dimension-3) || (row ==dimension-2  && column == dimension-4) || (row == dimension-2 && column == dimension-5)
                || (row == dimension-3 && column == dimension-1) || (row == dimension-3 && column == dimension-2) || (row == dimension-3 && column == dimension-3) || (row == dimension-3 && column == dimension-4)
                || (row == dimension-4 && column == dimension-1) || (row == dimension-4 && column == dimension-2) || (row == dimension-4 && column == dimension-3)
                || (row == dimension-5 && column == dimension-1) || (row == dimension-5 && column == dimension-2));
    }
    public boolean red4(int row,int column){
        return ((row == 0 && column == 0) || (row == 0 && column == 1) || (row == 0 && column == 2) || (row == 0 && column == 3)
                || (row == 1 && column == 0) || (row == 1 && column == 1) || (row == 1 && column == 2) || (row == 1 && column == 3)
                || (row == 2 && column == 0) || (row == 2 && column == 1) || (row == 2 && column == 2)
                || (row == 3 && column == 0) || (row == 3 && column == 1));
    }
    public boolean blue(int row,int column){
        return ((row == 0 && column == dimension-1) || (row == 0 && column == dimension-2) || (row == 0 && column == dimension-3) || (row == 0 && column == dimension-4)
            || (row == 1 && column == dimension-1) || (row == 1 && column == dimension-2) || (row == 1 && column ==dimension-3 ) || (row == 1 && column == dimension-4)
            || (row == 2 && column == dimension-1) || (row == 2 && column == dimension-2) || (row == 2 && column == dimension-3)
            || (row == 3 && column == dimension-1) || (row == 3 && column == dimension-2));
}
    public boolean green4(int row,int column){
        return ((row == dimension-1 && column == dimension-1) || (row == dimension-1 && column == dimension-2) || (row == dimension-1 && column == dimension-3) || (row == dimension-1 && column == dimension-4)
                || (row == dimension-2 && column == dimension-1) || (row ==dimension-2  && column == dimension-2) || (row == dimension-2 && column == dimension-3) || (row ==dimension-2  && column == dimension-4)
                || (row == dimension-3 && column == dimension-1) || (row == dimension-3 && column == dimension-2) || (row == dimension-3 && column == dimension-3)
                || (row == dimension-4 && column == dimension-1) || (row == dimension-4 && column == dimension-2));
    }
    public boolean black(int row,int column){
        return ((row == dimension-1 && column == 0) || (row == dimension-1 && column == 1) || (row == dimension-1 && column == 2) || (row == dimension-1 && column == 3)
                || (row == dimension-2 && column == 0) || (row == dimension-2 && column == 1) || (row == dimension-2 && column == 2) || (row == dimension-2 && column == 3)
                || (row == dimension-3 && column == 0) || (row ==dimension-3  && column == 1) || (row == dimension-3 && column == 2)
                || (row == dimension-4 && column == 0) || (row == dimension-4 && column == 1));
    }
    public boolean isLocationInArrayList(ChessBoardLocation checklocation) {
        return AllValidLocation.contains(checklocation);
    }

    public void setSrcLocationBFS(ChessBoardLocation src) {
        getGrid(src.getRow(), src.getColumn()).getLocation().setCount(0);
    }

    public boolean checkTwoLocationEuqals(ChessBoardLocation src, ChessBoardLocation dest) {
        if (src.getRow() == dest.getRow() && src.getColumn() == dest.getColumn()) {
            return true;
        }
        return false;
    }

    public boolean changeEightLocation(ChessBoardLocation src, ChessBoardLocation dest) {
        int row = src.getRow();
        int column = src.getColumn();
        int countNumber = src.getCount();
        int rowD=dest.getRow();
        int columnD=dest.getColumn();
        ChessPiece chesspiece=getGrid(rowD,columnD).getPiece();
        getGrid(rowD,columnD).setPiece(null);
        try {
            if (getChessPieceAt(row - 1, column - 1)!=null&&getChessPieceAt(row-2,column-2)==null) {
                int lastNmber = getGrid(row - 2, column - 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row - 2, column - 2).getLocation().setCount(countNumber + 1);
                    getGrid(row - 2, column - 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row - 2, column - 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row - 2, column - 2).getLocation());

                }
            }

        }catch (Exception e){

        }
        try {
            if (getChessPieceAt(row - 1, column )!=null&&getChessPieceAt(row-2,column)==null) {
                int lastNmber = getGrid(row - 2, column).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row - 2, column).getLocation().setCount(countNumber + 1);
                    getGrid(row - 2, column).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row - 2, column).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row - 2, column).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row - 1, column + 1)!=null&&getChessPieceAt(row-2,column+2)==null) {
                int lastNmber = getGrid(row - 2, column + 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row - 2, column + 2).getLocation().setCount(countNumber + 1);
                    getGrid(row - 2, column + 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row - 2, column + 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row - 2, column + 2).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row , column - 1)!=null&&getChessPieceAt(row,column-2)==null) {
                int lastNmber = getGrid(row, column - 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row, column - 2).getLocation().setCount(countNumber + 1);
                    getGrid(row, column - 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row, column - 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row, column - 2).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row , column + 1)!=null&&getChessPieceAt(row,column+2)==null) {
                int lastNmber = getGrid(row, column + 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row, column + 2).getLocation().setCount(countNumber + 1);
                    getGrid(row, column + 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row, column + 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row, column + 2).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row +1, column -1)!=null&&getChessPieceAt(row+2,column-2)==null) {
                int lastNmber = getGrid(row + 2, column - 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row + 2, column - 2).getLocation().setCount(countNumber + 1);
                    getGrid(row + 2, column - 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row + 2, column - 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row + 2, column - 2).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row +1, column )!=null&&getChessPieceAt(row+2,column)==null) {
                int lastNmber = getGrid(row + 2, column).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row + 2, column).getLocation().setCount(countNumber + 1);
                    getGrid(row + 2, column).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row + 2, column).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row + 2, column).getLocation());
                }

            }
        }catch (Exception e){

        }
        try {


            if (getChessPieceAt(row+1 , column + 1)!=null&&getChessPieceAt(row+2,column+2)==null) {
                int lastNmber = getGrid(row + 2, column + 2).getLocation().getCount();
                if (lastNmber == -1) {
                    getGrid(row + 2, column + 2).getLocation().setCount(countNumber + 1);
                    getGrid(row + 2, column + 2).getLocation().setMotherLocation(src);
                    if (checkTwoLocationEuqals(dest, getGrid(row + 2, column + 2).getLocation())) {
                        getGrid(rowD,columnD).setPiece(chesspiece);
                        return true;
                    }
                    this.queue.add(getGrid(row + 2, column + 2).getLocation());
                }

            }
        }catch (Exception e){


        }
        getGrid(rowD,columnD).setPiece(chesspiece);
        return false;


    }
    public void startBFS(ChessBoardLocation src,ChessBoardLocation dest){
        queue.add(src);
        while ( queue.isEmpty()==false){
            ChessBoardLocation searchLocation =queue.get(0);
            queue.remove(0);
            if(changeEightLocation(searchLocation,dest)){
                break;
            }
            else {
                continue;
            }
        }
    }
    public void getBFSWay(ChessBoardLocation src,ChessBoardLocation dest){
        ChessBoardLocation currentLocation=getGrid(src.getRow(),src.getColumn()).getLocation();
        while(checkTwoLocationEuqals(currentLocation,dest)==false){
            wayToDest.add(currentLocation.getMotherLocation());
            if(currentLocation.getMotherLocation()==null){
                break;
            };
            currentLocation=currentLocation.getMotherLocation();
        }

    }
    public void initAllLocationCount(){
        this.wayToDest.clear();
        this.queue.clear();
        for(int i=0;i<dimension;i++){
            for (int j = 0; j <dimension ; j++) {
                getGridAt(i,j).getLocation().setCount(-1);
                getGridAt(i,j).getLocation().setMotherLocation(null);

            }
        }
    }
    public void addSRCIntoAllList(ChessBoardLocation src){
        this.AllValidLocation.add(src);
    }
    public void deleteSRCFromAllList(){
        this.AllValidLocation.remove(AllValidLocation.size()-1);
    }
    public void BFSIniWayList(){
        wayToDest.clear();
    }
    public void IniQueue(){
        this.queue.clear();
    }

}
