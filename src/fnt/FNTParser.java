package fnt;

import java.util.ArrayList;
import java.util.List;

public class FNTParser {
    private Tokenizer mTokenizer;
    private Token     mToken;

    public FNTParser() {
        this.mTokenizer = new Tokenizer();
        this.mToken     = null;
    }

    public FNTDocument parse(String input) {
        this.mTokenizer.setInput(input);

        FNTDocument doc = new FNTDocument();
        this.mToken     = this.mTokenizer.next();

        if (!info(doc)) return null;
        if (!common(doc)) return null;
        if (!page(doc)) return null;
        if (!chars(doc)) return null;
        if (!kernings(doc)) return null;

        return doc;
    }

    private boolean info(FNTDocument doc) {
        if (!accept(TokenType.IDENTIFIER, "info")) return false;

        String face = (String) this.equal("face", TokenType.STRING);
        if (face == null) return false;

        Integer size = (Integer) this.equal("size", TokenType.INT);
        if (size == null) return false;

        Integer boldi = (Integer) this.equal("bold", TokenType.INT);
        Boolean bold = boldi != null && boldi >= 1;
        if (boldi == null) return false;

        Integer italici = (Integer) this.equal("italic", TokenType.INT);
        Boolean italic = italici != null && italici >= 1;
        if (italici == null) return false;

        String charset = (String) this.equal("charset", TokenType.STRING);
        if (charset == null) return false;

        Integer unicodei = (Integer) this.equal("unicode", TokenType.INT);
        Boolean unicode = unicodei != null && unicodei >= 1;
        if (unicodei == null) return false;

        Integer stretchH = (Integer) this.equal("stretchH", TokenType.INT);
        if (stretchH == null) return false;

        Integer smoothi = (Integer) this.equal("smooth", TokenType.INT);
        Boolean smooth  = smoothi != null && smoothi >= 1;
        if (smoothi == null) return false;

        Integer aai = (Integer) this.equal("aa", TokenType.INT);
        Boolean aa  = aai != null && aai >= 1;
        if (aai == null) return false;

        Object[] padding = this.equal("padding", TokenType.INT, true);
        if (padding == null) return false;

        Object[] spacing = this.equal("spacing", TokenType.INT, true);
        if (spacing == null) return false;

        FNTInfo info = doc.getInfo();
        info.face = face;
        info.size = size;
        info.bold = bold;
        info.italic = italic;
        info.charset = charset;
        info.unicode = unicode;
        info.stretchH = stretchH;
        info.smooth = smooth;
        info.aa = aa;
        info.padding_Left   = (Integer) padding[0];
        info.padding_Right  = (Integer) padding[1];
        info.padding_Top    = (Integer) padding[2];
        info.padding_Bottom = (Integer) padding[3];
        info.spacing1       = (Integer) spacing[0];
        info.spacing2       = (Integer) spacing[1];

        return true;
    }

    private boolean common(FNTDocument doc) {
        if (!accept(TokenType.IDENTIFIER, "common")) return false;

        Integer lineHeight = (Integer) this.equal("lineHeight", TokenType.INT);
        if (lineHeight == null) return false;

        Integer base = (Integer) this.equal("base", TokenType.INT);
        if (base == null) return false;

        Integer scaleW = (Integer) this.equal("scaleW", TokenType.INT);
        if (scaleW == null) return false;

        Integer scaleH = (Integer) this.equal("scaleH", TokenType.INT);
        if (scaleH == null) return false;

        Integer pages = (Integer) this.equal("pages", TokenType.INT);
        if (pages == null) return false;

        Integer packedi = (Integer) this.equal("packed", TokenType.INT);
        Boolean packed = packedi != null && packedi >= 1;
        if (packedi == null) return false;

        FNTCommon common  = doc.getCommon();
        common.lineHeight = lineHeight;
        common.base       = base;
        common.scaleW     = scaleW;
        common.scaleH     = scaleH;
        common.pages      = pages;
        common.packed     = packed;

        return true;
    }

    private boolean page(FNTDocument doc) {
        if (!this.accept(TokenType.IDENTIFIER, "page")) return false;

        Integer id = (Integer) this.equal("id", TokenType.INT);
        if (id == null) return false;

        String file = (String) this.equal("file", TokenType.STRING);
        if (file == null) return false;

        FNTPage page = doc.getPage();
        page.id      = id;
        page.file    = file;

        return true;
    }

    private boolean chars(FNTDocument doc) {
        if (!this.accept(TokenType.IDENTIFIER, "chars")) return false;
        if (!this.accept(TokenType.IDENTIFIER, "count")) return false;
        if (!this.accept(TokenType.EQUALS, "")) return false;
        if (!this.expect(TokenType.INT)) return false;
        this.mToken = this.mTokenizer.next();

        while (this.expect(TokenType.IDENTIFIER, "char")) {
            this.mToken = this.mTokenizer.next();

            Integer id = (Integer) this.equal("id", TokenType.INT);
            if (id == null) return false;

            Integer x = (Integer) this.equal("x", TokenType.INT);
            if (x == null) return false;

            Integer y = (Integer) this.equal("y", TokenType.INT);
            if (y == null) return false;

            Integer width = (Integer) this.equal("width", TokenType.INT);
            if (width == null) return false;

            Integer height = (Integer) this.equal("height", TokenType.INT);
            if (height == null) return false;

            Integer xoffset = (Integer) this.equal("xoffset", TokenType.INT);
            if (xoffset == null) return false;

            Integer yoffset = (Integer) this.equal("yoffset", TokenType.INT);
            if (yoffset == null) return false;

            Integer xadvance = (Integer) this.equal("xadvance", TokenType.INT);
            if (xadvance == null) return false;

            Integer page = (Integer) this.equal("page", TokenType.INT);
            if (page == null) return false;

            Integer chnl = (Integer) this.equal("chnl", TokenType.INT);
            if (chnl == null) return false;

            FNTChar fntChar = new FNTChar();
            fntChar.id = id;
            fntChar.x = x;
            fntChar.y = y;
            fntChar.width = width;
            fntChar.height = height;
            fntChar.xoffset = xoffset;
            fntChar.yoffset = yoffset;
            fntChar.xadvance = xadvance;
            fntChar.page = page;
            fntChar.chnl = chnl;

            doc.addChar(fntChar);
        }

        return true;
    }

    private boolean kernings(FNTDocument doc) {
        if (!this.accept(TokenType.IDENTIFIER, "kernings")) return false;
        if (!this.accept(TokenType.IDENTIFIER, "count")) return false;
        if (!this.accept(TokenType.EQUALS, "")) return false;
        if (!this.expect(TokenType.INT)) return false;
        this.mToken = this.mTokenizer.next();

        while (this.expect(TokenType.IDENTIFIER, "kerning")) {
            this.mToken = this.mTokenizer.next();

            Integer first = (Integer) this.equal("first", TokenType.INT);
            if (first == null) return false;

            Integer second = (Integer) this.equal("second", TokenType.INT);
            if (second == null) return false;

            Integer amount = (Integer) this.equal("amount", TokenType.INT);
            if (amount == null) return false;

            FNTKerning kerning = new FNTKerning();
            kerning.first  = first;
            kerning.second = second;
            kerning.amount = amount;

            doc.addKerning(kerning);
        }

        return true;
    }

    private Object equal(String name, TokenType expectedType) {
        if (!accept(TokenType.IDENTIFIER, name)) return null;
        if (!accept(TokenType.EQUALS, "")) return null;
        if (!expect(expectedType)) return null;

        String content = this.mToken.getContent();
        this.mToken = this.mTokenizer.next();

        switch (expectedType) {
            case INT: return Integer.parseInt(content);
            case STRING:return content;
        }

        return null;
    }

    private Object[] equal(String name, TokenType expectedType, boolean array) {
        if (!accept(TokenType.IDENTIFIER, name)) return null;
        if (!accept(TokenType.EQUALS, "")) return null;
        if (!expect(expectedType)) return null;

        String content = this.mToken.getContent();
        this.mToken = this.mTokenizer.next();

        List<Object> ret = new ArrayList<Object>();

        switch (expectedType) {
            case INT:
                ret.add(Integer.parseInt(content));
                break;

            case STRING:
                ret.add(content);
                break;
        }

        while (this.mToken.getType() == TokenType.COMMA) {
            this.mToken = this.mTokenizer.next(); // skip comma

            if (!expect(expectedType)) return null;
            content = this.mToken.getContent();

            switch (expectedType) {
                case INT:
                    ret.add(Integer.parseInt(content));
                break;

                case STRING:
                    ret.add(content);
                break;
            }

            this.mToken = this.mTokenizer.next();
        }

        return ret.toArray();
    }

    private boolean expect(TokenType type) {
        return this.mToken.getType() == type;
    }

    private boolean expect(TokenType type, String name) {
        return this.mToken.getType() == type && this.mToken.getContent().equals(name);
    }

    private boolean accept(TokenType type, String content) {
        boolean res = this.mToken.getType() == type && this.mToken.getContent().equals(content);
        this.mToken = this.mTokenizer.next();

        return res;
    }
}
