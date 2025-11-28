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

<@news end="2025-12-31">
Unfortunately, <b>Fair Oak have decided to withdraw from the Under-13 league</b>. All
their results to date have been expunged from the record, and their forthcoming matches
will not take place. 
</@news>

<#if started??>
	</div>
</#if>
