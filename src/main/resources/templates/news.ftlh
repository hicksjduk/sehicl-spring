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
<b>With the recent increase in the number of Covid-19 cases,</b> the committee is aware that this
may lead to clubs having difficulty in raising a team. If this applies to your side, and the problem
can be solved by including players who ordinarily would be ineligible to play for that team, we
invite you to contact us, <u>in advance of the game</u>, to discuss the situation.
Contact details for committee members can be found on <a href="/contacts">the Contacts page</a>.
</@news>

<@news end="2021-11-30">
<b>Two helmets were left behind by players playing on 24th October.</b> They can be
reclaimed from reception at the Leisure Centre.
</@news>

<@news end="2021-11-15">
We would like to remind all teams that <b>playing in coloured clothing is now permitted in the League</b>. 
The provisions
in the rules that require the wearing of white kit have been set aside. The following
requirements apply when coloured kit is worn:
<ul>
<li> It must be proper cricket
kit - not, for example, football or rugby shirts, and no shorts.
<li>The colour of shirts and trousers must
be uniform for all members of the same team.
<li>Pads worn by batters and wicketkeepers must be white.
<li>To avoid a clash with the colour of the ball, red kit may not be worn. If in doubt over the
acceptability of a kit colour, please consult the committee before wearing it.
</ul>
Whether white or coloured kit is worn, shoes may be coloured, and need not be of 
uniform colour across a team.
<br>The wearing of white kit is still perfectly acceptable, but is no
longer mandatory. 
</@news>

<#if started??>
	</div>
</#if>
