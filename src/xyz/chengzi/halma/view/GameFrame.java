package xyz.chengzi.halma.view;


import xyz.chengzi.halma.controller.GameController;
import xyz.chengzi.halma.model.ChessBoard;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;


public class GameFrame extends JFrame {
    JPanel pictureBackground;
    Image boys;

    public GameFrame() {
        JFrame jFrame = new JFrame();

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pictureBackground=new JPanel();
        setContentPane(pictureBackground);
        pictureBackground.setOpaque(false);
        picture();

        setTitle("Little Mouse's Happy Halma");
        setSize(1100, 900);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JLabel laststepLabel = new JLabel("此处显示上一步棋的走法o(*￣3￣)o");
        laststepLabel.setFont(new java.awt.Font("宋体", Font.BOLD, 13));
        laststepLabel.setForeground(new Color(255,250,205));
        laststepLabel.setLocation(770, 200);
        laststepLabel.setSize(300, 300);
        add(laststepLabel);
        laststepLabel.hide();
        JLabel retractLabel = new JLabel("此处显示你还可以悔几步棋ヾ(^▽^ヾ)");//增添的显示还能悔棋几步的按钮
        retractLabel.setFont(new java.awt.Font("宋体", Font.BOLD, 13));
        retractLabel.setForeground(new Color(255,250,205));
        retractLabel.setLocation(790, 150);
        retractLabel.setSize(400, 30);
        add(retractLabel);
        retractLabel.hide();
        JLabel BFSLabel = new JLabel("此处显示最短距离o(*////▽////*)q");//增添的显示还能悔棋几步的按钮
        BFSLabel.setFont(new java.awt.Font("宋体", Font.BOLD, 13));
        BFSLabel.setForeground(new Color(255,250,205));
        BFSLabel.setLocation(770, 500);
        BFSLabel.setSize(400, 30);
        add(BFSLabel);
        BFSLabel.hide();


        String str1= JOptionPane.showInputDialog(null,"Please type the mouse number (2 or 4 only)","Mouse Number",JOptionPane.PLAIN_MESSAGE);//增添了一个弹窗用来实现输入玩家数
        boolean J=true;
        int num1=0;
        while (J){

            try {
                num1 = Integer.parseInt(str1);
                if(num1==2||num1==4){
                    J=false;
                }else {
                    str1=JOptionPane.showInputDialog(null,"别输奇奇怪怪的数字了（｀へ´）","Mouse Number",JOptionPane.PLAIN_MESSAGE);
                }
            }catch (Exception f){
                str1= JOptionPane.showInputDialog(null,"别输奇奇怪怪的值了，谢谢你了！！！╰（‵□′）╯","Mouse Number",JOptionPane.PLAIN_MESSAGE);
            }

        }
        laststepLabel.show();
        retractLabel.show();
        BFSLabel.show();
        JLabel statusLabel = new JLabel("Current Mouse ");
        statusLabel.setLocation(0, 760);
        statusLabel.setSize(200, 10);
        add(statusLabel);


        ChessBoardComponent chessBoardComponent = new ChessBoardComponent(760, 16,num1);
        ChessBoard chessBoard = new ChessBoard(16);
        add(chessBoardComponent);
        GameController controller = new GameController(chessBoardComponent, chessBoard,num1,statusLabel,laststepLabel,retractLabel,BFSLabel);//同样在初始化界面上实

        Object[] options={"Cat","Mouse"};
        int input=JOptionPane.showOptionDialog(null,"Hey little mouse!(￣▽￣)~* Who do you want to play with?","Nice to meet you!ヾ(^▽^ヾ)",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
                null,options,options[0]);
        if(input==0){
            JOptionPane.showMessageDialog(null,"You are the first one to go! (●´З｀●)","Love you٩(๑>◡<๑)۶",JOptionPane.PLAIN_MESSAGE);
        }
        controller.setMachine(input);




        JButton buttonRestart = new JButton("RESTART");
        int finalNum = num1;
        buttonRestart.addActionListener(e -> {
                            if(input==0){
                                controller.restart(finalNum,true,true);
                                if(finalNum==2) {
                                    JOptionPane.showMessageDialog(null, "You can't ask for more cats/(ㄒoㄒ)/~~!", "Good luck!", JOptionPane.PLAIN_MESSAGE);
                                }else{
                                    JOptionPane.showMessageDialog(null,"You can't get rid of these cats/(ㄒoㄒ)/~~!","Good luck!",JOptionPane.PLAIN_MESSAGE);
                                }
                            }else {
                                String str2= JOptionPane.showInputDialog(null,"Please type the mouse number (2 or 4 only)","Mouse Number",JOptionPane.PLAIN_MESSAGE);//增添了一个弹窗用来实现输入玩家数
                                boolean K = true;
                                int num2 = 0;
                                while (K) {
                                    try {
                                        num2 = Integer.parseInt(str2);
                                        if (num2 == 2 || num2 == 4) {
                                            K = false;
                                        } else {
                                            str2 = JOptionPane.showInputDialog(null,"别输奇奇怪怪的数字了（｀へ´）","Mouse Number",JOptionPane.PLAIN_MESSAGE);
                                        }
                                    } catch (Exception f) {
                                        str2 = JOptionPane.showInputDialog(null,"别输奇奇怪怪的值了，谢谢你了！！！╰（‵□′）╯","Mouse Number",JOptionPane.PLAIN_MESSAGE);
                                    }
                                }

                                controller.restart(num2, true, true);
                            }
                        }
        );
        buttonRestart.setLocation(540, 760);
        buttonRestart.setSize(80, 24);
        add(buttonRestart);



        JButton btnSave = new JButton("SAVE");
        btnSave.addActionListener(d -> controller.saveFile());
        btnSave.setLocation(140, 760);
        btnSave.setSize(80, 24);
        add(btnSave);


        JButton btnLoad = new JButton("LOAD");
        btnLoad.addActionListener(d -> {
            if(input==0){
                JOptionPane.showMessageDialog(null, "Cats aren't patient enough to play the game before T^T", "Good luck!", JOptionPane.PLAIN_MESSAGE);
            }
            else{
            try {
                controller.load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }});
        btnLoad.setLocation(340, 760);
        btnLoad.setSize(80, 24);
        add(btnLoad);

        JButton btnRetract = new JButton("RETRACT");
        btnRetract.addActionListener(d ->
                controller.Retract());
        btnRetract.setLocation(825, 190);
        btnRetract.setSize(90, 24);
        add(btnRetract);

        JButton btnRebroadcast = new JButton("REBROADCAST");
        btnRebroadcast.addActionListener(d ->
                controller.rebroadcast()
        );
        btnRebroadcast.setLocation(820, 370);
        btnRebroadcast.setSize(120, 24);
        add(btnRebroadcast);
        JButton btnBFS= new JButton("CHEESE" );
        btnBFS.addActionListener(d ->
                controller.changeBFS()
        );
        btnBFS.setLocation(820, 540);
        btnBFS.setSize(100, 24);
        add(btnBFS);

}
    public void picture(){
        ImageIcon man=new ImageIcon("snow.jpg");
        boys=man.getImage();
        JLabel manLabel=new JLabel(man);
        this.getLayeredPane().add(manLabel,new Integer(Integer.MIN_VALUE));
        manLabel.setBounds(0,0,man.getIconWidth(),man.getIconHeight());

                    }
         }

