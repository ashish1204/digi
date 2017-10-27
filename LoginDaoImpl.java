/******************************************************************************
Copyright © 2016 Capgemini Group of companies. All rights reserved
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

package com.cg.digi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.cg.digi.logger.DigiLoggerUtils;
import com.cg.digi.logger.DigiLoggerUtils.LEVEL;
import com.cg.digi.model.Project;
import com.cg.digi.model.User;


@Component("loginDao")
public class LoginDaoImpl implements ILoginDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public User loginValidation(String username, String password) {
		String sql = "SELECT u.*,r.rolename from users u, roles r where username=? and userpassword=? AND u.roleid=r.roleid";
		User user = null;
		try {
			user = jdbcTemplate.queryForObject(sql,new Object[]{username,password}, new BeanPropertyRowMapper<User>(User.class));
			DigiLoggerUtils.log("User details in Dao loginValidation() : "+user, LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log("DataAccessException in loginValidation() : "+e, LEVEL.error);
		}catch (Exception e) {
			DigiLoggerUtils.log("Exception in loginValidation() : "+e, LEVEL.error);
		}
		return user;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkEmail(String email) {

		int  count =0;
		boolean flag=false;
		try {
			String query = "SELECT COUNT(*) from users where email='"+email+"'";
			
			count = jdbcTemplate.queryForInt(query);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		if(count>0){
			flag=true;
		} 
		return flag;
	
	}

	@Override
	public boolean addUser(User userDetails) {

		boolean flag=false;
		try{
		String query="INSERT INTO users (username,userpassword,firstname,lastname,roleid,createdby,creationtime,projectid,email,status) VALUES(?,?,?,?,?,?,current_timestamp,?,?,?)";
		Object[] params={userDetails.getEmail(),
				userDetails.getUserPassword(),
				userDetails.getFirstName(),
				userDetails.getLastName(),
				"2","1","1",
				userDetails.getEmail(),
				"N"
		};
		int result=jdbcTemplate.update(query, params);
		if(result==1){
			flag=true;
		}
		}catch(DataAccessException e){
			e.printStackTrace();
		}
		return flag;
	
	}

	@Override
	public boolean activateUser(String userName) {


		String query = "UPDATE users SET status = 'Y' WHERE username = '"+userName+"'";
		
		int temp = 0;
		try {
			temp = jdbcTemplate.update(query);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		if (temp > 0) {

			return true;
		} else {
			return false;
		}
	
	}

	@Override
	public boolean updatePassword(String userName, String userPassword) {


		String query = "UPDATE users SET userpassword = '"+userPassword+"' WHERE username = '"+userName+"'";
		
		int temp = 0;
		try {
			temp = jdbcTemplate.update(query);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		if (temp > 0) {

			return true;
		} else {
			return false;
		}
		}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Project> getAllProjects() {
		List<Project> list = null;
		try {
			list = jdbcTemplate.query("SELECT * FROM project",
					new BeanPropertyRowMapper(Project.class));
			DigiLoggerUtils
					.log("Data while retrieving all users from Database ::: "
							+ list, LEVEL.trace);
		} catch (DataAccessException e) {
			DigiLoggerUtils.log(
					"Error while retrieving all users from Database ::: "
							+ e.getMessage(), LEVEL.error);
		}
		return list;
	}
	
	
	
	
	

}
