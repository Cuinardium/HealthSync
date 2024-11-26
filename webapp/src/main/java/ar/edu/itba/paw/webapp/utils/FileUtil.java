package ar.edu.itba.paw.webapp.utils;

public class FileUtil {

    private FileUtil() {
        throw new RuntimeException();
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1).toLowerCase();
    }
}
