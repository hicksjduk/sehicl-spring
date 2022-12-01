<#macro div division note="">
	<h2>${division}</h2>
	<#if note != "">
		<p><i>(${note})</i></p>
	</#if>
	<table border="0">
		<tbody>
			<#nested/>
		</tbody>
	</table>
</#macro>

<#macro winner year team>
	<tr>
		<#assign prev = year - 1/>
		<#if (prev / 100)?int == (year/ 100)?int>
			<#assign season = (year % 100)?string("00")/>
		<#else>
			<#assign season = year?string.computer/>
		</#if>
		<td class="year">${prev?string.computer}-${season}</td>
		<td class="winner">${team}</td>
	</tr>
</#macro>

<h1>Divisional winners</h1>

<div id="divwinners">

<@div "Seniors, Group A">
	<@winner 1981 "United Services"/>
</@div>

<@div "Seniors, Group B">
	<@winner 1981 "Havant"/>
</@div>

<@div "Division 1" "from 1986-87 for the Peter Faulkner Shield">
	<@winner 1982 "Havant"/>
	<@winner 1983 "Havant"/>
	<@winner 1984 "Havant"/>
	<@winner 1985 "South Hants"/>
	<@winner 1986 "Hambledon"/>
	<@winner 1987 "Waterlooville"/>
	<@winner 1988 "Havant"/>
	<@winner 1989 "Portsmouth & Southsea"/>
	<@winner 1990 "Havant"/>
	<@winner 1991 "Havant A"/>
	<@winner 1992 "Havant A"/>
	<@winner 1993 "Havant A"/>
	<@winner 1994 "Havant A"/>
	<@winner 1995 "Havant A"/>
	<@winner 1996 "Havant A"/>
	<@winner 1997 "Waterlooville A"/>
	<@winner 1998 "Portsmouth A"/>
	<@winner 1999 "Portsmouth A"/>
	<@winner 2000 "Portsmouth A"/>
	<@winner 2001 "Purbrook A"/>
	<@winner 2002 "Harting"/>
	<@winner 2003 "Harting"/>
	<@winner 2004 "Havant A"/>
	<@winner 2005 "Havant A"/>
	<@winner 2006 "Havant A"/>
	<@winner 2007 "Havant A"/>
	<@winner 2008 "Havant A"/>
	<@winner 2009 "Purbrook B"/>
	<@winner 2010 "Waterlooville A"/>
	<@winner 2011 "Havant B"/>
	<@winner 2012 "Waterlooville A"/>
	<@winner 2013 "Waterlooville A"/>
	<@winner 2014 "Havant A"/>
	<@winner 2015 "Havant A"/>
	<@winner 2016 "Waterlooville A"/>
	<@winner 2017 "Waterlooville A"/>
	<@winner 2018 "Waterlooville A"/>
	<@winner 2019 "Havant A"/>
    <@winner 2020 "Havant A"/>
    <@winner 2022 "Havant A"/>
</@div>

<@div "Division 2" "from 1986-87 for the Barretts Shield">
	<@winner 1982 "Moneyfield"/>
	<@winner 1983 "Petersfield"/>
	<@winner 1984 "Denmead"/>
	<@winner 1985 "Fleetlands"/>
	<@winner 1986 "Portsmouth & Southsea"/>
	<@winner 1987 "Civil Service A"/>
	<@winner 1988 "South Hants"/>
	<@winner 1989 "Petersfield B"/>
	<@winner 1990 "Fareham"/>
	<@winner 1991 "Purbrook B"/>
	<@winner 1992 "Civil Service B"/>
	<@winner 1993 "Havant B"/>
	<@winner 1994 "Fareham & Crofton A"/>
	<@winner 1995 "Petersfield"/>
	<@winner 1996 "Locks Heath A"/>
	<@winner 1997 "Fareham & Crofton A"/>
	<@winner 1998 "Locks Heath A"/>
	<@winner 1999 "Purbrook A"/>
	<@winner 2000 "Corinthians"/>
	<@winner 2001 "Sarisbury Athletic A"/>
	<@winner 2002 "Locks Heath A"/>
	<@winner 2003 "Southsea A"/>
	<@winner 2004 "Sarisbury Athletic B"/>
	<@winner 2005 "United Services"/>
	<@winner 2006 "Purbrook A"/>
	<@winner 2007 "Purbrook B"/>
	<@winner 2008 "Hambledon"/>
	<@winner 2009 "Fareham & Crofton B"/>
	<@winner 2010 "Havant B"/>
	<@winner 2011 "Fareham & Crofton A"/>
	<@winner 2012 "United Services"/>
	<@winner 2013 "Bedhampton A"/>
	<@winner 2014 "Portsmouth & Southsea"/>
	<@winner 2015 "Portsmouth B"/>
	<@winner 2016 "Purbrook"/>
	<@winner 2017 "Portsmouth & Southsea A"/>
	<@winner 2018 "Portsmouth & Southsea A"/>
	<@winner 2019 "Fareham & Crofton A"/>
    <@winner 2020 "Hambledon A"/>
    <@winner 2022 "Havant B"/>
</@div>

<@div "Division 3" "for the Ferguson Shield">
	<@winner 1987 "Civil Service B"/>
	<@winner 1988 "Petersfield B"/>
	<@winner 1989 "Cormorants"/>
	<@winner 1990 "Civil Service B"/>
	<@winner 1991 "Enterprise A"/>
	<@winner 1992 "Havant B"/>
	<@winner 1993 "Flamingo"/>
	<@winner 1994 "Petersfield"/>
	<@winner 1995 "Gosport Borough B"/>
	<@winner 1996 "Curdridge"/>
	<@winner 1997 "Portchester A"/>
	<@winner 1998 "United Services"/>
	<@winner 1999 "Sarisbury Athletic"/>
	<@winner 2000 "Portsmouth C"/>
	<@winner 2001 "United Services"/>
	<@winner 2002 "Gosport Borough A"/>
	<@winner 2003 "Fareham & Crofton B"/>
	<@winner 2004 "Portchester B"/>
	<@winner 2005 "Waterlooville"/>
	<@winner 2006 "Purbrook B"/>
	<@winner 2007 "Hambledon"/>
	<@winner 2008 "Fareham & Crofton B"/>
	<@winner 2009 "Portsmouth & Southsea B"/>
	<@winner 2010 "DOE Cavaliers"/>
	<@winner 2011 "Hayling Island"/>
	<@winner 2012 "Sarisbury Athletic"/>
	<@winner 2013 "Corinthians"/>
	<@winner 2014 "Portsmouth B"/>
	<@winner 2015 "Bedhampton B"/>
	<@winner 2016 "Gosport Borough A"/>
	<@winner 2017 "Petersfield"/>
	<@winner 2018 "St James Casuals"/>
	<@winner 2019 "Hambledon A"/>
    <@winner 2020 "Havant B"/>
    <@winner 2022 "Fareham & Crofton"/>
</@div>

<@div "Division 4", "for the Joan Vause Shield">
	<@winner 1988 "Locks Heath"/>
	<@winner 1989 "Sarisbury Athletic"/>
	<@winner 1990 "Portsmouth & Southsea B"/>
	<@winner 1991 "Havant B"/>
	<@winner 1992 "Emsworth"/>
	<@winner 1993 "Fareham & Crofton B"/>
	<@winner 1994 "Waterlooville B"/>
	<@winner 1995 "DOE Gosport"/>
	<@winner 1996 "Portchester A"/>
	<@winner 1997 "United Services"/>
	<@winner 1998 "Sarisbury Athletic"/>
	<@winner 1999 "Cyanamid"/>
	<@winner 2000 "United Services"/>
	<@winner 2001 "Bedhampton B"/>
	<@winner 2002 "St James Casuals"/>
	<@winner 2003 "Cowplain"/>
	<@winner 2004 "Hambledon"/>
	<@winner 2005 "Rowner"/>
	<@winner 2006 "XIIth Men"/>
	<@winner 2007 "Bedhampton A"/>
	<@winner 2008 "Hampshire Bowman"/>
	<@winner 2009 "Wickham"/>
	<@winner 2010 "Rowner"/>
	<@winner 2011 "Hambledon B"/>
	<@winner 2012 "Emsworth"/>
	<@winner 2013 "Hambledon B"/>
	<@winner 2014 "Gosport Borough"/>
	<@winner 2015 "St James Casuals"/>
	<@winner 2016 "Gosport Borough B"/>
	<@winner 2017 "Hambledon B"/>
	<@winner 2018 "Sarisbury Athletic"/>
	<@winner 2019 "Afghan Society Portsmouth"/>
    <@winner 2020 "Fareham & Crofton"/>
    <@winner 2022 "Droxford"/>
</@div>

<@div "Division 5", "for the Brian Ravenhall Memorial Shield">
	<@winner 2018 "Hambledon C"/>
	<@winner 2019 "Waterlooville C"/>
    <@winner 2020 "Hambledon B"/>
    <@winner 2022 "Havant C"/>
</@div>

<@div "Division 6">
    <@winner 2020 "Knowle Village"/>
</@div>

<@div "Colts" "from 1986-87 for the Peter Faulkner Shield">
	<@winner 1981 "Waterlooville"/>
	<@winner 1982 "Portsdown"/>
	<@winner 1983 "Purbrook & Portsdown"/>
	<@winner 1984 "Purbrook & Portsdown"/>
	<@winner 1985 "Civil Service"/>
	<@winner 1986 "Emsworth"/>
	<@winner 1987 "Havant"/>
	<@winner 1988 "Havant"/>
	<@winner 1989 "Havant"/>
	<@winner 1990 "Havant"/>
	<@winner 1991 "Havant"/>
	<@winner 1992 "Fareham & Crofton"/>
	<@winner 1993 "Havant"/>
	<@winner 1994 "Fareham & Crofton"/>
	<@winner 1995 "Fareham & Crofton"/>
	<@winner 1996 "Purbrook"/>
	<@winner 1997 "Purbrook"/>
</@div>
<@div "Colts Under-16" "for the Peter Faulkner Shield">
	<@winner 1998 "Portsmouth"/>
	<@winner 1999 "Havant"/>
	<@winner 2000 "Gosport Borough"/>
	<@winner 2001 "Locks Heath"/>
	<@winner 2002 "Havant"/>
	<@winner 2003 "Havant"/>
	<@winner 2004 "Waterlooville"/>
	<@winner 2005 "Waterlooville"/>
	<@winner 2006 "Havant"/>
	<@winner 2007 "Havant"/>
	<@winner 2008 "Portsmouth & Southsea"/>
	<@winner 2009 "Portsmouth & Southsea"/>
	<@winner 2010 "Portsmouth & Southsea"/>
	<@winner 2011 "Fareham & Crofton"/>
	<@winner 2012 "Hambledon"/>
	<@winner 2013 "Sarisbury Athletic"/>
	<@winner 2014 "Havant"/>
	<@winner 2015 "Portsmouth & Southsea"/>
	<@winner 2016 "Petersfield"/>
	<@winner 2017 "Portsmouth & Southsea"/>
	<@winner 2018 "Hambledon"/>
	<@winner 2019 "Gosport Borough"/>
    <@winner 2020 "Havant"/>
    <@winner 2022 "Hambledon Jets"/>
</@div>

<@div "Colts Under-13" "for the Ron Cross MBE Trophy">
	<@winner 1998 "Bishops Waltham"/>
	<@winner 1999 "Sarisbury Athletic"/>
	<@winner 2000 "Locks Heath"/>
	<@winner 2001 "Locks Heath"/>
	<@winner 2002 "Havant"/>
	<@winner 2003 "Havant"/>
	<@winner 2004 "Havant"/>
	<@winner 2005 "Fareham & Crofton"/>
	<@winner 2006 "Havant"/>
	<@winner 2007 "Hambledon"/>
	<@winner 2008 "Fareham & Crofton"/>
	<@winner 2009 "Portsmouth"/>
	<@winner 2010 "Hambledon"/>
	<@winner 2011 "Hambledon"/>
	<@winner 2012 "Havant"/>
	<@winner 2013 "Portsmouth & Southsea"/>
	<@winner 2014 "Waterlooville"/>
	<@winner 2015 "Waterlooville"/>
	<@winner 2016 "Havant"/>
	<@winner 2017 "Havant"/>
	<@winner 2018 "Hambledon"/>
	<@winner 2019 "Havant"/>
    <@winner 2020 "Hambledon"/>
    <@winner 2022 "Grayshott"/>
</@div>

</div>