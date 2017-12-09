package graphics;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.io.*;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int mProgram;
    private int mVertexShader;
    private int mFragmentShader;

    public Shader(String shaderPath) {
        this.mProgram = glCreateProgram();

        String[] srcs = this.getShaderSource(shaderPath);
        this.mVertexShader   = this.createShader(GL_VERTEX_SHADER, srcs[0]);
        this.mFragmentShader = this.createShader(GL_FRAGMENT_SHADER, srcs[1]);

        glAttachShader(this.mProgram, this.mVertexShader);
        glAttachShader(this.mProgram, this.mFragmentShader);

        glLinkProgram(this.mProgram);
        glValidateProgram(this.mProgram);
    }

    private int createShader(int type, String src) {
        int shader = glCreateShader(type);

        glShaderSource(shader, src);
        glCompileShader(shader);

        int status = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (status == GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(shader));
        }

        return shader;
    }

    private String[] getShaderSource(String shaderPath) {
        String[] source = new String[2];
        source[0] = "";
        source[1] = "";
        int index = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(shaderPath))));
            String line = "";

            while ((line = reader.readLine()) != null) {
                String test = line.replaceAll(" ", "").toLowerCase();
                if (test.equals("#fragment")) {
                    index = 1;
                } else if (test.equals("#vertex")) {
                    index = 0;
                } else {
                    source[index] += line + "\n";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(source[0]);
        System.out.println(source[1]);

        return source;
    }

    public int GetProgram() {
        return this.mProgram;
    }

    public void bind() {
        glUseProgram(this.mProgram);
    }

    public static void unbind() {
        glUseProgram(0);
    }
}
