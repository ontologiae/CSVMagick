package solutions.greensoftware.CSVMagick;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MainEndToEndIT {

    private PrintStream originalOut;
    private ByteArrayOutputStream outStream;

    private PrintStream originalErr;
    private ByteArrayOutputStream errStream;

    @BeforeEach
    public void setup() {
        outStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outStream));

        errStream = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void should_ingest_main_sample() {
        Main.main(new String[]{"./src/test/resources/municipales_2020.csv"});
        Approvals.verify(
                "SystemOut log :\n" + outStream.toString() +
                        "SystemErr log :\n" + errStream.toString());
    }

}