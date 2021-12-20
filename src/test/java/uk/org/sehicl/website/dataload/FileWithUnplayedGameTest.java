package uk.org.sehicl.website.dataload;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Model;

public class FileWithUnplayedGameTest
{
    @Test
    public void test()
    {
        Model model = ModelLoader.getModel(22);
        Deque<String> actualIds = model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getUnplayedMatch() != null)
                .flatMap(m -> Stream.of(m.getHomeTeamId(), m.getAwayTeamId()))
                .sorted()
                .collect(Collectors.toCollection(ArrayDeque::new));
        Stream
                .of("EmsworthA", "FriendsUnited", "PortsmouthUniversity", "PortsmouthUniversityB",
                        "HaylingIsland", "HaylingIsland", "RailwayTriangle", "HampshireBowman",
                        "HambledonA", "RailwayTriangle", "PortsmouthC", "WaterloovilleB",
                        "HampshireBowman", "HavantA", "Droxford", "EmsworthB")
                .sorted()
                .forEach(id -> assertEquals(id, actualIds.pop()));
        assertTrue(actualIds.isEmpty());
        Deque<Integer> matchCounts = model
                .getLeagues()
                .stream()
                .sorted()
                .map(League::getMatches)
                .map(Collection::size)
                .collect(Collectors.toCollection(ArrayDeque::new));
        Stream.of(30, 30, 45, 36, 29, 36, 36).forEach(c -> assertEquals(c, matchCounts.pop()));
        assertTrue(matchCounts.isEmpty());
    }
}
