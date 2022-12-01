import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;

public class Main {

    private static final String server = "localhost";
    private static final int port = 21;
    private static final String USER = "andreipopa";
    private static final String PASSWORD = "alumno";


    public static void main(String[] args) throws IOException {

        FTPClient ftp = new FTPClient();


        LocalHostConnection(ftp);
        CheckConnectionSecure(ftp);
        SwitchingToPassiveMode(ftp);
        PrintWorkingDirectory(ftp);
       // RecursiveDeletion(ftp);
        PrintFilesRemoteDirectory(ftp);
        CreateRemoteDirectory(ftp);




    }


    //2. Conecta a localhost.
    private static void LocalHostConnection(FTPClient ftp) throws IOException {
        ftp.connect(server,port);
        System.out.println("Connection established");
    }


    //3. Comprueba si la conexión es correcta

    private static void CheckConnectionSecure(FTPClient ftp ) throws IOException {
        System.out.println("Checking the connection is correct...");

        int reply = ftp.getReplyCode();


        if(!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.err.println("Error" + reply);
            System.exit(1);
        }
    }

    //4. Cambia a modo pasivo y haz login con el usuario y contraseña que hayas configurado
    private static void SwitchingToPassiveMode(FTPClient ftp) throws IOException {
        System.out.println("Switching to Passive mode...");
        ftp.enterLocalPassiveMode();
        ftp.login(USER,PASSWORD);

        System.out.println("Connection approved");
    }

    //5. Imprime el nombre del directorio remoto actual

    private static void PrintWorkingDirectory(FTPClient ftp) throws IOException {
        System.out.println("The name of actually directory is: "+ftp.printWorkingDirectory());
    }


    //6. Borra todos los directorios que descienden del directorio remoto actual

    private static void RecursiveDeletion(FTPClient ftp) throws IOException {


        FTPFile[] files = ftp.listFiles();

        for (FTPFile file: files) {
            System.out.println(file.getName());
            if (file.isDirectory()) {
                ftp.changeWorkingDirectory(ftp.printWorkingDirectory() + "/" + file.getName());
                RecursiveDeletion(ftp);
                ftp.changeToParentDirectory();
                ftp.removeDirectory(file.getName());
            } else {
                ftp.deleteFile(file.getName());
            }

        }

        System.out.println("Deleting files...");

    }

    // 7. Imprime por pantalla la lista de todos los ficheros en el directorio remoto actual. Si no hay, imprime No files

    private static void PrintFilesRemoteDirectory(FTPClient ftp) throws IOException {

        FTPFile[] files = ftp.listFiles();

        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }

            System.out.println(details);
        }



    }


    private static void CreateRemoteDirectory(FTPClient ftp) throws IOException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        LocalDateTime localDateTime = LocalDateTime.now();

        String ldtString = formatter.format(localDateTime);


        ftp.makeDirectory(ldtString);






    }

}