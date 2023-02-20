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

<@news end="2023-02-26">
<b>Congratulations to League champions Havant</b>, who won the Hampshire indoor championship on
18th February.
Their next match is a playoff against the Dorset champions on 26th February. 
</@news>

<#if started??>
	</div>
</#if>
