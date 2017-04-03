/**
 * 
 */
package com.cg.digi.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cg.digi.model.Android_Rates;
import com.cg.digi.model.Basic_Details;
import com.cg.digi.model.Browser_Marketshare;
import com.cg.digi.model.Consolidate_Details;
import com.cg.digi.model.DSDevice_Details;
import com.cg.digi.model.DesktopBrowser_Version;
import com.cg.digi.model.MobileBrowser_Version;
import com.cg.digi.model.OS_Details;
import com.cg.digi.model.OS_Names;
import com.cg.digi.model.Reserve;
import com.cg.digi.model.Vendor_MarketShare;
import com.cg.digi.model.Vendor_Names;
import com.cg.digi.model.iOS_Rates;
/**
 * @author hp818661
 *
 */

public interface IDeviceCloudService {


	//Device Selection Matrix
	public List<Browser_Marketshare>  getBrowserData(); 

	public List<Browser_Marketshare>  getBrowserDataDesktop(); 

	public List<MobileBrowser_Version> getMobileBrowserVersionDetails();
	
	public List<DesktopBrowser_Version> getDesktopBrowserVersionDetails();
	
	public List<Browser_Marketshare>  getBrowserDataUS(); 
	
	public List<Browser_Marketshare>  getBrowserDataUSDesktop(); 
	
	
	public List<Browser_Marketshare>  getBrowserDataUK(); 
	
	public List<Browser_Marketshare>  getBrowserDataUKDesktop(); 
	
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
	public int android_versp(int vn, int nsp);
	public int android_vertb(int vn, int ntb);
	public List<Consolidate_Details> AppleSelectDevices(int os,int vn,String tp,int max);
	public int apple_versp(int vn, int nsp);
	public int apple_vertb(int vn, int ntb);
	public List<Consolidate_Details> AndroidSelectDevices(int os, int vn,String tp,int max);
	public int MWindows_versp(int vns, int nsp);
	public int MWindows_vertb(int vnt, int ntb);
	public List<Consolidate_Details> MWindowsSelectDevices(int os, int vn,String tp,int max);
	public int Rim_versp(int vns, int nsp);
	public int Rim_vertb(int vnt, int ntb);
	public List<Consolidate_Details> RIMSelectDevices(int os, int vn,String tp,int max);
	public boolean TruncateVendor_MarketShare();
	public boolean InsertVendor_MarketShare(String vend_names, String vend_namesm, double data_values);
	public boolean InsertOS_MarketShare(String os_names, String os_namesm, double data_values);
	public boolean Insert_BasicDeviceData(int vend_id,int OS_id,int ver_id,String ver_nm,String model_nm,String tp,String resolution);
	public int SuggestionsPriorityCount(int os, int p,int npd);
	public List<Consolidate_Details> Suggestions(int os, int p,int n);
	public boolean TruncateTemp_Data();
	public List<Vendor_MarketShare> getUKVendorData();
	public List<OS_Details> OSUKDeviceData();
	public boolean TruncateUKVendor_MarketShare();
	public boolean InsertUKVendor_MarketShare(String vend_names, String vend_namesm, double data_values);
	public boolean InsertUKOS_MarketShare(String os_names, String os_namesm, double data_values);
	public int Count_Records();
	public List<Vendor_MarketShare> getGlobalVendorData();
	public List<OS_Details> OSGlobalDeviceData();
	public boolean TruncateGlobalVendor_MarketShare();
	public boolean InsertGlobalVendor_MarketShare(String vend_names,String vend_namesm, double data_values);
	public boolean InsertGlobalOS_MarketShare(String os_names, String os_namesm,double data_values);
	public List<Vendor_MarketShare> getUserVendorData();
	public List<OS_Details> OSUserDeviceData();
	public boolean InsertUserOS_MarketShare(String os_names, String os_namesm,double data_values);
	public boolean InsertUserVendor_MarketShare(String vend_names, String vend_namesm,double data_values);
	public boolean TruncateUserVendor_MarketShare();
	public List<Android_Rates> AndroidRatesDataUser();
	public List<iOS_Rates> iOSRatesDataUser();
	public List<DSDevice_Details> DeviceData();
	public Reserve getCheckMail();
	public boolean addMail(int deviceid, String mailid,String status);

}

