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
<b>Many congratulations to last year's League champions Waterlooville</b>, who won the national 
indoor championships at Lord's on Sunday 29th March.
<br>In the semi-final they bowled out Cambridgeshire champions AK XI for 42 (Joseph Mitchell
taking 2 for 4 and Ray Reynolds 2 for 10) and scored 43 for 0 in 4.3 overs.
<br>In the final they bowled out Whitstable (Kent) for 79 (Ray Reynolds 2 for 18) and reached
the target with three wickets and ten balls to spare (Charlie Whitfield 22 not out). 
</@news>

<@news end="2026-04-19">
As a consequence of Waterlooville's success in reaching the national indoor finals, the
League's end-of-season presentation evening has been postponed from 29th March to 
<b>Sunday 19th April</b>. All are welcome to come along and celebrate the success of all
our divisional champions and award winners. 
</@news>

<@news end="2026-03-29">
We have been asked to inform all players that <b>all kit bags should be stored, while games are in
progress, in Studio 3</b>, which is the room immediately opposite the hall entrance that leads to the
walkway between the courts. They must not be left in corridors, changing rooms or toilets. 
</@news>

<#if started??>
	</div>
</#if>
