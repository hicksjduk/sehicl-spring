package uk.org.sehicl.website;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.StaticPage;
import uk.org.sehicl.website.template.Template;

@RestController
public class Controller
{

    private String getRequestUri(HttpServletRequest req)
    {
        String answer = Stream
                .of(req.getRequestURI(), req.getPathInfo())
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        return answer;
    }

    @RequestMapping("/")
    public String home(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new HomePage(uri)).process();
    }

    @RequestMapping("/contacts")
    public String contacts(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new ContactsPage(uri)).process();
    }

    @RequestMapping("/resources")
    public String resources(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("resources", "resources.ftlh", Section.RESOURCES, uri,
                "SEHICL Resources")).process();
    }
}