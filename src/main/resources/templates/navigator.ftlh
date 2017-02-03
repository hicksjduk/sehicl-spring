<#macro navRow row>
	<li>
		<#if row.uri??>
			<a href="${row.uri}">${row.title}</a>
		<#else>
			<span class="current">${row.title}</span>
		</#if>
		<#if row.subRows?size != 0>
			<ul id="currentMenu">
				<#list row.subRows as subRow>
					<@navRow subRow/>
				</#list>
			</ul>
		</#if>
	</li>
</#macro>
<ul class="navigator">
	<#list navigator.rows as row>
		<@navRow row/>
	</#list>
</ul>