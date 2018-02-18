package api.utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.TeamDrive;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class DriveHelper {

    /** controller.RegisterApplication name. */
    private static final String APPLICATION_NAME = "Google Drive API Client";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                DriveHelper.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
//        System.out.println(
//                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<File> getFirstTenAndPrint(Drive service) throws IOException {
        FileList result = service.files().list()
                //.setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
            return null;
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
            return files;
        }
    }

    public static File createFile(Drive service) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName("My Report");
        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");

        java.io.File filePath = new java.io.File("files/report.csv");
        FileContent mediaContent = new FileContent("text/csv", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file;
    }

    public static FileList searchFile(Drive service) throws IOException {
        String pageToken = null;
        FileList result = null;
        do {
             result = service.files().list()
                    .setQ("mimeType='image/jpeg'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                System.out.printf("Found file: %s (%s)\n",
                        file.getName(), file.getId());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return result;
    }

    public static OutputStream downloadFiles(Drive service, String fileId) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(fileId)
                .executeMediaAndDownloadTo(outputStream);
        return outputStream;
    }

    public static void downloadFilesByType(Drive service, String fileId) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        service.files().export(fileId, "application/pdf")
                .executeMediaAndDownloadTo(outputStream);

    }

    public static File createFolder(Drive service, String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = service.files().create(fileMetadata)
                .setFields("id")
                .execute();
        System.out.println("Folder ID: " + file.getId());
        return file;
    }

    public static File createFileInFolder(Drive service, String folderId, String fileName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file;
    }

    public static File moveFilesFromOneFolderToAnother(Drive service, String fileId, String folderId) throws IOException {
//        String fileId = "1sTWaJ_j7PkjzaBWtNc3IzovK5hQf21FbOw9yLeeLPNQ";
//        String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
// Retrieve the existing parents to remove
        File file = service.files().get(fileId)
                .setFields("parents")
                .execute();
        StringBuilder previousParents = new StringBuilder();
        for (String parent : file.getParents()) {
            previousParents.append(parent);
            previousParents.append(',');
        }
// Move the file to the new folder
        file = service.files().update(fileId, null)
                .setAddParents(folderId)
                .setRemoveParents(previousParents.toString())
                .setFields("id, parents")
                .execute();
        return file;
    }

    public static void retreiveDataFromApplicationDataFolder(Drive service) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName("config.json");
        fileMetadata.setParents(Collections.singletonList("appDataFolder"));
        java.io.File filePath = new java.io.File("files/config.json");
        FileContent mediaContent = new FileContent("application/json", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());

    }

    public static FileList listOfFilesInFolder(Drive service, String folderName) throws IOException {
        FileList files = service.files().list()
                //.setSpaces("appDataFolder")
                .setSpaces(folderName)
                .setFields("nextPageToken, files(id, name)")
                .setPageSize(10)
                .execute();
        for (File file : files.getFiles()) {
            System.out.printf("Found file: %s (%s)\n",
                    file.getName(), file.getId());
        }
        return files;
    }

    public static TeamDrive createTeamDrives(Drive service, String teamDriveName) throws IOException {
        TeamDrive teamDriveMetadata = new TeamDrive();
        //teamDriveMetadata.setName("Project Resources");
        teamDriveMetadata.setName(teamDriveName);
        String requestId = UUID.randomUUID().toString();
        TeamDrive teamDrive = service.teamdrives().create(requestId,
                teamDriveMetadata)
                .execute();
        System.out.println("Team Drive ID: " + teamDrive.getId());
        return teamDrive;
    }

}
