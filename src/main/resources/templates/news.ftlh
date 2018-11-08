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

<@news end="2018-11-11">
In commemoration of the centenary of the end of the First World War, there will be 
<b>a two-minute silence</b> on Sunday 11th November. This will take place at about 6.15pm,
between the Under-16 matches and the first senior games of the evening. All are welcome to
come into the hall to participate in this.
</@news>

<#if started??>
	</div>
</#if>
