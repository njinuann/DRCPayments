package RubyPayments;

import StarTimesPayment.GenerateReconFile;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToDbUpload
{

    static String billerfilePath = BRController.FilePath;
    static String ReconUploadTime = BRController.ReconUploadTime;
    public String Filename;
    public PreparedStatement pstm = null;
    public java.util.Date timestamp = new java.util.Date();
    FileInputStream input;

    public SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
    TXUtility util = new TXUtility();
    //BillerMain bmain = new BillerMain();
    GenerateReconFile recon = new GenerateReconFile();

    public void uploadBiller(String time)
    {
        //  log("201606201321 Starting upload >>>>> " + time.substring(8, 12));
        if (time.substring(8, 12).equals(BRController.ReconUploadTime))
        {
            log("Start generating Recon file.. its time and upload to the server  " + time.substring(8, 12));
            recon.generateFile();
        }
        File folder = new File(BRController.FilePath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles.length == 0)
        {
            if (BRController.ShowFilePathStatus.equalsIgnoreCase("Y"))
            {
                ExtPayMain.bRLogger.logEvent("No File Found in the path: " + folder.getAbsolutePath());
            }
        }
        else
        {
            if (util.backUpTable(time))
            {
                ExtPayMain.bRLogger.logEvent(listOfFiles.length + " File found! \n >>  ******************************************** <<");
                for (File file : listOfFiles)
                {

                    if (file.isFile())
                    {
                        for (int x = 1; x <= listOfFiles.length; x++)
                        {
                            for (int y = 1; y <= x; y++)
                            {
                                y++;
                            }
                            x++;
                            try
                            {
                                Filename = file.getName();
                                File billerFile = new File(billerfilePath.concat("\\").concat(Filename));

                                input = new FileInputStream(billerFile);
                                String fileName = billerFile.getName();

                                String fileExtn = GetFileExtension(fileName);
                                System.err.println("" + fileExtn);

                                ExtPayMain.bRLogger.logEvent("Processing File: " + Filename);
                                if (util.truncateTable(Filename))
                                {
                                    if (fileExtn.equalsIgnoreCase("xlsx"))
                                    {
                                        ExtPayMain.bRLogger.logEvent(fileExtn + " Document Uploading!");

                                        XSSFWorkbook wb = new XSSFWorkbook(input);
                                        XSSFSheet sheet = wb.getSheetAt(0);

                                        Row row;
                                        log("xlsx=" + wb.getSheetName(0));

                                        for (int i = 1; i <= sheet.getLastRowNum(); i++)
                                        {
                                            row = sheet.getRow(i);
                                            BFValue values = new BFValue();

                                            values.setBILLER_COL_ACCT(row.getCell(0).getStringCellValue());
                                            values.setBILLER_ID(row.getCell(1).getStringCellValue());

                                            values.setMEMBER_NO(row.getCell(2).getStringCellValue());
                                            values.setMEMBER_NAME(row.getCell(3).getStringCellValue());

                                            values.setMEMBER_MOBILE_NUMBER(row.getCell(4).getStringCellValue());
                                            values.setMEMBER_ID_NO(row.getCell(5).getStringCellValue());

                                            values.setCURRENCY(row.getCell(6).getStringCellValue());
                                            values.setGRADE_LEVEL(row.getCell(7).getStringCellValue());

                                            values.setSECTION(row.getCell(8).getStringCellValue());
                                            values.setCATEGORY(row.getCell(9).getStringCellValue());

                                            util.updateBillerTable(values);

                                            System.out.println("Import rows " + i);
                                        }
                                        ExtPayMain.bRLogger.logEvent("Successfully imported File " + Filename + "to Biller table: ");

                                    }
                                    else if (fileExtn.equalsIgnoreCase("xls"))
                                    {
                                        ExtPayMain.bRLogger.logEvent(fileExtn + " Document Uploading! - 97 workbook");

                                        POIFSFileSystem fs = new POIFSFileSystem(input);

                                        HSSFWorkbook wb2 = new HSSFWorkbook(fs);
                                        HSSFSheet sheet2 = wb2.getSheetAt(0);

                                        Row row;
                                        log("xls=" + wb2.getSheetName(0));

                                        for (int i = 1; i <= sheet2.getLastRowNum(); i++)
                                        {
                                            row = sheet2.getRow(i);
                                            BFValue values = new BFValue();

                                            values.setBILLER_COL_ACCT(row.getCell(0).getStringCellValue());
                                            values.setBILLER_ID(row.getCell(1).getStringCellValue());

                                            values.setMEMBER_NO(row.getCell(2).getStringCellValue());
                                            values.setMEMBER_NAME(row.getCell(3).getStringCellValue());

                                            values.setMEMBER_MOBILE_NUMBER(row.getCell(4).getStringCellValue());
                                            values.setMEMBER_ID_NO(row.getCell(5).getStringCellValue());

                                            values.setCURRENCY(row.getCell(6).getStringCellValue());
                                            values.setGRADE_LEVEL(row.getCell(7).getStringCellValue());

                                            values.setSECTION(row.getCell(8).getStringCellValue());
                                            values.setCATEGORY(row.getCell(9).getStringCellValue());

                                            util.updateBillerTable(values);
                                            System.out.println("Import rows " + i);

                                        }
                                        ExtPayMain.bRLogger.logEvent("Successfully imported File " + Filename + "to Biller table: ");
                                    }
                                    else
                                    {
                                        ExtPayMain.bRLogger.logEvent("File " + Filename + "" + fileExtn + " is invalid");
                                        billerFile.delete();
                                        return;
                                    }
                                    log(fileName);
                                }
                            }
                            catch (Exception e)
                            {
                                ExtPayMain.bRLogger.logError("Error ", e);
                            }
                        }
                    }
                }
                archivePrevFiles(billerfilePath);
            }
            //handle duplicates in the databse
            util.insertDup();
            if (util.deleteDuplicates())
            {
                ExtPayMain.bRLogger.logEvent("DUPLICATES CHECKED");
            }
        }
        if (BRController.ShowFilePathStatus.equalsIgnoreCase("Y"))
        {
            ExtPayMain.bRLogger.logEvent(">>  ======================================== <<");
        }
    }

    private static String GetFileExtension(String fname2)
    {

        String fileName = fname2;
        String fname = "";

        String ext;
        int mid = fileName.lastIndexOf(".");

        fname = fileName.substring(0, mid);
        ext = fileName.substring(mid + 1, fileName.length());

        return ext;
    }

    public static void log(String message)
    {

        System.out.println(message);
    }

    public void archivePrevFiles(String fromDir)
    {

        File outDir = new File(fromDir);
        ExtPayMain.bRLogger.logEvent("archiving the file " + fromDir);

        File archDir = new File(fromDir + "-Archive");
        if (!archDir.exists())
        {
            archDir.mkdirs();
            ExtPayMain.bRLogger.logEvent("Directory Not found " + fromDir);
        }

        File[] files = outDir.listFiles();
        for (File file : files)
        {
            log(file.getName() + "~" + Filename);
            if (!file.renameTo(new File(archDir, file.getName())))
            {
                System.out.println("archiving the file");
                file.delete();
            }
        }

    }
}
