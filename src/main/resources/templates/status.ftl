<#include "ordinaldate.ftl"/>

<#macro statusMessage status lastIncludedDate toCome type>
	<#if status == "FINAL">
		Final ${type}
	<#elseif status == "IN_PROGRESS">
		<#if toCome == 0>
			Includes all matches up to and including <@ordinalDate lastIncludedDate "MMMM" "yyyy"/>
		<#else>
			Date of last included result: <@ordinalDate lastIncludedDate "MMMM" "yyyy"/> 
			(${toCome} ${(toCome == 1)?then("result", "results")} to come)
		</#if>
	</#if>
</#macro>
