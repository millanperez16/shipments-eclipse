package ins.marianao.shipments.fxml;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.User;
import cat.institutmarianao.shipmentsws.model.Shipment.Category;
import ins.marianao.shipments.fxml.manager.ResourceManager;
import ins.marianao.shipments.fxml.services.ServiceSaveShipment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class ControllerFormShipment implements Initializable, ChangeListener<Pair<String, String>> {
	@FXML private BorderPane viewProfileForm;
	@FXML private HBox boxReceptionists;
	@FXML private HBox boxCouriers;

	@FXML private Button btnSave;

	@FXML private ComboBox<Pair<String, String>> cmbCategory;

	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private PasswordField txtConfirm;
	@FXML private TextField txtFullname;
	@FXML private TextField txtExtension;

	@FXML private TextField txtPlace;

	private boolean edicio;
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		this.edicio = false;
		
		//this.lblUsuari.setText("\u2386");
		//this.lblNom.setText("\u1F604");
		//this.lblExtensio.setText("\u2706");

		List<Pair<String,String>> categories = Stream.of(Shipment.Category.values()).map(new Function<Category,Pair<String,String>>() {
			@Override
			public Pair<String,String> apply(Category t) {
				String key = t.name();
				return new Pair<String, String>(key, resource.getString("fxml.text.formShipment.category.Category."+key));
			}
			
		}).collect(Collectors.toList());
		
		this.cmbCategory.setItems(FXCollections.observableArrayList(categories));
		this.cmbCategory.setConverter(Formatters.getStringPairConverter("Category"));
	}

	public void enableEdition() {
		this.edicio = true;

		String key = User.Role.COURIER.name();
		this.cmbCategory.setValue(new Pair<String, String>(key, ResourceManager.getInstance().getText("text.User."+key)));
		
		this.txtUsername.clear();
		this.txtFullname.clear();
		this.txtExtension.clear();
		this.txtPlace.clear();

		this.enableCourierFields();

		this.txtUsername.setDisable(false);
		this.txtUsername.setEditable(true);
		this.cmbCategory.setDisable(false);
		this.cmbCategory.valueProperty().addListener(this);
	}

	private void enableCourierFields() {
		this.boxCouriers.toFront();
		this.boxReceptionists.toBack();

		// Reset Receptionist fields
		this.txtPlace.clear();
		
		this.cmbCategory.setVisible(true);
	}

	private void enableReceptinistFields() {
		this.boxReceptionists.toFront();
		this.boxCouriers.toBack();
		
		// Reset Courier fields
		if (this.cmbCategory.getItems().isEmpty()) this.cmbCategory.setValue(null);
		else {
			this.cmbCategory.setValue(this.cmbCategory.getItems().get(0));
		}
		
		this.cmbCategory.setVisible(false);
	}

	@Override
	public void changed(ObservableValue<? extends Pair<String, String>> observable, Pair<String, String> oldValue, Pair<String, String> newValue) {
		if (observable.equals(this.cmbCategory.valueProperty())) {
			if (User.Role.COURIER.name().equals(newValue.getKey())) {
				// Couriers
				this.enableCourierFields();
			} else {
				// Receptionists or LogisticsManagers
				this.enableReceptinistFields();
			}
		}
	}

	@FXML
	public void saveShipmentClick(ActionEvent event) {
		try {
			Pair<String,String> categories = this.cmbCategory.getValue();

			String strUsername = this.txtUsername.getText();	/* Disable edition */
			String strFullName = this.txtFullname.getText();
			int numExt = (int) this.txtExtension.getTextFormatter().getValue();
			String strPlace = this.txtPlace.getText();

			String password = this.txtPassword.getText();
			String confirm = this.txtConfirm.getText();
			if (password != null && !password.equals(confirm)) throw new Exception(ResourceManager.getInstance().getText("error.formUser.password.check"));
			
			
		} catch (Exception e) {
			ControllerMenu.showError(e.getMessage(), e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		
	}
	
	private void saveShipment(Shipment shipment, boolean insert) throws Exception {
		final ServiceSaveShipment saveShipment = new ServiceSaveShipment(shipment, insert);
		
		saveShipment.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
            	
            	Shipment shipment = saveShipment.getValue();
            	
            	
            }
        });
		
		saveShipment.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent t) {
				Throwable e = t.getSource().getException();
				
				ControllerMenu.showError(ResourceManager.getInstance().getText("error.formUser.save.web.service"), e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
			
		});
		
		saveShipment.start();
	}
}



