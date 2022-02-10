package com.onlinemed.servises.api;

import com.onlinemed.model.dto.DrugHints;
import com.onlinemed.model.dto.DrugInfo;

import java.util.List;

public interface DrugEquivalentsService {

    List<DrugHints> generateEquivalentsHints(String searchedWord);

    List<DrugInfo> generateDrugInfoList(String queryLink);
}
