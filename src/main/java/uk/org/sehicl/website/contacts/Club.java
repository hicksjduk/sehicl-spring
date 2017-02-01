package uk.org.sehicl.website.contacts;

public enum Club
{
    Bedhampton,
    Clanfield,
    Curdridge,
    Denmead,
    Emsworth,
    Fareham_and_Crofton,
    Gosport_Borough,
    Hambledon,
    Hampshire_Bowman,
    Havant,
    Hayling_Island,
    IBM_South_Hants,
    Knowle_Village,
    OPCS_Titchfield,
    Petersfield,
    Portchester,
    Portsmouth,
    Portsmouth_Academics,
    Portsmouth_and_Southsea,
    Purbrook,
    Railway_Triangle,
    Sarisbury_Athletic,
    ServiceMaster,
    Southern_Electric,
    Southsea_Super_Kings,
    Steep,
    St_James_Casuals,
    Waterlooville,
    XIIth_Men;
    
    public String getName()
    {
        return name().replaceAll("_and_", " & ").replaceAll("_", " ");
    }
}
