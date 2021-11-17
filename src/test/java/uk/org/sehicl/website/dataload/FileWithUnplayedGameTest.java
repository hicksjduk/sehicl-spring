package uk.org.sehicl.website.dataload;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Model;

public class FileWithUnplayedGameTest
{
    @Test
    public void test()
    {
        Model model = ModelLoader.getModel(22);
        assertEquals(1, model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getUnplayedMatch() != null).count());
        assertEquals(7, model.getLeagues().size());
        assertEquals(28,
                model
                        .getLeagues()
                        .stream()
                        .filter(l -> l.getId().equals("Division5"))
                        .map(League::getMatches)
                        .flatMap(Collection::stream)
                        .count());
    }
}
