package command;
import javafx.util.Pair;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


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
            assert listDocs != null;
            Collections.addAll(infoOrdered, listDocs);
        } else if (file.isFile()) {
            infoOrdered.add(file);
        }
    }

    private static void getCommand_l(boolean h) {
        for (File document : infoOrdered) {
            String name = document.getName();
            String permission = (h) ? getPermission(document,true) : getPermission(document, false);
            String sizeFile = (h) ? getSizeHumanReadable(document.length()) : String.valueOf(document.length()).
                    concat(" ").concat("Bytes");
            String dateFile =  getDate(document);
            String line = name.concat(" ").concat(permission).concat(" ").
                    concat(dateFile).concat(" ").concat(sizeFile);
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


    private static void getCommand_o(ArrayList<String> list, File pathDirectoryOutput, boolean o) throws IOException{
        if (o) {
            BufferedWriter writer;
                writer = Files.newBufferedWriter(Paths.get(pathDirectoryOutput.toURI()));

                for (String line : list) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
                writer.close();
            }
        else
            {
            for (String info : list) System.out.println(info);
        }
    }

   private static String getPermission(File document, boolean h) {
       List<Pair<String, String>> list = new ArrayList<>();
       Pair l = new <String, String>Pair("0", "_");
       list.add(new Pair("1", "r"));
       list.add(new Pair<>("1", "w"));
       list.add(new Pair<>("1", "x"));

           String permission = (h)? (document.canRead() ? list.get(0).getValue() : (String) l.getValue()):
                   (document.canRead() ? list.get(0).getKey() : (String) l.getKey());
           permission = permission.concat((h)? (document.canWrite() ? list.get(1).getValue() :
                   (String) l.getValue()): (document.canWrite() ? list.get(1).getKey() : (String) l.getKey()));
           permission = permission.concat((h)? (document.canExecute() ? list.get(2).getValue() : (String) l.getValue()):
                   (document.canExecute() ? list.get(2).getKey() : (String) l.getKey()));
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
                getCommand_l(true);
                }
             else if (l) {
                getCommand_l(false);
            }
             else if (h) {
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

    public static void main(String[] args) throws CmdLineException, IOException {
        CommandLineArgument values = new CommandLineArgument(args);
        boolean l = values.longFormat;
        boolean h = values.humanReadable;
        boolean r = values.reverse;
        boolean o = values.out != null;
        String pd = values.pathFile;
        File pathDirectoryOutput = new File ("./src/test/resources/output/" + values.out);
        CommandLs commandLs = new CommandLs(pd);
            commandLs.process(l,h,r,o,pathDirectoryOutput);
        }
    }

