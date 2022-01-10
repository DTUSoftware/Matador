package dk.dtu.matador.objects.fields;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import gui_codebehind.GUI_Center;
import gui_codebehind.SwingComponentFactory;
import gui_fields.GUI_Ownable;
import gui_fields.GUI_Player;
import gui_resources.Attrs;

// Modified version of https://github.com/diplomit-dtu/Matador_GUI/blob/master/src/main/java/gui_fields/GUI_Brewery.java
public final class GUIBreweryField extends GUI_Ownable {
    private static final int TOPHEIGHT = 31;
    private static final int TITLEHEIGHT = 16;
    private static final int SUBTEXTHEIGHT = 14;
    private JLabel topLabel;
    private ImageIcon icon;
    private SwingComponentFactory factory = new SwingComponentFactory();
    private static int picCounter = 0;

    public GUIBreweryField(){
        this(PICTURE, TITLE, SUBTEXT, DESCRIPTION, RENT, BG_COLOR, FG_COLOR);
    }

    public GUIBreweryField(Image image){
        this(PICTURE, TITLE, SUBTEXT, DESCRIPTION, RENT, BG_COLOR, FG_COLOR);
        this.icon = new ImageIcon(image);
        this.topLabel.setIcon(this.icon);
    }

    public GUIBreweryField(String picture, String title, String subText, String description, String rent, Color bgColor, Color fgColor) {
        super(bgColor, fgColor, title, subText, description, rent);

        if ("default".equalsIgnoreCase(picture)) {
            int p = (picCounter++ % 2) + 1;
            String path = Attrs.getImagePath(String.format("GUI_Field.Image.Brewery%d", p));
            this.icon = this.factory.createIcon(path);
        } else {
            try {
                this.icon = new ImageIcon(picture);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(Attrs.getString("Error.BadArgument.ImagePath", picture));
            }
        }

        this.topLabel = makeTopLabel();
        this.titleLabel = makeRoadNameLabel(this.title);
        this.subTextLabel = makeBottomLabel(this.subText);
        this.layered.add(this.topLabel, this.factory.createGridBagConstraints(0, 0));
        this.layered.add(this.titleLabel, this.factory.createGridBagConstraints(0, 1));
        this.layered.add(this.subTextLabel, this.factory.createGridBagConstraints(0, 2));
    }

    private JLabel makeTopLabel() {
        JLabel l = makeLabel(TOPHEIGHT);
        l.setIcon(this.icon);
        return l;
    }
    private JLabel makeRoadNameLabel(String roadname) {
        JLabel roadnameLabel = makeLabel(TITLEHEIGHT);
        roadnameLabel.setText(roadname);
        return roadnameLabel;
    }
    private JLabel makeBottomLabel(String bottomText) {
        JLabel bottomLabel = makeLabel(SUBTEXTHEIGHT);
        bottomLabel.setText(bottomText);
        return bottomLabel;
    }
    @Override
    protected void displayOnCenter(GUI_Player[] playerList) {
        super.displayOnCenter(playerList);
        GUI_Center.label[1].setIcon(this.icon);
        GUI_Center.label[3].setText("__________________________");
        GUI_Center.label[3].setText(this.description);
        GUI_Center.label[4].setText(this.subText);
        if (this.ownerName != null) {
            GUI_Center.label[5].setText(getOwnableLabel() + getOwnerName());
            GUI_Center.label[6].setText(getRentLabel() + getRent());
        }
        super.displayCarOnCenter(playerList);
    }
    @Override
    public String toString() {
        return "GUI_Brewery [ownerName=" + ownerName
                + ", bgColor=" + bgColor + ", fgColor=" + fgColor + ", title="
                + title + ", subText=" + subText + ", description=" + description
                + "]";
    }


}
