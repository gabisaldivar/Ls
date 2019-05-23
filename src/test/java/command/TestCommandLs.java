
package command;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("Dashboard.pdf 111 13/12/2018 21:05:12 94481 Bytes\r\n" +
                "file1.txt 111 13/05/2019 19:04:10 1673 Bytes\r\n" +
                "file2.txt 111 13/05/2019 19:07:41 1530 Bytes\r\n", (baos.toString()));
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
        assertEquals("file2.txt 111 13/05/2019 19:07:41 1530 Bytes\r\n" +
                "file1.txt 111 13/05/2019 19:04:10 1673 Bytes\r\n" +
                "Dashboard.pdf 111 13/12/2018 21:05:12 94481 Bytes\r\n", (baos.toString()));
    }

    @Test
    void test3() throws IOException, CmdLineException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        System.setErr(ps);
        String[] args = new String[3];
        args[0] = "./src/test/resources/dir1";
        args[1] = "-h";
        args[2] = "-r";
        try {
            CommandLs.main(args);
        } catch (CmdLineException e) {

        }
        System.out.flush();
        System.err.flush();
        System.setOut(old);
        System.setErr(old);
        System.out.println(baos.toString());
        assertEquals("invalid input: option \"-h\" requires the option(s) [-l]\r\n", (baos.toString()));
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
                sb.append(line).append("\r\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println(sb);
        assertEquals("Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB\r\n" +
                "file1.txt rwx 13/05/2019 19:04:10 1.6337891 KB\r\n" +
                "file2.txt rwx 13/05/2019 19:07:41 1.4941406 KB\r\n", (sb.toString()));
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
                sb.append(line).append("\r\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println(sb);
        assertEquals("file2.txt rwx 13/05/2019 19:07:41 1.4941406 KB\r\n" +
                "file1.txt rwx 13/05/2019 19:04:10 1.6337891 KB\r\n" +
                "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB\r\n", (sb.toString()));
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
        assertEquals("selected file path does not exist: \r\n" +
                ".\\src\\test\\resources\\gaby97\r\n", (baos.toString()));
    }
}

