package uk.org.sehicl.website;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uk.org.sehicl.admin.UsersExporter;
import uk.org.sehicl.admin.UsersImporter;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ActivatePage;
import uk.org.sehicl.website.page.ArchiveIndexPage;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.DateResultsPage;
import uk.org.sehicl.website.page.DeleteUserPage;
import uk.org.sehicl.website.page.FullContactsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.LeagueBattingAveragesPage;
import uk.org.sehicl.website.page.LeagueBowlingAveragesPage;
import uk.org.sehicl.website.page.LeagueFixturesPage;
import uk.org.sehicl.website.page.LeagueResultsPage;
import uk.org.sehicl.website.page.LeagueTablePage;
import uk.org.sehicl.website.page.LeagueTablesPage;
import uk.org.sehicl.website.page.LoginPage;
import uk.org.sehicl.website.page.Page;
import uk.org.sehicl.website.page.ReconfirmPage;
import uk.org.sehicl.website.page.RegisterConfPage;
import uk.org.sehicl.website.page.RegisterPage;
import uk.org.sehicl.website.page.ResetPage;
import uk.org.sehicl.website.page.SeasonArchiveIndexPage;
import uk.org.sehicl.website.page.StaticPage;
import uk.org.sehicl.website.page.TeamAveragesIndexPage;
import uk.org.sehicl.website.page.TeamAveragesPage;
import uk.org.sehicl.website.page.TeamFixturesPage;
import uk.org.sehicl.website.page.UserDetailsPage;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.template.PageTemplate;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.Login;
import uk.org.sehicl.website.users.Reconfirm;
import uk.org.sehicl.website.users.Register;
import uk.org.sehicl.website.users.Reset;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserException;
import uk.org.sehicl.website.users.UserManager;

@RestController
public class Controller
{
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    private static final boolean forceHttps = EnvVar.FORCE_HTTPS
            .get()
            .filter(Boolean::parseBoolean)
            .isPresent();

    @Autowired
    private UserManager userManager;
    @Autowired
    private UsersExporter usersExporter;
    @Autowired
    private UsersImporter usersImporter;

    private String getRequestUri(UriComponentsBuilder uriBuilder)
    {
        if (forceHttps)
            uriBuilder.scheme("https://");
        return uriBuilder.toUriString();
    }

    private String getRequestUri(UriComponentsBuilder uriBuilder, String pathToResolve)
    {
        return URI.create(getRequestUri(uriBuilder)).resolve(pathToResolve).toString();
    }

    @RequestMapping("/")
    public String home(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new HomePage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/contacts")
    public String contacts(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new ContactsPage(getRequestUri(uriBuilder))).process();
    }

    public String fullContactsPlaceholder(UriComponentsBuilder uriBuilder) throws IOException
    {
        return new PageTemplate(new StaticPage("contacts", "fullContactsPlaceholder.ftlh",
                Section.CONTACTS, getRequestUri(uriBuilder), "SEHICL Full Contacts")).process();
    }

    @RequestMapping("/fullContacts")
    public String fullContacts(UriComponentsBuilder uriBuilder, HttpSession session,
            HttpServletResponse resp) throws IOException
    {
        final UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), null))
            return new PageTemplate(new FullContactsPage(getRequestUri(uriBuilder))).process();
        userSession.setRedirectTarget(getRequestUri(uriBuilder));
        resp.sendRedirect(getRequestUri(uriBuilder, "/login"));
        return "";
    }

    @RequestMapping("/resources")
    public String resources(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                getRequestUri(uriBuilder), "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("rules", "rules.ftlh", Section.RULES,
                getRequestUri(uriBuilder), "SEHICL Rules")).process();
    }

    @RequestMapping("/rules/constitution")
    public String constitution(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("rules", "rules/constitution/index.ftlh",
                Section.RULES, getRequestUri(uriBuilder), "SEHICL Constitution")).process();
    }

    @RequestMapping("/rules/administration")
    public String adminRules(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("rules", "rules/administration/index.ftlh",
                Section.RULES, getRequestUri(uriBuilder), "SEHICL Administrative Rules")).process();
    }

    @RequestMapping("/rules/playing")
    public String playingRules(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("rules", "rules/playing/index.ftlh", Section.RULES,
                getRequestUri(uriBuilder), "SEHICL Rules Of Play")).process();
    }

    @RequestMapping("/records")
    public String recordsIndex(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("records", "records/index.ftlh", Section.RECORDS,
                getRequestUri(uriBuilder), "SEHICL Records")).process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("records", "records/performances.ftlh",
                Section.RECORDS, getRequestUri(uriBuilder), "SEHICL Record Performances"))
                        .process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("divwinners", "records/divwinners.ftlh",
                Section.RECORDS, getRequestUri(uriBuilder), "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, getRequestUri(uriBuilder), "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                getRequestUri(uriBuilder), "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/presentation")
    public String presentationEvening(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("presentation", "presentation/schedule.ftlh",
                Section.HOME, getRequestUri(uriBuilder), "SEHICL Presentation Evening")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(UriComponentsBuilder uriBuilder, @PathVariable String season)
    {
        return new PageTemplate(new StaticPage("presentation",
                String.format("presentation/%s.ftlh", season), Section.ARCHIVE,
                getRequestUri(uriBuilder), String.format("SEHICL Presentation Evening %s", season)))
                        .process();
    }

    @RequestMapping("/tables")
    public String currentTables(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new LeagueTablesPage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(UriComponentsBuilder uriBuilder, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueTablePage(leagueId, getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(UriComponentsBuilder uriBuilder, @PathVariable String leagueId,
            @PathVariable int season)
    {
        var uri = getRequestUri(uriBuilder);
        Page page = season <= 5
                ? new StaticPage("archive", String.format("archive%d/%s.html", season, leagueId),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueTablePage(leagueId, season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(UriComponentsBuilder uriBuilder,
            @PathVariable String selector)
    {
        return new PageTemplate(new LeagueBattingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri(uriBuilder)))
                        .process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(UriComponentsBuilder uriBuilder,
            @PathVariable String selector, @PathVariable int season)
    {
        var uri = getRequestUri(uriBuilder);
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBatting.html", season, selector),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(UriComponentsBuilder uriBuilder,
            @PathVariable String selector)
    {
        return new PageTemplate(new LeagueBowlingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri(uriBuilder)))
                        .process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(UriComponentsBuilder uriBuilder,
            @PathVariable String selector, @PathVariable int season)
    {
        var uri = getRequestUri(uriBuilder);
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBowling.html", season, selector),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(UriComponentsBuilder uriBuilder, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}")
    public String archiveTeamAverages(UriComponentsBuilder uriBuilder, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, null, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(UriComponentsBuilder uriBuilder, @PathVariable String teamId,
            @PathVariable int season)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, season, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new TeamAveragesIndexPage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES,
                getRequestUri(uriBuilder), "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new ArchiveIndexPage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(UriComponentsBuilder uriBuilder, @PathVariable int season)
    {
        return new PageTemplate(new SeasonArchiveIndexPage(getRequestUri(uriBuilder), season))
                .process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(UriComponentsBuilder uriBuilder, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(UriComponentsBuilder uriBuilder, @PathVariable String teamId,
            @PathVariable int season)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, season, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new LeagueFixturesPage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/fixtures/{season}")
    public String leagueFixtures(UriComponentsBuilder uriBuilder, @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(UriComponentsBuilder uriBuilder, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueFixturesPage(leagueId, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/fixtures/league/{leagueId}/{season}")
    public String leagueFixtures(UriComponentsBuilder uriBuilder, @PathVariable String leagueId,
            @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, leagueId, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new DateResultsPage(getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResults(UriComponentsBuilder uriBuilder, @PathVariable String date)
            throws ParseException
    {
        return new PageTemplate(new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd"),
                getRequestUri(uriBuilder))).process();
    }

    @RequestMapping("/results/league/{leagueId}")
    public String leagueResults(UriComponentsBuilder uriBuilder, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueResultsPage(leagueId, getRequestUri(uriBuilder)))
                .process();
    }

    @RequestMapping("/dutyRota")
    public String dutyRota(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new StaticPage("dutyRota", "dutyRota.ftlh", Section.FIXTURES,
                getRequestUri(uriBuilder), "SEHICL Duty Rota")).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(UriComponentsBuilder uriBuilder)
    {
        return new PageTemplate(new LoginPage(getRequestUri(uriBuilder), userManager)).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(UriComponentsBuilder uriBuilder, HttpSession session,
            HttpServletResponse resp, @RequestParam String email, @RequestParam String password,
            @RequestParam String loginCmd) throws IOException
    {
        Login login = new Login(userManager, email, password);
        var uri = getRequestUri(uriBuilder);
        if (loginCmd != null)
        {
            Long token = login.validateAndLogin();
            if (token != null)
            {
                UserSession userSession = new UserSession(session);
                userSession.setToken(token);
                resp.sendRedirect(userSession.getRedirectTarget());
                return "";
            }
        }
        else
        {
            login.validateAndReset(URI.create(uri).resolve("/pwdReset").toString());
        }
        return new PageTemplate(new LoginPage(uri, login)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(UriComponentsBuilder uriBuilder) throws IOException
    {
        return new PageTemplate(new RegisterPage(getRequestUri(uriBuilder), userManager)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(UriComponentsBuilder uriBuilder, HttpServletResponse resp,
            @RequestParam String email, @RequestParam String name, @RequestParam String club,
            @RequestParam String password, @RequestParam String passwordConf,
            @RequestParam String agreement,
            @RequestParam("g-recaptcha-response") String recaptchaResponse) throws IOException
    {
        if (!realPerson(recaptchaResponse))
        {
            resp.sendRedirect(getRequestUri(uriBuilder, "/register"));
            return "";
        }
        Register register = new Register(userManager, email, name, club, password, passwordConf,
                agreement != null);
        User user = register.validateAndRegister(getRequestUri(uriBuilder, "/"));
        return new PageTemplate(user == null ? new RegisterPage(getRequestUri(uriBuilder), register)
                : new RegisterConfPage(getRequestUri(uriBuilder), user)).process();
    }

    @RequestMapping(path = "/activate/{userId}")
    public String activate(UriComponentsBuilder uriBuilder, @PathVariable long userId)
            throws IOException
    {
        User user = null;
        try
        {
            user = userManager.activateUser(userId);
        }
        catch (UserException ex)
        {
        }
        return new PageTemplate(new ActivatePage(getRequestUri(uriBuilder), user)).process();
    }

    @RequestMapping(path = "/userDetails/{userId}")
    public String userDetails(UriComponentsBuilder uriBuilder, HttpSession session,
            HttpServletResponse resp, @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new UserDetailsPage(getRequestUri(uriBuilder), user)).process();
        }
        userSession.setRedirectTarget(getRequestUri(uriBuilder));
        resp.sendRedirect(getRequestUri(uriBuilder, "/login"));
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.GET)
    public String deleteUser(UriComponentsBuilder uriBuilder, HttpSession session,
            HttpServletResponse resp, @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new DeleteUserPage(getRequestUri(uriBuilder), user, false))
                    .process();
        }
        userSession.setRedirectTarget(getRequestUri(uriBuilder));
        resp.sendRedirect(getRequestUri(uriBuilder, "/login"));
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.POST)
    public String deleteUserConfirmed(UriComponentsBuilder uriBuilder, HttpSession session,
            HttpServletResponse resp, @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            userManager.deleteUser(userId);
            return new PageTemplate(new DeleteUserPage(getRequestUri(uriBuilder), user, true))
                    .process();
        }
        userSession.setRedirectTarget(getRequestUri(uriBuilder));
        resp.sendRedirect(getRequestUri(uriBuilder, "/login"));
        return "";
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.GET)
    public String passwordReset(UriComponentsBuilder uriBuilder, @PathVariable long resetId)
            throws IOException
    {
        return new PageTemplate(
                new ResetPage(getRequestUri(uriBuilder), new Reset(resetId, userManager)))
                        .process();
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.POST)
    public String passwordReset(UriComponentsBuilder uriBuilder, HttpServletResponse resp,
            @PathVariable long resetId, @RequestParam String password,
            @RequestParam String passwordConf,
            @RequestParam("g-recaptcha-response") String recaptchaResponse) throws IOException
    {
        if (!realPerson(recaptchaResponse))
        {
            resp.sendRedirect(getRequestUri(uriBuilder, "/pwdReset/%d".formatted(resetId)));
            return "";
        }
        Reset reset = new Reset(resetId, userManager);
        if (reset.validateAndReset(password, passwordConf))
        {
            resp.sendRedirect(getRequestUri(uriBuilder, "/login"));
            return "";
        }
        return new PageTemplate(new ResetPage(getRequestUri(uriBuilder), reset)).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.GET)
    public String reconfirmUser(UriComponentsBuilder uriBuilder, @PathVariable long userId)
            throws IOException
    {
        return new PageTemplate(
                new ReconfirmPage(getRequestUri(uriBuilder), new Reconfirm(userId, userManager)))
                        .process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.POST)
    public String reconfirm(UriComponentsBuilder uriBuilder, HttpServletResponse resp,
            @PathVariable long userId, @RequestParam String agreement) throws IOException
    {
        Reconfirm reconfirm = new Reconfirm(userId, userManager);
        try
        {
            if (reconfirm.validateAndReconfirm(agreement != null))
            {
                resp.sendRedirect(getRequestUri(uriBuilder, "/reconfConf"));
                return "";
            }
        }
        catch (EmailException ex)
        {
            LOG.error("Unexpected exception", ex);
        }
        return new PageTemplate(new ReconfirmPage(getRequestUri(uriBuilder), reconfirm)).process();
    }

    @RequestMapping(path = "/reconfConf")
    public String confirmReconfirmation(UriComponentsBuilder uriBuilder) throws IOException
    {
        return new PageTemplate(new StaticPage("reconfirm", "reconfConf.ftlh", null,
                getRequestUri(uriBuilder), "Thank you")).process();
    }

    @RequestMapping(path = "/dp")
    public String dataProtection(UriComponentsBuilder uriBuilder) throws IOException
    {
        return new PageTemplate(new StaticPage("dp", "dataProtection.ftlh", Section.DP,
                getRequestUri(uriBuilder), "SEHICL Data Protection Policy")).process();
    }

    @RequestMapping(path = "/admin/reconf")
    public String sendUserReconfirmRequests(UriComponentsBuilder uriBuilder, HttpSession session)
            throws IOException
    {
        final UserSession userSession = new UserSession(session);
        if (!userManager.sessionHasRole(userSession.getToken(), "admin"))
            return "Not authorised";
        String reconfirmationPageAddress = URI
                .create(getRequestUri(uriBuilder))
                .resolve("/reconfirm")
                .toString();
        userManager.sendReconfirmationEmails(reconfirmationPageAddress);
        return "Emails sent";
    }

    @RequestMapping(path = "/admin/userExport")
    public String exportUsers(UriComponentsBuilder uriBuilder, @RequestHeader String adminSecret,
            HttpServletResponse resp) throws IOException
    {
        if (adminSecret == null || EnvVar.ADMIN_SECRET.get().filter(adminSecret::equals).isEmpty())
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "";
        }
        return usersExporter.export();
    }

    @PostMapping(path = "/admin/userImport")
    public String importUsers(UriComponentsBuilder uriBuilder, @RequestHeader String adminSecret,
            @RequestBody String content, HttpServletResponse resp) throws IOException
    {
        try
        {
            if (adminSecret == null
                    || EnvVar.ADMIN_SECRET.get().filter(adminSecret::equals).isEmpty())
            {
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                return "";
            }
            if (content.length() == 0)
            {
                resp.setStatus(HttpStatus.BAD_REQUEST.value());
                return "";
            }
            return String
                    .format("%d user(s) imported",
                            usersImporter.importUsers(new StringReader(content)));
        }
        catch (Throwable ex)
        {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw))
            {
                ex.printStackTrace(pw);
            }
            return sw.toString();
        }
    }

    static class CaptchaResponse
    {
        Boolean success;
        Date timestamp;
        String hostname;
        @JsonProperty("error-codes")
        List<String> errorCodes;
    }

    @Value("${recaptcha.url:https://www.google.com/recaptcha/api/siteverify}")
    private String recaptchaUrl;

    private boolean realPerson(String recaptchaResponse) throws IOException
    {
        var restTemplate = new RestTemplate();
        var requestMap = new LinkedMultiValueMap<String, String>();
        requestMap.add("secret", EnvVar.RECAPTCHA_SECRET.getAsString());
        requestMap.add("response", recaptchaResponse);
        var apiResponse = restTemplate
                .postForObject(recaptchaUrl, requestMap, CaptchaResponse.class);
        return apiResponse != null && Boolean.TRUE.equals(apiResponse.success);
    }
}