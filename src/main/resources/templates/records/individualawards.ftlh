<#macro awardh year player team performance header=false>
	<tr>
		<th class="year">${year}</th>
		<th class="winner">${player}</th>
		<th class="team">${team}</th>
		<th class="performance">${performance}</th>
	</tr>
</#macro>

<#macro award year player team performance header=false>
	<#assign prev = year - 1/>
	<#if (prev / 100)?int == (year/ 100)?int>
		<#assign season = (year % 100)?string("00")/>
	<#else>
		<#assign season = year?string.computer/>
	</#if>
	<tr>
		<td class="year">${prev?string.computer}-${season}</td>
		<td class="winner">${player}</td>
		<td class="team">${team}</td>
		<td class="performance">${performance}</td>
	</tr>
</#macro>

<div id="indivAwards">
<h1>Individual awards</h1>

<h2>A.R. Pay Batting Trophy</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award 1993 "G Marchant" "Fareham & Crofton A" "299"/>
	<@award 1994 "G Marchant" "Fareham & Crofton A" "290"/>
	<@award 1995 "L Prittipaul" "Purbrook Colts" "279"/>
	<@award 1996 "C Pay" "Harting" "325"/>
	<@award 1997 "N Cunningham" "United Services" "338"/>
	<@award 1998 "P Sparshott" "Locks Heath A" "338"/>
	<@award 1999 "G McCoy" "Purbrook A" "279"/>
	<@award 2000 "R Higgins" "Locks Heath B" "292"/>
	<@award 2001 "R Higgins" "Locks Heath B" "273"/>
	<@award 2002 "C Pay" "Harting" "273"/>
	<@award 2003 "M Hennessy" "Purbrook A" "268"/>
	<@award 2004 "J Scutt" "Hambledon" "330"/>
	<@award 2005 "C Grayston" "Bedhampton B" "268"/>
	<@award 2006 "O Jones" "Havant B" "313"/>
	<@award 2007 "J Scutt" "Hambledon" "294"/>
	<@award 2008 "R Marshall" "Hambledon" "263"/>
	<@award 2009 "S Wainwright" "Corinthians" "258"/>
	<@award 2010 "S Figgins" "Purbrook B" "239"/>
	<@award 2011 "M Le-Clercq" "Hambledon B" "302"/>
	<@award 2012 "J Floyd" "Sarisbury Athletic" "278"/>
	<@award 2013 "J Spiers" "Purbrook C" "242"/>
	<@award 2014 "J Kent" "Portsmouth Priory" "252"/>
	<@award 2015 "N Wyatt" "Portsmouth B" "321"/>
	<@award 2016 "D Clark" "Bedhampton A" "325"/>
	<@award 2017 "D Wallis" "Portsmouth B" "271"/>
	<@award 2018 "C Pratt" "Hambledon B" "285"/>
	<@award 2019 "H Peruzi" "Afghan Society Portsmouth" "313"/>
    <@award 2020 "B White" "St James Casuals" "321"/>
    <@award 2022 "M Bradley" "Clanfield A" "217"/>
    <@award 2023 "H Hovey" "Barracuda" "378"/>
    <@award 2024 "H Hovey" "Barracuda" "403"/>
</tbody>
</table>

<h2>Tony Dunster Memorial Bowling Cup</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award 1995 "J Jordan" "Petersfield" "14"/>
	<@award 1996 "P Loat" "Havant" "14"/>
	<@award 1997 "S Kelly" "Portchester A" "11"/>
	<@award 1998 "N Lane" "Denmead" "10"/>
	<@award 1999 "N Hillier" "Curdridge A" "11"/>
	<@award 2000 "J Floyd" "Sarisbury Athletic A" "12"/>
	<@award 2001 "S Kelly" "Portchester A" "12"/>
	<@award 2002 "N Crocombe" "Bedhampton A" "13"/>
	<@award 2003 "M Woodgate" "Bedhampton A" "14"/>
	<@award 2004 "N Webb" "Emsworth" "13"/>
	<@award 2005 "M Porton" "Cowplain" "12"/>
	<@award 2006 "N Kenny" "Purbrook B" "13"/>
	<@award 2007 "P Stride" "IBM South Hants A" "13"/>
	<@award 2008 "N Halliday" "Denmead" "14"/>
	<@award 2009 "F Hoolahan" "Havant A" "16"/>
	<@award 2010 "T Larner" "DOE Cavaliers" "13"/>
	<@award 2011 "D Carson" "Havant B" "12"/>
	<@award 2012 "R Marchant" "Fareham & Crofton B" "14"/>
	<@award 2013 "M Le-Clercq" "Hambledon B" "14"/>
	<@award 2014 "S Taylor" "Gosport Borough" "15"/>
	<@award 2015 "D Pitts" "Emsworth" "17"/>
	<@award 2016 "D Pitts" "Emsworth" "15"/>
	<@award 2017 "J Lewis" "Hambledon B" "13"/>
	<@award 2018 "F Gadd" "United Services" "13"/>
	<@award 2019 "A Sharma" "XIIth Men A" "17"/>
    <@award 2020 "F Gadd" "Havant A" "12"/>
    <@award 2022 "C Clark" "Fareham & Crofton" "11"/>
    <@award 2023 "J Headen" "Fareham & Crofton" "14"/>
    <@award 2024 "K Muthuraman" "Kerala A" "13"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-16 Batting Award</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award 1998 "J Blake" "Fareham & Crofton" "194"/>
	<@award 1999 "J Watts" "Fareham & Crofton" "181"/>
	<@award 2000 "D Wade" "Havant" "262"/>
	<@award 2001 "M Boyd" "Locks Heath" "254"/>
	<@award 2002 "G Ransley" "Locks Heath" "280"/>
	<@award 2003 "L Russell" "Portsmouth" "190"/>
	<@award 2004 "B Thomas" "Purbrook" "176"/>
	<@award 2005 "J Hill" "Locks Heath" "309"/>
	<@award 2006 "B Pay" "Purbrook" "298"/>
	<@award 2007 "S Knott" "Havant" "248"/>
	<@award 2008 "S Loat" "Havant" "241"/>
	<@award 2009 "T Kent" "Fareham & Crofton" "213"/>
	<@award 2010 "T Kent" "Fareham & Crofton" "364"/>
	<@award 2011 "M Crouch" "Petersfield" "309"/>
	<@award 2012 "D Sampson" "Sarisbury Athletic" "340"/>
	<@award 2013 "H Hovey" "Havant" "278"/>
	<@award 2014 "J McCoy" "Purbrook" "324"/>
	<@award 2015 "H Gadd" "Portsmouth & Southsea" "277"/>
	<@award 2016 "B Anscombe" "Petersfield" "277"/>
	<@award 2017 "J Kooner-Evans" "Portsmouth A" "276"/>
	<@award 2018 "C Pratt" "Hambledon" "274"/>
	<@award 2019 "T Collighan" "Portsmouth & Southsea" "317"/>
    <@award 2020 "W Parvin" "Hambledon A" "254"/>
    <@award 2022 "E Royle" "Gosport Borough" "153"/>
    <@award 2023 "S Harradine" "Hambledon Jets" "203"/>
    <@award 2024 "E Lewis" "Hambledon Jets" "257"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-16 Bowling Award</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award 1998 "C Stares" "Gosport Borough" "13"/>
	<@award 1999 "M Haynes" "Locks Heath" "10"/>
	<@award 2000 "L Sheppard" "Portsmouth" "10"/>
	<@award 2001 "C Stares" "Gosport Borough" "7"/>
	<@award 2002 "G Taylor" "United Services" "11"/>
	<@award 2003 "A Adams" "Locks Heath" "8"/>
	<@award 2004 "W Norman" "Hambledon" "8"/>
	<@award 2005 "S Matthews" "Purbrook" "9"/>
	<@award 2006 "S Matthews" "Purbrook" "12"/>
	<@award 2007 "A Winfield" "Havant" "9"/>
	<@award 2008 "G Chapman" "Portsmouth & Southsea" "11"/>
	<@award 2009 "G Greenwood" "Portsmouth Community" "11"/>
	<@award 2010 "A Marston" "Portsmouth" "12"/>
	<@award 2011 "S Shahid" "Portsmouth & Southsea" "12"/>
	<@award 2012 "A Marston" "Portsmouth" "11"/>
	<@award 2013 "H Whitworth" "Portsmouth & Southsea" "9"/>
	<@award 2014 "J Bowler" "Sarisbury Athletic" "9"/>
	<@award 2015 "B Marshall" "Sarisbury Athletic" "11"/>
	<@award 2016 "C Creal" "Gosport Borough" "10"/>
	<@award 2017 "F Gadd" "Portsmouth & Southsea" "9"/>
	<@award 2018 "H Woolf" "Portsmouth A" "11"/>
	<@award 2019 "C Aitken" "Gosport Borough" "9"/>
    <@award 2020 "J Oliver" "Hambledon A" "9"/>
    <@award 2022 "A Cole" "Portsmouth A" "6"/>
    <@award 2023 "F Hayes" "Hambledon Sharks" "5"/>
    <@award 2024 "T Rayner" "Fareham & Crofton" "10"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-13 Batting Award</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award 1998 "M Boyd" "Locks Heath" "128"/>
	<@award 1999 "M Journeaux" "Sarisbury Athletic" "261"/>
	<@award 2000 "E White" "Fareham & Crofton" "147"/>
	<@award 2001 "S Ransley" "Locks Heath" "191"/>
	<@award 2002 "O Winter" "Locks Heath" "274"/>
	<@award 2003 "C Morgan" "Havant" "288"/>
	<@award 2004 "J Rolfe" "Locks Heath" "205"/>
	<@award 2005 "S Loat" "Havant" "171"/>
	<@award 2006 "S Loat" "Havant" "230"/>
	<@award 2007 "T Kent" "Fareham & Crofton" "254"/>
	<@award 2008 "B Mengham" "Purbrook" "293"/>
	<@award 2009 "D Sampson" "Fareham & Crofton" "253"/>
	<@award 2010 "E Lewis" "Hambledon" "259"/>
	<@award 2011 "H Hovey" "Havant" "356"/>
	<@award 2012 "A Reynolds" "Waterlooville" "230"/>
	<@award 2013 "H Gadd" "Portsmouth & Southsea" "238"/>
	<@award 2014 "O Southon" "Fareham & Crofton" "278"/>
	<@award 2015 "T Wallis" "Portsmouth" "189"/>
	<@award 2016 "F Gadd" "Portsmouth & Southsea" "244"/>
	<@award 2017 "J Oliver" "Havant" "247"/>
	<@award 2018 "J Smitherman" "Portsmouth" "223"/>
	<@award 2019 "C Whitfield" "Havant" "161"/>
    <@award 2020 "R Reynolds" "Waterlooville" "204"/>
    <@award 2022 "H O'Hara" "Portsmouth & Southsea" "179"/>
    <@award 2023 "L Harradine" "Waterlooville" "240"/>
    <@award 2024 "H Hayes" "Hambledon Sharks" "221"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-13 Bowling Award</h2>
<table border="0">
<thead>
	<@awardh "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award 1998 "M Saunders" "Portsmouth" "4"/>
	<@award 1999 "C Stares" "Gosport Borough" "8"/>
	<@award 2000 "A Shepherd" "Waterlooville" "9"/>
	<@award 2001 "H Haran" "Purbrook" "9"/>
	<@award 2002 "C Morgan" "Havant" "8"/>
	<@award 2003 "N Hodgson" "Havant" "7"/>
	<@award 2004 "J Paskins" "Locks Heath" "10"/>
	<@award 2005 "S Knott" "Locks Heath" "7"/>
	<@award 2006 "B Kissane" "Fareham & Crofton" "9"/>
	<@award 2007 "L Le-Clercq" "Hambledon" "11"/>
	<@award 2008 "J Christian" "Purbrook" "8"/>
	<@award 2009 "M Hindry" "Locks Heath" "11"/>
	<@award 2010 "R Prentice" "Hambledon" "9"/>
	<@award 2011 "A Southon" "Fareham & Crofton" "8"/>
	<@award 2012 "O Milne" "Purbrook" "9"/>
	<@award 2013 "O Southon" "Hambledon" "8"/>
	<@award 2014 "O Pratt" "Fareham & Crofton" "11"/>
	<@award 2015 "T Wallis" "Portsmouth" "12"/>
	<@award 2016 "J Oliver" "Havant" "10"/>
	<@award 2017 "J Oliver" "Havant" "14"/>
	<@award 2018 "B Eales" "Portsmouth" "8"/>
	<@award 2019 "M Hayward" "Havant" "7"/>
    <@award 2020 "B Hardman" "Hambledon" "9"/>
    <@award 2022 "H Ferrett" "Portsmouth & Southsea" "9"/>
    <@award 2023 "J Pay" "Havant" "8"/>
    <@award 2024 "J Peat" "Portsmouth Warrior" "11"/>
</tbody>
</table>
</div>