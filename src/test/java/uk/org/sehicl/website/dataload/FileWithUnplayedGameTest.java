package uk.org.sehicl.website.dataload;

import static org.assertj.core.api.Assertions.*;

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
                        "HampshireBowman", "HavantA", "Droxford", "EmsworthB", "Barracuda",
                        "Westbourne", "Grayshott", "Havant0", "HaylingIsland", "PortsmouthC")
                .sorted()
                .forEach(id -> assertThat(actualIds.pop()).isEqualTo(id));
        assertThat(actualIds.isEmpty());
        Deque<Integer> matchCounts = model
                .getLeagues()
                .stream()
                .sorted()
                .map(League::getMatches)
                .map(Collection::size)
                .collect(Collectors.toCollection(ArrayDeque::new));
        Stream
                .of(26, 25, 38, 30, 25, 23, 30)
                .forEach(c -> assertThat(matchCounts.pop()).isEqualTo(c));
        assertThat(matchCounts.isEmpty());
    }
}
