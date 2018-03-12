package sample.bean;


import java.awt.*;

/**
 * Created by chenweiqi on 2018/3/9.
 */
public class WaterMark {
    MarkType markType;

    String content;
    int  fontSize;
    String fontColor;

    int rotate;

    String imagePath;

    int hSpace;
    int vSpace;


    public WaterMark(MarkType markType, String content, int fontSize, String fontColor, int rotate,int hSpace,int vSpace) {
        this.markType = markType;
        this.content = content;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.rotate = rotate;
        this.hSpace = hSpace;
        this.vSpace = vSpace;

    }

    public WaterMark(MarkType markType, int rotate, String imagePath) {
        this.markType = markType;
        this.rotate = rotate;
        this.imagePath = imagePath;
    }


    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getFontColor() {
        try {
            return Color.decode(fontColor);
        }catch (Exception e){
            return Color.BLACK;
        }
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int gethSpace() {
        return hSpace;
    }

    public void sethSpace(int hSpace) {
        this.hSpace = hSpace;
    }

    public int getvSpace() {
        return vSpace;
    }

    public void setvSpace(int vSpace) {
        this.vSpace = vSpace;
    }
}
