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
The 2024/25 season will commence on <b>Sunday 13th October</b>. Fixtures will be published by the end of September. 
<br>The line-up for each of the divisions is as follows:
<ul>
<li>Division 1 (6 teams playing each other twice each): Fareham & Crofton, Hambledon A, Hambledon B, Waterlooville A, Waterlooville B, XIIth Men A.
<li>Division 2 (6 teams playing each other twice each): Buffalo Portsmouth, Droxford, Hambledon C, Havant, Kerala A, Portsmouth & Southsea.
<li>Division 3 (6 teams playing each other twice each): Denmead, Kerala B, Knowle Village, Portsmouth, Purbrook, XIIth Men B.
<li>Division 4 (8 teams playing each other once each): Clanfield A, Clanfield B, Curdridge, Emsworth, OPCS Titchfield, Parco, Portsmouth Academics, Superkings.
<li>Colts Under-16 (10 teams playing each other once each): Fareham & Crofton, Gosport Borough, Grayshott, Hambledon, Havant, Locks Heath, Portsmouth, Portsmouth & Southsea, Sarisbury Athletic, Waterlooville.
<li>Colts Under-13 (11 teams playing each other once each): Clanfield, Fair Oak, Fareham & Crofton, Gosport Borough, Hambledon, Havant, Petersfield, Portchester, Portsmouth, Portsmouth & Southsea, Sarisbury Athletic.
</ul>
</@news>

<@news>
We have received some new stocks of indoor cricket balls, so normal arrangements should be resumed this
season with all Division 1 and Division 2 games using a new ball. 
Many thanks to 
<a href="http://www.gsam.co.uk/">Game Set and Match</a>
for their continued support.
</@news>


<#if started??>
	</div>
</#if>
