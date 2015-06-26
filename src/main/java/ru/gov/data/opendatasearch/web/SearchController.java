package ru.gov.data.opendatasearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gov.data.opendatasearch.dto.SearchResult;
import ru.gov.data.opendatasearch.service.SearchService;

@RestController
@RequestMapping({"/search"})
public class SearchController {
    @Autowired
    private SearchService searchService;

    public SearchController() {
    }

    @RequestMapping({"query"})
    public SearchResult query(@RequestParam String query) {
        return this.searchService.query(query);
    }

    @RequestMapping(value = {"kml"}, produces = {"application/octet-stream"})
    @ResponseBody
    public byte[] kml(@RequestParam String query) {
        String kml = this.searchService.kml(query);
        return kml.getBytes();
    }
}