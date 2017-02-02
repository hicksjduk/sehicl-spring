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

<@news end="2016-12-31">
<b>We are looking for teams to volunteer to trial a different type of
indoor ball</b> during some games between now and the end of the season. We
are in possession of six balls of the new type and would like to see how they
play under match conditions. The ball itself is fractionally lighter
than the one used at present and has a significantly more prominent
seam.
<br>If you would like to be considered to use the new type of ball,
please let us know so that we can start the trial. For the avoidance of
doubt, the trial matches will count towards the league standings and
unless both teams volunteer to use the new type of ball, the present
indoor ball will be used. We will encourage feedback from the teams and
officials and a decision will then be made at committee level as to the
next step, if indeed there is one.
<br>Some teams had a chance to look at
the ball on 20th November and I was encouraged by how it was received. Can
those teams that indicated to me then that they would be prepared
to give it a go please confirm so that the list can be started.
<br>Thank you for your co-operation. 
<br>Richard Matthews
</@news>

<@news>
We would like to remind all players of the arrangements for 
<b>restructuring the senior divisions</b> of the League. 
<br>As agreed at last year's Annual General Meeting, from 
the 2017/18 season onward the
League will have five senior divisions instead of the current four.
Divisions 1 and 2 will each contain six teams, and Divisions 3, 4 and 5
ten teams. To achieve this, <u>there will be no promotion</u>  
at the end of the 2016/17 season; all teams will either stay in the same
division, or go down one or two divisions. For full details of the
arrangements that will apply, please look at <a href="rules#sectionM">
the relevant section of the Rules.</a>
</@news>

<@news>
<b>This site is currently under reconstruction</b>
after our existing web hosting service was withdrawn. Some facilities
may be unavailable for a while (the links will not work). For further
information, please contact <@mailto address="website">the Webmaster</@mailto>.
</@news>

<#if started??>
	</div>
</#if>
