package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 */
public class VistaNavigator {

	/**
	 * Convenience constants for fxml layouts managed by the navigator.
	 */
	public static final String MAIN = "main.fxml";
	public static final String VISTA_1 = "vista1.fxml";
	public static final String VISTA_2 = "vista2.fxml";
	public static final String VISTA_QUANT_A = "quantA.fxml";

	/** The main application layout controller. */
	private static MainController mainController;
	private static Vista1Controller vista1Controller;
	private static Vista2Controller vista2Controller;

	/**
	 * Stores the main controller for later use in navigation tasks.
	 *
	 * @param mainController the main application layout controller.
	 */
	public static void setMainController(MainController mainController) {
		VistaNavigator.mainController = mainController;
	}

	/**
	 * Loads the vista specified by the fxml file into the vistaHolder pane of the
	 * main application layout.
	 *
	 * Previously loaded vista for the same fxml file are not cached. The fxml is
	 * loaded anew and a new vista node hierarchy generated every time this method
	 * is invoked.
	 *
	 * A more sophisticated load function could potentially add some enhancements or
	 * optimizations, for example: cache FXMLLoaders cache loaded vista nodes, so
	 * they can be recalled or reused allow a user to specify vista node reuse or
	 * new creation allow back and forward history like a browser
	 *
	 * @param fxml the fxml file to be loaded.
	 */
	public static void loadVista(String fxml) {
		try {
			FXMLLoader loader = new FXMLLoader();
			AnchorPane childPane = (AnchorPane) loader.load(VistaNavigator.class.getResourceAsStream(fxml));
			Object childController = loader.getController();

			Node view = mainController.getView(fxml);
			if (view != null) {
				view.toFront();
			} else {
				mainController.setVista(childPane);
				switch (fxml) {
				case VISTA_1:
					vista1Controller = (Vista1Controller) childController;
					break;
				case VISTA_2:
					vista2Controller = (Vista2Controller) childController;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}