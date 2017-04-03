package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.cg.digi.model.DesktopBrowser_Version;
import com.cg.digi.model.MobileBrowser_Version;

public class DesktopBrowserVersionResultExtractor implements ResultSetExtractor{

	@Override
	public Object extractData(ResultSet resultset) throws SQLException, DataAccessException {
		DesktopBrowser_Version desktopBrowserVersion= new DesktopBrowser_Version();
		desktopBrowserVersion.setId(Integer.parseInt(resultset.getString("id")));
		desktopBrowserVersion.setVersion(resultset.getString("version"));
		desktopBrowserVersion.setValue(Double.parseDouble(resultset.getString("value")));
		return desktopBrowserVersion;
	}

}
