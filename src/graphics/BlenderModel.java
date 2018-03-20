package graphics;

import fnt.Token;
import fnt.TokenType;
import fnt.Tokenizer;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class BlenderModel {
    private String mPath;
    private List<Vertex> mVertexis;

    public BlenderModel(String path) {
        this.mPath = path;
        this.mVertexis = new ArrayList<Vertex>();
    }

    private boolean load() {
        Tokenizer tkn = new Tokenizer();

        ArrayList<Token> specialChars = new ArrayList<>();
        specialChars.add(new Token(TokenType.HASH, "#"));
        specialChars.add(new Token(TokenType.SLASH, "/"));
        specialChars.add(new Token(TokenType.LPAREN, "("));
        specialChars.add(new Token(TokenType.RPAREN, ")"));
        specialChars.add(new Token(TokenType.DOUBLEPOINT, ":"));
        specialChars.add(new Token(TokenType.POINT, "."));
        specialChars.add(new Token(TokenType.NEWLINE, "\n"));

        tkn.skipNewlines(false);
        tkn.setSpecialChars(specialChars);
        tkn.setInput(FileUtils.readTextFile("test.obj"));


        return true;
    }

}
