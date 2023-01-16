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

<@news end="2023-03-01">
We have been asked to draw the attention of participants in the League to the Leisure Centre's
policy that <b>no filming or photography may take place without first gaining the Centre's
express permission.</b> This includes the use of helmet cameras.
</@news>

<@news end="2023-02-01">
<b>A number of helmets have been left at the Leisure Centre.</b>
If you have lost a helmet and would like to reclaim it, please ask at Leisure Centre reception. 
</@news>

<@news end="2023-02-01">
<b>The League has a large number of used indoor cricket balls which it no longer needs.</b>
If anyone would like some, please contact a member of the committee. 
</@news>

<#if started??>
	</div>
</#if>
