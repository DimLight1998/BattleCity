package common.logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;

import static common.item.tile.Tile.*;

/**
 * Created on 2017/05/18.
 */
public class MapDesigner extends JPanel implements ActionListener,MouseListener{
    JFrame mainFrame;

    int tileSelected;
    int[][] mapContent = new int[30][30];
    JLabel[][] labels_Map;

    JTextField textField_MapName;

    public static void main(String[] args) {
        new MapDesigner().display();
    }

    MapDesigner() {
        mainFrame = new JFrame("Map Designer");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setSize(615,520);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);

        JPanel panel_Map = new JPanel(new GridLayout(30,30));
        JPanel panel_Option = new JPanel(new GridLayout(12,1));

        labels_Map = new JLabel[30][30];

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                labels_Map[i][j] = new JLabel(new ImageIcon(getClass().getResource("/res/pic/plain_tile.png")));
                labels_Map[i][j].setName(String.format("%d_%d",i,j));
                labels_Map[i][j].addMouseListener(this);
                panel_Map.add(labels_Map[i][j]);
            }
        }

        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButton radioButton_PlainTile = new JRadioButton("Clear",true);
        JRadioButton radioButton_BrickWall = new JRadioButton("Brick wall");
        JRadioButton radioButton_MetalWall = new JRadioButton("Metal wall");
        JRadioButton radioButton_MetalTile = new JRadioButton("Metal tile");
        JRadioButton radioButton_Plant = new JRadioButton("Plant");
        JRadioButton radioButton_Water = new JRadioButton("Water");

        buttonGroup.add(radioButton_PlainTile);
        buttonGroup.add(radioButton_BrickWall);
        buttonGroup.add(radioButton_MetalWall);
        buttonGroup.add(radioButton_MetalTile);
        buttonGroup.add(radioButton_Plant);
        buttonGroup.add(radioButton_Water);

        radioButton_PlainTile.addActionListener(this);
        radioButton_BrickWall.addActionListener(this);
        radioButton_MetalWall.addActionListener(this);
        radioButton_MetalTile.addActionListener(this);
        radioButton_Plant.addActionListener(this);
        radioButton_Water.addActionListener(this);

        textField_MapName = new JTextField("Enter_map_name");
        JButton button_SymmetrizeLR = new JButton("Symmetrize (L to R)");
        JButton button_SymmetrizeRL = new JButton("Symmetrize (R to L)");
        JButton button_ClearAll = new JButton("Clear All");
        JButton button_Save = new JButton("Save map");
        JButton button_Exit = new JButton("Exit");

        button_SymmetrizeLR.addActionListener(this);
        button_SymmetrizeRL.addActionListener(this);
        button_ClearAll.addActionListener(this);
        button_Save.addActionListener(this);
        button_Exit.addActionListener(this);

        panel_Option.add(radioButton_PlainTile);
        panel_Option.add(radioButton_BrickWall);
        panel_Option.add(radioButton_MetalWall);
        panel_Option.add(radioButton_MetalTile);
        panel_Option.add(radioButton_Plant);
        panel_Option.add(radioButton_Water);
        panel_Option.add(button_SymmetrizeLR);
        panel_Option.add(button_SymmetrizeRL);
        panel_Option.add(button_ClearAll);
        panel_Option.add(textField_MapName);
        panel_Option.add(button_Save);
        panel_Option.add(button_Exit);

        mainFrame.add(panel_Map,BorderLayout.CENTER);
        mainFrame.add(panel_Option,BorderLayout.EAST);

        for(int i = 0;i<30;i++) {
            for(int j = 0;j<30;j++) {
                mapContent[i][j] = 0;
            }
        }

        setReserve();
    }

    void setReserve() {
        changeIcon(labels_Map[0][0],RESERVE);
        changeIcon(labels_Map[0][1],RESERVE);
        changeIcon(labels_Map[1][0],RESERVE);
        changeIcon(labels_Map[1][1],RESERVE);

        changeIcon(labels_Map[0][9],RESERVE);
        changeIcon(labels_Map[0][10],RESERVE);
        changeIcon(labels_Map[1][9],RESERVE);
        changeIcon(labels_Map[1][10],RESERVE);

        changeIcon(labels_Map[0][19],RESERVE);
        changeIcon(labels_Map[0][20],RESERVE);
        changeIcon(labels_Map[1][19],RESERVE);
        changeIcon(labels_Map[1][20],RESERVE);

        changeIcon(labels_Map[0][28],RESERVE);
        changeIcon(labels_Map[0][29],RESERVE);
        changeIcon(labels_Map[1][28],RESERVE);
        changeIcon(labels_Map[1][29],RESERVE);

        changeIcon(labels_Map[28][10],RESERVE);
        changeIcon(labels_Map[28][11],RESERVE);
        changeIcon(labels_Map[29][10],RESERVE);
        changeIcon(labels_Map[29][11],RESERVE);

        changeIcon(labels_Map[28][14],HEAD_QUARTER_LU);
        changeIcon(labels_Map[28][15],HEAD_QUARTER_RU);
        changeIcon(labels_Map[29][14],HEAD_QUARTER_LD);
        changeIcon(labels_Map[29][15],HEAD_QUARTER_RD);

        changeIcon(labels_Map[28][18],RESERVE);
        changeIcon(labels_Map[28][19],RESERVE);
        changeIcon(labels_Map[29][18],RESERVE);
        changeIcon(labels_Map[29][19],RESERVE);

        mapContent[28][14]=6;
        mapContent[28][15]=7;
        mapContent[29][14]=8;
        mapContent[29][15]=9;
    }

    void display() {
        mainFrame.setVisible(true);
    }

    boolean tileEditable(int row, int column) {
        if((row == 0) || (row==1)) {
            return !((column == 0 )||( column == 1)||(column==9)||(column==10)||(column==19)||(column==20)||(column==28)||(column==29));

        }

        if((row == 28) || (row == 29)) {
            int columnHalf = column/2;
            return !((columnHalf == 5) || (columnHalf == 7) || (columnHalf == 9));
        }

        return true;
    }

    void changeIcon(JLabel label,int id) {
        String fileName = "";

        switch (id) {
            case PLAIN_TILE:
                fileName = "plain_tile.png";
                break;
            case BRICK_WALL:
                fileName = "brick_wall.png";
                break;
            case METAL_WALL:
                fileName = "metal_wall.png";
                break;
            case METAL_TILE:
                fileName = "metal_tile.png";
                break;
            case PLANT:
                fileName = "plante.png";
                break;
            case WATER:
                fileName = "water.png";
                break;
            case HEAD_QUARTER_LU:
                fileName = "HQ_LU.png";
                break;
            case HEAD_QUARTER_RU:
                fileName = "HQ_RU.png";
                break;
            case HEAD_QUARTER_LD:
                fileName = "HQ_LD.png";
                break;
            case HEAD_QUARTER_RD:
                fileName = "HQ_RD.png";
                break;
            case RESERVE:
                fileName = "reserve.png";
                break;
        }

        label.setIcon(new ImageIcon(getClass().getResource("/res/pic/"+fileName)));
    }

    void saveMap()  {
        String name = textField_MapName.getText();
        name+=".map";

        File mapFile = new File(name);

        try {
            mapFile.createNewFile();
            FileWriter writer = new FileWriter(mapFile);

            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    writer.write(Integer.toString(mapContent[i][j]));
                    writer.write(" ");
                }
                writer.write('\n');
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());

        if(e.getActionCommand().equals("Clear")) {
            tileSelected = PLAIN_TILE;
        } else if(e.getActionCommand().equals("Brick wall")) {
            tileSelected = BRICK_WALL;
        } else if(e.getActionCommand().equals("Metal wall")) {
            tileSelected = METAL_WALL;
        } else if(e.getActionCommand().equals("Metal tile")) {
            tileSelected = METAL_TILE;
        } else if(e.getActionCommand().equals("Plant")) {
            tileSelected = PLANT;
        } else if(e.getActionCommand().equals("Water")) {
            tileSelected = WATER;
        } else if(e.getActionCommand().endsWith("(L to R)")) {
            for(int i = 0;i<30;i++) {
                for(int j = 0;j<15;j++) {
                    if (tileEditable(i, j)) {
                        mapContent[i][29 - j] = mapContent[i][j];
                        changeIcon(labels_Map[i][29 - j], mapContent[i][j]);
                    }
                }
            }
        } else if(e.getActionCommand().endsWith("(R to L)")) {
            for(int i = 0;i<30;i++) {
                for(int j = 0;j<15;j++) {
                    if (tileEditable(i, j)) {
                        mapContent[i][j] = mapContent[i][29 - j];
                        changeIcon(labels_Map[i][j], mapContent[i][j]);
                    }
                }
            }
        } else if(e.getActionCommand().equals("Clear All")) {
            for(int i = 0;i<30;i++) {
                for(int j = 0;j<30;j++) {
                    if (tileEditable(i, j)) {
                        mapContent[i][j] = 0;
                        changeIcon(labels_Map[i][j],0);
                    }
                }
            }
        }
        else if(e.getActionCommand().equals("Save map")) {
            saveMap();
        } else if(e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getComponent().getName()+" clicked this");

        String[] slices = e.getComponent().getName().split("_");
        int row = Integer.valueOf(slices[0]);
        int column = Integer.valueOf(slices[1]);

        if(tileEditable(row,column)) {
            changeIcon(labels_Map[row][column], tileSelected);
            mapContent[row][column] = tileSelected;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getComponent().getName()+" pressed this");
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    static final int RESERVE = -1;
}
