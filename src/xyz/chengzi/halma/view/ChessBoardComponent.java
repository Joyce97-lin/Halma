package xyz.chengzi.halma.view;

import xyz.chengzi.halma.listener.GameListener;
import xyz.chengzi.halma.listener.InputListener;
import xyz.chengzi.halma.listener.Listenable;
import xyz.chengzi.halma.model.ChessBoard;
import xyz.chengzi.halma.model.ChessBoardLocation;
import xyz.chengzi.halma.model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardComponent extends JComponent implements Listenable<InputListener>, GameListener {//相当于整个棋盘加棋子的总和，是画出来的表示
    private static final Color BOARD_COLOR_1 = new Color(255,250,205);//这两个颜色就是棋盘的两种颜色qwq
    private static final Color BOARD_COLOR_2 = new Color(255,200,100);

    private List<InputListener> listenerList = new ArrayList<>();//可能是监听的list。。似乎model里也有类似的列表
    private SquareComponent[][] gridComponents;//棋盘背景的数组
    private int dimension;//设置是几×几的棋盘
    private int gridSize;//应该是棋盘每一格的大小
    private int people;


    public ChessBoardComponent(int size, int dimension,int num) {//构造方法，传入大小和维数
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLayout(null); // Use absolute layout
        setSize(size, size);//利用传入的大小来设置窗口的大小
        people=num;
        this.gridComponents = new SquareComponent[dimension][dimension];//利用传入的维数来构造背景数组
        this.dimension = dimension;
        this.gridSize = size / dimension;//所以gridSize的意思就是每个小格子的大小，由窗口总大小除维数得到
        initGridComponents();
    }

    private void initGridComponents() {//初始化代码，即初始化背景数组
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                //然后把这个背景添加到画板上，把这行代码注释掉之后棋子也不见了，棋子是和背景绑定在一起的
                //设置对应小方块的x，y坐标，这个方法似乎是JFrame的方法，吗？（qwq感觉不是）
                if (initialSide(row, col,people)) {
                    gridComponents[row][col] = new SquareComponent(gridSize, Color.LIGHT_GRAY);
                } else {
                    gridComponents[row][col] = new SquareComponent(gridSize,//这个方法是SquareComponent那个类里面的构造方法
                            (row + col) % 2 == 0 ? BOARD_COLOR_1 : BOARD_COLOR_2);//用了一行布尔表达式来实现棋盘交替颜色，前一个参数为小方块的大小
                }
                gridComponents[row][col].setLocation(col * gridSize, row * gridSize);//设置对应小方块的x，y坐标，这个方法似乎是JFrame的方法，吗？（qwq感觉不是）
                add(gridComponents[row][col]);
            }
        }

    }
    public void restartChessBoard(){
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(initialSide(i,j,people)){
                    gridComponents[i][j].setColor(Color.LIGHT_GRAY);
                }
                else {
                    gridComponents[i][j].setColor(((i + j) % 2 == 0 ? BOARD_COLOR_1 : BOARD_COLOR_2));
                }
            }
        }
    }

    public SquareComponent getGridAt(ChessBoardLocation location) {//这个方法猜想应该和放棋子有关，传入一个model中的棋子位置对象，然后返回背景数组中储存的对应背景
        //注意返回的是SquareComponent对象，所含的信息有背景板的大小和颜色，且注意背景板对象是一个Jplane！
        return gridComponents[location.getRow()][location.getColumn()];
    }

    public void setChessAtGrid(ChessBoardLocation location, Color color) {//设置棋子到对应的背景板上
        removeChessAtGrid(location);//这个是下面的方法，注释掉之后代码正常运行
        getGridAt(location).add(new ChessComponent(color));//即在对应的Jplane对象上添加对应棋子的对象，由此猜测那个paint方法应该是自动调用的
    }

    public void removeChessAtGrid(ChessBoardLocation location) {//删除给定位置背景板上所有的对象，即通过getGridAt方法获取到对应位置背景板对象，然后进行清除
        //即清除对应小方格上的棋子，我感觉是初始化的时候会用到qwq（的确用了qwq）
        // Note: re-validation is required after remove / removeAll删除/移除后需要重新验证（这句注释我也没看懂。。。（我也）
        getGridAt(location).removeAll();
        getGridAt(location).revalidate();
    }

    private ChessBoardLocation getLocationByPosition(int x, int y) {//通过下面的方法知道这个xy应该是鼠标准确的xy坐标，通过整除得到x，y行列数
        return new ChessBoardLocation(y / gridSize, x / gridSize);//然后通过得到的行列数新建了一个棋子的行列位置，并将其返回
        //看完视频暂时觉得是讲鼠标的位置记录成行列数，并将其转化为行列位置对象，返回，应该是确定点击的到底是那一个坐标位置
    }


    @Override
    protected void processMouseEvent(MouseEvent e) {//下面就是事件方法了qwq这个应该是单击鼠标事件
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());//Jcompnents是组件类最开始的那个类，猜测这句话是获取对应xy坐标下的哪个组件？
            ChessBoardLocation location = getLocationByPosition(e.getX(), e.getY());//像猜想的那样，讲鼠标坐标对应的xy值转化为行列数
            for (InputListener listener : listenerList) {
                if (clickedComponent.getComponentCount() == 0) {//似乎是判断这个地方的组件数？

                    listener.onPlayerClickSquare(location, (SquareComponent) clickedComponent);//很好，这块暂时看不懂（我也qwq）
                } else {
                    listener.onPlayerClickChessPiece(location, (ChessComponent) clickedComponent.getComponent(0));
                }//很好，果然注释大法好，上面if语句是点到没有棋子的方格是似乎会对那个地板SquareComponent进行操作
                //下面那个则是点到了有组件的方格位置，似乎会对那个棋子进行操作
            }
        }
    }


    @Override
    public void onChessPiecePlace(ChessBoardLocation location, ChessPiece piece) {//传入棋子的行列数，即位置，和棋子的颜色
        if(location!=null&&piece!=null){
        setChessAtGrid(location, piece.getColor());//这个是初始化棋盘的时候调用的方法
        repaint();
        }
    }

    @Override
    public void onChessPieceRemove(ChessBoardLocation location) {
        removeChessAtGrid(location);//是消除对应位置的棋子
        repaint();
    }//说一下自己的猜想，我感觉是一旦改变初始化棋盘往里添加棋子，或者是删除棋子，棋盘就会重新打印，类似于刷新

    @Override
    public void onChessBoardReload(ChessBoard board) {//这个是重新加载一个棋盘所用的监听方法
        for (int row = 0; row < board.getDimension(); row++) {
            for (int col = 0; col < board.getDimension(); col++) {//相当于这个是board那边初始化好了位置和位置对应的颜色，然后通过调用这个方法把他画在棋盘上
                ChessBoardLocation location = new ChessBoardLocation(row, col);
                ChessPiece piece = board.getChessPieceAt(location);//通过初始化的board返回对应位置的棋子颜色
                if (piece != null) {
                    setChessAtGrid(location, piece.getColor());//应该是现在chessboard哪里设置了颜色和位置，然后通过setChessAtGrid去把对应的棋子画在背景板上
                }else{
                    removeChessAtGrid(location);
                }
            }
        }
        repaint();//重新显示（重画）
    }

    public void setPeople(int people) {
        this.people = people;
    }

    @Override
    public void registerListener(InputListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unregisterListener(InputListener listener) {
        listenerList.remove(listener);
    }


    public SquareComponent getGridComponents(int row, int column) {
        return gridComponents[row][column];
    }

    public boolean initialSide(int row,int column,int num){
        if(num==2){
            return ((row == 0 && column == 0) || (row == 0 && column == 1) || (row == 0 && column == 2) || (row == 0 && column == 3) || (row == 0 && column == 4)
                    || (row == 1 && column == 0) || (row == 1 && column == 1) || (row == 1 && column == 2) || (row == 1 && column == 3) || (row == 1 && column == 4)
                    || (row == 2 && column == 0) || (row == 2 && column == 1) || (row == 2 && column == 2) || (row == 2 && column == 3)
                    || (row == 3 && column == 0) || (row == 3 && column == 1) || (row == 3 && column == 2)
                    || (row == 4 && column == 0) || (row == 4 && column == 1)||
                    (row == dimension-1 && column == dimension-1) || (row == dimension-1 && column == dimension-2) || (row == dimension-1 && column == dimension-3) || (row == dimension-1 && column == dimension-4) || (row == dimension-1 && column == dimension-5)
                    || (row == dimension-2 && column == dimension-1) || (row ==dimension-2  && column == dimension-2) || (row == dimension-2 && column == dimension-3) || (row ==dimension-2  && column == dimension-4) || (row == dimension-2 && column == dimension-5)
                    || (row == dimension-3 && column == dimension-1) || (row == dimension-3 && column == dimension-2) || (row == dimension-3 && column == dimension-3) || (row == dimension-3 && column == dimension-4)
                    || (row == dimension-4 && column == dimension-1) || (row == dimension-4 && column == dimension-2) || (row == dimension-4 && column == dimension-3)
                    || (row == dimension-5 && column == dimension-1) || (row == dimension-5 && column == dimension-2));
        }else{
            return ((row == 0 && column == 0) || (row == 0 && column == 1) || (row == 0 && column == 2) || (row == 0 && column == 3)
                    || (row == 1 && column == 0) || (row == 1 && column == 1) || (row == 1 && column == 2) || (row == 1 && column == 3)
                    || (row == 2 && column == 0) || (row == 2 && column == 1) || (row == 2 && column == 2)
                    || (row == 3 && column == 0) || (row == 3 && column == 1)||
                    (row == 0 && column == dimension-1) || (row == 0 && column == dimension-2) || (row == 0 && column == dimension-3) || (row == 0 && column == dimension-4)
                    || (row == 1 && column == dimension-1) || (row == 1 && column == dimension-2) || (row == 1 && column ==dimension-3 ) || (row == 1 && column == dimension-4)
                    || (row == 2 && column == dimension-1) || (row == 2 && column == dimension-2) || (row == 2 && column == dimension-3)
                    || (row == 3 && column == dimension-1) || (row == 3 && column == dimension-2)||
                    (row == dimension-1 && column == dimension-1) || (row == dimension-1 && column == dimension-2) || (row == dimension-1 && column == dimension-3) || (row == dimension-1 && column == dimension-4)
                    || (row == dimension-2 && column == dimension-1) || (row ==dimension-2  && column == dimension-2) || (row == dimension-2 && column == dimension-3) || (row ==dimension-2  && column == dimension-4)
                    || (row == dimension-3 && column == dimension-1) || (row == dimension-3 && column == dimension-2) || (row == dimension-3 && column == dimension-3)
                    || (row == dimension-4 && column == dimension-1) || (row == dimension-4 && column == dimension-2)||
                    (row == dimension-1 && column == 0) || (row == dimension-1 && column == 1) || (row == dimension-1 && column == 2) || (row == dimension-1 && column == 3)
                    || (row == dimension-2 && column == 0) || (row == dimension-2 && column == 1) || (row == dimension-2 && column == 2) || (row == dimension-2 && column == 3)
                    || (row == dimension-3 && column == 0) || (row ==dimension-3  && column == 1) || (row == dimension-3 && column == 2)
                    || (row == dimension-4 && column == 0) || (row == dimension-4 && column == 1));
        }
    }
}
