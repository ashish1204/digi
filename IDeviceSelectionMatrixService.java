package com.cg.digi.service;

import java.util.List;

import com.cg.digi.model.Account;
import com.cg.digi.model.DeviceCatalog;
import com.cg.digi.model.Market;
import com.cg.digi.model.MobileLabCatalog;
import com.cg.digi.model.Project;
import com.cg.digi.model.Reservation;
import com.cg.digi.model.User;
import com.cg.digi.model.Vendor_MarketShare;

public interface IDeviceSelectionMatrixService {

	public List<Market> getMarkets();

	public List<DeviceCatalog> getDevices();

	public List<Vendor_MarketShare> getHighestMarket(String market);

	public List<DeviceCatalog> getRecommendedDevices(String market, String count);

	boolean addReservationDetails(Reservation reserve);

	public boolean updateReservationDetails(String deviceid);

	public boolean updateDeviceCatalog();

	public String getPerfectoReserveId(String deviceId);


	public boolean bookDevice(Reservation reservation);

	public List<MobileLabCatalog> getdeviceRequests();


	public boolean updateDeviceReservations(String reservationid,
			String status, String comment);

	public List<DeviceCatalog> getLabDevices();


	boolean addDeviceDetails(DeviceCatalog device, String vendor);

	public DeviceCatalog getDeviceDetails(String devicecatalogid);

	public boolean deleteDevice(String devicecatalogid);

	public boolean updateDeviceCatalog(DeviceCatalog device);

	//ALL REQUESTSS
		public List<MobileLabCatalog> getAllRequests();
		public List<MobileLabCatalog> getAllRequests(String createdby);
		
		//REQUESTED BOOKINGS
		public List<MobileLabCatalog> getAllRequestedBookings();
		public List<MobileLabCatalog> getUserRequestedBookings(String createdby);

		///USERS
		
		public List<User> viewUsers();
		public boolean addUser(User user);

	public	List<Reservation> getReservations(List<String> deviceId,
				String startDate, String endDate);

	public List<Reservation> getAllReservationsDetails();
	
	///PROJECTS
			public List<Project> viewProjects();
			public boolean deleteProject(String projectId);
			public boolean checkProject(String userName);
			public boolean addProject(Project project);
			public boolean editProject(Project project);
			public List<Account> getAccounts();

			

}
