package uk.org.sehicl.website.resultentry;

import static org.assertj.core.api.Assertions.*;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.resultentry.Result.ResultException;

public class ResultTest
{
    @Test
    public void testCreateExceptionNoArguments()
    {
        final ResultException ex = ResultException.create("Hello");
        assertThat(ex.getMessage()).isEqualTo("Hello");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    public void testCreateExceptionOneArgumentNotThrowable()
    {
        final ResultException ex = ResultException.create("Hello %s", "You");
        assertThat(ex.getMessage()).isEqualTo("Hello You");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    public void testCreateExceptionOneArgumentThrowable()
    {
        final Throwable cause = new Throwable();
        final ResultException ex = ResultException.create("Hello", cause);
        assertThat(ex.getMessage()).isEqualTo("Hello");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    public void testCreateExceptionMultipleArgumentsLastOneNotThrowable()
    {
        final ResultException ex = ResultException.create("Hello %d %d %d", 1, 2, 3);
        assertThat(ex.getMessage()).isEqualTo("Hello 1 2 3");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    public void testCreateExceptionMultipleArgumentsLastOneThrowable()
    {
        final IllegalAccessException cause = new IllegalAccessException();
        final ResultException ex = ResultException.create("Hello %d %d %d", 1, 2, 3, cause);
        assertThat(ex.getMessage()).isEqualTo("Hello 1 2 3");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test(expected = ResultException.class)
    public void testCreateResultLeagueNotFound() throws ResultException
    {
        new Result(ModelLoader.getModel(18), "rubbish", "rubbish", "rubbish");
    }

    @Test(expected = ResultException.class)
    public void testCreateResultHomeTeamNotFound() throws ResultException
    {
        new Result(ModelLoader.getModel(18), "Division5", "rubbish", "rubbish");
    }

    @Test(expected = ResultException.class)
    public void testCreateResultAwayTeamNotFound() throws ResultException
    {
        new Result(ModelLoader.getModel(18), "Division5", "OPCSTitchfield", "rubbish");
    }

    @Test(expected = ResultException.class)
    public void testCreateResultMatchNotFound() throws ResultException
    {
        new Result(ModelLoader.getModel(18), "Division5", "OPCSTitchfield", "PortsmouthSouthseaB");
    }

    @Test
    public void testCreateResultMatchFound() throws ResultException
    {
        final Result result = new Result(ModelLoader.getModel(18), "Division5",
                "PortsmouthSouthseaB", "OPCSTitchfield");
        assertThat(result.homeTeam.name).isEqualTo("Portsmouth & Southsea B");
        assertThat(result.awayTeam.name).isEqualTo("OPCS Titchfield");
        assertThat(result.court).isEqualTo("B");
        assertThat(result.date).isEqualTo("4th March 2018");
        assertThat(result.time).isEqualTo("9:15");
        assertThat(result.leagueId).isEqualTo("Division5");
        assertThat(result.leagueName).isEqualTo("Division 5");
    }

    @Test
    public void testJsonUnmarshal() throws Exception
    {
        final Result result = new Result(ModelLoader.getModel(18), "Division5", "PortsmouthSouthseaB", "OPCSTitchfield");
        final Writer sw = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writeValue(sw, result);
        System.out.println(sw.toString());
    }
}
