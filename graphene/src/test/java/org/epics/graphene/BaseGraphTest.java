/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import java.awt.Font;
import java.awt.Color;

/**
 * TODO: make the image follow the following format: $prefix.base.$testname.png
 *
 * @author Jiakung
 */
public abstract class BaseGraphTest<T extends Graph2DRendererUpdate<T>, S extends Graph2DRenderer<T>> {

    // TODO: make final
    private String resultPrefix;

    public BaseGraphTest(String resultPrefix) {
        this.resultPrefix = resultPrefix;
    }
    
    public abstract S createRenderer();

    public abstract BufferedImage draw(S renderer);

    @Test
    public void rightMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightMargin(10));
        // TODO: fix capitalization
        ImageAssert.compareImages(resultPrefix + "RightMargin", draw(renderer));
    }

    @Test
    public void leftMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().leftMargin(10));
        ImageAssert.compareImages(resultPrefix + "LeftMargin", draw(renderer));
    }
    
    @Test
    public void backgroundColor() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().backgroundColor(Color.BLUE));
        ImageAssert.compareImages(resultPrefix + "backgroundColor", draw(renderer));
    }
    
    @Test
    public void labelColor() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().labelColor(Color.GREEN));
        ImageAssert.compareImages(resultPrefix + "labelColor", draw(renderer));
    }
    
    @Test
    public void labelFont() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().labelFont(FontUtil.getLiberationSansRegular().deriveFont(Font.BOLD, 12)));
        ImageAssert.compareImages(resultPrefix + "labelFont", draw(renderer));
    }
    
    @Test
    public void bottomMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().bottomMargin(10));
        ImageAssert.compareImages(resultPrefix + "BottomMargin", draw(renderer));
    }
    
    @Test
    public void topMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().topMargin(10));
        ImageAssert.compareImages(resultPrefix + "TopMargin", draw(renderer));
    }
    
    @Test
    public void allMargins() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightMargin(10));
        renderer.update(renderer.newUpdate().leftMargin(10));
        renderer.update(renderer.newUpdate().bottomMargin(10));
        renderer.update(renderer.newUpdate().topMargin(10));
        ImageAssert.compareImages(resultPrefix + "AllMargins", draw(renderer));
    }
    @Test
    public void xLabelMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().xLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "xLabelMargin", draw(renderer));
    }
    
    @Test
    public void yLabelMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().yLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "yLabelMargin", draw(renderer));
    }
    
    @Test
    public void allLabelMargins() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().xLabelMargin(10));
        renderer.update(renderer.newUpdate().yLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "allLabelMargins", draw(renderer));
    }
    
    @Test
    public void bottomAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().bottomAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "BottomAreaMargin", draw(renderer));
    }
    
    @Test
    public void topAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().topAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "TopAreaMargin", draw(renderer));
    }
    
    @Test
    public void leftAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().leftAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "leftAreaMargin", draw(renderer));
    }
    
    @Test
    public void xrightAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "RightAreaMargin", draw(renderer));
    }
}
