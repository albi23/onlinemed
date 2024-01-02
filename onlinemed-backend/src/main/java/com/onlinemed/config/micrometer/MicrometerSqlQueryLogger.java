package com.onlinemed.config.micrometer;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.StatementType;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;

import java.util.List;
import java.util.SortedMap;

final class MicrometerSqlQueryLogger extends DefaultQueryLogEntryCreator {

    public String queryString(List<QueryInfo> queryInfoList) {
        var sb = new StringBuilder();
        for (QueryInfo queryInfo : queryInfoList) {
            sb.append(queryInfo.getQuery().replace("\n", " "));
            sb.append(",");
        }
        return sb.toString();
    }

    public String paramsEntry(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
        var sb = new StringBuilder();
        boolean isPrepared = execInfo.getStatementType() == StatementType.PREPARED;
        for (QueryInfo queryInfo : queryInfoList) {
            for (List<ParameterSetOperation> parameters : queryInfo.getParametersList()) {
                SortedMap<String, String> paramMap = getParametersToDisplay(parameters);
                // parameters per batch.
                //   for prepared: (val1,val2,...)
                //   for callable: (key1=val1,key2=val2,...)
                if (isPrepared) {
                    writeParamsForSinglePreparedEntry(sb, paramMap, execInfo, queryInfoList);
                } else {
                    writeParamsForSingleCallableEntry(sb, paramMap, execInfo, queryInfoList);
                }

            }
        }
        chompIfEndWith(sb, ',');
        return sb.toString();
    }

}