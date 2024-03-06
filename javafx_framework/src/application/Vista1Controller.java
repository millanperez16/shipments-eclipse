package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * Controller class for the first vista.
 */
public class Vista1Controller implements Initializable {

	@FXML
	private ImageView cardImg;

	@FXML
	private Button btnPlay;

	@FXML
	private Label lblWord;

	@FXML
	private Label lblUnderlines;

	@FXML
	private TextField tfLetter;

	@FXML
	private Label lblFailedLetters;

	@FXML
	private TextField tfFailedLetters;

	GameManager manager = GameManager.getManager();

	Image[] images = new Image[13];
	String[] possibleWords = { "AGNÒSTIQUES", "ESCURÇÓ", "FRUÏCIÓ", "GALVANOTÈCNIA", "PASTANAGA", "MASSÍS" };
	String hiddenWord;
	String revealedWord;
	String validLetters = "AÀBCÇDEÈÉFGHIÍÏJKLMNOÒÓPQRSTUÚÜVWXYZ";
	String formattedValidLetters = "AABCÇDEEEFGHIIIJKLMNOOOPQRSTUUUVWXYZ";
	String startGame = "Iniciar partida";
	String restartGame = "Reiniciar partida";

	boolean loaded = false;
	int count = 12;

	public void init() {
		if (!loaded) {
			instantiateImages();
			loaded = true;
		}
		lblFailedLetters.setVisible(manager.getShowFailedLetters());
		tfFailedLetters.setVisible(manager.getShowFailedLetters());
		cardImg.setImage(images[manager.getLives()]);
		tfLetter.setTextFormatter(new TextFormatter<TextField>(modifyChange));
	}

	public void instantiateImages() {
		for (int i = 0; i < 13; i++) {
			if (i < 10) {
				images[i] = new Image(getClass().getResourceAsStream("resource/carta_0" + i + ".jpg"));
			} else {
				images[i] = new Image(getClass().getResourceAsStream("resource/carta_" + i + ".jpg"));
			}
		}
	}

	public void gameStart() {
		count = manager.getLives();
		int randomWord = (int) (Math.random() * possibleWords.length);
		hiddenWord = possibleWords[randomWord];
		revealedWord = " ".repeat(hiddenWord.length());
		lblWord.setText("");
		lblUnderlines.setText("_".repeat(hiddenWord.length()));
		tfLetter.setDisable(false);
		lblFailedLetters.setVisible(manager.getShowFailedLetters());
		tfFailedLetters.setVisible(manager.getShowFailedLetters());
		cardImg.setImage(images[count]);
		btnPlay.setText(restartGame);
	}

	public void loadCard() {
		cardImg.setImage(images[--count]);
		if (count <= 0) {
			gameEnd();
		}
	}

	UnaryOperator<TextFormatter.Change> modifyChange = change -> {
		if (change.isContentChange()) { // hi ha hagut un canvi
			int newLength = change.getControlNewText().length();
			if (newLength > 1) {
				return null; // descarta el canvi
			}
		}
		return change;
	};

	public void checkWord() {
		tfLetter.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER && !tfLetter.getText().isBlank()) {
				char formattedLetter = formatWord(tfLetter.getText()).charAt(0);
				if (formatWord(hiddenWord).indexOf(formattedLetter) != -1) {
					char[] revealedWordToArray = revealedWord.toCharArray();
					char[] formattedHiddenWordToArray = formatWord(hiddenWord).toCharArray();
					char[] defaultHiddenWordToArray = hiddenWord.toCharArray();
					for (int i = 0; i < revealedWordToArray.length; i++) {
						if (formattedHiddenWordToArray[i] == formattedLetter) {
							revealedWordToArray[i] = defaultHiddenWordToArray[i];
						}
					}
					revealedWord = String.valueOf(revealedWordToArray);
					lblWord.setText(revealedWord);
					if (revealedWord.equals(hiddenWord)) {
						gameEnd();
					}
				} else {
					loadCard();
				}

			}
		});
	}

	public String formatWord(String word) {
		return word.toUpperCase().replace('À', 'A').replace('È', 'E').replace('É', 'E').replace('Í', 'I')
				.replace('Ï', 'I').replace('Ò', 'O').replace('Ó', 'O').replace('Ú', 'U').replace('Ü', 'U');
	}

	public void gameEnd() {
		tfLetter.setText("");
		tfLetter.setDisable(true);
		btnPlay.setText(startGame);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
	}
}