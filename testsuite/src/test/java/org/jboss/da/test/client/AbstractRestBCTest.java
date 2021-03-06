package org.jboss.da.test.client;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.da.test.ArquillianDeploymentFactory;
import org.jboss.da.test.ArquillianDeploymentFactory.DepType;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;

public abstract class AbstractRestBCTest extends AbstractRestApiTest {

    @Deployment
    public static EnterpriseArchive createDeployment() {
        return new ArquillianDeploymentFactory().createDeployment(DepType.BC);
    }

    @Override
    protected String getContextRoot() {
        return "testsuite-bc";
    }
}
