package com.concordia.smarthomesimulator.helpers;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** CREDITS : The following methods are based on an Android Studio tutorial video by Coding In Flow
 *URL https://www.youtube.com/watch?v=EcfUkjlL9RI&t=505s
 */

public final class ActivityLogHelper {

    /**Log can be found in internal storage
     *Tools -> Device File Explorer
     *scroll down through directories to  /data/user/0/com.concordia.smarthomesimulator/files
     */
    final static String FILE_NAME = "activityLog";


    /**First call will create the activityLog file inside internal storage.
     *Subsequent calls will strictly append to the existing file.
     *
     * @param message   message desired for entry
     * @param component component associated to entry
     * @param ctx  Context of the application
     */
    public static void add(String message, String component, Context ctx){
        try {
            FileOutputStream fOut = ctx.openFileOutput(FILE_NAME,Context.MODE_APPEND);
            fOut.write(formatEntry(message,component).getBytes() );
            fOut.close();
            File fileDir = new File(ctx.getFilesDir(),FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Reads log in it's entirety and stores it in a String
     * which is returned and the end of execution.
     *
     * @param ctx  Context of the application
     * @return String   Contents of the activityLog file
     */
    public static String readLog(Context ctx){
        FileInputStream fIn = null;
        String text = "";

        try{
            fIn = ctx.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader br =  new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while((text = br.readLine()) !=  null){
                sb.append(text).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fIn != null){
                try {
                    fIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return text;
        }
    }

    /**This will delete the log.
     *Right click "files" in Device File Explorer and select Synchronize
     *in order to refresh folder.
     *
     * @param ctx  Context of the application
     * */
    public static void clearLog(Context ctx){
        File fileDir = new File(ctx.getFilesDir(),FILE_NAME);
        fileDir.delete();
    }

    /**Returns formatted activityLog entry
     *
     * @param message   message desired for entry
     * @param component component associated to entry
     * @return String   formatted entry
     */
    private static String formatEntry(String message,String component){
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateTime = new Date();
        return (message + "  " + component + "  " +  df.format(dateTime) +
                '\n');
    }
}