package solutions.greensoftware.CSVMagick;


import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class DirtyTextGessOMatic {

    public enum DataType {
        DATETYP, INTTYP, DOUBLEFLOAT, TIMESTAMP, TEXTTYP, CHARTYP, BOOLTYP, NULLVAL
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
        // Fast fail here
        if (input == null) {
            return DataType.NULLVAL;
        }

        // Retrieve indices of regexs to use it in a switch instead of a list of if statements
        Integer[] matched = IntStream.range(0, regexs.length).filter(i -> regexs[i].matcher(input).find()).boxed().toArray(Integer[]::new);

        // Fast fail here
        if (matched.length == 0) {
            return DataType.NULLVAL;
        }

        switch (matched[0]) {
            case datetyp:
                return DataType.DATETYP;
            case timestamp:
                return DataType.TIMESTAMP;
            case chartyp:
                return DataType.CHARTYP;
            case booltyp:
                return DataType.BOOLTYP;
            case doublefloat:
                return DataType.DOUBLEFLOAT;
            case inttyp:
                return DataType.INTTYP;
            case texttyp:
                return DataType.TEXTTYP;
            default:
                return DataType.NULLVAL;
        }
    }
}
