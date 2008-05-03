/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.neo4j;

import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RdfValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;

/**
 * Author: josh
 * Date: Mar 13, 2008
 * Time: 12:03:26 PM
 */
public class Neo4jList extends RippleList {
    public RippleList push(RippleValue v) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNil() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RdfValue toRDF(ModelConnection mc) throws RippleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isActive() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void printTo(RipplePrintStream p) throws RippleException {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public int compareTo(RippleValue rippleValue) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RippleList invert() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
