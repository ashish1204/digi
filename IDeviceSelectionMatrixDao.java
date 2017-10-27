package com.cg.digi.dao;

import java.util.List;

import org.json.simple.JSONArray;

import com.cg.digi.model.Account;
import com.cg.digi.model.DeviceCatalog;
import com.cg.digi.model.Market;
import com.cg.digi.model.MobileLabCatalog;
import com.cg.digi.model.Project;
import com.cg.digi.model.Reservation;
import com.cg.digi.model.User;
import com.cg.digi.model.Vendor_MarketShare;

public interface IDeviceSelectionMatrixDao {	public List<Market> getMarkets();

public List<DeviceCatalog> getDevices();

public List<Vendor_MarketShare> getHighestMarket(String market);

public boolean addReservationDetails(Reservation reserve);

public boolean updateDeviceCatalog(String devicecatalogid,String status);

public boolean updateReservationDetails(String deviceid);


boolean updateDeviceCatalog(JSONArray seetsetDevices, String vendor);
public String getPerfectoReserveIdDao(String deviceId);

public List<Reservation> getReservations(List<String> deviceId, String startDate,
		String endDate);

public DeviceCatalog getDeviceDetails(String devicecatalogid);

public List<MobileLabCatalog> getdeviceRequests();


public boolean updateDeviceReservations(String reservationid, String status,
		String comment);

public List<DeviceCatalog> getLabDevices();

public boolean addDeviceDetails(DeviceCatalog device,String vendor);

public boolean deleteDevice(String devicecatalogid);

public boolean updateDeviceCatalog(DeviceCatalog device);

//ALL REQUESTS
public List<MobileLabCatalog> getAllRequestsDao();
public List<MobileLabCatalog> getAllRequestsDao(String createdby);

//REQUESTED BOOKINGS

public List<MobileLabCatalog> getUserRequestedBookingsDao(String createdby);

public List<MobileLabCatalog> getAllRequestedBookingsDao();

//USERS
public List<User> viewUsersDao();
boolean addUserDao(User user);

/////////PROJECTS////////

public List<Project> viewProjectsDao();
public boolean deleteProjectDao(String projectId);
public boolean checkProjectDao(String projectname);
public boolean addProjectDao(Project project);
public boolean editProjectDao(Project project);
public List<Account> getAccountsDao();


}
