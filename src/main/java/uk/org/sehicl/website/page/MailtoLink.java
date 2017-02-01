package uk.org.sehicl.website.page;

public class MailtoLink
{
    private final String text;

    private MailtoLink(Builder builder)
    {
        text = String.format("<script language=\"javascript\">"
                + "document.write(mailTo(\"%s\", \"%s\", \"%s\", \"%s\"));" + "</script>",
                builder.address, builder.domain, builder.description, builder.linkText);
    }

    public String toString()
    {
        return text;
    }

    public static class Builder
    {
        private final String address;
        private String domain = "";
        private String description = "";
        private String linkText = "";

        public Builder(String address)
        {
            this.address = address;
        }

        public Builder setDomain(String domain)
        {
            this.domain = domain;
            return this;
        }

        public Builder setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder setLinkText(String linkText)
        {
            this.linkText = linkText;
            return this;
        }
        
        public MailtoLink build()
        {
            return new MailtoLink(this);
        }
    }
}
