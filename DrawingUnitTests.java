/*
Drawing Tool Coding Challenge for Huge Inc.
Completed by Kelly Anlas 3/3/2016
*/
package Test;
/*
These unit tests are designed to check the corner cases for the key methods
in this program, adding lines, rectangles, and using the bucket fill, as well
as creating a canvas that is either too small or too large.
*/
import DrawingToolPackage.DrawingCanvas;
import org.junit.Test;
import static org.junit.Assert.*;

public class DrawingUnitTests {
    public DrawingUnitTests() {
    }
    
    @Test
    public void createCanvasWithZeros() {
        DrawingCanvas testCanvas = new DrawingCanvas();
        testCanvas.createCanvas(0, 0);
        assertFalse(testCanvas.hasCanvas());
        
        testCanvas.createCanvas(0, 15);
        assertFalse(testCanvas.hasCanvas());
    }
    
    @Test
    public void createOverlargeCanvas() {
        DrawingCanvas testCanvas = new DrawingCanvas();
        
        //This canvas is greater than 2^25 in size, should not create canvas
        testCanvas.createCanvas(5800, 5800);
        assertFalse(testCanvas.hasCanvas());
    }
    
    @Test
    public void lineBounds() {
        DrawingCanvas testCanvas = new DrawingCanvas();
        testCanvas.createCanvas(25, 25);
        
        //Point 1 is off canvas, should generate error string
        testCanvas.addLine(0, 5, 5, 5);
        assertEquals("Point 1 is out of bounds.", testCanvas.getErrorStr());
        
        //Point 2 is off canvas, should generate error string
        testCanvas.addLine(5, 5, 26, 5);
        assertEquals("Point 2 is out of bounds.", testCanvas.getErrorStr());
        
        //Horizontal line, should have no errors
        testCanvas.addLine(1, 1, 25, 1);
        assertNull(testCanvas.getErrorStr());
        
        //Vertical line, should have no errors
        testCanvas.addLine(25, 1, 25, 25);
        assertNull(testCanvas.getErrorStr());
    }
    
    @Test
    public void rectangleBounds () {
        DrawingCanvas testCanvas = new DrawingCanvas();
        testCanvas.createCanvas(25, 25);
        
        //Point 1 is off the canvas, should have error string
        testCanvas.addRectangle(0, 5, 5, 5);
        assertEquals("Point 1 is out of bounds.", testCanvas.getErrorStr());
        
        //Points are valid, should have no errors
        testCanvas.addRectangle(5, 1, 25, 3);
        assertNull(testCanvas.getErrorStr());
    }
    
    @Test
    public void bucketFillBounds () {
        DrawingCanvas testCanvas = new DrawingCanvas();
        
        testCanvas.createCanvas(25, 25);
        
        //Valid point, should have no errors
        testCanvas.bucketFill(5, 5, 'c');
        assertNull(testCanvas.getErrorStr());
        
        //Point is out of bounds, should generate error string
        testCanvas.bucketFill(26, 1, 'b');
        assertEquals("Point is out of bounds.", testCanvas.getErrorStr());
    }
    
    @Test
    public void bucketOverfill () {
        DrawingCanvas testCanvas = new DrawingCanvas();
        
        testCanvas.createCanvas(250, 250);
        
        /*While this point is valid, the recursive method to bucket fill will
         hit a stack overflow if the area is too large. This program is
         designed to stop filling and handle the error.
        */
        testCanvas.bucketFill(1, 1, 'x');
        assertEquals("Fill area too large.", testCanvas.getErrorStr());
    }
}
