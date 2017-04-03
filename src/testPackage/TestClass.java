package testPackage;



import java.util.List;

import com.cg.digi.model.Browser_Marketshare;
import com.cg.digi.service.DeviceCloudService;
import com.cg.digi.service.IDeviceCloudService;

public class TestClass {

	public static void main(String[] args) {
		
	
		IDeviceCloudService obj=new DeviceCloudService();
		List<Browser_Marketshare> a=obj.getBrowserData();
		

	}

}
