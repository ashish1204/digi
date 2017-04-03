package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.cg.digi.model.Browser_Marketshare;
import com.cg.digi.model.Vendor_MarketShare;

public class browserMarketshareResultExtractor implements ResultSetExtractor {

	@Override
	public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
	Browser_Marketshare browserMarketshare= new Browser_Marketshare();
		
	browserMarketshare.setSerialNo(Integer.parseInt(resultSet.getString("Sr_No")));
	browserMarketshare.setBrowserName(resultSet.getString("Browser_Name"));
	browserMarketshare.setMonth(resultSet.getString("Month"));
	browserMarketshare.setValue(Double.parseDouble(resultSet.getString("Value")));
		
		return browserMarketshare;
	}

}
