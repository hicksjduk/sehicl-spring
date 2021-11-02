package uk.org.sehicl.website.dataload;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;

public class FileWithUnplayedGameTest
{
    @Test
    public void test()
    {
        Model model = ModelLoader.getModel(22);
        Match match = model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getUnplayedMatch() != null)
                .findFirst()
                .orElse(null);
        assertNotNull(match);
        assertEquals("Both teams failed to turn up", match.getUnplayedMatch().getReason());
        assertTrue(match.getUnplayedMatch().getCountAsPlayed());
        assertEquals(7, model.getLeagues().size());
        assertEquals(36,
                model
                        .getLeagues()
                        .stream()
                        .filter(l -> l.getId().equals("Division5"))
                        .map(League::getMatches)
                        .flatMap(Collection::stream)
                        .count());
    }
}
