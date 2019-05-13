package command;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class CommandLs {

    public static String pathDirectoryOutput = "./src/main/resources/output/output.txt";

   public static String pathDirectory;
    private List<String> result;
    private ArrayList<File> infoOrdered;


    public CommandLs(String currentDirectory) {
        this.pathDirectory = currentDirectory;
        this.result = new ArrayList();
        this.infoOrdered = new ArrayList<>();
    }

    public void validateDirectory() {
        infoOrdered = new ArrayList<>();
        File file = new File(pathDirectory);
        File[] listDocs = file.listFiles();
        if (file.isDirectory()) {
            if (listDocs == null) throw new AssertionError();
            Collections.addAll(infoOrdered, listDocs);
        } else if (file.isFile()) {
            infoOrdered.add(file);
            Collections.sort(infoOrdered);
        }
    }

    public void getCommand_l() {
        for (File document : infoOrdered) {
            String name = document.getName();
            String permission = getPermission(document);
            String sizeFile = String.valueOf(document.length()).concat(" ").concat("Bytes");
            String dateFile = getDate(document);
            String line = name.concat(" ").concat(permission).concat(" ").concat(dateFile).concat(" ").concat(sizeFile);
            this.result.add(line);
        }
        printConsole();
    }

    public void getCommand_h() {
        for (File document : infoOrdered) {
            String name = document.getName();
            this.result.add(name);
        }
        printConsole();
    }

    public void getCommand_r() {
        for (File document : infoOrdered  ) {
            String name = document.getName();
            this.result.add(name);
        }
                Collections.reverse(result);
        printConsole();
            }



    public void getCommand_o() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(pathDirectoryOutput, "UTF-8");

            for (String line : this.result) {
                writer.println(line);
            }
        } catch (IOException ex) {
            System.out.println(":(");
        }
    }


    public void printConsole() {
        for (String line : this.result) {
            System.out.println(line);
        }
    }


    public String getResult() {
        StringBuilder s = new StringBuilder();
        for (String line : this.result) {
            s = s.append(line).append("\n");
        }
        return s.toString();
    }

    public String getPermission(File document) {
        String permission = (document.canRead()) ? "1" : "0";
        permission = permission.concat((document.canWrite()) ? "1" : "0");
        permission = permission.concat((document.canExecute()) ? "1" : "0");
        return permission;
    }

    public String getRWX(File document) {
        String permission = (document.canRead()) ? "r" : "_";
        permission = permission.concat((document.canWrite()) ? "w" : "_");
        permission = permission.concat((document.canExecute()) ? "x" : "_");
        return permission;
    }

    public String getSizeHumanReadable(long sizeDoc) {
        long KB = 1024L;
        long MB = 1024 * KB;
        long GB = 1024 * MB;
        if (sizeDoc > GB) {
            return new Float((double)sizeDoc / GB).toString().concat(" GB");
        }
        if (sizeDoc > MB) {
            return new Float((double) sizeDoc / MB).toString().concat(" MB");
        }
        return new Float((double)sizeDoc / KB).toString().concat(" KB");
    }

    public void getCommandlAndh(boolean l, boolean h, boolean r, boolean o) throws IOException{
        for (File document : infoOrdered) {
            String name = document.getName();
            if (l && h) {
                String permission = getRWX(document);
                String dateFile = getDate(document);
                String sizeFile = getSizeHumanReadable(document.length());
                String line = name.concat(" ").concat(permission).concat(" ").concat(dateFile).concat(" ").concat(sizeFile);
                this.result.add(line);
            }
        }
            if (r) {
                Collections.reverse(result);
                printConsole();
            }
            if (o) {
                getCommand_o();
            }
    }


    public String getDate(File file) {
        long time = file.lastModified();
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    public void process(String[] args) throws IOException{
        if (args.length > 0) {
            if (new File(pathDirectory).exists()) {
                validateDirectory();
                if (args[0].equals("ls")) {
                    if (args.length == 2) {
                        if (args[1].equals("-l")) getCommand_l();
                        else if (args[1].equals("-h")) getCommand_h();
                        else if (args[1].equals("-r")) getCommand_r();
                    } else if (args[1].equals("-l") && (args.length == 3 && args[2].equals("-h"))) {
                        getCommandlAndh(true, true, false, false);
                    } else if (args[1].equals("-l") && args.length == 4 && args[2].equals("-h") && args[3].equals("-r")) {
                        getCommandlAndh(true, true, true, false);

                    } else if (args[1].equals("-l") && args.length == 5 && args[2].equals("-h") && args[3].equals("-r") && args[4].equals("-o")) {
                        getCommandlAndh(true, true, true, true);

                    }
                    //write result in file
                    else if (args.length == 3 && args[2].equals("-o")) {
                        getCommand_o();
                    }
                } else {
                    System.out.print("Argument no valid");
                }
            } else {
                System.out.println("selected file path does not exist: ");
                System.out.println(new File(pathDirectory));
            }
        }
    }

    public static void main(String[] args) {
        CommandLs commandLs = new CommandLs(pathDirectory);
        try {
            commandLs.process(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

