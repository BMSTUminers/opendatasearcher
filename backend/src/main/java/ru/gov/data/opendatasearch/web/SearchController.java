package ru.gov.data.opendatasearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gov.data.opendatasearch.dto.SearchResult;
import ru.gov.data.opendatasearch.service.SearchService;

import java.util.List;

@RestController
@RequestMapping({"/search"})
public class SearchController {
    @Autowired
    private SearchService searchService;

    public SearchController() {
    }

    @RequestMapping({"query"})
    public List<SearchResult> query(@RequestParam String query) {
        return this.searchService.query(query);
    }

    @RequestMapping(value = {"kml"}, produces = {"application/octet-stream"})
    @ResponseBody
    public byte[] kml(@RequestParam String id) {
        String kml = this.searchService.kml(id);
        return kml.getBytes();
    }
}