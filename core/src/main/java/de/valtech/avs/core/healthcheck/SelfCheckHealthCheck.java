/*
 * Copyright 2020 Valtech GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.valtech.avs.core.healthcheck;

import java.io.ByteArrayInputStream;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.hc.api.HealthCheck;
import org.apache.sling.hc.api.Result;
import org.apache.sling.hc.api.Result.Status;
import org.apache.sling.hc.util.FormattingResultLog;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.valtech.avs.api.service.AvsException;
import de.valtech.avs.api.service.AvsService;
import de.valtech.avs.api.service.scanner.ScanResult;
import de.valtech.avs.core.serviceuser.ServiceResourceResolverService;

/**
 * Checks if the basic setup is ok.
 *
 * @author Roland Gruber
 */
@Component(immediate = true, service = HealthCheck.class, property = {HealthCheck.TAGS + "=avs",
        HealthCheck.NAME + "=AVS Self Check", HealthCheck.MBEAN_NAME + "=avsSelfCheckHCmBean"})
public class SelfCheckHealthCheck implements HealthCheck {

    @Reference
    private ServiceResourceResolverService resolverService;

    @Reference
    private AvsService avsService;

    @Override
    public Result execute() {
        final FormattingResultLog resultLog = new FormattingResultLog();
        checkServiceResolver(resultLog);
        if (resultLog.getAggregateStatus().equals(Status.CRITICAL)) {
            return new Result(resultLog);
        }
        checkActiveScannersAvailable(resultLog);
        return new Result(resultLog);
    }

    /**
     * Checks if the service resource resolvers are accessible.
     * 
     * @param resultLog result log
     */
    private void checkServiceResolver(FormattingResultLog resultLog) {
        try (ResourceResolver resolver = resolverService.getServiceResourceResolver()) {
            if (resolver == null) {
                resultLog.critical("Unable to open service resource resolver: null");
                return;
            }
            resultLog.info("Service user ok");
        } catch (LoginException e) {
            resultLog.critical("Unable to open service resource resolver {}", e.getMessage());
        }
    }

    /**
     * Checks if there is at least one scan engine configured.
     * 
     * @param resultLog result log
     */
    private void checkActiveScannersAvailable(FormattingResultLog resultLog) {
        if (!avsService.hasActiveScanEngines()) {
            resultLog.critical("No active scan engines available");
        } else {
            resultLog.info("At least one active scan engine available");
            checkSampleScan(resultLog);
        }
    }

    /**
     * Performs a test scan.
     * 
     * @param resultLog result log
     */
    private void checkSampleScan(FormattingResultLog resultLog) {
        ByteArrayInputStream stream = new ByteArrayInputStream(SelfCheckHealthCheck.class.getName().getBytes());
        try {
            ScanResult result = avsService.scan(stream, "HEALTH CHECK");
            if (result.isClean()) {
                resultLog.info("Test scan ok");
            } else {
                resultLog.critical("False positive result: {}", result.getOutput());
            }
        } catch (AvsException e) {
            resultLog.critical("Error running test scan: {}", e.getMessage());
        }
    }

}
