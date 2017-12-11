package fnt;

public class Token {
    private TokenType mType;
    private String    mContent;

    public Token(TokenType type) {
        this(type, null);
    }

    public Token(TokenType type, String content) {
        this.mType    = type;
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public TokenType getType() {
        return mType;
    }
}
