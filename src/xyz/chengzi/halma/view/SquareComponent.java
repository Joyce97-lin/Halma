package xyz.chengzi.halma.view;

import javax.swing.*;
import java.awt.*;

public class SquareComponent extends JPanel {//相当于棋盘上一个小方格的颜色和大小
    private Color color;//网格的颜色

    public SquareComponent(int size, Color color) {
        setLayout(new GridLayout(1, 1)); // Use 1x1 grid layout//网格布局，一行一列
        setSize(size, size);//设置大小
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//这句暂时没看懂
        paintSquare(g);
    }

    private void paintSquare(Graphics g) {//绘制方格正方形
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth(), getHeight());
    }


    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public Color getColor() {
        return color;
    }
}
