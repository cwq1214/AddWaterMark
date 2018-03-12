package sample.util;

/**
 * Created by chenweiqi on 2018/3/9.
 */
public class TextUtil {

    public static boolean isEmpty(CharSequence str){
        if (str==null || str==""){
            return true;
        }
        return false;
    }
}
