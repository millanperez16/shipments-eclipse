package application;

public class GameManager {

	private Integer lives;
	private Boolean showFailedLetters;
	private Boolean countRepetitionsAsErrors;
	private static GameManager manager;

	private GameManager(Integer lives, Boolean showFailedLetters, Boolean countRepetitionsAsErrors) {
		super();
		this.lives = lives;
		this.showFailedLetters = showFailedLetters;
		this.countRepetitionsAsErrors = countRepetitionsAsErrors;
	}

	public static GameManager getManager() {
		if (manager == null) {
			manager = new GameManager(12, true, false);
		}
		return manager;
	}

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		if (lives != null) {
			if (lives < 6) {
				this.lives = 6;
			} else if (lives > 12) {
				this.lives = 12;
			} else {
				this.lives = lives;
			}
		}
	}

	public Boolean getShowFailedLetters() {
		return showFailedLetters;
	}

	public void setShowFailedLetters(Boolean showFailedLetters) {
		this.showFailedLetters = showFailedLetters;
	}

	public Boolean getCountRepetitionsAsErrors() {
		return countRepetitionsAsErrors;
	}

	public void setCountRepetitionsAsErrors(Boolean countRepetitionsAsErrors) {
		this.countRepetitionsAsErrors = countRepetitionsAsErrors;
	}

}
