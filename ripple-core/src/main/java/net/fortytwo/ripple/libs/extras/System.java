/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-core/src/main/java/net/fortytwo/ripple/libs/etc/Get.java $
 * $Revision: 96 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * A primitive which enables system calls to the underlying operating system.
 */
public class System extends PrimitiveStackMapping {
    private static final int
            OUTPUT_MAXLEN = 100,
            ERROR_MAXLEN = 100;

    public String[] getIdentifiers() {
        return new String[]{
                ExtrasLibrary.NS_2011_04 + "system",
                ExtrasLibrary.NS_2008_08 + "system"};
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("command", "a system command to execute", true)};
    }

    public String getComment() {
        return "makes a system call to the underlying operating system";
    }

    public System()
            throws RippleException {
        super();
    }

    public void apply(final StackContext arg,
                      final Sink<StackContext, RippleException> solutions)
            throws RippleException {
        final ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        String command = mc.toString(stack.getFirst());
        stack = stack.getRest();

        int exitCode;
        String normalOutput;
        String errorOutput;

        Runtime r = Runtime.getRuntime();

        try {
            Process p = r.exec(command);
            exitCode = p.waitFor();
            normalOutput = readInputStream(p.getInputStream(), OUTPUT_MAXLEN);
            errorOutput = readInputStream(p.getErrorStream(), OUTPUT_MAXLEN);
        } catch (IOException e) {
            throw new RippleException(e);
        } catch (InterruptedException e) {
            throw new RippleException(e);
        }

        solutions.put(arg.with(
                stack.push(mc.numericValue(exitCode))
                        .push(mc.plainValue(normalOutput))
                        .push(mc.plainValue(errorOutput))));
    }

    private String readInputStream(final InputStream is,
                                   final int maxLen) throws RippleException {
        try {
            int len = is.available();
            if (len > maxLen) {
                len = maxLen;
            }

            byte[] data = new byte[len];
            is.read(data);

            return new String(data);
        } catch (IOException e) {
            throw new RippleException(e);
        }
    }
}
