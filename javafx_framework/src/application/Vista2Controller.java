package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Controller class for the second vista.
 */
public class Vista2Controller implements Initializable {

	@FXML
	Spinner<Integer> spLives;

	@FXML
	CheckBox cbFailedLetters;

	@FXML
	CheckBox cbRepeatError;

	@FXML
	Button btnReturn;

	GameManager manager = GameManager.getManager();

	public void init() {
		if (spLives.getValueFactory() == null) {
			spLives.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 12));
		}
		spLives.getValueFactory().setValue(manager.getLives());
		cbFailedLetters.setSelected(manager.getShowFailedLetters());
		cbRepeatError.setSelected(manager.getCountRepetitionsAsErrors());
		spLives.valueProperty().addListener((arg0, arg1, arg2) -> {
			manager.setLives(arg2);
		});
	}

	public void setRules() {
		manager.setLives(spLives.getValue());
		manager.setShowFailedLetters(cbFailedLetters.isSelected());
		manager.setCountRepetitionsAsErrors(cbRepeatError.isSelected());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
		btnReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setRules();
				init();
				VistaNavigator.loadVista(VistaNavigator.VISTA_1);
			}
		});
	}

}