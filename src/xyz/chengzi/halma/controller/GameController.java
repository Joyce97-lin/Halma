package xyz.chengzi.halma.controller;
import xyz.chengzi.halma.listener.InputListener;
import xyz.chengzi.halma.model.*;
import xyz.chengzi.halma.view.ChessBoardComponent;
import xyz.chengzi.halma.view.ChessComponent;
import xyz.chengzi.halma.view.Music;
import xyz.chengzi.halma.view.SquareComponent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class GameController implements InputListener {//这个相当于游戏本体
    private ChessBoardComponent view;
    private ChessBoard model;//这个是模型
    private int playernumber;
    private ArrayList<Color> colorList = new ArrayList<>();
    private MoveData movedatalist;

    private ChessBoardLocation selectedLocation;//一个小方格的位置
    private Color currentPlayer;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private String strcolor;
    private Color initialPlayer;
    private int machine;
    private ArrayList<ChessBoardLocation> machine2 = new ArrayList<>();
    private ArrayList<ChessBoardLocation> machine41 = new ArrayList<>();
    private ArrayList<ChessBoardLocation> machine42 = new ArrayList<>();
    private ArrayList<ChessBoardLocation> machine43 = new ArrayList<>();
    private ArrayList<ChessBoardLocation> machine44 = new ArrayList<>();
    private Color judge;
    private boolean BFS;
    private boolean MUSIC;


    public GameController(ChessBoardComponent chessBoardComponent, ChessBoard chessBoard, int num1, JLabel jlaber1, JLabel jlaber2, JLabel jlaber3,JLabel jlaber4) {//添加了一个新的参数用来判定玩家数（2，4）
        this.view = chessBoardComponent;
        this.movedatalist = new MoveData();
        this.model = chessBoard;
        this.playernumber = num1;
        this.jLabel1 = jlaber1;
        this.jLabel2 = jlaber2;
        this.jLabel3 = jlaber3;
        this.jLabel4=jlaber4;
        this.BFS=false;
        this.MUSIC=false;
        if (num1 == 4) {
            colorList.add(Color.RED);
            colorList.add(Color.BLUE);
            colorList.add(Color.GREEN);
            colorList.add(Color.BLACK);

            int randomnum = new Random().nextInt(4);
            this.currentPlayer = colorList.get(randomnum);//设置谁先走，用来表示当前玩家的颜色，通过随机数来实现随机取值
            judge = colorList.get(randomnum);
        } else {
            colorList.add(Color.RED);
            colorList.add(Color.GREEN);
            int randomnum = new Random().nextInt(2);
            this.currentPlayer = colorList.get(randomnum);
            judge = colorList.get(randomnum);
        }
        showCurrentPlayer();
        this.initialPlayer = currentPlayer;


        view.registerListener(this);//视图添加了一个监听事件源，是他自己
        model.registerListener(view);//实体添加了一个监听器是视图
        model.placeInitialPieces(num1);
    }
    public void changeBFS(){
        if(BFS){
            jLabel4.setText("最快抢奶酪模式关闭(๑•̀ㅂ•́)و✧");
            jLabel4.repaint();
            BFS=false;
        }else {
            jLabel4.setText("最快抢奶酪模式开启(๑•̀ㅂ•́)و✧");
            jLabel4.repaint();
            BFS=true;
        }
    }

    public void rebroadcast() {
        if (hasSelectedLocation()) {
            ChessComponent chess = (ChessComponent) view.getGridComponents(selectedLocation.getRow(), selectedLocation.getColumn()).getComponent(0);//照抄chesscomponet的方法，因为知道选定的位置一定是有棋子的，所以返回给位置的棋子对象
            chess.setSelected(!chess.isSelected());//对该位置的棋子做取消选择操作
            chess.repaint();//这三部废了好多劲就是为了取消十字。。。
            resetSelectedLocation();
            backColor();//和前面一样。先清空选择位置和画板颜色，避免出现bug

        }

        restart(playernumber, false,false);
        JOptionPane.showMessageDialog(null, "现在要开始复现棋局了，你就慢慢点吧");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentPlayer = initialPlayer;
        for (int i = 0; i < movedatalist.getCount(); i++) {
            model.allValidLocation(movedatalist.getMoveDataSrc().get(i));

            model.moveChessPiece(movedatalist.getMoveDataSrc().get(i), movedatalist.getMoveDataDest().get(i));
            showLastMove(movedatalist.getMoveDataSrc().get(i), movedatalist.getMoveDataDest().get(i));
            backColor();
            resetSelectedLocation();//然后清除其实的位置
            if (playernumber == 2) {
                if (someoneWin2()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                    System.exit(0);
                }
            } else if (playernumber == 4) {
                if (someoneWin4()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                    System.exit(0);
                }
            }
            nextPlayer(playernumber);
            showCurrentPlayer();

            JOptionPane.showMessageDialog(null, returnStrLastMove(movedatalist.getMoveDataSrc().get(i), movedatalist.getMoveDataDest().get(i)));


        }
    }

    public ChessBoardLocation returnLocationFromInt(int row, int column) {
        ChessBoardLocation thislocation = new ChessBoardLocation(row, column);
        return thislocation;

    }

    public void restart(int num, boolean J,boolean K) {//这个就是重启调用的方法
        if (hasSelectedLocation()) {
            resetSelectedLocation();
            backColor();//用于清空选择位置和画板

        }
        if (num == 4) {
            this.colorList = new ArrayList<>();
            colorList.add(Color.RED);
            colorList.add(Color.BLUE);
            colorList.add(Color.GREEN);
            colorList.add(Color.BLACK);
            if(machine!=0) {
                int randomnum = new Random().nextInt(4);
                this.currentPlayer = colorList.get(randomnum);//设置谁先走，用来表示当前玩家的颜色，通过随机数来实现随机取值
            }
        } else {
            ArrayList<Color> newColorList = new ArrayList<>();
            newColorList.add(Color.RED);
            newColorList.add(Color.GREEN);
                int randomnum = new Random().nextInt(2);
                this.currentPlayer = newColorList.get(randomnum);//设置谁先走，用来表示当前玩家的颜色，通过随机数来实现随机取值
        }
        view.setPeople(num);
        view.restartChessBoard();


        model.placeInitialPieces(num);
        if (J) {
            this.initialPlayer = this.currentPlayer;
        }
        if(K){
            movedatalist.moveDataInitial();
        }
        showCurrentPlayer();
        showRetractStep();//然后返回还能悔棋几步
        this.playernumber = num;
    }

    public void showRetractStep() {
        this.jLabel3.setText("还可以悔棋" + movedatalist.getCount() + "步ヾ(^▽^ヾ)");//调用MoveData中的count值，来看还能悔棋几步
        jLabel3.setFont(new java.awt.Font("宋体", Font.BOLD, 14));
        jLabel3.setForeground(new Color(255,250,205));
        this.jLabel3.repaint();
    }

    public void Retract() {//这个就是悔棋调用的方法
        if (hasSelectedLocation()) {
            ChessComponent chess = (ChessComponent) view.getGridComponents(selectedLocation.getRow(), selectedLocation.getColumn()).getComponent(0);//照抄chesscomponet的方法，因为知道选定的位置一定是有棋子的，所以返回给位置的棋子对象
            chess.setSelected(!chess.isSelected());//对该位置的棋子做取消选择操作
            chess.repaint();//这三部废了好多劲就是为了取消十字。。。
            resetSelectedLocation();
            backColor();//和前面一样。先清空选择位置和画板颜色，避免出现bug

        }//一下if，else是用来更新currentplyaer的颜色（逆时针）
        if(movedatalist.getCount()>0) {
            if (playernumber == 2) {
                currentPlayer = currentPlayer == Color.RED ? Color.GREEN : Color.RED;
            } else {
                int num2 = colorList.indexOf(currentPlayer);
                int num3;
                if (num2 == 0) {
                    num3 = 3;
                } else {
                    num3 = num2 - 1;
                }
                currentPlayer = colorList.get(num3);
            }
            showCurrentPlayer();//然后更新当前的player
            ChessBoardLocation dest = movedatalist.popDestLocation();//这两行不加也行，最开始改十字的时候写错了qwq
            ChessBoardLocation src = movedatalist.popSrcLocation();
            model.allValidLocation(dest);

            model.moveChessPiece(dest, src);//然后将起始位置和终止位置调换，并进行出栈操作，同时将返回的位置输入move函数
            //需要注意的是这个函数虽然没有写监听器重新打印，但是实现moveChessPiece方法的函数写了监听器，因此实现了棋盘的重新打印
            showRetractStep();//然后返回还能悔棋几步
            retractShowLastMove(movedatalist.getMoveDataSrc().get(movedatalist.getMoveDataSrc().size() - 1), movedatalist.getMoveDataDest().get(movedatalist.getMoveDataDest().size() - 1));
        }else {
            JOptionPane.showMessageDialog(null,"都告诉你0步了，还点d(･｀ω´･d*)");
        }

    }

    public void showLastMove(ChessBoardLocation src, ChessBoardLocation dest) {
        String str1 = String.valueOf(src.getRow() + 1);
        String str2 = String.valueOf(src.getColumn() + 1);
        String str3 = String.valueOf(dest.getRow() + 1);
        String str4 = String.valueOf(dest.getColumn() + 1);

        this.jLabel2.setText( getStrColor() + " rat"+" FROM " + "[ " + str1 + ", " + str2 + " ]" + " TO " + "[ " + str3 + " ," + str4 + " ]");
        jLabel2.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
        jLabel2.setForeground(new Color(255,250,205));
        this.jLabel2.repaint();
    }

    public void retractShowLastMove(ChessBoardLocation src, ChessBoardLocation dest) {
        String str1 = String.valueOf(src.getRow() + 1);
        String str2 = String.valueOf(src.getColumn() + 1);
        String str3 = String.valueOf(dest.getRow() + 1);
        String str4 = String.valueOf(dest.getColumn() + 1);

        this.jLabel2.setText( getLastStrColor()+" rat" + " FROM " + "[ " + str1 + ", " + str2 + " ]" + " TO " + "[ " + str3 + " ," + str4 + " ]");
        jLabel2.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
        jLabel2.setForeground(new Color(255,250,205));
        this.jLabel2.repaint();

    }

    public String returnStrLastMove(ChessBoardLocation src, ChessBoardLocation dest) {
        String str1 = String.valueOf(src.getRow() + 1);
        String str2 = String.valueOf(src.getColumn() + 1);
        String str3 = String.valueOf(dest.getRow() + 1);
        String str4 = String.valueOf(dest.getColumn() + 1);

        return  getLastStrColor() + " rat"+" FROM " + "[ " + str1 + ", " + str2 + " ]" + " TO " + "[ " + str3 + " ," + str4 + " ]";

    }

    public String getLastStrColor() {
        Color lastPlayer;
        if (playernumber == 2) {
            lastPlayer = currentPlayer == Color.RED ? Color.GREEN : Color.RED;
        } else {
            int num2 = colorList.indexOf(currentPlayer);
            int num3;
            if (num2 == 0) {
                num3 = 3;
            } else {
                num3 = num2 - 1;
            }
            lastPlayer = colorList.get(num3);
        }
        if (lastPlayer == Color.RED) {
            return "red";
        } else if (lastPlayer == Color.BLUE) {
            return "blue";
        } else if (lastPlayer == Color.BLACK) {
            return "black";
        } else {
            return "green";
        }

    }

    public String getLinePosition(int num) {//储存文件时的操作，通过下面的方法，将grid中每一行的颜色位置转化为数字字符串（传入的参数int num即代表了是正在将哪一行转化为字符串储存
        String colorstrlist = "";
        Square[][] thisgrid = model.getGrid();
        for (int i = 0; i < thisgrid[num].length; i++) {
            if (thisgrid[num][i].getPiece() != null) {
                colorstrlist += getStrcolorWord(thisgrid[num][i].getPiece().getColor());
            } else {
                colorstrlist += "0";
            }
        }
        return colorstrlist;
    }

    public String getStrcolorWord(Color thiscolor) {//储存文件时的操作，根据对应位置传入的颜色值将其转化为1 2 3 4 从而存储进txt文件中
        if (thiscolor == Color.RED) {
            return "1";
        } else if (thiscolor == Color.BLACK) {
            return "2";
        } else if (thiscolor == Color.BLUE) {
            return "3";
        } else {
            return "4";
        }
    }

    public Color getColorFromStrColor(char word) {//读取文件时的操作，根据文件里每行语句的1 2 3 4来设置对应棋子的颜色为红，黑。绿。蓝四种颜色
        if (word == '1') {
            return Color.RED;
        } else if (word == '2') {
            return Color.BLACK;
        } else if (word == '3') {
            return Color.BLUE;
        } else {
            return Color.GREEN;
        }
    }


    public void setjLabel(JLabel jLabel) {
        this.jLabel1 = jLabel;

    }

    public void showCurrentPlayer() {//这个就是更新那个jFabel的方法，应该加入显示步数的时候应该还要改
        String str_color = "";
        if (currentPlayer == Color.RED) {
            str_color = "Red";
        } else if (currentPlayer == Color.BLACK) {
            str_color = "Black";
        } else if (currentPlayer == Color.BLUE) {
            str_color = "Blue";
        } else {
            str_color = "Green";
        }
        this.jLabel1.setText(str_color);
        this.jLabel1.repaint();
    }

    public ChessBoardLocation getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(ChessBoardLocation location) {
        this.selectedLocation = location;
    }

    public void resetSelectedLocation() {
        setSelectedLocation(null);
    }

    public boolean hasSelectedLocation() {
        return selectedLocation != null;
    }

    public Color nextPlayer(int num1) {//用来循环user的方法，按照顺时针方向移动
        if (num1 == 2) {
            return currentPlayer = currentPlayer == Color.RED ? Color.GREEN : Color.RED;
        }
        int num2 = colorList.indexOf(currentPlayer);
        int num3;
        if (num2 == 3) {
            num3 = 0;
        } else {
            num3 = num2 + 1;
        }
        return currentPlayer = colorList.get(num3);

    }//就是当前角色颜色是红的时候将其变成绿
    //正确的话就执行表达式二

    @Override
    public void onPlayerClickSquare(ChessBoardLocation location, SquareComponent component) {//这条语句接chessboardlocation的语句，鼠标
        //点击到一个位置，然后通过判断这个是方格不是棋子，然后讲方格位置和方格对象传入该函数
            if (hasSelectedLocation()) {
                if (model.isValidMove(getSelectedLocation(), location)) {//这行语句应该是先判断是不是先选中了棋子，然后再判断棋子的位置和新选中的
                    //空格的位置是否可以移动
                    if(BFS) {
                        if(Math.abs(getSelectedLocation().getRow()-location.getRow())>1||Math.abs(getSelectedLocation().getColumn()-location.getColumn())>1) {


                            model.initAllLocationCount();
                            model.setSrcLocationBFS(location);
                            model.addSRCIntoAllList(getSelectedLocation());
                            model.startBFS(location, getSelectedLocation());
                            model.getBFSWay(getSelectedLocation(), location);
                            model.deleteSRCFromAllList();
                            ArrayList<ChessBoardLocation> way = model.getWayToDest();
                            JOptionPane.showMessageDialog(null, "距离最快抢到奶酪还有" + way.size() + "步（￣︶￣）↗");
                            ChessBoardLocation currentLocation = getSelectedLocation();
                            for (int i = 0; i < way.size(); i++) {
                                model.moveChessPiece(currentLocation, way.get(i));
                                showLastMove(currentLocation, way.get(i));
                                backColor();
                                currentLocation = way.get(i);
                                JOptionPane.showMessageDialog(null, "第" + (i+1) + "步");
                            }
                        }else {
                            model.moveChessPiece(selectedLocation, location);

                            showLastMove(selectedLocation, location);

                        }
                    }else {
                        //然后执行移动操作  虽然move函数没有监听，但是其拆分的remove和set函数都设置了监听方法，用来重新打印
                        model.moveChessPiece(selectedLocation, location);

                        showLastMove(selectedLocation, location);
                    }

                    showLastMove(selectedLocation, location);
                    movedatalist.pushSrcLocation(selectedLocation);
                    movedatalist.pushDestLocation(location);
                    backColor();
                    resetSelectedLocation();//然后清除其实的位置
                    nextPlayer(playernumber);
                    showCurrentPlayer();
                    showRetractStep();//然后返回还能悔棋几步
                    if (playernumber == 2) {
                        if (someoneWin2()) {
                            JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                            System.exit(0);
                        }
                    } else if (playernumber == 4) {
                        if (someoneWin4()) {
                            JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                            System.exit(0);
                        }
                    }
                }
            }
        if (machine == 0) {
        while(!judge.equals(currentPlayer)) {
            if(playernumber==4) {
                ArrayList<ChessBoardLocation> replace = movingMachine();
                int row1 = replace.get(0).getRow() + 1;
                int column1 = replace.get(0).getColumn() + 1;
                int row2 = replace.get(1).getRow() + 1;
                int column2 = replace.get(1).getColumn() + 1;
                JOptionPane.showMessageDialog(null, "Last move of the Cat is from [" + row1 + "," + column1 + "] to [" + row2 + "," + column2 + "]");
            }else if(playernumber==2){
                movingMachine();
            }
            }
        }
        }

    public void backColor(){
        Color BOARD_COLOR_1 = new Color(255,250,205);
        Color BOARD_COLOR_2 = new Color(255,200,100);
        for (int i = 0; i < model.getAllValidLocation().size(); i++) {//跳完之后恢复颜色
            if(view.initialSide(model.getAllValidLocation().get(i).getRow(),model.getAllValidLocation().get(i).getColumn(),playernumber)){
                view.getGridComponents(model.getAllValidLocation().get(i).getRow(), model.getAllValidLocation().get(i).getColumn()).setColor(Color.LIGHT_GRAY);
            }else if((model.getAllValidLocation().get(i).getColumn()+model.getAllValidLocation().get(i).getRow())%2==0) {
                view.getGridComponents(model.getAllValidLocation().get(i).getRow(), model.getAllValidLocation().get(i).getColumn()).setColor(BOARD_COLOR_1);
            }else{
                view.getGridComponents(model.getAllValidLocation().get(i).getRow(), model.getAllValidLocation().get(i).getColumn()).setColor(BOARD_COLOR_2);
            }
        }
    }

    @Override
    public void onPlayerClickChessPiece(ChessBoardLocation location, ChessComponent component) {
        ChessPiece piece = model.getChessPieceAt(location);
        if (piece.getColor() == currentPlayer && (!hasSelectedLocation() || location.equals(getSelectedLocation()))) {//第一个判断用来判断是否点到了当前的那个player
            //第二个判断条件没看懂，但是已经选上了的话会返回false，应该是前一个判断如果已经选上了，false，然后点的不是选的那个，会直接跳过，是选的那个会清除须按则
            if (!hasSelectedLocation()) {
                setSelectedLocation(location);
                changeColor(location);//选完之后，调用方法换颜色
            } else {
                resetSelectedLocation();//判断是不是已经选的，是已经选的那个清除选择
                backColor();
            }
            component.setSelected(!component.isSelected());
            component.repaint();
        }
    }

    public String getStrColor() {
        if (currentPlayer == Color.RED) {
            return "red";
        } else if (currentPlayer == Color.BLUE) {
            return "blue";
        } else if (currentPlayer == Color.BLACK) {
            return "black";
        } else {
            return "green";
        }
    }

    public String getStrIniColor() {
        if (initialPlayer == Color.RED) {
            return "red";
        } else if (initialPlayer == Color.BLUE) {
            return "blue";
        } else if (initialPlayer == Color.BLACK) {
            return "black";
        } else {
            return "green";
        }
    }

    public boolean checkUser(String str) {//读取文件时检查第一行的字符，讲user设置成应该进行的user
        if (str.equals("red")) {
            currentPlayer = Color.RED;
            showCurrentPlayer();
            return true;
        } else if (str.equals("black")) {
            currentPlayer = Color.BLACK;
            showCurrentPlayer();
            return true;
        } else if (str.equals("green")) {
            currentPlayer = Color.GREEN;
            showCurrentPlayer();
            return true;
        } else if (str.equals("blue")) {
            currentPlayer = Color.BLUE;
            showCurrentPlayer();
            return true;
        } else {
            return false;
        }
    }


    public void saveFile() {
        //弹出文件选择框
        JFileChooser chooser = new JFileChooser();

        //后缀名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "通讯录文件(*.txt)", "txt");
        chooser.setFileFilter(filter);

        //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
        int option = chooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {    //假如用户选择了保存
            File file = chooser.getSelectedFile();

            String fname = chooser.getName(file);    //从文件名输入框中获取文件名

            //假如用户填写的文件名不带我们制定的后缀名，那么我们给它添上后缀
            if (fname.indexOf(".txt") == -1) {
                file = new File(chooser.getCurrentDirectory(), fname + ".txt");
                fname = file.getAbsolutePath() + ".txt";
                System.out.println("renamed");
                System.out.println(file.getName());
            }
            fname = file.getAbsolutePath();

            try {
                FileOutputStream fos = new FileOutputStream(file);
                FileWriter out = new FileWriter(fname);//以上都是在网上找的，大概理解了qwq
                out.write("password_somethingyoucannotguess"+"\n");
                out.write(String.valueOf(playernumber) + "\n");
                out.write(getStrColor() + "\n");//首先将当前谁走写入
                out.write(getStrIniColor() + "\n");
                for (int i = 0; i < model.getGrid().length; i++) {
                    out.write(getLinePosition(i) + "\n");//然后按照行写入当前棋盘棋子的信息
                }
                out.write("$$"+"\n");
                for (int i = 0; i < movedatalist.getCount(); i++) {
                    out.write(String.valueOf(movedatalist.getMoveDataSrc().get(i).getRow()) + "\n");
                    out.write(String.valueOf(movedatalist.getMoveDataSrc().get(i).getColumn()) + "\n");
                    out.write(String.valueOf(movedatalist.getMoveDataDest().get(i).getRow()) + "\n");
                    out.write(String.valueOf(movedatalist.getMoveDataDest().get(i).getColumn()) + "\n");
                }

                out.close();
                //写文件操作……
                fos.close();

            } catch (IOException e) {
                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
    }

    public  void load() throws Exception {
        // TODO Auto-generated method stub
        if(hasSelectedLocation()){
            resetSelectedLocation();
            backColor();//用于清空选择位置和画板
        }
        this.movedatalist.moveDataInitial();
        model.loadInititalPieces();
        JFileChooser jfc=new JFileChooser();
        if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            File file=jfc.getSelectedFile();
            Scanner input=new Scanner(file);
            int count=0;
            int count1=0;
            int count2=0;
            int count3=0;
            int count4=0;
            boolean J=true;
            while(input.hasNext()){
                if(count==0){
                    String numbers=input.nextLine();//读取文件，好像可以用缓存器效果会更好，等到时候再改改，但是读取速度都差不多
                    if(numbers.equals("password_somethingyoucannotguess")){
                        count++;
                    }else {

                        JOptionPane.showMessageDialog(null,"加载的文件不对，请不要尝试奇奇怪怪的文件");
                        restart(4,true,true);
                        J=false;
                        break;
                    }
                }

                if(count==1){
                    String numbers=input.nextLine();//读取文件，好像可以用缓存器效果会更好，等到时候再改改，但是读取速度都差不多
                    if(Integer.parseInt(numbers)!=2&&Integer.parseInt(numbers)!=4){
                        JOptionPane.showMessageDialog(null,"玩家数量错误");
                        restart(4,true,true);
                        J=false;
                        break;
                    }
                    restart(Integer.parseInt(numbers),true,true);
                    this.movedatalist.moveDataInitial();
                    model.loadInititalPieces();
                    count++;
                }
                else if(count==2){
                    String colorStr=input.nextLine();
                    if(colorStr.equals("red")){
                        initialPlayer=Color.RED;


                    }else if(colorStr.equals("black")){
                        initialPlayer=Color.BLACK;


                    }else if(colorStr.equals("green")){
                        initialPlayer=Color.GREEN;

                    }else if (colorStr.equals("blue")) {
                        initialPlayer = Color.BLUE;

                    }
                    else{
                        System.out.println("文件错误");
                        break;

                    }
                    count++;
                }
                else if(count==3){//还是判断字符串是不是“red”这样的，来设置当前的currentplayer 并更新user显示器
                    String numbers=input.nextLine();//读取文件，好像可以用缓存器效果会更好，等到时候再改改，但是读取速度都差不多
                    checkUser(numbers);
                    count++;
                }else if(count<20){
                    String numbers=input.nextLine();//读取文件，好像可以用缓存器效果会更好，等到时候再改改，但是读取速度都差不多
                    if(numbers.length()>16){
                        JOptionPane.showMessageDialog(null,"棋子超出边界");
                        restart(playernumber,true,true);
                        J=false;
                        break;
                    }
                    for(int i=0;i<numbers.length();i++){
                        if(numbers.charAt(i)=='0'){
                            continue;
                        }else {
                            model.setColorPieceInGrid(count-4,i,getColorFromStrColor(numbers.charAt(i)));//i代表着列数，然后一个个写入
                            if(numbers.charAt(i)=='1'){
                                count1++;
                            }else if(numbers.charAt(i)=='2'){
                                count2++;
                            }else if (numbers.charAt(i)=='3'){
                                count3++;

                            }else {
                                count4++;
                            }
                        }
                    }
                    count++;//count代表着现在录入到了第几行

                }else if(count==20){
                    String numbers=input.nextLine();
                    if(numbers.equals("$$")){

                    }else {
                        JOptionPane.showMessageDialog(null,"棋子超出边界");
                        restart(playernumber,true,true);
                        J=false;
                        break;
                    }
                    count++;
                }else {
                    String rowS=input.nextLine();
                    int rowIntS=Integer.parseInt(rowS);
                    String columnS=input.nextLine();
                    int columnIntS=Integer.parseInt(columnS);
                    String rowD=input.nextLine();
                    int rowIntD=Integer.parseInt(rowD);
                    String columnD=input.nextLine();
                    int columnIntD=Integer.parseInt(columnD);
                    movedatalist.pushSrcLocation(returnLocationFromInt(rowIntS,columnIntS));
                    movedatalist.pushDestLocation(returnLocationFromInt(rowIntD,columnIntD));

                }


            }
            if(J) {
                if (playernumber == 2) {
                    if (count1 != 19 || count4 != 19) {
                        JOptionPane.showMessageDialog(null, "棋子数量错误");
                        restart(playernumber, true, true);
                    }
                } else {
                    if (count1 != 13 || count2 != 13 || count3 != 13 || count4 != 13) {
                        JOptionPane.showMessageDialog(null, "棋子数量错误");
                        restart(playernumber, true, true);
                    }
                }
                if (playernumber == 2) {
                    if (someoneWin2()) {
                        JOptionPane.showMessageDialog(null, "开局就有玩家胜利");
                        restart(playernumber, true, true);
                    }
                }
                if (playernumber == 4) {
                    if (someoneWin4()) {
                        JOptionPane.showMessageDialog(null, "开局就有玩家胜利");
                        restart(playernumber, true, true);
                    }
                }
            }
            showRetractStep();//然后返回还能悔棋几步
            model.loadOver();//这一行和chessboard中的初始化一样，添加了监听事件，用来显示重新加载的棋盘
            input.close();
        }
        else
            System.out.println("No file is selected!");
    }

    public void changeColor(ChessBoardLocation src) {//就是用来给可以跳的地方换颜色的
        model.allValidLocation(src);
        for (int i = 0; i < model.getAllValidLocation().size(); i++) {
            view.getGridComponents(model.getAllValidLocation().get(i).getRow(), model.getAllValidLocation().get(i).getColumn()).setColor(currentPlayer);
        }
    }

    public boolean someoneWin2() {
        if (model.getGrid(0, 0).getPiece() != null &&
                model.getGrid(0, 1).getPiece() != null &&
                model.getGrid(0, 2).getPiece() != null &&
                model.getGrid(0, 3).getPiece() != null &&
                model.getGrid(0, 4).getPiece() != null &&
                model.getGrid(1, 0).getPiece() != null &&
                model.getGrid(1, 1).getPiece() != null &&
                model.getGrid(1, 2).getPiece() != null &&
                model.getGrid(1, 3).getPiece() != null &&
                model.getGrid(1, 4).getPiece() != null &&
                model.getGrid(2, 0).getPiece() != null &&
                model.getGrid(2, 1).getPiece() != null &&
                model.getGrid(2, 2).getPiece() != null &&
                model.getGrid(2, 3).getPiece() != null &&
                model.getGrid(3, 0).getPiece() != null &&
                model.getGrid(3, 1).getPiece() != null &&
                model.getGrid(3, 2).getPiece() != null &&
                model.getGrid(4, 0).getPiece() != null &&
                model.getGrid(4, 1).getPiece() != null &&
                model.getGrid(0, 0).getPiece().getColor().equals(Color.GREEN) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 3).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 4).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 3).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 4).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 3).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(3, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(3, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(3, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(4, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(4, 1).getPiece().getColor())) {
            return true;
        }
        if (model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 5).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 5).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 5, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 5, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(Color.RED) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 5).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 5).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 5, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 5, model.getDimension() - 2).getPiece().getColor())) {
            return true;
        }
        return false;
    }

    public boolean someoneWin4() {
        if (model.getGrid(0, 0).getPiece() != null &&
                model.getGrid(0, 1).getPiece() != null &&
                model.getGrid(0, 2).getPiece() != null &&
                model.getGrid(0, 3).getPiece() != null &&
                model.getGrid(1, 0).getPiece() != null &&
                model.getGrid(1, 1).getPiece() != null &&
                model.getGrid(1, 2).getPiece() != null &&
                model.getGrid(1, 3).getPiece() != null &&
                model.getGrid(2, 0).getPiece() != null &&
                model.getGrid(2, 1).getPiece() != null &&
                model.getGrid(2, 2).getPiece() != null &&
                model.getGrid(3, 0).getPiece() != null &&
                model.getGrid(3, 1).getPiece() != null &&
                model.getGrid(0, 0).getPiece().getColor().equals(Color.GREEN) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(0, 3).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(1, 3).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 1).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(2, 2).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(3, 0).getPiece().getColor()) &&
                model.getGrid(0, 0).getPiece().getColor().equals(model.getGrid(3, 1).getPiece().getColor())) {
            return true;
        }
        if (model.getGrid(model.getDimension() - 1, 0).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, 0).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, 0).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, 0).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(Color.BLUE) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, 0).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, 0).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, 0).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, 0).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, 1).getPiece().getColor())) {
            return true;
        }
        if (model.getGrid(0, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(0, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(0, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(0, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(1, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(1, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(1, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(1, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(2, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(2, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(2, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(3, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(3, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(Color.BLACK) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(0, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(0, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(0, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(1, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(1, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(1, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(1, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(2, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(2, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(2, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(3, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(0, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(3, model.getDimension() - 2).getPiece().getColor())) {
            return true;
        }
        if (model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 2, model.getDimension() - 4).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 3, model.getDimension() - 3).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, model.getDimension() - 1).getPiece() != null &&
                model.getGrid(model.getDimension() - 4, model.getDimension() - 2).getPiece() != null &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(Color.RED) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 1, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 2, model.getDimension() - 4).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 2).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 3, model.getDimension() - 3).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, model.getDimension() - 1).getPiece().getColor()) &&
                model.getGrid(model.getDimension() - 1, model.getDimension() - 1).getPiece().getColor().equals(model.getGrid(model.getDimension() - 4, model.getDimension() - 2).getPiece().getColor())) {
            return true;
        }
        return false;
    }

    public void setMachine(int machine) {
        this.machine = machine;
    }

    public ArrayList<ChessBoardLocation> movingMachine() {
        ArrayList<ChessBoardLocation> chessBoardLocations=new ArrayList<>();
        if (playernumber == 2) {
            if (currentPlayer == Color.RED) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine2.contains(model.getGrid(i, j).getLocation())) {
                            machine2.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
            }
            if (currentPlayer == Color.GREEN) {
                for (int i = 15; i > 9; i--) {
                    for (int j = 15; j > 9; j--) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine2.contains(model.getGrid(i, j).getLocation())) {
                            machine2.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
            }
            int indexSrc = new Random().nextInt(machine2.size());
            model.allValidLocation(machine2.get(indexSrc));
            while(model.getAllValidLocation().size() == 0){
                indexSrc=new Random().nextInt(machine2.size());
                model.allValidLocation(machine2.get(indexSrc));
            }
            int indexDest = new Random().nextInt(model.getAllValidLocation().size());
            model.moveChessPiece(machine2.get(indexSrc), model.getAllValidLocation().get(indexDest));
            showLastMove(machine2.get(indexSrc), model.getAllValidLocation().get(indexDest));
            movedatalist.pushSrcLocation(machine2.get(indexSrc));
            movedatalist.pushDestLocation(model.getAllValidLocation().get(indexDest));
            if (someoneWin2()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
            chessBoardLocations.add(machine2.get(indexSrc));
            chessBoardLocations.add(model.getAllValidLocation().get(indexDest));
            machine2.remove(machine2.get(indexSrc));
            machine2.add(model.getAllValidLocation().get(indexDest));
            backColor();
            resetSelectedLocation();//然后清除其实的位置
            nextPlayer(playernumber);
            showCurrentPlayer();
        }else if(playernumber==4){
            if (currentPlayer == Color.RED) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine41.contains(model.getGrid(i, j).getLocation())) {
                            machine41.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
                int indexSrc = new Random().nextInt(machine41.size());
                model.allValidLocation(machine41.get(indexSrc));
                while(model.getAllValidLocation().size() == 0){
                    indexSrc=new Random().nextInt(machine41.size());
                    model.allValidLocation(machine41.get(indexSrc));
                }
                    int indexDest = new Random().nextInt(model.getAllValidLocation().size());
                    model.moveChessPiece(machine41.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    showLastMove(machine41.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    movedatalist.pushSrcLocation(machine41.get(indexSrc));
                    movedatalist.pushDestLocation(model.getAllValidLocation().get(indexDest));
                chessBoardLocations.add(machine41.get(indexSrc));
                chessBoardLocations.add(model.getAllValidLocation().get(indexDest));
                machine41.remove(machine41.get(indexSrc));
                machine41.add(model.getAllValidLocation().get(indexDest));
            }
            if (currentPlayer == Color.BLUE) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 15; j >10; j--) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine42.contains(model.getGrid(i, j).getLocation())) {
                            machine42.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
                int indexSrc = new Random().nextInt(machine42.size());
                model.allValidLocation(machine42.get(indexSrc));
                while(model.getAllValidLocation().size() == 0){
                    indexSrc=new Random().nextInt(machine42.size());
                    model.allValidLocation(machine42.get(indexSrc));
                }
                    int indexDest = new Random().nextInt(model.getAllValidLocation().size());
                    model.moveChessPiece(machine42.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    showLastMove(machine42.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    movedatalist.pushSrcLocation(machine42.get(indexSrc));
                    movedatalist.pushDestLocation(model.getAllValidLocation().get(indexDest));
                chessBoardLocations.add(machine42.get(indexSrc));
                chessBoardLocations.add(model.getAllValidLocation().get(indexDest));
                    machine42.remove(machine42.get(indexSrc));
                    machine42.add(model.getAllValidLocation().get(indexDest));
            }
            if (currentPlayer == Color.BLACK) {
                for (int i = 15; i >10; i--) {
                    for (int j = 0; j <4; j++) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine43.contains(model.getGrid(i, j).getLocation())) {
                            machine43.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
                int indexSrc = new Random().nextInt(machine43.size());
                model.allValidLocation(machine43.get(indexSrc));
                while(model.getAllValidLocation().size() == 0){
                    indexSrc=new Random().nextInt(machine43.size());
                    model.allValidLocation(machine43.get(indexSrc));
                }
                    int indexDest = new Random().nextInt(model.getAllValidLocation().size());
                    model.moveChessPiece(machine43.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    showLastMove(machine43.get(indexSrc), model.getAllValidLocation().get(indexDest));
                    movedatalist.pushSrcLocation(machine43.get(indexSrc));
                    movedatalist.pushDestLocation(model.getAllValidLocation().get(indexDest));
                chessBoardLocations.add(machine43.get(indexSrc));
                chessBoardLocations.add(model.getAllValidLocation().get(indexDest));
                    machine43.remove(machine43.get(indexSrc));
                    machine43.add(model.getAllValidLocation().get(indexDest));
            }
            if (currentPlayer == Color.GREEN) {
                for (int i = 15; i >10; i--) {
                    for (int j = 15; j >10; j--) {
                        if (model.getGrid(i, j).getPiece() != null&&!machine44.contains(model.getGrid(i, j).getLocation())) {
                            machine44.add(model.getGrid(i, j).getLocation());
                        }
                    }
                }
                int indexSrc = new Random().nextInt(machine44.size());
                model.allValidLocation(machine44.get(indexSrc));
                while(model.getAllValidLocation().size() == 0){
                    indexSrc=new Random().nextInt(machine44.size());
                    model.allValidLocation(machine44.get(indexSrc));
                }
                int indexDest = new Random().nextInt(model.getAllValidLocation().size());
                model.moveChessPiece(machine44.get(indexSrc), model.getAllValidLocation().get(indexDest));
                showLastMove(machine44.get(indexSrc), model.getAllValidLocation().get(indexDest));
                movedatalist.pushSrcLocation(machine44.get(indexSrc));
                movedatalist.pushDestLocation(model.getAllValidLocation().get(indexDest));
                chessBoardLocations.add(machine44.get(indexSrc));
                chessBoardLocations.add(model.getAllValidLocation().get(indexDest));
                machine44.remove(machine44.get(indexSrc));
                machine44.add(model.getAllValidLocation().get(indexDest));
            }

            if (someoneWin4()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You little mouse win!","The Game is over!",JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
            backColor();
            resetSelectedLocation();//然后清除其实的位置
            nextPlayer(playernumber);
            showCurrentPlayer();
        }
        return chessBoardLocations;
    }
}
