/*
Drawing Tool Coding Challenge for Huge Inc.
Completed by Kelly Anlas 3/3/2016
*/
package DrawingToolPackage;

/*
This main class creates an instance of DrawingCanvas, interprets input
from the input.txt file by means of regular expressions, and calls the
appropriate methods. When the end of file is reached, it closes the output.txt
file and ends the program.
*/
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class DrawingCanvasMain {
    public static void main (String args[]) throws IOException
    {
        String input;
        DrawingCanvas canvas = new DrawingCanvas();           
        
        Scanner in = new Scanner (new FileInputStream("input.txt"));
            
        System.out.println("Drawing...");
        while (in.hasNextLine()) {
            input = in.nextLine();
            String[] piece = input.split(" "); 
            if (input.matches("^(C\\s\\d+\\s\\d+)")) {
                canvas.createCanvas(toInt(piece[1]), 
                                toInt(piece[2]));
            }
            else if (!canvas.hasCanvas()) {
                canvas.writeToFile("Needs canvas.");
            }
            else if (input.matches("^(L\\s\\d+\\s\\d+\\s\\d+\\s\\d+)")) {
                canvas.addLine(toInt(piece[1]),
                        toInt(piece[2]),
                        toInt(piece[3]),
                        toInt(piece[4]));
            }
            else if (input.matches("^(R\\s\\d+\\s\\d+\\s\\d+\\s\\d+)")) {
                canvas.addRectangle(toInt(piece[1]),
                        toInt(piece[2]),
                        toInt(piece[3]),
                        toInt(piece[4]));
            }
            else if (input.matches("^(B\\s\\d+\\s\\d+\\s\\S)")) {
                canvas.bucketFill(toInt(piece[1]),
                        toInt(piece[2]),
                        piece[3].charAt(0));
            }
            else {
                canvas.writeToFile("Invalid input."); 
            }
        }
        canvas.closeFile();
    }
    
    public static int toInt(String str) {
        return Integer.parseInt(str);
    }
}
       

