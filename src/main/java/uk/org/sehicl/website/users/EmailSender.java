package uk.org.sehicl.website.users;

public interface EmailSender
{
    void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException;

    public static class Addressee
    {
        private final String address;
        private final String name;

        public Addressee(String address)
        {
            this(address, null);
        }

        public Addressee(String address, String name)
        {
            this.address = address;
            this.name = name;
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
            return "Addressee [address=" + address + ", name=" + name + "]";
        }
    }
}
