package VMail.Entity;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;


public class settings {
    public static String username;
    public static String password;
    public static Context context;
    public static String name;
    public static boolean stop = false;
    public static String path;

    public static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
