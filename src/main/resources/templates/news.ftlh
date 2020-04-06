<#assign today = .now?date?string.iso/>
<#macro news start=today end=today>
	<!-- News start: ${start}, end: ${end}, today: ${today} -->
	<#if start?date.iso <= today?date.iso && today?date.iso <= end?date.iso>
		<#if !started??>
			<div id="news">
			<#assign started = true/>
		</#if>
		<p>
			<#nested/>
		</p>
	<#else>
		<!-- 
			<#nested/>
		-->
	</#if>
</#macro>

<@news end="2020-03-29">
<b>Coronavirus</b>
<p>Following the latest changes to Government advice, the remainder of the 2019/20 season is
<b><u>cancelled</u></b>. There will be no matches on 22nd March, and the end-of-season presentation
evening on 29th March will not take place. We will be in touch with award winners later to arrange
to get their awards to them.
</@news>

<@news>
<b>Our sponsors Game Set and Match are offering a 10% discount to participants in the League.</b> Click
on the image below to find out more.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="secretary" description="SEHICL Secretary">
	Adam Smith, the current Secretary,
	</@mailto> to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
