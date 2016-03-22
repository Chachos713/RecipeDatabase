import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The main entry porint and GUI for the program.
 * 
 * @author Kyle Diller
 *
 */
public class RDBGUI implements ListSelectionListener, ActionListener,
		WindowListener {

	public static final boolean DEBUG = true;

	/**
	 * Runs the program.
	 * 
	 * @param args
	 *            the arguements passed into the program.
	 */
	public static void main(String[] args) {
		new RDBGUI();
	}

	private JList<String> listNames;
	private DefaultListModel<String> names;
	private JTextArea recipe;
	private JFrame frame;
	private JFileChooser save, open;

	private File prev;
	private Database data;
	private boolean change;

	/**
	 * Creates and displays the form for the user.
	 */
	public RDBGUI() {
		data = new Database();

		frame = new JFrame("Recipe Database");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);

		names = new DefaultListModel<String>();
		listNames = new JList<String>(names);
		listNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listNames.setSelectedIndex(-1);
		listNames.addListSelectionListener(this);

		recipe = new JTextArea(30, 30);
		recipe.setEditable(false);
		recipe.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

		JScrollPane list = new JScrollPane(listNames);
		list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		list.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		list.setPreferredSize(new Dimension(300, 700));

		JScrollPane rec = new JScrollPane(recipe);
		rec.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rec.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		rec.setPreferredSize(new Dimension(800, 700));

		frame.setJMenuBar(createMenuBar());
		frame.getContentPane().add(BorderLayout.WEST, list);
		frame.getContentPane().add(BorderLayout.CENTER, rec);

		frame.pack();
		frame.setVisible(true);

		save = new JFileChooser();
		save.setAcceptAllFileFilterUsed(false);
		save.setFileFilter(new TXTFilter());
		save.setFileFilter(new PDFFilter());

		open = new JFileChooser();
		open.setAcceptAllFileFilterUsed(false);
		open.setFileFilter(new TXTFilter());

		displayRecipes();
	}

	/**
	 * Creates the menu bar for the form.
	 * 
	 * @return the menu bar that is to be displayed.
	 */
	public JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic('f');
		JMenu data = new JMenu("Database");
		data.setMnemonic('D');
		JMenu reci = new JMenu("Recipe");
		reci.setMnemonic('R');

		JMenuItem fOpen = new JMenuItem("Open", 'O');
		fOpen.setActionCommand("fo");
		fOpen.addActionListener(this);

		JMenu fSave = new JMenu("Save");
		fSave.setActionCommand("fs");
		fSave.setMnemonic('s');

		JMenuItem sData = new JMenuItem("Database", 'D');
		sData.setActionCommand("fsd");
		sData.addActionListener(this);

		JMenuItem sReci = new JMenuItem("Recipe", 'R');
		sReci.setActionCommand("fsr");
		sReci.addActionListener(this);

		JMenuItem fAs = new JMenuItem("Save As", 'A');
		fAs.setActionCommand("fa");
		fAs.addActionListener(this);

		JMenuItem dNew = new JMenuItem("New", 'N');
		dNew.setActionCommand("dn");
		dNew.addActionListener(this);

		JMenuItem dMerge = new JMenuItem("Merge", 'm');
		dMerge.setActionCommand("dm");
		dMerge.addActionListener(this);

		JMenuItem dSearch = new JMenuItem("Search", 's');
		dSearch.setActionCommand("ds");
		dSearch.addActionListener(this);

		JMenuItem rNew = new JMenuItem("New", 'n');
		rNew.setActionCommand("rn");
		rNew.addActionListener(this);

		JMenuItem rEdit = new JMenuItem("Edit", 'e');
		rEdit.setActionCommand("re");
		rEdit.addActionListener(this);

		fSave.add(sData);
		fSave.add(sReci);

		file.add(fOpen);
		file.add(fSave);
		file.add(fAs);

		data.add(dNew);
		data.add(dMerge);
		data.add(dSearch);

		reci.add(rNew);
		reci.add(rEdit);

		bar.add(file);
		bar.add(data);
		bar.add(reci);

		return bar;
	}

	/**
	 * Displays the names of the recipes in the list box.
	 */
	public void displayRecipes() {
		ArrayList<String> name = data.getNames();
		names.clear();

		for (int i = 0; i < name.size(); i++) {
			names.add(i, name.get(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 * 
	 * Displays a selected recipe to the form
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		String r = data.displayRecipe(listNames.getSelectedValue());
		recipe.setText(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * Handles the menu item clicks
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command.charAt(0)) {
		case 'f':
			file(command);
			break;
		case 'd':
			data(command);
			break;
		case 'r':
			reci(command);
			break;
		default:
			System.err.println("Unknown Action");
			return;
		}
	}

	/**
	 * Determines what happens when a menu item in the recipe tab is selected.
	 * 
	 * @param command
	 *            the command that determines what to do.
	 */
	private void reci(String command) {
		Recipe r = null;

		switch (command.charAt(1)) {
		case 'e':
			r = data.popRecipe(listNames.getSelectedValue());

			if (r == null) {
				System.err.println("Unknown Recipe");
				JOptionPane.showMessageDialog(frame, "NO SELECTED RECIPE",
						"ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}

			break;
		case 'n':
			break;
		default:
			System.err.println("Unknown Action");
			return;
		}

		r = RecipeEditor.edit(r);

		if (r == null)
			return;

		data.addRecipe(r);
		data.sort();
		displayRecipes();
		listNames.setSelectedValue(r.getName(), true);
		change = true;
	}

	/**
	 * Determines what happens when a menu item in the database tab is selected.
	 * 
	 * @param command
	 *            the command that determines what to do.
	 */
	private void data(String command) {
		switch (command.charAt(1)) {
		case 'n':
			if (change) {
				int choice = JOptionPane.showConfirmDialog(frame,
						"Do you wish to save?", "Edit",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (choice == JOptionPane.CANCEL_OPTION)
					return;
				if (choice == JOptionPane.YES_OPTION)
					save("fsd");
			}

			data = new Database();
			change = false;
			break;
		case 'm':
			int choice = open.showOpenDialog(frame);

			if (choice == JFileChooser.CANCEL_OPTION)
				return;

			File o = open.getSelectedFile();
			Database other = RecipeOperations.openDatabase(o);
			data = RecipeOperations.mergeDatabase(data, other);
			data.sort();
			displayRecipes();
			prev = o;

			change = true;
			break;
		case 's':
			searchDatabase();
			return;
		default:
			System.err.println("Unknown Action");
			return;
		}

		recipe.setText("");
		displayRecipes();
	}

	/**
	 * Determines what happens when a menu item in the file tab is selected.
	 * 
	 * @param command
	 *            the command stringthat determines the action.
	 */
	private void file(String command) {
		switch (command.charAt(1)) {
		case 's':
			save(command);
			break;
		case 'a':
			saveAs();
			break;
		case 'o':
			if (change) {
				int choice = JOptionPane.showConfirmDialog(frame,
						"Do you wish to save?", "Edit",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (choice == JOptionPane.CANCEL_OPTION)
					return;
				if (choice == JOptionPane.YES_OPTION)
					save("fsd");
			}

			change = false;

			int choice = open.showOpenDialog(frame);

			if (choice == JFileChooser.CANCEL_OPTION)
				return;

			File o = open.getSelectedFile();
			data = RecipeOperations.openDatabase(o);
			displayRecipes();
			prev = o;
			break;
		default:
			System.err.println("Unknown Action");
			return;
		}
	}

	/**
	 * Checks that the file name ends with the right extension.
	 * 
	 * @param f
	 *            the file to check.
	 * @param string
	 *            what the extension should be.
	 * @return the new file with the correct extension.
	 */
	private File checkEnd(File f, String string) {
		if (!f.getName().endsWith(string)) {
			return new File(f.toString() + string);
		}

		return f;
	}

	/**
	 * Saves the database into a pdf or text format.
	 */
	private void saveAs() {
		int sChoice = save.showSaveDialog(frame);

		if (sChoice == JFileChooser.CANCEL_OPTION)
			return;

		File f = save.getSelectedFile();
		if (save.getFileFilter() instanceof TXTFilter) {
			f = checkEnd(f, ".txt");

			prev = f;
			data.saveDataTXT(prev);
			change = false;
		} else {
			f = checkEnd(f, ".rtf");
			data.saveDataPDF(f);
		}
	}

	/**
	 * Saves a database or recipe based on the command string.
	 * 
	 * @param command
	 *            the string that is passed to determine what and how to save.
	 */
	private void save(String command) {
		switch (command.charAt(2)) {
		case 'r':
			int sChoice = save.showSaveDialog(frame);

			if (sChoice == JFileChooser.CANCEL_OPTION)
				return;

			File f = save.getSelectedFile();
			if (save.getFileFilter() instanceof TXTFilter) {
				f = checkEnd(f, ".txt");

				data.saveRecipeTXT(f, listNames.getSelectedValue());
				change = false;
			} else {
				f = checkEnd(f, ".rtf");
				data.saveRecipePDF(f, listNames.getSelectedValue());
			}
			break;
		case 'd':
			if (prev == null) {
				System.err.println("No Default File Location");
				JOptionPane.showMessageDialog(frame,
						"NO PREVIOUS FILE LOCATION", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			data.saveDataTXT(prev);
			change = false;
			break;
		default:
			System.err.println("Unknown Action");
			return;
		}
	}

	/**
	 * Searches the database for a specific recipe.
	 */
	private void searchDatabase() {
		String name = RecipeOperations.search(data);

		if (name == null)
			return;

		Recipe r = data.popRecipe(name);

		data.addRecipe(r);
		data.sort();
		displayRecipes();
		listNames.setSelectedValue(r.getName(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 * 
	 * Asks if the user wants to save changes.
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		if (change) {
			int choice = JOptionPane.showConfirmDialog(frame,
					"Do you wish to save?", "Edit",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (choice == JOptionPane.CANCEL_OPTION)
				return;
			if (choice == JOptionPane.YES_OPTION)
				if (prev != null)
					save("fsd");
				else
					saveAs();
		}

		frame.setVisible(false);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}
