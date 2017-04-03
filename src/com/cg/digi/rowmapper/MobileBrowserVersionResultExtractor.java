package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.cg.digi.model.MobileBrowser_Version;

public class MobileBrowserVersionResultExtractor implements ResultSetExtractor{

	@Override
	public Object extractData(ResultSet resultset) throws SQLException, DataAccessException {
		
		MobileBrowser_Version mobileBrowserVersion= new MobileBrowser_Version();
		mobileBrowserVersion.setId(Integer.parseInt(resultset.getString("id")));
		mobileBrowserVersion.setVersion(resultset.getString("version"));
		mobileBrowserVersion.setValue(Double.parseDouble(resultset.getString("value")));
		return mobileBrowserVersion;
	}

}
