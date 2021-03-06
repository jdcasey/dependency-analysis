package org.jboss.da.listings.impl.dao;

import javax.ejb.Stateless;

import org.jboss.da.listings.api.dao.BlackArtifactDAO;
import org.jboss.da.listings.api.model.BlackArtifact;

/**
 * 
 * @author Jozef Mrazek <jmrazek@redhat.com>
 *
 */
@Stateless
public class BlackArtifactDAOImpl extends ArtifactDAOImpl<BlackArtifact> implements
        BlackArtifactDAO {

    public BlackArtifactDAOImpl() {
        super(BlackArtifact.class);
    }
}
