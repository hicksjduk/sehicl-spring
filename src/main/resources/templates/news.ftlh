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

<@news>
We have been informed by the Leisure Centre that complaints were received on Sunday (10th March), about a smell 
outside the entrance of the Centre which suggested that illegal drugs were being smoked; and
CCTV footage indicates that cricketers may have been responsible. The Committee will be taking
steps to identify the culprits, but in the meantime we would like to remind everyone involved in
the League that this is not acceptable behaviour while people are at the Centre for cricket.
</@news>

<#if started??>
	</div>
</#if>
