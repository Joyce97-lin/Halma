package xyz.chengzi.halma.model;

import javax.naming.InitialContext;
import java.util.ArrayList;


public class MoveData {
    private ArrayList<ChessBoardLocation> moveDataSrc;
    private ArrayList<ChessBoardLocation> moveDataDest;
    private int count;

    public ArrayList<ChessBoardLocation> getMoveDataSrc() {
        return moveDataSrc;
    }

    public ArrayList<ChessBoardLocation> getMoveDataDest() {
        return moveDataDest;
    }

    public void moveDataInitial(){
        moveDataSrc.clear();
        moveDataDest.clear();
        this.count=0;


    }

    public MoveData() {
        moveDataSrc=new ArrayList<>();//存放起始位置
        moveDataDest=new ArrayList<>();//存放终止位置
        this.count=0;//记录走了几步，来显示还能悔棋几步

    }
    public void pushSrcLocation(ChessBoardLocation rowlocation){//讲起始位置值加入列表，相当于入栈操作
        moveDataSrc.add(rowlocation);
        count++;
    }
    public void pushDestLocation(ChessBoardLocation columnlocation){
        moveDataDest.add(columnlocation);


    }
    public ChessBoardLocation popSrcLocation(){
        if(moveDataSrc.isEmpty()){
           return null;
        }
        count--;
        return moveDataSrc.remove(moveDataSrc.size()-1);
    }
    public ChessBoardLocation popDestLocation(){//相当于返回最后一个并删除，相当于出栈操作，下面同理
        if(moveDataDest.isEmpty()){
            return null;
        }

        return moveDataDest.remove((moveDataDest.size())-1);
    }

    public int getCount() {
        return count;
    }
}
