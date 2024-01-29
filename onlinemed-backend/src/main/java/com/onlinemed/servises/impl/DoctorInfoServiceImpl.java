package com.onlinemed.servises.impl;

import com.onlinemed.model.*;
import com.onlinemed.model.translations.Language_;
import com.onlinemed.servises.api.DoctorInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value="doctorInfoServiceImpl")
public class DoctorInfoServiceImpl extends BaseObjectServiceImpl<DoctorInfo> implements DoctorInfoService {


    @Override
    public List<DoctorInfo> findAll2() {
        return super.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorInfo> findAll() {
        EntityGraph<DoctorInfo> entityGraph = getEntityGraph();// (EntityGraph<DoctorInfo>) entityManager.getEntityGraph("post-entity-graph");
        var cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DoctorInfo> criteriaQuery = cb.createQuery(DoctorInfo.class);
        Root<DoctorInfo> root = criteriaQuery.from(DoctorInfo.class);
        TypedQuery<DoctorInfo> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setHint("jakarta.persistence.loadgraph", entityGraph);
        return typedQuery.getResultList();
    }


    private EntityGraph<DoctorInfo> getEntityGraph() {
        EntityGraph<DoctorInfo> graph = entityManager.createEntityGraph(DoctorInfo.class);
        graph.addAttributeNodes(
                DoctorInfo_.phoneNumber.getName(),
                DoctorInfo_.specialization.getName(),
                DoctorInfo_.doctorType.getName()
        );
        Subgraph<FacilityLocation> facilityLocations = graph.addSubgraph(DoctorInfo_.facilityLocations.getName());
        facilityLocations.addAttributeNodes(
                FacilityLocation_.facilityName.getName(),
                FacilityLocation_.facilityAddress.getName(),
                FacilityLocation_.visitsPriceList.getName()
        );
        Subgraph<Person> personGraph = graph.addSubgraph(DoctorInfo_.person.getName());
        personGraph.addAttributeNodes(
                Person_.name.getName(),
                Person_.surname.getName(),
                Person_.userName.getName(),
                Person_.email.getName(),
                Person_.phoneNumber.getName()
        );
        return graph;

    }
}
