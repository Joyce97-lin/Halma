package xyz.chengzi.halma.view;

import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.model.ChessBoard;

import javax.swing.*;
import java.awt.*;

public class ChessComponent extends JComponent {//画棋子的类
    private ChessComponent[][] chessComponents;
    private Color color;
    private boolean selected;//布尔值，用来判定该棋子是不是被选中了

    public ChessComponent(Color color) {
        this.color = color;
    }//构造方法用来传入棋子颜色
    public boolean isSelected() {//返回布尔值，来判断该棋子是否被选中
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }//设置棋子是否被选中

    @Override
    protected void paintComponent(Graphics g) {//画出棋子
        super.paintComponent(g);//还是不知道干什么用qwq
        paintChess(g);
    }

    private void paintChess(Graphics g) {//所以这个Graphics似乎是一个JFrame对象？不确定。。
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//Graphics似乎是设置棋子的样子的，先不管
        g.setColor(color);

        int spacing = (int) (getWidth() * 0.05);
        g.fillOval(spacing, spacing, getWidth() - 2 * spacing, getHeight() - 2 * spacing);//fillOval似乎是画圆形和椭圆形形状的图形
        //似乎是X,Y坐标，画图形左上角的坐标，和宽和长吧，我觉得是长轴和短轴，不行你再看看，感觉不重要，我点进去看了看

        if (selected) { // Draw a + sign in the center of the piece 用来画棋子被选中时的十字线
            g.setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));//似乎是用反转色更清晰？
            g.drawLine(getWidth() / 2, getHeight() / 4, getWidth() / 2, getHeight() * 3 / 4);
            g.drawLine(getWidth() / 4, getHeight() / 2, getWidth() * 3 / 4, getHeight() / 2);
        }
    }

}
