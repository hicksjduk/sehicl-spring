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

<@news end="2025-03-31">
Can we remind all players that the studio, opposite Court B, is for the storage of bags and equipment only, it is 
not to be used as a changing room.
</@news>

<@news end="2025-03-17">
Hambledon won the Hampshire Indoor Championships last week. We wish them the best of luck for the regional finals
in Taunton on the 16th March.
</@news>

<@news end="2025-02-24">
It has been brought to our attention that the number of people in the scoring area has increased recently.<br><br>

For colts' matches, colts' managers and a coach are permitted in the area, as are the players, for safeguarding
reasons. One extra person to help with the administration of fielders' helmets is preferred, to aid pace of play,
and a person to update the manual scoreboard is also required. All other team officials and parents must watch from the
viewing area upstairs.<br><br>

For adult cricket, no players are allowed in the scoring area during their team's match, unless directed by a League 
official or umpire. Prior to their match, of course, captains must use the area to deal with pre-match admin and the toss.
</@news>

<@news end="2024-11-17">
Congratulations to <b>Xavier Warnes</b> of Havant, whose figures of 5 for 20 against Gosport Borough on 20th October
were a new Colts Under-13 record, and in fact the first five-for ever taken in the 27-year history of the Under-13 league.
</@news>

<@news end="2024-10-13">
<b><a href="/fixtures">The fixtures are now available</a></b> for the 2024/25 season, which will commence on Sunday 13th October. 
</@news>

<@news end="2024-10-31">
We have received some new stocks of indoor cricket balls, so normal arrangements should be resumed this
season with all Division 1 and Division 2 games using a new ball. 
Many thanks to 
<a href="http://www.gsam.co.uk/">Game Set and Match</a>
for their continued support.
</@news>


<#if started??>
	</div>
</#if>
