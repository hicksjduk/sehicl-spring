<h1>Duty team rota 2023-24</h1>
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
        <@duty "24th September" "Knowle Village" />
        <@duty "1st October" "Swanmore" />
        <@duty "15th October" "Curdridge" />
        <@duty "22nd October" "Havant B" />
        <@duty "29th October" "Bedhampton" />
        <@duty "5th November" "Hambledon C" />
        <@duty "12th November" "Kerala A" />
        <@duty "19th November" "Barracuda" />
        <@duty "26th November" "Waterlooville A" />
        <@duty "3rd December" "Portsmouth & Southsea" />
        <@duty "10th December" "Leigh Park United" />
        <@duty "17th December" "Waterlooville B" />
        <@duty "7th January" "XIIth Men B" />
        <@duty "14th January" "Kerala B" />
        <@duty "21st January" "Droxford" />
        <@duty "28th January" "Buffalo Portsmouth" />
        <@duty "4th February" "Parco" />
        <@duty "11th February" "Hambledon A" />
        <@duty "18th February" "Clanfield" />
        <@duty "25th February" "Purbrook" />
        <@duty "3rd March" "XIIth Men A" />
        <@duty "10th March" "Havant A" />
        <@duty "17th March" "Hambledon B" />
	</tbody>
</table>

<#macro duty date team>
	<tr>
    	<td class="datecol">${date}</td>
		<td class="teamcol">${team}</td>
	</tr>
</#macro>