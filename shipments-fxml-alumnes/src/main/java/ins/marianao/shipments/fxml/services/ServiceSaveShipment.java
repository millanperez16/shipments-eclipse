package ins.marianao.shipments.fxml.services;

import cat.institutmarianao.shipmentsws.model.Shipment;

public class ServiceSaveShipment extends ServiceSaveBase<Shipment> {

	private static final String PATH_INSERT_SHIPMENT = "save";
	private static final String PATH_UPDATE_SHIPMENT = "update";
	
	public ServiceSaveShipment(Shipment shipment, boolean insert) throws Exception {
		super(shipment, Shipment.class, new String[] {
				ServiceQueryShipments.PATH_REST_SHIPMENTS,
				insert?PATH_INSERT_SHIPMENT:PATH_UPDATE_SHIPMENT }, insert?Method.POST:Method.PUT);
	}
	
}


