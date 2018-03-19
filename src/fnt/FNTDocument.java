package fnt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FNTDocument {
    private FNTInfo          mInfo;
    private FNTCommon        mCommon;
    private List<FNTPage>    mPages;
    private List<FNTKerning> mKernings;

    protected FNTDocument() {
        this.mInfo     = new FNTInfo();
        this.mCommon   = new FNTCommon();
        this.mPages    = new ArrayList<FNTPage>();
        this.mKernings = new ArrayList<FNTKerning>();
    }

    /**
     * Call this method before using getChar(char)!
     * Sorts(ascending) and fills the space between the ids in the character array of all pages.
     */
    public void extendChars() {
        for (FNTPage page : this.mPages) {
            page.extendChars();
        }
    }

    /**
     * @param page The page to add.
     */
    protected void addPage(FNTPage page) {
        this.mPages.add(page);
    }

    /**
     * @param kerning The kerning to add.
     */
    protected void addKerning(FNTKerning kerning) {
        this.mKernings.add(kerning);
    }

    /**
     * @param c Requested character.
     *
     * @return The requested character or null.
     */
    public FNTChar getChar(char c) {
        return this.getPageOfChar(c).getChar(c);
    }

    /**
     * Returns the page that contains the given character.
     *
     * @param c Requested character.
     * @return The page that contains that character or null.
     */
    public FNTPage getPageOfChar(char c) {
        for (FNTPage page : this.mPages) {
            if (page.containsChar(c)) {
                return page;
            }
        }

        return null;
    }

    /**
     * @return The info like the size of the characters, bold, italic, etc.
     *         of the .fnt file.
     */
    public FNTInfo getInfo() {
        return this.mInfo;
    }

    /**
     * @return The common information like lineHeight,
     *         width and height of the texture files, page count, etc.
     *         of the .fnt file.
     */
    public FNTCommon getCommon() {
        return mCommon;
    }

    /**
     * @return The list of pages.
     */
    protected List<FNTPage> getPages() {
        return this.mPages;
    }

    /**
     * @param id The id of the requested page.
     *
     * @return The requested page or null.
     */
    public FNTPage getPage(int id) {
        return id < this.mPages.size() ? this.mPages.get(id) : null;
    }

    /**
     * @return The number of pages in the list.
     */
    public int getPageCount() {
        return this.mPages.size();
    }

    /**
     * @return The list of kernings
     */
    public List<FNTKerning> getKernings() {
        return mKernings;
    }

    /**
     * @return The number of kernings in the list.
     */
    public int getKerningCount() {
        return this.mKernings.size();
    }
}
