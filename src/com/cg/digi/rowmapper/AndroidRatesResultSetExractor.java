/******************************************************************************
Copyright � 2016 Capgemini Group of companies. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF Capgemini GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND Capgemini GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
******************************************************************************/

package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.cg.digi.model.Android_Rates;

public class AndroidRatesResultSetExractor implements ResultSetExtractor{

	@Override
	public Object extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
			
		Android_Rates androidRatess = new Android_Rates();
		
		androidRatess.setVer_id(Integer.parseInt(resultSet.getString("Version_ID")));
		androidRatess.setVer_name(resultSet.getString("Version_name"));
		androidRatess.setRt(Double.parseDouble(resultSet.getString("Rate")));
		
		return androidRatess;
	}

}
