import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class RecipeEditor {
	private static Box display;
	private static JTextField name;
	private static JTextArea ingredients, steps;

	private static void create() {
		display = Box.createVerticalBox();

		name = new JTextField(50);
		ingredients = new JTextArea(10, 50);
		steps = new JTextArea(10, 50);

		JScrollPane ing = new JScrollPane(ingredients);
		ing.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ing.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JScrollPane stp = new JScrollPane(steps);
		stp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		stp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		display.add(new JLabel("Name:"));
		display.add(name);
		display.add(new JLabel("Ingredients:"));
		display.add(ing);
		display.add(new JLabel("Steps:"));
		display.add(stp);
	}

	public static Recipe edit(Recipe r) {
		if (display == null)
			create();

		setEdits(r);

		int choice = JOptionPane.showConfirmDialog(null, display,
				"Recipe Editor", JOptionPane.OK_CANCEL_OPTION);

		if (choice == JOptionPane.CANCEL_OPTION)
			return r;

		return getRecipe();
	}

	private static Recipe getRecipe() {
		String n = name.getText();

		if (n.isEmpty()) {
			JOptionPane.showMessageDialog(null, "NO RECIPE NAME", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		String[] i = ingredients.getText().split("\n");
		String[] s = steps.getText().split("\n");

		return new Recipe(n, i, s);
	}

	private static void setEdits(Recipe r) {
		if (r == null) {
			r = new Recipe();
		}

		name.setText(r.getName());
		ingredients.setText("");
		steps.setText("");

		for (int i = 0; i < r.getNumIngredients(); i++) {
			if (i != 0)
				ingredients.append("\n");

			ingredients.append(r.getIngredient(i));
		}

		for (int i = 0; i < r.getNumSteps(); i++) {
			if (i != 0)
				steps.append("\n");

			steps.append(r.getStep(i));
		}
	}
}
