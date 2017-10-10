<#if (user.name)??>
<h1>Activation successful</h1>
<p>You have successfully activated the following account:</p>
<ul>
	<li>Name: ${user.name}</li>
	<li>E-mail: ${user.email}</li>
	<li>Club: ${user.club!"(Not specified)"}</li>
</ul>
<#else>
<h1>Activation failed</h1>
<p>No user was found with the specified identity.</p>
</#if>
<p><a href="/login">Go to the Login page.</a></p>