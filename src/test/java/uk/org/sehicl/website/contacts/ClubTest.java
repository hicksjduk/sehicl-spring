package uk.org.sehicl.website.contacts;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClubTest
{
    @Test
    public void testOneWord()
    {
        assertEquals("Petersfield", Club.Petersfield.getName());
    }

    @Test
    public void testMultipleWords()
    {
        assertEquals("IBM South Hants", Club.IBM_South_Hants.getName());
    }
    
    @Test
    public void testMultipleWordsAndAmpersand()
    {
        assertEquals("Fareham & Crofton", Club.Fareham_and_Crofton.getName());
    }
}
