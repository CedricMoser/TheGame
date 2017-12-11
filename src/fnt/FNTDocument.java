package fnt;

import java.util.ArrayList;
import java.util.List;

public class FNTDocument {
    private FNTInfo          mInfo;
    private FNTCommon        mCommon;
    private FNTPage          mPage;
    private List<FNTChar>    mChars;
    private List<FNTKerning> mKernings;

    protected FNTDocument() {
        this.mInfo     = new FNTInfo();
        this.mCommon   = new FNTCommon();
        this.mPage     = new FNTPage();
        this.mChars    = new ArrayList<FNTChar>();
        this.mKernings = new ArrayList<FNTKerning>();
    }

    public void addChar(FNTChar fntChar) {
        this.mChars.add(fntChar);
    }

    public void addKerning(FNTKerning kerning) {
        this.mKernings.add(kerning);
    }

    public FNTInfo getInfo() {
        return this.mInfo;
    }

    public FNTCommon getCommon() {
        return mCommon;
    }

    public FNTPage getPage() {
        return mPage;
    }

    public List<FNTChar> getChars() {
        return mChars;
    }

    public List<FNTKerning> getKernings() {
        return mKernings;
    }
}
