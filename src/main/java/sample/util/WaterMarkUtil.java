package sample.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.poi.ss.util.WorkbookUtil;
import sample.bean.WaterMark;
import sun.awt.image.JPEGImageDecoder;

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
        int suffixBeginIndex = sourcePath.lastIndexOf("/");
        String fileName = sourcePath.substring(suffixBeginIndex + 1, sourcePath.length());
        fileName = fileName.toLowerCase();

        int FONT_SIZE = waterMark.getFontSize();
        String MARK_TEXT = waterMark.getContent();
        Color FONT_COLOR = waterMark.getFontColor();

        int V_SPACE = waterMark.getvSpace();
        int H_SPACE = waterMark.gethSpace();
//        Color FONT_COLOR = Color.GRAY;
        int ROTATE = waterMark.getRotate();


        if (fileName.matches(".*.pdf")) {

//         Document document = new Document();
//
//            PdfReader reader = new PdfReader(sourcePath);
//            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(new File(savePath+"/"+fileName)));
//            PdfGState pdfGState = new PdfGState();
//            pdfGState.setFillOpacity(0.2f);
//            int pages = reader.getNumberOfPages();
//
//            PdfContentByte under;
//
//            System.out.println("pages:"+pages);
//
//            int length = getTextLength(MARK_TEXT);
//
//            int textImgWidth = length*FONT_SIZE;
//            int textImgHeight = (int) (FONT_SIZE*1.5);
//
//            //根据文字生成临时图片
//            BufferedImage bufferedImage = new BufferedImage(textImgWidth,textImgHeight,BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics2d = bufferedImage.createGraphics();
//            bufferedImage = graphics2d.getDeviceConfiguration().createCompatibleImage(textImgWidth, textImgHeight, Transparency.TRANSLUCENT);
//            graphics2d = bufferedImage.createGraphics();
//            graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
//            graphics2d.setColor(FONT_COLOR);
//            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, ALPHA));
//            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
//            graphics2d.drawString(MARK_TEXT,0,FONT_SIZE);
//            graphics2d.dispose();
//            File saveFile =new File("tempImage.png");
//            saveFile.createNewFile();
//            ImageIO.write(bufferedImage,"PNG",saveFile);
//
//
//            for (int i=1;i<pages+1;i++){
//                under = stamper.getUnderContent(i);
//
//
//                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("tempImage.png");
//                image.setAbsolutePosition(50,50);
//                under.addImage(image);
//
//            }
//            stamper.close();
//            reader.close();


        } else if (fileName.matches(".*.(jpg|png|gif|jpeg)")) {


            Image image= ImageIO.read(new File(sourcePath));
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2d = bufferedImage.createGraphics();
            graphics2d.drawImage(image, 0, 0, width, height, null);
            graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graphics2d.setColor(FONT_COLOR);

            //使用绘图工具将水印绘制到图片上
            //计算文字水印宽高值
            int waterWidth = FONT_SIZE*getTextLength(MARK_TEXT);
            int waterHeight = FONT_SIZE;

            //水印透明设置
            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
            graphics2d.rotate(Math.toRadians(ROTATE), bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);

            int x = -width/2;
            int y = -height/2;

            while(x < width*1.5){
                y = -height/2;
                while(y < height*1.5){
                    graphics2d.drawString(MARK_TEXT, x, y);
                    y+=waterHeight+V_SPACE;
                }
                x+=waterWidth+H_SPACE;
            }
            graphics2d.dispose();


            File file ;
            File savePathFile= new File(savePath);
            if (savePathFile.isFile()){
                file = savePathFile;
            }else {
                file = new File(savePath+"/"+fileName);
                if (!file.exists()) {
                    if (file.getParentFile()!=null)
                        file.getParentFile().mkdirs();
                }

            }

            ImageIO.write(bufferedImage,"PNG",file);
//            OutputStream os = new FileOutputStream(file);
//            //创建图像编码工具类
//            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
//            //使用图像编码工具类，输出缓存图像到目标文件
//            en.encode(bufferedImage);
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

        if ((length/2+"").contains(".")){
            length = length+1;
        }
        return length;
    }


}
