import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class to store the information of the recipe.
 * 
 * @author Kyle Diller
 *
 */
public class Recipe implements Comparable<Recipe> {
	/**
	 * The information of the recipe.
	 */
	private String name;
	private ArrayList<String> ingredients;
	private ArrayList<String> steps;

	/**
	 * Creates an empty recipe.
	 */
	public Recipe() {
		ingredients = new ArrayList<String>();
		steps = new ArrayList<String>();
		name = "";
	}

	/**
	 * Creates a copy of a recipe.
	 * 
	 * @param r
	 *            the recipe to copy.
	 */
	public Recipe(Recipe r) {
		this();

		name = r.name;
		for (String i : r.ingredients) {
			ingredients.add(i);
		}

		for (String s : r.steps) {
			steps.add(s);
		}
	}

	/**
	 * Creates a recipe from provided information.
	 * 
	 * @param nam
	 *            the name of the recipe.
	 * @param ing
	 *            the ingridients of the reicpe.
	 * @param stp
	 *            the steps of the recipe.
	 */
	public Recipe(String nam, String[] ing, String[] stp) {
		this();

		name = nam;

		for (String i : ing) {
			ingredients.add(i);
		}

		for (String s : stp) {
			steps.add(s);
		}
	}

	/**
	 * @return the name of the recipe.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Saves a recipe to either pdf format or text format.
	 * 
	 * @param os
	 *            the output stream to write to.
	 * @param data
	 *            the way to save the recipe. True means text format and false
	 *            means pdf format.
	 * @throws IOException
	 *             if there is a problem writing to the stream.
	 */
	private void saveRecipe(FileWriter os, boolean data) throws IOException {
		String line = "";

		if (data)
			line = "BEGINRECIPE";

		os.write(line + "\n");

		if (data)
			line = "NAME";

		os.write(line + name + "\n");

		if (data)
			line = "INGR";

		for (String l : ingredients) {
			os.write(line + l + "\n");
		}

		if (data)
			line = "STEP";

		for (String l : steps) {
			os.write(line + l + "\n");
		}

		if (data)
			line = "ENDRECIPE";

		os.write(line + "\n");
	}

	/**
	 * Saves a recipe in pdf format.
	 * 
	 * @param os
	 *            the output stream to write to.
	 * @throws IOException
	 *             if there is a problem in writing the file.
	 */
	public void saveRecipePDF(FileWriter os) throws IOException {
		saveRecipe(os, false);
	}

	/**
	 * Saves a recipe in text format.
	 * 
	 * @param os
	 *            the output stream to write to.
	 * @throws IOException
	 *             if there is a problem in writing the file.
	 */
	public void saveRecipeTXT(FileWriter os) throws IOException {
		saveRecipe(os, true);
	}

	/**
	 * Changes a recipe based on what is passed in.
	 * 
	 * @param nam
	 *            the new name of the recipe.
	 * @param ing
	 *            the new ingredients of the recipe.
	 * @param stp
	 *            the new steps to the recipe.
	 */
	public void editRecipe(String nam, String[] ing, String[] stp) {
		Recipe other = new Recipe(nam, ing, stp);
		this.name = other.name;
		this.ingredients = other.ingredients;
		this.steps = other.steps;
	}

	/**
	 * Checks if a recipe is to be added to the filtered list or not.
	 * 
	 * @param search
	 *            the substring to filter by.
	 * @param name
	 *            whether to check the name or not.
	 * @param ingr
	 *            whether to check the ingredients.
	 * @param step
	 *            whether to check the steps.
	 * @return whether the recipe is to be added to the filtered list.
	 */
	public boolean contains(String search, boolean name, boolean ingr,
			boolean step) {
		search = search.toUpperCase();

		return (name && isName(search)) || (ingr && hasIngr(search))
				|| (step && hasStep(search));
	}

	/**
	 * Checks if a name contains a substring.
	 * 
	 * @param search
	 *            the substring to check.
	 * @return whether the name contains a substring.
	 */
	private boolean isName(String search) {
		return name.toUpperCase().contains(search);
	}

	/**
	 * Checks if the recipe contains an ingredient.
	 * 
	 * @param search
	 *            the substring to search for.
	 * @return whether the recipe has a ingredient substring.
	 */
	private boolean hasIngr(String search) {
		for (String i : ingredients) {
			if (i.toUpperCase().contains(search))
				return true;
		}

		return false;
	}

	/**
	 * Checks if a recipe has a step.
	 * 
	 * @param search
	 *            the substring to search for.
	 * @return whether a step has the substring or not.
	 */
	private boolean hasStep(String search) {
		for (String i : steps) {
			if (i.toUpperCase().contains(search))
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * Creates a string representation of the recipe.
	 */
	public String toString() {
		String recipe = name + "\n";

		recipe += "\nIngredients:\n";

		for (int i = 0; i < ingredients.size(); i++) {
			recipe += (i + 1) + ": " + ingredients.get(i) + "\n";
		}

		recipe += "\nSteps:\n";

		for (int i = 0; i < steps.size(); i++) {
			recipe += (i + 1) + ": " + steps.get(i) + "\n";
		}

		return recipe;
	}

	/**
	 * Checks if the two names are the same.
	 * 
	 * @param name2
	 *            the name to check against.
	 * @return the name to check against.
	 */
	public boolean nameEquals(String name2) {
		return name.equals(name2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * Compares based on the names of the recipes.
	 */
	@Override
	public int compareTo(Recipe arg0) {
		return this.name.compareTo(arg0.name);
	}

	/**
	 * @return the number of ingredients in the recipe.
	 */
	public int getNumIngredients() {
		return ingredients.size();
	}

	/**
	 * Gets an ingredient at a given index.
	 * 
	 * @param i
	 *            the index to get the ingredient from.
	 * @return the ingredient at a given index.
	 */
	public String getIngredient(int i) {
		return ingredients.get(i);
	}

	/**
	 * @return the number of steps in this recipe.
	 */
	public int getNumSteps() {
		return steps.size();
	}

	/**
	 * Gets the step at an index.
	 * 
	 * @param i
	 *            the index to get the step from.
	 * @return the step at a given index.
	 */
	public String getStep(int i) {
		return steps.get(i);
	}

	/**
	 * Sets the name of the recipe.
	 * 
	 * @param substring
	 *            the name to set to.
	 */
	public void setName(String substring) {
		name = substring;
	}

	/**
	 * Adds an ingredient to the recipe.
	 * 
	 * @param substring
	 *            the ingredient to add.
	 */
	public void addIngredient(String substring) {
		ingredients.add(substring);
	}

	/**
	 * Adds a step to the recipe.
	 * 
	 * @param substring
	 *            the step to add.
	 */
	public void addStep(String substring) {
		steps.add(substring);
	}
}
