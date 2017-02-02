<#macro mailto address domain="" description="">
<script language=\"javascript\">
	document.write(mailTo("${address}", "${domain}", "${description}", "<#nested/>"));
</script>
</#macro>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/sehicl.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/sehicl.js" type="text/javascript"></script>
<title>${title}</title>
</head>
<body id="${pageId}">
	<@mailto address="website" description="SEHICL Webmaster">
	the Webmaster
	</@mailto>
	<div id="page">
		<div id="leftNavigator">
		<#include "navigator.ftlh"/>
		</div>
		
		<div id="main">
  			<#include "header.ftlh"/>
			<div id="content">
				<#include contentTemplate/>
			</div>
			<#include "footer.ftlh"/>
		</div>
	</div>
</body>
</html>