import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

/**
 * A class with static methods that does a lot of the main operations between
 * file streams, as well as between multiple databases.
 * 
 * @author Kyle Diller
 *
 */
public class RecipeOperations {
	/**
	 * Reads in the next recipe in the file. Format of a recipe: BEGINRECIPE
	 * NAME<SOME_VALUE> INGREDIENT<SOME_VALUE> STEP<SOME_VALUE> ENDRECIPE
	 * 
	 * @param br
	 *            the buffered stream to read the recipe from.
	 * @return the next recipe from the buffered read, br.
	 * @throws Exception
	 *             thrown if there is a problem in reading the file.
	 */
	public static Recipe getNextRecipe(BufferedReader br) throws Exception {
		Recipe r = new Recipe();

		String line = br.readLine();

		if (line.equals("BEGINRECIPE")) {
			line = br.readLine();
			if (line.startsWith("NAME")) {
				r.setName(line.substring(4, line.length()));

				while (!((line = br.readLine()).equals("ENDRECIPE"))) {
					if (line.startsWith("INGR")) {
						r.addIngredient(line.substring(4, line.length()));
					} else if (line.startsWith("STEP")) {
						r.addStep(line.substring(4, line.length()));
					}
				}
				return r;
			}
		}

		return null;
	}

	/**
	 * Merges two databases together. This is done by doing a simple merging
	 * algorithm, but when 2 recipes have the same name, the user determines
	 * which to keep.
	 * 
	 * @param data1
	 *            the first database to merge.
	 * @param data2
	 *            the second database to merge.
	 * @return a new database that is the combination of the 2 databases passed
	 *         in.
	 */
	public static Database mergeDatabase(Database data1, Database data2) {
		Database merge = new Database();

		int i = 0, j = 0;
		int compare, keep;

		/*
		 * Goes through the 2 databases and compares the names of them. If they
		 * are the same, the user selects which one to keep. Goes till one
		 * database is read completely through.
		 */
		while (i < data1.getNumRecipes() && j < data2.getNumRecipes()) {
			compare = data1.getRecipe(i).compareTo(data2.getRecipe(j));
			if (compare < 0) {
				merge.addRecipe(data1.getRecipe(i));
				i++;
			} else if (compare > 0) {
				merge.addRecipe(data2.getRecipe(j));
				j++;
			} else {
				keep = JOptionPane.showConfirmDialog(null,
						"Keep Old Recipe(Yes) or New Recipe(No):\n"
								+ data1.getRecipe(i).getName(), "Duplicates",
						JOptionPane.YES_NO_OPTION);

				if (keep == JOptionPane.YES_OPTION) {
					merge.addRecipe(data1.getRecipe(i));
				} else {
					merge.addRecipe(data2.getRecipe(j));
				}

				i++;
				j++;
			}
		}

		/*
		 * Finishes reading the first database if the second is read completely
		 * and the first is not.
		 */
		for (; i < data1.getNumRecipes(); i++) {
			merge.addRecipe(data1.getRecipe(i));
		}

		/*
		 * Finished reading the second database if the first is read completely
		 * and the second is not.
		 */
		for (; j < data2.getNumRecipes(); j++) {
			merge.addRecipe(data1.getRecipe(j));
		}

		merge.sort();

		return merge;
	}

	/**
	 * Reads a database from a file. Calls getNextRecipe to read each individual
	 * recipe from the file.
	 * 
	 * @param f
	 *            the file to read.
	 * @return a database from the file that is read.
	 */
	public static Database openDatabase(File f) {
		Database d = null;
		try {
			d = new Database();

			FileInputStream fileIn = new FileInputStream(f);
			DataInputStream in = new DataInputStream(fileIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			Recipe r = new Recipe();

			while (r != null) {
				r = getNextRecipe(br);
				d.addRecipe(r);
			}
		} catch (Exception e) {
		}

		d.sort();
		return d;
	}

	/**
	 * Asks the user to search through the database based on certain criteria
	 * that the user selects.
	 * 
	 * @param data
	 *            the database to search through.
	 * @return the name of the recipe the user selects from the filtered list.
	 */
	public static String search(Database data) {
		/*
		 * Creates and displays the form for the user to filter the database by.
		 */
		JLabel queryLabel = new JLabel("What are you looking for?");
		JTextField queryField = new JTextField(20);
		JCheckBox cbName = new JCheckBox("Names");
		JCheckBox cbIngr = new JCheckBox("Ingredients");
		JCheckBox cbStep = new JCheckBox("Steps");
		Object[] params = { queryLabel, queryField, cbName, cbIngr, cbStep };

		int choice = JOptionPane.showConfirmDialog(null, params,
				"Search Parameters", JOptionPane.OK_CANCEL_OPTION);

		/*
		 * Does error checking
		 */
		if (choice == JOptionPane.CANCEL_OPTION)
			return null;

		if (queryField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "NO SEARCH FIELD", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!cbName.isSelected() && !cbIngr.isSelected()
				&& !cbStep.isSelected()) {
			JOptionPane.showMessageDialog(null, "NO SEARCH AREA", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		/*
		 * Displays the filtered list for the user to select from.
		 */
		ArrayList<String> names = data.search(queryField.getText(),
				cbName.isSelected(), cbIngr.isSelected(), cbStep.isSelected());

		if (names.isEmpty()) {
			JOptionPane.showMessageDialog(null, "NO RECIPES FOUND", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		JList<Object> found = new JList<Object>(names.toArray());
		found.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		found.setVisibleRowCount(5);

		JScrollPane scroller = new JScrollPane(found);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		Object[] parameters = { new JLabel("Recipes found:"), scroller };

		choice = JOptionPane.showConfirmDialog(null, parameters,
				"Recipes Found:", JOptionPane.OK_CANCEL_OPTION);

		/*
		 * A little error checking for the selection form.
		 */
		if (choice == JOptionPane.CANCEL_OPTION)
			return null;

		if (found.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(null, "NO RECIPES SELECTED", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return found.getSelectedValue().toString();
	}
}
