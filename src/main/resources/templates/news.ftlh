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
	</#if>
</#macro>

<@news end="2018-06-04">
<b>The Annual General Meeting of the League</b> will be held at Fareham Leisure Centre on Monday
4th June at 7.30pm. Each club in the League is required to send a representative.<br>
At this meeting, we will need to elect a replacement for the League's Hon Treasurer, Barry Plumb, 
who is stepping down after 29 years of service.
We are grateful to Barry for the excellent job he has done, and would be pleased to hear from anyone who
is interested in succeeding him in this post.
Please contact <@mailto "secretary">the Secretary</@mailto> 
to express your interest. 
</@news>

<@news end="2018-10-01">
<b>The League's <a href="/dp">Data Protection Policy</a> is now available.</b>
Please contact <@mailto "dpo">the Data Protection Officer</@mailto> 
if you have any concerns or questions regarding the League's processing of personal data. 
</@news>

<#if started??>
	</div>
</#if>
