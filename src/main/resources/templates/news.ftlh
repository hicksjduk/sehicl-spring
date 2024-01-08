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

<@news end="2024-01-31">
It has been the League's practice to use a new ball for all matches in Divisions 1 and 2.
Unfortunately, due to supply issues our stock of new balls is running very low; so until
further notice new balls will only be used in Division 1 matches, not in Division 2. 
</@news>

<@news end="2023-12-31">
Congratulations to <b>Luke Harradine</b> of Purbrook, on taking a hat-trick against OPCS
Titchfield on 10th December. 
</@news>

<#if started??>
	</div>
</#if>
