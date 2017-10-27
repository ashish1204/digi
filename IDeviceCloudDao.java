package com.cg.digi.dao;

import java.util.List;

import com.cg.digi.model.AndroidDistribution;
import com.cg.digi.model.Android_Rates;
import com.cg.digi.model.Basic_Details;
import com.cg.digi.model.Consolidate_Details;
import com.cg.digi.model.DSDevice_Details;
import com.cg.digi.model.DesktopBrowserDistribution;
import com.cg.digi.model.DesktopBrowserVersionDistribution;
import com.cg.digi.model.File;
import com.cg.digi.model.IosDistribution;
import com.cg.digi.model.Market;
import com.cg.digi.model.MobileBrowserDistribution;
import com.cg.digi.model.MobileBrowserVersionDistribution;
import com.cg.digi.model.ORSheetDetails;
import com.cg.digi.model.OS_Details;
import com.cg.digi.model.OS_Names;
import com.cg.digi.model.OemDistribution;
import com.cg.digi.model.OsDistribution;
import com.cg.digi.model.Reserve;
import com.cg.digi.model.User;
import com.cg.digi.model.UserOemDistribution;
import com.cg.digi.model.UserOsDistribution;
import com.cg.digi.model.Vendor_MarketShare;
import com.cg.digi.model.Vendor_Names;
import com.cg.digi.model.iOS_Rates;



public interface IDeviceCloudDao {
	
	///MARKET
	public List<Market> getMarketsDao();

	//Device Trends
	
	public List<OemDistribution> getOEMDistributionDao(int id);
	public List<OsDistribution> getOsDistributionDao(int id);
	public List<AndroidDistribution> getAndroidDistributionDao();
	public List<IosDistribution> getIosDistributionDao();

	
	//BrowserTrends
	
	public List<MobileBrowserDistribution> getMobileBrowserDistributionDao(int id);
	public List<DesktopBrowserDistribution> getDesktopBrowserDistributionDao(int id);
	public List<MobileBrowserVersionDistribution> getMobileBrowserVersionDistributionDao(String name);
	public List<DesktopBrowserVersionDistribution> getDesktopBrowserVersionDistributionDao(String name);
	
	
	
	//USER TRENDS
	public List<UserOemDistribution> getUserOemDistributionDao();
	public List<UserOsDistribution> getUserOsDistributionDao();
	
	
	//UPDATE DEVICE TRENDS
	public String updateDeviceTrendsDao(List<OemDistribution>listOEM,List<OsDistribution> listOS);
	
	//UPDATE ROWSER TRENDS
	public String updateBrowserTrendsDao(List<MobileBrowserDistribution>listMobileBrowser,List<DesktopBrowserDistribution> listDesktopBrowser);

	
	//UPDATE USER TRENDS
	public String updateUserTrendsDao(List<UserOemDistribution>listOEM,List<UserOsDistribution> listOS);

	
	///UPDATE BROWSER GLOBAL
	public boolean truncateBrowserMarketshareDao();	
	public boolean insertBrowserMarketshareDao(String browserName,String month, double value);
	public boolean insertBrowserDesktopMarketshareDao(String browserDeskName,String month, double value);

	///UPDATE BROWSER  US
	public boolean truncateBrowserUSMarketshareDao();
	public boolean insertBrowserUSMarketshareDao(String browserName,String month, double value);
	public boolean insertBrowserUSDesktopMarketshareDao(String browserDeskName,String month, double value);

	
	//UPDATE BROWSER UK
	public boolean truncateBrowserUKMarketshareDao();
	public boolean insertBrowserUKMarketshareDao(String browserName,String month, double value);
	public boolean insertBrowserUKDesktopMarketshareDao(String browserDeskName,String month, double value);
	
	
	
	public User loginValidation(String username, String password);

	public boolean checkEmail(String email);

	public boolean addUser(User userDetails);

	public boolean activateUser(String userName);

	public boolean updatePassword(String userName, String userPassword);

	
	//methods of Device Selection Matrix	

	public List<Vendor_MarketShare> getVendorData();
	public List<Basic_Details> BasicDeviceData();
	public boolean BasicToConsolidatedData();
	public List<OS_Details> OSDeviceData();
	public List<Android_Rates> AndroidRatesData();
	public List<iOS_Rates> iOSRatesData();
	public List<Consolidate_Details> ConsolidatedData();
	public boolean updateData(Consolidate_Details cd);
	public List<Vendor_Names> DisplayVendor_Names();
	public List<OS_Names> DisplayOS_Names();
	public int apple_versp(int vn, int nsp);
	public int apple_vertb(int vn, int ntb);
	public List<Consolidate_Details> AppleSelectDevices(int os,int vn,String tp,int max);
	public int android_versp(int vn, int nsp);
	public int android_vertb(int vn, int ntb);
	public List<Consolidate_Details> AndroidSelectDevices(int os,int vn,String tp,int max);
	public int MWindows_versp(int vns, int nsp);
	public int MWindows_vertb(int vnt, int ntb);
	public List<Consolidate_Details> MWindowsSelectDevices(int os,int vn,String tp,int max);
	public int Rim_versp(int vns, int nsp);
	public int Rim_vertb(int vnt, int ntb);
	public List<Consolidate_Details> RIMSelectDevices(int os,int vn,String tp,int max);
	public boolean TruncateVendor_MarketShare();
	public boolean InsertVendor_MarketShare(String vend_names, String vend_namesm, double data_values);
	public boolean InsertOS_MarketShare(String os_names, String os_namesm, double data_values);
	public boolean Insert_BasicDeviceData(int vend_id,int OS_id,int ver_id,String ver_nm,String model_nm,String tp,String resolution);
	public int SuggestionsPriorityCount(int os, int p,int npd);
	public List<Consolidate_Details> Suggestions(int os, int p,int n);
	public boolean TruncateTemp_Data();
	public List<Vendor_MarketShare> getUKVendorData();
	public List<Vendor_MarketShare> getGlobalVendorData();
	public List<OS_Details> OSUKDeviceData();
	public List<OS_Details> OSGlobalDeviceData();
	public boolean TruncateUKVendor_MarketShare();
	public boolean InsertUKVendor_MarketShare(String vend_names, String vend_namesm, double data_values);
	public boolean InsertUKOS_MarketShare(String os_names, String os_namesm, double data_values);
	public int Count_Records();

	public boolean addFileDetails(File fileDetails);

	public List<ORSheetDetails> getORSheetDetails(String userId);

	public boolean TruncateGlobalVendor_MarketShare();

	public boolean InsertGlobalVendor_MarketShare(String vend_names,String vend_namesm, double data_values);

	public boolean InsertGlobalOS_MarketShare(String os_names,String os_namesm, double data_values);

	public List<Vendor_MarketShare> getUserVendorData();

	public List<OS_Details> OSUserDeviceData();

	public boolean InsertUserVendor_MarketShare(String vend_names, String vend_namesm,double data_values);

	public boolean TruncateUserVendor_MarketShare();

	public boolean InsertUserOS_MarketShare(String os_names, String os_namesm,double data_values);

	public List<Android_Rates> AndroidRatesDataUser();

	public List<iOS_Rates> iOSRatesDataUser();

	public int updateReservationId(String deviceId , String reservationId);
	public String getDeviceResevationId(String device_id);
	
	//methods of perfecto
	public int addPerfectoDevices(String deviceId , String reservationId);
	public int addDevices(int id , String vendor, String device_id, String name ,String version , String os , String status , String reservation , String reservation_id, String udid,String deviceCategory,String location, String resolution);
	public int deleteAllVendorDevice(String vendor);	
	public String getPerfectoResevationId(String device_id);
	public int deletePerfectoDevice(String device_id);

	public List<DSDevice_Details> DeviceData();

	public int updateReservationtime(String deviceId, String time);

	public int addReservationTime(String deviceId, String reservation_time);

	public int updateReservationTimeDB();

	public int deleteReservationTime(String device_id);
	public boolean addMail(int deviceid, String mailid, String status);

	public Reserve getCheckMail();
	
}

