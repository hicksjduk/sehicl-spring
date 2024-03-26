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

<@news>
The Committee is considering the possibility of <b>running a women's league</b> next season,
and would like to gauge interest. Any club (whether or not they are currently a member of the League)
who would be interested in having a women's team in such a competition is invited to contact
<@mailto address="secretary" description="SEHICL Secretary">the Secretary</@mailto> as
soon as possible, and preferably before the Annual General Meeting which is on Monday 3rd June.
</@news>

<#if started??>
	</div>
</#if>
