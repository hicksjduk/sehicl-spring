<#macro performance division team performance year player="">
	<tr>
		<td class="division">${division}</td>
		<#if player != "">
			<td class="player">${player}</td>
		</#if>
		<td class="team">${team}</td>
		<td class="performance">${performance}</td>
		<td class="year">${year}</td>
	</tr>
</#macro>
<div id="recordPerformances">
<h1>Record performances</h1>
<h2>
<a id="hiteamtotal"></a>Highest team total</h2>
<table border="0">
<tbody>
<@performance "Division 1" "Havant A" "206 for 3" "2009-10"/>
<@performance "Division 2" "United Services" "192 for 3" "1991-92"/>
<@performance "Division 3" "United Services" "207 for 1" "1997-98"/>
<@performance "Division 4" "ServiceMaster" "207 for 0" "2014-15"/>
<@performance "Division 5" "Portsmouth C" "214 for 2" "2017-18"/>
<@performance "Colts Under-16" "Havant" "269 for 1" "2003-04"/>
<@performance "Colts Under-13" "Portsmouth" "227 for 1" "2017-18"/>
</tbody>
</table>
<h2>
<a id="loteamtotal"></a>Lowest team total</h2>
<table border="0">
<tbody>
<@performance "Division 1" "Portsmouth A" "11 all out" "2011-12"/>
<@performance "Division 2" "IBM South Hants A" "27 all out" "2008-09"/>
<@performance "Division 3" "Cyanamid" "12 all out" "1988-89"/>
<@performance "Division 4" "IBM South Hants B" "17 all out" "2003-04"/>
<@performance "" "Flutters" "17 all out" "2006-07"/>
<@performance "Division 5" "OPCS Titchfield" "46 all out" "2017-18"/>
<@performance "Colts Under-16" "Portsmouth" "14 all out" "2006-07"/>
<@performance "Colts Under-13" "Portsmouth & Southsea" "15 all out" "2014-15"/>
</tbody>
</table>
<h2>
<a id="hiscore"></a>Highest individual score</h2>
<table border="0">
<tbody>
<@performance "Division 1" "South Hants" "81*" "1988-89" "R Marston"/>
<@performance "Division 2" "United Services" "102*" "1988-89" "P Barsby"/>
<@performance "Division 3" "Locks Heath B" "84" "1999-2000" "R Higgins"/>
<@performance "Division 4" "OPCS Titchfield" "95*" "2016-17" "A Jagadeesh"/>
<@performance "Division 5" "Afghan Society Portsmouth" "71*" "2017-18" "H Peruzi"/>
<@performance "Colts Under-16" "Portsmouth & Southsea" "97" "2010-11" "T Benfield"/>
<@performance "Colts Under-13" "Havant" "122" "2010-11" "H Hovey"/>
</tbody>
</table>
<h2>
<a id="bestbowling"></a>Best bowling performance</h2>
<table border="0">
<tbody>
<@performance "Division 1" "Portsmouth & Southsea" "5 for 9" "1987-88" "I Chaplen"/>
<@performance "Division 2" "Portsmouth & Southsea" "5 for 12" "2013-14" "K Dunstan"/>
<@performance "Division 3" "Portchester B" "5 for 10" "2003-04" "S Gale"/>
<@performance "Division 4" "Harting" "5 for 7" "1988-89" "S Kyte"/>
<@performance "Division 5" "Swanmore" "5 for 11" "2018-19" "J Gradwell"/>
<@performance "Colts Under-16" "Fareham & Crofton" "5 for 25" "2017-18" "S Khan"/>
<@performance "Colts Under-13" "Hambledon" "4 for 0" "2008-09" "S Le-Clercq"/>
</tbody>
</table>
</div>