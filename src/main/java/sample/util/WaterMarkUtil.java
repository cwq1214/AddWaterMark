package sample.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.apache.poi.ss.util.WorkbookUtil;
import sample.bean.WaterMark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by chenweiqi on 2018/3/9.
 */
public class WaterMarkUtil {

    public String FONT_NAME = "宋体";
    public int FONT_STYLE = Font.BOLD;
    public float ALPHA = 0.5f;

    private static WaterMarkUtil waterMarkUtil = new WaterMarkUtil();

    public static WaterMarkUtil getInstance() {
        return waterMarkUtil;
    }

    public void create(String sourcePath, String savePath, WaterMark waterMark)throws Exception {


        String fileName = new File(sourcePath).getName();

        int FONT_SIZE = waterMark.getFontSize();
        String MARK_TEXT = waterMark.getContent();
        Color FONT_COLOR = waterMark.getFontColor();

        int V_SPACE = waterMark.getvSpace();
        int H_SPACE = waterMark.gethSpace();
//        FONT_COLOR = Color.RED;
        int ROTATE = waterMark.getRotate();


        if (fileName.matches(".*.pdf")) {
            if (!savePath.toLowerCase().contains(".pdf")){
                String name = "未命名";
                if (!savePath.endsWith("/")){
                    name = "/" + name;
                }
                savePath =savePath+name;
            }
         Document document = new Document();

            PdfReader reader = new PdfReader(sourcePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(new File(savePath)));
            PdfGState pdfGState = new PdfGState();
            pdfGState.setFillOpacity(0.2f);
            int pages = reader.getNumberOfPages();


            System.out.println("pages:"+pages);

            int length = getTextLength(MARK_TEXT);


            int A4Height = 842;
            int A4Width = 595;
            //根据文字生成临时图片
            BufferedImage bufferedImage = new BufferedImage(A4Height,A4Height,BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2d = bufferedImage.createGraphics();
            bufferedImage = graphics2d.getDeviceConfiguration().createCompatibleImage(A4Width, A4Height, Transparency.TRANSLUCENT);
            graphics2d = bufferedImage.createGraphics();
            graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graphics2d.setColor(FONT_COLOR);
            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, ALPHA));
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);


            graphics2d.rotate(Math.toRadians(ROTATE), bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);

            drawText(graphics2d,A4Width,A4Height,MARK_TEXT,FONT_SIZE,V_SPACE,H_SPACE);


            graphics2d.dispose();

            File saveFile =new File("tempImage.png");
            saveFile.createNewFile();
            ImageIO.write(bufferedImage,"PNG",saveFile);

            PdfContentByte over;

            for (int i=1;i<pages+1;i++){
                over = stamper.getOverContent(i);
//              over   = stamper.getUnderContent(i);


                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("tempImage.png");
                image.setAbsolutePosition(0,0);
                over.addImage(image);

            }
            stamper.close();
            reader.close();
            saveFile.deleteOnExit();

        } else if (fileName.matches(".*.(jpg|png|gif|jpeg)")) {

            Image image= ImageIO.read(new File(sourcePath));
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2d = bufferedImage.createGraphics();
            graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graphics2d.setColor(FONT_COLOR);
            graphics2d.drawImage(image, 0, 0, width, height, null);

            //使用绘图工具将水印绘制到图片上
            //计算文字水印宽高值


            //水印透明设置
            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

            graphics2d.rotate(Math.toRadians(ROTATE), bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);

            drawText(graphics2d,width,height,MARK_TEXT,FONT_SIZE,V_SPACE,H_SPACE);


            graphics2d.dispose();


            File file ;
            File savePathFile= new File(savePath);
            if (savePath.contains(".")){
                if (savePathFile.getParentFile()!=null){
                    savePathFile.getParentFile().mkdirs();
                }
                file = savePathFile;
            }else {
                file = new File(savePath+"/"+fileName);
                if (!file.exists()) {
                    if (file.getParentFile()!=null)
                        file.getParentFile().mkdirs();
                }

            }

            ImageIO.write(bufferedImage,"PNG",file);
        }else {
            throw new Exception("位置类型");
        }

    }

    private void drawText(Graphics2D graphics2D,int width,int height,String text,int fontSize,int vSpace,int hSpace){
        int maxSize = (int) ((width>=height?height:height) *2);

        int waterWidth = fontSize*getTextLength(text);
        int waterHeight = fontSize;

        int x = -maxSize;
        int y = -maxSize;
        while(x < maxSize){
            while(y < maxSize){
                System.out.println("drawString ");
                System.out.println("x "+x);
                System.out.println("y "+y);
                graphics2D.drawString(text, x, y);
                y+=waterHeight+vSpace;
            }
            y =  -maxSize;
            x+=waterWidth+hSpace;
        }
    }


    public int getTextLength(String text) {
//水印文字长度
        int length = text.length();

        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
//        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        length=length/2;
        if ((length+"").contains(".")){
            length = length+1;
        }
        return length;
    }


}
