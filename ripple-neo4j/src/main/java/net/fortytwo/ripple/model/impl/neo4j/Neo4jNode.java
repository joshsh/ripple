/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import org.neo4j.api.core.Node;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:07:50 PM
 */
public class Neo4jNode implements RippleValue {
    private Node node;

    public Node getNode() {
        return node;
    }
    
    public RDFValue toRDF(final ModelConnection mc) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isActive() {
        return false;
    }

    public void printTo(final RipplePrintStream p) throws RippleException {
        p.print(node.toString());
    }

    public int compareTo(final RippleValue other) {
        if (other instanceof Neo4jNode) {
            long hc = this.node.hashCode();
            long otherHc = ((Neo4jNode) other).node.hashCode();
            // HACK -- because Neo4j does not implement Comparable<Node>
            return (hc < otherHc) ? -1 : hc > otherHc ? 1 : 0;
        } else {
            return Neo4jNode.class.getName().compareTo(other.getClass().getName());
        }
    }
    
    public boolean equals( final Object other )
    {
        return ( other instanceof Neo4jNode )
                ? ( (Neo4jNode) other ).node.equals( node )
                : false;
    }

    public int hashCode()
    {
        return 298232837 + node.hashCode();
    }
}
