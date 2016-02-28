import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Database {
	private ArrayList<Recipe> recipes;

	public Database() {
		recipes = new ArrayList<Recipe>();
	}

	public void addRecipe(Recipe r) {
		recipes.add(r);
	}

	public void sort() {
		Collections.sort(recipes);
	}

	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();

		for (Recipe rec : recipes) {
			names.add(rec.getName());
		}

		return names;
	}

	public String displayRecipe(String name) {
		int i = findRecipe(name);

		if (i < 0)
			return null;
		return recipes.get(i).toString();
	}

	public Recipe popRecipe(String name) {
		int i = findRecipe(name);

		if (i < 0)
			return null;
		return recipes.remove(i);
	}

	public int findRecipe(String name) {
		for (int i = 0; i < recipes.size(); i++) {
			if (recipes.get(i).nameEquals(name))
				return i;
		}

		return -1;
	}

	public void saveDataPDF(File f) {
		try {
			FileWriter os = new FileWriter(f);

			for (Recipe r : recipes) {
				r.saveRecipePDF(os);
			}
		} catch (Exception e) {
		}
	}

	public void saveDataTXT(File f) {
		try {
			FileWriter os = new FileWriter(f);

			for (Recipe r : recipes) {
				r.saveRecipeTXT(os);
			}
		} catch (Exception e) {
		}
	}

	public void saveRecipePDF(File f, String name) {
		try {
			FileWriter os = new FileWriter(f);

			int i = findRecipe(name);
			recipes.get(i).saveRecipePDF(os);
		} catch (Exception e) {
		}
	}

	public void saveRecipeTXT(File f, String name) {
		try {
			FileWriter os = new FileWriter(f);

			int i = findRecipe(name);
			recipes.get(i).saveRecipeTXT(os);
		} catch (Exception e) {
		}
	}

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

	public int getNumRecipes() {
		return recipes.size();
	}

	public Recipe getRecipe(int i) {
		return recipes.get(i);
	}
}
