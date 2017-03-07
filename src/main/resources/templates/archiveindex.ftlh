<h1>Archive</h1>
<p>Click one of the links below to view archived information from
	the relevant season.</p>
<ul>
	<#list 4..<currentSeason as season>
        <li>
            <a href="/archive/season/${season}">${(1999+season)?string.computer}-${season?string("00")}</a>
        </li>
	</#list>
</ul>
