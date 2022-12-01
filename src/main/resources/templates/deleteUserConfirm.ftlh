<#if (user.name)??>
<h1>User details</h1>
<ul>
	<li>Name: ${user.name}</li>
	<li>E-mail: ${user.email}</li>
	<li>Club: ${user.club!"(Not specified)"}</li>
</ul>
<#if confirmed>
User deleted.
<#else>
<form action="/deleteUser/${user.id?string.computer}" method="post">
<p>Are you sure you want to do this?</p>
<input type="Submit" value="Confirm">
</form>
</#if>
<#else>
<p>No user was found with the specified identity.</p>
</#if>
