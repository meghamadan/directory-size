
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This program provides a tool that given a name of a directory,
 * explores all its sub-directories and files and does two things: 
 *  - computes the total size of all the files and sub-directories,
 *  - prints a list of n largest files (their sizes and absolute paths). 
 *  
 * @author Joanna Klukowska & Megha Madan
 *
 */
public class DirectorySize {
	
	/**list of files found in the directory structure */
	static List <FileOnDisk> listOfFiles ; 
	/**list of visited directories (kept to avoid 
	 * circular links and infinite recursion) */
	static List <String> listOfVisitedDirs;
	
	/** 
	 * This method expects one or two arguments. 
	 * @param args Array of arguments passed to the program. The first one 
	 * is the name of the directory to be explored. The second (optional) is the
	 * max number of largest files to be printed to the screen.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		if(args.length < 1){
			System.err.printf("ERROR: The directory name is omitted");
			System.exit(1);
		}
		
		// use directory name entered from the command line
		// verify if the directory is valid, terminate if not
		String directory = args[0];
		File dir = new File(directory);
		   if(! dir.isDirectory()){
			   System.err.printf("ERROR: The directory name is not valid");
			   System.exit(1);
		   }
		
		
		//create an empty list of files
		listOfFiles = new LinkedList<FileOnDisk> ();
		//create an empty list of directories
		listOfVisitedDirs = new ArrayList<String> ();
		
		// Display the total size of the directory/file
		long size = getSize( dir );
		
		if (size < 1024 ) //print bytes
			System.out.printf("Total space used: %7.2f bytes\n", 
					(float) size  );
		else if (size/1024 < 1024 )//print kilobytes
			System.out.printf("Total space used: %7.2f KB\n", 
					(float) size / 1024.0 );
		else if (size/1024/1024 < 1024 )//print megabytes
			System.out.printf("Total space used: %7.2f MB\n", 
					(float) size / (1024.0 * 1024));
		else //print gigabytes
			System.out.printf("Total space used: %7.2f GB\n", 
					(float) size / (1024.0 * 1024*1024));
		
		
		// Display the largest files in the directory
		int numOfFiles = 20; //default value
		try {
			if (args.length == 2 )  {
				numOfFiles = Integer.parseInt(args[1]);
			}
		}
		catch (NumberFormatException ex) {
			System.err.printf("ERROR: Invalid number of files provided." +
					"The second argument should be an integer. \n");
			System.exit(1);
		}
		System.out.printf("Largest %d files: \n", numOfFiles );
		
		Collections.sort(listOfFiles);
		
		for (int i = 0; i < listOfFiles.size() && i < numOfFiles; i++)
			//print from the back so that the largest files are printed
		    System.out.println(listOfFiles.get(listOfFiles.size() - i - 1));
	}


	/**
	 * Recursively determines the size of a directory or file represented 
	 * by the abstract parameter object file.   
	 * This method populates the listOfFiles with all the files contained in the
	 * explored directory. 
	 * This method populates the listOfVisitedDirs with canonical path names of 
	 * all the visited directories. 
	 * @param file directory/file name whose size should be determined
	 * @return number of bytes used for storage on disk
	 * @throws IOException 
	 */
	
	
	public static long getSize (File potentialDir) throws IOException   {
		long size = 0; // Store the total size of all files
		
		//First check if the input is a directory
		if(potentialDir.isDirectory()){
			//first we add the directory to a list to mark it as searched/visited
			//this is to avoid searching a directory that was linked in the same directory which would lead to infinite recursion
			if(!listOfVisitedDirs.contains(potentialDir.getCanonicalPath())){
				//Create FileonDisk object to add to visited directories use canonical path to avoid infinite recursion
				listOfVisitedDirs.add(potentialDir.getCanonicalPath());
				//now add the size of the directory to total size (not necessary on Windows systems because
				//directories have a size 0, but this is here to work on Unix based systems)
				size = size + potentialDir.length();
				
				//if statement to make sure the files in each directory (when we use the listFiles method) do not lead to a null pointer exception
				if(potentialDir.listFiles() != null){
					File [] tmpList = potentialDir.listFiles();
					//for loop to recursively add the size of each file or sub-directory contained in the current directory we are checking
					for(int i = 0; i < tmpList.length; i++){
						//each recursive call added to total size
						size = size + getSize(tmpList[i]);
					}
				}
			}
		}
		//if it is not a directory, check if it is a file
		else if(potentialDir.isFile()){
			//create a FileOnDisk object to add to the list of files which will then be printed out
			FileOnDisk x = new FileOnDisk(potentialDir.getCanonicalPath(), potentialDir.length());
			listOfFiles.add(x);
			//also add the size of the file to the total size
			size = size + potentialDir.length();
		}
		return size;
	}
	
}