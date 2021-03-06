package org.jboss.da.bc;

import static org.jboss.da.common.Constants.REST_API_VERSION;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest/v-" + REST_API_VERSION)
public class BCRestActivator extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addSwaggerResources(resources);
        addProjectResources(resources);
        return resources;
    }

    /**
     * Swagger classes required to generate the API JSON generation
     * @param resources
     */
    public void addSwaggerResources(Set<Class<?>> resources) {
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);
    }

    /**
     * Add all JAX-RS classes here to get activated!
     * @param resources
     */
    public void addProjectResources(Set<Class<?>> resources) {
        resources.add(BuildConfigurationsProduct.class);
    }
}
