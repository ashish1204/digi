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

package com.cg.digi.service;

import java.util.List;

import com.cg.digi.model.Project;
import com.cg.digi.model.User;

/**
 * @author hapemmas
 *
 */
public interface ILoginService {
	
	public User loginValidation(String username, String password);

	public boolean checkEmail(String email);

	public boolean addUser(User userDetails);

	public boolean activateUser(String userName);

	public boolean updatePassword(String userName, String userPassword);
	public  boolean sendMail(String macroFile, String userName,
			String password);

	public List<Project> getProjects();


}
