package com.cg.digi.dao;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.cg.digi.logger.DigiLoggerUtils;
import com.cg.digi.logger.DigiLoggerUtils.LEVEL;
import com.cg.digi.model.Account;
import com.cg.digi.model.DeviceCatalog;
import com.cg.digi.model.Market;
import com.cg.digi.model.MobileLabCatalog;
import com.cg.digi.model.Project;
import com.cg.digi.model.Reservation;
import com.cg.digi.model.User;
import com.cg.digi.model.Vendor_MarketShare;

@Component("matrixDao")
public class DeviceSelectionMatrixDaoImpl implements IDeviceSelectionMatrixDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public String getPerfectoReserveIdDao(String deviceId) {

		// System.out.println(deviceId+"Perfecto");
		String query = "SELECT * FROM  mobilelab.reservation WHERE devicecatalogid='"
				+ deviceId + "' and status='Y'";
		Reservation reservation;
		String reservationId = "";
		try {
			reservation = jdbcTemplate.queryForObject(query,
					new BeanPropertyRowMapper<>(Reservation.class));
			reservationId = reservation.getReservationsessionid();
		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"DataAccessException in getPerfectoReserveIdDao() :: " + e,
					LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getPerfectoReserveIdDao() :: "
					+ e, LEVEL.error);
		}
		return reservationId;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Market> getMarkets() {
		String query = "SELECT * FROM  mobilelab.market WHERE status= 'Y'";
		List<Market> markets = null;
		try {
			markets = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					Market.class));
			DigiLoggerUtils.log("Market details in Dao  : " + markets,
					LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getMarkets() :: " + e,
					LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getMarkets() :: " + e,
					LEVEL.error);
		}
		return markets;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DeviceCatalog> getDevices() {
		String query = "SELECT * FROM  mobilelab.device_catalog where status='Y'";
		List<DeviceCatalog> devices = null;
		try {
			devices = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					DeviceCatalog.class));
			DigiLoggerUtils.log("Devices in Dao  : " + devices, LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getDevices() :: " + e,
					LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getDevices() :: " + e,
					LEVEL.error, DeviceSelectionMatrixDaoImpl.class);

		}
		// System.out.println("overall Devices:"+devices);
		return devices;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Vendor_MarketShare> getHighestMarket(String market) {
		List<Vendor_MarketShare> marketData = null;
		String marketTable = "dscglobalvendor_marketshare";
		if (market.equalsIgnoreCase("US")) {
			marketTable = "dscuservendor_marketshare";
		} else if (market.equalsIgnoreCase("UK")) {
			marketTable = "dscukvendor_marketshare";
		} else if (market.equalsIgnoreCase("AUSTRALIA")) {
			marketTable = "dscaustraliavendor_marketshare";
		} else if (market.equalsIgnoreCase("GLOBAL")) {
			marketTable = "dscglobalvendor_marketshare";
		}
		String query = "Select * from ( SELECT  a.Sr_No,a.Vendor_Name,str_to_date(a.Month,'%Y-%m') as Month,a.Value FROM  "
				+ marketTable
				+ " a) as c "
				+ "INNER JOIN       (        SELECT   MAX(str_to_date(Month,'%Y-%m')) mxdate"
				+ " FROM    " + marketTable + " )b  ON  c.Month=b.mxdate";

		try {
			marketData = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					Vendor_MarketShare.class));
			DigiLoggerUtils.log("Market Data in Dao  : " + marketData,
					LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getDevices() :: " + e,
					LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getHighestMarket() :: " + e,
					LEVEL.error, DeviceSelectionMatrixDaoImpl.class);
		}
		return marketData;
	}

	@Override
	public boolean addReservationDetails(Reservation reserve) {
		int result = 0;
		String query = "INSERT INTO mobilelab.reservation(devicecatalogid,reservationsessionid,starttime,endtime,message,status,creationtime,createdby,approvalStatus) VALUES (?,?,?,?,?,'Y',current_time,?,?)";
		Object[] params = new Object[] { reserve.getDevicecatalogid(),
				reserve.getReservationsessionid(), reserve.getStarttime(),
				reserve.getEndtime(), reserve.getMessage(),
				reserve.getCreatedby(), reserve.getApprovalStatus() };
		try {
			result = jdbcTemplate.update(query, params);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error inaddReservationDetails(Reservation reserve) Dao :: "
							+ e.getMessage(), LEVEL.error);
			jdbcTemplate.update("rollback");
		}

		if (result >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean updateDeviceCatalog(String devicecatalogid, String status) {
		int result = 0;
		String query = "UPDATE mobilelab.device_catalog SET availability=? where devicecatalogid=?";
		try {
			Object[] params = new Object[] { status, devicecatalogid };
			result = jdbcTemplate.update(query, params);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error updateDeviceCatalog(String devicecatalogid) Dao :: "
							+ e.getMessage(), LEVEL.error);
			jdbcTemplate.update("rollback");
		}

		if (result >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean updateReservationDetails(String deviceid) {
		int result = 0;
		String query = "UPDATE mobilelab.reservation SET status=?,endtime=current_time where devicecatalogid=? and status=?";
		try {
			Object[] params = new Object[] { "N", deviceid, "Y" };
			result = jdbcTemplate.update(query, params);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error updateReservationDetails(String deviceid) Dao :: "
							+ e.getMessage(), LEVEL.error);
			jdbcTemplate.update("rollback");
		}

		if (result >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean updateDeviceCatalog(JSONArray devices, String vendor) {
		String query = "UPDATE  mobilelab.device_catalog SET  status=? where vendor=?";
		try {

			Object[] params = new Object[] { "N", vendor };
			jdbcTemplate.update(query, params);
			for (Object deviceObj : devices) {
				JSONObject device = (JSONObject) deviceObj;

				// check for availability of device
				if (!checkDevice(device)) {
					// insert new data
					if (!device.get("osVersion").equals("")) {
						addDevice(device, vendor);
					}
				} else {

					query = "UPDATE  mobilelab.device_catalog SET  availability=?, status=? where vendordeviceid=?";
					params = new Object[] {
							device.get("displayStatus").toString(), "Y",
							device.get("id").toString() };
					jdbcTemplate.update(query, params);
				}

			}
		} catch (Exception e) {
			DigiLoggerUtils.log(
					"Error updateDeviceCatalog(JSONArray seetsetDevices) Dao :: "
							+ e.getMessage(), LEVEL.error);
			jdbcTemplate.update("rollback");
		}
		return true;
	}

	private boolean addDevice(JSONObject device, String vendor) {
		int result = 0;
		try {
			String query = "INSERT INTO mobilelab.device_catalog(devicename,model,os,version,devicetype,vendor,manufacturer,status,vendordeviceid,availability) VALUES (?,?,?,?,?,?,?,?,?,?)";
			Object[] params = new Object[] {
					device.get("deviceName").toString(),
					device.get("model").toString(),
					device.get("deviceOs").toString(),
					device.get("osVersion").toString(),
					device.get("deviceCategory").toString(), vendor,
					device.get("manufacturer").toString(), "Y",
					device.get("id").toString(),
					device.get("displayStatus").toString() };
			result = jdbcTemplate.update(query, params);
			DigiLoggerUtils.log("Device Inserted " + device, LEVEL.info);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error updateDeviceCatalog(JSONArray seetsetDevices) Dao :: "
							+ e.getMessage(), LEVEL.error);
		}

		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean checkDevice(JSONObject device) {
		int result = 0;
		try {
			String query = "SELECT COUNT(*) FROM mobilelab.device_catalog where vendordeviceid='"
					+ device.get("id").toString() + "'";
			result = jdbcTemplate.queryForInt(query);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error updateDeviceCatalog(JSONArray seetsetDevices) Dao :: "
							+ e.getMessage(), LEVEL.error);
			jdbcTemplate.update("rollback");
		}

		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Reservation> getReservations(List<String> deviceId,
			String startDate, String endDate) {
		String deviceIdSeries = "";
		for (String string : deviceId) {
			deviceIdSeries += string + ",";
		}
		deviceIdSeries = deviceIdSeries.substring(0,
				deviceIdSeries.lastIndexOf(","));
		String query = "SELECT reservationid,d.devicename,r.devicecatalogid,reservationsessionid,date(r.starttime) as starttime,date(r.endtime) as endtime,message,r.status,creationtime,createdby,modifiedby,modifiedtime,approvalStatus"
				+ " FROM  mobilelab.reservation r, mobilelab.device_catalog d where (date(r.starttime)>='"
				+ startDate
				+ "' AND date(r.endtime)<='"
				+ endDate
				+ "') and r.status='Y' and d.devicecatalogid=r.devicecatalogid and r.devicecatalogid IN ("
				+ deviceIdSeries + ")";
		System.out.println(query);
		List<Reservation> reservations = null;
		try {
			reservations = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					Reservation.class));
			DigiLoggerUtils.log("getReservations in Dao  : " + reservations,
					LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getReservations() :: "
					+ e, LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getReservations() :: " + e,
					LEVEL.error, DeviceSelectionMatrixDaoImpl.class);

		}
		System.out.println("overall Devices:" + reservations);
		return reservations;
	}

	@Override
	public DeviceCatalog getDeviceDetails(String devicecatalogid) {
		String query = "SELECT * FROM  mobilelab.device_catalog WHERE devicecatalogid='"
				+ devicecatalogid + "' and status='Y'";
		DeviceCatalog device = null;
		try {
			device = jdbcTemplate.queryForObject(query,
					new BeanPropertyRowMapper<>(DeviceCatalog.class));
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getDeviceDetails() :: "
					+ e, LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getDeviceDetails() :: " + e,
					LEVEL.error);
		}
		return device;

	}

	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DeviceCatalog> getdeviceRequests() {
		String query = "SELECT reservationid,d.devicename,r.devicecatalogid,d.os,d.version,d.resolution,d.vendor,date(r.starttime) as starttime,date(r.endtime) as endtime,r.approvalStatus,u.username as createdby "
				+ "from mobilelab.device_catalog d,mobilelab.reservation r,digiassure.users u where d.devicecatalogid=r.devicecatalogid and r.status='Y' and r.approvalStatus='Requested' and u.userid=r.createdby ";
		List<DeviceCatalog> catalog = null;
		try {
			catalog = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					DeviceCatalog.class));
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getdeviceRequests()"
					+ e.getMessage(), DigiLoggerUtils.LEVEL.error,
					this.getClass());
		} catch (Exception e) {
e.printStackTrace();
		}
		System.out.println("catalog:"+catalog);
		return catalog;
	}*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<MobileLabCatalog> getdeviceRequests() {
		String query = "SELECT reservationid,d.devicename,r.devicecatalogid,d.os,d.version,d.resolution,d.vendor,date(r.starttime) as starttime,date(r.endtime) as endtime,r.approvalStatus,u.username as createdby "
				+ "from mobilelab.device_catalog d,mobilelab.reservation r, users u where d.devicecatalogid=r.devicecatalogid and r.status='Y' and r.approvalStatus='Requested' and u.userid=r.createdby ";
		List<MobileLabCatalog> catalog = null;
		try {
			catalog = jdbcTemplate.query(query, new BeanPropertyRowMapper(MobileLabCatalog.class));
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getdeviceRequests()"+e.getMessage(), DigiLoggerUtils.LEVEL.error , this.getClass());
		}catch(Exception e){

		}
		return catalog;
	}

	@Override
	public boolean updateDeviceReservations(String reservationid,
			String status, String comment) {
		int result = 0;
		try {
			String query = "UPDATE  mobilelab.reservation SET  approvalStatus=?,message=? where reservationid=?";
			Object[] params = new Object[] { status, comment, reservationid };
			result = jdbcTemplate.update(query, params);
		} catch (DataAccessException e) {
			DigiLoggerUtils
			.log("DataAccessException in updateDeviceReservations(String reservationid, String status)"
					+ e.getMessage(), DigiLoggerUtils.LEVEL.error,
					this.getClass());
		} catch (Exception e) {

		}
		if (result > 0) {
			return true;
		}

		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DeviceCatalog> getLabDevices() {
		// String query =
		// "SELECT * FROM  mobilelab.device_catalog where status='Y' and vendor='Lab'";

		String query = "SELECT devicecatalogid,devicename,model,os,version,launchdate,resolution,devicetype,vendor,manufacturer,status,vendordeviceid,availability,imei,simno,location,"
				+ "browsers,connectivity,remarks,procurementdate, case when belongsto='0' then 'none' else (select projectname from project where projectid=belongsto) end as belongsto"
				+ "	FROM mobilelab.device_catalog d where vendor='Lab' and status='Y' ";

		List<DeviceCatalog> devices = null;
		try {
			devices = jdbcTemplate.query(query, new BeanPropertyRowMapper(
					DeviceCatalog.class));
			DigiLoggerUtils.log("Devices in Dao  : " + devices, LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in getDevices() :: " + e,
					LEVEL.error);
		} catch (Exception e) {
			DigiLoggerUtils.log("Exception in getDevices() :: " + e,
					LEVEL.error, DeviceSelectionMatrixDaoImpl.class);

		}
		return devices;
	}

	@Override
	public boolean addDeviceDetails(DeviceCatalog device, String vendor) {
		int result = 0;
		String browsers = "";
		for (String browser : device.getBrowsers()) {
			browsers += browser + ",";
		}

		try {
			String query = "INSERT INTO mobilelab.device_catalog(devicename,model,os,version,devicetype,vendor,manufacturer,status,vendordeviceid,availability,location,simno,browsers,connectivity,remarks,procurementdate,belongsto,imei) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] params = new Object[] { device.getDevicename(),
					device.getModel(), device.getOs(), device.getVersion(),
					device.getDevicetype(), vendor, device.getManufacturer(),
					"Y", device.getVendordeviceid(), "Available In Lab",
					device.getLocation(), device.getSimno(), browsers,
					device.getConnectivity(), device.getRemarks(),
					device.getProcurementdate(), device.getBelongsto(),
					device.getImei() };
			result = jdbcTemplate.update(query, params);
			DigiLoggerUtils.log("Device added: " + device, LEVEL.info);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error addDeviceDetails(DeviceCatalog  device, String vendor) Dao :: "
							+ e.getMessage(), LEVEL.error);
		}

		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteDevice(String devicecatalogid) {
		String query = "UPDATE  mobilelab.device_catalog SET  status=? where devicecatalogid=?";
		int result = 0;
		try {

			Object[] params = new Object[] { "N", devicecatalogid };
			result = jdbcTemplate.update(query, params);
		} catch (Exception e) {

		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean updateDeviceCatalog(DeviceCatalog device) {
		String browsers = "";
		for (String browser : device.getBrowsers()) {
			browsers += browser + ",";
		}
		String query = "UPDATE  mobilelab.device_catalog SET  devicecatalogid=?,devicename=?,model=?,os=?,version=?,launchdate=?,resolution=?,devicetype=?,vendor=?,manufacturer=?,status=?,vendordeviceid=?,availability=?,imei=?,simno=?,belongsto=?,location=?,browsers=?,connectivity=?,remarks=?,procurementdate=? where devicecatalogid=?";
		int result = 0;
		try {

			Object[] params = new Object[] { device.getDevicecatalogid(),
					device.getDevicename(), device.getModel(), device.getOs(),
					device.getVersion(), device.getLaunchdate(),
					device.getResolution(), device.getDevicetype(),
					device.getVendor(), device.getManufacturer(), "Y",
					device.getVendordeviceid(), "Available In Lab",
					device.getImei(), device.getSimno(), device.getBelongsto(),
					device.getLocation(), browsers, device.getConnectivity(),
					device.getRemarks(), device.getProcurementdate(),
					device.getDevicecatalogid() };
			result = jdbcTemplate.update(query, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<MobileLabCatalog> getAllRequestsDao() {



		String query="SELECT d.devicename,d.os,d.version,d.resolution,d.vendor,r.starttime,r.endtime,r.approvalStatus,r.createdby from mobilelab.device_catalog d,mobilelab.reservation r where d.devicecatalogid=r.devicecatalogid and r.status='Y'";
		List<MobileLabCatalog> catalog= jdbcTemplate.query(query, new BeanPropertyRowMapper(MobileLabCatalog.class));

		return catalog;
	}

	@Override
	public List<MobileLabCatalog> getAllRequestsDao(String createdby) {
		String query="SELECT d.devicename,d.os,d.version,d.resolution,d.vendor,r.starttime,r.endtime,r.approvalStatus,r.createdby from mobilelab.device_catalog d,mobilelab.reservation r where d.devicecatalogid=r.devicecatalogid and r.status='Y' and r.createdby='"+createdby+"'";
		List<MobileLabCatalog> catalog= jdbcTemplate.query(query, new BeanPropertyRowMapper(MobileLabCatalog.class));

		return catalog;
	}


	@Override
	public List<MobileLabCatalog> getUserRequestedBookingsDao(String createdby) {
		String query="SELECT d.devicename,d.os,d.version,d.resolution,d.vendor,r.starttime,r.endtime,r.approvalStatus,r.createdby from mobilelab.device_catalog d,mobilelab.reservation r where d.devicecatalogid=r.devicecatalogid and r.status='Y' and r.approvalStatus='requested' and r.createdby='"+createdby+"'";
		List<MobileLabCatalog> catalog= jdbcTemplate.query(query, new BeanPropertyRowMapper(MobileLabCatalog.class));

		return catalog;
	}

	@Override
	public List<MobileLabCatalog> getAllRequestedBookingsDao() {
		String query="SELECT d.devicename,d.os,d.version,d.resolution,d.vendor,r.starttime,r.endtime,r.approvalStatus,r.createdby from mobilelab.device_catalog d,mobilelab.reservation r where d.devicecatalogid=r.devicecatalogid and r.status='Y' and r.approvalStatus='requested'";
		List<MobileLabCatalog> catalog= jdbcTemplate.query(query, new BeanPropertyRowMapper(MobileLabCatalog.class));

		return catalog;
	}
	@Override
	public List<User> viewUsersDao() {

		String query = "SELECT u.firstname,u.lastname,u.username,u.phonenumber,u.email,r.rolename from users u, roles r where r.roleid=u.roleid and u.status='Y'";
		List<User> users = jdbcTemplate.query(query,
				new BeanPropertyRowMapper(User.class));
		return users;
	}

	@Override
	public boolean addUserDao(User user) {

		try {
			String query2 = "SELECT roleid from roles where rolename='"
					+ user.getRolename()+"'";
			int roleId = jdbcTemplate.queryForInt(query2);
			String query = "INSERT INTO users(username,userpassword,firstname,lastname,email,phonenumber,roleid,status,projectid) VALUES (?,?,?,?,?,?,?,?,?)";
			Object[] params = new Object[] { user.getUserName(),
					user.getUserPassword(), user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber(), roleId,"Y" ,user.getProjectid()};

			int result = jdbcTemplate.update(query, params);
			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	///////////	/////////////PROJETCS	////////////	////////////

	@Override
	public List<Project> viewProjectsDao() {
		String query="SELECT p.projectid,p.projectname,p.projectdescription,p.startdate,p.enddate,p.status,a.accountname from digiassureschema.project p, digiassureschema.accounts a where a.accountid=p.accountid and p.status='Y' ORDER BY p.projectname ASC";
		List<Project> projects=jdbcTemplate.query(query, new BeanPropertyRowMapper(Project.class));
		return projects;
	}

	@Override
	public boolean deleteProjectDao(String projectId) {

		String query="UPDATE project SET  status='N' where projectid='"+projectId+"'";
		int result=jdbcTemplate.update(query);
		if(result>0)
			return true;
		else
			return false;
	}

	@Override
	public boolean checkProjectDao(String projectname) {
		int result=0;
		try {
			String query="SELECT COUNT(*) FROM project where status='Y' and projectname='"+projectname.toLowerCase()+"'";
			result=	 jdbcTemplate.queryForInt(query);

		} catch (DataAccessException e) {
			DigiLoggerUtils.log("Error checkUserDAo Dao :: " + e.getMessage(), LEVEL.error);

		}

		if (result >0) {
			return true;	
		} else {
			return false;
		}
	}

	@Override
	public boolean addProjectDao(Project project) {
		String query2="SELECT accountid from accounts where  accountname='"+project.getAccountname().toLowerCase()+"'";
		int accountId=jdbcTemplate.queryForInt(query2);

		String query="INSERT INTO project(projectname,projectdescription,startdate,enddate,status,accountid,creationtime) VALUES (?,?,?,?,'Y',?,current_time)";
		Object[] params = new Object[] {project.getProjectName().toLowerCase(),project.getProjectDescription(),project.getStartDate(),project.getEndDate(),accountId};

		int result=	 jdbcTemplate.update(query,params);
		if (result >0) {
			return true;	
		} else {
			return false;
		}
	}

	@Override
	public boolean editProjectDao(Project project) {
		int result = 0;
		String query1="SELECT accountid from accounts where status='Y' and accountname='"+project.getAccountname()+"'";
		int accountId=jdbcTemplate.queryForInt(query1);
		String query2="UPDATE project set projectdescription=?, startdate=?,enddate=?,accountid=? where projectname=?";
		try {

			Object[] params = new Object[] {project.getProjectDescription(),project.getStartDate(),project.getEndDate(),accountId,project.getProjectName()};
			result= jdbcTemplate.update(query2,params);
		}catch(Exception e){

		}
		if(result>0)
			return true;
		else
			return false;
	}

	@Override
	public List<Account> getAccountsDao() {
		String query="Select * from digiassureschema.accounts where status='Y'";
		List<Account> accounts=jdbcTemplate.query(query, new BeanPropertyRowMapper(Account.class));
		return accounts;
	}




}
