package ins.marianao.shipments.fxml;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cat.institutmarianao.shipmentsws.model.Action;
import cat.institutmarianao.shipmentsws.model.Assignment;
import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Shipment.Category;
import cat.institutmarianao.shipmentsws.model.Shipment.Status;
import ins.marianao.shipments.fxml.manager.ResourceManager;
import ins.marianao.shipments.fxml.services.ServiceQueryShipments;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerShipmentsList implements Initializable {
	@FXML private BorderPane viewShipments;
	
	@FXML private ComboBox<Pair<String,String>> cmbStatus;
	@FXML private ComboBox<Pair<String,String>> cmbCategory;
	@FXML private TextField txtRecepcionist;
	@FXML private TextField txtCourier;
	@FXML private DatePicker from;
	@FXML private DatePicker to;
	
	@FXML private TableView<Shipment> shipmentsTable;
	@FXML private TableColumn<Shipment, Number> colIndex;
	@FXML private TableColumn<Shipment, String> colCategory;
	@FXML private TableColumn<Shipment, String> colRecipient;
	@FXML private TableColumn<Shipment, String> colSender;
	@FXML private TableColumn<Shipment, String> colReceptionist;
	@FXML private TableColumn<Shipment, String> colCourier;
	@FXML private TableColumn<Shipment, String> colDimensions;
	@FXML private TableColumn<Shipment, String> colNote;
	@FXML private TableColumn<Shipment, String> colWeight;
	@FXML private TableColumn<Shipment, Boolean> colExpress;
	@FXML private TableColumn<Shipment, Boolean> colFragile;

	@Override
	public void initialize(URL location, ResourceBundle resource) {
		
		// Category
		List<Pair<String,String>> categories = Stream.of(Shipment.Category.values()).map(new Function<Category,Pair<String,String>>() {
			@Override
			public Pair<String,String> apply(Category t) {
				String key = t.name();
				return new Pair<String, String>(key, resource.getString("text.Category."+key));
			}
			
		}).collect(Collectors.toList());
		
		ObservableList<Pair<String,String>> listCategories = FXCollections.observableArrayList(categories);
		listCategories.add(0, null);
		
		this.cmbCategory.setItems(listCategories);
		this.cmbCategory.setConverter(Formatters.getStringPairConverter("Category"));
		
		this.cmbCategory.valueProperty().addListener(new ChangeListener<Pair<String,String>>() {
			@Override
			public void changed(ObservableValue<? extends Pair<String,String>> observable, Pair<String,String> oldValue, Pair<String,String> newValue) {
				reloadShipments();
			}
		});
		
		// Status
		List<Pair<String,String>> status = Stream.of(Shipment.Status.values()).map(new Function<Status,Pair<String,String>>() {
			@Override
			public Pair<String,String> apply(Status t) {
				String key = t.name();
				return new Pair<String, String>(key, resource.getString("text.Status."+key));
			}
			
		}).collect(Collectors.toList());
		
		ObservableList<Pair<String,String>> listStatus = FXCollections.observableArrayList(status);
		listStatus.add(0, null);
		
		this.cmbStatus.setItems(listStatus);
		this.cmbStatus.setConverter(Formatters.getStringPairConverter("Status"));
		
		this.cmbStatus.valueProperty().addListener(new ChangeListener<Pair<String,String>>() {
			@Override
			public void changed(ObservableValue<? extends Pair<String,String>> observable, Pair<String,String> oldValue, Pair<String,String> newValue) {
				reloadShipments();
			}
		});
		
		this.txtRecepcionist.setDisable(false);
		this.txtRecepcionist.setEditable(true);
		
		this.txtCourier.setDisable(false);
		this.txtCourier.setEditable(true);
		
		this.from.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                reloadShipments();
            }
        });
		
		this.to.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                reloadShipments();
            }
        });
		
		this.reloadShipments();
		
		this.shipmentsTable.setEditable(true);
		this.shipmentsTable.getSelectionModel().setCellSelectionEnabled(true);
		this.shipmentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		this.colIndex.setMinWidth(40);
		this.colIndex.setMaxWidth(60);
		this.colIndex.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Shipment, Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(TableColumn.CellDataFeatures<Shipment, Number> enviament) {
				return new SimpleLongProperty( shipmentsTable.getItems().indexOf(enviament.getValue()) + 1 );
			}
		});
		
		this.colCategory.setMaxWidth(70);
		//this.colRol.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
		//this.colRol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));
		this.colCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Shipment, String> cellData) {
				String key = cellData.getValue().getCategory().toString();
				return new SimpleStringProperty(resource.getString("text.Category."+key));
			}
		});
		this.colCategory.setCellFactory(TextFieldTableCell.forTableColumn());
		this.colCategory.setEditable(false);
		
		this.colReceptionist.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Shipment, String> cellData) {
				String key = cellData.getValue().getReceptionist();
				
				return new SimpleStringProperty(key);
			}
		});
		
		this.colCourier.setMaxWidth(170);
		this.colCourier.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Shipment, String> cellData) {
				String key = cellData.getValue().getCourier();
				
				return new SimpleStringProperty(key);
			}
		});
		
		this.colRecipient.setMinWidth(140);
		this.colRecipient.setMaxWidth(200);
		
		TableColumn<Shipment, String> nameColumnRecipient = new TableColumn<>("Name");
		TableColumn<Shipment, String> streetColumnRecipient = new TableColumn<>("Street");
		TableColumn<Shipment, String> cityColumnRecipient = new TableColumn<>("City");
		this.colRecipient.getColumns().addAll(nameColumnRecipient, streetColumnRecipient, cityColumnRecipient);
		nameColumnRecipient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecipient().getName()));
		streetColumnRecipient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecipient().getStreet()));
		cityColumnRecipient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecipient().getCity()));
		
		
		this.colSender.setMinWidth(140);
		this.colSender.setMaxWidth(200);
		TableColumn<Shipment, String> nameColumnSender = new TableColumn<>("Name");
		TableColumn<Shipment, String> streetColumnSender = new TableColumn<>("Street");
		TableColumn<Shipment, String> cityColumnSender = new TableColumn<>("City");
		this.colSender.getColumns().addAll(nameColumnSender, streetColumnSender, cityColumnSender);
		nameColumnSender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSender().getName()));
		streetColumnSender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSender().getStreet()));
		cityColumnSender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSender().getCity()));
		
		this.colDimensions.setMinWidth(140);
		this.colDimensions.setMaxWidth(200);
		this.colDimensions.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Shipment, String> cellData) {
				String key = "";
				Float height = cellData.getValue().getHeight();
				Float width = cellData.getValue().getWidth();
				Float length = cellData.getValue().getLength();
				if (height != null && width != null && length != null) {
					key = String.format("%.1f x %.1f x %.1f", height, width, length);
				}
				
				return new SimpleStringProperty(key);
			}
		});
		this.colDimensions.setCellFactory(TextFieldTableCell.forTableColumn());
		
		this.colNote.setMinWidth(140);
		this.colNote.setMaxWidth(200);
		this.colNote.setCellValueFactory(new PropertyValueFactory<Shipment,String>("note"));
		this.colNote.setCellFactory(TextFieldTableCell.forTableColumn());
		this.colNote.setEditable(false);
		
		this.colWeight.setMinWidth(50);
		this.colWeight.setMaxWidth(70);
		this.colWeight.setCellValueFactory(new PropertyValueFactory<Shipment,String>("weight"));
		
		this.colExpress.setMinWidth(50);
		this.colExpress.setMaxWidth(70);
		this.colExpress.setCellValueFactory(new PropertyValueFactory<Shipment,Boolean>("express"));
		
		this.colFragile.setMinWidth(50);
		this.colFragile.setMaxWidth(70);
		this.colFragile.setCellValueFactory(new PropertyValueFactory<Shipment,Boolean>("fragile"));
		
		
	}
	
	private void reloadShipments() {
		Category categories = null;
		Pair<String,String> category = this.cmbCategory.getValue();
		if (category != null) {
			String categoryName = category.getKey(); 
		    categories = Category.valueOf(categoryName);
		}
	    
		Status status = null;
		Pair<String,String> stat = this.cmbStatus.getValue();
		if (stat != null) {
			String statusName = stat.getKey();
			status = Status.valueOf(statusName);
		}
		
		LocalDate dateFrom = this.from.getValue();
		Date from = null;
		if (dateFrom != null) {
			from = Date.from(dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} 
		
		LocalDate dateTo = this.to.getValue();
		Date to = null;
		if (dateTo != null) {
			to = Date.from(dateTo.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} 
		
		
		this.shipmentsTable.setEditable(false);
		
		final ServiceQueryShipments queryShipments = new ServiceQueryShipments(categories, status, "", "", from, to);
		
		queryShipments.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
            	shipmentsTable.setEditable(true);
            	
            	shipmentsTable.getItems().clear();
            	
                ObservableList<Shipment> users = FXCollections.observableArrayList(queryShipments.getValue());

                shipmentsTable.setItems( users );
            }
        });
		
		queryShipments.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				shipmentsTable.setEditable(true);

				Throwable e = t.getSource().getException();
				
				ControllerMenu.showError(ResourceManager.getInstance().getText("error.viewShipments.web.service"), e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
			
		});
		
		queryShipments.start();
	}
}
