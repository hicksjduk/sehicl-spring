<#macro award year player team performance header=false>
	<#assign tag=header?then("th", "td")/>
	<tr>
		<${tag} class="year">${year}</${tag}>
		<${tag} class="winner">${player}</${tag}>
		<${tag} class="team">${team}</${tag}>
		<${tag} class="performance">${performance}</${tag}>
	</tr>
</#macro>

<div id="indivAwards">
<h1>Individual awards</h1>

<h2>A.R. Pay Batting Trophy</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award "1992-93" "G Marchant" "Fareham & Crofton A" "299"/>
	<@award "1993-94" "G Marchant" "Fareham & Crofton A" "290"/>
	<@award "1994-95" "L Prittipaul" "Purbrook Colts" "279"/>
	<@award "1995-96" "C Pay" "Harting" "325"/>
	<@award "1996-97" "N Cunningham" "United Services" "338"/>
	<@award "1997-98" "P Sparshott" "Locks Heath A" "338"/>
	<@award "1998-99" "G McCoy" "Purbrook A" "279"/>
	<@award "1999-2000" "R Higgins" "Locks Heath B" "292"/>
	<@award "2000-01" "R Higgins" "Locks Heath B" "273"/>
	<@award "2001-02" "C Pay" "Harting" "273"/>
	<@award "2002-03" "M Hennessy" "Purbrook A" "268"/>
	<@award "2003-04" "J Scutt" "Hambledon" "330"/>
	<@award "2004-05" "C Grayston" "Bedhampton B" "268"/>
	<@award "2005-06" "O Jones" "Havant B" "313"/>
	<@award "2006-07" "J Scutt" "Hambledon" "294"/>
	<@award "2007-08" "R Marshall" "Hambledon" "263"/>
	<@award "2008-09" "S Wainwright" "Corinthians" "258"/>
	<@award "2009-10" "S Figgins" "Purbrook B" "239"/>
	<@award "2010-11" "M Le-Clercq" "Hambledon B" "302"/>
	<@award "2011-12" "J Floyd" "Sarisbury Athletic" "278"/>
	<@award "2012-13" "J Spiers" "Purbrook C" "242"/>
	<@award "2013-14" "J Kent" "Portsmouth Priory" "252"/>
	<@award "2014-15" "N Wyatt" "Portsmouth B" "321"/>
	<@award "2015-16" "D Clark" "Bedhampton A" "325"/>
	<@award "2016-17" "D Wallis" "Portsmouth B" "271"/>
</tbody>
</table>

<h2>Tony Dunster Memorial Bowling Cup</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award "1994-95" "J Jordan" "Petersfield" "14"/>
	<@award "1995-96" "P Loat" "Havant" "14"/>
	<@award "1996-97" "S Kelly" "Portchester A" "11"/>
	<@award "1997-98" "N Lane" "Denmead" "10"/>
	<@award "1998-99" "N Hillier" "Curdridge A" "11"/>
	<@award "1999-2000" "J Floyd" "Sarisbury Athletic A" "12"/>
	<@award "2000-01" "S Kelly" "Portchester A" "12"/>
	<@award "2001-02" "N Crocombe" "Bedhampton A" "13"/>
	<@award "2002-03" "M Woodgate" "Bedhampton A" "14"/>
	<@award "2003-04" "N Webb" "Emsworth" "13"/>
	<@award "2004-05" "M Porton" "Cowplain" "12"/>
	<@award "2005-06" "N Kenny" "Purbrook B" "13"/>
	<@award "2006-07" "P Stride" "IBM South Hants A" "13"/>
	<@award "2007-08" "N Halliday" "Denmead" "14"/>
	<@award "2008-09" "F Hoolahan" "Havant A" "16"/>
	<@award "2009-10" "T Larner" "DOE Cavaliers" "13"/>
	<@award "2010-11" "D Carson" "Havant B" "12"/>
	<@award "2011-12" "R Marchant" "Fareham & Crofton B" "14"/>
	<@award "2012-13" "M Le-Clercq" "Hambledon B" "14"/>
	<@award "2013-14" "S Taylor" "Gosport Borough" "15"/>
	<@award "2014-15" "D Pitts" "Emsworth" "17"/>
	<@award "2015-16" "D Pitts" "Emsworth" "15"/>
	<@award "2016-17" "J Lewis" "Hambledon B" "13"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-16 Batting Award</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award "1997-98" "J Blake" "Fareham & Crofton" "194"/>
	<@award "1998-99" "J Watts" "Fareham & Crofton" "181"/>
	<@award "1999-2000" "D Wade" "Havant" "262"/>
	<@award "2000-01" "M Boyd" "Locks Heath" "254"/>
	<@award "2001-02" "G Ransley" "Locks Heath" "280"/>
	<@award "2002-03" "L Russell" "Portsmouth" "190"/>
	<@award "2003-04" "B Thomas" "Purbrook" "176"/>
	<@award "2004-05" "J Hill" "Locks Heath" "309"/>
	<@award "2005-06" "B Pay" "Purbrook" "298"/>
	<@award "2006-07" "S Knott" "Havant" "248"/>
	<@award "2007-08" "S Loat" "Havant" "241"/>
	<@award "2008-09" "T Kent" "Fareham & Crofton" "213"/>
	<@award "2009-10" "T Kent" "Fareham & Crofton" "364"/>
	<@award "2010-11" "M Crouch" "Petersfield" "309"/>
	<@award "2011-12" "D Sampson" "Sarisbury Athletic" "340"/>
	<@award "2012-13" "H Hovey" "Havant" "278"/>
	<@award "2013-14" "J McCoy" "Purbrook" "324"/>
	<@award "2014-15" "H Gadd" "Portsmouth & Southsea" "277"/>
	<@award "2015-16" "B Anscombe" "Petersfield" "277"/>
	<@award "2016-17" "J Kooner-Evans" "Portsmouth A" "276"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-16 Bowling Award</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award "1997-98" "C Stares" "Gosport Borough" "13"/>
	<@award "1998-99" "M Haynes" "Locks Heath" "10"/>
	<@award "1999-2000" "L Sheppard" "Portsmouth" "10"/>
	<@award "2000-01" "C Stares" "Gosport Borough" "7"/>
	<@award "2001-02" "G Taylor" "United Services" "11"/>
	<@award "2002-03" "A Adams" "Locks Heath" "8"/>
	<@award "2003-04" "W Norman" "Hambledon" "8"/>
	<@award "2004-05" "S Matthews" "Purbrook" "9"/>
	<@award "2005-06" "S Matthews" "Purbrook" "12"/>
	<@award "2006-07" "A Winfield" "Havant" "9"/>
	<@award "2007-08" "G Chapman" "Portsmouth & Southsea" "11"/>
	<@award "2008-09" "G Greenwood" "Portsmouth Community" "11"/>
	<@award "2009-10" "A Marston" "Portsmouth" "12"/>
	<@award "2010-11" "S Shahid" "Portsmouth & Southsea" "12"/>
	<@award "2011-12" "A Marston" "Portsmouth" "11"/>
	<@award "2012-13" "H Whitworth" "Portsmouth & Southsea" "9"/>
	<@award "2013-14" "J Bowler" "Sarisbury Athletic" "9"/>
	<@award "2014-15" "B Marshall" "Sarisbury Athletic" "11"/>
	<@award "2015-16" "C Creal" "Gosport Borough" "10"/>
	<@award "2016-17" "F Gadd" "Portsmouth & Southsea" "9"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-13 Batting Award</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Runs scored" true/>
</thead>
<tbody>
	<@award "1997-98" "M Boyd" "Locks Heath" "128"/>
	<@award "1998-99" "M Journeaux" "Sarisbury Athletic" "261"/>
	<@award "1999-2000" "E White" "Fareham & Crofton" "147"/>
	<@award "2000-01" "S Ransley" "Locks Heath" "191"/>
	<@award "2001-02" "O Winter" "Locks Heath" "274"/>
	<@award "2002-03" "C Morgan" "Havant" "288"/>
	<@award "2003-04" "J Rolfe" "Locks Heath" "205"/>
	<@award "2004-05" "S Loat" "Havant" "171"/>
	<@award "2005-06" "S Loat" "Havant" "230"/>
	<@award "2006-07" "T Kent" "Fareham & Crofton" "254"/>
	<@award "2007-08" "B Mengham" "Purbrook" "293"/>
	<@award "2008-09" "D Sampson" "Fareham & Crofton" "253"/>
	<@award "2009-10" "E Lewis" "Hambledon" "259"/>
	<@award "2010-11" "H Hovey" "Havant" "356"/>
	<@award "2011-12" "A Reynolds" "Waterlooville" "230"/>
	<@award "2012-13" "H Gadd" "Portsmouth & Southsea" "238"/>
	<@award "2013-14" "O Southon" "Fareham & Crofton" "278"/>
	<@award "2014-15" "T Wallis" "Portsmouth" "189"/>
	<@award "2015-16" "F Gadd" "Portsmouth & Southsea" "244"/>
	<@award "2016-17" "J Oliver" "Havant" "247"/>
</tbody>
</table>

<h2>Michael Austin Harlick Under-13 Bowling Award</h2>
<table border="0">
<thead>
	<@award "Season" "Player" "Team" "Wickets taken" true/>
</thead>
<tbody>
	<@award "1997-98" "M Saunders" "Portsmouth" "4"/>
	<@award "1998-99" "C Stares" "Gosport Borough" "8"/>
	<@award "1999-2000" "A Shepherd" "Waterlooville" "9"/>
	<@award "2000-01" "H Haran" "Purbrook" "9"/>
	<@award "2001-02" "C Morgan" "Havant" "8"/>
	<@award "2002-03" "N Hodgson" "Havant" "7"/>
	<@award "2003-04" "J Paskins" "Locks Heath" "10"/>
	<@award "2004-05" "S Knott" "Locks Heath" "7"/>
	<@award "2005-06" "B Kissane" "Fareham & Crofton" "9"/>
	<@award "2006-07" "L Le-Clercq" "Hambledon" "11"/>
	<@award "2007-08" "J Christian" "Purbrook" "8"/>
	<@award "2008-09" "M Hindry" "Locks Heath" "11"/>
	<@award "2009-10" "R Prentice" "Hambledon" "9"/>
	<@award "2010-11" "A Southon" "Fareham & Crofton" "8"/>
	<@award "2011-12" "O Milne" "Purbrook" "9"/>
	<@award "2012-13" "O Southon" "Hambledon" "8"/>
	<@award "2013-14" "O Pratt" "Fareham & Crofton" "11"/>
	<@award "2014-15" "T Wallis" "Portsmouth" "12"/>
	<@award "2015-16" "J Oliver" "Havant" "10"/>
	<@award "2016-17" "J Oliver" "Havant" "14"/>
</tbody>
</table>
</div>