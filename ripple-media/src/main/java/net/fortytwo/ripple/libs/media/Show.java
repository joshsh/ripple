/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.media;

import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.util.HTTPUtils;

import org.openrdf.model.URI;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpClient;

public class Show extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

	private static final String[] MIME_TYPES = ImageIO.getReaderMIMETypes();

    private static final String[] IDENTIFIERS = {
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

	public int arity()
	{
		return ARITY;
	}

    public void apply( final StackContext arg,
                         final Sink<StackContext, RippleException> solutions )
            throws RippleException
	{
        ModelConnection mc = arg.getModelConnection();
        RippleList stack = arg.getStack();

        URI uri = mc.createURI( mc.toString( stack.getFirst() ) );
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
		solutions.put( arg.with( stack ) );
	}

	//panel used to draw image on
	private class ImagePanel extends JPanel
	{
		private static final long serialVersionUID = -1290238560297598746L;
		
		//image object
		public Image img;
		
		public ImagePanel( final URI uri ) throws RippleException
		{
            HttpMethod method = HTTPUtils.createRdfGetMethod( uri.toString() );
            HTTPUtils.setAcceptHeader( method, MIME_TYPES);
            HTTPUtils.registerMethod( method );
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
