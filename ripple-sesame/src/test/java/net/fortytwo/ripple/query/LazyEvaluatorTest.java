/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.libs.math.Abs;
import net.fortytwo.ripple.libs.math.Sqrt;
import net.fortytwo.ripple.libs.stack.Dup;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.RippleException;

public class LazyEvaluatorTest extends RippleTestCase
{
    public void testSimple() throws Exception
    {
        ModelConnection mc = getTestModel().getConnection( null );
        Evaluator eval = new LazyStackEvaluator();
        Collector<RippleList, RippleException> expected = new Collector<RippleList, RippleException>();
        final Collector<RippleList, RippleException> actual = new Collector<RippleList, RippleException>();
        RippleList input;

        Sink<StackContext, RippleException> actualSink = new Sink<StackContext, RippleException>() {

            public void put( final StackContext arg ) throws RippleException
            {
                actual.put( arg.getStack() );
            }
        };

        StackContext arg = new StackContext( mc.list(), mc );

        RippleValue op = Operator.OP;
        RippleValue
            dup = new Dup(),
            sqrt = new Sqrt(),
            abs = new Abs();
        RippleValue
            minusone = mc.value( -1.0 ),
            one = mc.value( 1 ),
            two = mc.value( 2 );

        // passive stack passes through unchanged
        // (1 2) -> (1 2)
        input = createStack( mc, one, two );
        expected.clear();
        expected.put( createStack( mc, one, two ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // replacement rules are applied at the head of the stack
        // (1 /dup) -> (1 1)
        input = createStack( mc, one, dup, op );
        expected.clear();
        expected.put( createStack( mc, one, one ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // evaluation is recursive
        // (1 /dup /dup) -> (1 1 1)
        input = createStack( mc, one, dup, op, dup, op );
        expected.clear();
        expected.put( createStack( mc, one, one, one ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // evaluator drops anything which can't be reduced to head-normal form
        // (/dup) ->
        input = createStack( mc, dup, op );
        expected.clear();
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // evaluator drops the nil list
        // () ->
        input = mc.list();
        expected.clear();
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // distributive reduction
        // (1 /sqrt /dup) -> (1 1), (-1, -1)
        input = createStack( mc, one, sqrt, op, dup, op );
        expected.clear();
        expected.put( createStack( mc, one, one ) );
        expected.put( createStack( mc, minusone, minusone ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // no eager reduction
        // (2 /dup 1) -> (2 /dup 1)
        input = createStack( mc, two, dup, op, one );
        expected.clear();
        expected.put( createStack( mc, two, dup, op, one ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // lists are opaque
        // ((2 /dup)) -> ((2 /dup))
        input = createStack( mc, createQueue( mc, two, dup, op ) );
        expected.clear();
        expected.put( createStack( mc, createQueue( mc, two, dup, op ) ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // list dequotation
        // (2 /(1 /dup)) -> (2 1 1)
        input = createStack( mc, two, createQueue( mc, one, dup, op ), op );
        expected.clear();
        expected.put( createStack( mc, two, one, one ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        // results are not necessarily a set
        // (1 /sqrt /abs)
        input = createStack( mc, one, sqrt, op, abs, op );
        expected.clear();
        expected.put( createStack( mc, one ) );
        expected.put( createStack( mc, one ) );
        actual.clear();
        eval.apply( arg.with( input ), actualSink );
        assertCollectorsEqual( expected, actual );

        mc.close();
    }
}
