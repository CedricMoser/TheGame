package fnt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FNTPage {
    private int           mId;
    private String        mFile;
    private List<FNTChar> mChars;

    protected FNTPage() {
        this.mId    = 0;
        this.mFile  = "";
        this.mChars = new ArrayList<FNTChar>();
    }

    /**
     * Call this method before using getChar(char) and containsChar(char)!
     * Sorts(ascending) and fills the space between the ids in the character array
     */
    protected void extendChars() {
        if (!this.mChars.isEmpty()) {
            this.mChars.sort(Comparator.comparingInt(o -> o.id));
            FNTChar last = this.mChars.get(0);

            for (int i = 1; i < this.mChars.size(); i++) {
                FNTChar cur = this.mChars.get(i);

                for (int j = last.id + 1; j < cur.id; j++, i++) {
                    this.mChars.add(i, null);
                }

                last = cur;
            }
        }
    }

    protected void setID(int id) {
        this.mId = id;
    }

    protected void setFile(String file) {
        this.mFile = file;
    }

    /**
     * @param fntChar Character to add.
     */
    protected void addChar(FNTChar fntChar) {
        this.mChars.add(fntChar);
    }

    public int getID() {
        return this.mId;
    }

    public String getFile() {
        return this.mFile;
    }

    public FNTChar getChar(char c) {
        return this.mChars.get(c);
    }

    public boolean containsChar(char c) {
        return c < this.mChars.size() && this.mChars.get(c) != null;
    }
}
