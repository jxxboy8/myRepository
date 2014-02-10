/* CS580 Homework 3 */

#include        "stdafx.h"
#include        "stdio.h"
#include        "math.h"
#include        "Gz.h"
#include        "rend.h"

#define PI 3.14159265
#define D_TO_R( degrees ) ( degrees * PI / 180 )

// to help with matrix transform stack
#define STACK_IS_EMPTY -1
#define XFORM_P_TO_S 0
#define XFORM_I_TO_P 1

enum EdgeColoring
{
        YES, // for left & top edges
        NO
};

typedef struct
{
	    GzCoord vertex;
		int origIdx;
}VertexArray;

typedef struct
{
        VertexArray tail;
        VertexArray head;
        EdgeColoring coloring;
} Edge;

/*** HELPER FUNCTION DECLARATIONS ***/
// from HW2
int sortByX( const void * va1, const void * va2 ); // helper function for getting bounding box around tri (used with qsort)
int sortForCCW( const void * va1, const void * va2 ); // helper function for sorting tri verts (used with qsort)
bool orderVertsCCW( Edge edges[3], GzCoord * vertices ); 
bool getBoundary( float * X_min, float * X_max, float * Y_min, float * Y_max, GzCoord * vertices );
bool getLineCoeff( float * A, float * B, float * C, Edge edge );
bool getCP( GzCoord CP, const Edge edge1, const Edge edge2 );
bool LEE( GzRender * render, GzCoord * screenSpaceVerts, GzCoord * imageSpaceVerts, GzCoord * imageSpaceNormals, GzTextureIndex * textureCoords);
// from HW3

bool clearStack( GzRender * render );
bool matMultiply( GzMatrix matProduct, const GzMatrix mat1, const GzMatrix mat2 );
bool homoMatVectorMultiply( GzCoord result, const GzMatrix mat, const GzCoord vector );
float vectorDot( const GzCoord vector1, const GzCoord vector2 );
bool vectorCP( const GzCoord vector1, const GzCoord vector2, GzCoord CP );
bool vectorSub( const GzCoord vector1, const GzCoord vector2, GzCoord diff ); 
bool vectorScale( const GzCoord origVector, float factor, GzCoord result );
bool negateVector( const GzCoord origVector, GzCoord negVector );
bool normalize( GzCoord vector );
bool boundaryCheck( GzRender * render, GzCoord * vertices );
bool getCameraXPara( GzRender * render );
bool getXsp( GzRender * render );

bool getColor(GzRender * render, const GzCoord imageSpaceVert, const GzCoord imageSpaceNormal, const GzTextureIndex textureCoords, GzColor colorvalue);
bool vectorSum(const GzCoord vec1, const GzCoord vec2, GzCoord sum);
bool vectorMultiply(const GzCoord vec1, const GzCoord vec2, GzCoord product);

float getPlaneD(float planeA, float planeB, float planeC, float pixel_x, float pixel_y, float paramToInterp);
float interpPlaneCoeffs( float planeA, float planeB, float planeC, float planeD, float pixel_x, float pixel_y);
float imgSpaceParamToPerspSpace( float screenSpaceInterpZ, float param );
float perspSpaceParamToImgSpace( float screenSpaceInterpZ, float param );
float getVPrimeZ( float screenSpaceInterpZ ); 
// from Professor
short ctoi( float color );
/*** END HELPER FUNCTION DECLARATIONS ***/

int GzRotXMat(float degree, GzMatrix mat)
{
// Create rotate matrix : rotate along x axis
// Pass back the matrix using mat value
/*
 * X-Rotation matrix format:
 * 
 *      1               0                       0                       0       
 *      0               cos(theta)      -sin(theta)     0
 *      0               sin(theta)      cos(theta)      0
 *      0               0                       0                       1
 */

        float radians = ( float )D_TO_R( degree );

        // row 0
        mat[0][0] = 1;
        mat[0][1] = 0;
        mat[0][2] = 0;
        mat[0][3] = 0;

        // row 1
        mat[1][0] = 0;
        mat[1][1] = cos( radians );
        mat[1][2] = -sin( radians );
        mat[1][3] = 0;

        // row 2
        mat[2][0] = 0;
        mat[2][1] = sin( radians );
        mat[2][2] = cos( radians );
        mat[2][3] = 0;

        // row 3
        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 0;
        mat[3][3] = 1;

        return GZ_SUCCESS;
}


int GzRotYMat(float degree, GzMatrix mat)
{
// Create rotate matrix : rotate along y axis
// Pass back the matrix using mat value
/*
 * Y-Rotation matrix format:
 * 
 *      cos(theta)      0                       sin(theta)      0       
 *      0                       1                       0                       0
 *      -sin(theta)     0                       cos(theta)      0
 *      0                       0                       0                       1
 */

        float radians = ( float )D_TO_R( degree );

        // row 0
        mat[0][0] = cos( radians );
        mat[0][1] = 0;
        mat[0][2] = sin( radians );
        mat[0][3] = 0;

        // row 1
        mat[1][0] = 0;
        mat[1][1] = 1;
        mat[1][2] = 0;
        mat[1][3] = 0;

        // row 2
        mat[2][0] = -sin( radians );
        mat[2][1] = 0;
        mat[2][2] = cos( radians );
        mat[2][3] = 0;

        // row 3
        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 0;
        mat[3][3] = 1;

        return GZ_SUCCESS;
}


int GzRotZMat(float degree, GzMatrix mat)
{
// Create rotate matrix : rotate along z axis
// Pass back the matrix using mat value
/*
 * Z-Rotation matrix format:
 * 
 *      cos(theta)      -sin(theta)     0               0                       
 *      sin(theta)      cos(theta)      0               0
 *      0                       0                       1               0
 *      0                       0                       0               1       
 */
        
        float radians = ( float )D_TO_R( degree );

        // row 0
        mat[0][0] = cos( radians );
        mat[0][1] = -sin( radians );
        mat[0][2] = 0;
        mat[0][3] = 0;

        // row 1
        mat[1][0] = sin( radians );
        mat[1][1] = cos( radians );
        mat[1][2] = 0;
        mat[1][3] = 0;

        // row 2
        mat[2][0] = 0;
        mat[2][1] = 0;
        mat[2][2] = 1;
        mat[2][3] = 0;

        // row 3
        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 0;
        mat[3][3] = 1;

        return GZ_SUCCESS;
}


int GzTrxMat(GzCoord translate, GzMatrix mat)
{
// Create translation matrix
// Pass back the matrix using mat value
/*
 * Translation matrix format:
 *      1       0       0       Tx
 *      0       1       0       Ty
 *      0       0       1       Tz
 *      0       0       0       1
 */

        // row 0
        mat[0][0] = 1;
        mat[0][1] = 0;
        mat[0][2] = 0;
        mat[0][3] = translate[X];

        // row 1
        mat[1][0] = 0;
        mat[1][1] = 1;
        mat[1][2] = 0;
        mat[1][3] = translate[Y];

        // row 2
        mat[2][0] = 0;
        mat[2][1] = 0;
        mat[2][2] = 1;
        mat[2][3] = translate[Z];

        // row 3
        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 0;
        mat[3][3] = 1;

        return GZ_SUCCESS;
}


int GzScaleMat(GzCoord scale, GzMatrix mat)
{
// Create scaling matrix
// Pass back the matrix using mat value
/*
 * Scaling matrix format:
 *      Sx      0       0       0
 *      0       Sy      0       0
 *      0       0       Sz      0
 *      0       0       0       1
 */

        // row 0
        mat[0][0] = scale[X];
        mat[0][1] = 0;
        mat[0][2] = 0;
        mat[0][3] = 0;

        // row 1
        mat[1][0] = 0;
        mat[1][1] = scale[Y];
        mat[1][2] = 0;
        mat[1][3] = 0;

        // row 2
        mat[2][0] = 0;
        mat[2][1] = 0;
        mat[2][2] = scale[Z];
        mat[2][3] = 0;

        // row 3
        mat[3][0] = 0;
        mat[3][1] = 0;
        mat[3][2] = 0;
        mat[3][3] = 1;

        return GZ_SUCCESS;
}


//----------------------------------------------------------
// Begin main functions

int GzNewRender(GzRender **render, GzRenderClass renderClass, GzDisplay *display)
{
/*  
- malloc a renderer struct 
- keep closed until all inits are done 
- setup Xsp and anything only done once <-- this actually relies on the camera, so wait until GzBeginRender
- span interpolator needs pointer to display 
- check for legal class GZ_Z_BUFFER_RENDER 
- init default camera 
*/ 

        // check for bad pointers
        if( !render || !display )
                return GZ_FAILURE;

        // make sure render class is legal
        if( renderClass != GZ_Z_BUFFER_RENDER )
                return GZ_FAILURE;

        /* MALLOC A RENDERER STRUCT */
        GzRender * newRender = new GzRender;
        // make sure allocation worked
        if( !newRender )
                return GZ_FAILURE;

        newRender->renderClass = renderClass;
        newRender->display = display;
        newRender->open = 0; // zero value indicates that renderer is closed

        
        /* DONE MALLOCING RENDERER STRUCT */

        // init default camera - must be done BEFORE calling constructXsp
        newRender->camera.FOV = DEFAULT_FOV;
        // default lookat is (0,0,0)
        newRender->camera.lookat[X] = newRender->camera.lookat[Y] = newRender->camera.lookat[Z] = 0; 
        // camera position is the image plane origin
        newRender->camera.position[X] = DEFAULT_IM_X;
        newRender->camera.position[Y] = DEFAULT_IM_Y;
        newRender->camera.position[Z] = DEFAULT_IM_Z;
        // default world up is (0,1,0)
        newRender->camera.worldup[X] = newRender->camera.worldup[Z] = 0;
        newRender->camera.worldup[Y] = 1;

        // the rest of the struct members will be initialized in GzBeginRender.
		newRender->ambientlight.color[RED] = 0.0f;
		newRender->ambientlight.color[BLUE] = 0.0f;
		newRender->ambientlight.color[GREEN] = 0.0f;

		newRender->numlights = 0;

		GzColor defaultKa = DEFAULT_AMBIENT, defaultKd = DEFAULT_DIFFUSE, defaultKs = DEFAULT_SPECULAR;
        newRender->Ka[RED] = defaultKa[RED];
        newRender->Ka[GREEN] = defaultKa[GREEN];
        newRender->Ka[BLUE] = defaultKa[BLUE];
        newRender->Kd[RED] = defaultKd[RED];
        newRender->Kd[GREEN] = defaultKd[GREEN];
        newRender->Kd[BLUE] = defaultKd[BLUE];
        newRender->Ks[RED] = defaultKs[RED];
        newRender->Ks[GREEN] = defaultKs[GREEN];
        newRender->Ks[BLUE] = defaultKs[BLUE];

		newRender->spec = DEFAULT_SPEC;

		newRender->interp_mode = GZ_RGB_COLOR;

		newRender->tex_fun = 0;

        *render = newRender;

        return GZ_SUCCESS;
}


int GzFreeRender(GzRender *render)
{
/* 
-free all renderer resources
*/
        // check for bad pointer
        if( !render )
                return GZ_FAILURE;

        // do NOT free the display here. It should be freed elsewhere.
        render->display = NULL;

        free( render );
        render = NULL;

        return GZ_SUCCESS;
}


int GzBeginRender(GzRender *render)
{
/*  
- set up for start of each frame - clear frame buffer 
- compute Xiw and projection xform Xpi from camera definition 
- init Ximage - put Xsp at base of stack, push on Xpi and Xiw 
- now stack contains Xsw and app can push model Xforms if it wants to. 
*/ 
        // check for bad pointer
        if( !render )
                return GZ_FAILURE;      

        // use red for default flat color
        render->flatcolor[RED] = 0; 
        render->flatcolor[GREEN] = 1;
        render->flatcolor[BLUE] = 0;

        // Calculate Xsp, Xpi, and Xiw
        if( !getCameraXPara( render ) )
                return GZ_FAILURE;

        if( !getXsp( render ) )
                return GZ_FAILURE;

        // empty the transform stack
        clearStack( render );

        int status = GZ_SUCCESS;

        // put Xsp at the base of the stack
        status |= GzPushMatrix( render, render->Xsp );

        // push Xpi onto the stack
        status |= GzPushMatrix( render, render->camera.Xpi );

        // push Xiw onto the stack
        status |= GzPushMatrix( render, render->camera.Xiw );

        render->open = 1; // non-zero value indicates that renderer is now open

        return status;
}

int GzPutCamera(GzRender *render, GzCamera *camera)
{
/*
- overwrite renderer camera structure with new camera definition
*/
        if( !render || !camera )
                return GZ_FAILURE;

        memcpy( &( render->camera ), camera, sizeof( GzCamera ) );

        return GZ_SUCCESS;      
}

int GzPushMatrix(GzRender *render, GzMatrix     matrix)
{
/*
- push a matrix onto the Ximage stack
- check for stack overflow
*/
        if( !render || render->matlevel == MATLEVELS )
                return GZ_FAILURE;

        // if the stack is empty, just put this matrix directly onto the stack (no multiplication necessary)
        if( render->matlevel == STACK_IS_EMPTY )
        {
                // copy the matrix into the stack
                memcpy( render->Ximage[render->matlevel + 1], matrix, sizeof( GzMatrix ) );
        }
        else
        {
                // the matrix we push onto the stack should be the top of the stack multiplied by the new transform (on the right)
                GzMatrix xformProduct;
                matMultiply( xformProduct, render->Ximage[render->matlevel], matrix );

                // copy the matrix into the stack
                memcpy( render->Ximage[render->matlevel + 1], xformProduct, sizeof( GzMatrix ) );
        }

        // another matrix has been pushed onto the stack
        render->matlevel++;

		if( render->matlevel == XFORM_P_TO_S || render->matlevel == XFORM_I_TO_P )
        {
                GzMatrix identityMat;

                // row 0
                identityMat[0][0] = 1;
                identityMat[0][1] = 0;
                identityMat[0][2] = 0;
                identityMat[0][3] = 0;
                // row 1
                identityMat[1][0] = 0;
                identityMat[1][1] = 1;
                identityMat[1][2] = 0;
                identityMat[1][3] = 0;
                // row 2
                identityMat[2][0] = 0;
                identityMat[2][1] = 0;
                identityMat[2][2] = 1;
                identityMat[2][3] = 0;
                // row 3
                identityMat[3][0] = 0;
                identityMat[3][1] = 0;
                identityMat[3][2] = 0;
                identityMat[3][3] = 1;

                // copy the identity matrix into the normals transform stack
                memcpy( render->Xnorm[render->matlevel], identityMat, sizeof( GzMatrix ) );
        }
        // this is NOT the Xsp or Xpi matrix, so include it on the normals transform stack. 
        else
        {
                // Note: Transforms and rotations are NOT allowed on the normals transform stack.
                //       It may only contain unitary rotations and uniform scaling.
                //       Thus we must pre-process the matrix being pushed on the stack to make sure it fits these criteria.
                GzMatrix processedMat;

                // row 0 - last column has no translation
                processedMat[0][0] = matrix[0][0];
                processedMat[0][1] = matrix[0][1];
                processedMat[0][2] = matrix[0][2];
                processedMat[0][3] = 0;
                // row 1 - last column has no translation
                processedMat[1][0] = matrix[1][0];
                processedMat[1][1] = matrix[1][1];
                processedMat[1][2] = matrix[1][2];
                processedMat[1][3] = 0;
                // row 2 - last column has no translation
                processedMat[2][0] = matrix[2][0];
                processedMat[2][1] = matrix[2][1];
                processedMat[2][2] = matrix[2][2];
                processedMat[2][3] = 0;
                // row 3 - last column has no translation
                processedMat[3][0] = matrix[3][0];
                processedMat[3][1] = matrix[3][1];
                processedMat[3][2] = matrix[3][2];
                processedMat[3][3] = 1;

                // We must now ensure that the upper 3x3 matrix is a unitary rotation matrix. (i.e. the length of any row or column is 1)
                // Since the only allowed matrices here are rotations and UNIFORM scales, 
                // we can do this by finding the length of any one row or column (they will all be the same)
                // and dividing each component of the upper 3x3 matrix by that length.
                // So choose ANY column or row and we can get K = 1 / sqrt(a^2 + b^2 + c^2)
                float normFactor = 1 / sqrt( matrix[0][0] * matrix[0][0] + matrix[1][0] * matrix[1][0] + matrix[2][0] * matrix[2][0] );
                // loop through upper 3 rows
                for( int row = 0; row < 3; row++ )
                {
                        // loop through upper 3 columns
                        for( int col = 0; col < 3; col++ )
                        {
                                // normalize this component of upper 3x3 matrix
                                processedMat[row][col] *= normFactor;
                        }
                }

                // the matrix we push onto the geometry stack should be the top of the stack multiplied by the new transform (on the right)
                GzMatrix xformProduct;
                matMultiply( xformProduct, render->Xnorm[render->matlevel - 1], processedMat );

                // copy the matrix into the geometry stack
                memcpy( render->Xnorm[render->matlevel], xformProduct, sizeof( GzMatrix ) );
        }

        return GZ_SUCCESS;
}

int GzPopMatrix(GzRender *render)
{
/*
- pop a matrix off the Ximage stack
- check for stack underflow
*/
        if( !render || render->matlevel == STACK_IS_EMPTY )
                return GZ_FAILURE;

        // a matrix hss been popped off of the stack
        render->matlevel--;

        return GZ_SUCCESS;
}


int GzPutAttribute(GzRender     *render, int numAttributes, GzToken     *nameList, 
        GzPointer       *valueList) /* void** valuelist */
{
/*
- set renderer attribute states (e.g.: GZ_RGB_COLOR default color)
- later set shaders, interpolaters, texture maps, and lights
*/
        
        // check for bad pointers
        if( !render || !nameList || !valueList )
                return GZ_FAILURE;

        for( int i = 0; i < numAttributes; i++ )
        {
                switch( nameList[i] )
                {
                case GZ_RGB_COLOR: // set default flat-shader color
                        GzColor * colorPtr;
                        colorPtr = ( static_cast<GzColor *>( valueList[i] ) );
                        // assign the color values in valueList to render variable
                        render->flatcolor[RED] = ( *colorPtr )[RED];
                        render->flatcolor[GREEN] = ( *colorPtr )[GREEN];
                        render->flatcolor[BLUE] = ( *colorPtr )[BLUE];
                        break;
                case GZ_INTERPOLATE: // shader interpolation mode 
                        int * interpModePtr;
                        interpModePtr = ( static_cast<int *>( valueList[i] ) );
                        render->interp_mode = *interpModePtr;
                        break;
                case GZ_DIRECTIONAL_LIGHT: // add a directional light
                        // only add a light if we have room for another one
                        if( render->numlights < MAX_LIGHTS )
                        {
                                GzLight * Light_directional;
                                Light_directional = ( static_cast<GzLight *>( valueList[i] ) );
                                // copy light color
                                memcpy( render->lights[render->numlights].color, Light_directional->color, sizeof( GzColor ) );
                                // copy light direction
                                memcpy( render->lights[render->numlights].direction, Light_directional->direction, sizeof( GzCoord ) );

                                // just to be safe, make sure the light direction is normalized
                                normalize( render->lights[render->numlights].direction );

                                // another light has been added
                                render->numlights++;
                        }
                        break;
                case GZ_AMBIENT_LIGHT: // set ambient light color
                        GzLight * Light_ambient;
                        Light_ambient = ( static_cast<GzLight *>( valueList[i] ) );
                        // copy ambient light color only. ambient light direction is irrelevant
                        memcpy( render->ambientlight.color, Light_ambient->color, sizeof( GzColor ) );
                        break;
                case GZ_AMBIENT_COEFFICIENT: // Ka ambient reflectance coef's
                        GzColor * coeffKa;
                        coeffKa = ( static_cast<GzColor *>( valueList[i] ) );
                        render->Ka[RED] = ( *coeffKa )[RED];
                        render->Ka[GREEN] = ( *coeffKa )[GREEN];
                        render->Ka[BLUE] = ( *coeffKa )[BLUE];
                        break;
                case GZ_DIFFUSE_COEFFICIENT: // Kd diffuse reflectance coef's
                        GzColor * coeffKd;
                        coeffKd = ( static_cast<GzColor *>( valueList[i] ) );
                        render->Kd[RED] = ( *coeffKd )[RED];
                        render->Kd[GREEN] = ( *coeffKd )[GREEN];
                        render->Kd[BLUE] = ( *coeffKd )[BLUE];
                        break;
                case GZ_SPECULAR_COEFFICIENT: // Ks ambient reflectance coef's
                        GzColor * coeffKs;
                        coeffKs = ( static_cast<GzColor *>( valueList[i] ) );
                        render->Ks[RED] = ( *coeffKs )[RED];
                        render->Ks[GREEN] = ( *coeffKs )[GREEN];
                        render->Ks[BLUE] = ( *coeffKs )[BLUE];
                        break; 
                case GZ_DISTRIBUTION_COEFFICIENT: // specular power
                        float * specPower;
                        specPower = ( static_cast<float *>( valueList[i] ) );
                        render->spec = *specPower;
                        break;
				case GZ_TEXTURE_MAP:
					    GzTexture textureFunction;
						textureFunction = ( static_cast<GzTexture>( valueList[i] ) );
						render->tex_fun =textureFunction;
						break;
                }
        }

        return GZ_SUCCESS;
}

int GzPutTriangle(GzRender      *render, int numParts, GzToken *nameList, 
                                  GzPointer     *valueList)
/* numParts : how many names and values */
{
/*  
- pass in a triangle description with tokens and values corresponding to 
      GZ_POSITION:3 vert positions in model space 
- Xform positions of verts  
- Clip - just discard any triangle with verts behind view plane 
       - test for triangles with all three verts off-screen 
- invoke triangle rasterizer  
TRANSLATION:
Similar to GzPutAttribute but check for GZ_POSITION and GZ_NULL_TOKEN in nameList
Takes in a list of triangles and transforms each vertex from model space to screen space.
Then uses the scan-line or LEE method of rasterizing.
Then calls GzPutDisplay() to draw those pixels to the display.
*/ 
        // check for bad pointers
        if( !render || !nameList || !valueList )
                return GZ_FAILURE;

        GzCoord * screenSpaceVerts = ( GzCoord * )malloc( 3 * sizeof( GzCoord ) );

        // allocate space for the three vertices and three normals of this triangle in image space
        GzCoord * imageSpaceVerts = ( GzCoord * )malloc( 3 * sizeof( GzCoord ) );
        GzCoord * imageSpaceNormals = ( GzCoord * )malloc( 3 * sizeof( GzCoord ) );
        
        // prepare for pointers to 3 vertices and 3 normals
        GzCoord * modelSpaceVerts = 0;
        GzCoord * modelSpaceNormals = 0;
		GzTextureIndex * textureCoords = 0;

        for( int i = 0; i < numParts; i++ )
        {
                switch( nameList[i] )
                {
                case GZ_NULL_TOKEN:
                        // do nothing - no values
                        break;
                case GZ_POSITION:
                        modelSpaceVerts = static_cast<GzCoord *>( valueList[i] );
                        
                        bool triBehindViewPlane;
                        triBehindViewPlane = false;
                        
                        for( int vertIdx = 0; vertIdx < 3; vertIdx++ )
                        {
                                // first we need to transform the position into screen space
                                homoMatVectorMultiply( screenSpaceVerts[vertIdx], render->Ximage[render->matlevel], modelSpaceVerts[vertIdx] );

                                // discard entire triangle if this vert is behind the view plane
                                if( screenSpaceVerts[vertIdx][Z] < 0 )
                                {
                                        triBehindViewPlane = true;
                                        break;
                                }
                        }
                        
                        // we don't need to rasterize this triangle if it's behind the view plane or if it's completely outside of the image plane
                        if( triBehindViewPlane || boundaryCheck( render, screenSpaceVerts ) )
                                return GZ_SUCCESS;
                        
                        break; // end case: GZ_POSITION
                case GZ_NORMAL:
                        modelSpaceNormals = static_cast<GzCoord *>( valueList[i] );
                        break; // end case: GZ_NORMAL
                case GZ_TEXTURE_INDEX:
					    textureCoords = static_cast<GzTextureIndex *>(valueList[i]);
                        break; // end case: GZ_TEXTURE_INDEX
                } // end switch( nameList[i] )
        } // end loop over numparts

        // if we didn't read in model space vertices and normals, we had a problem.
        if( !modelSpaceVerts || !modelSpaceNormals )
        {
                AfxMessageBox( "Error: no vertices and/or normals found in GzPutTriangle!!!\n" );
                return GZ_FAILURE;
        }

        // now transform the model space vertices and normals into image space (using normals transformation stack) for rasterization use
        for( int i = 0; i < 3; i++ )
        {
                homoMatVectorMultiply( imageSpaceVerts[i], render->Xnorm[render->matlevel], modelSpaceVerts[i]  );
                homoMatVectorMultiply( imageSpaceNormals[i], render->Xnorm[render->matlevel], modelSpaceNormals[i]  );
        }

		if(!LEE(render, screenSpaceVerts, imageSpaceVerts, imageSpaceNormals, textureCoords))
			return GZ_FAILURE;

		free( screenSpaceVerts );
		screenSpaceVerts = 0;
		free( imageSpaceVerts );
		imageSpaceVerts = 0;
		free( imageSpaceNormals );
		imageSpaceNormals = 0;

        return GZ_SUCCESS;
}

/* NOT part of API - just for general assistance */

short   ctoi(float color)               /* convert float color to GzIntensity short */
{
  return(short)((int)(color * ((1 << 12) - 1)));
}

/*** BEGIN HW2 FUNCTIONS ***/

bool orderVertsCCW( Edge edges[3], GzCoord * vertices )
{
        if( !vertices )
        {
                return false;
        }

		VertexArray arrayVerts[3];
		for( int vertIndex = 0; vertIndex < 3; vertIndex++ ){
		     memcpy(arrayVerts[vertIndex].vertex, vertices[vertIndex], sizeof( GzCoord ));
			 arrayVerts[vertIndex].origIdx = vertIndex;
		}
        // first sort vertices by Y coordinate (low to high)
        qsort( arrayVerts, 3, sizeof( VertexArray ), sortForCCW );

        /*      
         * now the CCW oriented edges are either:
         *    0-1, 1-2, 2-0
         * OR
         *    0-2, 2-1, 1-0
         * depending on which are left and right edges.
         *
         * NOTE: We need to determine left/right & top/bottom edges to establish triangle edge ownership.
         *       We'll use the convention that a triangle owns all left & top edges, but not right & bottom edges.
         *
         * Algorithm to do this:
         * IF NO 2 VERTS HAVE SAME Y COORD:
         * - Find point along edge 0-2 that has the same Y value as vert 1
         * - Compare X value at this point (x') to X value at vert 1 (x1)
         *   x' < x1 => edges with vert 1 must be right edges
         *   x' > x1 => edges with vert 2 must be left edges
         *   x' = x1 shouldn't happen if no 2 verts have the exact same Y coordinate
         * IF ANY 2 VERTS HAVE SAME Y COORD: (similar algorithm, but for top/bottom edges)
         * - We can arbitrarily determine the orientation of the edges and which are colored/uncolored.
         * - Call the verts that make up the horizontal edge v1 & v2, and the other vert v3.
         *   v3.y < v1.y => CCW edges: v1 -> v2 (uncolored), v2 -> v3 (uncolored), v3 -> v1 (colored)
         *   v3.y > v1.y => CCW edges: v2 -> v1 (colored), v1 -> v3 (colored), v3 -> v2 (uncolored)
         */

        // check to see if any two y-coords are equal; since they're ordered by Y first, equal Y coords will be adjacent
		if( arrayVerts[0].vertex[Y] == arrayVerts[1].vertex[Y] ) // 2 Y coords are equal
        {
                // since verts are sorted by Y first, we know that 3rd vert has a greater Y than the other 2
                edges[0].tail = arrayVerts[1];
                edges[0].head = arrayVerts[0];
                edges[0].coloring = YES;

                edges[1].tail = arrayVerts[0];
                edges[1].head = arrayVerts[2];
                edges[1].coloring = YES;

                edges[2].tail = arrayVerts[2];
                edges[2].head = arrayVerts[1];
                edges[2].coloring = NO;
        }
        else if( arrayVerts[1].vertex[Y] == arrayVerts[2].vertex[Y] ) // 2 Y coords are equal
        {
                // since verts are sorted by Y first, we know that the 1st vert has a smaller Y than the other 2
                edges[0].tail = arrayVerts[0];
                edges[0].head = arrayVerts[1];
                edges[0].coloring = YES;

                edges[1].tail = arrayVerts[1];
                edges[1].head = arrayVerts[2];
                edges[1].coloring = NO;

                edges[2].tail = arrayVerts[2];
                edges[2].head = arrayVerts[0];
                edges[2].coloring = NO;
        }
        else // all 3 Y coords are distinct
        {
                // formulate line equation for v0 - v2
			    float slope = ( arrayVerts[2].vertex[Y] - arrayVerts[0].vertex[Y] ) / ( arrayVerts[2].vertex[X] - arrayVerts[0].vertex[X] );
                // use slope to find point along v0 - v2 line with same y coord as v1's y coord
                // y - y1 = m( x - x1 ) => x = ( ( y - y1 )/m ) + x1. We'll use vert0 for x1 and y1.
                float midpointX = ( ( arrayVerts[1].vertex[Y] - arrayVerts[0].vertex[Y] ) / slope ) + arrayVerts[0].vertex[X];

                // midpointX is less than v1's x, so all edges touching v1 are uncolored
                if( midpointX < arrayVerts[1].vertex[X] )
                {
                        edges[0].tail = arrayVerts[0];
                        edges[0].head = arrayVerts[2];
                        edges[0].coloring = YES;

                        edges[1].tail = arrayVerts[2];
                        edges[1].head = arrayVerts[1];
                        edges[1].coloring = NO;

                        edges[2].tail = arrayVerts[1];
                        edges[2].head = arrayVerts[0];
                        edges[2].coloring = NO;
                }
                // midpoint X is greater than v1's x, so all edges touching v1 are colored
                else if( midpointX > arrayVerts[1].vertex[X] )
                {
                        edges[0].tail = arrayVerts[0];
                        edges[0].head = arrayVerts[1];
                        edges[0].coloring = YES;

                        edges[1].tail = arrayVerts[1];
                        edges[1].head = arrayVerts[2];
                        edges[1].coloring = YES;

                        edges[2].tail = arrayVerts[2];
                        edges[2].head = arrayVerts[0];
                        edges[2].coloring = NO;
                }
                // they are exactly equal. this shouldn't happen.
                else
                {
                        return false;
                }
        }

        return true;
}

int sortForCCW( const void * va1, const void * va2 )
{
        VertexArray * arrayVerts1 = ( VertexArray * )va1;
        VertexArray * arrayVerts2 = ( VertexArray * )va2;

        // find the difference in Y coordinate values
		float Y_Diff = arrayVerts1->vertex[Y] - arrayVerts2->vertex[Y];

        // need to return an int, so just categorize by sign
        if( Y_Diff < 0 )
                return -1;
        else if( Y_Diff > 0 )
                return 1;
        else
        {
                 return sortByX(va1, va2);
        }
}

int sortByX( const void * va1, const void * va2 )
{
        VertexArray * arrayVerts1 = ( VertexArray * )va1;
        VertexArray * arrayVerts2 = ( VertexArray * )va2;

		float X_Diff = arrayVerts1->vertex[X] - arrayVerts2->vertex[X];
        if( X_Diff < 0 )
                return -1;
        else if( X_Diff > 0 )
                return 1;
        else
                return 0;
}

bool getBoundary( float * X_min, float * X_max, float * Y_min, float * Y_max, GzCoord * vertices )
{
        if( !X_min || !X_max || !Y_min || !Y_max || !vertices )
        {
                return false;
        }

		VertexArray arrayVerts[3];
		for( int vertIndex = 0; vertIndex < 3; vertIndex++ ){
		     memcpy(arrayVerts[vertIndex].vertex, vertices[vertIndex], sizeof( GzCoord ));
			 arrayVerts[vertIndex].origIdx = vertIndex;
		}

        qsort( arrayVerts, 3, sizeof( VertexArray ), sortByX );
		*X_min = arrayVerts[0].vertex[X];
		*X_max = arrayVerts[2].vertex[X];

        qsort( arrayVerts, 3, sizeof( VertexArray ), sortForCCW );
		*Y_min = arrayVerts[0].vertex[Y];
		*Y_max = arrayVerts[2].vertex[Y];

        return true;
}

bool getLineCoeff( float * A, float * B, float * C, Edge edge )
{
        if( !A || !B || !C )
        {
                return false;
        }

        // recall: start point of vector is the tail, end point of vector is the head

        /*
         * Algorithm:
         * - Define tail as (X,Y) and head as (X + dX, Y + dY).
         * - Edge Equation:  E(x,y) = dY (x-X) - dX (y-Y)
         *                          = 0 for points (x,y) on line
         *                          = + for points in half-plane to right/below line (assuming CCW orientation)
         *                          = - for points in half-plane to left/above line (assuming CCW orientation)
         * - Use above definition and cast into general form of 2D line equation Ax + By + C = 0
         *   dYx - dYX - dXy + dXY = 0    (multiply terms)
         *   dYx + (-dXy) + (dXY - dYX) = 0   (collect terms) 
         *   A = dY
         *   B = -dX
         *   C = dXY  dYX    (Compute A,B,C from edge verts)
         */

		float X_tail = edge.tail.vertex[0];
		float Y_tail = edge.tail.vertex[1];

		float dX = edge.head.vertex[0] - X_tail;
		float dY = edge.head.vertex[1] - Y_tail;

        *A = dY;
        *B = -dX;
        *C = ( dX * Y_tail ) - ( dY * X_tail );

        return true;
}

bool getCP( GzCoord CP, const Edge edge1, const Edge edge2 )
{
        if( !CP )
        {
                return false;
        }

        GzCoord vector1, vector2;

        // define edge1 vector
		vector1[0] = edge1.head.vertex[0] - edge1.tail.vertex[0];
		vector1[1] = edge1.head.vertex[1] - edge1.tail.vertex[1];
		vector1[2] = edge1.head.vertex[2] - edge1.tail.vertex[2];

        // define edge2 vector
		vector2[0] = edge2.head.vertex[0] - edge2.tail.vertex[0];
		vector2[1] = edge2.head.vertex[1] - edge2.tail.vertex[1];
		vector2[2] = edge2.head.vertex[2] - edge2.tail.vertex[2];

        // compute cross product
        CP[X] = vector1[Y] * vector2[Z] - vector1[Z] * vector2[Y];
        CP[Y] = -( vector1[X] * vector2[Z] - vector1[Z] * vector2[X] );
        CP[Z] = vector1[X] * vector2[Y] - vector1[Y] * vector2[X];

        return true;
}

bool LEE( GzRender * render, GzCoord * screenSpaceVerts, GzCoord * imageSpaceVerts, GzCoord * imageSpaceNormals, GzTextureIndex * textureCoords)
{
        // get bounding box around triangle
        float X_min, X_max, Y_min, Y_max;
        if( !getBoundary( &X_min, &X_max, &Y_min, &Y_max, screenSpaceVerts ) )
                return false;

        // use convention of orienting all edges to point in counter-clockwise direction
        Edge edges[3];
        if( !orderVertsCCW( edges, screenSpaceVerts ) )
                return false;   

        // calculate 2D line equations coefficients for all edges
        float edge0_A, edge0_B, edge0_C, edge1_A, edge1_B, edge1_C, edge2_A, edge2_B, edge2_C;
        getLineCoeff( &edge0_A, &edge0_B, &edge0_C, edges[0] );
        getLineCoeff( &edge1_A, &edge1_B, &edge1_C, edges[1] );
        getLineCoeff( &edge2_A, &edge2_B, &edge2_C, edges[2] );

        // need to create triangle plane eqn for all pixels that will be rendered in order to interpolate Z
        // A general plane equation has four terms: Ax + By + Cz + D = 0
        // Cross-product of two tri edges produces (A,B,C) vector
        // (X,Y,Z)0 X (X,Y,Z)1 = (A,B,C) = norm to plane of edges (and tri)
        // Solve for D at any vertex, using (A,B,C) from above
        GzCoord crossProduct;
        if( !( getCP( crossProduct, edges[0], edges[1] ) ) )
                return false;

        float plane_A, plane_B, plane_C, plane_D;
        plane_A = crossProduct[X];
        plane_B = crossProduct[Y];
        plane_C = crossProduct[Z];
        plane_D = getPlaneD(plane_A, plane_B, plane_C, screenSpaceVerts[0][X], screenSpaceVerts[0][Y], screenSpaceVerts[0][Z]);

        int X_start, Y_start, X_end, Y_end;
        // make sure starting pixel value is non-negative
        X_start = max( static_cast<int>( ceil( X_min ) ), 0 );
        Y_start = max( static_cast<int>( ceil( Y_min ) ), 0 );
        // make sure ending pixel value is within display size 
        // (note that pixel indeces are 0-based, so the maximum value for X and Y are xres - 1 and yres - 1, respectively
        X_end = min( static_cast<int>( floor( X_max ) ), render->display->xres - 1 );
        Y_end = min( static_cast<int>( floor( Y_max ) ), render->display->yres - 1 );

		GzColor colorPlaneA, colorPlaneB, colorPlaneC, colorPlaneD;
        // interpolation values for Phong shading:
        GzCoord imgSpaceVertsPlaneA, imgSpaceVertsPlaneB, imgSpaceVertsPlaneC, imgSpaceVertsPlaneD;
        GzCoord imgSpaceNormalsPlaneA, imgSpaceNormalsPlaneB, imgSpaceNormalsPlaneC, imgSpaceNormalsPlaneD;
		GzTextureIndex textCoordPlaneA, textCoordPlaneB, textCoordPlaneC, textCoordPlaneD;

        // we'll need to interpolate either colors or normals, so set up the pieces we know now
        GzCoord interpHelper1, interpHelper2, interpCrossProd;
        // the X and Y values for the two vectors we need to take cross products of will not change - they are the X and Y pixel coords
        interpHelper1[X] = edges[0].head.vertex[X] - edges[0].tail.vertex[X];
        interpHelper1[Y] = edges[0].head.vertex[Y] - edges[0].tail.vertex[Y];
        interpHelper2[X] = edges[1].head.vertex[X] - edges[1].tail.vertex[X];
        interpHelper2[Y] = edges[1].head.vertex[Y] - edges[1].tail.vertex[Y];

        switch( render->interp_mode )
        {
        case GZ_COLOR: // Gouraud shading.
                // compute the color at each vertex
                GzColor vertColors[3];
                for( int vertColorIndex = 0; vertColorIndex < 3; vertColorIndex++ )
                {
                        getColor( render, imageSpaceVerts[vertColorIndex], imageSpaceNormals[vertColorIndex], textureCoords[vertColorIndex], vertColors[vertColorIndex] );
                }

                // set up interpolation coefficients for each color component
                for( int compIndex = 0; compIndex < 3; compIndex++ )
                {
                        interpHelper1[Z] = vertColors[edges[0].head.origIdx][compIndex] - vertColors[edges[0].tail.origIdx][compIndex];
                        interpHelper2[Z] = vertColors[edges[1].head.origIdx][compIndex] - vertColors[edges[1].tail.origIdx][compIndex];
                        vectorCP( interpHelper1, interpHelper2, interpCrossProd );
                        colorPlaneA[compIndex] = interpCrossProd[X];
                        colorPlaneB[compIndex] = interpCrossProd[Y];
                        colorPlaneC[compIndex] = interpCrossProd[Z];
                        colorPlaneD[compIndex] = getPlaneD( colorPlaneA[compIndex], colorPlaneB[compIndex], colorPlaneC[compIndex], 
							                                edges[0].tail.vertex[X], edges[0].tail.vertex[Y], vertColors[edges[0].tail.origIdx][compIndex]);
                }
                break;
        case GZ_NORMALS:
                // construct coeffecients for all three components of vertices and normals
                for( int compIndex = 0; compIndex < 3; compIndex++ )
                {
                        // set up interpolation coefficients for vertices
                        interpHelper1[Z] = imageSpaceVerts[edges[0].head.origIdx][compIndex] - imageSpaceVerts[edges[0].tail.origIdx][compIndex];
                        interpHelper2[Z] = imageSpaceVerts[edges[1].head.origIdx][compIndex] - imageSpaceVerts[edges[1].tail.origIdx][compIndex];
                        vectorCP( interpHelper1, interpHelper2, interpCrossProd );
                        imgSpaceVertsPlaneA[compIndex] = interpCrossProd[X];
                        imgSpaceVertsPlaneB[compIndex] = interpCrossProd[Y];
                        imgSpaceVertsPlaneC[compIndex] = interpCrossProd[Z];
                        imgSpaceVertsPlaneD[compIndex] = getPlaneD( imgSpaceVertsPlaneA[compIndex], imgSpaceVertsPlaneB[compIndex], imgSpaceVertsPlaneC[compIndex],
							                                        edges[0].tail.vertex[X], edges[0].tail.vertex[Y], imageSpaceVerts[edges[0].tail.origIdx][compIndex]);

                        // set up interpolation coefficients for normals
                        interpHelper1[Z] = imageSpaceNormals[edges[0].head.origIdx][compIndex] - imageSpaceNormals[edges[0].tail.origIdx][compIndex];
                        interpHelper2[Z] = imageSpaceNormals[edges[1].head.origIdx][compIndex] - imageSpaceNormals[edges[1].tail.origIdx][compIndex];
                        vectorCP( interpHelper1, interpHelper2, interpCrossProd );
                        imgSpaceNormalsPlaneA[compIndex] = interpCrossProd[X];
                        imgSpaceNormalsPlaneB[compIndex] = interpCrossProd[Y];
                        imgSpaceNormalsPlaneC[compIndex] = interpCrossProd[Z];
                        imgSpaceNormalsPlaneD[compIndex] = getPlaneD( imgSpaceNormalsPlaneA[compIndex], imgSpaceNormalsPlaneB[compIndex], imgSpaceNormalsPlaneC[compIndex], 
							                                          edges[0].tail.vertex[X], edges[0].tail.vertex[Y], imageSpaceNormals[edges[0].tail.origIdx][compIndex]);
                }

		        GzTextureIndex perspTextureCoords[3];
                float screenSpaceZ[3];
                for( int vertIndex = 0; vertIndex < 3; vertIndex++ )
                {
                     screenSpaceZ[vertIndex] = interpPlaneCoeffs( plane_A, plane_B, plane_C, plane_D, 
                                                                               screenSpaceVerts[vertIndex][X], screenSpaceVerts[vertIndex][Y] );

             // transform affine space (u,v) to perspective space (U,V) to avoid perspective warping
                     perspTextureCoords[vertIndex][U] = imgSpaceParamToPerspSpace( screenSpaceZ[vertIndex], textureCoords[vertIndex][U] );
                     perspTextureCoords[vertIndex][V] = imgSpaceParamToPerspSpace( screenSpaceZ[vertIndex], textureCoords[vertIndex][V] );
                 }

                // set up interpolation coefficients for texture coordinates
                 for( int textCompIndex = 0; textCompIndex < 2; textCompIndex++ )
                 {
             // set up interpolation coefficients for perspective space texture coordinate
                      interpHelper1[Z] = perspTextureCoords[edges[0].head.origIdx][textCompIndex] - perspTextureCoords[edges[0].tail.origIdx][textCompIndex];
                      interpHelper2[Z] = perspTextureCoords[edges[1].head.origIdx][textCompIndex] - perspTextureCoords[edges[1].tail.origIdx][textCompIndex];
                      vectorCP( interpHelper1, interpHelper2, interpCrossProd );
                      textCoordPlaneA[textCompIndex] = interpCrossProd[X];
                      textCoordPlaneB[textCompIndex] = interpCrossProd[Y];
                      textCoordPlaneC[textCompIndex] = interpCrossProd[Z];
                      textCoordPlaneD[textCompIndex] = getPlaneD( textCoordPlaneA[textCompIndex],
				                                              textCoordPlaneB[textCompIndex],
                                                              textCoordPlaneC[textCompIndex],
                                                              edges[0].tail.vertex[X],
                                                              edges[0].tail.vertex[Y],
                                                              perspTextureCoords[edges[0].tail.origIdx][textCompIndex] );
                 }
                
                 break;
        }

        // now walk through all pixels within bounding box and rasterize
        // Y coords are rows 
        for( int y = Y_start; y <= Y_end; y++ )
        {
                // Y coords are columns
                for( int x = X_start; x <= X_end; x++ )
                {
                        bool display = false;

                        // test this pixel against edge0
                        float edge0_d = edge0_A * x + edge0_B * y + edge0_C;
                        if( edge0_d == 0 )
                        {
                                if( edges[0].coloring == YES )
                                        display = true;
                                else
                                        continue; // pixel is on a triangle edge that should not be shaded
                        }
                        else if( edge0_d < 0 ) // negative value means it's not in the triangle
                                continue;
                        // end edge0 test

                        // if we know we're going to shade the pixel, don't test against any more edges
                        if( !display ) 
                        {
                                // test this pixel against edge1
                                float edge1_d = edge1_A * x + edge1_B * y + edge1_C;
                                if( edge1_d == 0 )
                                {
                                        if( edges[1].coloring == YES )
                                                display = true;
                                        else
                                                continue; // pixel is on a triangle edge that should not be shaded
                                }
                                else if( edge1_d < 0 ) // negative value means it's not in the triangle
                                        continue;
                                // end edge1 test
                        }

                        // if we know we're going to shade the pixel, don't test against any more edges
                        if( !display )
                        {
                                // test this pixel against edge2
                                float edge2_d = edge2_A * x + edge2_B * y + edge2_C;
                                if( edge2_d == 0 )
                                {
                                        if( edges[2].coloring == YES )
                                                display = true;
                                        else
                                                continue; // pixel is on a triangle edge that should not be shaded
                                }
                                else if( edge2_d < 0 ) // negative value means it's not in the triangle
                                        continue;
                                // end edge2 test
                        }

                        // if we're here, the pixel should be shaded. 
                        // First interpolate Z and check value against z-buffer
                        // Ax + By + Cz + D = 0 => z = -( Ax + By + D ) / C
                        float Z_interp = interpPlaneCoeffs( plane_A, plane_B, plane_C, plane_D, x, y);

                        // don't render pixels of triangles that reside behind camera
                        if( Z_interp < 0 )
                                continue;

                        GzIntensity r, g, b, a;
                        GzDepth z;
                        // returns a non-zero value for failure
                        if( GzGetDisplay( render->display, x, y, &r, &g, &b, &a, &z ) )
                        {
                                // NOTE: this shouldn't happen. But just in case it does, simply skip this pixel, not the whole triangle.
                                continue;
                        }

                        // if the interpolated z value is smaller than the current z value, write pixel to framebuffer
                        if( Z_interp < z )
                        {
                                GzColor color;
                                // calculate appropriate color based on selected interpolation method
                                switch( render->interp_mode )
                                {
                                case GZ_COLOR: // Gourad shading
                                        // just interpolate the pre-calculated vertex colors at this pixel
                                        for( int compIndex = 0; compIndex < 3; compIndex++ )
                                        {
                                                color[compIndex] = interpPlaneCoeffs( colorPlaneA[compIndex], colorPlaneB[compIndex], colorPlaneC[compIndex],
													                                  colorPlaneD[compIndex], x, y);
                                        }
                                        break;
                                case GZ_NORMALS: // Phong shading
                                        // first we must use bilinear interpolation to find the normal at this pixel
                                        GzCoord interpImageSpaceVert, interpImageSpaceNormal;
										GzTextureIndex interpTextureCoords;

                                        for( int compIndex = 0; compIndex < 3; compIndex++ )
                                        {
                                                interpImageSpaceVert[compIndex] = interpPlaneCoeffs( imgSpaceVertsPlaneA[compIndex], 
													                                                 imgSpaceVertsPlaneB[compIndex],
																									 imgSpaceVertsPlaneC[compIndex],
                                                                                                     imgSpaceVertsPlaneD[compIndex],
																									 x, y);

                                                interpImageSpaceNormal[compIndex] = interpPlaneCoeffs( imgSpaceNormalsPlaneA[compIndex],
                                                                                                       imgSpaceNormalsPlaneB[compIndex],
                                                                                                       imgSpaceNormalsPlaneC[compIndex],
																					                   imgSpaceNormalsPlaneD[compIndex],
																									   x, y);
                                        }

										for( int textCompIndex = 0; textCompIndex < 2; textCompIndex++ )
										{
										        interpTextureCoords[textCompIndex] = interpPlaneCoeffs( textCoordPlaneA[textCompIndex],
													                                                    textCoordPlaneB[textCompIndex],
																										textCoordPlaneC[textCompIndex],
																										textCoordPlaneD[textCompIndex],
																										x, y);

												interpTextureCoords[textCompIndex] = perspSpaceParamToImgSpace( Z_interp, interpTextureCoords[textCompIndex] );
										}

                                        // now that we have the interpolated normal, make sure it's normalized
                                        normalize( interpImageSpaceNormal );
                                        // now we can compute the color using the interpolated normal
                                        getColor( render, interpImageSpaceVert, interpImageSpaceNormal, interpTextureCoords, color );

                                        break;
                                default:
                                        // just use flat shading as the default
                                        color[RED] = render->flatcolor[RED];
                                        color[GREEN] = render->flatcolor[GREEN];
                                        color[BLUE] = render->flatcolor[BLUE];
                                        break;
                                }

                                if( GzPutDisplay( render->display, x, y, 
                                                      ctoi( color[RED] ),
                                                                  ctoi( color[GREEN] ),
                                                                  ctoi( color[BLUE] ),
                                                                  a, // just duplicate existing alpha value for now
                                                                  ( GzDepth )Z_interp ) )
                                {
                                        AfxMessageBox( "Error: could not put new values into display in GzPutTriangle()!\n" );
                                        return false;
                                }
                        }
                } // end column for loop (X)
        } // end row for loop (Y)

        return true;
}

/*** END HW2 FUNCTIONS ***/

/*** BEGIN HW3 FUNCTIONS ***/

bool clearStack( GzRender * render )
{
        if( !render )
                return false;

        render->matlevel = STACK_IS_EMPTY;
        return true;
}

bool matMultiply( GzMatrix matProduct, const GzMatrix mat1, const GzMatrix mat2 )
{
        /* 
         * Recall that when we multiply two 4x4 matrices A & B:
         *              ( AB )i,j = summation for k = 1 to k = 4 of ( A )i,k * ( B )k,j
         */

        // initialize all elements of the matrix to 0
        for( int row = 0; row < 4; row++ )
        {
                for( int col = 0; col < 4; col++ )
                {
                        matProduct[row][col] = 0;
                }
        }

        for( int row = 0; row < 4; row++ )
        {
                for( int col = 0; col < 4; col++ )
                {
                        for( int sumIndex = 0; sumIndex < 4; sumIndex++ )
                        {
                                matProduct[row][col] += mat1[row][sumIndex] * mat2[sumIndex][col];
                        }
                }
        }

        return true;
}

bool homoMatVectorMultiply( GzCoord result, const GzMatrix mat, const GzCoord vector )
{
        float tempResult[4];
        float tempVector[4];

		for( int i = 0; i < 4; i++ )
                tempResult[i] = 0;

        tempVector[X] = vector[X];
        tempVector[Y] = vector[Y];
        tempVector[Z] = vector[Z];
        tempVector[3] = 1;

        for( int i = 0; i < 4; i++ )
                tempResult[i] = 0;
        
        for( int index = 0; index < 4; index++ )
        {
                for( int sumIndex = 0; sumIndex < 4; sumIndex++ )
                {
                        tempResult[index] += mat[index][sumIndex] * tempVector[sumIndex];
                }
        }

        result[X] = tempResult[X] / tempResult[3];
        result[Y] = tempResult[Y] / tempResult[3];
        result[Z] = tempResult[Z] / tempResult[3];

        return true;
}

float vectorDot( const GzCoord vector1, const GzCoord vector2 )
{
        return vector1[X] * vector2[X] + vector1[Y] * vector2[Y] + vector1[Z] * vector2[Z];
}

bool vectorCP( const GzCoord vector1, const GzCoord vector2, GzCoord CP )
{
        CP[X] = vector1[Y] * vector2[Z] - vector1[Z] * vector2[Y];
        CP[Y] = vector1[Z] * vector2[X] - vector1[X] * vector2[Z];
        CP[Z] = vector1[X] * vector2[Y] - vector1[Y] * vector2[X];

        return true;
}

bool negateVector( const GzCoord origVector, GzCoord negVector )
{
        negVector[X] = -origVector[X];
        negVector[Y] = -origVector[Y];
        negVector[Z] = -origVector[Z];

        return true;
}

bool vectorSub( const GzCoord vector1, const GzCoord vector2, GzCoord diff )
{
        diff[X] = vector1[X] - vector2[X];
        diff[Y] = vector1[Y] - vector2[Y];
        diff[Z] = vector1[Z] - vector2[Z];

        return true;
}

bool vectorScale( const GzCoord origVector, float factor, GzCoord result )
{
        result[X] = origVector[X] * factor;
        result[Y] = origVector[Y] * factor;
        result[Z] = origVector[Z] * factor;

        return true;
}

bool normalize( GzCoord vector )
{
        float magnitude = sqrt( vector[X] * vector[X] + vector[Y] * vector[Y] + vector[Z] * vector[Z] );

        vector[X] = vector[X]/magnitude;
        vector[Y] = vector[Y]/magnitude;
        vector[Z] = vector[Z]/magnitude;

        return true;
}

bool getXsp( GzRender * render )
{
        // check for bad pointers
        if( !render || !render->display )
                return false;

        // NOTE: camera should be initialized FIRST!!!

        /* SETUP Xsp 
         * Format:
         *      xs/2    0               0               xs/2
         *      0               -ys/2   0               ys/2
         *      0               0               Zmax/d  0
         *      0               0               0               1
         *
         * Note that Zmax is the highest possible Z-value (e.g. depth value).
         * d comes from the camera's field of view [1/d = tan( FOV /2 ), so d = 1 / ( tan( FOV / 2 ) )]. 
         */

        // since FOV is in degrees, we must first convert to radians.
        float d = ( float )( 1 / tan( D_TO_R( render->camera.FOV ) / 2 ) );

        // row 0
        render->Xsp[0][0] = render->display->xres / ( float )2; // upcast the denominator to maintain accuracy
        render->Xsp[0][1] = 0;
        render->Xsp[0][2] = 0;
        render->Xsp[0][3] = render->display->xres / ( float )2; // upcast the denominator to maintain accuracy

        // row 1
        render->Xsp[1][0] = 0;
        render->Xsp[1][1] = -render->display->yres / ( float )2; // upcast the denominator to maintain accuracy
        render->Xsp[1][2] = 0;
        render->Xsp[1][3] = render->display->yres / ( float )2; // upcast the denominator to maintain accuracy

        // row 2
        render->Xsp[2][0] = 0;
        render->Xsp[2][1] = 0;
        render->Xsp[2][2] = INT_MAX / d;
        render->Xsp[2][3] = 0;

        // row 3
        render->Xsp[3][0] = 0;
        render->Xsp[3][1] = 0;
        render->Xsp[3][2] = 0;
        render->Xsp[3][3] = 1;
        /* DONE SETTING UP Xsp */

        return true;
}

bool getCameraXPara( GzRender * render )
{
        // check for bad pointers
        if( !render || !render->display )
                return false;
        
        // NOTE: the camera MUST be initialized BEFORE this function is called!!!

        // recall that d comes from the camera's field of view [1/d = tan( FOV /2 ), so d = 1 / ( tan( FOV / 2 ) )]. 
        // since FOV is in degrees, we must first convert to radians.
        float FOV_radians = ( float )D_TO_R( render->camera.FOV );
        float d = 1 / tan( FOV_radians / 2 );

        // Construct Xpi
        /*
         * Format of Xpi:
         *      1       0       0       0
         *      0       1       0       0
         *      0       0       1       0
         *      0       0       1/d     1
         */
        // row 0
        render->camera.Xpi[0][0] = 1;
        render->camera.Xpi[0][1] = 0;
        render->camera.Xpi[0][2] = 0;
        render->camera.Xpi[0][3] = 0;

        // row 1
        render->camera.Xpi[1][0] = 0;
        render->camera.Xpi[1][1] = 1;
        render->camera.Xpi[1][2] = 0;
        render->camera.Xpi[1][3] = 0;

        // row 2
        render->camera.Xpi[2][0] = 0;
        render->camera.Xpi[2][1] = 0;
        render->camera.Xpi[2][2] = 1;
        render->camera.Xpi[2][3] = 0;

        // row 3
        render->camera.Xpi[3][0] = 0;
        render->camera.Xpi[3][1] = 0;
        render->camera.Xpi[3][2] = 1 / d;
        render->camera.Xpi[3][3] = 1;

        // Construct Xiw
        /*
         * We'll build Xiw by building Xwi and taking its inverse.
         * We already know the camera position (c) and look at point (l) in world space.
         * The camera's z axis in world space is therefore 
         *              Z = ( c->l ) / || ( c->l ) ||. 
         *              Observe that c->l is ( l - c ).
         * We know the camera's up vector (up) in world space.
         * up' (the up vector in image space) is therefore
         *              up' = up - dot( up, Z ) * Z
         * Thus the camera's Y vector in image space is
         *              Y = up' / || up' ||
         * Finally, we use a left-handed coordinate system for the camera. Therefore
         *              X = ( Y x Z )
         *
         * So we now have our Xwi transform:
         *              Xx      Yx      Zx      Cx
         *              Xy      Yy      Zy      Cy
         *              Xz      Yz      Zz      Cz
         *              0       0       0       1
         * Observe that this matrix has the form 
         *              Xwi = T * R
         * Thus we can derive Xiw:
         *              Xiw = inv( Xwi ) = inv( T * R ) = inv( R ) * inv( T )
         * That is, Xiw looks like this:
         *              Xx      Xy      Xz      dot( -X, c )
         *              Yx      Yy      Yz      dot( -Y, c )
         *              Zx      Zy      Zz      dot( -Z, c )
         *              0       0       0       1
         * Note that for any vectors A and B, dot( -A, B ) = -dot( A, B ).
         */

        GzCoord X_camera, Y_camera, Z_camera, upDotCamZTimesCamZ;

        // first build camera Z vector
        vectorSub( render->camera.lookat, render->camera.position, Z_camera );
        normalize( Z_camera );

        // now build camera Y vector
        float upDotCamZ = vectorDot( render->camera.worldup, Z_camera );
        vectorScale( Z_camera, upDotCamZ, upDotCamZTimesCamZ );
        vectorSub( render->camera.worldup, upDotCamZTimesCamZ, Y_camera );
        normalize( Y_camera );

        // now build camera X vector
        vectorCP( Y_camera, Z_camera, X_camera ); // observe that crossing 2 unit vectors yields a unit vector, so we don't need to normalize

        // row 0
        render->camera.Xiw[0][0] = X_camera[X];
        render->camera.Xiw[0][1] = X_camera[Y];
        render->camera.Xiw[0][2] = X_camera[Z];
        render->camera.Xiw[0][3] = -vectorDot( X_camera, render->camera.position );

        // row 1
        render->camera.Xiw[1][0] = Y_camera[X];
        render->camera.Xiw[1][1] = Y_camera[Y];
        render->camera.Xiw[1][2] = Y_camera[Z];
        render->camera.Xiw[1][3] = -vectorDot( Y_camera, render->camera.position );

        // row 2
        render->camera.Xiw[2][0] = Z_camera[X];
        render->camera.Xiw[2][1] = Z_camera[Y];
        render->camera.Xiw[2][2] = Z_camera[Z];
        render->camera.Xiw[2][3] = -vectorDot( Z_camera, render->camera.position );

        // row 3
        render->camera.Xiw[3][0] = 0;
        render->camera.Xiw[3][1] = 0;
        render->camera.Xiw[3][2] = 0;
        render->camera.Xiw[3][3] = 1;

        return true;
}

bool boundaryCheck( GzRender * render, GzCoord * vertices )
{
        // NOTE: verts should be give in screen space coordinates
        if( !render || !render->display || !vertices )
                return true; // no image plane is available!

        // all vertices are above image plane (e.g all Y coords are < 0)
        if( vertices[0][Y] < 0 && vertices[1][Y] < 0 && vertices[2][Y] < 0 )
                return true;
        // all vertices are below image plane (e.g all Y coords are > yres)
        else if( vertices[0][Y] > render->display->yres && vertices[1][Y] > render->display->yres && vertices[2][Y] > render->display->yres )
                return true;
        // all vertices are to left of image plane (e.g all X coords are < 0)
        else if( vertices[0][X] < 0 && vertices[1][X] < 0 && vertices[2][X] < 0 )
                return true;
        // all vertices are to right of image plane (e.g all X coords are > xres)
        else if( vertices[0][X] > render->display->xres && vertices[1][X] > render->display->xres && vertices[2][X] > render->display->xres )
                return true;
        // we can't determine the triangle is completely outside the image plane
        else
                return false;
}

bool getColor( GzRender * render, const GzCoord imageSpaceVert, const GzCoord imageSpaceNormal, const GzTextureIndex textureCoords, GzColor colorvalue )
{
        // check for bad pointer
        if( !render )
                return false;

        // intialize color to black just in case
        colorvalue[RED] = colorvalue[GREEN] = colorvalue[BLUE] = 0;

        // Shading equation: 
        //              Color = (Ks * sumOverLights[ lightIntensity ( R dot E )^s ] ) + (Kd * sumOverLights[lightIntensity (N dot L)] ) + ( Ka Ia ) 
        // So we must sum over all the lights.
        GzColor KsComponent, KdComponent;
        KsComponent[RED] = KsComponent[GREEN] = KsComponent[BLUE] = KdComponent[RED] = KdComponent[GREEN] = KdComponent[BLUE] = 0;
        for( int lightIndex = 0; lightIndex < render->numlights; lightIndex++ )
        {
                // compute N dot L
                float NdotL = vectorDot( imageSpaceNormal, render->lights[lightIndex].direction );

                // In image space, the direction to the eye (e.g. camera) is simply (0, 0, -1)
                GzCoord eyeDirection;
                eyeDirection[X] = eyeDirection[Y] = 0;
                eyeDirection[Z] = -1;
                // no need to normalize the eye direction explicitly; it's defined to be a unit vector

                // compute N dot E
                float NdotE = vectorDot( imageSpaceNormal, eyeDirection );

                // We need to consider the sign of N dot L and N dot E:
                //              Both positive : compute lighting model
                //              Both negative : flip normal and compute lighting model on backside of surface
                //              Both different sign : light and eye on opposite sides of surface so that light contributes zero  skip it
                GzCoord newImageSpaceNormal;
                if( NdotL > 0 && NdotE > 0 )
                {
                        // keep the same normal
                        newImageSpaceNormal[X] = imageSpaceNormal[X];
                        newImageSpaceNormal[Y] = imageSpaceNormal[Y];
                        newImageSpaceNormal[Z] = imageSpaceNormal[Z];
                }
                else if( NdotL < 0 && NdotE < 0 )
                {
                        // flip normal
                        vectorScale( imageSpaceNormal, -1, newImageSpaceNormal );
                        // we've flipped the normal, so we need to flip NdotL & NdotE
                        NdotL *= -1;
                        NdotE *= -1;
                }
                else
                {
                        continue;
                }

                // Compute reflected ray. R = 2(N dot L)N - L   
                GzCoord twoNdotLTimesN, reflectRay;
                vectorScale( newImageSpaceNormal, 2 * NdotL, twoNdotLTimesN );
                vectorSub( twoNdotLTimesN, render->lights[lightIndex].direction, reflectRay );
                // make sure reflected ray is normalized
                normalize( reflectRay );

                // now add in the Kd and Ks contributions from this light
                float RdotE = vectorDot( reflectRay, eyeDirection );
                // don't allow RdotE to be a negative value
                if( RdotE < 0 )
                        RdotE = 0;

                GzColor tempKsComponent, tempKdComponent;
                vectorScale( render->lights[lightIndex].color, pow( RdotE, render->spec ), tempKsComponent );
                vectorScale( render->lights[lightIndex].color, NdotL, tempKdComponent );       

                vectorSum( KsComponent, tempKsComponent, KsComponent );
                vectorSum( KdComponent, tempKdComponent, KdComponent );
        }

        // the Ka, Kd, and Ks we will use depend upon whether or not we are using textures and what type of shading we're doing
        GzColor Ka, Kd, Ks;
        // we're using a texture
        if( render->tex_fun )
        {
                GzColor textureColor;
                render->tex_fun( textureCoords[U], textureCoords[V], textureColor );

                memcpy( Ka, textureColor, sizeof( GzColor ) );
                memcpy( Kd, textureColor, sizeof( GzColor ) );

                // if we're doing Gouraud shading, use the texture color for Ks
                if( render->interp_mode == GZ_COLOR )
                {
                        memcpy( Ks, textureColor, sizeof( GzColor ) );
                }
                // otherwise use the Ks stored in the renderer
                else
                {
                        memcpy( Ks, render->Ks, sizeof( GzColor ) );
                }
        }
        // just use the object's color
        else
        {
                memcpy( Ka, render->Ka, sizeof( GzColor ) );
                memcpy( Kd, render->Kd, sizeof( GzColor ) );
                memcpy( Ks, render->Ks, sizeof( GzColor ) );
        }

        // finally, put the shading equation together:
        //              Color = (Ks * sumOverLights[ lightIntensity ( R dot E )^s ] ) + (Kd * sumOverLights[lightIntensity (N dot L)] ) + ( Ka Ia ) 
        GzCoord KaComponent;
        vectorMultiply( Ks, KsComponent, KsComponent );
        vectorMultiply( Kd, KdComponent, KdComponent );
        vectorMultiply( Ka, render->ambientlight.color, KaComponent );
        
        // add all components together
        vectorSum( KsComponent, KdComponent, colorvalue );
        vectorSum( KaComponent, colorvalue, colorvalue );

        return true;
}

bool vectorSum( const GzCoord vec1, const GzCoord vec2, GzCoord sum )
{
        sum[X] = vec1[X] + vec2[X];
        sum[Y] = vec1[Y] + vec2[Y];
        sum[Z] = vec1[Z] + vec2[Z];
        return true;
}

bool vectorMultiply( const GzCoord vec1, const GzCoord vec2, GzCoord product )
{
        for( int idx = 0; idx < 3; idx++ )
        {
                product[idx] = vec1[idx] * vec2[idx];
        }

        return true;
}

float getPlaneD(float planeA, float planeB, float planeC, float pixel_x, float pixel_y, float paramToInterp)
{
        return -( planeA * pixel_x + planeB * pixel_y + planeC * paramToInterp );  
}

float interpPlaneCoeffs( float planeA, float planeB, float planeC, float planeD, float pixel_x, float pixel_y)
{
        return -( planeA * pixel_x + planeB * pixel_y  + planeD ) / planeC;
}

float imgSpaceParamToPerspSpace( float screenSpaceInterpZ, float param )
{
	    return param / ( getVPrimeZ( screenSpaceInterpZ ) + 1 );  
}

float perspSpaceParamToImgSpace( float screenSpaceInterpZ, float param )
{
        return param * ( getVPrimeZ( screenSpaceInterpZ ) + 1 );  
}

float getVPrimeZ( float screenSpaceInterpZ )
{
	    return screenSpaceInterpZ / ( INT_MAX - screenSpaceInterpZ );
}