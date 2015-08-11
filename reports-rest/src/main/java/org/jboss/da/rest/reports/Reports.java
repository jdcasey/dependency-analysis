package org.jboss.da.rest.reports;

import org.apache.commons.lang.NotImplementedException;
import org.jboss.da.communication.CommunicationException;
import org.jboss.da.communication.model.GAV;
import org.jboss.da.listings.api.service.BlackArtifactService;
import org.jboss.da.listings.api.service.WhiteArtifactService;
import org.jboss.da.reports.api.ArtifactReport;
import org.jboss.da.reports.api.ReportsGenerator;
import org.jboss.da.reports.backend.api.VersionFinder;
import org.jboss.da.rest.reports.model.LookupReport;
import org.jboss.da.rest.reports.model.Report;
import org.jboss.da.rest.reports.model.SCMRequest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Main end point for the reports
 * 
 * @author Dustin Kut Moy Cheung
 * @author Jakub Bartecek <jbartece@redhat.com>
 *
 */
@Path("/reports")
@Api(value = "/reports", description = "Get report of dependencies of projects")
public class Reports {

    @Inject
    private VersionFinder versionFinder;

    @Inject
    private ReportsGenerator reportsGenerator;

    @Inject
    private WhiteArtifactService whiteArtifactService;

    @Inject
    private BlackArtifactService blackArtifactService;

    @POST
    @Path("/scm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get dependency report for a project specified in a repository URL",
            hidden = true)
    // TODO unhide when the method will be implemented
    public Report scmGenerator(SCMRequest scmRequest) {
        throw new NotImplementedException();
    }

    @POST
    @Path("/gav")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get dependency report for a GAV " // TODO change when dependencies will be implemented
                    + "(Currently the dependencies and dependency_versions_satisfied don't contains usefull values)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report was successfully generated"),
            @ApiResponse(code = 404,
                    message = "Requested GA was not found or the repository is not available"), })
    public Response gavGenerator(GAV gavRequest) {
        ArtifactReport artifactReport = reportsGenerator.getReport(gavRequest);
        if (artifactReport == null)
            return Response.status(Status.NOT_FOUND)
                    .entity("Requested GA was not found or the repository is not available")
                    .build();
        else
            return Response.ok().entity(toReport(artifactReport)).build();
    }

    @POST
    @Path("/lookup/gav")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Lookup built versions for the list of provided GAVs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup report was successfully generated"),
            @ApiResponse(code = 404,
                    message = "Requested GA was not found or the repository is not available"), })
    public Response lookupGav(List<GAV> gavRequest) {
        try {
            List<LookupReport> reportsList = new ArrayList<>();
            for (GAV gav : gavRequest)
                reportsList.add(toLookupReport(gav));

            return Response.ok().entity(reportsList).build();
        } catch (CommunicationException e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Requested GA was not found or the repository is not available")
                    .build();
        }
    }

    private Report toReport(ArtifactReport report) {
        return new Report(report.getGav().getGroupId(), report.getGav().getArtifactId(), report
                .getGav().getVersion(), new ArrayList<String>(report.getAvailableVersions()),
        // TODO change when dependencies will be implemented
                report.getBestMatchVersion(), false, new ArrayList<Report>(),
                report.isBlacklisted(), report.isWhiteListed());
    }

    private LookupReport toLookupReport(GAV gav) throws CommunicationException {
        return new LookupReport(gav, versionFinder.getBestMatchVersionFor(gav), isBlacklisted(gav),
                isWhitelisted(gav));
    }

    private boolean isBlacklisted(GAV gav) {
        return blackArtifactService.isArtifactPresent(gav.getGroupId(), gav.getArtifactId(),
                gav.getVersion());
    }

    private boolean isWhitelisted(GAV gav) {
        return whiteArtifactService.isArtifactPresent(gav.getGroupId(), gav.getArtifactId(),
                gav.getVersion());
    }

}
