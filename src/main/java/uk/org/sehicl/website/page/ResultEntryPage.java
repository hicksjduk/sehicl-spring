package uk.org.sehicl.website.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.resultentry.Result;
import uk.org.sehicl.website.resultentry.Result.ResultException;

public class ResultEntryPage extends Page
{
    private final Result result;

    public ResultEntryPage(String uri, String leagueId, String homeTeamId, String awayTeamId)
            throws ResultException
    {
        super("result", "resultEntry.ftlh", null, uri);
        this.result = new Result(ModelLoader.getModel(18), leagueId, homeTeamId, awayTeamId);
    }

    @Override
    public String getTitle()
    {
        return String.format("SEHICL result: %s v %s", result.homeTeam.name, result.awayTeam.name);
    }

    public Result getResult()
    {
        return result;
    }

    public String getJsonResult()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try
        {
            return mapper.writeValueAsString(result);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error marshalling result data to Json", e);
        }
    }
}
