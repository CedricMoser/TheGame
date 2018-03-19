package gui;

import graphics.Color;

public class Button {
    private int mHight;
    private int mWidth;
    private Color mColor;
    private int mXPos;
    private int mYPos;
    private String mName;
    private String mText;

    public int getHight() {
        return mHight;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getXPos() {
        return mXPos;
    }

    public Color getColor() {
        return mColor;
    }

    public int getYPos() {
        return mYPos;
    }

    public String getName() {
        return mName;
    }

    public String getmText() {
        return mText;
    }

    public void setColor(Color Color) {
        this.mColor = Color;
    }

    public void setHight(int Hight) {
        this.mHight = Hight;
    }

    public void setName(String Name) {
        this.mName = Name;
    }

    public void setText(String Text) {
        this.mText = Text;
    }

    public void setWidth(int Width) {
        this.mWidth = Width;
    }

    public void setXPos(int XPos) {
        this.mXPos = XPos;
    }

    public void setYPos(int YPos) {
        this.mYPos = YPos;
    }

    public void setPos(int XPos, int YPos) {
        this.mXPos = XPos;
        this.mYPos = YPos;
    }

    public void setSize(int Width, int Hight) {
        this.mWidth = Width;
        this.mHight = Hight;
    }

    public int[] getPos() {
        int[] Pos = new int[1];
        Pos[0] = this.mXPos;
        Pos[1] = this.mYPos;

        return Pos;

    }

    public int[] getSize() {
        int[] Size = new int[1];
        Size[0] = this.mWidth;
        Size[1] = this.mHight;
        return Size;
    }

}
