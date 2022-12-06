package uk.org.sehicl.website.report;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.report.BattingAverages.BattingRow;
import uk.org.sehicl.website.report.ReportStatus.Status;
import uk.org.sehicl.website.rules.Rules;

public class BattingAveragesTest
{
    @Test
    public void testIncompleteSeasonSenior()
    {
        final BattingAverages averages = new BattingAverages.Builder(LeagueSelector.SENIOR,
                Completeness.COMPLETE, 50, new ModelAndRules(17)).build();
        checkAverages(averages, validate(getDate("2017-02-12"), Status.IN_PROGRESS, 3),
                validate("DWallis", "D Wallis", "PortsmouthB", "Portsmouth B", 246, 66, true),
                validate("DQuincey", "D Quincey", "Curdridge", "Curdridge", 198, 47, false),
                validate("GMarshall", "G Marshall", "HambledonA", "Hambledon A", 196, 69, false),
                validate("INandra", "I Nandra", "PortsmouthA", "Portsmouth A", 192, 56, true),
                validate("MBenfield", "M Benfield", "PortsmouthSouthseaA",
                        "Portsmouth & Southsea A", 182, 42, true),
                validate("SIzzard", "S Izzard", "KnowleVillage", "Knowle Village", 179, 48, false),
                validate("SStoddart", "S Stoddart", "FarehamCroftonA", "Fareham & Crofton A", 169,
                        54, true),
                validate("NWyatt", "N Wyatt", "PortsmouthA", "Portsmouth A", 167, 41, true),
                validate("MBerg", "M Berg", "PortsmouthSouthseaB", "Portsmouth & Southsea B", 166,
                        76, false),
                validate("HGadd", "H Gadd", "HavantA", "Havant A", 166, 46, true),
                validate("GKitchin", "G Kitchin", "GosportBoroughA", "Gosport Borough A", 161, 51,
                        false),
                validate("JMcCoy", "J McCoy", "Purbrook", "Purbrook", 161, 49, true),
                validate("BTurk", "B Turk", "Petersfield", "Petersfield", 161, 45, false),
                validate("AKing", "A King", "StJamesCasuals", "St James Casuals", 158, 30, false),
                validate("RPeterson", "R Peterson", "XIIthMenA", "XIIth Men A", 154, 45, false),
                validate("DLee", "D Lee", "ServiceMaster", "ServiceMaster", 152, 64, true),
                validate("MHayes", "M Hayes", "HaylingIsland", "Hayling Island", 148, 60, true),
                validate("LHarrop", "L Harrop", "GosportBoroughA", "Gosport Borough A", 143, 34,
                        false),
                validate("JFriedrich", "J Friedrich", "SarisburyAthletic", "Sarisbury Athletic",
                        141, 27, false),
                validate("ALuff", "A Luff", "StJamesCasuals", "St James Casuals", 140, 29, false),
                validate("JLewis", "J Lewis", "HambledonB", "Hambledon B", 139, 36, false),
                validate("HWoolf", "H Woolf", "PortsmouthC", "Portsmouth C", 139, 43, false),
                validate("SRansley", "S Ransley", "HavantA", "Havant A", 137, 48, false),
                validate("SBarnard", "S Barnard", "HaylingIsland", "Hayling Island", 136, 42,
                        false),
                validate("TBenfield", "T Benfield", "PortsmouthSouthseaA",
                        "Portsmouth & Southsea A", 132, 45, true),
                validate("JHarris", "J Harris", "GosportBoroughB", "Gosport Borough B", 132, 31,
                        false),
                validate("MLeClercq", "M Le-Clercq", "HambledonB", "Hambledon B", 131, 35, false),
                validate("LLeClercq", "L Le-Clercq", "HambledonB", "Hambledon B", 130, 29, false),
                validate("GMartin", "G Martin", "HaylingIsland", "Hayling Island", 129, 36, false),
                validate("JMarshall", "J Marshall", "HambledonA", "Hambledon A", 128, 49, true),
                validate("DWimble", "D Wimble", "FarehamCroftonB", "Fareham & Crofton B", 128, 34,
                        false),
                validate("RBarnard", "R Barnard", "IBMSouthHants", "IBM South Hants", 126, 39,
                        false),
                validate("JPalmerGoddard", "J Palmer-Goddard", "PortsmouthA", "Portsmouth A", 125,
                        40, true),
                validate("SLoat", "S Loat", "HavantA", "Havant A", 124, 34, false),
                validate("MGoddard", "M Goddard", "WaterloovilleA", "Waterlooville A", 122, 32,
                        true),
                validate("HKhan0", "H Khan", "Clanfield", "Clanfield", 122, 47, false),
                validate("SRyan", "S Ryan", "KnowleVillage", "Knowle Village", 122, 54, true),
                validate("JScutt", "J Scutt", "WaterloovilleA", "Waterlooville A", 122, 45, true),
                validate("JTodd", "J Todd", "GosportBoroughB", "Gosport Borough B", 119, 35, false),
                validate("MSmith", "M Smith", "Emsworth", "Emsworth", 118, 49, true),
                validate("HSole", "H Sole", "Portchester", "Portchester", 114, 43, true),
                validate("SWoodgate", "S Woodgate", "BedhamptonC", "Bedhampton C", 114, 39, false),
                validate("BAnscombe", "B Anscombe", "Petersfield", "Petersfield", 113, 49, true),
                validate("JHudson", "J Hudson", "WaterloovilleA", "Waterlooville A", 113, 32, true),
                validate("GKing", "G King", "GosportBoroughA", "Gosport Borough A", 110, 31, false),
                validate("TClark0", "T Clark", "SarisburyAthletic", "Sarisbury Athletic", 109, 27,
                        false),
                validate("MHovey", "M Hovey", "BedhamptonA", "Bedhampton A", 107, 49, true),
                validate("Sathish", "Sathish", "SouthseaSuperKings", "Southsea Super Kings", 107,
                        37, false),
                validate("AShephard", "A Shephard", "WaterloovilleA", "Waterlooville A", 106, 27,
                        false),
                validate("JAshford", "J Ashford", "ServiceMaster", "ServiceMaster", 104, 30, true),
                validate("Reuben", "Reuben", "SouthseaSuperKings", "Southsea Super Kings", 104, 49,
                        false),
                validate("CSalmon", "C Salmon", "Petersfield", "Petersfield", 104, 52, false));
    }

    @Test
    public void testCompleteSeasonSenior()
    {
        final BattingAverages averages = new BattingAverages.Builder(LeagueSelector.SENIOR,
                Completeness.COMPLETE, 25, new ModelAndRules(16)).build();
        checkAverages(averages, validate(getDate("2016-03-13"), Status.FINAL, 0),
                validate("DClark", "D Clark", "BedhamptonA", "Bedhampton A", 325, 61, false),
                validate("GMartin", "G Martin", "HaylingIsland", "Hayling Island", 271, 54, true),
                validate("RBasketter", "R Basketter", "BedhamptonC", "Bedhampton C", 251, 43,
                        false),
                validate("LHarrop", "L Harrop", "GosportBoroughA", "Gosport Borough A", 249, 44,
                        true),
                validate("JBulled", "J Bulled", "HavantB", "Havant B", 245, 38, true),
                validate("JPalmerGoddard", "J Palmer-Goddard", "PortsmouthA", "Portsmouth A", 242,
                        47, false),
                validate("CLuetchford", "C Luetchford", "LocksHeathB", "Locks Heath B", 232, 50,
                        false),
                validate("GMarshall", "G Marshall", "HambledonA", "Hambledon A", 231, 72, false),
                validate("CPennicott", "C Pennicott", "GosportBoroughB", "Gosport Borough B", 219,
                        47, false),
                validate("JScutt", "J Scutt", "WaterloovilleA", "Waterlooville A", 217, 49, true),
                validate("GKitchin", "G Kitchin", "GosportBoroughB", "Gosport Borough B", 208, 82,
                        false),
                validate("PFriedrich", "P Friedrich", "SarisburyAthleticB", "Sarisbury Athletic B",
                        206, 41, false),
                validate("SWainwright", "S Wainwright", "BedhamptonB", "Bedhampton B", 206, 54,
                        false),
                validate("SRansley", "S Ransley", "HavantA", "Havant A", 202, 52, false),
                validate("MGoddard", "M Goddard", "WaterloovilleA", "Waterlooville A", 195, 32,
                        false),
                validate("JFloyd", "J Floyd", "SarisburyAthleticA", "Sarisbury Athletic A", 192, 38,
                        false),
                validate("PGwynn", "P Gwynn", "KnowleVillage", "Knowle Village", 190, 47, true),
                validate("JHughes", "J Hughes", "LocksHeathA", "Locks Heath A", 190, 47, true),
                validate("ADean", "A Dean", "Purbrook", "Purbrook", 189, 47, false),
                validate("PHungerford", "P Hungerford", "Portchester", "Portchester", 188, 57,
                        false),
                validate("PSmith", "P Smith", "HampshireBowman", "Hampshire Bowman", 188, 41,
                        false),
                validate("HKhan0", "H Khan", "Clanfield", "Clanfield", 187, 37, false),
                validate("SLoat", "S Loat", "HavantA", "Havant A", 186, 53, false),
                validate("AAnthony", "A Anthony", "Denmead", "Denmead", 182, 34, false),
                validate("SFloyd", "S Floyd", "SarisburyAthleticA", "Sarisbury Athletic A", 181, 46,
                        false));
    }

    @Test
    public void testCompleteSeasonUnder16()
    {
        final BattingAverages averages = new BattingAverages.Builder(LeagueSelector.COLTSUNDER16,
                Completeness.COMPLETE, 25, new ModelAndRules(16)).build();
        checkAverages(averages, validate(getDate("2016-03-13"), Status.FINAL, 0),
                validate("BAnscombe0", "B Anscombe", "Petersfield0", "Petersfield", 277, 68, false),
                validate("JDunn0", "J Dunn", "SarisburyAthletic", "Sarisbury Athletic", 262, 55,
                        false),
                validate("GBroadhurst0", "G Broadhurst", "Petersfield0", "Petersfield", 223, 43,
                        false),
                validate("TWallis", "T Wallis", "Portsmouth", "Portsmouth", 213, 43, false),
                validate("SReynolds0", "S Reynolds", "Waterlooville", "Waterlooville", 203, 50,
                        false),
                validate("JHarris0", "J Harris", "GosportBorough", "Gosport Borough", 195, 32,
                        false),
                validate("VSahu0", "V Sahu", "FarehamCrofton", "Fareham & Crofton", 194, 30, true),
                validate("OKanavan0", "O Kanavan", "PortsmouthSouthsea0", "Portsmouth & Southsea",
                        156, 47, true),
                validate("TToogood", "T Toogood", "Hambledon", "Hambledon", 156, 33, false),
                validate("DWimble0", "D Wimble", "FarehamCrofton", "Fareham & Crofton", 156, 27,
                        false),
                validate("JRichards0", "J Richards", "GosportBorough", "Gosport Borough", 155, 60,
                        false),
                validate("JPearce", "J Pearce", "Waterlooville", "Waterlooville", 152, 30, false),
                validate("HWoolf0", "H Woolf", "Portsmouth", "Portsmouth", 149, 54, false),
                validate("MCarter0", "M Carter", "Havant", "Havant", 148, 45, false),
                validate("HGadd0", "H Gadd", "PortsmouthSouthsea0", "Portsmouth & Southsea", 137,
                        36, false),
                validate("BMarshall0", "B Marshall", "SarisburyAthletic", "Sarisbury Athletic", 137,
                        27, false),
                validate("HRandell", "H Randell", "PortsmouthSouthsea0", "Portsmouth & Southsea",
                        136, 41, true),
                validate("WDoyle", "W Doyle", "Waterlooville", "Waterlooville", 127, 53, true),
                validate("OCreal", "O Creal", "GosportBorough", "Gosport Borough", 126, 26, false),
                validate("MFrost0", "M Frost", "FarehamCrofton", "Fareham & Crofton", 122, 30,
                        false),
                validate("JCampbell", "J Campbell", "Petersfield0", "Petersfield", 121, 30, false),
                validate("MChapman0", "M Chapman", "Portsmouth", "Portsmouth", 117, 27, false),
                validate("AGrimes", "A Grimes", "Hambledon", "Hambledon", 115, 35, true),
                validate("BTurk0", "B Turk", "Petersfield0", "Petersfield", 110, 41, false),
                validate("DRussell0", "D Russell", "Havant", "Havant", 104, 39, false));
    }

    @Test
    public void testCompleteSeasonUnder13()
    {
        final BattingAverages averages = new BattingAverages.Builder(LeagueSelector.COLTSUNDER13,
                Completeness.COMPLETE, 25, new ModelAndRules(16)).build();
        checkAverages(averages, validate(getDate("2016-03-13"), Status.FINAL, 0),
                validate("FGadd0", "F Gadd", "PortsmouthSouthsea1", "Portsmouth & Southsea", 244,
                        65, false),
                validate("TCollighan0", "T Collighan", "PortsmouthSouthsea1",
                        "Portsmouth & Southsea", 234, 53, false),
                validate("OPratt0", "O Pratt", "FarehamCrofton0", "Fareham & Crofton", 223, 50,
                        false),
                validate("JOliver", "J Oliver", "Havant0", "Havant", 203, 35, true),
                validate("JCollett0", "J Collett", "Portsmouth0", "Portsmouth", 168, 45, false),
                validate("BClarke", "B Clarke", "GosportBorough0", "Gosport Borough", 156, 53,
                        true),
                validate("OAmis", "O Amis", "Petersfield1", "Petersfield", 151, 33, false),
                validate("MWalton0", "M Walton", "Portsmouth0", "Portsmouth", 151, 27, false),
                validate("JHovey0", "J Hovey", "Havant0", "Havant", 140, 42, false),
                validate("MButcher", "M Butcher", "Petersfield1", "Petersfield", 138, 24, true),
                validate("OCreal0", "O Creal", "GosportBorough0", "Gosport Borough", 136, 30, true),
                validate("NKajendran", "N Kajendran", "LocksHeath", "Locks Heath", 133, 27, false),
                validate("OHillier", "O Hillier", "SarisburyAthletic0", "Sarisbury Athletic", 124,
                        25, false),
                validate("HAston", "H Aston", "FarehamCrofton0", "Fareham & Crofton", 120, 39,
                        false),
                validate("CWalker1", "C Walker", "Portsmouth0", "Portsmouth", 120, 24, false),
                validate("PLarcombe0", "P Larcombe", "SarisburyAthletic0", "Sarisbury Athletic",
                        110, 36, false),
                validate("WBolton", "W Bolton", "SarisburyAthletic0", "Sarisbury Athletic", 98, 33,
                        true),
                validate("MShergill", "M Shergill", "LocksHeath", "Locks Heath", 94, 21, false),
                validate("BJones", "B Jones", "Hambledon0", "Hambledon", 93, 26, false),
                validate("SReynolds1", "S Reynolds", "Waterlooville0", "Waterlooville", 91, 22,
                        false),
                validate(
                        "DAitken", "D Aitken", "GosportBorough0", "Gosport Borough", 87, 29, false),
                validate("MCripps", "M Cripps", "Havant0", "Havant", 86, 23, false),
                validate("AMackie", "A Mackie", "SarisburyAthletic0", "Sarisbury Athletic", 86, 26,
                        true),
                validate("CCreal0", "C Creal", "GosportBorough0", "Gosport Borough", 83, 23, false),
                validate("CDean0", "C Dean", "Havant0", "Havant", 82, 32, false),
                validate("TGriffin", "T Griffin", "LocksHeath", "Locks Heath", 82, 27, true),
                validate("CParker0", "C Parker", "Waterlooville0", "Waterlooville", 82, 26, false),
                validate("TStratfordTuke", "T Stratford-Tuke", "Petersfield1", "Petersfield", 82,
                        29, false));
    }

    @Test
    public void testEmptyModel()
    {
        final BattingAverages result = new BattingAverages.Builder(LeagueSelector.SENIOR,
                Completeness.CONSISTENT, 50,
                new ModelAndRules(new Model(), new Rules.Builder().build())).build();
        checkAverages(result, validate(null, Status.UNSTARTED, 0));
    }

    @Test
    public void testModelWithLeaguesButNoMatches()
    {
        Model model = new Model();
        Stream
                .of("Division1", "Division2", "Division3", "Division4", "Division5", "ColtsUnder16",
                        "ColtsUnder13")
                .map(s ->
                {
                    League l = new League();
                    l.setId(s);
                    return l;
                })
                .forEach(model::addLeague);
        final BattingAverages result = new BattingAverages.Builder(LeagueSelector.SENIOR,
                Completeness.CONSISTENT, 50, new ModelAndRules(model, new Rules.Builder().build()))
                        .build();
        checkAverages(result, validate(null, Status.UNSTARTED, 0));
    }

    @Test
    public void testTeam()
    {
        final BattingAverages result = new BattingAverages.Builder(
                new TeamSelector("OPCSTitchfield"), Completeness.COMPLETE, null,
                new ModelAndRules(16)).build();
        checkAverages(result, validate(getDate("2016-02-28"), Status.FINAL, 0),
                validate("AWhite0", "A White", "OPCSTitchfield", "OPCS Titchfield", 6, 2, 82, 26,
                        false, 20.50),
                validate("Prasad", "Prasad", "OPCSTitchfield", "OPCS Titchfield", 5, 0, 61, 25,
                        true, 12.20),
                validate("PCallen", "P Callen", "OPCSTitchfield", "OPCS Titchfield", 5, 0, 49, 23,
                        true, 9.80),
                validate("JHicks", "J Hicks", "OPCSTitchfield", "OPCS Titchfield", 5, 0, 44, 23,
                        true, 8.80),
                validate("PHollyman", "P Hollyman", "OPCSTitchfield", "OPCS Titchfield", 6, 1, 42,
                        17, true, 8.40),
                validate("TPage", "T Page", "OPCSTitchfield", "OPCS Titchfield", 5, 2, 41, 29,
                        false, 13.67),
                validate("BSamuel", "B Samuel", "OPCSTitchfield", "OPCS Titchfield", 6, 4, 27, 10,
                        false, 13.50),
                validate("CParker", "C Parker", "OPCSTitchfield", "OPCS Titchfield", 4, 0, 18, 9,
                        true, 4.50),
                validate("SPrimmer", "S Primmer", "OPCSTitchfield", "OPCS Titchfield", 3, 0, 16, 9,
                        true, 5.33),
                validate("PBeadle", "P Beadle", "OPCSTitchfield", "OPCS Titchfield", 5, 1, 15, 9,
                        true, 3.75),
                validate("PMatthews", "P Matthews", "OPCSTitchfield", "OPCS Titchfield", 4, 0, 14,
                        10, true, 3.50));
    }

    @SafeVarargs
    private final void checkAverages(BattingAverages averages,
            Consumer<BattingAverages> tableValidator, Consumer<BattingRow>... rowValidators)
    {
        tableValidator.accept(averages);
        final Iterator<BattingRow> rowIterator = averages.getRows().stream().iterator();
        Arrays.stream(rowValidators).forEach(v -> v.accept(rowIterator.next()));
        assertThat(rowIterator.hasNext()).isFalse();
    }

    private Consumer<BattingAverages> validate(Date lastIncludedDate, Status status, int toCome)
    {
        return averages ->
        {
            assertThat(averages.getLastIncludedDate()).isEqualTo(lastIncludedDate);
            assertThat(averages.getStatus()).isEqualTo(status);
            assertThat(averages.getToCome()).isEqualTo(toCome);
        };
    }

    private Consumer<BattingRow> validate(String playerId, String playerName, String teamId,
            String teamName, int total, int best, boolean bestOut)
    {
        return row ->
        {
            assertThat(row.getPlayer().getId()).isEqualTo(playerId);
            assertThat(row.getPlayer().getName()).isEqualTo(playerName);
            assertThat(row.getTeam().getId()).isEqualTo(teamId);
            assertThat(row.getTeam().getName()).isEqualTo(teamName);
            assertThat(row.getRuns()).isEqualTo(total);
            assertThat(row.getBest().getRunsScored()).isEqualTo(best);
            assertThat(row.getBest().isOut()).isEqualTo(bestOut);
        };
    }

    private Consumer<BattingRow> validate(String playerId, String playerName, String teamId,
            String teamName, int inns, int notOut, int total, int best, boolean bestOut,
            Double average)
    {
        return row ->
        {
            assertThat(row.getPlayer().getId()).isEqualTo(playerId);
            assertThat(row.getPlayer().getName()).isEqualTo(playerName);
            assertThat(row.getTeam().getId()).isEqualTo(teamId);
            assertThat(row.getTeam().getName()).isEqualTo(teamName);
            assertThat(row.getInnings()).isEqualTo(inns);
            assertThat(row.getNotOut()).isEqualTo(notOut);
            assertThat(row.getRuns()).isEqualTo(total);
            assertThat(row.getBest().getRunsScored()).isEqualTo(best);
            assertThat(row.getBest().isOut()).isEqualTo(bestOut);
            assertThat(row.getAverage()).isEqualTo(average, offset(0.01));
        };
    }

    private Date getDate(String isoDate)
    {
        try
        {
            return DateUtils.truncate(DateUtils.parseDate(isoDate, "yyyy-MM-dd"), Calendar.DATE);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
