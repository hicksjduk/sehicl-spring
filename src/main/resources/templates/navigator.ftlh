<#assign lastIndent = 0/>
<ul class="navigator">
<#list navigator.rows as row>
<#if (row.nestingLevel > lastIndent)>
<ul id="currentMenu">
<#elseif (row.nestingLevel < lastIndent)>
</ul>
</#if>
<#assign lastIndent = row.nestingLevel/>
<li>
<#if row.uri??>
<a href="${row.uri}">${row.title}</a>
<#else>
<span class="current">${row.title}</span>
</#if>
</li>
</#list>
</ul>