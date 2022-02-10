package com.onlinemed.servises.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.model.dto.DrugHints;
import com.onlinemed.model.dto.DrugInfo;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.servises.api.DrugEquivalentsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Service
public class DrugEquivalentsImpl implements DrugEquivalentsService {

    private final String BASE_URL = "https://www.lekinfo24.pl";
    private final String URl_TEMPLATE = BASE_URL + "/wyniki-wyszukiwania?qh=%s&q=&index-type=handlowa";
    private static final WebClient c;

    static {
        c = new WebClient();
        c.getOptions().setJavaScriptEnabled(false);
        c.getOptions().setCssEnabled(false);
        c.getOptions().setUseInsecureSSL(false);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }

    public List<DrugHints> generateEquivalentsHints(String searchedWord) {

        if (searchedWord == null || searchedWord.isEmpty()) {
            throw new ValidationException(new Violation("model.incorrect-query-word"));
        }
        final ArrayList<DrugHints> hints = new ArrayList<>();
        try {
            final HtmlPage page = c.getPage(String.format(URl_TEMPLATE, searchedWord));
            final List<HtmlUnorderedList> byXPath = (List<HtmlUnorderedList>) page.getByXPath("//*[starts-with(@class, 'lista-handlowe')]");
            for (HtmlUnorderedList ul : byXPath) {
                for (DomElement el : ul.getChildElements()) {
                    final HtmlAnchor anchor = (HtmlAnchor) el.getFirstElementChild();
                    hints.add(new DrugHints(anchor.asText(), anchor.getHrefAttribute()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValidationException(new Violation("model.temporary-error"));
        }
        return hints;
    }

    public List<DrugInfo> generateDrugInfoList(String queryLink) {

        if (queryLink == null || queryLink.isEmpty()) {
            throw new ValidationException(new Violation("model.url-link"));
        }
        List<DrugInfo> drugInfo = new ArrayList<>();
        try {
            HtmlPage page = c.getPage(BASE_URL + queryLink);
            HtmlTableBody tableBody = (HtmlTableBody) page.getByXPath("//tbody").get(0);
            List<HtmlTableRow> rows = tableBody.getRows();
            String[] info = new String[11];
            for (HtmlTableRow row : rows) {
                List<HtmlTableCell> cells = row.getCells();
                HtmlTableCell firstCell = cells.iterator().next();
                HtmlAnchor anchor = firstCell.getFirstByXPath("div[@class='zamiennik']/a");
                HtmlBold b = firstCell.getFirstByXPath("b[@class='nazwa-leku']");
                int iteration = 0;
                if (b != null) info[iteration++] = b.asText();
                if (anchor != null) info[iteration] = anchor.getHrefAttribute();
                for (HtmlTableCell cell : cells.subList(1, cells.size())) {
                    info[++iteration] = cell.asText();
                    if (iteration + 1 >= info.length) break;
                }
                drugInfo.add(toFixedDrugInfoForm(info));
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new ValidationException(new Violation("model.temporary-error"));
        }
        return drugInfo;
    }

    private DrugInfo toFixedDrugInfoForm(String[] info) {
        info[9] = extractPriceAndCurrency(info[9]);
        info[8] = extractPercentage(info[8]);
        final String[] split = info[7].split(":");
        info[7] = split.length == 1 ? split[0] : split[1];
        return new DrugInfo(info[0], info[1], info[2], info[3], info[4], info[7], info[8], info[9]);

    }

    private String extractPriceAndCurrency(String drugData) {
        final String[] split = drugData.split(":");
        return (split.length == 1) ? split[0] : split[1].concat(split[0].substring(split[0].length() - 5));
    }

    private String extractPercentage(String drugData) {
        final String[] split = drugData.split(":");
        return (split.length == 1) ? split[0] : split[1];
    }
}
