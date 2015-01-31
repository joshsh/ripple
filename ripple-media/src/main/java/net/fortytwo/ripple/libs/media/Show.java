package net.fortytwo.ripple.libs.media;

import net.fortytwo.flow.Sink;
import net.fortytwo.flow.rdf.HTTPUtils;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.openrdf.model.Value;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Show extends PrimitiveStackMapping {
    private static final Logger logger = Logger.getLogger(Show.class.getName());

    private static final String[] MIME_TYPES = ImageIO.getReaderMIMETypes();

    private static final String[] IDENTIFIERS = {
            MediaLibrary.NS_2013_03 + "show",
            MediaLibrary.NS_2008_08 + "show",
            MediaLibrary.NS_2007_08 + "show"};

    public String[] getIdentifiers() {
        return IDENTIFIERS;
    }

    public Show()
            throws RippleException {
        super();
    }

    public Parameter[] getParameters() {
        return new Parameter[]{
                new Parameter("imgUri", null, true)};
    }

    public String getComment() {
        return "imgUri => imgUri  -- has the side-effect of displaying the image at URI imgUri";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Value uri = mc.valueOf(URI.create(mc.toString(arg.getFirst())));
        //stack = stack.getRest();

        ImagePanel panel;

        try {
            panel = new ImagePanel(uri);

            JFrame f = new JFrame();

            f.getContentPane().add(panel);

            int width = panel.img.getWidth(null);
            int height = panel.img.getHeight(null);

            //show frame
            f.setBounds(0, 0, width, height);
            f.setVisible(true);
        } catch (RippleException e) {
            logger.log(Level.WARNING, "error while showing graphics", e);
        }

        // Pass the stack along, unaltered.
        solutions.put(arg);
    }

    //panel used to draw image on
    private class ImagePanel extends JPanel {
        private static final long serialVersionUID = -1290238560297598746L;

        //image object
        public Image img;

        public ImagePanel(final Value uri) throws RippleException {
            HttpGet method = HTTPUtils.createRdfGetMethod(uri.toString());
            HTTPUtils.setAcceptHeader(method, MIME_TYPES);
            HTTPUtils.throttleHttpRequest(method);
            HttpClient client = HTTPUtils.createClient(true);

            InputStream is;

            try {
                HttpResponse response = client.execute(method);
                is = response.getEntity().getContent();
            } catch (IOException e) {
                throw new RippleException(e);
            }

            try {
                //load image
                img = ImageIO.read(is);
            } catch (IOException e) {
                throw new RippleException(e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RippleException(e);
                }
            }

            if (null == img) {
                throw new RippleException("no displayable image found at URI " + uri);
            }
        }

        //override paint method of panel
        public void paint(final Graphics g) {
            //draw the image
            if (img != null) {
                g.drawImage(img, 0, 0, this);
            }
        }
    }
}
