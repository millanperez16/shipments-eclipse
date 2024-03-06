package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Main controller class for the entire layout.
 */
public class MainController {

	/** Holder of a switchable vista. */
	@FXML
	private AnchorPane vistaHolder;

	/**
	 * Replaces the vista displayed in the vista holder with a new vista.
	 *
	 * @param node the vista node to be swapped in.
	 */
	public void setVista(Node node) {
		vistaHolder.getChildren().add(node);
	}

	@FXML
	void setVista1(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.VISTA_1);
	}

	@FXML
	void setVista2(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.VISTA_2);
	}

	@FXML
	void setQuantA(ActionEvent event) {
		VistaNavigator.loadVista(VistaNavigator.VISTA_QUANT_A);
	}

	public void llistarVistes() {
		int i = 1;
		for (Node view : vistaHolder.getChildren()) {
			System.out.println("núm: " + (i++) + " vista: " + view.getId());
		}
		System.out.println("----------");
	}

	public Node getView(String fxml) {
		for (Node view : vistaHolder.getChildren()) {
			if (fxml.contains(view.getId())) {
				return view;
			}
		}
		return null;
	}

}