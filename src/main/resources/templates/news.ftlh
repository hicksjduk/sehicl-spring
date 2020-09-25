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
<b>Latest Covid-19 news</b>
<br>On 22nd September, the Leisure Centre advised us that they are still not in a position to allow us
to play there, and they have no idea when that will change. Also, the Prime Minister announced that from
Monday 28th September, the "rule of six" is to be extended to cover indoor team sports.
<br>The following day, the League committee met and decided that, as a consequence of these facts: 
<br>(a) all matches scheduled for the rest of 2020
will be cancelled. 
<br>(b) all monies already paid by clubs in respect of the 2020/21 season will be refunded. 
<br>We
still hope to start a truncated season on 3rd January 2021, and if that proves to be possible the
clubs that are interested will be asked to commit financially to participating.
<br>Further information will be published here as soon as anything changes.
<br>Read 
<a href="https://i.emlfiles4.com/cmpdoc/0/8/7/0/3/1/files/91276_ecb-guidance-for-indoor-cricket-sept-2020_v3.pdf?utm_campaign=11827817_16092020_IndoorGuidance_ClubsLeagues&utm_medium=Email&utm_source=Email_AdHoc&email=&dm_i=1FT6,71IEH,WUIJGL,SESRO,1">the ECB's guidelines 
for Covid-safe indoor cricket</a>.
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
