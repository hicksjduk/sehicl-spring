package uk.org.sehicl.website.contacts;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ClubTest
{
    @Test
    public void testOneWord()
    {
        assertThat(Club.Petersfield.getName()).isEqualTo("Petersfield");
    }

    @Test
    public void testMultipleWords()
    {
        assertThat(Club.IBM_South_Hants.getName()).isEqualTo("IBM South Hants");
    }
    
    @Test
    public void testMultipleWordsAndAmpersand()
    {
        assertThat(Club.Fareham_and_Crofton.getName()).isEqualTo("Fareham & Crofton");
    }
}
