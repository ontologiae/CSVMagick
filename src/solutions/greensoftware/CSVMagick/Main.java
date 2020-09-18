package solutions.greensoftware.CSVMagick;

import org.javatuples.Triplet;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;


import org.javatuples.Pair;

import javax.swing.text.html.Option;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            readVariableColumnsWithCsvListReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Optional<Triplet<CsvPreference,Integer,List<DirtyTextGessOMatic.DataType>>> guessCsvTypeAndColumns(String filePath) throws IOException {
        // On renvoi la 1ère analyse du CSV : nombre de colonne, type de CSV, et liste de type de colonne à confirmer
        CsvPreference prefs[] = new CsvPreference[] {
            CsvPreference.TAB_PREFERENCE,
            CsvPreference.STANDARD_PREFERENCE,
            CsvPreference.EXCEL_PREFERENCE,
            CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE
        };

        CsvPreference findGoodCsvType;

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }


        FileReader finalFileReader = fileReader;
        var goodPrefs = Arrays.stream(prefs).filter(e -> {
            CsvListReader listReader = new CsvListReader(finalFileReader, e);
                    try {
                        var header = listReader.getHeader(true);
                        return (listReader.read() != null && (listReader.length() > 1));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return false;
                }
        ).findFirst();

        if (goodPrefs.isPresent()) {

            int cpt = 0;
            ICsvListReader listReader = null;
            try {
                listReader = new CsvListReader(fileReader, goodPrefs.get());

                listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
                List<String> csvLine = new LinkedList<String>();
                int max = 0;
                while ((csvLine = listReader.read()) != null && cpt < 128) { // on ne lit que 128 lignes
                    cpt++;
                    max = Math.max(listReader.length(), max);

                    //TODO algo qui fait la synthèse de type sur les 128 lignes d'échantillon
                    int idx = (int) (Math.random() * max);
                    if (DirtyTextGessOMatic.guesser(csvLine.get(idx)) != DirtyTextGessOMatic.DataType.NULLVAl)
                        System.out.println(max + ";" + idx + ";" + csvLine.get(idx) + ";" + DirtyTextGessOMatic.guesser(csvLine.get(idx)));
                }
            } finally {
                if (listReader != null) {
                    listReader.close();
                }
            }
            return Optional.of(Triplet.with(goodPrefs.get(),max,// TODO: 16/09/2020  ))
        } else return Optional.empty();
    }

    private static void readVariableColumnsWithCsvListReader() throws Exception {

        final CellProcessor[] allProcessors = new CellProcessor[]{new UniqueHashCode(), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
                new ParseDate("dd/MM/yyyy")}; // birthDate

        final CellProcessor[] noBirthDateProcessors = new CellProcessor[]{allProcessors[0], // customerNo
                allProcessors[1], // firstName
                allProcessors[2]}; // lastName


        // Stratégie
        // on lit 128 lignes, si on a une seule colonne, alors le type de CSV n'est pas le bon, on essaye un autre
        // Lorsque type CSV trouvé, on calcul la liste des parseurs qui correspondent au type de fichier, en faisant un group by colonne, puis un distinct
        // et on regarde si on a plusieurs types et si oui, si on un texte qui se glisse dedans, en fonction on construit le parseur
        // Ensuite on génère le SQL
        // Plus tard on gèrera l'OBDC
        final CsvContext ANONYMOUS_CSVCONTEXT = new CsvContext(1, 2, 3);

        ICsvListReader listReader = null;
        //CellProcessor cellDateValidator = (CellProcessor) new ParseDate("dd/MM/yyyy").execute("25/12/2011", ANONYMOUS_CSVCONTEXT);
        try {
            listReader = new CsvListReader(new FileReader(
                    // /Users/ontologiae/Documents/Projets/NAINWAK/Nainwak/Events.csv - Dump postgresql
                    //"/Users/ontologiae/Documents/Projets/CODE-ANALYSIS/ReposCodePourTest/metasfresh/de.metas.adempiere.adempiere/client/target/test-classes/org/compiere/apps/form/fileimport/regularlines.csv"
                    "/Users/ontologiae/Downloads/municipales_2020.csv"
            ),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
            List<String> csvLine = new LinkedList<String>();
            int max = 0;
            //System.out.println(csvLine);
            //final CellProcessor[] processors;
            /*                if( listReader.length() == noBirthDateProcessors.length ) {
                    processors = noBirthDateProcessors;
                } else {
                    processors = allProcessors;
                }*/
            //final List<Object> customerList = listReader.executeProcessors(processors);
            //System.out.println(String.format("lineNo=%s, rowNo=%s, columns=%s, customerList=%s",
            //        listReader.getLineNumber(), listReader.getRowNumber(), customerList.size(), customerList));
            while ((csvLine = listReader.read()) != null) {
                max = Math.max(listReader.length(), max);
                int idx = (int) (Math.random() * max);
                if (DirtyTextGessOMatic.guesser(csvLine.get(idx)) != DirtyTextGessOMatic.DataType.NULLVAl)
                    System.out.println(max + ";" + idx + ";" + csvLine.get(idx) + ";" + DirtyTextGessOMatic.guesser(csvLine.get(idx)));
            }
            System.out.println(String.format("%d colonnes max", max));

        } finally {
            if (listReader != null) {
                listReader.close();
            }
        }
    }
}
