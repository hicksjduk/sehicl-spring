function mailTo(id, domain, addrDesc, linkText)
{
	var at = "@";
	var mailAddr = id + at + (domain == "" ? "sehicl.org.uk" : domain);
	if (addrDesc != "")
		mailAddr = addrDesc + " <" + mailAddr + ">";
	var linkContent = "";
	if (linkText != "")
		linkContent = linkText;
	else if (addrDesc != "")
		linkContent = addrDesc;
	else
		linkContent = mailAddr;
	mailAddr = mailAddr.replace("&", "%26");
	var answer = linkContent.link("mailto:" + mailAddr);
	return answer;
}