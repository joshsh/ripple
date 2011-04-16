/*
 * $URL: http://svn.fortytwo.net/projects/ripple/trunk/ripple-logo/RippleEffect.java $
 * $Revision: 1054 $
 * $Author: josh $
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Random;

public class RippleEffect
{
	Color [][] matrix;
	int width, height;

	Point2D mid;
	Point2D[] centers;
	double radius, rotateBy, frequencyFactor;
	double rippleRadius, dampenRadius, radiusRest, tanFactor;

	Color[] colors;
	Color backgroundColor;

	Image image;
	Random random;
	long randomSeed;

static double sloshMax = 0;
static double distMin = 100000;
static double distMax = 0;
	private class Slosh
	{
		public Point2D location;
		public double magnitude;
		public Slosh()
		{
			double minMagnitude = 0.5;

			double x = random.nextDouble();
			double y = random.nextDouble();
			location = new Point2D.Double( x, y );

			magnitude = minMagnitude * ( random.nextDouble() * ( 1 - minMagnitude ) );
			if ( random.nextBoolean() )
				magnitude = -magnitude;

System.out.println( "new slosh: " + magnitude + " at " + location );
		}

		public double waveFunc( Point2D p )
		{
			double distance = p.distance( location );
			double d = Math.cos( ( Math.PI / 2.0 ) - ( sloshConstA / ( ( sloshPointiness * distance ) + sloshConstB ) ) );
//			double d = ( 0 == distance ) ? 1 : Math.exp( distance );

// if ( d > sloshMax ){
// sloshMax = d;
// System.out.println( "d = " + d );}

// if ( distance < distMin ){
// distMin = distance;
// System.out.println( "distMin = " + distance );
// System.out.println( "  d = " + d ); }
// 
// if ( distance > distMax ){
// distMax = distance;
// System.out.println( "        distMax = " + distance );
// System.out.println( "          d = " + d ); }

			double result = d * magnitude;
			return result;
		}
	}

	double sloshWeight;
	Slosh[] sloshes;
	int nSloshes;
	double[][] sloshField;
	double sloshConstA, sloshConstB, sloshPointiness;

	double dampen( double distance )
	{
		return Math.cos( ( distance / dampenRadius ) * Math.PI / 2.0 );
	}

	double dampen( Point2D p )
	{
		double min = 100;
		for ( int i = 0; i < 3; i++ )
		{
			double d = p.distance( centers[i] );
			if ( d < min )
				min = d;
		}

		return dampen( min );
	}

	double waveFunc( double distance )
	{
		if ( distance >= rippleRadius )
			return 0;
		else
			return
				dampen( distance ) *
//				Math.cos(
Math.sin(
					frequencyFactor * Math.tan( distance * tanFactor ) );
//				dampen( distance ) *
//				( 1.0 + Math.cos(
//					frequencyFactor * Math.tan( distance * tanFactor ) ) ) / 2.0;
//				* ( 1.0 + Math.cos( frequencyFactor / ( radiusRest - distance ) ) ) / 2.0;
	}

	// Just a debug method.
	void drawCenters()
	{
		for ( int i = 0; i < 3; i++ )
		{
			Point2D p = centers[i];
			int x = (int) ( width * p.getX() );
			int y = (int) ( width * p.getY() );
			matrix[y][x] = Color.RED;
		}
	}

	// Just a debug method.
	void drawSloshCenters()
	{
		for ( int i = 0; i < nSloshes; i++ )
		{
			Point2D p = sloshes[i].location;
			int x = (int) ( width * p.getX() );
			int y = (int) ( width * p.getY() );
			matrix[y][x] = Color.BLACK;
		}
	}

	double intensify( double val, int n )
	{
		for ( int i = 0; i < n; i++ )
			val = Math.sin( val * Math.PI / 2.0 );

		return val;
	}

/*
	double fieldFunc( Point2D point )
	{
		
	}
*/

	void createPoints()
	{
		mid = new Point2D.Double( 0.5, 0.5 );
		centers = new Point2D.Double[3];

		for ( int i = 0; i < 3; i++ )
		{
			double angle = ( rotateBy + i/3.0 ) * 2 * Math.PI;
			double x = mid.getX() + radius * Math.cos( angle );
			double y = mid.getY() + radius * Math.sin( angle );
			centers[i] = new Point2D.Double( x, y );
		}
	}

	void createField_WithColors()
		throws Exception
	{
		matrix = new Color[height][width];

		double r0 = colors[0].getRed() / 255.0;
		double g0 = colors[0].getGreen() / 255.0;
		double b0 = colors[0].getBlue() / 255.0;
		double r1 = colors[1].getRed() / 255.0;
		double g1 = colors[1].getGreen() / 255.0;
		double b1 = colors[1].getBlue() / 255.0;
		double r2 = colors[2].getRed() / 255.0;
		double g2 = colors[2].getGreen() / 255.0;
		double b2 = colors[2].getBlue() / 255.0;

		double rBack = backgroundColor.getRed() / 255.0;
		double gBack = backgroundColor.getGreen() / 255.0;
		double bBack = backgroundColor.getBlue() / 255.0;

		for ( int i = 0; i < height; i++ )
		{
			for ( int j = 0; j < width; j++ )
			{
				Point2D p = new Point2D.Double(
					(double) i / (double) height,
					1.0 - ( (double) j / (double) width ) );

				if ( p.distance( mid ) >= 0.5 )
				{
					matrix[i][j] = backgroundColor;
					continue;
				}

				double dist0 = p.distance( centers[0] );
				double dist1 = p.distance( centers[1] );
				double dist2 = p.distance( centers[2] );

				// Don't draw anything beyond the trefoil border.
				if ( dist0 >= radiusRest && dist1 >= radiusRest && dist2 >= radiusRest )
				{
					matrix[i][j] = backgroundColor;
					continue;
				}

				double val0 = waveFunc( dist0 );
				double val1 = waveFunc( dist1 );
				double val2 = waveFunc( dist2 );

				double slosh = sloshField[i][j];

				double intensity = ( val0 + val1 + val2 + ( slosh * sloshWeight ) ) / ( 3.0 + sloshWeight );
//				double intensity = ( val0 + val1 + val2 ) / 3.0;
//				double intensity = 1;
//				double intensity = slosh / sloshWeight;

				// Fade out at edges of trefoil.
				intensity = intensity * dampen( p );

				if ( intensity < 0 )
					intensity = 0;

intensity = intensify( intensity, 4 );
/*
int v = (int) ( 255 * intensity );
Color result = new Color( v, v, v );
//*/

//*
				if ( val0 < 0 )
					val0 = 0;
				if ( val1 < 0 )
					val1 = 0;
				if ( val2 < 0 )
					val2 = 0;

				double m0_a = val0;
				double m1_a = val1;
				double m2_a = val2;

				double m0_b = dist0 > radiusRest ? 0 : 1.0 - ( dist0 / radiusRest );
				double m1_b = dist1 > radiusRest ? 0 : 1.0 - ( dist1 / radiusRest );
				double m2_b = dist2 > radiusRest ? 0 : 1.0 - ( dist2 / radiusRest );

double r = m0_b;
double g = m1_b;
double b = m2_b;

/*
r = Math.sqrt(r);//r*r;
g = Math.sqrt(g);//g*g;
b = Math.sqrt(b);//b*b;
*/

/*
double brightness = ( r + g + b ) / 3;
double sq = Math.sqrt(brightness);
r /= sq;
g /= sq;
b /= sq;
if ( r > 1 ) r = 1;
if ( g > 1 ) g = 1;
if ( b > 1 ) b = 1;
*/



//*/
/*
		double favor = 500.0;

				double m0 = m0_a + favor*m0_b;
				double m1 = m1_a + favor*m1_b;
				double m2 = m2_a + favor*m2_b;
				double sum = m0 + m1 + m2;

				double w0 = m0 / sum;
				double w1 = m1 / sum;
				double w2 = m2 / sum;

				double r = ( r0*w0 + r1*w1 + r2*w2 ) / 3.0;
				double g = ( g0*w0 + g1*w1 + g2*w2 ) / 3.0;
				double b = ( b0*w0 + b1*w1 + b2*w2 ) / 3.0;
*/

/*
double cmax = r;
if ( g > cmax )
	cmax = g;
if ( b > cmax )
	cmax = b;
r /= cmax;
g /= cmax;
b /= cmax;
*/
/*double dred = r;
double dgreen = g;
double dblue = b;


				int n = 7;
				double dred = intensify( intensity * r, n );
				double dgreen = intensify( intensity * g, n );
				double dblue = intensify( intensity * b, n );
*/
//				double sumRgb = r + g + b;
//				double dred = intensify( intensity * r / sumRgb )
//				double dgreen = intensify( intensity * g / sumRgb )
//				double dblue = intensify( intensity * b / sumRgb )

/*
double sat = dred;
if ( dgreen > sat )
	sat = dgreen;
if ( dblue > sat )
	sat = dblue;

dred = dred * sat + ( rBack * ( 1 - sat ) );
dgreen = dgreen * sat + ( gBack * ( 1 - sat ) );
dblue = dblue * sat + ( bBack * ( 1 - sat ) );
*/

/*
int opaque = (int) ( intensity * 255 );
//System.out.println( "opaque = " + opaque );
				int red = (int) ( dred * 255 );
				int green = (int) ( dgreen * 255 );
				int blue = (int) ( dblue * 255 );

				if ( red > 255 )
					red = 255;
				if ( green > 255 )
					green = 255;
				if ( blue > 255 )
					blue = 255;

				Color result = new Color( red, green, blue, opaque );
*/
Color med = new Color( (float) r, (float) g, (float) b );
//int nBrighten = 5;
int nBrighten = 3;
for ( int k = 0; k < nBrighten; k++ )
	med = med.brighter();

				Color result = new Color( med.getRed(), med.getGreen(), med.getBlue(), (int) (255*intensity) );
//				Color result = new Color( (float) r, (float) g, (float) b, (float) intensity ).brighter();
//*/
				matrix[i][j] = result;
			}
		}
	}





	void createField()
		throws Exception
	{
		matrix = new Color[height][width];

		for ( int i = 0; i < height; i++ )
		{
			for ( int j = 0; j < width; j++ )
			{
				Point2D p = new Point2D.Double(
					(double) i / (double) height,
					1.0 - ( (double) j / (double) width ) );

				if ( p.distance( mid ) >= 0.5 )
				{
					matrix[i][j] = backgroundColor;
					continue;
				}

				double dist0 = p.distance( centers[0] );
				double dist1 = p.distance( centers[1] );
				double dist2 = p.distance( centers[2] );

				// Don't draw anything beyond the trefoil border.
				if ( dist0 >= radiusRest && dist1 >= radiusRest && dist2 >= radiusRest )
				{
					matrix[i][j] = backgroundColor;
					continue;
				}

				double val0 = waveFunc( dist0 );
				double val1 = waveFunc( dist1 );
				double val2 = waveFunc( dist2 );

				double slosh = sloshField[i][j];

				double intensity = ( val0 + val1 + val2 + ( slosh * sloshWeight ) ) / ( 3.0 + sloshWeight );


//				double intensity = ( val0 + val1 + val2 ) / 3.0;
//				double intensity = 1;
//				double intensity = slosh / sloshWeight;

				// Fade out at edges of trefoil.
				intensity = intensity * dampen( p );

				if ( intensity < 0 )
					intensity = 0;

intensity = intensify( intensity, 4 );
//*
int v = (int) ( 255 * intensity );
Color result = new Color( 255, 255, 255, v );
//*/

/*
				if ( val0 < 0 )
					val0 = 0;
				if ( val1 < 0 )
					val1 = 0;
				if ( val2 < 0 )
					val2 = 0;

				double m0_a = val0;
				double m1_a = val1;
				double m2_a = val2;

				double m0_b = dist0 > radiusRest ? 0 : 1.0 - ( dist0 / radiusRest );
				double m1_b = dist1 > radiusRest ? 0 : 1.0 - ( dist1 / radiusRest );
				double m2_b = dist2 > radiusRest ? 0 : 1.0 - ( dist2 / radiusRest );

double r = m0_b;
double g = m1_b;
double b = m2_b;


Color med = new Color( (float) r, (float) g, (float) b );
//int nBrighten = 5;
int nBrighten = 3;
for ( int k = 0; k < nBrighten; k++ )
	med = med.brighter();

				Color result = new Color( med.getRed(), med.getGreen(), med.getBlue(), (int) (255*intensity) );
//				Color result = new Color( (float) r, (float) g, (float) b, (float) intensity ).brighter();
//*/
				matrix[i][j] = result;
			}
		}
	}







	void createSloshes()
	{
		sloshConstB = 2 * sloshConstA / Math.PI;

		sloshes = new Slosh[nSloshes];
		for ( int i = 0; i < nSloshes; i++ )
			sloshes[i] = new Slosh();

		sloshField = new double[height][width];
		double max = 0;
		for ( int i = 0; i < height; i++ )
		{
			for ( int j = 0; j < width; j++ )
			{
				Point2D p = new Point2D.Double( j / ( (double) width ), i / ( (double) height ) );
				double sum = 0;
				for ( int k = 0; k < nSloshes; k++ )
					sum += sloshes[k].waveFunc( p );
				sloshField[i][j] = sum;
				if ( sum > 0 )
				{
					if ( sum > max )
						max = sum;
				}

				else if ( sum < 0 )
				{
					if ( -sum > max )
						max = -sum;
				}
			}
		}
System.out.println( "max = " + max );

		// Put the extremum at exactly 1 or -1.
		for ( int i = 0; i < height; i++ )
			for ( int j = 0; j < width; j++ )
				sloshField[i][j] = sloshField[i][j] / max;
	}

	void createImage()
		throws Exception
	{
		int []bytes = new int[width * height];
		int k = 0;
		for ( int i = 0; i < height; i++ )
		{
			for ( int j = 0; j < width; j++ )
			{
				Color c = matrix[i][j];
				bytes[k++] = (c.getAlpha() << 24)
					|  (c.getRed() << 16)
					|  (c.getGreen() << 8 )
					| c.getBlue();
			}
		}

		int off = 0;
		int scan = width;

		image = Toolkit.getDefaultToolkit().createImage(
			new MemoryImageSource( width, height, bytes, off, scan ) );
	}

	void init()
		throws Exception
	{
		boolean wide = true;

		if ( wide )
		{
			radius = 0.06;
			rotateBy = 1/2.0 + 1/20.0;
			frequencyFactor = 20.0;
			backgroundColor = Color.BLUE;
		}

		else
		{
			radius = 0.15;
			rotateBy = 1/2.0 + 1/20.0;
			frequencyFactor = 9.0;
			backgroundColor = Color.WHITE;
		}

		radiusRest = 0.5 - radius;
		dampenRadius = radiusRest;
//		rippleRadius = radiusRest;
rippleRadius = radiusRest * 1.2;
		tanFactor = Math.PI / ( rippleRadius * 2 );

		width = 1000;
		height = 1000;

		nSloshes = 40;
		sloshWeight = 2.0;
		sloshConstA = 1.0;
		sloshPointiness = 20.0;
randomSeed = 8212;
//		randomSeed = 2750;
//		randomSeed = ( new Random() ).nextInt( 10000 );

		colors = new Color[3];
		colors[0] = Color.RED;
		colors[1] = Color.GREEN;
		colors[2] = Color.BLUE;


		random = new Random( randomSeed );

		createPoints();
		createSloshes();
		createField();
//drawSloshCenters();
//drawCenters();
		createImage();

		System.out.println( "randomSeed = " + randomSeed );
	}

	public void show()
		throws Exception
	{
		ImagePanel panel = new ImagePanel( image );
		JFrame f = new JFrame();
f.setBackground( backgroundColor );
		f.getContentPane().add( panel );
		
		int width = panel.img.getWidth( null );
		int height = panel.img.getHeight( null );

		//show frame
		f.setBounds( 0, 0, width, height );
		f.setVisible( true );
	}

	//panel used to draw image on
	private class ImagePanel extends JPanel
	{
		Image img;
		
		public ImagePanel( final Image img )
		{
			this.img = img;
//setBackground( Color.WHITE );
		}
		
		//override paint method of panel
		public void paint( Graphics g )
		{
//setBackground( Color.WHITE );
			g.drawImage( img, 0, 0, this );
		}
	}

	public static void main( String [] args )
	{
		try
		{
			RippleEffect effect = new RippleEffect();
			effect.init();
			effect.show();
		}

		catch ( Exception e )
		{
			System.out.println( "Error: " + e.toString() );
		}
	}
}

// kate: tab-width 4
