package uk.org.sehicl.website;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.template.Template;

@RestController
public class Controller
{

    private String getRequestUri(HttpServletRequest req)
    {
        return req.getRequestURI() + req.getPathInfo() == null ? "" : req.getPathInfo();
    }

    @RequestMapping("/")
    public String home(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new HomePage(uri)).process();
    }

}