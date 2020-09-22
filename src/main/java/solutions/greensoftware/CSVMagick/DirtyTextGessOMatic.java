package solutions.greensoftware.CSVMagick;


import java.util.Arrays;
import java.util.regex.Pattern;

public class DirtyTextGessOMatic {

    public enum DataType {
        DATETYP, INTTYP, DOUBLEFLOAT, TIMESTAMP, TEXTTYP, CHARTYP, BOOLTYP, NULLVAl
    }

    static final int datetyp = 0;
    static final int inttyp = 1;
    static final int doublefloat = 2;
    static final int timestamp = 3;
    static final int texttyp = 4;
    static final int chartyp = 5;
    static final int booltyp = 6;


    static final Pattern[] regexs = new Pattern[]{
            Pattern.compile("^\\d+[/-]\\d+[/-]\\d+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("-?\\d+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^-?\\d+\\.\\d+$", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^\\d+[/-]\\d+[/-]\\d+\\s\\d+:\\d+(:\\d+)?(\\.\\d+\\+\\d+)?$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^[^\\d]$", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([Tt]rue|[Ff]alse)", Pattern.CASE_INSENSITIVE)
    };

    static DataType guesser(String input) {
        DataType res;
        if (input != null) {
            Boolean[] matched = Arrays.stream(regexs).map(e -> e.matcher(input).find()).toArray(Boolean[]::new);

            if (matched[datetyp]) {
                res = DataType.DATETYP;
            } else if (matched[timestamp]) {
                res = DataType.TIMESTAMP;
            } else if (matched[chartyp]) {
                res = DataType.CHARTYP;
            } else if (matched[booltyp]) {
                res = DataType.BOOLTYP;
            } else if (matched[doublefloat]) {
                res = DataType.DOUBLEFLOAT;
            } else if (matched[inttyp]) {
                res = DataType.INTTYP;
            } else if (matched[texttyp]) {
                res = DataType.TEXTTYP;
            } else {
                res = DataType.NULLVAl;
            }
        } else {
            res = DataType.NULLVAl;
        }

        return res;
    }
}
