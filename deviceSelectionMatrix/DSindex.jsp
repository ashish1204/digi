<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Device List</title>
</head>
<body>
	<c:if test="${country == 'UK' }">
		<jsp:forward page="../../mobileLab/deviceSelectionMatrix/DSHomeUKPage"></jsp:forward>
	</c:if>
	<c:if test="${country == 'US' }">
		<jsp:forward page="../../mobileLab/deviceSelectionMatrix/DSHomePage"></jsp:forward>
	</c:if>
	<c:if test="${country == 'Global' }">
		<jsp:forward page="../../mobileLab/deviceSelectionMatrix/DSGlobalPage"></jsp:forward>
	</c:if>
	<c:if test="${country == 'User' }">
		<jsp:forward
			page="../../mobileLab/deviceSelectionMatrix/DSUserTrendsPage"></jsp:forward>
	</c:if>
	
	<c:if test="${countryBrowser == 'bGlobal' }">
		<jsp:forward
			page="../../mobileLab/deviceSelectionMatrix/BrowserIndex.jsp"></jsp:forward>
	</c:if>
	<c:if test="${countryBrowser == 'bUS' }">
		<jsp:forward
			page="../../mobileLab/deviceSelectionMatrix/BrowserIndex.jsp"></jsp:forward>
	</c:if>
	<c:if test="${countryBrowser == 'bUK' }">
		<jsp:forward
			page="../../mobileLab/deviceSelectionMatrix/BrowserIndex.jsp"></jsp:forward>
	</c:if>
</body>
</html>