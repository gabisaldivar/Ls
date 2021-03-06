
package command;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCommandLs {
    @Test
    void test1() throws IOException, CmdLineException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        String[] args = new String[2];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-l";
        CommandLs.main(args);
        System.out.flush();
        System.setOut(old);
        System.out.println(baos.toString());
        assertEquals("Alv.txt 111 26/05/2019 10:46:09 1673 Bytes" + System.lineSeparator() +
                "Dashboard.pdf 111 13/12/2018 21:05:12 94481 Bytes"  + System.lineSeparator()  +
                "Zoc.txt 111 13/05/2019 19:07:41 1530 Bytes"  + System.lineSeparator(), (baos.toString()));
    }

    @Test
    void test2() throws IOException, CmdLineException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        String[] args = new String[3];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-l";
        args[2] = "-r";
        CommandLs.main(args);
        System.out.flush();
        System.setOut(old);
        System.out.println(baos.toString());
        assertEquals("Zoc.txt 111 13/05/2019 19:07:41 1530 Bytes" + System.lineSeparator() +
                "Dashboard.pdf 111 13/12/2018 21:05:12 94481 Bytes" + System.lineSeparator() +
                "Alv.txt 111 26/05/2019 10:46:09 1673 Bytes" + System.lineSeparator(), (baos.toString()));
    }

    @Test
    void test3() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        System.setErr(ps);
        String[] args = new String[3];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-h";
        args[2] = "-r";
        assertThrows(CmdLineException.class, () -> CommandLs.main(args));
        System.out.flush();
        System.err.flush();
        System.setOut(old);
        System.setErr(old);
        System.out.println(baos.toString());
        assertEquals("invalid input: option \"-h\" requires the option(s) [-l]" + System.lineSeparator(), (baos.toString()));
    }

    @Test
    void test4() throws IOException, CmdLineException {
        String[] args = new String[5];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-l";
        args[2] = "-h";
        args[3] = "-o";
        args[4] = "output.txt3";
        CommandLs.main(args);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("./src/test/resources/output/" +
                "output.txt3" ))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println(sb);
        assertEquals("Alv.txt rwx 26/05/2019 10:46:09 1.6337891 KB" + System.lineSeparator() +
                "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB" + System.lineSeparator() +
                "Zoc.txt rwx 13/05/2019 19:07:41 1.4941406 KB" + System.lineSeparator(), (sb.toString()));
    }

    @Test
    void test5() throws IOException, CmdLineException {
        String[] args = new String[6];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-l";
        args[2] = "-h";
        args[3] = "-r";
        args[4] = "-o";
        args[5] = "output.txt4";
        CommandLs.main(args);
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(Paths.get("./src/test/resources/output/" +
                "output.txt4" )))
        {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println(sb);
        assertEquals("Zoc.txt rwx 13/05/2019 19:07:41 1.4941406 KB" + System.lineSeparator() +
                "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB" + System.lineSeparator() +
                "Alv.txt rwx 26/05/2019 10:46:09 1.6337891 KB" + System.lineSeparator(), (sb.toString()));
    }

    @Test
    void nonexistentDirectory() throws IOException, CmdLineException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        String[] args = new String[3];
        args[0] = "./src/test/resources/gaby97";
        args[1] = "-l";
        args[2] = "-h";
        CommandLs.main(args);
        System.out.flush();
        System.setOut(old);
        System.out.println(baos.toString());
        assertEquals("selected file path does not exist: " + System.lineSeparator() +
                 ".\\src\\test\\resources\\gaby97" + System.lineSeparator(), (baos.toString()));
    }
}

