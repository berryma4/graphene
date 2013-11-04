/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.math.*;
/**
 *
 * @author carcassi
 */
public class IntensityGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{

    private ValueColorScheme colorScheme;

    public IntensityGraph2DRenderer(int imageWidth, int imageHeight) {
        super(imageWidth, imageHeight); 
    }

    public IntensityGraph2DRenderer() {
        this(300, 200);
    }
      
    public void update(IntensityGraph2DRendererUpdate update) {
        super.update(update);    
    }

    //Working on: Making the drawing of cells more generic / able to draw with large quantities of data.
    public void draw(Graphics2D g, Cell2DDataset data) {
        //Use super class to draw basics of graph.
        this.g = g;
        calculateRanges(data.getXRange(), data.getYRange());
        drawBackground();
        calculateLabels();
        calculateGraphArea();        
        drawGraphArea();
        
        //Set color scheme
        colorScheme = ValueColorSchemes.grayScale(data.getStatistics());

        double xStartGraph = super.xPlotCoordStart;
        double yEndGraph = super.yPlotCoordEnd;

        //Get graph width and height from super class.
        double xWidthTotal = super.xPlotCoordWidth;
        double yHeightTotal = super.yPlotCoordHeight;
        
        //Get range of both x and y coordinates.
        double xRange = data.getXBoundaries().getInt(data.getXCount()) - data.getXBoundaries().getInt(0);
        double yRange = data.getYBoundaries().getInt(data.getYCount()) - data.getYBoundaries().getInt(0);
        
        //Set width and height of cells to be colored in by finding the width and height for the first cell.
        double cellHeight = (yHeightTotal)/data.getYCount();
        double cellWidth = (xWidthTotal)/data.getXCount();
        
        //Draw the cells of data by filling rectangles, if the width and height are greater than one pixel.
        if(cellWidth >= 1 && cellHeight >= 1){
            drawRectangles(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
        }
        
        //Draw graph when cell width or height is smaller than one pixel.
        if(cellWidth < 1 || cellHeight < 1){
            if(cellHeight > 1){
                System.out.println("Case: Small X");
                drawRectanglesSmallX(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            if(cellWidth > 1){
                System.out.println("Case: Small Y");
                drawRectanglesSmallY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            if(cellWidth < 1 && cellHeight < 1){
                System.out.println("Case: Small X and Y");
                drawRectanglesSmallXAndY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            
        }
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new IntensityGraph2DRendererUpdate();
    }
    
    public void drawRectangles(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY = 0;
        int countX;
        double yPosition = yEndGraph;
        int yPositionInt = (int)yEndGraph;
        while (countY < data.getYCount()){
                countX = 0;
                double xPosition = xStartGraph;
                int xPositionInt = (int)xStartGraph;
                while (countX < data.getXCount()){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    Rectangle2D.Double currentRectangle = new Rectangle2D.Double(xPositionInt, yPositionInt-(int)cellHeight, (int)cellWidth+1, (int)cellHeight+1);
                    g.fill(currentRectangle);
                    xPosition = xPosition + cellWidth;
                    xPositionInt = (int)xPosition;
                    countX++;
                }
                yPosition = yPosition - cellHeight;
                yPositionInt = (int)yPosition;
                countY++;
            }
    }

    //Draws rectangles for the case when there are more x values than pixels, but no more y values than pixels.
    //Uses the first value within each pixel to choose a color.
    public void drawRectanglesSmallX(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY = 0;
        int countX;
        double yPosition = yEndGraph;
        int yPositionInt = (int)(Math.ceil(yEndGraph));
        while (countY < data.getYCount()-1){
                countX = 0;
                double xPosition = xStartGraph;
                int xPositionInt = (int)xStartGraph;
                while (countX < data.getXCount()-1){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    double xPositionInitial = xPosition;
                    int xPositionInitialInt = xPositionInt;
                    while(xPosition <= (xPositionInitialInt +1) && countX < data.getXCount()-1){
                        xPosition += cellWidth;
                        if(countX<data.getXCount()-1)
                            countX+=1;
                    }
                    xPositionInt+=1;
                    Rectangle2D.Double rect;
                    if((yPositionInt-(int)cellHeight) >= (yPosition-cellHeight+1) )
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt-((int)cellHeight),1,(int)cellHeight+1);
                    else
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt-((int)cellHeight),1,(int)cellHeight+1);
                    g.fill(rect);
                }
                yPosition = yPosition - cellHeight;
                yPositionInt = (int)(Math.ceil(yPosition));
                countY++;
            }
    }
//Same logic as drawRectanglesSmallX, but for when there are more y values than pixels.
public void drawRectanglesSmallY(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY;
        int countX = 0;
        double xPosition = xStartGraph;
        int xPositionInt = (int)xStartGraph;
        while (countX < data.getXCount()-1){
                countY = 0;
                double yPosition = yEndGraph;
                int yPositionInt = (int)yEndGraph;
                while (countY < data.getYCount()-1){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    int yPositionInitialInt = yPositionInt;
                    while(yPosition >= yPositionInitialInt - 1 && countY < data.getYCount()-1){
                        yPosition-= cellHeight;
                        if(countY<data.getYCount()-1)
                            countY+=1;
                    }
                    yPositionInt-=1;
                    Rectangle2D.Double rect;
                    if(xPositionInt+(int)cellWidth <= xPosition+cellWidth-1)
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt,(int)cellWidth+1,1);
                    else
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt,(int)cellWidth+1,1);
                    g.fill(rect);
                }
                xPosition = xPosition + cellWidth;
                xPositionInt = (int)xPosition;
                countX++;
            }
    }
//Draws for the case when there are both more x values and y values than pixels.
//Picks the value at approximately the top left of each pixel to set color. Skips other values within the pixel. 
public void drawRectanglesSmallXAndY(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
    int countY = 0;
    int countX;
    int yPositionInt = (int)yEndGraph;
    int xPositionInt;
    double yPosition = yEndGraph;
    double xPosition;
    while (countY < data.getYCount()-1){
        countX = 0;
        xPosition = xStartGraph;
        xPositionInt = (int) xStartGraph;
        int yPositionInitialInt = yPositionInt;
        while(yPosition >= yPositionInitialInt - 1 && countY < data.getYCount()-1){
            yPosition-= cellHeight;
            if(countY<data.getYCount()-1)
                countY+=1;
        }
        yPositionInt-=1;
        while (countX < data.getXCount()-1){
            g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
            int xPositionInitialInt = xPositionInt;
            while(xPosition <= xPositionInitialInt +1 && countX < data.getXCount()-1){
                xPosition += cellWidth;
                if(countX<data.getXCount()-1)
                    countX++;
            }
            Rectangle2D.Double rect = new Rectangle2D.Double(xPositionInt,yPositionInt,1,1);
            g.fill(rect);
            xPositionInt+=1;
        }

    }
}
}