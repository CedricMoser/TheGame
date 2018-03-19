package utils;

import java.io.*;

public class FileUtils {
    /**
     * @param path Filepath.
     * @return Content of the file as a String.
     */
    public static String readTextFile(String path) {
        StringBuilder  builder = new StringBuilder();
        BufferedReader reader  = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader != null) {
            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
