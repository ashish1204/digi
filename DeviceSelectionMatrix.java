package com.cg.digi.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cg.digi.model.AndroidDistribution;
import com.cg.digi.model.Android_Rates;
import com.cg.digi.model.Basic_Details;
import com.cg.digi.model.Consolidate_Details;
import com.cg.digi.model.DSDevice_Details;
import com.cg.digi.model.DesktopBrowserDistribution;
import com.cg.digi.model.DesktopBrowserVersionDistribution;
import com.cg.digi.model.Device;
import com.cg.digi.model.Handset;
import com.cg.digi.model.IosDistribution;
import com.cg.digi.model.Market;
import com.cg.digi.model.MobileBrowserDistribution;
import com.cg.digi.model.MobileBrowserVersionDistribution;
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
import com.cg.digi.service.DeviceCloudService;
import com.cg.digi.service.IAdminService;
import com.cg.digi.service.IDeviceCloudService;
import com.cg.digi.service.ILoginService;
import com.cg.digi.service.ReleaseSM;
import com.cg.digi.utilities.ReadExcelUtility;
import com.cg.digi.utilities.ExcelUtil;
import com.cg.digi.utilities.FileUtils;
import com.cg.digi.utilities.PerfectoAPI1;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@Scope("session")
@Controller
public class DeviceSelectionMatrix {

	@Value("${mobileLabUpdateFilePath}")
	String mobileLabUpdateFilePath;


	@Value("${macroFile}")
	String macroFile;	
	@Autowired
	ILoginService loginService;
	@Autowired
	IAdminService adminService;
	@Autowired
	DeviceCloudService deviceCloudService;


	//UPDATE USER TRENDS
	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/updateUserTrends")
	public @ResponseBody String updateUserTrends(Model model,@RequestParam (value="file",required=false)MultipartFile file){

		File convFile = new File(file.getOriginalFilename());
		System.out.println(convFile);
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch (IOException e) {

			e.printStackTrace();
		} 


		File finalPath=new File(mobileLabUpdateFilePath+"\\userTrends");
		finalPath.mkdirs();

		File fileCopy=new File(mobileLabUpdateFilePath+"\\userTrends\\"+convFile.getName());
		FileUtils.copyFile(convFile, fileCopy);
		List<UserOemDistribution> ListOEM=  ReadExcelUtility.readUserOEM(fileCopy);
		List<UserOsDistribution> ListOS= ReadExcelUtility.readUserOS(fileCopy);

		String result= deviceCloudService.updateUserTrends(ListOEM,ListOS);
		JsonObject obj=new JsonObject();
		obj.addProperty("Result",result);
		return obj.toString();

	}


	//UPDATE DEVICES TRENDS
	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/updateDeviceTrends")
	public @ResponseBody String updateDeviceTrends(Model model,@RequestParam (value="file",required=false)MultipartFile file,String id){

		File convFile = new File(file.getOriginalFilename());
		id=id.replaceAll("\"","");
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch (IOException e) {

			e.printStackTrace();
		} 
		File finalPath=new File(mobileLabUpdateFilePath+"\\deviceTrends\\"+id);
		finalPath.mkdirs();

		File fileCopy=new File(mobileLabUpdateFilePath+"\\deviceTrends\\"+id+"\\"+convFile.getName());
		System.out.println(fileCopy.getAbsolutePath());

		FileUtils.copyFile(convFile, fileCopy);

		List<OemDistribution> ListOEM=  ReadExcelUtility.readOEM(fileCopy,id);
		List<OsDistribution> ListOS= ReadExcelUtility.readOS(fileCopy,id);
		String result= deviceCloudService.updateDeviceTrends(ListOEM,ListOS);
		JsonObject obj=new JsonObject();
		obj.addProperty("Result",result);
		obj.addProperty("ID", id);
		return obj.toString();

	}

	//UPDATE BROWSER TRENDS
	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/updateBrowserTrends")
	public @ResponseBody String updateBrowserTrends(Model model,@RequestParam (value="file",required=false)MultipartFile file,String id){

		File convFile = new File(file.getOriginalFilename());
		id=id.replaceAll("\"","");
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch (IOException e) {

			e.printStackTrace();
		} 
		File finalPath=new File(mobileLabUpdateFilePath+"\\browserTrends\\"+id);
		finalPath.mkdirs();

		File fileCopy=new File(mobileLabUpdateFilePath+"\\browserTrends\\"+id+"\\"+convFile.getName());
		
		FileUtils.copyFile(convFile, fileCopy);
		
		List<MobileBrowserDistribution> ListMobileBrowser=  ReadExcelUtility.readMobileBrowser(fileCopy,id);
		 List<DesktopBrowserDistribution> ListDesktopBrowser= ReadExcelUtility.readDesktopBrowser(fileCopy, id);
		//System.out.println(ListOS);
		 String result= deviceCloudService.updateBrowserTrends(ListMobileBrowser,ListDesktopBrowser);
		JsonObject obj=new JsonObject();
		obj.addProperty("Result",result);
		obj.addProperty("ID", id);
		return obj.toString();

	}



	//GET MARKETS

	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/getMarkets")
	public @ResponseBody String getMarkets()throws Exception{

		List<Market> markets=deviceCloudService.getMarkets();
		String json = new Gson().toJson(markets );
		return json;	
	}


	//GET DEVICES TRENDS

	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/getDevicesData",method = RequestMethod.POST)

	public @ResponseBody String getMarketData(Model model,@RequestBody String id)throws Exception{


		int marketID=Integer.parseInt(""+id.charAt(5));
		List<OemDistribution> oemDistribution=deviceCloudService.getOEMDistribution(marketID);
		int length=oemDistribution.size();
		String month[]=new String[length];
		int size=0;
		String mon = " ";
		for(OemDistribution obj : oemDistribution){
			mon = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-05", "-May");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-06", "-June");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-07", "-July");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(mon);
			month[size]=obj.getMonth();
			size++;
		}

		int end = month.length;
		//System.out.println(end);
		int c=0;
		for (int i = 0; i < end; i++) {
			for (int j = i + 1; j < end; j++) {
				if (month[i].equalsIgnoreCase(month[j])) {                  
					c++;
					month[j] = month[j + 1];// shifting the values
					j--;

					end--;

				}
			}
		}



		double appleVal[]=new double[length];
		int appleCounter=0;
		for(OemDistribution obj:oemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Apple")){

				if(appleCounter<=length-1)
					appleVal[appleCounter]=obj.getValue();

				appleCounter++;
			}
		}

		double lgVal[]=new double[length];
		int lgCounter=0;
		for(OemDistribution obj:oemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Lg")){

				if(lgCounter<=length-1)
					lgVal[lgCounter]=obj.getValue();

				lgCounter++;
			}
		}

		double googleVal[]=new double[length];
		int googleCounter=0;
		for(OemDistribution obj:oemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Google")){

				if(googleCounter<=length-1)
					googleVal[googleCounter]=obj.getValue();

				googleCounter++;
			}
		}

		double samsungVal[]=new double[length];
		int samsungCounter=0;
		for(OemDistribution obj:oemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Samsung")){

				if(samsungCounter<=length-1)
					samsungVal[samsungCounter]=obj.getValue();

				samsungCounter++;
			}
		}

		double motoVal[]=new double[length];
		int motoCounter=0;
		for(OemDistribution obj:oemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Motorola")){

				if(motoCounter<=length-1)
					motoVal[motoCounter]=obj.getValue();

				motoCounter++;
			}
		}


		List<AndroidDistribution> androidDistribution=deviceCloudService.getAndroidDistribution();

		List<IosDistribution> iosDistribution = deviceCloudService.getIosDistribution();

		List<OsDistribution> osDistribution =deviceCloudService.getOsDistribution(marketID);
		int os_length=osDistribution.size();
		String month_os[]=new String[os_length];
		int size_os=0;
		String mon_os = " ";
		for(OsDistribution obj : osDistribution){
			mon_os = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-05", "-May");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-06", "-June");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-07", "-July");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(mon_os);
			month_os[size_os]=obj.getMonth();
			size_os++;

		}

		int end_os = month_os.length;
		int c_os=0;
		for (int i = 0; i < end_os; i++) {
			for (int j = i + 1; j < end_os; j++) {
				if (month_os[i].equalsIgnoreCase(month_os[j])) {                  
					c_os++;
					month_os[j] = month_os[j + 1];// shifting the values
					j--;

					end_os--;

				}
			}
		}

		double iosVal[]=new double[os_length];
		int iosCounter=0;
		for(OsDistribution obj:osDistribution){
			if((obj.getOs()).equalsIgnoreCase("IOS"))
			{

				if(iosCounter<=os_length-1){
					iosVal[iosCounter]=obj.getValue();
					iosCounter++;
				}


			}
		}

		double androidVal[]=new double[os_length];
		int androidCounter=0;
		for(OsDistribution obj:osDistribution){
			if((obj.getOs()).equalsIgnoreCase("Android"))
			{

				if(androidCounter<=os_length-1){
					androidVal[androidCounter]=obj.getValue();
					androidCounter++;
				}


			}
		}

		double windowsVal[]=new double[os_length];
		int windowsCounter=0;
		for(OsDistribution obj:osDistribution){
			if((obj.getOs()).equalsIgnoreCase("Windows Phone"))
			{

				if(windowsCounter<=os_length-1){
					windowsVal[windowsCounter]=obj.getValue();
					windowsCounter++;
				}


			}
		}

		System.out.println("----------------");


		///////////////////	

		JsonArray oemArray=new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month",month[i]);
			obj.addProperty("appleData", appleVal[i]);
			obj.addProperty("lgData", lgVal[i]);
			obj.addProperty("googleData", googleVal[i]);
			obj.addProperty("samsungData", samsungVal[i]);
			obj.addProperty("motoData", motoVal[i]);
			oemArray.add(obj);
		}

		JsonArray androidArray=new JsonArray();
		for(AndroidDistribution obj : androidDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version",obj.getVersion());
			obj2.addProperty("value",obj.getRate());
			androidArray.add(obj2);
		}

		JsonArray iosArray=new JsonArray();
		for(IosDistribution obj : iosDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version",obj.getIosVersion());
			obj2.addProperty("value",obj.getRate());
			iosArray.add(obj2);
		}

		JsonArray osArray=new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month",month_os[i]);
			obj.addProperty("iosData", iosVal[i]);
			obj.addProperty("androidData", androidVal[i]);
			obj.addProperty("windowsData", windowsVal[i]);

			osArray.add(obj);
		}

		JsonObject finalObject =new JsonObject();
		finalObject.add("tableOEM", oemArray);
		finalObject.add("pieAndroid", androidArray);
		finalObject.add("pieIOS", iosArray);
		finalObject.add("tableOS", osArray);
		//	System.out.println(finalObject);

		////////////////////////////////




		return finalObject.toString();

	}


	//GET BROWSER TRENDS

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/getBrowserData",method = RequestMethod.POST)

	public @ResponseBody String getBrowserData(Model model,@RequestBody String id)throws Exception{

		int marketID=Integer.parseInt(""+id.charAt(5));
		List<MobileBrowserDistribution> mobileBrowserDistribution=deviceCloudService.getMobileBrowserDistribution(marketID);
		List<DesktopBrowserDistribution> desktopBrowserDistribution=deviceCloudService.getDesktopBrowserDistribution(marketID);
		int len=mobileBrowserDistribution.size();
		String month[]=new String[len];
		int size=0;
		String mon = " ";
		for(MobileBrowserDistribution obj : mobileBrowserDistribution){
			mon = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-05", "-May");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-06", "-June");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-07", "-July");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(mon);
			month[size]=obj.getMonth();
			size++;
		}

		int end = month.length;
		int c=0;
		for (int i = 0; i < end; i++) {
			for (int j = i + 1; j < end; j++) {
				if (month[i].equals(month[j])) {                  
					c++;
					month[j] = month[j + 1];
					j--;

					end--;

				}
			}
		}

		double chromeVal[]=new double[len];
		int counter=0;
		for(MobileBrowserDistribution obj:mobileBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("chrome"))
			{	

				if(counter<=len-1){
					chromeVal[counter]=obj.getValue();
					counter++;
				}
			}
		}


		double firefoxVal[]=new double[len];
		int counterFirefox=0;
		for(MobileBrowserDistribution obj:mobileBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("firefox"))
			{	
				if(counterFirefox<=len-1){
					firefoxVal[counterFirefox]=obj.getValue();
					counterFirefox++;
				}

			}
		}

		double safariVal[]=new double[len];
		int counterSafari=0;
		for(MobileBrowserDistribution obj:mobileBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("safari"))
			{	
				if(counterSafari<=len-1){
					safariVal[counterSafari]=obj.getValue();
					counterSafari++;
				}

			}
		}

		double ieVal[]=new double[len];
		int counterIE=0;
		for(MobileBrowserDistribution obj:mobileBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("ie"))
			{	
				if(counterIE<=len-1){
					ieVal[counterIE]=obj.getValue();
					counterIE++;
				}

			}
		}

		double operaVal[]=new double[len];
		int counterOpera=0;
		for(MobileBrowserDistribution obj:mobileBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("opera"))
			{	
				if(counterOpera<=len-1){
					operaVal[counterOpera]=obj.getValue();
					counterOpera++;
				}

			}
		}



		double chromeTotal = 0;
		double firefoxTotal=0;
		double safariTotal=0;
		double ieTotal=0;
		double operaTotal=0;
		for(int i=0;i<end;i++){

			chromeTotal=chromeTotal+chromeVal[i];
			firefoxTotal=firefoxTotal+firefoxVal[i];
			safariTotal=safariTotal+safariVal[i];
			ieTotal=ieTotal+ieVal[i];
			operaTotal=operaTotal+operaVal[i];
		}
		String bigName="Chrome";
		double big=chromeTotal;
		if(firefoxTotal>big){
			big=firefoxTotal;
			bigName="Firefox";
		}
		if(safariTotal>big){
			big=safariTotal;
			bigName="safari";
		}
		if(ieTotal>big){
			big=ieTotal;
			bigName="IE";
		}
		if(operaTotal>big){
			big=operaTotal;
			bigName="Opera";
		}


		List<MobileBrowserVersionDistribution> mobileBrowserVersionDistribution=deviceCloudService.getMobileBrowserVersionDistribution(bigName);

		int lenD=desktopBrowserDistribution.size();
		String monthD[]=new String[lenD];
		String nameD[]=new String[lenD];
		int sizeD=0;
		String monD = " ";
		for(DesktopBrowserDistribution obj : desktopBrowserDistribution){
			monD = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-05", "-May");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-06", "-June");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-07", "-July");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(monD);
			monD = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(monD);
			monthD[sizeD]=obj.getMonth();
			sizeD++;

		}

		int endD = monthD.length;
		int cD=0;
		for (int i = 0; i < endD; i++) {
			for (int j = i + 1; j < endD; j++) {
				if (monthD[i].equals(monthD[j])) {                  
					cD++;
					monthD[j] = monthD[j + 1];
					j--;

					endD--;

				}
			}
		}


		double chromeValD[]=new double[lenD];
		int counterD=0;
		for(DesktopBrowserDistribution obj:desktopBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("chrome"))
			{	

				if(counterD<=lenD-1){
					chromeValD[counterD]=obj.getValue();
					counterD++;
				}

			}
		}


		double firefoxValD[]=new double[lenD];
		int counterFirefoxD=0;
		for(DesktopBrowserDistribution obj:desktopBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("firefox"))
			{	

				if(counterFirefoxD<=lenD-1){
					firefoxValD[counterFirefoxD]=obj.getValue();
					counterFirefoxD++;
				}

			}
		}



		double safariValD[]=new double[lenD];
		int counterSafariD=0;
		for(DesktopBrowserDistribution obj:desktopBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("safari"))
			{	//chromeVal+=obj.getValue()+",";

				if(counterSafariD<=lenD-1){
					safariValD[counterSafariD]=obj.getValue();
					counterSafariD++;
				}

			}
		}

		double ieValD[]=new double[lenD];
		int counterIED=0;
		for(DesktopBrowserDistribution obj:desktopBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("ie"))
			{	//chromeVal+=obj.getValue()+",";

				if(counterIED<=lenD-1){
					ieValD[counterIED]=obj.getValue();
					counterIED++;
				}

			}
		}

		double operaValD[]=new double[lenD];
		int counterOperaD=0;
		for(DesktopBrowserDistribution obj:desktopBrowserDistribution){
			if((obj.getBrowserName()).equalsIgnoreCase("opera"))
			{	//chromeVal+=obj.getValue()+",";

				if(counterOperaD<=lenD-1){
					operaValD[counterOperaD]=obj.getValue();
					counterOperaD++;
				}

			}
		}


		double chromeTotalD = 0;
		double firefoxTotalD=0;
		double safariTotalD=0;
		double ieTotalD=0;
		double operaTotalD=0;

		for(int i=0;i<endD;i++){

			chromeTotalD=chromeTotalD+chromeValD[i];
			firefoxTotalD=firefoxTotalD+firefoxValD[i];
			safariTotalD=safariTotalD+safariValD[i];
			ieTotalD=ieTotalD+ieValD[i];
			operaTotalD=operaTotalD+operaValD[i];
		}
		String bigNameD="Chrome";
		double bigD=chromeTotalD;
		if(firefoxTotalD>bigD){
			bigD=firefoxTotalD;
			bigNameD="Firefox";
		}
		if(safariTotalD>bigD){
			bigD=safariTotalD;
			bigNameD="safari";
		}
		if(ieTotalD>bigD){
			bigD=ieTotalD;
			bigNameD="IE";
		}
		if(operaTotalD>bigD){
			bigD=operaTotalD;
			bigNameD="Opera";
		}

		List<DesktopBrowserVersionDistribution> desktopBrowserVersionDistribution=deviceCloudService.getDesktopBrowserVersionDistribution(bigNameD);

		JsonArray mobileArray= new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month", month[i]);
			obj.addProperty("chromeData", chromeVal[i]);
			obj.addProperty("firefoxData", firefoxVal[i]);
			obj.addProperty("safariData", safariVal[i]);
			obj.addProperty("ieData", ieVal[i]);
			obj.addProperty("operaData", operaVal[i]);
			mobileArray.add(obj);
		}

		JsonArray mobileVersionArray=new JsonArray();
		for(MobileBrowserVersionDistribution obj : mobileBrowserVersionDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version", obj.getVersion());
			obj2.addProperty("value", obj.getRate());
			mobileVersionArray.add(obj2);
		}

		JsonArray desktopArray=new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month", monthD[i]);
			obj.addProperty("chromeData", chromeValD[i]);
			obj.addProperty("firefoxData", firefoxValD[i]);
			obj.addProperty("safariData", safariValD[i]);
			obj.addProperty("ieData", ieValD[i]);
			obj.addProperty("operaData", operaValD[i]);
			desktopArray.add(obj);
		}


		JsonArray desktopVersionArray=new JsonArray();
		for(DesktopBrowserVersionDistribution obj : desktopBrowserVersionDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version", obj.getVersion());
			obj2.addProperty("value", obj.getRate());
			desktopVersionArray.add(obj2);
		}

		JsonObject finalObject=new JsonObject();
		finalObject.add("tableM", mobileArray);
		finalObject.add("pieM", mobileVersionArray);
		finalObject.add("tableD", desktopArray);
		finalObject.add("pieD", desktopVersionArray);

		//System.out.println(finalObject.toString());
		return finalObject.toString();

	}



	////USER TRENDS 


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/DSUserTrendsPage")
	public @ResponseBody String getUserTrends(Model model) throws Exception {


		//System.out.println("hello");
		List<UserOemDistribution> userOemDistribution = deviceCloudService.getUserOemDistribution();
		int len=userOemDistribution.size();
		String month[]=new String[len];

		int size=0;
		String mon = " ";
		for(UserOemDistribution obj : userOemDistribution){
			mon = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-05", "-May");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-06", "-June");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-07", "-July");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(mon);
			mon = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(mon);
			month[size]=obj.getMonth();
			size++;

		}

		int end = month.length;
		int c=0;
		for (int i = 0; i < end; i++) {
			for (int j = i + 1; j < end; j++) {
				if (month[i].equalsIgnoreCase(month[j])) {                  
					c++;
					month[j] = month[j + 1];// shifting the values
					j--;

					end--;

				}
			}
		}

		double appleVal[]=new double[len];
		int appleCounter=0;
		for(UserOemDistribution obj:userOemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Apple")){

				if(appleCounter<=len-1)
					appleVal[appleCounter]=obj.getValue();

				appleCounter++;
			}
		}

		double lgVal[]=new double[len];
		int lgCounter=0;
		for(UserOemDistribution obj:userOemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Lg")){

				if(lgCounter<=len-1)
					lgVal[lgCounter]=obj.getValue();

				lgCounter++;
			}
		}

		double googleVal[]=new double[len];
		int googleCounter=0;
		for(UserOemDistribution obj:userOemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Google")){

				if(googleCounter<=len-1)
					googleVal[googleCounter]=obj.getValue();

				googleCounter++;
			}
		}

		double samsungVal[]=new double[len];
		int samsungCounter=0;
		for(UserOemDistribution obj:userOemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Samsung")){

				if(samsungCounter<=len-1)
					samsungVal[samsungCounter]=obj.getValue();

				samsungCounter++;
			}
		}

		double motoVal[]=new double[len];
		int motoCounter=0;
		for(UserOemDistribution obj:userOemDistribution)
		{
			if(obj.getVendorName().equalsIgnoreCase("Motorola")){

				if(motoCounter<=len-1)
					motoVal[motoCounter]=obj.getValue();

				motoCounter++;
			}
		}


		List<AndroidDistribution> androidDistribution = deviceCloudService.getAndroidDistribution();


		List<IosDistribution> iosDistribution =  deviceCloudService.getIosDistribution();

		List<UserOsDistribution> userOsDistribution =  deviceCloudService.getUserOsDistribution();

		int len_os=userOsDistribution.size();
		String month_os[]=new String[len_os];

		int size_os=0;
		String mon_os = " ";
		for(UserOsDistribution obj : userOsDistribution){
			mon_os = obj.getMonth().replace("-01", "-Jan");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-02", "-Feb");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-03", "-Mar");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-04", "-Apr");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-05", "-May");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-06", "-June");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-07", "-July");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-08", "-Aug");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-09", "-Sep");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-10", "-Oct");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-11", "-Nov");
			obj.setMonth(mon_os);
			mon_os = obj.getMonth().replace("-12", "-Dec");
			obj.setMonth(mon_os);
			month_os[size_os]=obj.getMonth();
			size_os++;

		}

		int end_os = month_os.length;
		int c_os=0;
		for (int i = 0; i < end_os; i++) {
			for (int j = i + 1; j < end_os; j++) {
				if (month_os[i].equalsIgnoreCase(month_os[j])) {                  
					c_os++;
					month_os[j] = month_os[j + 1];// shifting the values
					j--;

					end_os--;

				}
			}
		}

		double iosVal[]=new double[len_os];
		int iosCounter=0;
		for(UserOsDistribution obj:userOsDistribution){
			if((obj.getOs()).equalsIgnoreCase("IOS"))
			{

				if(iosCounter<=len_os-1){
					iosVal[iosCounter]=obj.getValue();
					iosCounter++;
				}


			}
		}

		double androidVal[]=new double[len_os];
		int androidCounter=0;
		for(UserOsDistribution obj:userOsDistribution){
			if((obj.getOs()).equalsIgnoreCase("Android"))
			{

				if(androidCounter<=len_os-1){
					androidVal[androidCounter]=obj.getValue();
					androidCounter++;
				}


			}
		}


		double windowsVal[]=new double[len_os];
		int windowsCounter=0;
		for(UserOsDistribution obj:userOsDistribution){
			if((obj.getOs()).equalsIgnoreCase("Windows Phone"))
			{

				if(windowsCounter<=len_os-1){
					windowsVal[windowsCounter]=obj.getValue();
					windowsCounter++;
				}


			}
		}




		JsonArray oemArray=new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month",month[i]);
			obj.addProperty("appleData", appleVal[i]);
			obj.addProperty("lgData", lgVal[i]);
			obj.addProperty("googleData", googleVal[i]);
			obj.addProperty("samsungData", samsungVal[i]);
			obj.addProperty("motoData", motoVal[i]);
			oemArray.add(obj);
		}

		JsonArray androidArray=new JsonArray();
		for(AndroidDistribution obj : androidDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version",obj.getVersion());
			obj2.addProperty("value",obj.getRate());
			androidArray.add(obj2);
		}

		JsonArray iosArray=new JsonArray();
		for(IosDistribution obj : iosDistribution){
			JsonObject obj2=new JsonObject();
			obj2.addProperty("version",obj.getIosVersion());
			obj2.addProperty("value",obj.getRate());
			iosArray.add(obj2);
		}

		JsonArray osArray=new JsonArray();
		for(int i=0;i<=12;i++){
			JsonObject obj=new JsonObject();
			obj.addProperty("month",month_os[i]);
			obj.addProperty("iosData", iosVal[i]);
			obj.addProperty("androidData", androidVal[i]);
			obj.addProperty("windowsData", windowsVal[i]);

			osArray.add(obj);
		}

		JsonObject finalObject =new JsonObject();
		finalObject.add("tableOEM", oemArray);
		finalObject.add("pieAndroid", androidArray);
		finalObject.add("pieIOS", iosArray);
		finalObject.add("tableOS", osArray);
		//	System.out.println(finalObject);



		return finalObject.toString();
	}



	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/DSBasicData")
	public String basic(Model model) throws Exception {

		List<Basic_Details> basicDetails = null;
		basicDetails = deviceCloudService.BasicDeviceData();
		model.addAttribute("temp", basicDetails);
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSBasicData";

	}




	///////////update Browser Global

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataBrowserGlobal")
	public  String uploadFileBrowserGlobal(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {

		//	List<Browser_Marketshare> browser_MarketShareDetails=deviceCloudService.getBrowserData();

		String dir = "D:\\UpdateDSCdatabase";
		//System.out.println("controller");
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";

		} else {
			dir += "\\" + "Global";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "Global";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] browserName = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] value = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					value[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserName[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into browser marketshare Table

		deviceCloudService.truncateBrowserMarketshare();

		int cc = 0;
		for (int v = 1; v <= 5; v++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 5) {
					deviceCloudService.insertBrowserMarketshare(
							browserName[v], browserName[m], value[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into browserr desktop marketshare TABLE

		String[] browserDeskName = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] valueDesktop = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					valueDesktop[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserDeskName[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.insertBrowserDesktopMarketshare(browserDeskName[v1],
							browserDeskName[m], valueDesktop[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}

		model.addAttribute("countryBrowser", "bGlobal");
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";
		/*return "http://localhost:8080/digiDEMO/integratedQALabs/mobileLab/deviceSelectionMatrix/BrowserIndex.jsp#/global";*/
	}



	//////////////UPDATE BROWSER US


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataBrowserUS")
	public  String uploadFileBrowserUS(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {


		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";

		} else {
			dir += "\\" + "Global";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "Global";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] browserName = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] value = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					value[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserName[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into browser marketshare Table

		deviceCloudService.truncateBrowserUSMarketshare();

		int cc = 0;
		for (int v = 1; v <= 5; v++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 5) {
					deviceCloudService.insertBrowserUSMarketshare(
							browserName[v], browserName[m], value[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into browserr desktop marketshare TABLE

		String[] browserDeskName = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] valueDesktop = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					valueDesktop[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserDeskName[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.insertBrowserUSDesktopMarketshare(browserDeskName[v1],
							browserDeskName[m], valueDesktop[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}		

		model.addAttribute("countryBrowser", "bUS");

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";	




	}




	//////////////UPDATE BROWSER UK


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataBrowserUK")
	public  String uploadFileBrowserUK(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {


		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";

		} else {
			dir += "\\" + "Global";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "Global";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] browserName = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] value = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					value[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserName[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into browser marketshare Table

		deviceCloudService.truncateBrowserUKMarketshare();

		int cc = 0;
		for (int v = 1; v <= 5; v++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 5) {
					deviceCloudService.insertBrowserUKMarketshare(
							browserName[v], browserName[m], value[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into browserr desktop marketshare TABLE

		String[] browserDeskName = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] valueDesktop = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					valueDesktop[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					browserDeskName[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.insertBrowserUKDesktopMarketshare(browserDeskName[v1],
							browserDeskName[m], valueDesktop[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}


		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}		

		model.addAttribute("countryBrowser", "bUK");

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";	






	}


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/DSfinalSelection")
	public String deviceSelection(Model model,
			@RequestParam("category") String category,
			@RequestParam("subcategory") String subcategory,
			@RequestParam("optionnm") String optionnm,
			@RequestParam("n") int n, HttpSession session) throws IOException {

		model.addAttribute("n", n);

		model.addAttribute("category", category);
		model.addAttribute("subcategory", subcategory);
		model.addAttribute("optionnm", optionnm);
		model.addAttribute("n", n);

		int os = 0;
		int op=0;

		switch (category) {
		case "Select OS":
			os = 0;
			break;
		case "iOS":
			os = 1;
			break;
		case "Android":
			os = 2;
			break;
		case "Windows Phone OS":
			os = 4;
			break;
		case "BlackBerry OS":
			os = 5;
			break;

		}

		model.addAttribute("os", os);

		switch (optionnm) {
		case "Only Selected Version":
			op = 1;
			break;
		case "Selected and Higher Versions":
			op = 2;
			break;
		}
		model.addAttribute("op", op);
		//System.out.println("selected option:::"+optionnm);
		if(op==2){
			if (os == 1) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;
				switch (subcategory) {
				case "iOS 7":
					ver = 1;
					break;
				case "iOS 8":
					ver = 2;
					break;
				case "iOS 9":
					ver = 3;
					break;

				}
				deviceCloudService.TruncateTemp_Data();

				int[] vn = new int[3];
				vn = new int[] { 1, 2, 3 };
				int[] sp = new int[3];
				int[] tb = new int[3];

				for (int j = 0; j < 3; j++) {
					if (vn[j] >= ver) {
						int temp = nsp;
						nsp = deviceCloudService.apple_versp(vn[j], nsp);
						sp[j] = nsp - temp;
					}
				}
				for (int j = 0; j < 3; j++) {
					if (vn[j] >= ver) {
						int temp = ntb;
						ntb = deviceCloudService.apple_vertb(vn[j], ntb);
						tb[j] = ntb - temp;
					}
				}
				//System.out.println("nsp" + nsp);
				//System.out.println("ntb" + ntb);

				int n1=0;
				int n2=0;
				int n3=0;
				int nspn1 = sp[0];
				int ntbn1 = tb[0];
				int csp_maxn1 = 0;
				int ctb_maxn1 = 0;
				int nspn2 = sp[1];
				int ntbn2 = tb[1];
				int csp_maxn2 = 0;
				int ctb_maxn2 = 0;
				int nspn3 = sp[2];
				int ntbn3 = tb[2];
				int csp_maxn3 = 0;
				int ctb_maxn3 = 0;
				int spn1=0;
				int tbn1=0;
				int spn2=0;
				int tbn2=0;
				int[] spn = new int[3];
				int[] tbn = new int[3];

				switch (ver) {
				case 1:
					if (n % 3 == 0) {
						n1=n/3;
						n2=n/3;
						n3=n/3;
					}

					if (n % 3 == 1) {
						n1=n/3;
						n2=n/3;
						n3=(n/3)+1;
					}

					if (n % 3 == 2) {
						n1=n/3;
						n2=(n/3)+1;
						n3=(n/3)+1;
					}
					//for n1
					if (n1 % 2 == 0) {
						ctb_maxn1 = n1 / 2;
						tbn1=ctb_maxn1;

						if (ntbn1 < ctb_maxn1) {
							ctb_maxn1 = ntbn1;
						}

						csp_maxn1 = n1 - ctb_maxn1;
						spn1=csp_maxn1;

						if (nspn1 < csp_maxn1) {
							csp_maxn1 = nspn1;
						}
					}

					if (n1 % 2 == 1) {
						ctb_maxn1 = ((n1-1)/2);
						tbn1=ctb_maxn1;

						if (ntbn1 < ctb_maxn1) {
							ctb_maxn1 = ntbn1;
						}

						csp_maxn1 = n1 - ctb_maxn1;
						spn1=csp_maxn1;

						if (nspn1 < csp_maxn1) {
							csp_maxn1 = nspn1;
						}
					}

					//for n2
					if (n2 % 2 == 0) {
						ctb_maxn2 = (n2 / 2)+(tbn1-ctb_maxn1);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2)+(spn1-csp_maxn1);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}

					if (n2 % 2 == 1) {
						ctb_maxn2 = ((n2-1)/2)+(tbn1-ctb_maxn1);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2)+(spn1-csp_maxn1);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}
					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2)+(tbn2-ctb_maxn2);
						//tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						//spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2)+(tbn2-ctb_maxn2);
						//tbn2=ctb_maxn2;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						//spn2=csp_maxn2;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					break;
				case 2:
					if (n % 2 == 0) {
						n2=n/2;
						n3=n/2;
					}

					if (n % 2 == 1) {
						n2=n/2;
						n3=(n/2)+1;
					}
					//for n2
					if (n2 % 2 == 0) {
						ctb_maxn2 = (n2 / 2);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}

					if (n2 % 2 == 1) {
						ctb_maxn2 = ((n2-1)/2);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}
					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2)+(tbn2-ctb_maxn2);
						//tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						//spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2)+(tbn2-ctb_maxn2);
						//tbn2=ctb_maxn2;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						//spn2=csp_maxn2;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					break;
				case 3:
					n3=n;
					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2);
						//tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3);
						//spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2);
						//tbn2=ctb_maxn2;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3);
						//spn2=csp_maxn2;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					break;

				}

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();
				int z=0;
				int x=0;

				for (int j = 2; j >= 0; j--) {

					listASP= deviceCloudService.AppleSelectDevices(os, vn[j], "SP", spn[j]);
					listASP1.add(z,listASP);
					z=z+1;
					model.addAttribute("tempASP1", listASP1);				


					listATB = deviceCloudService.AppleSelectDevices(os, vn[j], "TB", tbn[j]);
					listATB1.add(x,listATB);
					x=x+1;
					model.addAttribute("tempATB1", listATB1);

				}


				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 2) {
				int nsp1 = 0;
				int ntb1 = 0;
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "2.2":ver = 1;
				break;
				case "2.3":ver = 2;
				break;
				case "3.x":ver = 3;
				break;
				case "4.1.x":ver = 4;
				break;
				case "4.2.x":ver = 5;
				break;
				case "4.3":ver = 6;
				break;
				case "4.4":ver = 7;
				break;
				case "5.0":ver = 8;
				break;
				case "5.1":ver = 9;
				break;
				case "6.0":ver = 10;
				break;

				}

				//System.out.println("selected version:"+subcategory);
				//System.out.println("ver version:"+ver);

				int[] vn = new int[10];
				vn = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
				int[] sp = new int[10];
				int[] tb = new int[10];

				deviceCloudService.TruncateTemp_Data();

				for (int j = 0; j < 10; j++) {
					if (vn[j] >= ver) {
						nsp1 = deviceCloudService.android_versp(vn[j], nsp);	
						sp[j] = nsp1;	
						nsp= nsp + nsp1;	
					}
				}
				for (int j = 0; j < 10; j++) {
					if (vn[j] >= ver) {
						ntb1 = deviceCloudService.android_vertb(vn[j], ntb);
						tb[j] = ntb1;
						ntb=ntb+ntb1;
						//System.out.println("tb["+j+"]:::" + tb[j]);
					}
				}
				//System.out.println("nsp" + nsp);
				//System.out.println("ntb" + ntb);

				int n1=0;
				int n2=0;
				int n3=0;
				int n4=0;
				int n5=0;
				int n6=0;
				int n7=0;
				int n8=0;
				int n9=0;
				int n10=0;

				int nspn1 = sp[0];
				int ntbn1 = tb[0];
				int csp_maxn1 = 0;
				int ctb_maxn1 = 0;

				int nspn2 = sp[1];
				int ntbn2 = tb[1];
				int csp_maxn2 = 0;
				int ctb_maxn2 = 0;

				int nspn3 = sp[2];
				int ntbn3 = tb[2];
				int csp_maxn3 = 0;
				int ctb_maxn3 = 0;

				int nspn4 = sp[3];
				int ntbn4 = tb[3];
				int csp_maxn4 = 0;
				int ctb_maxn4 = 0;

				int nspn5 = sp[4];
				int ntbn5 = tb[4];
				int csp_maxn5 = 0;
				int ctb_maxn5 = 0;

				int nspn6 = sp[5];
				int ntbn6 = tb[5];
				int csp_maxn6 = 0;
				int ctb_maxn6 = 0;

				int nspn7 = sp[6];
				int ntbn7 = tb[6];
				int csp_maxn7 = 0;
				int ctb_maxn7 = 0;

				int nspn8 = sp[7];
				int ntbn8 = tb[7];
				int csp_maxn8 = 0;
				int ctb_maxn8 = 0;

				int nspn9 = sp[8];
				int ntbn9 = tb[8];
				int csp_maxn9 = 0;
				int ctb_maxn9 = 0;

				int nspn10 = sp[9];
				int ntbn10 = tb[9];
				int csp_maxn10 = 0;
				int ctb_maxn10 = 0;

				int spn1=0;
				int tbn1=0;

				int spn2=0;
				int tbn2=0;

				int spn3=0;
				int tbn3=0;

				int spn4=0;
				int tbn4=0;

				int spn5=0;
				int tbn5=0;

				int spn6=0;
				int tbn6=0;

				int spn7=0;
				int tbn7=0;

				int spn8=0;
				int tbn8=0;

				int spn9=0;
				int tbn9=0;

				int spn10=0;
				int tbn10=0;


				int[] spn = new int[10];
				int[] tbn = new int[10];

				switch (ver) {
				case 1:
					if (n % 10 == 0) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=n/10;
						n7=n/10;
						n8=n/10;
						n9=n/10;
						n10=n/10;
					}

					if (n % 10 == 1) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=n/10;
						n7=n/10;
						n8=n/10;
						n9=n/10;
						n10=(n/10)+1;
					}

					if (n % 10 == 2) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=n/10;
						n7=n/10;
						n8=n/10;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 3) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=n/10;
						n7=n/10;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 4) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=n/10;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 5) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=n/10;
						n6=(n/10)+1;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 6) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=n/10;
						n5=(n/10)+1;
						n6=(n/10)+1;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 7) {
						n1=n/10;
						n2=n/10;
						n3=n/10;
						n4=(n/10)+1;
						n5=(n/10)+1;
						n6=(n/10)+1;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 8) {
						n1=n/10;
						n2=n/10;
						n3=(n/10)+1;
						n4=(n/10)+1;
						n5=(n/10)+1;
						n6=(n/10)+1;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					if (n % 10 == 9) {
						n1=n/10;
						n2=(n/10)+1;
						n3=(n/10)+1;
						n4=(n/10)+1;
						n5=(n/10)+1;
						n6=(n/10)+1;
						n7=(n/10)+1;
						n8=(n/10)+1;
						n9=(n/10)+1;
						n10=(n/10)+1;
					}
					//for n1
					if (n1 % 2 == 0) {
						ctb_maxn1 = n1 / 2;
						tbn1=ctb_maxn1;

						if (ntbn1 < ctb_maxn1) {
							ctb_maxn1 = ntbn1;
						}

						csp_maxn1 = n1 - ctb_maxn1;
						spn1=csp_maxn1;

						if (nspn1 < csp_maxn1) {
							csp_maxn1 = nspn1;
						}
					}

					if (n1 % 2 == 1) {
						ctb_maxn1 = ((n1-1)/2);
						tbn1=ctb_maxn1;

						if (ntbn1 < ctb_maxn1) {
							ctb_maxn1 = ntbn1;
						}

						csp_maxn1 = n1 - ctb_maxn1;
						spn1=csp_maxn1;

						if (nspn1 < csp_maxn1) {
							csp_maxn1 = nspn1;
						}
					}

					//for n2
					if (n2 % 2 == 0) {
						ctb_maxn2 = (n2 / 2)+(tbn1-ctb_maxn1);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2)+(spn1-csp_maxn1);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}

					if (n2 % 2 == 1) {
						ctb_maxn2 = ((n2-1)/2)+(tbn1-ctb_maxn1);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2)+(spn1-csp_maxn1);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}
					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2)+(tbn2-ctb_maxn2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2)+(tbn2-ctb_maxn2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					//for n4
					if (n4 % 2 == 0) {
						ctb_maxn4 = (n4 / 2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}

					if (n4 % 2 == 1) {
						ctb_maxn4 = ((n4-1)/2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}
					//for n5
					if (n5 % 2 == 0) {
						ctb_maxn5 = (n5 / 2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}

					if (n5 % 2 == 1) {
						ctb_maxn5 = ((n5-1)/2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}
					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 2:
					if (n % 9 == 0) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=n/9;
						n7=n/9;
						n8=n/9;
						n9=n/9;
						n10=n/9;
					}

					if (n % 9 == 1) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=n/9;
						n7=n/9;
						n8=n/9;
						n9=n/9;
						n10=(n/9)+1;
					}

					if (n % 9 == 2) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=n/9;
						n7=n/9;
						n8=n/9;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 3) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=n/9;
						n7=n/9;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 4) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=n/9;
						n7=(n/9)+1;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 5) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=n/9;
						n6=(n/9)+1;
						n7=(n/9)+1;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 6) {
						n2=n/9;
						n3=n/9;
						n4=n/9;
						n5=(n/9)+1;
						n6=(n/9)+1;
						n7=(n/9)+1;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 7) {
						n2=n/9;
						n3=n/9;
						n4=(n/9)+1;
						n5=(n/9)+1;
						n6=(n/9)+1;
						n7=(n/9)+1;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}
					if (n % 9 == 8) {
						n2=n/9;
						n3=(n/9)+1;
						n4=(n/9)+1;
						n5=(n/9)+1;
						n6=(n/9)+1;
						n7=(n/9)+1;
						n8=(n/9)+1;
						n9=(n/9)+1;
						n10=(n/9)+1;
					}


					//for n2
					if (n2 % 2 == 0) {
						ctb_maxn2 = (n2 / 2);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}

					if (n2 % 2 == 1) {
						ctb_maxn2 = ((n2-1)/2);
						tbn2=ctb_maxn2;

						if (ntbn2 < ctb_maxn2) {
							ctb_maxn2 = ntbn2;
						}

						csp_maxn2 = (n2 - ctb_maxn2);
						spn2=csp_maxn2;

						if (nspn2 < csp_maxn2) {
							csp_maxn2 = nspn2;
						}
					}
					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2)+(tbn2-ctb_maxn2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2)+(tbn2-ctb_maxn2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3)+(spn2-csp_maxn2);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					//for n4
					if (n4 % 2 == 0) {
						ctb_maxn4 = (n4 / 2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}

					if (n4 % 2 == 1) {
						ctb_maxn4 = ((n4-1)/2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}
					//for n5
					if (n5 % 2 == 0) {
						ctb_maxn5 = (n5 / 2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}

					if (n5 % 2 == 1) {
						ctb_maxn5 = ((n5-1)/2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}
					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 3:
					if (n % 8 == 0) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=n/8;
						n7=n/8;
						n8=n/8;
						n9=n/8;
						n10=n/8;
					}

					if (n % 8 == 1) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=n/8;
						n7=n/8;
						n8=n/8;
						n9=n/8;
						n10=(n/8)+1;
					}

					if (n % 8 == 2) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=n/8;
						n7=n/8;
						n8=n/8;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}
					if (n % 8 == 3) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=n/8;
						n7=n/8;
						n8=(n/8)+1;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}
					if (n % 8 == 4) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=n/8;
						n7=(n/8)+1;
						n8=(n/8)+1;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}
					if (n % 8 == 5) {
						n3=n/8;
						n4=n/8;
						n5=n/8;
						n6=(n/8)+1;
						n7=(n/8)+1;
						n8=(n/8)+1;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}
					if (n % 8 == 6) {
						n3=n/8;
						n4=n/8;
						n5=(n/8)+1;
						n6=(n/8)+1;
						n7=(n/8)+1;
						n8=(n/8)+1;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}
					if (n % 8 == 7) {
						n3=n/8;
						n4=(n/8)+1;
						n5=(n/8)+1;
						n6=(n/8)+1;
						n7=(n/8)+1;
						n8=(n/8)+1;
						n9=(n/8)+1;
						n10=(n/8)+1;
					}


					//for n3
					if (n3 % 2 == 0) {
						ctb_maxn3 = (n3 / 2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}

					if (n3 % 2 == 1) {
						ctb_maxn3 = ((n3-1)/2);
						tbn3=ctb_maxn3;

						if (ntbn3 < ctb_maxn3) {
							ctb_maxn3 = ntbn3;
						}

						csp_maxn3 = (n3 - ctb_maxn3);
						spn3=csp_maxn3;

						if (nspn3 < csp_maxn3) {
							csp_maxn3 = nspn3;
						}
					}
					//for n4
					if (n4 % 2 == 0) {
						ctb_maxn4 = (n4 / 2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}

					if (n4 % 2 == 1) {
						ctb_maxn4 = ((n4-1)/2)+(tbn3-ctb_maxn3);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4)+(spn3-csp_maxn3);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}
					//for n5
					if (n5 % 2 == 0) {
						ctb_maxn5 = (n5 / 2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}

					if (n5 % 2 == 1) {
						ctb_maxn5 = ((n5-1)/2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}
					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 4:
					if (n % 7 == 0) {
						n4=n/7;
						n5=n/7;
						n6=n/7;
						n7=n/7;
						n8=n/7;
						n9=n/7;
						n10=n/7;
					}

					if (n % 7 == 1) {
						n4=n/7;
						n5=n/7;
						n6=n/7;
						n7=n/7;
						n8=n/7;
						n9=n/7;
						n10=(n/7)+1;
					}

					if (n % 7 == 2) {
						n4=n/7;
						n5=n/7;
						n6=n/7;
						n7=n/7;
						n8=n/7;
						n9=(n/7)+1;
						n10=(n/7)+1;
					}
					if (n % 7 == 3) {
						n4=n/7;
						n5=n/7;
						n6=n/7;
						n7=n/7;
						n8=(n/7)+1;
						n9=(n/7)+1;
						n10=(n/7)+1;
					}
					if (n % 7 == 4) {
						n4=n/7;
						n5=n/7;
						n6=n/7;
						n7=(n/7)+1;
						n8=(n/7)+1;
						n9=(n/7)+1;
						n10=(n/7)+1;
					}
					if (n % 7 == 5) {
						n4=n/7;
						n5=n/7;
						n6=(n/7)+1;
						n7=(n/7)+1;
						n8=(n/7)+1;
						n9=(n/7)+1;
						n10=(n/7)+1;
					}
					if (n % 7 == 6) {
						n4=n/7;
						n5=(n/7)+1;
						n6=(n/7)+1;
						n7=(n/7)+1;
						n8=(n/7)+1;
						n9=(n/7)+1;
						n10=(n/7)+1;
					}								

					//for n4
					if (n4 % 2 == 0) {
						ctb_maxn4 = (n4 / 2);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}

					if (n4 % 2 == 1) {
						ctb_maxn4 = ((n4-1)/2);
						tbn4=ctb_maxn4;

						if (ntbn4 < ctb_maxn4) {
							ctb_maxn4 = ntbn4;
						}

						csp_maxn4 = (n4 - ctb_maxn4);
						spn4=csp_maxn4;

						if (nspn4 < csp_maxn4) {
							csp_maxn4 = nspn4;
						}
					}
					//for n5
					if (n5 % 2 == 0) {
						ctb_maxn5 = (n5 / 2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}

					if (n5 % 2 == 1) {
						ctb_maxn5 = ((n5-1)/2)+(tbn4-ctb_maxn4);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5)+(spn4-csp_maxn4);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}
					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 5:
					if (n % 6 == 0) {
						n5=n/6;
						n6=n/6;
						n7=n/6;
						n8=n/6;
						n9=n/6;
						n10=n/6;
					}

					if (n % 6 == 1) {
						n5=n/6;
						n6=n/6;
						n7=n/6;
						n8=n/6;
						n9=n/6;
						n10=(n/6)+1;
					}

					if (n % 6 == 2) {
						n5=n/6;
						n6=n/6;
						n7=n/6;
						n8=n/6;
						n9=(n/6)+1;
						n10=(n/6)+1;
					}
					if (n % 6 == 3) {
						n5=n/6;
						n6=n/6;
						n7=n/6;
						n8=(n/6)+1;
						n9=(n/6)+1;
						n10=(n/6)+1;
					}
					if (n % 6 == 4) {
						n5=n/6;
						n6=n/6;
						n7=(n/6)+1;
						n8=(n/6)+1;
						n9=(n/6)+1;
						n10=(n/6)+1;
					}
					if (n % 6 == 5) {
						n5=n/6;
						n6=(n/6)+1;
						n7=(n/6)+1;
						n8=(n/6)+1;
						n9=(n/6)+1;
						n10=(n/6)+1;
					}							

					//for n5
					if (n5 % 2 == 0) {
						ctb_maxn5 = (n5 / 2);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}

					if (n5 % 2 == 1) {
						ctb_maxn5 = ((n5-1)/2);
						tbn5=ctb_maxn5;

						if (ntbn5 < ctb_maxn5) {
							ctb_maxn5 = ntbn5;
						}

						csp_maxn5 = (n5 - ctb_maxn5);
						spn5=csp_maxn5;

						if (nspn5 < csp_maxn5) {
							csp_maxn5 = nspn5;
						}
					}
					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2)+(tbn5-ctb_maxn5);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6)+(spn5-csp_maxn5);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 6:
					if (n % 5 == 0) {
						n6=n/5;
						n7=n/5;
						n8=n/5;
						n9=n/5;
						n10=n/5;
					}

					if (n % 5 == 1) {
						n6=n/5;
						n7=n/5;
						n8=n/5;
						n9=n/5;
						n10=(n/5)+1;
					}

					if (n % 5 == 2) {
						n6=n/5;
						n7=n/5;
						n8=n/5;
						n9=(n/5)+1;
						n10=(n/5)+1;
					}
					if (n % 5 == 3) {
						n6=n/5;
						n7=n/5;
						n8=(n/5)+1;
						n9=(n/5)+1;
						n10=(n/5)+1;
					}
					if (n % 5 == 4) {
						n6=n/5;
						n7=(n/5)+1;
						n8=(n/5)+1;
						n9=(n/5)+1;
						n10=(n/5)+1;
					}														

					//for n6
					if (n6 % 2 == 0) {
						ctb_maxn6 = (n6 / 2);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}

					if (n6 % 2 == 1) {
						ctb_maxn6 = ((n6-1)/2);
						tbn6=ctb_maxn6;

						if (ntbn6 < ctb_maxn6) {
							ctb_maxn6 = ntbn6;
						}

						csp_maxn6 = (n6 - ctb_maxn6);
						spn6=csp_maxn6;

						if (nspn6 < csp_maxn6) {
							csp_maxn6 = nspn6;
						}
					}
					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2)+(tbn6-ctb_maxn6);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7)+(spn6-csp_maxn6);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}
					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 7:
					if (n % 4 == 0) {
						n7=n/4;
						n8=n/4;
						n9=n/4;
						n10=n/4;
					}

					if (n % 4 == 1) {
						n7=n/4;
						n8=n/4;
						n9=n/4;
						n10=(n/4)+1;
					}

					if (n % 4 == 2) {
						n7=n/4;
						n8=n/4;
						n9=(n/4)+1;
						n10=(n/4)+1;
					}
					if (n % 4 == 3) {
						n7=n/4;
						n8=(n/4)+1;
						n9=(n/4)+1;
						n10=(n/4)+1;
					}													

					//for n7
					if (n7 % 2 == 0) {
						ctb_maxn7 = (n7 / 2);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					if (n7 % 2 == 1) {
						ctb_maxn7 = ((n7-1)/2);
						tbn7=ctb_maxn7;

						if (ntbn7 < ctb_maxn7) {
							ctb_maxn7 = ntbn7;
						}

						csp_maxn7 = (n7 - ctb_maxn7);
						spn7=csp_maxn7;

						if (nspn7 < csp_maxn7) {
							csp_maxn7 = nspn7;
						}
					}

					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2)+(tbn7-ctb_maxn7);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8)+(spn7-csp_maxn7);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 8:
					if (n % 3 == 0) {
						n8=n/3;
						n9=n/3;
						n10=n/3;
					}

					if (n % 3 == 1) {
						n8=n/3;
						n9=n/3;
						n10=(n/3)+1;
					}

					if (n % 3 == 2) {
						n8=n/3;
						n9=(n/3)+1;
						n10=(n/3)+1;
					}

					//for n8
					if (n8 % 2 == 0) {
						ctb_maxn8 = (n8 / 2);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}

					if (n8 % 2 == 1) {
						ctb_maxn8 = ((n8-1)/2);
						tbn8=ctb_maxn8;

						if (ntbn8 < ctb_maxn8) {
							ctb_maxn8 = ntbn8;
						}

						csp_maxn8 = (n8 - ctb_maxn8);
						spn8=csp_maxn8;

						if (nspn8 < csp_maxn8) {
							csp_maxn8 = nspn8;
						}
					}
					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2)+(tbn8-ctb_maxn8);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9)+(spn8-csp_maxn8);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 9:
					if (n % 2 == 0) {
						n9=n/2;
						n10=n/2;
					}

					if (n % 2 == 1) {
						n9=n/2;
						n10=(n/2)+1;
					}

					//for n9
					if (n9 % 2 == 0) {
						ctb_maxn9 = (n9 / 2);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}

					if (n9 % 2 == 1) {
						ctb_maxn9 = ((n9-1)/2);
						tbn9=ctb_maxn9;

						if (ntbn9 < ctb_maxn9) {
							ctb_maxn9 = ntbn9;
						}

						csp_maxn9 = (n9 - ctb_maxn9);
						spn9=csp_maxn9;

						if (nspn9 < csp_maxn9) {
							csp_maxn9 = nspn9;
						}
					}
					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2)+(tbn9-ctb_maxn9);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10)+(spn9-csp_maxn9);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				case 10:

					n10=n;

					//for n10
					if (n10 % 2 == 0) {
						ctb_maxn10 = (n10 / 2);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}

					if (n10 % 2 == 1) {
						ctb_maxn10 = ((n10-1)/2);
						tbn10=ctb_maxn10;

						if (ntbn10 < ctb_maxn10) {
							ctb_maxn10 = ntbn10;
						}

						csp_maxn10 = (n10 - ctb_maxn10);
						spn10=csp_maxn10;

						if (nspn10 < csp_maxn10) {
							csp_maxn10 = nspn10;
						}
					}


					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					spn[2]=csp_maxn3;
					spn[3]=csp_maxn4;
					spn[4]=csp_maxn5;
					spn[5]=csp_maxn6;
					spn[6]=csp_maxn7;
					spn[7]=csp_maxn8;
					spn[8]=csp_maxn9;
					spn[9]=csp_maxn10;

					tbn[0]=ctb_maxn1;
					tbn[1]=ctb_maxn2;
					tbn[2]=ctb_maxn3;
					tbn[3]=ctb_maxn4;
					tbn[4]=ctb_maxn5;
					tbn[5]=ctb_maxn6;
					tbn[6]=ctb_maxn7;
					tbn[7]=ctb_maxn8;
					tbn[8]=ctb_maxn9;
					tbn[9]=ctb_maxn10;

					break;
				}

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();
				int z=0;
				int x=0;

				for (int j = 9; j >= 0; j--) {

					listASP= deviceCloudService.AndroidSelectDevices(os, vn[j], "SP", spn[j]);
					//System.out.println("vn[j]......spn[j]:::::"+vn[j]+"........"+spn[j]);
					listASP1.add(z,listASP);
					z=z+1;
					model.addAttribute("tempASP1", listASP1);				

					listATB = deviceCloudService.AndroidSelectDevices(os, vn[j], "TB", tbn[j]);
					listATB1.add(x,listATB);
					x=x+1;
					model.addAttribute("tempATB1", listATB1);

				}

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 4) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "Microsoft Windows 8":
					ver = 1;
					break;
				case "Microsoft Windows 10":
					ver = 2;
					break;
				case "Internet Tablet OS 2007":
					ver = 3;
					break;
				case "Internet Tablet OS 2008":
					ver = 4;
					break;
				case "Microsoft Windows RT":
					ver = 5;
					break;

				}

				deviceCloudService.TruncateTemp_Data();

				//System.out.println("Selected ver:" + ver);
				int[] vns = new int[2];
				vns = new int[] { 1, 2 };
				int[] sp = new int[2];
				int[] tb = new int[3];
				if (ver == 1 || ver == 2) {
					for (int j = 0; j < 2; j++) {
						if (vns[j] >= ver) {					
							int temp = nsp;
							nsp = deviceCloudService.MWindows_versp(vns[j], nsp);
							sp[j] = nsp - temp;
						}
					}
				}
				int[] vnt = new int[3];
				vnt = new int[] { 3, 4, 5 };
				if (ver == 3 || ver == 4 || ver == 5) {
					for (int j = 0; j < 3; j++) {
						if (vnt[j] >= ver) {
							int temp = ntb;
							ntb = deviceCloudService.MWindows_vertb(vnt[j], ntb);
							tb[j] = ntb - temp;
						}
					}
				}

				int n1=0;
				int n2=0;

				int nspn1 = sp[0];
				int csp_maxn1 = 0;

				int nspn2 = sp[1];
				int csp_maxn2 = 0;

				int spn1=0;

				int[] spn = new int[2];

				switch (ver) {
				case 1:
					if (n % 2 == 0) {
						n1=n/2;
						n2=n/2;
					}

					if (n % 2 == 1) {
						n1=n/2;
						n2=(n/2)+1;
					}

					//for n1
					if (nspn1 <= n1) {
						csp_maxn1 = nspn1;
						spn1=n1-csp_maxn1;
					} else {
						csp_maxn1 = n1;
					}

					//for n2				
					if ((nspn2+spn1) <= n2) {
						csp_maxn2 = nspn2+spn1;
					} else {
						csp_maxn2 = n2+spn1;
					}

					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					break;
				case 2:
					n2=n;
					//for n2
					if (nspn2 <= n2) {
						csp_maxn2 = nspn2;
					} else {
						csp_maxn2 = n2;
					}

					spn[0]=csp_maxn1;
					spn[1]=csp_maxn2;
					break;

				}

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();
				int z=0;
				int x=0;

				for (int j = 1; j >= 0; j--) {
					listASP= deviceCloudService.MWindowsSelectDevices(os, vns[j], "SP", spn[j]);
					//System.out.println("vns[j].......spn[j]:::::::"+vns[j]+"...."+spn[j]);
					listASP1.add(z,listASP);
					z=z+1;
					model.addAttribute("tempASP1", listASP1);				
				}

				int n3=0;
				int n4=0;
				int n5=0;

				int ntbn3 = tb[0];
				int ctb_maxn3 = 0;

				int ntbn4 = tb[1];
				int ctb_maxn4 = 0;

				int ntbn5 = tb[2];
				int ctb_maxn5 = 0;

				int tbn3=0;
				int tbn4=0;

				int[] tbn = new int[3];		

				switch (ver) {
				case 3:
					if (n % 3 == 0) {
						n3=n/3;
						n4=n/3;
						n5=n/3;
					}

					if (n % 3 == 1) {
						n3=n/3;
						n4=n/3;
						n5=(n/3)+1;
					}

					if (n % 3 == 2) {
						n3=n/3;
						n4=(n/3)+1;
						n5=(n/3)+1;
					}

					//for n3
					if (ntbn3 <= n3) {
						ctb_maxn3 = ntbn3;
						tbn3=n3-ctb_maxn3;
					} else {
						ctb_maxn3 = n3;
					}
					//for n4
					if ((ntbn4+tbn3) <= n4) {
						ctb_maxn4 = ntbn4+tbn3;
						tbn4=n4-ctb_maxn4;
					} else {
						ctb_maxn4 = n4+tbn3;
					}
					//for n5
					if ((ntbn5+tbn4) <= n5) {
						ctb_maxn5 = ntbn5+tbn4;
					} else {
						ctb_maxn5 = n5+tbn4;
					}

					tbn[0]=ctb_maxn3;
					tbn[1]=ctb_maxn4;
					tbn[2]=ctb_maxn5;
					break;
				case 4:
					if (n % 2 == 0) {
						n4=n/2;
						n5=n/2;
					}

					if (n % 2 == 1) {
						n4=n/2;
						n5=(n/2)+1;
					}

					//for n4
					if (ntbn4 <= n4) {
						ctb_maxn4 = ntbn4;
						tbn4=n4-ctb_maxn4;
					} else {
						ctb_maxn4 = n4;
					}
					//for n5
					if ((ntbn5+tbn4) <= n5) {
						ctb_maxn5 = ntbn5+tbn4;
					} else {
						ctb_maxn5 = n5+tbn4;
					}

					tbn[0]=ctb_maxn3;
					tbn[1]=ctb_maxn4;
					tbn[2]=ctb_maxn5;
					break;
				case 5:
					n5=n;

					//for n5
					if (ntbn5 <= n5) {
						ctb_maxn5 = ntbn5;
					} else {
						ctb_maxn5 = n5;
					}

					tbn[0]=ctb_maxn3;
					tbn[1]=ctb_maxn4;
					tbn[2]=ctb_maxn5;
					break;

				}

				for (int j = 2; j >= 0; j--) {
					listATB = deviceCloudService.MWindowsSelectDevices(os, vnt[j], "TB", tbn[j]);
					listATB1.add(x,listATB);
					x=x+1;
					model.addAttribute("tempATB1", listATB1);
				}


				//SUGGESTIONS

				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 5) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "BlackBerry 7":
					ver = 1;
					break;
				case "BlackBerry 10":
					ver = 2;
					break;
				case "Tablet OS":
					ver = 3;
					break;
				}
				//System.out.println("Selected ver:" + ver);
				int[] vns = new int[2];
				vns = new int[] { 1, 2 };
				int[] sp = new int[2];

				deviceCloudService.TruncateTemp_Data();

				if (ver == 1 || ver == 2) {
					for (int j = 0; j < 2; j++) {
						if (vns[j] >= ver) {					
							int temp = nsp;
							nsp = deviceCloudService.Rim_versp(vns[j], nsp);
							sp[j] = nsp - temp;
						}
					}
				}

				if (ver == 3) {
					ntb = deviceCloudService.Rim_vertb(ver, ntb);
				}


				int n1=0;
				int n2=0;

				int nspn1 = sp[0];
				int csp_maxn1 = 0;

				int nspn2 = sp[1];
				int csp_maxn2 = 0;

				int spn1=0;

				int[] spn = new int[2];

				if (ver == 1 || ver == 2) {		
					switch (ver) {
					case 1:
						if (n % 2 == 0) {
							n1=n/2;
							n2=n/2;
						}

						if (n % 2 == 1) {
							n1=n/2;
							n2=(n/2)+1;
						}

						//for n1
						if (nspn1 <= n1) {
							csp_maxn1 = nspn1;
							spn1=n1-csp_maxn1;
						} else {
							csp_maxn1 = n1;
						}

						//for n2				
						if ((nspn2+spn1) <= n2) {
							csp_maxn2 = nspn2+spn1;
						} else {
							csp_maxn2 = n2+spn1;
						}

						spn[0]=csp_maxn1;
						spn[1]=csp_maxn2;
						break;
					case 2:
						n2=n;
						//for n2
						if (nspn2 <= n2) {
							csp_maxn2 = nspn2;
						} else {
							csp_maxn2 = n2;
						}

						spn[0]=csp_maxn1;
						spn[1]=csp_maxn2;
						break;

					}

					List<Consolidate_Details> listASP = null;			
					ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();			
					int z=0;

					for (int j = 1; j >= 0; j--) {
						listASP= deviceCloudService.RIMSelectDevices(os, vns[j], "SP", spn[j]);
						//System.out.println("vns[j].......spn[j]:::::::"+vns[j]+"...."+spn[j]);
						listASP1.add(z,listASP);
						z=z+1;
						model.addAttribute("tempASP1", listASP1);				
					}
				}

				int n3=0;

				int ntbn3 = ntb;
				int ctb_maxn3 = 0;

				int tbn = 0;	

				if (ver == 3) {	
					switch (ver) {
					case 3:
						n3=n;

						//for n3
						if (ntbn3 <= n3) {
							ctb_maxn3 = ntbn3;
						} else {
							ctb_maxn3 = n3;
						}

						tbn=ctb_maxn3;
						break;

					}

					List<Consolidate_Details> listATB = null;
					ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();

					listATB = deviceCloudService.RIMSelectDevices(os, ver,"TB", tbn);
					listATB1.add(0,listATB);
					model.addAttribute("tempATB1", listATB1);
				}

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			}

		}
		else if(op==1){

			if (os == 1) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;
				switch (subcategory) {
				case "iOS 7":
					ver = 1;
					break;
				case "iOS 8":
					ver = 2;
					break;
				case "iOS 9":
					ver = 3;
					break;

				}
				deviceCloudService.TruncateTemp_Data();

				nsp = deviceCloudService.apple_versp(ver, nsp);
				ntb = deviceCloudService.apple_vertb(ver, ntb);

				//System.out.println("nsp" + nsp);
				//System.out.println("ntb" + ntb);

				if (n % 2 == 0) {
					ctb_max = n / 2;

					if (ntb < ctb_max) {
						ctb_max = ntb;
					}

					csp_max = n - ctb_max;

					if (nsp < csp_max) {
						csp_max = nsp;
					}
				}

				if (n % 2 == 1) {
					ctb_max = ((n-1)/2);

					if (ntb < ctb_max) {
						ctb_max = ntb;
					}

					csp_max = n - ctb_max;

					if (nsp < csp_max) {
						csp_max = nsp;
					}
				}


				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;
				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();

				// SMARTPHONES

				listASP= deviceCloudService.AppleSelectDevices(os, ver, "SP", csp_max);
				listASP1.add(0,listASP);
				model.addAttribute("tempASP1", listASP1);

				// TABLETS

				listATB = deviceCloudService.AppleSelectDevices(os, ver, "TB", ctb_max);
				listATB1.add(0,listATB);
				model.addAttribute("tempATB1", listATB1);

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 2) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "2.2":ver = 1;
				break;
				case "2.3":ver = 2;
				break;
				case "3.x":ver = 3;
				break;
				case "4.1.x":ver = 4;
				break;
				case "4.2.x":ver = 5;
				break;
				case "4.3":ver = 6;
				break;
				case "4.4":ver = 7;
				break;
				case "5.0":ver = 8;
				break;
				case "5.1":ver = 9;
				break;
				case "6.0":ver = 10;
				break;

				}

				deviceCloudService.TruncateTemp_Data();

				nsp = deviceCloudService.android_versp(ver, nsp);
				ntb = deviceCloudService.android_vertb(ver, ntb);

				if (n % 2 == 0) {
					ctb_max = n / 2;

					if (ntb < ctb_max) {
						ctb_max = ntb;
					}

					csp_max = n - ctb_max;

					if (nsp < csp_max) {
						csp_max = nsp;
					}
				}

				if (n % 2 == 1) {
					ctb_max = ((n-1)/2);

					if (ntb < ctb_max) {
						ctb_max = ntb;
					}

					csp_max = n - ctb_max;

					if (nsp < csp_max) {
						csp_max = nsp;
					}
				}

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();

				// SMARTPHONES

				listASP= deviceCloudService.AndroidSelectDevices(os, ver, "SP", csp_max);
				listASP1.add(0,listASP);
				model.addAttribute("tempASP1", listASP1);


				// TABLETS
				listATB = deviceCloudService.AndroidSelectDevices(os, ver, "TB", ctb_max);
				listATB1.add(0,listATB);
				model.addAttribute("tempATB1", listATB1);

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 4) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "Microsoft Windows 8":
					ver = 1;
					break;
				case "Microsoft Windows 10":
					ver = 2;
					break;
				case "Internet Tablet OS 2007":
					ver = 3;
					break;
				case "Internet Tablet OS 2008":
					ver = 4;
					break;
				case "Microsoft Windows RT":
					ver = 5;
					break;

				}

				deviceCloudService.TruncateTemp_Data();

				if (ver == 1 || ver == 2) {
					nsp = deviceCloudService.MWindows_versp(ver, nsp);
				}

				if (ver == 3 || ver == 4 || ver == 5) {
					ntb = deviceCloudService.MWindows_vertb(ver, ntb);					
				}

				if (ver == 1 || ver == 2) {
					if (nsp <= n) {
						csp_max = nsp;
					} else {
						csp_max = n;
					}
				}
				if (ver == 3 || ver == 4 || ver == 5) {
					if (ntb <= n) {
						ctb_max = ntb;
					} else {
						ctb_max = n;
					}
				}
				//System.out.println("csp_max" + csp_max);
				//System.out.println("ctb_max" + ctb_max);

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();

				if (ver == 1 || ver == 2) {					
					// SMARTPHONES
					listASP= deviceCloudService.MWindowsSelectDevices(os, ver, "SP", csp_max);
					listASP1.add(0,listASP);
					model.addAttribute("tempASP1", listASP1);
				}


				if (ver == 3 || ver == 4 || ver == 5) {

					// TABLETS

					listATB = deviceCloudService.MWindowsSelectDevices(os, ver, "TB", ctb_max);
					listATB1.add(0,listATB);
					model.addAttribute("tempATB1", listATB1);
				}

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			} else if (os == 5) {
				int nsp = 0;
				int ntb = 0;
				int csp_max = 0;
				int ctb_max = 0;
				int ver = 0;

				switch (subcategory) {
				case "BlackBerry 7":
					ver = 1;
					break;
				case "BlackBerry 10":
					ver = 2;
					break;
				case "Tablet OS":
					ver = 3;
					break;
				}

				deviceCloudService.TruncateTemp_Data();

				if (ver == 1 || ver == 2) {
					nsp = deviceCloudService.Rim_versp(ver, nsp);
				}

				if (ver == 3) {
					ntb = deviceCloudService.Rim_vertb(ver, ntb);
				}

				if (ver == 1 || ver == 2) {
					if (nsp <= n) {
						csp_max = nsp;
					} else {
						csp_max = n;
					}
				}
				if (ver == 3) {
					if (ntb <= n) {
						ctb_max = ntb;
					} else {
						ctb_max = n;
					}
				}

				List<Consolidate_Details> listASP = null;
				List<Consolidate_Details> listATB = null;

				ArrayList<List<Consolidate_Details>> listASP1 = new ArrayList<List<Consolidate_Details>>();
				ArrayList<List<Consolidate_Details>> listATB1 = new ArrayList<List<Consolidate_Details>>();

				if (ver == 1 || ver == 2) {

					// SMARTPHONES
					listASP= deviceCloudService.RIMSelectDevices(os, ver, "SP", csp_max);
					listASP1.add(0,listASP);
					model.addAttribute("tempASP1", listASP1);
				}			

				//TABLETS
				if (ver == 3) {
					listATB = deviceCloudService.RIMSelectDevices(os, ver,"TB", ctb_max);
					listATB1.add(0,listATB);
					model.addAttribute("tempATB1", listATB1);
				}

				//SUGGESTIONS
				List<Consolidate_Details> listSUG = null;	
				ArrayList<List<Consolidate_Details>> listSUG1 = new ArrayList<List<Consolidate_Details>>();			  
				int y=0;
				int npdt=0;
				int i=0;
				int[] npdarrt = new int[3];

				for(int p=1;p<=3;p++){

					int temp=npdt;
					npdt=deviceCloudService.SuggestionsPriorityCount(os,p,npdt);
					npdarrt[i]=npdt-temp;
					i=i+1;
					//System.out.println("npdarrt[i]"+npdarrt[i-1]);
				}

				i=0;
				int sumpd=0;
				int pcount=0;

				for (int p=1;p<=3;p++) {
					if (sumpd !=n) {
						if(i > 0){
							if (npdarrt[i] <= (n - npdarrt[i-1])) {
								pcount = npdarrt[i];
							} else if (npdarrt[i] > (n - npdarrt[i-1])) {
								pcount = n - npdarrt[i-1];
							}
						}
						if (npdarrt[i] > n && i == 0) {
							pcount = n;
						} else if (npdarrt[i] <= n && i == 0) {
							pcount = npdarrt[i];
						}
						i=i+1;

						if(sumpd !=n){
							//System.out.println("pcount:"+pcount);
							listSUG = deviceCloudService.Suggestions(os,p,pcount);
							listSUG1.add(y,listSUG);
							y=y+1;
							model.addAttribute("tempSUG1", listSUG1);}

						if (sumpd !=n)
						{sumpd=sumpd+pcount;}
					}
				}

			}
		}
		/*Device device = new Device();
	getDevices(model, device);*/

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSfinalSelection";

	}

	public String reserveDevice(String deviceID)throws IOException,MalformedURLException {
		String host = "10.102.22.86";    // <== udpate your server here
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices/"+deviceID+"/reservations/new");
		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);


		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		Date dateobj = new Date();

		//System.out.println("current date is :" + df.format(dateobj));

		final long hour = 3600*1000;
		Date newDate = new Date(dateobj.getTime() + 2 * hour);
		// System.out.println("current date after 2 hour is :" + df.format(newDate));

		String urlParameters  = "clientCurrentTimestamp="+df.format(dateobj).toString()+"&start="+df.format(dateobj).toString()+"&end="+df.format(newDate).toString();
		// System.out.println(urlParameters);
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

		try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
			wr.write( postData );
			//System.out.println("url is : "+ wr);
		}

		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;       

		InputStream stream = null;

		if (httpURLConnection.getResponseCode() >= 400) {
			stream = httpURLConnection.getErrorStream();

		} else {
			stream = httpURLConnection.getInputStream();       
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		//System.out.println("Response Buffer "+responseBuffer.toString());
		String msg="";
		if (httpURLConnection.getResponseCode() < 300) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject  = (JSONObject) parser.parse(responseBuffer.toString());
				JSONObject data = (JSONObject) jsonObject.get("data");
				msg = (String) data.get("reservations");

				//System.out.println(msg);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//System.out.println("unable to parse json : "+e.getMessage());;
			}

		} else {
			throw new RuntimeException(responseBuffer.toString());
		}
		return msg;

	}

	//dec
	//@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/reserveDevice")	
	public String deviceReservation(Model model,@RequestParam("deviceId")String deviceID,@RequestParam("deviceName")String deviceName)throws IOException,MalformedURLException {

		//System.out.println("device ID = "+ deviceID);
		String host = "10.102.22.86";    // <== udpate your server here
		//String port = "9191";                 // <== update to relevant port
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices/"+deviceID+"/reservations/new");


		// System.out.println(url);
		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);


		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		Date dateobj = new Date();

		//System.out.println("current date is :" + df.format(dateobj));

		final long hour = 3600*1000;
		Date newDate = new Date(dateobj.getTime() + 2 * hour);
		// System.out.println("current date after 2 hour is :" + df.format(newDate));

		String urlParameters  = "clientCurrentTimestamp="+df.format(dateobj).toString()+"&start="+df.format(dateobj).toString()+"&end="+df.format(newDate).toString();
		// System.out.println(urlParameters);
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

		try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
			wr.write( postData );
			//System.out.println("url is : "+ wr);
		}

		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;       

		InputStream stream = null;

		if (httpURLConnection.getResponseCode() >= 400) {
			stream = httpURLConnection.getErrorStream();

		} else {
			stream = httpURLConnection.getInputStream();       
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		//System.out.println("Response Buffer "+responseBuffer.toString());

		if (httpURLConnection.getResponseCode() < 300) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject  = (JSONObject) parser.parse(responseBuffer.toString());
				JSONObject data = (JSONObject) jsonObject.get("data");
				String msg = (String) data.get("reservations");
				model.addAttribute("message",deviceName+" "+msg);
				//System.out.println(msg);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//System.out.println("unable to parse json : "+e.getMessage());;
			}

		} else {
			throw new RuntimeException(responseBuffer.toString());
		}

		Device device = new Device();
		getDevices(model,device);
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudDevices";

	}

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudDevices")
	public String listDevices(Model model,Device device)throws IOException {



		//Device device1 = new Device();
		getDevices(model,device);

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudDevices";

	}


	//dec
	//@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/releaseDevice")	
	public String deviceRelease(Model model,@RequestParam("deviceId")String deviceID,@RequestParam("deviceName")String deviceName)throws IOException,MalformedURLException {

		//System.out.println("device ID = "+ deviceID);
		String host = "10.102.22.86";    // <== udpate your server here
		//String port = "9191";                 // <== update to relevant port
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices/"+deviceID+"/release");


		//System.out.println(url);
		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);


		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		Date dateobj = new Date();

		// System.out.println("current date is :" + df.format(dateobj));

		final long hour = 3600*1000;
		Date newDate = new Date(dateobj.getTime() + 2 * hour);
		// System.out.println("current date after 2 hour is :" + df.format(newDate));

		String urlParameters  = "clientCurrentTimestamp="+df.format(dateobj).toString()+"&start="+df.format(dateobj).toString()+"&end="+df.format(newDate).toString();
		//System.out.println(urlParameters);
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

		try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
			wr.write( postData );
			//System.out.println("url is : "+ wr);
		}


		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;       

		InputStream stream1 = null;
		//System.out.println("response code="+httpURLConnection.getResponseCode());
		if (httpURLConnection.getResponseCode() >= 400) {
			stream1 = httpURLConnection.getErrorStream();

		} else {
			stream1 = httpURLConnection.getInputStream();       
		}


		BufferedReader in = new BufferedReader(new InputStreamReader(stream1));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		//System.out.println("Response Buffer "+responseBuffer.toString());


		if (httpURLConnection.getResponseCode() < 300) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject  = (JSONObject) parser.parse(responseBuffer.toString());
				String status = (String) jsonObject.get("status");
				if("success".equalsIgnoreCase(status))
					status="Device Released Successfully";
				model.addAttribute("message",deviceName+" "+status);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//System.out.println("unable to parse json : "+e.getMessage());;
			}

		} else {
			throw new RuntimeException(responseBuffer.toString());
		}

		Device device = new Device();
		getDevices(model,device);
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudDevices";

	}

	public String releaseDevice(String deviceID)throws IOException,MalformedURLException {

		//System.out.println("device ID = "+ deviceID);
		String host = "10.102.22.86";    // <== udpate your server here
		//String port = "9191";                 // <== update to relevant port
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices/"+deviceID+"/release");


		//System.out.println(url);
		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);


		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		Date dateobj = new Date();

		// System.out.println("current date is :" + df.format(dateobj));

		final long hour = 3600*1000;
		Date newDate = new Date(dateobj.getTime() + 2 * hour);
		// System.out.println("current date after 2 hour is :" + df.format(newDate));

		String urlParameters  = "clientCurrentTimestamp="+df.format(dateobj).toString()+"&start="+df.format(dateobj).toString()+"&end="+df.format(newDate).toString();
		//System.out.println(urlParameters);
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

		try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
			wr.write( postData );
			//System.out.println("url is : "+ wr);
		}


		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;       

		InputStream stream1 = null;
		//System.out.println("response code="+httpURLConnection.getResponseCode());
		if (httpURLConnection.getResponseCode() >= 400) {
			stream1 = httpURLConnection.getErrorStream();

		} else {
			stream1 = httpURLConnection.getInputStream();       
		}


		BufferedReader in = new BufferedReader(new InputStreamReader(stream1));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		//System.out.println("Response Buffer "+responseBuffer.toString());

		String status ="";
		if (httpURLConnection.getResponseCode() < 300) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject  = (JSONObject) parser.parse(responseBuffer.toString());
				status = (String) jsonObject.get("status");
				if("success".equalsIgnoreCase(status))
					status="Device Released Successfully";


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//System.out.println("unable to parse json : "+e.getMessage());;
			}

		} else {
			throw new RuntimeException(responseBuffer.toString());
		}
		return status;
	}
	//change
	public void updateSeetestDevice() throws IOException{
		//System.out.println("In Get devices");
		int availableCount=0;
		int offlineCount=0;
		int useCount=0;
		//String host = "10.51.25.10";    // <== udpate your server here
		String host = "10.102.22.86";
		// String port = "9191";                 // <== update to relevant port
		//String webPage= "http://"+host+":"+port+"/";
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices");
		//System.out.println(url);

		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();
		//System.out.println(result);
		JSONParser parser = new JSONParser();
		List<DSDevice_Details> deviceDetails = new ArrayList<DSDevice_Details>();
		DSDevice_Details device = null;
		try {
			JSONObject jsonObject  = (JSONObject) parser.parse(result);


			JSONArray data = (JSONArray) jsonObject.get("data");



			for(int i=0;i<data.size();i++){
				JSONObject objects = (JSONObject) data.get(i);
				//System.out.println("Device Name :" + objects.get("name")+"      "+objects.get("id"));
				////device = new ("seetest",(String) objects.get("name"),(String)objects.get("version"),(String)objects.get("os"),(String)objects.get("status"));
				String reservation = (String)objects.get("currentUser");
				if(reservation.equals("None")){
					reservation="false";
				}else
					reservation="true";

				device = new DSDevice_Details("seetest",(String) objects.get("deviceName"),(String) objects.get("osVersion"),(String)objects.get("deviceOs"),(String)objects.get("displayStatus"),(String)objects.get("id"),reservation,(String)objects.get("time"),(String)objects.get("reservation_time"),
						(String)objects.get("udid"),(String)objects.get("deviceCategory"),(String)objects.get("agentLocation"),(String)objects.get("resolution"));

				//device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus());
				deviceDetails.add(device);

			}
			//System.out.println("size ----"+deviceDetails.size());
			/*for(DSDevice_Details obj : deviceDetails){

					System.out.println(obj.getname()+"            "+obj.getdevice_id());

					}*/
			//session.setAttribute("deviceList",deviceDetails );
			deleteSeetestDevicesDB("seetest");
			updateSeetestDevicesDB(deviceDetails);


		} catch (ParseException e) {
			//System.out.println("cannot convert to json "+ e.getMessage());
		}

	}




	//change


	public void getDevices(Model model,Device device) throws IOException{
		//System.out.println("In Get devices");
		int availableCount=0;
		int offlineCount=0;
		int useCount=0;
		//String host = "10.51.25.10";    // <== udpate your server here
		String host = "10.102.22.86";
		// String port = "9191";                 // <== update to relevant port
		//String webPage= "http://"+host+":"+port+"/";
		String webPage= "http://"+host+"/";
		String authStringEnc;
		String name = "digiassure";                    // <== update the log in name
		String password = "Digiassure123";        // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage+"/api/v1/devices");
		//System.out.println(url);

		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();

		JSONParser parser = new JSONParser();
		List<Device> deviceDetails = new ArrayList<Device>();
		device = null;
		try {
			JSONObject jsonObject  = (JSONObject) parser.parse(result);


			JSONArray data = (JSONArray) jsonObject.get("data");



			for(int i=0;i<data.size();i++){
				JSONObject objects = (JSONObject) data.get(i);
				//System.out.println("Device Name :" + objects.get("deviceName")+"      "+objects.get("id"));
				device = new Device((String) objects.get("deviceName"),(String) objects.get("id"),(String)objects.get("displayStatus"),(String)objects.get("deviceOs"),(String)objects.get("osVersion"));

				//device = new Device((String) objects.get("deviceName"),String.valueOf(i+1),(String) objects.get("displayStatus"),(String) objects.get("deviceOs"),(String) objects.get("osVersion"));





				//if(category.equalsIgnoreCase((String) objects.get("deviceOs"))&& subcategory.equalsIgnoreCase((String) objects.get("osVersion")) && optionnm.equalsIgnoreCase("Selected and Higher Versions"))


				/*  Double osver=Double.parseDouble((String) objects.get("osVersion"));
						Double osver1=Double.parseDouble(subcategory);

						if(osver>=osver1)
						{
							System.out.println("####"+osver);
							osver++;
							deviceDetails.add(device);
						}
				 */

				deviceDetails.add(device);




				/*	if("Available".equalsIgnoreCase((String) objects.get("displayStatus"))){
						availableCount ++;
						model.addAttribute("Available", availableCount);
					}
					else if("In Use".equalsIgnoreCase((String) objects.get("displayStatus"))){
						useCount ++;
						model.addAttribute("InUse", useCount);
					}
					else if("Offline".equalsIgnoreCase((String) objects.get("displayStatus"))){
						offlineCount ++;
						model.addAttribute("offline",offlineCount );
					}*/
			}


			//session.setAttribute("deviceList",deviceDetails );
			deleteSeetestDevicesDB("seetest");
			//updateSeetestDevicesDB(deviceDetails);
			model.addAttribute("device",device);
			model.addAttribute("deviceDetails",deviceDetails);

			for(Device obj : deviceDetails){

				//	System.out.println(obj.getDeviceName()+"            "+obj.getDeviceID());

			}
		} catch (ParseException e) {
			//System.out.println("cannot convert to json "+ e.getMessage());
		}

	}

	public void deleteSeetestDevicesDB(String vendor){
		deviceCloudService.deleteAllVendorDevice("seetest");
	}

	public void updateResevationTime(String vendor){
		deviceCloudService.deleteAllVendorDevice("seetest");
	}
	public void updateSeetestDevicesDB(List<DSDevice_Details> deviceDetails){
		for(DSDevice_Details hs: deviceDetails){
			int id=0;
			String vendor="seetest";
			String device_id=hs.getdevice_id();
			String name=hs.getname();
			String version=hs.getversion();
			String os=hs.getos();
			String status=hs.getstatus();
			String reservation=hs.getreservation();
			String reservation_id="Null";
			String udid=hs.getUdid();
			String devicecategory = hs.getdevicecategory();
			String agentlocation=hs.getagentlocation();
			String resolution="--";
			//System.out.println(" "+id+" "+" "+device_id+" "+" "+name+" "+" "+version+" "+" "+os+" "+" "+status+" "+" "+reservation+" "+reservation_id+" "+udid+" "+devicecategory+" "+agentlocation+" "+resolution);
			deviceCloudService.addDevices(id, vendor, device_id, name, version, os, status, reservation, reservation_id,udid,devicecategory,agentlocation,resolution);

		}
	}

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelData")
	public String uploadFile(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {

		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "US";

		} else {
			dir += "\\" + "US";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "US";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "US";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] vend_names = new String[25];

		//System.out.println("filePath:::"+filePath);
		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] data_values = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					vend_names[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into Vendor_MarketShare Table

		deviceCloudService.TruncateVendor_MarketShare();

		int cc = 0;
		for (int v = 1; v <= 10; v++) {
			int rr = 0;
			for (int m = 11; m < 24; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 10) {
					deviceCloudService.InsertVendor_MarketShare(
							vend_names[v], vend_names[m], data_values[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into OS TABLE

		String[] os_names = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] data_values1 = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values1[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					os_names[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inser values into Device_OS Table

		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					deviceCloudService.InsertOS_MarketShare(os_names[v1],
							os_names[m], data_values1[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		// insert values into Device_Details

		FileInputStream file2 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook2 = new HSSFWorkbook(file2);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet2 = workbook2.getSheetAt(2);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator2 = sheet2.iterator();

		Boolean temp = false;
		int r2 = 0;
		int r3 = 0;
		int rcnt = 0;
		String[][] BASIC = new String[500][10];
		int[][] int_values1 = new int[500][10];

		while (rowIterator2.hasNext()) {
			rcnt = rcnt + 1;
			Row row = rowIterator2.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			int c1 = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:

					// //System.out.print(cell.getNumericCellValue() + "  ");
					int_values1[r3][c1] = (int) cell.getNumericCellValue();
					c1 = c1 + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					BASIC[r2][c] = cell.getStringCellValue();
					c = c + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r2 = r2 + 1;
			r3 = r3 + 1;
		}
		file2.close();

		int ven_id = 0;
		int os_id = 0;
		int ver_id = 0;
		for (int rr = 1; rr < rcnt; rr++) {
			for (int cc1 = 0; cc1 < 1; cc1++) {
				ven_id = int_values1[rr][cc1];
				os_id = int_values1[rr][cc1 + 1];
				ver_id = int_values1[rr][cc1 + 2];
				temp = deviceCloudService.Insert_BasicDeviceData(ven_id,
						os_id, ver_id, BASIC[rr][cc1 + 1], BASIC[rr][cc1],
						BASIC[rr][cc1 + 2], BASIC[rr][cc1 + 3]);
				// //System.out.println(ven_id+"  "+os_id+"  "+ver_id+"  "+BASIC[rr][cc1]+"  "+BASIC[rr][cc1+1]+"  "+BASIC[rr][cc1+2]+"  "+BASIC[rr][cc1+3]);
			}
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}

		//Add data to Consolidated table and add priority too..............!!!!!!!!!!

		deviceCloudService.BasicToConsolidatedData();
		List<Consolidate_Details> Consolidate_Details = null;
		Consolidate_Details = deviceCloudService.ConsolidatedData();
		//System.out.println("b2c size:" + Consolidate_Details.size());
		int p = 0;
		int[] priority = new int[Consolidate_Details.size()];

		for (int i1 = 0; i1 < Consolidate_Details.size(); i1++) {

			// Add priority to iOS devices

			String dev1 = Consolidate_Details.get(i1).getDev_name();
			CharSequence ven1 = "Apple";
			int os1 = Consolidate_Details.get(i1).getOs_no();
			int ver1 = Consolidate_Details.get(i1).getVer_no();
			if (dev1.contains(ven1) && os1 == 1 && ver1 != 1) {
				p = 1;
				priority[i1] = p;
				Consolidate_Details cd = Consolidate_Details.get(i1);
				cd.setPri(p);
				//System.out.println(p);
			} else if (dev1.contains(ven1) && os1 == 1 && ver1 == 1) {
				p = 2;
				priority[i1] = p;
				Consolidate_Details cd = Consolidate_Details.get(i1);
				cd.setPri(p);
				//System.out.println(p);
			} else if (os1 == 2
					&& (ver1 == 7 || ver1 == 8 || ver1 == 9 || ver1 == 10)) {
				String[] vend = { "Samsung", "HTC", "LG", "Motorola", "Google",
				"Nokia" };
				for (int j1 = 0; j1 <= 5; j1++) {
					if (dev1.contains(vend[j1])) {
						p = 1;
						priority[i1] = p;
						Consolidate_Details cd = Consolidate_Details.get(i1);
						cd.setPri(p);
						//System.out.println(p);
					}
				}
			} else if (os1 == 2 && (ver1 == 4 || ver1 == 5 || ver1 == 6)) {
				String[] vend = { "Samsung", "HTC", "LG", "Motorola", "Google",
				"Nokia" };
				for (int j1 = 0; j1 <= 5; j1++) {
					if (dev1.contains(vend[j1])) {
						p = 2;
						priority[i1] = p;
						Consolidate_Details cd = Consolidate_Details.get(i1);
						cd.setPri(p);
						//System.out.println(p);
					}
				}
			} else if (os1 == 2 && (ver1 == 1 || ver1 == 2 || ver1 == 3)) {
				String[] vend = { "Samsung", "HTC", "LG", "Motorola", "Google",
				"Nokia" };
				for (int h = 0; h <= 5; h++) {
					if (dev1.contains(vend[h])) {
						p = 3;
						priority[i1] = p;
						Consolidate_Details cd = Consolidate_Details.get(i1);
						cd.setPri(p);
						//System.out.println(p);
					}
				}
			} else if (os1 == 3 || os1 == 4 || os1 == 5) {
				p = 3;
				priority[i1] = p;
				Consolidate_Details cd = Consolidate_Details.get(i1);
				cd.setPri(p);
				//System.out.println(p);
			}

			//UPDATE PRIORITIES.......................
			deviceCloudService.updateData(Consolidate_Details.get(i1));
		}

		model.addAttribute("country", "US");
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";
	}

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataUK")
	public String uploadFileUK(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {


		List<Vendor_MarketShare> Vendor_MarketShareDetails = null;
		Vendor_MarketShareDetails = deviceCloudService.getUKVendorData();
		model.addAttribute("temp", Vendor_MarketShareDetails);

		List<OS_Details> OS_Details = null;
		OS_Details = deviceCloudService.OSUKDeviceData();
		model.addAttribute("temp1", OS_Details);

		List<Android_Rates> Android_Rates = null;
		Android_Rates = deviceCloudService.AndroidRatesData();
		model.addAttribute("temp2", Android_Rates);

		List<iOS_Rates> iOS_Rates = null;
		iOS_Rates = deviceCloudService.iOSRatesData();
		model.addAttribute("temp3", iOS_Rates);

		List<Vendor_Names> Vendor_Names = null;
		Vendor_Names = deviceCloudService.DisplayVendor_Names();
		model.addAttribute("tempV", Vendor_Names);

		List<OS_Names> OS_Names = null;
		OS_Names = deviceCloudService.DisplayOS_Names();
		model.addAttribute("tempOS", OS_Names);

		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "UK";

		} else {
			dir += "\\" + "UK";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "UK";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "UK";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] vend_names = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] data_values = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					vend_names[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into Vendor_MarketShare Table

		deviceCloudService.TruncateUKVendor_MarketShare();

		int cc = 0;
		for (int v = 1; v <= 10; v++) {
			int rr = 0;
			for (int m = 11; m < 24; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 10) {
					deviceCloudService.InsertUKVendor_MarketShare(
							vend_names[v], vend_names[m], data_values[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into OS TABLE

		String[] os_names = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] data_values1 = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values1[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					os_names[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.InsertUKOS_MarketShare(os_names[v1],
							os_names[m], data_values1[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}		


		model.addAttribute("country", "UK");


		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";
	}


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataUser")
	public String uploadFileUser(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {


		List<Vendor_MarketShare> Vendor_MarketShareDetails = null;
		Vendor_MarketShareDetails = deviceCloudService.getUserVendorData();
		model.addAttribute("temp", Vendor_MarketShareDetails);

		List<OS_Details> OS_Details = null;
		OS_Details = deviceCloudService.OSUserDeviceData();
		model.addAttribute("temp1", OS_Details);

		List<Android_Rates> Android_Rates = null;
		Android_Rates = deviceCloudService.AndroidRatesData();
		model.addAttribute("temp2", Android_Rates);

		List<iOS_Rates> iOS_Rates = null;
		iOS_Rates = deviceCloudService.iOSRatesData();
		model.addAttribute("temp3", iOS_Rates);

		List<Vendor_Names> Vendor_Names = null;
		Vendor_Names = deviceCloudService.DisplayVendor_Names();
		model.addAttribute("tempV", Vendor_Names);

		List<OS_Names> OS_Names = null;
		OS_Names = deviceCloudService.DisplayOS_Names();
		model.addAttribute("tempOS", OS_Names);

		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "User";

		} else {
			dir += "\\" + "User";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "User";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "User";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] vend_names = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] data_values = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					vend_names[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into Vendor_MarketShare Table

		deviceCloudService.TruncateUserVendor_MarketShare();

		int cc = 0;
		for (int v = 1; v <= 10; v++) {
			int rr = 0;
			for (int m = 11; m < 24; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 10) {
					deviceCloudService.InsertUserVendor_MarketShare(
							vend_names[v], vend_names[m], data_values[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into OS TABLE

		String[] os_names = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] data_values1 = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values1[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					os_names[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.InsertUserOS_MarketShare(os_names[v1],
							os_names[m], data_values1[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}		


		model.addAttribute("country", "User");


		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";
	}



	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/uploadFileExcelDataGlobal")
	public String uploadFileGlobal(Model model, HttpSession session,
			@RequestParam(value = "recentData", required = false) MultipartFile fileName1) throws IOException {


		List<Vendor_MarketShare> Vendor_MarketShareDetails = null;
		Vendor_MarketShareDetails = deviceCloudService.getGlobalVendorData();
		model.addAttribute("temp", Vendor_MarketShareDetails);

		List<OS_Details> OS_Details = null;
		OS_Details = deviceCloudService.OSGlobalDeviceData();
		model.addAttribute("temp1", OS_Details);

		List<Android_Rates> Android_Rates = null;
		Android_Rates = deviceCloudService.AndroidRatesData();
		model.addAttribute("temp2", Android_Rates);

		List<iOS_Rates> iOS_Rates = null;
		iOS_Rates = deviceCloudService.iOSRatesData();
		model.addAttribute("temp3", iOS_Rates);

		List<Vendor_Names> Vendor_Names = null;
		Vendor_Names = deviceCloudService.DisplayVendor_Names();
		model.addAttribute("tempV", Vendor_Names);

		List<OS_Names> OS_Names = null;
		OS_Names = deviceCloudService.DisplayOS_Names();
		model.addAttribute("tempOS", OS_Names);

		String dir = "D:\\UpdateDSCdatabase";

		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";

		} else {
			dir += "\\" + "Global";
		}

		directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
			dir += "\\" + "Global";
			directory = new File(dir);
			directory.mkdir();

		} else {
			dir += "\\" + "Global";
			directory = new File(dir);
			if (!directory.exists()) {
				directory.mkdir();
			}

		}
		String fileNameOriginal = fileName1.getOriginalFilename();
		//System.out.println("fileNameOriginal:::"+fileNameOriginal);

		String filePath = dir + "\\" + fileNameOriginal;
		File dest = new File(filePath);

		fileName1.transferTo(dest);

		String[] vend_names = new String[25];

		int i = 0;
		// fn="D:/Archana Softwares/javaExcel/a.xls"
		FileInputStream file = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		double[][] data_values = new double[14][11];
		int r = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values[r][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					vend_names[i] = cell.getStringCellValue();
					i = i + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r = r + 1;
		}
		file.close();

		// insert values into Vendor_MarketShare Table

		deviceCloudService.TruncateGlobalVendor_MarketShare();

		int cc = 0;
		for (int v = 1; v <= 10; v++) {
			int rr = 0;
			for (int m = 11; m < 24; m++) {
				rr = rr + 1;
				if (rr < 14 && cc < 10) {
					deviceCloudService.InsertGlobalVendor_MarketShare(
							vend_names[v], vend_names[m], data_values[rr][cc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			cc = cc + 1;
		}

		// Inser into OS TABLE

		String[] os_names = new String[25];

		int j = 0;
		FileInputStream file1 = new FileInputStream(new File(filePath));

		// Create Workbook instance holding reference to .xlsx file
		HSSFWorkbook workbook1 = new HSSFWorkbook(file1);

		// Get first/desired sheet from the workbook
		HSSFSheet sheet1 = workbook1.getSheetAt(1);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator1 = sheet1.iterator();

		double[][] data_values1 = new double[14][11];
		int r1 = 0;
		while (rowIterator1.hasNext()) {
			Row row = rowIterator1.next();
			// For each row, iterate through all the columns
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			int c = 0;
			while (cellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				// Check the cell type and format accordingly
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					// //System.out.print(cell.getNumericCellValue() + "  ");
					data_values1[r1][c] = cell.getNumericCellValue();
					c = c + 1;
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					os_names[j] = cell.getStringCellValue();
					j = j + 1;
					// //System.out.print(cell.getStringCellValue() + "  ");
					break;
				}
			}
			r1 = r1 + 1;
		}
		file1.close();

		// inserT values into Device_OS Table

		Boolean temp = false;
		int ccc = 0;
		for (int v1 = 1; v1 <= 5; v1++) {
			int rr = 0;
			for (int m = 6; m < 19; m++) {
				rr = rr + 1;
				if (rr < 14 && ccc < 5) {
					temp = deviceCloudService.InsertGlobalOS_MarketShare(os_names[v1],
							os_names[m], data_values1[rr][ccc]);
					// //System.out.println(vend_names[v]+" "+vend_names[m]+" "+data_values[rr][cc]);
				}
			}
			ccc = ccc + 1;
		}

		if (temp == true) {
			model.addAttribute("result", "Database updated successfully..!!!");
		}		

		model.addAttribute("country", "Global");

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSindex";
	}

	//dec
	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/reserveDeviceGlobal")	
	public String deviceReservationGlobal(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor,@RequestParam("time") String time,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {

		//System.out.println("resrvation time---"+time);
		//System.out.println("Device id ----->>"+deviceid);
		//System.out.println("Device Name----->>"+deviceName);
		//System.out.println("Vendor----->>"+vendor);
		model.addAttribute("devicename", deviceid);
		LocalTime restime = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS);
		if(time.equals("1"))
		{
			restime = restime.plusMinutes(60);
			model.addAttribute("restime", restime);
		}

		else if(time.equals("2"))
		{
			restime = restime.plusMinutes(120);
			model.addAttribute("restime", restime);

		}

		else 
		{
			restime = restime.plusMinutes(180);
			model.addAttribute("restime", restime);
		}

		deviceCloudService.addReservationTime(deviceid, restime.toString());

		if(vendor.equals("seetest")){
			String success = reserveDevice(deviceid);
			model.addAttribute("message",deviceName+" "+success);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			PerfectoAPI1 perfecto = new PerfectoAPI1();
			String reservationId=perfecto.reserveDevice(deviceid);
			if(!(reservationId.equals(null)|| reservationId.equals("")))
				model.addAttribute("message","Device reserverd with "+reservationId);
			else
				model.addAttribute("message","Device not reserved ");	
			//System.out.println("$$$$$$$$$$$$ resrevationID "+reservationId);
			//deviceCloudService.updateReservationId(deviceId, reservationId);
			deviceCloudService.addPerfectoDevices(deviceid, reservationId);
			Thread.sleep(4000);
			List<Handset> handsetDetailList =getPerfectoDevicesList();
			deleteAllVendorDevicesDB("perfecto_partner");
			updatePerfectoDevicesDB(handsetDetailList);

		}else if (vendor.equals("Emulator")){

		}
		deviceCloudService.updateReservationTimeDB();

		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						////System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=4 , numAndroid = 6;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
			list1 = iOSDeviceList.subList(0,6);
			list1.addAll(androidDeviceList.subList(0,4));


			list1.addAll(iOSDeviceList.subList(10,16));

			list1.addAll(androidDeviceList.subList(4,8));


			list1.addAll(iOSDeviceList.subList(26,32));
			list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesGlobal";
	}
	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/reserveDeviceUS")	
	public String deviceReservationUS(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor, @RequestParam("time") String time,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {

		//System.out.println("resrvation time---"+time);
		//System.out.println("Device id ----->>"+deviceid);
		//System.out.println("Device Name----->>"+deviceName);
		//System.out.println("Vendor----->>"+vendor);
		model.addAttribute("devicename", deviceid);
		LocalTime restime = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS);
		if(time.equals("1"))
		{
			restime = restime.plusMinutes(60);
			model.addAttribute("restime", restime);
		}

		else if(time.equals("2"))
		{
			restime = restime.plusMinutes(120);
			model.addAttribute("restime", restime);

		}

		else 
		{
			restime = restime.plusMinutes(180);
			model.addAttribute("restime", restime);
		}

		deviceCloudService.addReservationTime(deviceid, restime.toString());

		if(vendor.equals("seetest")){
			String success = reserveDevice(deviceid);
			model.addAttribute("message",deviceName+" "+success);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			PerfectoAPI1 perfecto = new PerfectoAPI1();
			String reservationId=perfecto.reserveDevice(deviceid);
			if(!(reservationId.equals(null)|| reservationId.equals("")))
				model.addAttribute("message","Device reserverd with "+reservationId);
			else
				model.addAttribute("message","Device not reserved ");	
			//System.out.println("$$$$$$$$$$$$ resrevationID "+reservationId);
			//deviceCloudService.updateReservationId(deviceId, reservationId);
			deviceCloudService.addPerfectoDevices(deviceid, reservationId);
			Thread.sleep(4000);
			List<Handset> handsetDetailList =getPerfectoDevicesList();
			deleteAllVendorDevicesDB("perfecto_partner");
			updatePerfectoDevicesDB(handsetDetailList);

		}else if (vendor.equals("Emulator")){

		}
		deviceCloudService.updateReservationTimeDB();
		//System.out.println("update result in controler for recommend device"+result);
		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					//System.out.println("reservation time : "+obj.getReservation_time());
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);

						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			/*System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				System.out.println(dev.getname()+"      "+dev.getversion());
			}*/
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			/*System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				System.out.println(dev.getname()+"      "+dev.getversion());
			}*/
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						// System.out.println("remain in ios "+remain);
						// System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						// System.out.println("remain in android "+remain);
						// System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
			list1 = iOSDeviceList.subList(0,6);
			list1.addAll(androidDeviceList.subList(0,4));


			list1.addAll(iOSDeviceList.subList(10,16));

			list1.addAll(androidDeviceList.subList(4,8));


			list1.addAll(iOSDeviceList.subList(26,32));
			list1.addAll(androidDeviceList.subList(8,12));*/
			//System.out.println("final list elements ------------------------------------------------------------ :  ");
			for(DSDevice_Details dev:list1){
				//	System.out.println("@@@@@@@@@@@@@@@@@@@@   " +dev.getname()+"      "+dev.getversion()+"   "+dev.getReservation_time());
			}
			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevices";
	}

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/reserveDeviceUK")	
	public String deviceReservationUK(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor,@RequestParam("time") String time,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {


		//System.out.println("resrvation time---"+time);
		//System.out.println("Device id ----->>"+deviceid);
		//System.out.println("Device Name----->>"+deviceName);
		//System.out.println("Vendor----->>"+vendor);
		model.addAttribute("devicename", deviceid);
		LocalTime restime = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS);
		if(time.equals("1"))
		{
			restime = restime.plusMinutes(60);
			model.addAttribute("restime", restime);
		}

		else if(time.equals("2"))
		{
			restime = restime.plusMinutes(120);
			model.addAttribute("restime", restime);

		}

		else 
		{
			restime = restime.plusMinutes(180);
			model.addAttribute("restime", restime);
		}

		deviceCloudService.addReservationTime(deviceid, restime.toString());

		if(vendor.equals("seetest")){
			String success = reserveDevice(deviceid);
			model.addAttribute("message",deviceName+" "+success);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			PerfectoAPI1 perfecto = new PerfectoAPI1();
			String reservationId=perfecto.reserveDevice(deviceid);
			if(!(reservationId.equals(null)|| reservationId.equals("")))
				model.addAttribute("message","Device reserverd with "+reservationId);
			else
				model.addAttribute("message","Device not reserved ");	
			//System.out.println("$$$$$$$$$$$$ resrevationID "+reservationId);
			//deviceCloudService.updateReservationId(deviceId, reservationId);
			deviceCloudService.addPerfectoDevices(deviceid, reservationId);
			Thread.sleep(4000);
			List<Handset> handsetDetailList =getPerfectoDevicesList();
			deleteAllVendorDevicesDB("perfecto_partner");
			updatePerfectoDevicesDB(handsetDetailList);

		}else if (vendor.equals("Emulator")){

		}
		deviceCloudService.updateReservationTimeDB();

		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
			list1 = iOSDeviceList.subList(0,6);
			list1.addAll(androidDeviceList.subList(0,4));


			list1.addAll(iOSDeviceList.subList(10,16));

			list1.addAll(androidDeviceList.subList(4,8));


			list1.addAll(iOSDeviceList.subList(26,32));
			list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesUK";
	}

	//dec
	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/releaseDeviceGlobal")	
	public String deviceReleaseGlobal(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {


		if(vendor.equals("seetest")){
			String status = releaseDevice(deviceid);
			model.addAttribute("message",deviceName+" "+status);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			String reservationId="";
			try{
				reservationId = deviceCloudService.getPerfectoResevationId(deviceid);
				//System.out.println("$$$$$$ reservation id "+reservationId);
			}catch(Exception e){
				//System.out.println("DB exception");
			}
			if(!reservationId.equals("")){
				deviceCloudService.deletePerfectoDevice(deviceid);
				PerfectoAPI1 perfecto = new PerfectoAPI1();
				perfecto.releaseDevice(reservationId);
				if(!(reservationId.equals(null)|| reservationId.equals("")))
					model.addAttribute("message","Device released with "+reservationId);
				else
					model.addAttribute("message","Device not released ");	
				Thread.sleep(6000);
				List<Handset> handsetDetailList =getPerfectoDevicesList();
				deleteAllVendorDevicesDB("perfecto_partner");
				updatePerfectoDevicesDB(handsetDetailList);
			}

		}else if (vendor.equals("Emulator")){

		}


		Reserve res=deviceCloudService.getCheckMail();
		String mail=res.getMailId();
		//System.out.println("================="+mail);
		File file = new File(macroFile);
		if (file.exists()) {
			file = new File(macroFile +mail);
			file.mkdir();
			if (file.exists()) {
				if (ReleaseSM
						.confirm(macroFile, mail)) {
					//System.out.println("Mail Sent to User");
				} else {
					//System.out.println("Mail not sent to User");
				}
			} else {
				//System.out.println("File doesnot exists");
			}
		} else {
			//System.out.println("Macro File Doesnot exists");
		}

		deviceCloudService.deleteReservationTime(deviceid);

		deviceCloudService.updateReservationTimeDB();
		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=4 , numAndroid = 6;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
				list1 = iOSDeviceList.subList(0,6);
				list1.addAll(androidDeviceList.subList(0,4));


				list1.addAll(iOSDeviceList.subList(10,16));

				list1.addAll(androidDeviceList.subList(4,8));


				list1.addAll(iOSDeviceList.subList(26,32));
				list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesGlobal";
	}

	//dec
	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/releaseDeviceUS")	
	public String deviceReleaseUS(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {


		if(vendor.equals("seetest")){
			String status = releaseDevice(deviceid);
			model.addAttribute("message",deviceName+" "+status);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			String reservationId="";
			try{
				reservationId = deviceCloudService.getPerfectoResevationId(deviceid);
				//System.out.println("$$$$$$ reservation id "+reservationId);
			}catch(Exception e){
				System.out.println("DB exception");
			}
			if(!reservationId.equals("")){
				deviceCloudService.deletePerfectoDevice(deviceid);
				PerfectoAPI1 perfecto = new PerfectoAPI1();
				perfecto.releaseDevice(reservationId);
				if(!(reservationId.equals(null)|| reservationId.equals("")))
					model.addAttribute("message","Device released with "+reservationId);
				else
					model.addAttribute("message","Device not released ");	
				Thread.sleep(6000);
				List<Handset> handsetDetailList =getPerfectoDevicesList();
				deleteAllVendorDevicesDB("perfecto_partner");
				updatePerfectoDevicesDB(handsetDetailList);
			}

		}else if (vendor.equals("Emulator")){

		}
		Reserve res=deviceCloudService.getCheckMail();
		String mail=res.getMailId();
		//System.out.println("================="+mail);
		File file = new File(macroFile);
		if (file.exists()) {
			file = new File(macroFile +mail);
			file.mkdir();
			if (file.exists()) {
				if (ReleaseSM
						.confirm(macroFile, mail)) {
					//System.out.println("Mail Sent to User");
				} else {
					//System.out.println("Mail not sent to User");
				}
			} else {
				//System.out.println("File doesnot exists");
			}
		} else {
			//System.out.println("Macro File Doesnot exists");
		}
		deviceCloudService.deleteReservationTime(deviceid);

		deviceCloudService.updateReservationTimeDB();

		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
						list1 = iOSDeviceList.subList(0,6);
						list1.addAll(androidDeviceList.subList(0,4));


						list1.addAll(iOSDeviceList.subList(10,16));

						list1.addAll(androidDeviceList.subList(4,8));


						list1.addAll(iOSDeviceList.subList(26,32));
						list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevices";
	}

	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/releaseDeviceUK")	
	public String deviceReleaseUK(Model model,@RequestParam("deviceid")String deviceid,@RequestParam("deviceName")String deviceName,@RequestParam("vendor")String vendor,DSDevice_Details device)throws IOException,MalformedURLException, InterruptedException {


		if(vendor.equals("seetest")){
			String status = releaseDevice(deviceid);
			model.addAttribute("message",deviceName+" "+status);
			updateSeetestDevice();

		}else if (vendor.equals("perfecto_partner")){
			String reservationId="";
			try{
				reservationId = deviceCloudService.getPerfectoResevationId(deviceid);

				//System.out.println("$$$$$$ reservation id "+reservationId);
			}catch(Exception e){
				//System.out.println("DB exception");
			}
			if(!reservationId.equals("")){
				deviceCloudService.deletePerfectoDevice(deviceid);
				PerfectoAPI1 perfecto = new PerfectoAPI1();
				perfecto.releaseDevice(reservationId);
				if(!(reservationId.equals(null)|| reservationId.equals("")))
					model.addAttribute("message","Device released with "+reservationId);
				else
					model.addAttribute("message","Device not released ");	
				Thread.sleep(6000);
				List<Handset> handsetDetailList =getPerfectoDevicesList();
				deleteAllVendorDevicesDB("perfecto_partner");
				updatePerfectoDevicesDB(handsetDetailList);
			}
		}else if (vendor.equals("Emulator")){

		}
		Reserve res=deviceCloudService.getCheckMail();
		String mail=res.getMailId();
		//System.out.println("================="+mail);
		File file = new File(macroFile);
		if (file.exists()) {
			file = new File(macroFile +mail);
			file.mkdir();
			if (file.exists()) {
				if (ReleaseSM
						.confirm(macroFile, mail)) {
					//System.out.println("Mail Sent to User");
				} else {
					//System.out.println("Mail not sent to User");
				}
			} else {
				//System.out.println("File doesnot exists");
			}
		} else {
			//System.out.println("Macro File Doesnot exists");
		}
		deviceCloudService.deleteReservationTime(deviceid);

		deviceCloudService.updateReservationTimeDB();
		//db 
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
						list1 = iOSDeviceList.subList(0,6);
						list1.addAll(androidDeviceList.subList(0,4));


						list1.addAll(iOSDeviceList.subList(10,16));

						list1.addAll(androidDeviceList.subList(4,8));


						list1.addAll(iOSDeviceList.subList(26,32));
						list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesUK";
	}			





	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/perfectoCloud")
	public String listPerfectoDevices1(Model model,Device device)throws IOException {


		//System.out.println("inside perfecto");
		getPerfectoDevices(model,device);

		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudPerfecto";

	}



	public void getPerfectoDevices(Model model,Device device) throws IOException{

		List<Handset> handsetDetailList=new PerfectoAPI1().getDeviceList("anuja.saraf@capgemini.com", "Perfecto123");
		deleteAllVendorDevicesDB("perfecto_partner");
		updatePerfectoDevicesDB(handsetDetailList);
		model.addAttribute("perfectoDevice", handsetDetailList);

	}

	public void deleteAllVendorDevicesDB(String vendor){
		deviceCloudService.deleteAllVendorDevice("perfecto_partner");
	}

	public void updatePerfectoDevicesDB(List<Handset> handsetDetailList){
		for(Handset hs: handsetDetailList){
			int id=0;
			String vendor="perfecto_partner";
			String device_id=hs.getDeviceId();
			String name=hs.getDeviceName();
			String version=hs.getVersion();
			String os=hs.getOs();
			String status=hs.getStatus();
			String reservation=hs.getReservation();
			String reservation_id=hs.getReservationId();
			String udid=hs.getnativeImei();
			String devicecategory = "--";
			String agentlocation=hs.getlocation();
			String resolution=hs.getresolution();
			//System.out.println(" "+id+" "+" "+device_id+" "+" "+name+" "+" "+version+" "+" "+os+" "+" "+status+" "+" "+reservation+" "+reservation_id+" "+udid+" "+devicecategory+" "+agentlocation+" "+resolution);
			deviceCloudService.addDevices(id, vendor, device_id, name, version, os, status, reservation, reservation_id,udid,devicecategory,agentlocation,resolution);

		}
	}
	public List<Handset> getPerfectoDevicesList() throws IOException{

		List<Handset> handsetDetailList=new PerfectoAPI1().getDeviceList("anuja.saraf@capgemini.com", "Perfecto123");

		for(Handset s: handsetDetailList){
			//System.out.println(s.getSrNo()+" "+s.getDeviceName()+" "+s.getDeviceId()+" "+s.getOs()+" "+s.getVersion()+" "+s.getStatus()+" "+s.getReservationId() );
		}

		return handsetDetailList;
	}



	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/RecommendDeviceUS")
	public String recommendDeviceUS(Model model, DSDevice_Details device) throws IOException,MalformedURLException, InterruptedException {
		//System.out.println("in device recommend section");

		// change

		updateSeetestDevice();
		List<Handset> handsetDetailList=new PerfectoAPI1().getDeviceList("anuja.saraf@capgemini.com", "Perfecto123");
		deleteAllVendorDevicesDB("perfecto_partner");
		updatePerfectoDevicesDB(handsetDetailList);
		int result = deviceCloudService.updateReservationTimeDB();
		//System.out.println("update result in controler for recommend device"+result);
		//database
		Thread.sleep(3000);
		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			for(DSDevice_Details d: allDeviceList){

				//System.out.println("$$$$$$***$1"+d.getReservation_time());
			}

			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					//System.out.println("resrvation time  "+obj.getReservation_time());
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);

						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());

			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			for(DSDevice_Details d: iOSDeviceList){

				//System.out.println("$$$$$$***$2"+d.getReservation_time());
			}
			/*System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
				for(DSDevice_Details dev:iOSDeviceList){
					System.out.println(dev.getname()+"      "+dev.getversion());
				}*/
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			/*System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
				for(DSDevice_Details dev:androidDeviceList){
					System.out.println(dev.getname()+"      "+dev.getversion());
				}*/
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//  System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						// System.out.println("remain in ios "+remain);
						// System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						// System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//   System.out.println("remain in android "+remain);
						//   System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}

			for(DSDevice_Details d: list1){

				//System.out.println("$$$$$$***$"+d.getReservation_time());
			}
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevices";
	}

	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/RecommendDeviceGlobal")
	public String recommendDeviceGlobal(Model model, DSDevice_Details device) throws IOException,MalformedURLException, InterruptedException {
		//System.out.println("in device recommend section");

		// change

		updateSeetestDevice();
		List<Handset> handsetDetailList=new PerfectoAPI1().getDeviceList("anuja.saraf@capgemini.com", "Perfecto123");
		deleteAllVendorDevicesDB("perfecto_partner");
		updatePerfectoDevicesDB(handsetDetailList);
		int result = deviceCloudService.updateReservationTimeDB();
		//System.out.println("update result in controler for recommend device"+result);
		//database
		Thread.sleep(3000);

		//databa
		/*		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){


				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);



					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){




							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());
							iOSDeviceList.add(device);

						}

						else{
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());

							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());

							androidDeviceList.add(device);
						}

					}

				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());

			iOSDeviceList.addAll(iOSDeviceList1);

			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());


			list1 = iOSDeviceList.subList(0,4);
			list1.addAll(androidDeviceList.subList(0,6));


			list1.addAll(iOSDeviceList.subList(10,14));

			list1.addAll(androidDeviceList.subList(6,12));


			list1.addAll(iOSDeviceList.subList(24,28));
			list1.addAll(androidDeviceList.subList(12,18));

			System.out.println("size--- "+list1.size());
			model.addAttribute("devList1",list1);



        }*/

		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=4 , numAndroid = 6;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			System.out.println(list1);
			/*
			list1 = iOSDeviceList.subList(0,6);
			list1.addAll(androidDeviceList.subList(0,4));


			list1.addAll(iOSDeviceList.subList(10,16));

			list1.addAll(androidDeviceList.subList(4,8));


			list1.addAll(iOSDeviceList.subList(26,32));
			list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesGlobal";
	}

	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/RecommendDeviceUK")
	public String recommendDeviceUK(Model model, DSDevice_Details device) throws IOException,MalformedURLException, InterruptedException {
		//System.out.println("in device recommend section");
		// change

		updateSeetestDevice();
		List<Handset> handsetDetailList=new PerfectoAPI1().getDeviceList("anuja.saraf@capgemini.com", "Perfecto123");
		deleteAllVendorDevicesDB("perfecto_partner");
		updatePerfectoDevicesDB(handsetDetailList);
		int result = deviceCloudService.updateReservationTimeDB();
		//System.out.println("update result in controler for recommend device"+result);
		//database
		Thread.sleep(3000);
		//database
		/*try{
					List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
					allDeviceList = deviceCloudService.DeviceData();   
					System.out.println("Number of devices - "+allDeviceList.size());

					List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
					List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
					List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

					List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


					for(DSDevice_Details obj : allDeviceList){


						if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
							String[] ver = (obj.getversion()).split("\\.");
							int i = Integer.parseInt(ver[0]);



							if(obj.getos().equalsIgnoreCase("IOS")){
								//int i = Integer.parseInt(obj.getversion());
								//System.out.println("version  "+i);
								if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){




									device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());
									iOSDeviceList.add(device);

								}

								else{
									//System.out.println(i);
									device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());

									iOSDeviceList1.add(device);
								}
							}
							else if(obj.getos().equalsIgnoreCase("android")){
								System.out.println("version in android "+obj.getversion());
								if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
									//System.out.println(i);
									device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime());

									androidDeviceList.add(device);
								}

							}

						}
					}

					iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
					iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());

					iOSDeviceList.addAll(iOSDeviceList1);

					androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());


					list1 = iOSDeviceList.subList(0,6);
					list1.addAll(androidDeviceList.subList(0,4));


					list1.addAll(iOSDeviceList.subList(10,16));

					list1.addAll(androidDeviceList.subList(4,8));


					list1.addAll(iOSDeviceList.subList(26,32));
					list1.addAll(androidDeviceList.subList(8,12));

					System.out.println("size--- "+list1.size());
					model.addAttribute("devList1",list1);



		        }*/

		try{
			List<DSDevice_Details> allDeviceList = new ArrayList<DSDevice_Details>();
			allDeviceList = deviceCloudService.DeviceData();   
			//System.out.println("Number of devices - "+allDeviceList.size());

			List<DSDevice_Details> iOSDeviceList =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> iOSDeviceList1 =  new ArrayList<DSDevice_Details>();
			List<DSDevice_Details> androidDeviceList =  new ArrayList<DSDevice_Details>();

			List<DSDevice_Details> list1 = new ArrayList<DSDevice_Details>();


			for(DSDevice_Details obj : allDeviceList){

				if((!obj.getversion().equals(null) && !obj.getversion().isEmpty())){
					String[] ver = (obj.getversion()).split("\\.");
					int i = Integer.parseInt(ver[0]);
					if(obj.getos().equalsIgnoreCase("IOS")){
						//int i = Integer.parseInt(obj.getversion());
						//System.out.println("version  "+i);
						if(i >= 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList.add(device);
						}
						else if(i < 10 && (obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							iOSDeviceList1.add(device);
						}
					}
					else if(obj.getos().equalsIgnoreCase("android")){
						//System.out.println("version in android "+obj.getversion());
						if((obj.getstatus().equalsIgnoreCase("Available") || obj.getstatus().equalsIgnoreCase("In Use") || obj.getstatus().equalsIgnoreCase("Create"))){
							//System.out.println(i);
							device = new DSDevice_Details(obj.getvendor(),obj.getname(),obj.getversion(),obj.getos(),obj.getstatus(),obj.getdevice_id(),obj.getreservation(),obj.getTime(),obj.getReservation_time(),obj.getUdid(),obj.getdevicecategory(),obj.getagentlocation(),obj.getresolution());
							androidDeviceList.add(device);
						}
					}
				}
			}

			iOSDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("above version 10------------------------"+iOSDeviceList.size());
			//System.out.println("below version 10------------------------"+iOSDeviceList1.size());
			iOSDeviceList1.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			iOSDeviceList.addAll(iOSDeviceList1);
			//System.out.println("ios size ------------------------------- :  "+iOSDeviceList.size());
			for(DSDevice_Details dev:iOSDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			androidDeviceList.sort(Comparator.comparing(DSDevice_Details::getversion).reversed());
			//System.out.println("android  size ----------------------- :  "+androidDeviceList.size());
			for(DSDevice_Details dev:androidDeviceList){
				//System.out.println(dev.getname()+"      "+dev.getversion());
			}
			int startIOS = 0, startAndroid = 0;
			int numIOS=6 , numAndroid = 4;
			int l = (iOSDeviceList.size()/numIOS)>(androidDeviceList.size()/numAndroid)?(iOSDeviceList.size()/numIOS):(androidDeviceList.size()/numAndroid);
			//System.out.println("l         "+l);

			int c1=0,c2=0;
			for(int i=0;i<l+1;i++){


				if(c1 <= iOSDeviceList.size()/numIOS){
					if((startIOS+numIOS) <= iOSDeviceList.size()){
						//System.out.println("startIOS,startIOS+numIOS ------- "+startIOS+","+(startIOS+numIOS));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+numIOS));
						c1++;
					}
					else{
						int remain = iOSDeviceList.size()%numIOS;
						//System.out.println("remain in ios "+remain);
						//System.out.println("startIOS,startIOS+remain ------- "+startIOS+","+(startIOS+remain));
						list1.addAll(iOSDeviceList.subList(startIOS,startIOS+remain));
						c1++;
					}
				}
				if(c2 <= androidDeviceList.size()/numAndroid){
					if((startAndroid+numAndroid) <= androidDeviceList.size()){
						//System.out.println("startAndroid,startAndroid+numAndroid ------- "+startAndroid+","+(startAndroid+numAndroid));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+numAndroid));
						c2++;
					}
					else{
						int remain =androidDeviceList.size()%numAndroid;
						//System.out.println("remain in android "+remain);
						//System.out.println("startAndroid,startAndroid+remain ------- "+startAndroid+","+(startAndroid+remain));
						list1.addAll(androidDeviceList.subList(startAndroid,startAndroid+remain));
						c2++;
					} 
					if(startIOS+numIOS < iOSDeviceList.size())
						startIOS+=numIOS;
					if(startAndroid+numAndroid <androidDeviceList.size())
						startAndroid+=numAndroid;

				}
			}
			/*
					list1 = iOSDeviceList.subList(0,6);
					list1.addAll(androidDeviceList.subList(0,4));


					list1.addAll(iOSDeviceList.subList(10,16));

					list1.addAll(androidDeviceList.subList(4,8));


					list1.addAll(iOSDeviceList.subList(26,32));
					list1.addAll(androidDeviceList.subList(8,12));*/

			//System.out.println("final list size--- "+list1.size());
			model.addAttribute("devList1",list1);



		}
		catch (Exception e) {
			System.out.println("cannot convert to json "+ e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesUK";
	}

	//plus
	// perfecto reservation
	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/reservePerfectoDevice")	
	public String reservePerfectoDevice(Model model,@RequestParam("deviceId")String deviceId)throws IOException,MalformedURLException, InterruptedException {
		PerfectoAPI1 perfecto = new PerfectoAPI1();
		String reservationId=perfecto.reserveDevice(deviceId);
		deviceCloudService.addPerfectoDevices(deviceId, reservationId);
		Thread.sleep(4000);
		List<Handset> handsetDetailList =getPerfectoDevicesList();
		model.addAttribute("perfectoDevice", handsetDetailList);
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudPerfecto";
	}


	@RequestMapping(value = "integratedQALabs/mobileLab/deviceSelectionMatrix/releasePerfectoDevice")	
	public String releasePerfectoDevice(Model model,@RequestParam("deviceId")String deviceId)throws IOException,MalformedURLException, InterruptedException {
		String reservationId="";
		try{
			reservationId = deviceCloudService.getPerfectoResevationId(deviceId);
		}catch(Exception e){
			System.out.println("DB exception");
		}
		if(!reservationId.equals("")){
			deviceCloudService.deletePerfectoDevice(deviceId);
			PerfectoAPI1 perfecto = new PerfectoAPI1();
			perfecto.releaseDevice(reservationId);
			Thread.sleep(6000);
			List<Handset> handsetDetailList =getPerfectoDevicesList();
			model.addAttribute("perfectoDevice", handsetDetailList);
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DScloudPerfecto";
	}


	@RequestMapping(value="integratedQALabs/mobileLab/deviceSelectionMatrix/confirmmail")
	public String confirmMail(Model model,@ModelAttribute("User") User userDetails,@RequestParam ("id")int id1, HttpSession session) throws IOException,MalformedURLException {
		try{
			//System.out.println("in confirm mail");
			String id= (String) session.getAttribute("userid");
			int idd=Integer.parseInt(id);
			User user= adminService.getUser(idd);
			String emailid=user.getUserName();
			String status="Y";
			////System.out.println("==========="+id1);
			boolean reserve=deviceCloudService.addMail(id1, emailid,status);
			////System.out.println("=============="+emailid);
			File file = new File(macroFile);
			if (file.exists()) {
				file = new File(macroFile +emailid);
				file.mkdir();
				if (file.exists()) {
					if (ReleaseSM
							.confirm(macroFile, emailid)) {
						//System.out.println("Mail Sent to User");
					} else {
						//System.out.println("Mail not sent to User");
					}
				} else {
					//System.out.println("File doesnot exists");
				}
			} else {
				//System.out.println("Macro File Doesnot exists");
			}

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return "integratedQALabs/mobileLab/deviceSelectionMatrix/DSselectDevicesGlobal";
	}




}





