/*   CS580 HW   */
#include    "stdafx.h"  
#include        "Gz.h"
#include        "disp.h"

int GzNewFrameBuffer(char** framebuffer, int width, int height)
{
/* create a framebuffer:
 -- allocate memory for framebuffer : 3 x (sizeof)char x width x height
 -- pass back pointer 
*/
        if( !framebuffer )
                return GZ_FAILURE;

        // allocate 3 chars per pixel to allot for R, G, and B
        *framebuffer = ( char * )calloc( 3 * width * height, sizeof( char ) );

        return GZ_SUCCESS;
}

int GzNewDisplay(GzDisplay      **display, GzDisplayClass dispClass, int xRes, int yRes)
{

/* create a display:
  -- allocate memory for indicated class and resolution
  -- pass back pointer to GzDisplay object in display
*/
        // check for a bad incoming pointer; don't want to deref it & cause seg fault
        if( !display )
                return GZ_FAILURE;

        // make sure xRes and yRes are valid
        if( xRes < 0 || yRes < 0 || xRes > MAXXRES || yRes > MAXYRES )
                return GZ_FAILURE;

        GzDisplay * newdisp = new GzDisplay;

        newdisp->xres = xRes;
        newdisp->yres = yRes;
        newdisp->fbuf = ( GzPixel * )calloc( xRes * yRes, sizeof( GzPixel ) );
        newdisp->open = 1; // non-zero value indicates the display is open
        newdisp->dispClass = GZ_RGBAZ_DISPLAY;
        
        *display = newdisp;

        return GZ_SUCCESS;
}


int GzFreeDisplay(GzDisplay     *display)
{
/* clean up, free memory */

        // check for bad pointer
        if( !display )
                return GZ_FAILURE;

        free( display->fbuf );
        display->fbuf = NULL;

        free( display );
        display = NULL;

        return GZ_SUCCESS;
}


int GzGetDisplayParams(GzDisplay *display, int *xRes, int *yRes, GzDisplayClass *dispClass)
{
/* pass back values for an open display */

        // check for bad pointers
        if( !display || !xRes || !yRes || !dispClass )
                return GZ_FAILURE;

        *xRes = display->xres;
        *yRes = display->yres;
        *dispClass = display->dispClass;

        return GZ_SUCCESS;
}


int GzInitDisplay(GzDisplay     *display)
{
/* set everything to some default values - start a new frame */

        // check for bad pointer
        if( !display )
                return GZ_FAILURE;

        // loop through all GzPixels in the display
        // display rows (Y coords)
        for( int row = 0; row < display->xres; row++ )
        {
                // display columns (X coords)
                for( int col = 0; col < display->yres; col++ )
                {
                        int fb_index = ARRAY( col, row );

                        // use brown color for background
                        display->fbuf[fb_index].red = 50 << 4;
                        display->fbuf[fb_index].green = 200 << 4;
                        display->fbuf[fb_index].blue = 50 << 4;

                        // make the pixel completely opaque
                        display->fbuf[fb_index].alpha = 1;

                        // use maximum Z value so it will be sure to be overwritten later
                        display->fbuf[fb_index].z = INT_MAX;
                }
        }

        return GZ_SUCCESS;
}


int GzPutDisplay(GzDisplay *display, int i, int j, GzIntensity r, GzIntensity g, GzIntensity b, GzIntensity a, GzDepth z)
{
/* write pixel values into the display */
        
        // check for bad pointer
        if( !display )
                return GZ_FAILURE;

        // if i or j is out of bounds, just ignore it
        if( i < 0 || j < 0 || i >= display->xres || j >= display->yres )
                return GZ_SUCCESS;

        // first clamp the RGB values to be within the appropriate range
	    if( r > 4095 )
		    r = 4095;
	    else if( r < 0)
		    r = 0;

	    if( g > 4095 )
		    g = 4095;
	    else if( g < 0)
		    g = 0;

	    if( b > 4095 )
		    b = 4095;
	    else if( b < 0)
		    b = 0;

        // note: i => columns (X coords), j => rows (Y coords)
        int index = ARRAY( i, j );

        display->fbuf[index].red = r;
        display->fbuf[index].green = g;
        display->fbuf[index].blue = b;

        display->fbuf[index].alpha = a;
        display->fbuf[index].z = z;

        return GZ_SUCCESS;
}


int GzGetDisplay(GzDisplay *display, int i, int j, GzIntensity *r, GzIntensity *g, GzIntensity *b, GzIntensity *a, GzDepth *z)
{
        /* pass back pixel value in the display */
        /* check display class to see what vars are valid */

        // check for bad pointers
        if( !display || !r || !g || !b || !a || !z )
                return GZ_FAILURE;

        // make sure i and j are within appropriate bounds
        if( i < 0 || j < 0 || i >= display->xres || j >= display->yres )
                return GZ_FAILURE;

        // note: i => columns (X coords), j => rows (Y coords)
        int index = ARRAY( i, j );

        *r = display->fbuf[index].red;
        *g = display->fbuf[index].green;
        *b = display->fbuf[index].blue;
        *a = display->fbuf[index].alpha;
        *z = display->fbuf[index].z;

        return GZ_SUCCESS;
}


int GzFlushDisplay2File(FILE* outfile, GzDisplay *display)
{

        /* write pixels to ppm file based on display class -- "P6 %d %d 255\r" */

        if( !outfile || ! display )
                return GZ_FAILURE;

        char charbuf[1024];

        // create and write out header
        sprintf_s( charbuf, 1024, "P6 %d %d 255\r", display->xres, display->yres );
        fwrite( charbuf, sizeof( char ), strlen( charbuf ), outfile );

        for( int y = 0; y < display->yres; y++ )
        {
                for( int x = 0; x < display->xres; x++ )
                {
                        // clear the buffer just in case
                        memset( charbuf, 0, sizeof( 1024 ) );

                        int index = ARRAY( x, y );

                        // we want binary representation in the file, so shift 4 bits over and write RGB chars to file
                        sprintf_s( charbuf, 1024, "%c%c%c", 
                                display->fbuf[index].red >> 4, 
                                display->fbuf[index].green >> 4, 
                                display->fbuf[index].blue >> 4 );
                        fwrite( charbuf, sizeof( char ), strlen( charbuf ), outfile );
                }
        }

        return GZ_SUCCESS;
}

int GzFlushDisplay2FrameBuffer(char* framebuffer, GzDisplay *display)
{

        /* write pixels to framebuffer: 
                - Put the pixels into the frame buffer
                - Caution: store the pixel to the frame buffer as the order of blue, green, and red 
                - Not red, green, and blue !!!
        */

        // check for bad pointers
        if( !framebuffer || !display )
                return GZ_FAILURE;

        // display rows (Y coords)
        for( int x = 0; x < display->xres; x++ )
        {
                // display columns (X coords)
                for( int y = 0; y < display->yres; y++ )
                {
                        int index = ARRAY( x, y );

                        char * bindex = &( framebuffer[index * 3] ); // each idx has 3 chars: B, G, & R
                        char * gindex = bindex + sizeof( char );
                        char * rindex = gindex + sizeof( char );

                        // we need to reduce the 12-bit RGB intensity values to fit into 8 bits
                        char red = display->fbuf[index].red >> 4;
                        char green = display->fbuf[index].green >> 4;
                        char blue = display->fbuf[index].blue >> 4;

                        *bindex = blue;
                        *gindex = green;
                        *rindex = red;
                }
        }

        return GZ_SUCCESS;
}