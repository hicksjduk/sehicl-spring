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
We are still hoping to <b>start the 2020/21 season</b> on the scheduled date of 4th October.
However, we have been advised that the Leisure Centre is not currently in a position to accept
bookings, so our planned schedule cannot be confirmed.
<br><b>Latest update (18th September):</b> The ECB have now published
<a href="https://i.emlfiles4.com/cmpdoc/0/8/7/0/3/1/files/91276_ecb-guidance-for-indoor-cricket-sept-2020_v3.pdf?utm_campaign=11827817_16092020_IndoorGuidance_ClubsLeagues&utm_medium=Email&utm_source=Email_AdHoc&email=&dm_i=1FT6,71IEH,WUIJGL,SESRO,1">guidelines on indoor cricket</a>.
The committee will be considering this over the coming days, and further information will be communicated 
as soon as possible.
</@news>

<@news>
<b>Our sponsors Game Set and Match are offering a 10% discount to participants in the League.</b> Click
on the image below to find out more.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="sehiclsecretary" domain="outlook.com" description="SEHICL Secretary">
	Adam Smith, the current Secretary,
	</@mailto> to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
