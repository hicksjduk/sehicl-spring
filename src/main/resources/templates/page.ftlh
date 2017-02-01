<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/sehicl.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/sehicl.js" type="text/javascript"></script>
<title>${title}</title>
</head>
<body id="${pageId}">
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