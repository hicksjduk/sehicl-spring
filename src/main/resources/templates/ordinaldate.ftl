<#macro ordinalDate date monthTemplate yearTemplate>
	${date?string("d")}${suffix(date?string("d"))} ${date?string(monthTemplate + " " + yearTemplate)}
</#macro>

<#function suffix numstr>
	<#assign num = numstr?number/>
	<#assign tens = (num / 10) % 10/>
	<#assign units = num % 10/>
	<#if tens != 1>
		<#if units == 1>
			<#return "st"/>
		<#elseif units == 2>
			<#return "nd"/>
		<#elseif units == 3>
			<#return "rd"/>
		</#if>
	</#if>
	<#return "th"/>
<</#function>
