package com.onlinemed.config.micrometer;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.time.Duration.ofMillis;


public final class MicrometerListener extends SLF4JQueryLoggingListener {

    private final MicrometerSqlQueryLogger logCreator;
    private static final Logger LOGGER = LoggerFactory.getLogger(MicrometerListener.class);


    public MicrometerListener() {
        super();
        this.logCreator = new MicrometerSqlQueryLogger();

    }

    @Override
    public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
        String query = logCreator.queryString(queryInfoList);
        String params = logCreator.paramsEntry(execInfo, queryInfoList).replace(" ", "_");

        LOGGER.debug("query >{}<  | param >{}<", query, params);
        long elapsedTime = Math.max(1L, execInfo.getElapsedTime());
        Timer.builder("sql.query")
                .description("Provide information about sql time execution ")
                .tags(query, params)
                .serviceLevelObjectives(ofMillis(elapsedTime))
                .publishPercentileHistogram()
                .register(Metrics.globalRegistry);

        super.afterQuery(execInfo, queryInfoList);
    }
}
