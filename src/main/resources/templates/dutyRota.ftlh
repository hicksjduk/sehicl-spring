<h1>Duty team rota 2021-22</h1>
<p>
	<a href="/rules#sectionT">Article T</a> of the League's rules states
	that the designated duty team for each evening must attend and operate
	the manual scoreboards for all senior (<i>not</i> Colts) games on the designated evening.
</p>
<#--
<p>The duty rota will be published shortly.</p>
-->
<table id="dutyrota">
	<tbody>
        <@duty "10th October" "Droxford" />
        <@duty "17th October" "Bosham" />
        <@duty "24th October" "United Services" />
        <@duty "31st October" "Waterlooville A" />
        <@duty "7th November" "Hambledon A" />
        <@duty "14th November" "Knowle Village" />
        <@duty "21st November" "Swanmore" />
        <@duty "28th November" "Portsmouth University A" />
        <@duty "5th December" "Barracuda" />
        <@duty "12th December" "Portsmouth A" />
        <@duty "19th December" "Curdridge" />
        <@duty "2nd January" "Havant A" />
        <@duty "9th January" "Portsmouth & Southsea" />
        <@duty "16th January" "Westbourne" />
        <@duty "23rd January" "Clanfield B" />
        <@duty "30th January" "Portsmouth University B" />
        <@duty "6th February" "Waterlooville B" />
        <@duty "13th February" "Friends United" />
        <@duty "20th February" "Portsmouth C" />
        <@duty "27th February" "Havant B" />
        <@duty "6th March" "XIIth Men B" />
        <@duty "13th March" "Clanfield A" />
        <@duty "20th March" "Hambledon C" />
        <@duty "27th March" "Hambledon B" />
	</tbody>
</table>

<#macro duty date team>
	<tr>
    	<td class="datecol">${date}</td>
		<td class="teamcol">${team}</td>
	</tr>
</#macro>