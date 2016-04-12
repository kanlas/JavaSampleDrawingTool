/*
Drawing Tool Coding Challenge for Huge Inc.
Completed by Kelly Anlas 3/3/2016
*/
package DrawingToolPackage;
/*
This class contains the Canvas object and all methods pertaining to the canvas,
including creating the canvas, adding a line, adding a rectangle, and using
the bucket fill, as well as helpful methods such as writing to the output file,
converting an (x,y) point to an integer, and verifying that point.
*/
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class DrawingCanvas {
    /*
    width - int - the drawable width of the canvas
    height - int - the drawable height of the canvas
    w - int - the full width of canvas + border
    h - int - the full height of canvas + border
    size - int - the size of the full canvas + border
    sizew - int - the size minus the bottom border
    curChar - int - when using bucket fill, the original char of the start point
    newChar - int - when using bucket fill, the new char of any affected point
    curCharEmpty - boolean - when using bucket fill, is the start point empty
    hasCanvas - boolean - flag set when a canvas has been created
    canvas - HashMap<Integer, Character> - a map of keys(points) and chars
    errorStr - String - String used to unit test errors before writing to file
    */
    int w, h, width, height, size, sizew, i;
    char curChar, newChar;
    boolean curCharEmpty = true, hasCanvas = false;
    String errorStr = null;
    FileWriter output;
    HashMap<Integer, Character> canvas;
    
    public DrawingCanvas () {
        try {
            output = new FileWriter("output.txt");
        }catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void createCanvas (int width, int height) {
        errorStr = null;
        if (width*height > Math.pow(2, 25)) {
            errorStr = ("Canvas is too large.");
            writeToFile(errorStr);
        }
        else if (width < 1 || height < 1) {
            errorStr = ("Canvas minimum is 1x1.");
            writeToFile(errorStr);
        }
        else {
            w = width+2;
            h = height+2;
            this.width = width;
            this.height = height;
            size = w*h;
            sizew = size - w;
            canvas = new HashMap<>();

            //Create border of canvas
            for (i=1; i<=w; i++) {
                canvas.put(i, '-');
            }
            for (i=w+1; i<=sizew; i+=w) {
                canvas.put(i, '|');
            }
            for (i=2*w; i<=sizew; i+=w) {
                canvas.put(i, '|');
            }
            for (i=size; i>sizew; i--) {
                canvas.put(i, '-');
            }
            printCanvas();
            hasCanvas = true;
        }
    }
    
    public void printCanvas () {
        try {
            for (i=1; i<=size; i++) {
                if (!canvas.containsKey(i)) 
                    output.write(" ");
                else {
                    output.write(canvas.get(i));
                    if (i%w == 0) { 
                        //if the point is on right hand side, add CRLF
                        output.write("\r\n");
                    }
                }
            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void addLine (int x1, int y1, int x2, int y2) {
        errorStr = null;
        int point1 = convertPoint(x1, y1);
        int point2 = convertPoint(x2, y2);
        
        if (verifyPoint(point1)) {
            if (verifyPoint(point2)) {
                addLine(point1, point2);
                printCanvas();
            }
            else {
                errorStr = "Point 2 is out of bounds.";
                writeToFile(errorStr);
            }
        }
        else {
            errorStr = "Point 1 is out of bounds.";
            writeToFile(errorStr);
        }
    }
    
    private void addLine (int point1, int point2) {
        //If point2 is less, switch the points
        if (point2 < point1) {
            int temp = point2;
            point2 = point1;
            point1 = temp;
        }
        
        //If points are separated by a multiple of w (vertical)
        if ((point2 - point1)%w == 0) { 
            for (i=point1; i<=point2; i+=w) {
                canvas.put(i, 'X');
            }
        }
        //Else if points are on the same row (difference of less than width)
        else if ((point2-point1) < width) {
            for (i=point1; i<=point2; i++) {
                canvas.put(i, 'X');
            }   
        }
        else {
            errorStr = "Line must be horizontal or vertical.";
            writeToFile(errorStr);
        }
            
    }
    
    public void addRectangle(int x1, int y1, int x2, int y2) {
        errorStr = null;
        int rowDiff = (y2-y1)*w; //Used to determine remaining rectangle corners
        int point1 = convertPoint(x1, y1);
        int point2 = convertPoint(x2, y2);
        
        if (verifyPoint(point1)) {
            if (verifyPoint(point2)) {
                addLine(point1, point1+rowDiff);
                addLine(point1, point2-rowDiff);
                addLine(point2, point1+rowDiff);
                addLine(point2, point2-rowDiff);
                printCanvas();
            }
            else {
                errorStr = "Point 2 is out of bounds.";
                writeToFile(errorStr);
            }
        }
        else {
            errorStr = "Point 1 is out of bounds.";
            writeToFile(errorStr);
        }
    }
    
    public void bucketFill(int x, int y, char c) {
        errorStr = null;
        newChar = c;
        int start = convertPoint(x,y);
        if (canvas.containsKey(start)){
            curChar = canvas.get(start);
            curCharEmpty = false;
        }
        else
            curCharEmpty = true;
        
        try {
            if (verifyPoint(start)){
                fill(start);
                printCanvas();
            }
            else {
                errorStr = "Point is out of bounds.";
                writeToFile(errorStr);
            }
                
        } catch(StackOverflowError e) {
            errorStr = "Fill area too large.";
            writeToFile(errorStr);
        } catch(NullPointerException b) {
            System.out.println(b);
        }
    }
    
    private void fill(int key) {
        if ((!canvas.containsKey(key) && curCharEmpty)
            || canvas.containsKey(key) && canvas.get(key) == curChar) {
                canvas.put(key, newChar);
                if (verifyPoint(key-1))
                    fill(key-1);
                if (verifyPoint(key+1))
                    fill(key+1);
                if (verifyPoint(key+w))
                    fill(key+w);
                if (verifyPoint(key-w))
                    fill(key-w);
        }
    }
    
    private boolean verifyPoint (int point) {
        return point > w 
                && point < sizew
                && point % w != 0
                && (point-1)% w != 0;
    }
    
    private int convertPoint (int x, int y) {
        return (width*y)+(2*y)+x+1; //simplified from (width*y)+(w+1)+(y+1)+(y-1)
    }
    
    public void closeFile() {
        try {
            System.out.println("Finished.");
            output.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public boolean hasCanvas() {
        return hasCanvas;
    }
    
    public void writeToFile (String str) {
        try {
            output.write(str);
            output.write("\r\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public String getErrorStr() {
        return errorStr;
    }
}
