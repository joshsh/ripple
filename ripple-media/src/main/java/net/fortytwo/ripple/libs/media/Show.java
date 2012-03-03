/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.media;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Show extends PrimitiveStackMapping
{
	private static final String[] MIME_TYPES = ImageIO.getReaderMIMETypes();

    private static final String[] IDENTIFIERS = {
            MediaLibrary.NS_2011_08 + "show",
            MediaLibrary.NS_2008_08 + "show",
            MediaLibrary.NS_2007_08 + "show"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Show()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "imgUri", null, true )};
    }

    public String getComment()
    {
        return "imgUri => imgUri  -- has the side-effect of displaying the image at URI imgUri";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RDFValue uri = mc.uriValue(mc.toString(arg.getFirst()));
		//stack = stack.getRest();

		ImagePanel panel;

		try
		{
			panel = new ImagePanel( uri );

			JFrame f = new JFrame();
	
			f.getContentPane().add( panel );
			
			int width = panel.img.getWidth( null );
			int height = panel.img.getHeight( null );
	
			//show frame
			f.setBounds( 0, 0, width, height );
			f.setVisible( true );
		}

		catch ( RippleException e )
		{
			e.logError();
		}

		// Pass the stack along, unaltered.
		solutions.put( arg );
	}

	//panel used to draw image on
	private class ImagePanel extends JPanel
	{
		private static final long serialVersionUID = -1290238560297598746L;
		
		//image object
		public Image img;
		
		public ImagePanel( final RDFValue uri ) throws RippleException
		{
            HttpMethod method = HTTPUtils.createRdfGetMethod( uri.toString() );
            HTTPUtils.setAcceptHeader( method, MIME_TYPES);
            HTTPUtils.throttleHttpRequest(method);
            HttpClient client = HTTPUtils.createClient();

            InputStream is;

            try {
                client.executeMethod( method );
                is = method.getResponseBodyAsStream();
            } catch ( IOException e ) {
                throw new RippleException( e );
            }

            try {
                //load image
                img = ImageIO.read( is );
            } catch ( IOException e ) {
                throw new RippleException( e );
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RippleException( e );
                }
            }

			if ( null == img )
            {
                throw new RippleException( "no displayable image found at URI " + uri );
            }
        }
		
		//override paint method of panel
		public void paint( final Graphics g )
		{
			//draw the image
			if( img != null )
			{
				g.drawImage( img, 0, 0, this );
			}
		}
	}
}
