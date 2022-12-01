<#if (user.name)??>
<h1>User details</h1>
<ul>
	<li>Name: ${user.name}</li>
	<li>E-mail: ${user.email}</li>
	<li>Club: ${user.club!"(Not specified)"}</li>
</ul>
<a href="/deleteUser/${user.id?string.computer}">Delete this user</a>
<#else>
<p>No user was found with the specified identity.</p>
</#if>
