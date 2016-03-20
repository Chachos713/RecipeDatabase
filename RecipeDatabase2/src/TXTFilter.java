import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A simple class that is used to filter files based on a .txt extension. Used
 * when reading and writing a database.
 * 
 * @author Kyle Diller
 *
 */
public class TXTFilter extends FileFilter {
	/**
	 * The array of acceptable extensions
	 */
	private final String[] okFileExt = new String[] { ".txt" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		for (String ext : okFileExt) {
			if (file.getName().toLowerCase().endsWith(ext)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return ".txt";
	}
}
