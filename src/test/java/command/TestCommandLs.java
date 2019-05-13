package command;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommandLs {
    @org.junit.jupiter.api.Test
    public void test1() throws IOException {
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[2];
        args[0] = "ls";
        args[1] = "-l";
        String goodResult = ("Dashboard.pdf 111 13/12/2018 21:05:12 94481 Bytes\n" +
                "file1.txt 111 28/04/2019 17:32:02 7 Bytes\n" +
        "file2.txt 111 28/04/2019 17:32:20 7 Bytes\n");
        commandLs.process(args);
        String result = commandLs.getResult();
        assertTrue(goodResult.equals(result));
    }

    @org.junit.jupiter.api.Test
    public void test2() throws IOException {
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[2];
        args[0] = "ls";
        args[1] = "-h";
        String goodResult = "Dashboard.pdf\n" +
                "file1.txt\n" +
                "file2.txt\n";
        commandLs.process(args);
        String result = commandLs.getResult();
        assertTrue(goodResult.equals(result));
    }

    @org.junit.jupiter.api.Test
    public void test3() throws IOException {
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[2];
        args[0] = "ls";
        args[1] = "-r";
        String goodResult = "file2.txt\n" +
                "file1.txt\n" +
                "Dashboard.pdf\n";
        commandLs.process(args);
        String result = commandLs.getResult();

        assertTrue(goodResult.equals(result));
    }

    @org.junit.jupiter.api.Test
    public void test4() throws IOException{
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[3];
        args[0] = "ls";
        args[1] = "-l";
        args[2] = "-h";
        String goodResult = "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB\n" +
                "file1.txt rwx 28/04/2019 17:32:02 0.0068359375 KB\n" +
                "file2.txt rwx 28/04/2019 17:32:20 0.0068359375 KB\n" ;
        commandLs.process(args);
        String result = commandLs.getResult();
        assertTrue(goodResult.equals(result));
    }

    @org.junit.jupiter.api.Test
    public void test5() throws IOException{
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[4];
        args[0] = "ls";
        args[1] = "-l";
        args[2] = "-h";
        args[3] = "-r";
        String goodResult = "file2.txt rwx 28/04/2019 17:32:20 0.0068359375 KB\n" +
                "file1.txt rwx 28/04/2019 17:32:02 0.0068359375 KB\n" +
                "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB\n";
        commandLs.process(args);
        String result = commandLs.getResult();
        assertTrue(goodResult.equals(result));
    }

    @org.junit.jupiter.api.Test
    public void inexistenceDirectory() throws IOException{
        CommandLs commandLs = new CommandLs("./src/main/resources/gaby18");
        String[] args = new String[4];
        args[0] = "ls";
        args[1] = "-l";
        args[2] = "-h";
        args[3] = "-r";
        commandLs.process(args);
        String result;
        PrintStream originalOut = System.out;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream capture = new PrintStream(os);
            System.setOut(capture);
            test();
            capture.flush();
            result = os.toString();
        } finally {
            System.setOut(originalOut);
        }
        System.out.println(result);
    }

    private static void test() {
        System.out.println("selected file path does not exist: .\\\\src\\\\main\\\\resources\\\\gaby18\"");
    }

    @org.junit.jupiter.api.Test
    public void test7() throws IOException{
        CommandLs commandLs = new CommandLs("./src/main/resources/dir1");
        String[] args = new String[5];
        args[0] = "ls";
        args[1] = "-l";
        args[2] = "-h";
        args[3] = "-r";
        args[4] = "-o";
        commandLs.process(args);
        String result = commandLs.getResult();
        assertEquals("file2.txt rwx 28/04/2019 17:32:20 0.0068359375 KB\n" +
                "file1.txt rwx 28/04/2019 17:32:02 0.0068359375 KB\n" +
                "Dashboard.pdf rwx 13/12/2018 21:05:12 92.2666 KB\n", result);
    }
}

