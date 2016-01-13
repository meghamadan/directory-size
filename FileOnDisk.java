/**
 * FileOnDisk class works with DirectorySize program
 * A FileOnDisk object represents a file/ sub-directory on a disk drive according to its size and absolute path
 * 
 * @author Megha Madan
 * February 17, 2015
 *
 */
public class FileOnDisk implements Comparable <FileOnDisk> {

	//String absPath represents the absolute path and long size represents the bytes the file/sub directory has on the disk
	private String absPath;
	private long size;
	
	//constructor that takes in the absolute path as a string an the size of the file as a long
	FileOnDisk(String absPath, long size){
		this.absPath = absPath;
		this.size = size;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Override the toString method and print out the fileOnDisk object's size followed by bytes, KB, MB, or GB
	 * and print out the path name
	 */
	@Override
	public String toString(){
		
		if (size < 1024 ) //print bytes
			return String.format("%7.2f bytes    %-90s", 
					(float) size , absPath);
		else if (size/1024 < 1024 )//print kilobytes
			return String.format("%7.2f KB       %-90s", 
					(float) size / 1024.0 , absPath);
		else if (size/1024/1024 < 1024 )//print megabytes
			return String.format("%7.2f MB       %-90s", 
					(float) size / (1024.0 * 1024) , absPath);
		else //print gigabytes
			return String.format("%7.2f GB       %-90s", 
					(float) size / (1024.0 * 1024*1024) , absPath);
		
	}
	
	/**
	 * @return absolute path of current FileOnDisk object
	 */
	public String getAbsPath(){
		return absPath;
	}
	
	
	/**
	 * @return size of current FileOnDisk object
	 */
	public long getSize(){
		return size;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Override compareTo method to compare by size of the file first and resolve ties using 
	 * alphabetical order of the absolute pathname
	 */
	@Override
	public int compareTo(FileOnDisk x) {
		//first check if the sizes are equal to each other 
		if(x.size == size){
			//if they are equal compare according to alphabetical order of file path name
			if(x.absPath.compareTo(absPath) == 0)
				return 0;
			else if(x.absPath.compareTo(absPath) > 0)
				return 1;
			else
				return -1;
		}
		//if not equal sizes - continue comparison with size
		else if(x.size > size)
			return -1;
		else
			return 1;
	}
	
}
