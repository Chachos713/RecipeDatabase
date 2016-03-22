import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The model to keep track of recipes.
 * 
 * @author Kyle Diller
 *
 */
public class Database {
	/**
	 * The list of recipes.
	 */
	private ArrayList<Recipe> recipes;

	/**
	 * Creates an empty database.
	 */
	public Database() {
		recipes = new ArrayList<Recipe>();
	}

	/**
	 * Adds a recipe to the database.
	 * 
	 * @param r
	 *            the recipe to add to database.
	 */
	public void addRecipe(Recipe r) {
		recipes.add(r);
	}

	/**
	 * Sorts the recipes based on name.
	 */
	public void sort() {
		Collections.sort(recipes);
	}

	/**
	 * Gives the name of all the recipes.
	 * 
	 * @return the list of names of the recipes.
	 */
	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();

		for (Recipe rec : recipes) {
			names.add(rec.getName());
		}

		return names;
	}

	/**
	 * Returns a string representation of a recipe.
	 * 
	 * @param name
	 *            the name of the recipe to find.
	 * @return the string representation of the recipe.
	 */
	public String displayRecipe(String name) {
		int i = findRecipe(name);

		if (i < 0)
			return null;
		return recipes.get(i).toString();
	}

	/**
	 * Removes a recipe from the list.
	 * 
	 * @param name
	 *            the recipe to remove.
	 * @return the recipe that was removed.
	 */
	public Recipe popRecipe(String name) {
		int i = findRecipe(name);

		if (i < 0)
			return null;
		return recipes.remove(i);
	}

	/**
	 * Finds a recipe in the list.
	 * 
	 * @param name
	 *            the name of the recipe to find.
	 * @return the location of the recipe in the list.
	 */
	public int findRecipe(String name) {
		for (int i = 0; i < recipes.size(); i++) {
			if (recipes.get(i).nameEquals(name))
				return i;
		}

		return -1;
	}

	/**
	 * Saves a database in pdf format to a file.
	 * 
	 * @param f
	 *            the location to save the pdf to.
	 */
	public void saveDataPDF(File f) {
		try {
			FileWriter os = new FileWriter(f);

			for (Recipe r : recipes) {
				r.saveRecipePDF(os);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Saves the database in text format. USed to read latter.
	 * 
	 * @param f
	 *            the location to save the database to.
	 */
	public void saveDataTXT(File f) {
		try {
			FileWriter os = new FileWriter(f);

			for (Recipe r : recipes) {
				r.saveRecipeTXT(os);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Saves one recipe to a pdf format.
	 * 
	 * @param f
	 *            the location to save the recipe to.
	 * @param name
	 *            the recipe to save in pdf format.
	 */
	public void saveRecipePDF(File f, String name) {
		try {
			FileWriter os = new FileWriter(f);

			int i = findRecipe(name);
			recipes.get(i).saveRecipePDF(os);
		} catch (Exception e) {
		}
	}

	/**
	 * Saves a recipe to text format for later use.
	 * 
	 * @param f
	 *            the location to save the recipe to.
	 * @param name
	 *            the name of the recipe to save.
	 */
	public void saveRecipeTXT(File f, String name) {
		try {
			FileWriter os = new FileWriter(f);

			int i = findRecipe(name);
			recipes.get(i).saveRecipeTXT(os);
		} catch (Exception e) {
		}
	}

	/**
	 * Gets a filtered list of names.
	 * 
	 * @param name
	 *            the string to search for.
	 * @param cbName
	 *            search by name.
	 * @param cbIngr
	 *            search by ingredients.
	 * @param cbStep
	 *            search by steps.
	 * @return a filtered list of names based on what is set to search by.
	 */
	public ArrayList<String> search(String name, boolean cbName,
			boolean cbIngr, boolean cbStep) {
		name = name.toUpperCase();
		ArrayList<String> names = new ArrayList<String>();

		for (Recipe rec : recipes) {
			if (rec.contains(name, cbName, cbIngr, cbStep)) {
				names.add(rec.getName());
			}
		}

		return names;
	}

	/**
	 * The number of recipes in the list.
	 * 
	 * @return the number of recipes in the database.
	 */
	public int getNumRecipes() {
		return recipes.size();
	}

	/**
	 * Gets a recipe based on an index.
	 * 
	 * @param i
	 *            the index of the recipe.
	 * @return the recipe from the index provided.
	 */
	public Recipe getRecipe(int i) {
		return recipes.get(i);
	}
}
