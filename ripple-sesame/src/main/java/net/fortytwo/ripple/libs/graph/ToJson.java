/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/graph/ToUri.java $
 * $Revision: 134 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.json.JSONValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A primitive which consumes a literal value and produces the resource
 * identified by the corresponding URI (if any).
 */
public class ToJson extends PrimitiveStackMapping {
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2010_08 + "toJson"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public ToJson()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("s", null, true)};
    }

    public String getComment() {
        return "s  =>  json -- where json is the JSON object whose string representation is s";
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        String s;

        s = mc.toString(stack.getFirst());
        stack = stack.getRest();

        RippleValue json = toJson(s, mc);

        solutions.put(arg.with(
                stack.push(json)));
    }

    private RippleValue toJson(final String s,
                               final ModelConnection mc) throws RippleException {
        String st = s.trim();
        try {
            return st.startsWith("[")
                    ? JSONValue.toRippleValue(new JSONArray(st), mc)
                    : JSONValue.toRippleValue(new JSONObject(st), mc);
        } catch (JSONException e) {
            throw new RippleException(e);
        }
    }
}