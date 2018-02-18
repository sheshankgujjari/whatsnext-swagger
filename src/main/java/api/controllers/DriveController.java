package api.controllers;

import api.utilities.DriveHelper;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.TeamDrive;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/drive")
public class DriveController {
    @RequestMapping(method= RequestMethod.GET)
    String index(){
        return "drive";
    }

    @RequestMapping(value = "/getFiles", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<File> getFiles() throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.getFirstTenAndPrint(service);
    }

    @RequestMapping(value = "/createFile", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public File createFile() throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.createFile(service);
    }

    @RequestMapping(value = "/createFolder", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public File createFolder(@PathVariable(name = "folderName") String folderName) throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.createFolder(service, folderName);
    }

    @RequestMapping(value = "/searchFile", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FileList searchFile() throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.searchFile(service);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OutputStream downloadFile(@PathVariable(name = "fileId") String fileId) throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.downloadFiles(service, fileId);
    }

    @RequestMapping(value = "/createFileInFolder", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public File createFileInFolder(@PathVariable(name = "folderId") String folderId,
                                   @PathVariable(name = "fileName") String fileName) throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.createFileInFolder(service, folderId, fileName);
    }

    @RequestMapping(value = "/moveFilesFromOneFolderToAnother", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public File moveFilesFromOneFolderToAnother(@PathVariable(name = "folderId") String folderId,
                                   @PathVariable(name = "fileName") String fileName) throws IOException {
        Drive service = DriveHelper.getDriveService();
       return DriveHelper.moveFilesFromOneFolderToAnother(service, folderId, fileName);
    }

    @RequestMapping(value = "/listOfFilesInFolder", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FileList listOfFilesInFolder(@PathVariable(name = "folderName") String folderName) throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.listOfFilesInFolder(service, folderName);
    }

    @RequestMapping(value = "/createTeamDrive", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TeamDrive createTeamDrive(@PathVariable(name = "teamDriveName") String teamDriveName) throws IOException {
        Drive service = DriveHelper.getDriveService();
        return DriveHelper.createTeamDrives(service, teamDriveName);
    }
}
