import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A simple class that extends file filter to save to a pdf file (.rtf for now)
 * 
 * @author Kyle Diller
 *
 */
public class PDFFilter extends FileFilter {
	/**
	 * The array of acceptable file extensions
	 */
	private final String[] okFileExt = new String[] { ".rtf" };

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return ".rtf";
	}
}
