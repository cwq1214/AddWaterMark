package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.bean.MarkType;
import sample.bean.WaterMark;
import sample.util.TextUtil;
import sample.util.WaterMarkUtil;

import java.io.File;


public class Controller {
    @FXML
    TextField inputMarkContent;
    @FXML
    TextField inputMarkFontSize;
    @FXML
    TextField inputMarkRotate;
    @FXML
    TextField inputMarkColor;
    @FXML
    TextField inputSourcePath;
    @FXML
    TextField inputSavePath;
    @FXML
    Button btnSelSourcePath;
    @FXML
    Button btnSelSavePath;
    @FXML
    Button btnCreate;
    @FXML
    TextField inputHorizontalSpace;
    @FXML
    TextField inputVerticalSpace;


    String markContent ;
    String fontSize ;
    String rotate ;
    String color ;

    String sourcePath;
    String savePath;

    String hSpace;
    String vSpace;



    @FXML
    private void initialize() {
        setListener();
        setDefaultValue();

    }

    private void setDefaultValue(){
        inputMarkContent.setText("广州聚义堂科技有限公司");
        inputMarkFontSize.setText("24");
        inputMarkColor.setText("#808080");
        inputMarkRotate.setText("-45");
        inputHorizontalSpace.setText("100");
        inputVerticalSpace.setText("100");
    }

    private void setListener(){

        inputMarkContent.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                markContent = inputMarkContent.getText();
            }
        });

        inputMarkFontSize.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fontSize = inputMarkFontSize.getText();
            }
        });

        inputMarkRotate.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rotate = inputMarkRotate.getText();
            }
        });

        inputMarkColor.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                color = inputMarkColor.getText();
            }
        });
        inputSourcePath.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                sourcePath = inputSourcePath.getText();

                int lastDot = sourcePath.lastIndexOf(".");
                String savePath = sourcePath.substring(0,lastDot)+"___"+sourcePath.substring(lastDot,sourcePath.length());
                inputSavePath.setText(savePath);
            }
        });

        inputSourcePath.setOnDragOver(new DragOverEvent(inputSourcePath));

        inputSourcePath.setOnDragDropped(new DragDroppedEvent(inputSourcePath));

        inputSavePath.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                savePath = inputSavePath.getText();
            }
        });




        inputHorizontalSpace.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                hSpace = inputHorizontalSpace.getText();
            }
        });

        inputVerticalSpace.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                vSpace = inputVerticalSpace.getText();
            }
        });


        btnSelSourcePath.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("选择文件");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("图片", "*.*")
//                        new FileChooser.ExtensionFilter("GIF", "*.gif"),
//                        new FileChooser.ExtensionFilter("BMP", "*.bmp"),
//                        new FileChooser.ExtensionFilter("PNG", "*.png"),
//                        new FileChooser.ExtensionFilter("PDF", "*.pdf")

                );
                File file = fileChooser.showOpenDialog(new Stage());
                if (file!=null){
                    if (!file.getName().toLowerCase().matches(".*.(jpg|png|jpeg|pdf)")){
                        new Alert(Alert.AlertType.INFORMATION,"目前只支持JPG,PNG,JPEG,PDF").show();
                        return;
                    }

                    inputSourcePath.setText(file.getAbsolutePath());
                }
            }
        });


        btnSelSavePath.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("选择保存路径");
                File file = fileChooser.showSaveDialog(new Stage());
                if (file!=null){
                    inputSavePath.setText(file.getAbsolutePath());
                }
            }
        });


        btnCreate.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (TextUtil.isEmpty(markContent)){
                    new Alert(Alert.AlertType.ERROR,"水印内容不能为空").show();
                    return ;
                }
                if (TextUtil.isEmpty(fontSize)){
                    new Alert(Alert.AlertType.ERROR,"字体大小不能为空").show();
                    return ;
                }else if (!fontSize.matches("-?[0-9].?[0-9]*")){
                    new Alert(Alert.AlertType.ERROR,"请输入数字").show();
                    return ;
                }
                if (TextUtil.isEmpty(rotate)){
                    new Alert(Alert.AlertType.ERROR,"旋转角度不能为空").show();
                    return ;
                }else if (!rotate.matches("-?[0-9].?[0-9]*")){
                    new Alert(Alert.AlertType.ERROR,"请输入数字").show();
                    return ;
                }
                if (TextUtil.isEmpty(color)){
                    new Alert(Alert.AlertType.ERROR,"水印内容不能为空").show();
                    return ;
                }else if (!color.matches("^#([0-9|A-F]{3}|[0-9|A-F]{6})")){
                    System.out.println(color);
                    new Alert(Alert.AlertType.ERROR,"请输入正确颜色").show();
                    return ;
                }

                if (TextUtil.isEmpty(sourcePath)){
                    new Alert(Alert.AlertType.ERROR,"请输入文件路径").show();
                    return ;
                }
                if (TextUtil.isEmpty(savePath)){
                    new Alert(Alert.AlertType.ERROR,"请输入文件保存路径").show();
                    return ;
                }
                if (TextUtil.isEmpty(hSpace)){
                    new Alert(Alert.AlertType.ERROR,"请输入水平间距").show();
                    return ;
                }
                if (TextUtil.isEmpty(vSpace)){
                    new Alert(Alert.AlertType.ERROR,"请输入垂直间距").show();
                    return ;
                }


                WaterMark waterMark = new WaterMark(MarkType.TEXT,markContent,Integer.valueOf(fontSize),color,Integer.valueOf(rotate),Integer.valueOf(hSpace),Integer.valueOf(vSpace));

                try {
                    WaterMarkUtil.getInstance().create(sourcePath,savePath,waterMark);
                    new Alert(Alert.AlertType.CONFIRMATION,"生成完毕").show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }

            }
        });
    }

    public class DragOverEvent implements EventHandler<DragEvent> {

        private TextField textField;

        public DragOverEvent(TextField textField){
            this.textField = textField;
        }

        public void handle(DragEvent event) {

            if (event.getGestureSource() != textField){
                event.acceptTransferModes(TransferMode.ANY);
            }
        }
    }

    public class DragDroppedEvent implements EventHandler<DragEvent> {


        private TextField textField;

        public DragDroppedEvent(TextField textField){
            this.textField = textField;
        }

        public void handle(DragEvent event) {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()){
                try {
                    File file = dragboard.getFiles().get(0);
                    if (file != null) {
                        textField.setText(file.getAbsolutePath());
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
