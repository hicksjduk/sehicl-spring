<#if (reconfirm.user.name)??>
<h1>Reconfirm your account</h1>
<p>You are reconfirming the account with the following details:</p>
<ul>
	<li>Name: ${reconfirm.user.name}</li>
	<li>Email: ${reconfirm.user.email}</li>
	<li>Club: ${reconfirm.user.club!"(Not specified)"}</li>
</ul>
<p>To reconfirm this account, please read the statements below, tick the box 
to confirm that you agree with them, and press "Reconfirm".
<u>Do not continue unless you understand what you are agreeing to and are
willing to be bound by it.</u>
</p>
<form action="/reconfirm/${reconfirm.user.id?string.computer}" method="post">
	<p><input type="checkbox" name="agreement" $({(reconfirm.validation.agreement)??)?then("checked", "")}>
		I confirm that:</input>
<#include "dataprotectionagreement.ftlh"/>
	</p>
	<p>${(reconfirm.validation.agreementMessage)!""}</p>
	<p>
		<input type="Submit" value="Reconfirm">
	</p>
</form>
<#else>
<h1>Reconfirmation not possible</h1>
<p>No user was found with the specified identity.</p>
<p><a href="/login">Go to the Login page.</a></p>
</#if>
