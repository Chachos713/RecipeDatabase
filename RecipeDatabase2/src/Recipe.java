import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Recipe implements Comparable<Recipe> {
	private String name;
	private ArrayList<String> ingredients;
	private ArrayList<String> steps;

	public Recipe() {
		ingredients = new ArrayList<String>();
		steps = new ArrayList<String>();
		name = "";
	}

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

	public String getName() {
		return name;
	}

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

	public void saveRecipePDF(FileWriter os) throws IOException {
		saveRecipe(os, false);
	}

	public void saveRecipeTXT(FileWriter os) throws IOException {
		saveRecipe(os, true);
	}

	public void editRecipe(String nam, String[] ing, String[] stp) {
		Recipe other = new Recipe(nam, ing, stp);
		this.name = other.name;
		this.ingredients = other.ingredients;
		this.steps = other.steps;
	}

	public boolean contains(String search, boolean name, boolean ingr,
			boolean step) {
		search = search.toUpperCase();

		return (name && isName(search)) || (ingr && hasIngr(search))
				|| (step && hasStep(search));
	}

	private boolean isName(String search) {
		return name.toUpperCase().contains(search);
	}

	private boolean hasIngr(String search) {
		for (String i : ingredients) {
			if (i.toUpperCase().contains(search))
				return true;
		}

		return false;
	}

	private boolean hasStep(String search) {
		for (String i : steps) {
			if (i.toUpperCase().contains(search))
				return true;
		}

		return false;
	}

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

	public boolean nameEquals(String name2) {
		return name.equals(name2);
	}

	@Override
	public int compareTo(Recipe arg0) {
		return this.name.compareTo(arg0.name);
	}

	public int getNumIngredients() {
		return ingredients.size();
	}

	public String getIngredient(int i) {
		return ingredients.get(i);
	}

	public int getNumSteps() {
		return steps.size();
	}

	public String getStep(int i) {
		return steps.get(i);
	}

	public void setName(String substring) {
		name = substring;
	}

	public void addIngredient(String substring) {
		ingredients.add(substring);
	}

	public void addStep(String substring) {
		steps.add(substring);
	}
}
