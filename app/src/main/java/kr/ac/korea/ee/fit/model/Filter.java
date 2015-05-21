package kr.ac.korea.ee.fit.model;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 5. 21..
 */
public class Filter {

    public static final String COLORS = "COLORS";
    public static final String PATTERNS = "PATTERNS";

    int typeId;
    String typeLabel;
    ArrayList<Integer> colors;
    ArrayList<Integer> patterns;

    public Filter(int typeId, String typeLabel, ArrayList<Integer> colors, ArrayList<Integer> patterns) {
        this.typeId = typeId;
        this.typeLabel = typeLabel;
        this.colors = colors;
        this.patterns = patterns;
    }

    public int getTypeId() {
        return typeId;
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public ArrayList<Integer> getPatterns() {
        return patterns;
    }

    public String getTypeLabel() {
        return typeLabel;
    }
}
