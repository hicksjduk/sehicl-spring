package uk.org.sehicl.website.users;

import java.util.Optional;

public interface EmailSender
{
    void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException;

    public static class Addressee
    {
        public static Addressee withAddress(String address)
        {
            return new Addressee(address);
        }

        private final String address;
        private final String name;

        private Addressee(String address)
        {
            this(address, null);
        }

        private Addressee(String address, String name)
        {
            this.address = address;
            this.name = name;
        }

        public Addressee withName(String name)
        {
            return new Addressee(address, name);
        }

        public String getAddress()
        {
            return address;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return Optional
                    .ofNullable(name)
                    .map(n -> "%s <%s>".formatted(n, address))
                    .orElse("%s".formatted(address));
        }
    }
}
