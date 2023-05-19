package jg.actionfigures.server.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jg.actionfigures.server.API.FigureRepository;
import jg.actionfigures.server.API.FilterRepository;
import jg.actionfigures.server.Models.PostgerSql.Figure;
import jg.actionfigures.server.Models.PostgerSql.Filter;

@RestController
@RequestMapping("/test")
public class MainController {

    @Autowired
    FigureRepository figureRepository;

    @Autowired
    FilterRepository filterRepository;

    @GetMapping("/xd")
    public String test() {
        return "тест";
    }

    @GetMapping("/catalog")
    public List<Filter> catalog() {
        List<Filter> figureList = (List<Filter>) filterRepository.findAll();
        return figureList;
    }
}
