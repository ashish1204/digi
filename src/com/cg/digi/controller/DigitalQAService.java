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

package com.cg.digi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cg.digi.logger.DigiLoggerUtils;
import com.cg.digi.logger.DigiLoggerUtils.LEVEL;
import com.cg.digi.model.Batch;
import com.cg.digi.model.Defect;
import com.cg.digi.model.Device;
import com.cg.digi.model.ExecutionResults;
import com.cg.digi.model.Testcase;
import com.cg.digi.model.Tool;
import com.cg.digi.model.User;
import com.cg.digi.service.IAdminService;
import com.cg.digi.service.IDigitalQAService;
import com.cg.digi.utilities.Appbot;
import com.cg.digi.utilities.Appsee;
import com.cg.digi.utilities.DeviceDetails;
import com.cg.digi.utilities.InRoads;

/**
 * @author hapemmas
 *
 */
@Scope("session")
@Controller
public class DigitalQAService {

	@Value("${macURL}")
	String macURL;
	
	@Value("${userReviews}")
	 String userReviewPath;

	@Autowired
	IAdminService adminService;

	@Autowired
	IDigitalQAService digiQAService;

	@RequestMapping(value = "digitalQAService/accessibilityTesting/addManualChecklisttestCase")
	public String addManualChecklisttestCase(Model model) {

		Testcase testcase = new Testcase();
		model.addAttribute("testcase", testcase);

		List<Tool> tool = adminService.getAllTools();
		model.addAttribute("tools", tool);

		return "/digitalQAService/accessibilityTesting/addManualAccessibilityTC";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/addNewAccessibilityTestCase")
	public String addNewAccessibilityTestCase(Model model,
			@ModelAttribute("testcase") Testcase testcase, HttpSession session) {

		DigiLoggerUtils.log("Bean Values from user -->" + testcase, LEVEL.info);
		model.addAttribute("message", "TestCase Added Succesfully");

		Testcase successTestcase = digiQAService.addManualTestCases(testcase,
				(String) session.getAttribute("userid"));
		if (successTestcase == null) {
			model.addAttribute("errorMessage",
					"Failed to Add Testcase to Database");
		} else {
			model.addAttribute("successMessage",
					successTestcase.getTestcaseName() + " added Successfully");
		}

		List<Tool> tool = adminService.getAllTools();
		model.addAttribute("tools", tool);
		return "/digitalQAService/accessibilityTesting/addManualAccessibilityTC";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/viewTestcases")
	public String validateTestcases(Model model,
			@RequestParam("batchid") String batchid, HttpSession session) {

		List<Testcase> list = digiQAService
				.getAllocatedTestcasesToUser(batchid);
		model.addAttribute("testcases", list);
		model.addAttribute("batchid", batchid);

		List<Testcase> validatedTestcases = new ArrayList<Testcase>();
		model.addAttribute("validatedTestcases", validatedTestcases);
		return "digitalQAService/accessibilityTesting/viewTestcases";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/validateTestcases")
	public String validateTestcases(Model model, HttpSession session,
			@ModelAttribute("validatedTestcases") Testcase testcase,
			@RequestParam("batchid") String batchid) {
		
		List<Testcase> list = digiQAService.getAllTestcases(adminService
				.getTool("AccessibilityTesting").getToolid());
		model.addAttribute("testcases", list);

		String[] data = testcase.getTcstatus().split(",");
		double passCount = 0;
		double failCount = 0;
		double notApplicable = 0;
		double norun = 0;
		DigiLoggerUtils.log("Total Test case length -->>" + data.length,
				LEVEL.error);
		for (String string : data) {
			if (string.contains("PASSED")) {
				passCount++;
			} else if (string.contains("FAILED")) {
				failCount++;
			} else {
				if (string.contains("BLOCKED")) {
					failCount++;
				} else if (string.contains("NOT_COMPLETED")
						|| string.contains("NO RUN")) {
					norun++;
				}
			}
		}
		double total = passCount + failCount + norun + notApplicable;
		DigiLoggerUtils.log("Count : Passed : " + passCount + " Failed : "
				+ failCount + " NOT APPLICABLE : " + notApplicable
				+ " No Run ::" + norun + ":: Total ::" + total, LEVEL.error);

		boolean result = digiQAService.updateTestcaseResults(testcase,
				(String) session.getAttribute("userid"), passCount, failCount,
				total);
		if (result) {
			model.addAttribute("successMessage", "Results Submitted ");
		} else {
			model.addAttribute("errorMessage", "Failed to Update Results");
		}
		// transfered data
		List<Batch> batches = digiQAService
				.getAllAllocatedBatches((String) session.getAttribute("userid"));
		model.addAttribute("batches", batches);
		return "digitalQAService/accessibilityTesting/viewAllocatedBatches";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/addTestSuite")
	public String accessibilityTesting(Model model,
			@RequestParam("toolName") String toolName, HttpSession session) {

		Batch batch = new Batch();
		model.addAttribute("batch", batch);

		List<Tool> tool = adminService.getAllTools();
		model.addAttribute("tools", tool);

		List<User> users = adminService.getAllUsers((String) session
				.getAttribute("projectid"));
		model.addAttribute("users", users);

		return "digitalQAService/accessibilityTesting/addTestSuite";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/addNewTestSuite")
	public String addNewTestSuite(Model model,
			@ModelAttribute("batch") Batch batch, HttpSession session) {

		batch = adminService.newBatch(batch,
				(String) session.getAttribute("userid"));

		if (batch.getBatchname() != null) {
			model.addAttribute("successMessage", batch.getBatchname()
					+ " successfully updated in Database");
		} else {
			model.addAttribute("successMessage",
					"Failed to Add Batch in Database");
		}
		Batch batch1 = new Batch();
		model.addAttribute("batch", batch1);

		List<Tool> tool = adminService.getAllTools();
		model.addAttribute("tools", tool);

		List<User> users = adminService.getAllUsers((String) session
				.getAttribute("projectid"));
		model.addAttribute("users", users);

		List<Batch> batch3 = adminService
				.getManualTestSuiteBatches(adminService.getTool(
						"AccessibilityTesting").getToolid());
		model.addAttribute("batches", batch3);

		return "digitalQAService/accessibilityTesting/batchHome";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/batchHome")
	public String newBatch(Model model, HttpSession session) {
		List<Batch> batch = adminService.getManualTestSuiteBatches(adminService
				.getTool("AccessibilityTesting").getToolid());
		model.addAttribute("batches", batch);
		return "digitalQAService/accessibilityTesting/batchHome";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/testSuiteCreator")
	public String addTestCasesToBatch(Model model, HttpSession session,
			@RequestParam("batchid") String batchid) {
		Testcase testcase = new Testcase();
		model.addAttribute("selectedTestcases", testcase);
		List<Testcase> list = digiQAService.getAllTestcases(adminService
				.getTool("AccessibilityTesting").getToolid());
		//model.addAttribute("testcases", list);
		model.addAttribute("batchid", batchid);
		model.addAttribute("newtestcases", list);
		return "digitalQAService/accessibilityTesting/testSuiteCreator";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/addTestcasestoBatch")
	public String addTestcasestoBatch(Model model,
			@ModelAttribute("selectedTestcases") Testcase testcase,
			HttpSession session, @RequestParam("batchid") String batchid) {
		String[] data = testcase.getTestcaseid().split(",");
		int output = digiQAService.addTestcasestoBatch(data, batchid);
		List<Batch> batch = adminService.getManualTestSuiteBatches(adminService
				.getTool("AccessibilityTesting").getToolid());
		model.addAttribute("batches", batch);
		model.addAttribute("successMessage", output
				+ " : Testcases added to Suite");
		return "digitalQAService/accessibilityTesting/batchHome";
	}

	@RequestMapping(value = "digitalQAService/accessibilityTesting/viewAllocatedBatch")
	public String viewAllocatedBatch(Model model, HttpSession session) {

		List<Batch> batches = digiQAService
				.getAllAllocatedBatches((String) session.getAttribute("userid"));
		model.addAttribute("batches", batches);
		return "digitalQAService/accessibilityTesting/viewAllocatedBatches";
	}

	@RequestMapping(value = "/digitalQAService/accessibilityTesting/viewExecutionResults")
	public String viewExecutionResults(Model model,
			@RequestParam("batchid") String batchid) {

		List<ExecutionResults> results = digiQAService
				.getExecutionResults(batchid);
		model.addAttribute("results", results);
		return "digitalQAService/accessibilityTesting/viewExecutionResults";
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis")
	@ResponseBody
	public String sentimentAnalysis(Model model) {

		JSONObject listOfApps = Appbot.listYourApps("", "");
		model.addAttribute("appList", listOfApps);
		return listOfApps.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/detailReport")
	public String detailReport(Model model,
			@RequestParam("appName") String appName) {
		model.addAttribute("appName", appName);
		return "digitalQAService/userExperienceTesting/sentiment/detailReport";
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/sentimentBreakdown")
	@ResponseBody
	public String sentimentBreakdown(Model model,
			@RequestParam("appId") String appId) {
		JSONObject sentimentBreakDown = Appbot.sentimentBreakdown("", appId);
		model.addAttribute("sentimentBreakDown", sentimentBreakDown);
		return sentimentBreakDown.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/popularWords")
	@ResponseBody
	public String popularWords(Model model, @RequestParam("appId") String appId) {
		JSONObject sentimentBreakDown = Appbot.getPopularWords("", "", appId);
		return sentimentBreakDown.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/critiaclWords")
	@ResponseBody
	public String criticalWords(Model model, @RequestParam("appId") String appId) {
		JSONObject criticalWords = Appbot.getCriticalWords("", "", appId);
		return criticalWords.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/sentimentTimeline")
	@ResponseBody
	public String sentimentTimeline(Model model,
			@RequestParam("appId") String appId) {
		JSONObject sentimentBreakDown = Appbot.sentimentTimeline("", appId);

		return sentimentBreakDown.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/emotions")
	@ResponseBody
	public String emotions(Model model, @RequestParam("appId") String appId) {
		JSONObject emotions = Appbot.getEmotions("", appId);
		return emotions.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/topics")
	@ResponseBody
	public String topics(Model model, @RequestParam("appId") String appId) {
		JSONObject topics = Appbot.getTopics("", appId);
		return topics.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentimentAnalysis/reviews")
	@ResponseBody
	public String reviews(Model mode, @RequestParam("appId") String appId) {
		JSONObject reviews = Appbot.reviews("", appId);
		return reviews.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeAnalysis")
	@ResponseBody
	public String screens(Model model) {
		JSONObject screens = Appsee.getSessions("", "");
		return screens.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeUsage")
	@ResponseBody
	public String usage(Model model) {
		JSONObject usage = Appsee.getUsage("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeOS")
	@ResponseBody
	public String os(Model model) {
		JSONObject usage = Appsee.getOS("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeEvent")
	@ResponseBody
	public String eventsCount(Model model) {
		JSONObject resolution = Appsee.getEvents();
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeScreen")
	@ResponseBody
	public String Screen(Model model) {
		JSONObject resolution = Appsee.getScreen();
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeConnectivity")
	@ResponseBody
	public String Conectivity(Model model) {
		JSONObject resolution = Appsee.getConnectivity("", "");
		return resolution.toString();
	}
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/inRoadsVersion")
	@ResponseBody
	public String inRoadsVersion(Model model) {
		JSONObject resolution = Appsee.getVersions("", "");
		return resolution.toString();
	}
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/inRoadsAppVersion")
	@ResponseBody
	public String inRoadsAppVersion(Model model) {
		JSONObject resolution = InRoads.getAppVersions("", "");
		return resolution.toString();
	}
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/inRoadsAppDevice")
	@ResponseBody
	public String inRoadsAppDevice(Model model) {
		JSONObject resolution = InRoads.getAppDevice("", "");
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeResolution")
	@ResponseBody
	public String resolution(Model model) {
		JSONObject usage = Appsee.getResolution("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeTU")
	@ResponseBody
	public String topuser(Model model) {
		JSONObject usage = Appsee.getTopuser("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeCrash")
	@ResponseBody
	public String crash(Model model) {
		JSONObject usage = Appsee.getCrash("", "");
		return usage.toString();

	}

	// parametrized appsee functions below

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeAnalysis1")
	@ResponseBody
	public String screens(Model model, @RequestParam("appId") String appId) {
		JSONObject screens = Appsee.getSessions("", "");
		return screens.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeUsage1")
	@ResponseBody
	public String usage1(Model model, @RequestParam("appId") String appId) {
		JSONObject usage = Appsee.getUsage("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeOS1")
	@ResponseBody
	public String os1(Model model, @RequestParam("appId") String appId) {
		JSONObject usage = Appsee.getOS("", "");
		return usage.toString();

	}
	
	/*@RequestMapping(value = "/digitalQAService/userExperienceTesting/inRoadsVersion")
	@ResponseBody
	public String inRoadsVersion(Model model, @RequestParam("appId") String appId) {
		JSONObject usage = Appsee.getVersions("","");
		return usage.toString();

	}*/

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeEvent1")
	@ResponseBody
	public String eventsCount1(Model model, @RequestParam("appId") String appId) {
		JSONObject resolution = Appsee.getEvents();
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeScreen1")
	@ResponseBody
	public String Screen1(Model model, @RequestParam("appId") String appId) {
		JSONObject resolution = Appsee.getScreen();
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeConnectivity1")
	@ResponseBody
	public String Conectivity1(Model model, @RequestParam("appId") String appId) {
		JSONObject resolution = Appsee.getConnectivity("", "");
		return resolution.toString();
	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeResolution1")
	@ResponseBody
	public String resolution1(Model model, @RequestParam("appId") String appId) {
		JSONObject usage = Appsee.getResolution("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeTU1")
	@ResponseBody
	public String topuser1(Model model, @RequestParam("appId") String appId) {
		JSONObject usage = Appsee.getTopuser("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/appseeCrash1")
	@ResponseBody
	public String crash1(Model model, @RequestParam("appId") String appIdSS) {
		JSONObject usage = Appsee.getCrash("", "");
		return usage.toString();

	}

	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentiment/logDefect")
	@ResponseBody
	public String logDefect(Model model,HttpSession session,HttpServletRequest request,
			@RequestParam("defectname") String defectName,
			@RequestParam("defectdescription") String defectDescription,
			@RequestParam("buildid") String buildId) {
	
		Defect defect=new Defect();
		defect.setDefectname(defectName);
		defect.setDefectdescription(defectDescription);
		defect.setBuildid(buildId);
		defect.setToolid("8");
		defect.setUserid((String) session.getAttribute("userid"));
		defect.setBuildid(buildId);
		defect.setProjectid((String)session.getAttribute("testcaseid"));
		
		
		if(digiQAService.logDefect(defect)){
		DigiLoggerUtils.log("Deffect Added Successfully...", LEVEL.info, DigitalQAService.class);
		return "Deffect Added Successfully...";
		}else{
			DigiLoggerUtils.log("Error while adding a Deffect...", LEVEL.error, DigitalQAService.class);
			return "Error while adding a Deffect...";
		}
		
	}
	
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentiment/suggest")
	@ResponseBody
	public String suggest(Model model,HttpSession session,HttpServletRequest request,
			@RequestParam("suggestion") String defectName,
			@RequestParam("suggestiondetail") String defectDescription,
			@RequestParam("buildid") String buildId) {
	
		Defect defect=new Defect();
		defect.setDefectname(defectName);
		defect.setDefectdescription(defectDescription);
		defect.setBuildid(buildId);
		defect.setToolid("8");
		defect.setUserid((String) session.getAttribute("userid"));
		defect.setBuildid(buildId);
		defect.setProjectid((String)session.getAttribute("testcaseid"));
		
		
		if(digiQAService.addSuggest(defect)){
		DigiLoggerUtils.log("Suggestion Added Successfully...", LEVEL.info, DigitalQAService.class);
		return "Thanks for your Suggestion...";
		}else{
			DigiLoggerUtils.log("Error while adding a Suggestion...", LEVEL.error, DigitalQAService.class);
			return "Error while adding a Suggestion...";
		}
		
	}
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentiment/addTestCase")
	public String addTestCase(Model model,HttpSession session,
			@RequestParam("defectname") String defectName,
			@RequestParam("defectdescription") String defectDescription,
			@RequestParam("buildid") String buildId) {
		Defect defect=new Defect();
		defect.setDefectname(defectName);
		defect.setDefectdescription(defectDescription);
		defect.setBuildid(buildId);
		defect.setToolid("8");
		defect.setUserid((String) session.getAttribute("userid"));
		defect.setBuildid(buildId);
		defect.setProjectid((String)session.getAttribute("testcaseid"));
		
		
		if(digiQAService.logDefect(defect)){
		DigiLoggerUtils.log("Deffect Added Successfully...", LEVEL.info, DigitalQAService.class);
		}else{
			DigiLoggerUtils.log("Error while adding a Deffect...", LEVEL.info, DigitalQAService.class);
		}
		return "/digitalQAService/userExperienceTesting/sentiment/detailReport";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/sentiment/getCriticalReviews")
	@ResponseBody
	public String getCriticalReviews(Model model,
			@RequestParam("appId") String appId,
			@RequestParam("word") String word){
		
		org.json.simple.JSONArray appReview=(org.json.simple.JSONArray) Appbot.getUserReviews("", appId,userReviewPath).get("results");
		org.json.simple.JSONArray filteredReviews=new org.json.simple.JSONArray();
	for (int i = 0; i < appReview.size(); i++) {
		org.json.simple.JSONObject currentReview=(org.json.simple.JSONObject) appReview.get(i);
		if(currentReview.get("body").toString().toLowerCase().matches(".*"+word.toLowerCase()+".*") ||currentReview.get("subject").toString().toLowerCase().matches(".*\\b"+word.toLowerCase()+"\\b.*")){
			filteredReviews.add(currentReview);
		}
	}
		return filteredReviews.toString();
	}
	
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/getAllDevices")
	@ResponseBody
	public String getAllDevices(Model model,@RequestParam("os") String os,
			@RequestParam("version") String version){
		 JSONArray devices = null ;
		try {
		  devices = DeviceDetails.getSeetestDevices(os, version);
		  System.out.println(devices);
			DigiLoggerUtils.log("SeeTest Devices:"+devices, LEVEL.info);
			
		} catch (IOException e) {
			DigiLoggerUtils.log("Error while getting SeeTest Cloud devices SeeTest Devices", LEVEL.warn);
		}
				return devices.toString();
		
	}
	@RequestMapping(value = "/digitalQAService/userExperienceTesting/reserveDevice")
	@ResponseBody
	public String reserveDevice(Model model,@RequestParam("deviceId") String deviceId){
		 String device = null ;
		try {
			device = DeviceDetails.reserveSeetestDevice(deviceId);
			DigiLoggerUtils.log("SeeTest Devices:"+device, LEVEL.info);
			
		} catch (IOException e) {
			DigiLoggerUtils.log("Error while getting SeeTest Cloud devices SeeTest Devices", LEVEL.warn);
		}
				return device;
		
	}

}
