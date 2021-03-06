/* program to draw a 1D texture on a cube */
package basics;

import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.FPSAnimator;
import java.io.File;
import java.net.URL;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Use JOGL to draw a simple cube
 * with each face being a different color.  Rotations
 * can be applied with the arrow keys, the page up
 * key, and the page down key.  The home key will set
 * all rotations to 0.  Initial rotations about the
 * x, y, and z axes are 15, -15, and 0.  
 *
 * This program is meant as an example of using modeling
 * transforms, with glPushMatrix and glPopMatrix.
 *
 * Note that this program does not use lighting.
 */
public class Texture2Dim implements GLEventListener, KeyListener {
	File file;
	Texture texture;
    /**
     * A main routine to create and show a window that contains a
     * panel of type UnlitCube.  The program ends when the
     * user closes the window.
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("A Simple Unlit Cube -- ARROW KEYS ROTATE");
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Texture2Dim cube = new Texture2Dim();
        glcanvas.addGLEventListener(cube);        
        glcanvas.setSize(400,400);
        glcanvas.addKeyListener(cube);
        
        window.add( glcanvas );
        window.pack();
        window.setLocation(50,50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        glcanvas.requestFocusInWindow();
        FPSAnimator animator=new FPSAnimator(glcanvas,300,true);
        animator.start();
    }
    
    /**
     * Constructor for class UnlitCube.
     */
    public Texture2Dim() {
    	
    	
    }
    
    //-------------------- methods to draw the cube ----------------------
    
    double rotateX = 15;    // rotations of the cube about the axes
    double rotateY = 15;
    double rotateZ = 0;
    
    private void square(GL2 gl2, double r, double g, double b) {
        gl2.glColor3d(r,g,b);
        gl2.glBegin(GL2.GL_POLYGON);
        gl2.glNormal3f(0,0,1);
        gl2.glTexCoord2d(0, 0);
        gl2.glVertex3d(-0.5, -0.5, 0.5);
        gl2.glTexCoord2d(2, 0);
        gl2.glVertex3d(0.5, -0.5, 0.5);
        gl2.glTexCoord2d(2, 2);
        gl2.glVertex3d(0.5, 0.5, 0.5);
        gl2.glTexCoord2d(0, 2);
        gl2.glVertex3d(-0.5, 0.5, 0.5);
        gl2.glEnd();
    }
    
    private void cube(GL2 gl2, double size) {
        gl2.glPushMatrix();
        gl2.glScaled(size,size,size); // scale unit cube to desired size
        
        square(gl2,1, 1, 1); // red front face
        
        gl2.glPushMatrix();
        gl2.glRotated(90, 0, 1, 0);
        square(gl2,1, 1, 1); // green right face
        gl2.glPopMatrix();
        
        gl2.glPushMatrix();
        gl2.glRotated(-90, 1, 0, 0);
        square(gl2,1, 1, 1); // blue top face
        gl2.glPopMatrix();
        
        gl2.glPushMatrix();
        gl2.glRotated(180, 0, 1, 0);
        square(gl2,1, 1, 1); // cyan back face
        gl2.glPopMatrix();
        
        gl2.glPushMatrix();
        gl2.glRotated(-90, 0, 1, 0);
        square(gl2,1, 1, 1); // magenta left face
        gl2.glPopMatrix();
        
        gl2.glPushMatrix();
        gl2.glRotated(90, 1, 0, 0);
        square(gl2,1, 1, 1); // yellow bottom face
        gl2.glPopMatrix();
        
        gl2.glPopMatrix(); // Restore matrix to its state before cube() was called.
    }
    
    
    //-------------------- GLEventListener Methods -------------------------

    /**
     * The display method is called when the panel needs to be redrawn.
     * The is where the code goes for drawing the image, using OpenGL commands.
     */
    public void display(GLAutoDrawable drawable) {    
        
        GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.
         
        gl2.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        
        gl2.glLoadIdentity();             // Set up modelview transform. 
        gl2.glRotated(rotateZ,0,0,1);
        gl2.glRotated(rotateY,0,1,0);
        gl2.glRotated(rotateX,1,0,0);

        cube(gl2,1);
        
    } // end display()

    public void init(GLAutoDrawable drawable) {
           // called when the panel is created
    	try {                
	        
    		file=new File("E:\\workspace(juno)\\JOGL1\\src\\texture\\popeye.jpeg");
	        texture=TextureIO.newTexture(file,true);
	       } catch (Exception ex) {
	            // handle exception...
	    	   System.out.println("Exception");
	       }
    	
    	
    	GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glOrtho(-1, 1 ,-1, 1, -1, 1);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glClearColor( 0, 0, 0, 1 );
        gl2.glEnable(GL2.GL_DEPTH_TEST);
       // gl2.glEnable(GL2.GL_TEXTURE_2D);
       // texture.enable(gl2);
        //texture.bind(gl2);
        gl2.glEnable(texture.getTarget());
        
        texture.setTexParameteri(gl2,GL2.GL_TEXTURE_WRAP_S,GL2.GL_REPEAT);
        texture.setTexParameteri(gl2,GL2.GL_TEXTURE_WRAP_T,GL2.GL_REPEAT);
    }

    public void dispose(GLAutoDrawable drawable) {
            // called when the panel is being disposed
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            // called when user resizes the window
    }
    
    // ----------------  Methods from the KeyListener interface --------------

    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        if ( key == KeyEvent.VK_LEFT )
            rotateY -= 15;
         else if ( key == KeyEvent.VK_RIGHT )
            rotateY += 15;
         else if ( key == KeyEvent.VK_DOWN)
            rotateX += 15;
         else if ( key == KeyEvent.VK_UP )
            rotateX -= 15;
        
    }

    public void keyReleased(KeyEvent evt) {
    }
    
    public void keyTyped(KeyEvent evt) {
    }
    
}

