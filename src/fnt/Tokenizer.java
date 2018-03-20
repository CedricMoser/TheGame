package fnt;

import java.util.ArrayList;

public class Tokenizer {
    private String           mInput;
    private int              mPos;
    private char             mCurrent;
    private boolean          mSkipNewlines;
    private ArrayList<Token> mSpecialChars;

    public Tokenizer() {
        this.setInput("");
        this.mSkipNewlines = true;
        this.mSpecialChars = new ArrayList<Token>();
        this.mSpecialChars.add(new Token(TokenType.EQUALS, "="));
        this.mSpecialChars.add(new Token(TokenType.COMMA , ","));
    }

    public void setInput(String input) {
        this.mInput   = input;
        this.mPos     = 0;
        this.mCurrent = '\0';
    }

    public Token next() {
        this.skipWhitespaces();

        StringBuilder content     = new StringBuilder();
        TokenType     type        = TokenType.EOF;
        char          stringBegin = '"';

        FOR:
        for (/*this.mPos*/; this.mPos < this.mInput.length(); this.mPos++) {
            this.mCurrent = this.mInput.charAt(this.mPos);

            switch (type) {
                case EOF:
                    for (Token cur : this.mSpecialChars) {
                        if (cur.getContent().equals(String.valueOf(this.mCurrent))) {
                            type = cur.getType();
                            this.mPos++;
                            break FOR;
                        }
                    }

                    if (this.isSymbol(this.mCurrent)) {
                        type = TokenType.IDENTIFIER;
                        content.append(this.mCurrent);
                    } else if (this.isDigit(this.mCurrent) || this.mCurrent == '-') {
                        type = TokenType.INT;
                        content.append(this.mCurrent);
                    } else if (this.mCurrent == '"' || this.mCurrent == '\'') {
                        type = TokenType.STRING;
                        stringBegin = this.mCurrent;
                    } else if (this.mCurrent != ' ' && this.mCurrent != '\n' && this.mCurrent != '\r' && this.mCurrent != '\t') {
                        // error
                        content.append(this.mCurrent);
                        type = TokenType.UNKNOWN;
                        this.mPos++;
                        break FOR;
                    }
                break;

                case IDENTIFIER:
                    if (this.isSymbol(this.mCurrent)) {
                        content.append(this.mCurrent);
                    } else {
                        //this.mPos--;
                        break FOR;
                    }
                break;

                case INT:
                    if (this.isDigit(this.mCurrent)) {
                        content.append(this.mCurrent);
                    } else if (this.mCurrent == '.') {
                        content.append(this.mCurrent);
                        type = TokenType.FLOAT;
                    } else {
                        //this.mPos--;
                        break FOR;
                    }
                break;

                case FLOAT:
                    if (this.isDigit(this.mCurrent)) {
                        content.append(this.mCurrent);
                    } else {
                        break FOR;
                    }
                break;

                case STRING:
                    if (this.mCurrent != stringBegin) {
                        content.append(this.mCurrent);
                    } else {
                        this.mPos++;
                        break FOR;
                    }
                break;
            }
        }

        return new Token(type, content.toString());
    }

    private boolean isSymbol(char c) {
        return (c >= 'A' && c <= 'Z')
        ||     (c >= 'a' && c <= 'z');
    }

    private  boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public void skipNewlines(boolean skip) {
        this.mSkipNewlines = skip;
    }

    public void setSpecialChars(ArrayList<Token> specialChars) {
        this.mSpecialChars = specialChars;
    }

    private void skipWhitespaces() {
        if (this.mSkipNewlines) {
            while ((this.mCurrent == ' ' || this.mCurrent == '\n' || this.mCurrent == '\r' || this.mCurrent == '\t') && this.mPos < this.mInput.length() - 1) {
                this.mCurrent = this.mInput.charAt(++this.mPos);
            }
        } else {
            while ((this.mCurrent == ' ' || this.mCurrent == '\r' || this.mCurrent == '\t') && this.mPos < this.mInput.length() - 1) {
                this.mCurrent = this.mInput.charAt(++this.mPos);
            }
        }
    }
}
