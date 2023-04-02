<#macro award year winner>
	<#assign prev = year - 1/>
	<#if (prev / 100)?int == (year/ 100)?int>
		<#assign season = (year % 100)?string("00")/>
	<#else>
		<#assign season = year?string.computer/>
	</#if>
	<tr>
		<td class="year">${prev?string.computer}-${season}</td>
		<td class="winner">${winner}</td>
	</tr>
</#macro>

<div id="fairplay">
<h1>Sporting and Efficiency</h1>
<h2>Alastair MacDonald Award (Seniors)</h2>
<table border="0">
<tbody>
<@award 1995 "Mr W.R. Jones"/>
<@award 1996 "Enterprise A"/>
<@award 1997 "Portsmouth B"/>
<@award 1998 "Locks Heath A"/>
<@award 1999 "Champagne"/>
<@award 2000 "Cowplain, DOE Gosport Cavaliers B (shared)"/>
<@award 2001 "DOE Gosport Cavaliers B"/>
<@award 2002 "DOE Gosport Cavaliers A"/>
<@award 2003 "Portchester A"/>
<@award 2004 "DOE Gosport Cavaliers"/>
<@award 2005 "Portchester B"/>
<@award 2006 "St James Casuals"/>
<@award 2007 "Denmead"/>
<@award 2008 "Havant A"/>
<@award 2009 "Hambledon"/>
<@award 2010 "Fareham & Crofton B"/>
<@award 2011 "Portsmouth Priory"/>
<@award 2012 "Fareham & Crofton C"/>
<@award 2013 "Fareham & Crofton A, Havant A, Sarisbury Athletic A (shared)"/>
<@award 2014 "Havant A"/>
<@award 2015 "Gosport Borough"/>
<@award 2016 "Denmead"/>
<@award 2017 "Havant B"/>
<@award 2018 "Emsworth B"/>
<@award 2019 "Gosport Borough B"/>
<@award 2020 "Clanfield"/>
<@award 2022 "Clanfield B"/>
<@award 2023 "Gosport Borough A"/>
</tbody>
</table>

<h2>Roy Jones Award (Colts)</h2>
<table border="0">
<tbody>
<@award 1998 "Gosport Borough"/>
<@award 1999 "Portchester Under-16"/>
<@award 2000 "Portsmouth Under-13"/>
<@award 2001 "Havant Under-16"/>
<@award 2002 "Havant Under-13"/>
<@award 2003 "Portsmouth Under-16"/>
<@award 2004 "Fareham & Crofton Under-16"/>
<@award 2005 "Hambledon Under-16"/>
<@award 2006 "Portsmouth Community Under-16"/>
<@award 2007 "Locks Heath Under-13"/>
<@award 2008 "Portsmouth Under-13"/>
<@award 2009 "Locks Heath Under-13"/>
<@award 2010 "Fareham & Crofton Under-13, Havant Under-16, Locks Heath Under-13, Portsmouth Under-16, Purbrook Under-13 (shared)"/>
<@award 2011 "Havant Under-16"/>
<@award 2012 "Waterlooville Under-16"/>
<@award 2013 "Portsmouth Community Under-13"/>
<@award 2014 "Petersfield Under-16, Portsmouth & Southsea Under-13 (shared)"/>
<@award 2015 "Sarisbury Athletic Under-16"/>
<@award 2016 "Fareham & Crofton Under-13"/>
<@award 2017 "Havant Under-13, Waterlooville Under-16 (shared)"/>
<@award 2018 "Hambledon Under-16"/>
<@award 2019 "Hambledon A Under-16, Hambledon Sharks Under-13 (shared)"/>
<@award 2020 "Havant Under-16"/>
<@award 2022 "Hambledon Under-13"/>
<@award 2023 "Waterlooville Under-16"/>
</tbody>
</table>
</div>