package uk.org.sehicl.website.contacts;

import java.util.List;

import uk.org.sehicl.website.page.MailtoLink;

public class Contact
{
    private final String name;
    private final String address;
    private final List<Long> phoneNumbers;
    private final List<Role> roles;
    
    public class Role
    {
        private final String club;
        private final String role;
        private final MailtoLink email;
        
        public class Builder
        {
            private final String club;
            private final String role;
            private final MailtoLink email;
        }
    }
    
    public enum RoleScope
    {
        LEAGUE,
        BEDHAMPTON("Bedhampton"),
        
        
        private final String club;
        
        private RoleScope()
        {
            this(null);
        }
        
        private RoleScope(String club)
        {
            this.club = club;
    }
    }
}
