package zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;


public class ZipCompression {
    public static void main(String[] args) throws ZipException {
        String pdsPath = "D:/pdf";
        String zipPath = "D:/zip";
        String resultPath = "D:/result";
        ZipFile zipFile = new ZipFile(pdsPath);
        zipFile.extractAll(resultPath);
        System.out.println("done");
    }
}
