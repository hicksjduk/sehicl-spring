<h1>Contacts</h1>
<#if contacts.restricted>
<#assign clubTitle = "Club secretaries"/>
<#else>
<#assign clubTitle = "Club contacts"/>
</#if>
<p>
	<a href="#committee">Committee</a> | <a href="#club">${clubTitle}</a>
</p>
<p>
	<a href="http://www.gsam.co.uk/">Game Set &amp; Match</a>, the League's
	sponsors, are located at Unit 1, Beaver Trade Park, Quarry Lane,
	Chichester PO19 8NY (tel: 01243 538800).
</p>
<p>
	The main means of communication on league matters is by e-mail. <b>For
		urgent contacts, however, it is preferable to telephone; telephone
		numbers for committee members, club secretaries and team captains and
		managers are available
		<#if contacts.restricted> 
		on <a href="/fullContacts">the Full Contacts page</a>.
		<#else>
		below.
		</#if>
		</b>
</p>
<p>General contacts should be addressed to <@mailto address="contacts"/>.</p>
<p>Non-member clubs who wish to enter one or more teams in the
	League are asked to contact <@mailto address="secretary">the Secretary</@mailto> in the first instance.</p>
<p>
	Result sheets may be sent electronically to <@mailto address="fixturesec">the Fixture Secretary</@mailto>,
	using one of the template documents available on the <a
		href="/resources">Resources</a> page.
</p>
<p>Please let <@mailto address="website">the Webmaster</@mailto> know if any of the information below
	is incomplete or incorrect.</p>
<h2>
	<a id="committee">Committee</a>
</h2>
<table>
	<tbody>
	<#list contacts.committeeContacts as contact>
        <tr>
            <td class="role">${contact.role.name}</td>
            <td>
            <#if contact.role.email??>
                <@mailto address=contact.role.email.id domain=contact.role.email.domain!"">${contact.name}</@mailto>
            <#else>
            	${contact.name}
            </#if>
            <#if !contacts.restricted>
            <#if contact.address??><br>${contact.address}</#if>
            <#if contact.phoneNumbers?size != 0><br>${contact.phoneNumbers?join(", ")}</#if>
            </#if>
            </td>
        </tr>
    </#list>
	</tbody>
</table>
<h2>
	<a id="club">${clubTitle}</a>
</h2>
<table>
	<tbody>
	<#assign lastClub = ""/>
	<#list contacts.clubContacts as contact>
      	<#assign club = contact.role.club/>
        <tr>
            <td class="role"><#if club != lastClub>${club}</#if></td>
            <td>
			<#if contact.role.name == "Secretary">
            	<#if contacts.restricted>
		            <#if contact.role.email??>
                        <@mailto address=contact.role.email.id domain=contact.role.email.domain!"">${contact.name}</@mailto>
            		<#else>
            			${contact.name}
            		</#if>
            	<#else>
            		${contact.role.name}:
    				<#if contact.role.email??>
        		    	<@mailto address=contact.role.email.id domain=contact.role.email.domain!"">${contact.name}</@mailto>
            		<#else>
            			${contact.name}
            		</#if>
            		<#-- <#if contact.address??><br>${contact.address}</#if> --> 
            		<#if contact.phoneNumbers?size != 0><br>${contact.phoneNumbers?join(", ")}</#if>
            	</#if>
            <#else>
            	<#if !contacts.restricted>
            		${contact.role.name}: ${contact.name}
            		<#if contact.phoneNumbers?size != 0>${contact.phoneNumbers?join(", ")}</#if>
	            </#if>
            </#if>
            </td>
        </tr>
		<#assign lastClub = club/>
    </#list>
	</tbody>
</table>
