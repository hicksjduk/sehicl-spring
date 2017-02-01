package uk.org.sehicl.website;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.template.Template;

@RestController
public class Controller {

    @RequestMapping("/")
    public String home() {
        return new Template(new HomePage("/")).process();
    }

}