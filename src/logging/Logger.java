package logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger {
    private PrintWriter mPWriter;
    private FileWriter  mFileWriter;
    public Logger(){
        mPWriter = null;
        mFileWriter = null;
    }
    public void info(String txt){
        txt.trim();
        File mLogData = new File("Log.txt");
        SimpleDateFormat mFormatDate=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Timestamp mTimeStamp =new Timestamp(System.currentTimeMillis());
        String mdateString =mFormatDate.format(mTimeStamp);

        try {
            mFileWriter= new FileWriter(mLogData, true);
            mFileWriter.write("["+mdateString+"] Info: "+txt);
            mFileWriter.write(System.getProperty("line.separator"));
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void warning(String txt){
        txt.trim();
        File mLogData = new File("Log.txt");
        SimpleDateFormat mFormatDate=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Timestamp mTimeStamp =new Timestamp(System.currentTimeMillis());
        String mdateString =mFormatDate.format(mTimeStamp);

        try {
            mFileWriter= new FileWriter(mLogData, true);
            mFileWriter.write("["+mdateString+"] Warning: "+txt);
            mFileWriter.write(System.getProperty("line.separator"));
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void error(String txt){
        txt.trim();
        File mLogData = new File("Log.txt");
        SimpleDateFormat mFormatDate=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Timestamp mTimeStamp =new Timestamp(System.currentTimeMillis());
        String mdateString =mFormatDate.format(mTimeStamp);

        try {
            mFileWriter= new FileWriter(mLogData, true);
            mFileWriter.write("["+mdateString+"] Error: "+txt);
            mFileWriter.write(System.getProperty("line.separator"));
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
