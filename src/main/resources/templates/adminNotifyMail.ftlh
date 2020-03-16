Account details:
<br>User ID:
<#if userDetailsPageAddress??> 
    <a href="${userDetailsPageAddress}/${user.id?string.computer}">${user.id?string.computer}</a>
<#else>
    ${user.id?string.computer}
</#if>
<br>Name: ${user.name}
<br>Club: ${user.club!"(Not specified)"}
<br>Email: ${user.email}

<p>I confirm that:
<#include "dataprotectionagreement.ftlh"/>
