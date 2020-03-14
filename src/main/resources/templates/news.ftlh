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

<@news end="2020-03-29">
<b>Coronavirus</b>
<p>Following the recent cancellation of many sporting events due to the coronavirus
outbreak, the League is committed to following&nbsp;- but not going beyond&nbsp;- the advice
given by the Government based on the latest scientific advice.
<ul>
<li>Unless and until specific guidance is received otherwise, the league programme will continue.
<li>Any player or umpire who develops a persistent cough or a high temperature <u>must not come 
to the Leisure Centre under any circumstances</u>. 
<li>We encourage all participants to observe good hygiene, and avoid handshakes 
(though non-contact ways of acknowledging each other are encouraged).
<li>No team is being forced to play. Any team that does not want to play, or cannot raise a
team, because of genuine concerns over coronavirus will not be penalised for failing to fulfil
a fixture, provided that the opposition and the umpires' coordinator are informed by midday
on the day of the game; instead of being treated as a default, the match will simply be cancelled.
</ul>
We continue to monitor the advice given by the Government and relevant sporting bodies, and will
communicate any changes to the position stated above as soon as possible.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="secretary" description="SEHICL Secretary">
	Adam Smith, the current Secretary,
	</@mailto> to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
