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

<@news end="2022-01-30">
<b><u>All matches scheduled for 16th and 23rd January have been cancelled</u></b>
<br>With the sustained high levels of new Covid cases in the last few weeks and the ever-present 
likelihood of close contact pinging rearing its head, the committee has decided to
cancel the matches due to be played on 16th and 23rd January. We remain hopeful that play
can resume on 30th January, but are monitoring the situation and will make further announcements in
due course.
</@news>

<@news>
<b>With the recent increase in the number of Covid-19 cases,</b> the committee is aware that this
may lead to clubs having difficulty in raising a team. If this applies to your side, and the problem
can be solved by including players who ordinarily would be ineligible to play for that team, we
invite you to contact us, <u>in advance of the game</u>, to discuss the situation.
Contact details for committee members can be found on <a href="/contacts">the Contacts page</a>.
</@news>

<@news>
<b>The attention of everyone involved in the League is drawn to the following important Covid precautions</b>,
which were sent to club secretaries at the beginning of the season but have not been widely followed so far.
<ul>
<li>The changing rooms at the Leisure Centre are available for use, but we strongly advise players to arrive in their kit and ready to play.
<li>Participants are asked to try to arrive no earlier than 20 minutes before the scheduled start time of their game.
<li>Scorers are asked to bring their own pens and clipboards. Scoresheets will be provided as usual, but pens will not be available, nor will anything to rest a scoresheet on.
<li>The submission of hard-copy result sheets is strongly discouraged. Those who are unable or unwilling to submit results via email are asked to arrange for another member of their team to do so.
<li>Participants are not required to wear a face covering - and those actually playing should not wear one unless advised to do so by a doctor - but are welcome to do so if they wish.
<li>Balls, stumps and other things that are touched by multiple people will be sanitised between innings and between matches. The duty team is asked to sanitise the scoreboards at the end of the evening. Suitable sanitising equipment will be provided.
<li>Nobody who has recently tested positive for Covid-19, or who has Covid-like symptoms, should come to the Leisure Centre.
<li>If a team thinks they may have problems raising a side because of players contracting Covid, they are asked to give as much notice as possible to the umpires' co-ordinator and the opposition. A team that is unable to raise a side for this reason can call off the game without penalty, provided that they contact the umpires' co-ordinator and the opposition, <u>by telephone</u>, no later than 2pm on the day of the game.
<li>We strongly urge that the number of spectators be limited - for Colts games no more than one person should accompany each player to the Leisure Centre if at all possible - and that safe distances should be left between people of different households.
<li>During play, the members of the batting side who are not actually batting <u>must not</u> cluster around the door leading to the hall. Only the next batter to come in may stand there. All other members of the batting side who wish to watch the game must do so from the viewing gallery upstairs.
</ul>
</@news>

<@news end="2021-11-30">
<b>Portsmouth University B have withdrawn from the League.</b>
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
