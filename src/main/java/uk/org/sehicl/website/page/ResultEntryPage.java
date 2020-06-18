package uk.org.sehicl.website.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.resultentry.HowOut;
import uk.org.sehicl.website.resultentry.InningsDataField;
import uk.org.sehicl.website.resultentry.MatchData;
import uk.org.sehicl.website.resultentry.MatchData.ResultException;
import uk.org.sehicl.website.resultentry.PlayerDataField;

public class ResultEntryPage extends Page
{
    private final MatchData result;
    private final TemplateModel howOut;
    private final TemplateModel inningsDataField;
    private final TemplateModel playerDataField;

    public ResultEntryPage(String uri, Integer season, String leagueId, String homeTeamId,
            String awayTeamId) throws ResultException
    {
        super("result", "resultEntry.ftlh", null, uri);
        final Model model = season == null ? ModelLoader.getModel() : ModelLoader.getModel(season);
        this.result = new MatchData(model, leagueId, homeTeamId, awayTeamId);
        try
        {
            BeansWrapper wrapper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
            howOut = wrapper.getEnumModels().get(HowOut.class.getName());
            inningsDataField = wrapper.getEnumModels().get(InningsDataField.class.getName());
            playerDataField = wrapper.getEnumModels().get(PlayerDataField.class.getName());
        }
        catch (TemplateModelException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTitle()
    {
        return String.format("SEHICL result: %s v %s", result.getHomeTeam().name, result.getAwayTeam().name);
    }

    public MatchData getResult()
    {
        return result;
    }

    public TemplateModel getHowOut()
    {
        return howOut;
    }

    public TemplateModel getInningsDataField()
    {
        return inningsDataField;
    }

    public TemplateModel getPlayerDataField()
    {
        return playerDataField;
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
