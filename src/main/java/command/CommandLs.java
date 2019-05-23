package command;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class CommandLs {
    private static String pathDirectory;
    private static ArrayList result;
    private static ArrayList<File> infoOrdered;


    public CommandLs(String currentDirectory) {
        pathDirectory = currentDirectory;
        result = new ArrayList<>();
        infoOrdered = new ArrayList<>();
    }

    private void validateDirectory() {
        infoOrdered = new ArrayList<>();
        File file = new File(pathDirectory);
        File[] listDocs = file.listFiles();
        if (file.isDirectory()) {
            if (listDocs == null) throw new AssertionError();
            Collections.addAll(infoOrdered, listDocs);
        } else if (file.isFile()) {
            infoOrdered.add(file);
            Collections.addAll(infoOrdered);
        }
    }

    private static void getCommand_l() {
        for (File document : infoOrdered) {
            String name = document.getName();
            String permission = getPermission(document);
            String sizeFile = String.valueOf(document.length()).concat(" ").concat("Bytes");
            String dateFile = getDate(document);
            String line = name.concat(" ").concat(permission).concat(" ").concat(dateFile).concat(" ").concat(sizeFile);
            result.add(line);
        }
    }

    private static void getCommand_h() {
        for (File document : infoOrdered) {
            String name = document.getName();
            result.add(name);
        }
    }

    private static void getCommand_r() {
        ArrayList<String> r = new ArrayList<>();
        if (result.isEmpty()) {
            for (File document : infoOrdered) {
                String name = document.getName();
                r.add(name);
            }
            Collections.reverse(r);
        } else {
            Collections.reverse(result);

        }
    }


    private static void getCommand_o(ArrayList<String> list, File pathDirectoryOutput, boolean o) {
        if (o) {
            BufferedWriter writer;
            try {
                writer = Files.newBufferedWriter(Paths.get(pathDirectoryOutput.toURI()));

                for (String line : list) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                System.out.println(":(");
            }
        } else {
            for (String info : list) System.out.println(info);
        }
    }

    private static String getPermission(File document) {
        String permission = (document.canRead()) ? "1" : "0";
        permission = permission.concat((document.canWrite()) ? "1" : "0");
        permission = permission.concat((document.canExecute()) ? "1" : "0");
        return permission;
    }

    private static String getRWX(File document) {
        String permission = (document.canRead()) ? "r" : "_";
        permission = permission.concat((document.canWrite()) ? "w" : "_");
        permission = permission.concat((document.canExecute()) ? "x" : "_");
        return permission;
    }

    private static String getSizeHumanReadable(long sizeDoc) {
        long KB = 1024L;
        long MB = 1024 * KB;
        long GB = 1024 * MB;
        if (sizeDoc > GB) {
            return new Float((double) sizeDoc / GB).toString().concat(" GB");
        }
        if (sizeDoc > MB) {
            return new Float((double) sizeDoc / MB).toString().concat(" MB");
        }
        return new Float((double) sizeDoc / KB).toString().concat(" KB");
    }

    private static String getDate(File file) {
        long time = file.lastModified();
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    private static List<String> info(boolean l, boolean h, boolean r) {
            if (l && h) {
                for (File document : infoOrdered) {
                    String name = document.getName();
                    String permission = getRWX(document);
                    String dateFile = getDate(document);
                    String sizeFile = getSizeHumanReadable(document.length());
                    String line = name.concat(" ").concat(permission).concat(" ").concat(dateFile).concat(" ").concat(sizeFile);
                    result.add(line);
                }
            } else if (l) {
                getCommand_l();
            } else if (h) {
                getCommand_h();
            }

            if (r) {
                getCommand_r();
            }
        return result;
    }

    public void process (boolean l, boolean h, boolean r, boolean o, File pathDirectoryOutput) throws IOException {
        if (new File(pathDirectory).exists()) {
            validateDirectory();
            List<String> list = info(l,h,r);
            getCommand_o((ArrayList<String>) list, pathDirectoryOutput,o);
        } else {
            System.out.println("selected file path does not exist: ");
            System.out.println(new File(pathDirectory));
        }

    }

    public static void main(String[] args) throws IOException, CmdLineException {
        CommandLineArgument values = new CommandLineArgument(args);
        boolean l = values.longFormat;
        boolean h = values.humanReadable;
        boolean r = values.reverse;
        boolean o = values.out != null;
        String pd = values.pathFile;
        File pathDirectoryOutput = new File ("./src/test/resources/output/" + values.out);

        CommandLs commandLs = new CommandLs(pd);
        try {
            commandLs.process(l,h,r,o,pathDirectoryOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
