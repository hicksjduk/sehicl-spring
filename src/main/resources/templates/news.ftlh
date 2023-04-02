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

<@news end="2023-04-02">
We have been asked to draw the attention of participants in the League to the Leisure Centre's
policy that <b>no filming or photography may take place without first gaining the Centre's
express permission.</b> This includes the use of helmet cameras.
</@news>

<@news end="2023-04-30">
<b>Our sponsors Game Set & Match</b> have asked us to draw attention to the range of cricket equipment
they currently have available, or are expecting to have in stock soon. 
<a href="https://www.gsam.co.uk/collections/cricket-2023?sort_by=created-descending">Click here to see
more details.</a>
</@news>

<#if started??>
	</div>
</#if>
