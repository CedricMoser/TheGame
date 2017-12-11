package fnt;

public class Tokenizer {
    private String mInput;
    private int    mPos;
    private char   mCurrent;

    public Tokenizer() {
        this.setInput("");
    }

    public void setInput(String input) {
        this.mInput   = input;
        this.mPos     = 0;
        this.mCurrent = '\0';
    }

    public Token next() {
        this.skipWhitespaces();

        StringBuilder content = new StringBuilder();
        TokenType     type    = TokenType.EOF;

        FOR:
        for (/*this.mPos*/; this.mPos < this.mInput.length(); this.mPos++) {
            this.mCurrent = this.mInput.charAt(this.mPos);

            switch (type) {
                case EOF:
                    switch (this.mCurrent) {
                        case '=': type = TokenType.EQUALS; this.mPos++; break FOR;
                        case ',': type = TokenType.COMMA;  this.mPos++; break FOR;
                    }

                    if (this.isSymbol(this.mCurrent)) {
                        type = TokenType.IDENTIFIER;
                        content.append(this.mCurrent);
                    } else if (this.isDigit(this.mCurrent) || this.mCurrent == '-') {
                        type = TokenType.INT;
                        content.append(this.mCurrent);
                    } else if (this.mCurrent == '"') {
                        type = TokenType.STRING;
                    } else {
                        // error
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
                    } else {
                        //this.mPos--;
                        break FOR;
                    }
                break;

                case STRING:
                    if (this.mCurrent != '"') {
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

    private void skipWhitespaces() {
        while ((this.mCurrent == ' ' || this.mCurrent == '\n' || this.mCurrent == '\r' || this.mCurrent == '\t') && this.mPos < this.mInput.length() - 1) {
            this.mCurrent = this.mInput.charAt(++this.mPos);
        }
    }
}
