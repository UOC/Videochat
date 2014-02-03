package org.uoc.red5.videoconference.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileUtils {

	/**
	 * Create a directory with subdirectories. Return true if create.
	 * Sample strDirectory: dir1/dir2/dir3
	 * @param strDirectoy
	 * @return
	 */
	public static boolean makeDirectories(String strDirectoy) {

		boolean success = false;
		try{
			// Create multiple directories
		    success = (new File(strDirectoy)).mkdirs();
		    if (success) {
		      success = true;
		    }

	    } catch (Exception e) {//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	    
	    return success;
	}

	/**
	 * Create a directory. Return true if create.
	 * Sample strDirectory: dir1
	 * @param strDirectoy
	 * @return
	 */
	public static boolean makeDirectory(String strDirectoy) {

		boolean success = false;
		try{
			// Create one directory
		    success = (new File(strDirectoy)).mkdir();
		    if (success) {
		      success = true;
		    }

	    } catch (Exception e) {//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	    
	    return success;
	}
	
	/**
	 * Return if the directory exist.
	 * @param strDirectoy
	 * @return
	 */
	public static boolean existDirectory(String strDirectoy) {

		boolean success = false;
		try{
			// Create one directory
		    success = (new File(strDirectoy)).exists();
		    if (success) {
		      success = true;
		    }

	    } catch (Exception e) {//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	    
	    return success;
	}	
	
	/**
	 * Return the list of files from path
	 * @param path
	 * @return
	 */
	public static String[] getFiles(String path) {
	
		File file = new File(path);
		String[] directories = file.list();

		return directories;
	}
	
	public static boolean deleteDirectoryFiles(String path) {

		File file = new File(path);
		String[] files = getFiles(path);
		for (int i=0;i<files.length; i++) {
			String file_ = files[i];
			file = new File(path + File.separator + file_);
			if (file.isFile())
				file.delete();
		}
		return true;
	}

	public static void copyDirectoryFiles(File srcDir, File dstDir) {
		
        try{
            if (srcDir.isDirectory()) {
                if (!dstDir.exists()) {
                    dstDir.mkdir();
                }

                String[] children = srcDir.list();
                for (int i=0; i<children.length; i++) {
                	copyDirectoryFiles(new File(srcDir, children[i]),
                        new File(dstDir, children[i]));
                }
            } else {
                copyFile(srcDir, dstDir);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

	/**
	 * Copia un solo archivo
	 * @param s
	 * @param t
	 * @throws IOException
	 */
	public static void copyFile(File s, File t)
	{
	    try{
	          FileChannel in = (new FileInputStream(s)).getChannel();
	          FileChannel out = (new FileOutputStream(t)).getChannel();
	          in.transferTo(0, s.length(), out);
	          in.close();
	          out.close();
	    }
	    catch(Exception e)
	    {
	        System.out.println(e);
	    }
	}
	
	public static void renameDirectory(String fromDir, String toDir) {

	    File from = new File(fromDir);

	    if (!from.exists() || !from.isDirectory()) {

	      System.out.println("Directory does not exist: " + fromDir);
	      return;
	    }

	    File to = new File(toDir);
	    from.renameTo(to);
	}
	
}
