package dk.dtu.matador.objects.fields;

import gui_codebehind.GUI_Center;
import gui_codebehind.SwingComponentFactory;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_resources.Attrs;

import javax.swing.*;
import java.awt.*;

// Modified version of https://github.com/diplomit-dtu/Matador_GUI/blob/master/src/main/java/gui_fields/GUI_Jail.java
public class GUIJailField extends GUI_Field {
    private static final int TOPHEIGHT = 47;
    private static final int SUBTEXTHEIGHT = 14;
    private ImageIcon icon;
    private final JLabel topLabel;
    private final SwingComponentFactory factory = new SwingComponentFactory();
    private static int picCounter = 0;

    public GUIJailField(){
        this(PICTURE, TITLE, SUBTEXT, DESCRIPTION, new Color(125, 125, 125), Color.BLACK);
    }

    public GUIJailField(String picture, String title, String subText, String description, Color bgColor, Color fgColor){
        super(bgColor, fgColor, title, subText, description);

        if("default".equalsIgnoreCase(picture)){
            int p = (picCounter++ % 2);
            String path1 = Attrs.getImagePath("GUI_Field.Image.GoToJail");
            String path2 = Attrs.getImagePath("GUI_Field.Image.Jail");
            this.icon = this.factory.createIcon(p>0 ? path1 : path2);
        }
        else if (picture.startsWith("GUI_Field.Image")) {
            this.icon = this.factory.createIcon(Attrs.getImagePath(picture));
        }
        else{
            try{
                this.icon = new ImageIcon(picture);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Bad Resource: "+picture);
            }
        }

        this.topLabel = makeTopLabel();
        this.subTextLabel = makeBottomLabel(this.subText);
        this.layered.add(this.topLabel, this.factory.createGridBagConstraints(0, 0));
        this.layered.add(this.subTextLabel, this.factory.createGridBagConstraints(0, 1));
    }
    private JLabel makeTopLabel(){
        JLabel l = makeLabel(TOPHEIGHT);
        l.setIcon(this.icon);
        return l;
    }
    private JLabel makeBottomLabel(String subTextJail){
        JLabel bottomLabel = makeLabel(SUBTEXTHEIGHT);
        bottomLabel.setText(subTextJail);
        return bottomLabel;
    }
    public String getBottomText(){
        return this.subText;
    }
    @Override
    protected void displayOnCenter(GUI_Player[] playerList){
        super.displayOnCenter(playerList);
        GUI_Center.label[1].setIcon(this.icon);
        GUI_Center.label[2].setText("__________________________");
        GUI_Center.label[3].setText(this.description);
        super.displayCarOnCenter(playerList);
    }
    @Override
    public String toString() {
        return "GUI_Jail [bgColor=" + bgColor
                + ", fgColor=" + fgColor + ", title=" + title + ", subText="
                + subText + ", description=" + description + "]";
    }

}
