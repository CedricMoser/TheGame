package logging;

import java.io.*;

public class Logger {

    public Logger(String Loggername,String error){
        PrintWriter pWriter = null;
        Loggername.trim();
        error.trim();
        FileWriter fileWriter;
        File LogData = new File("Log.txt");
        try {
            fileWriter= new FileWriter(LogData, true);
            fileWriter.write("#"+Loggername+": "+error);
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
