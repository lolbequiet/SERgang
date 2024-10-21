package Engine;

import GameObject.ImageEffect;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHandler {
    private Graphics2D g;

    public Graphics2D getGraphics() {
        return g;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    // Method to draw an image
    public void drawImage(BufferedImage image, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void drawImage(BufferedImage image, int x, int y, int width, int height, ImageEffect imageEffect) {
        switch (imageEffect) {
            case NONE -> drawImage(image, x, y, width, height);
            case FLIP_HORIZONTAL -> g.drawImage(image, x + width, y, -width, height, null);
            case FLIP_VERTICAL -> g.drawImage(image, x, y + height, width, -height, null);
            case FLIP_H_AND_V -> g.drawImage(image, x + width, y + height, -width, -height, null);
        }
    }

    // Draw rectangle with integer parameters
    public void drawRectangle(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color, int borderThickness) {
        g.setStroke(new BasicStroke(borderThickness));
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    // Overloaded method to draw rectangle with float parameters
    public void drawRectangle(float x, float y, float width, float height, Color color) {
        g.setColor(color);
        g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
    }

    // Draw filled rectangle with integer parameters
    public void drawFilledRectangle(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    // Overloaded method to draw filled rectangle with float parameters
    public void drawFilledRectangle(float x, float y, float width, float height, Color color) {
        g.setColor(color);
        g.fillRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
    }

    public void drawFilledRectangleWithBorder(int x, int y, int width, int height, Color fillColor, Color borderColor, int borderThickness) {
        drawFilledRectangle(x, y, width, height, fillColor);
        drawRectangle(x, y, width, height, borderColor, borderThickness);
    }

    public void drawString(String text, int x, int y, Font font, Color color) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public void drawStringWithOutline(String text, int x, int y, Font font, Color textColor, Color outlineColor, float outlineThickness) {
        // Save original settings
        Color originalColor = g.getColor();
        Stroke originalStroke = g.getStroke();
        RenderingHints originalHints = g.getRenderingHints();

        // Set stroke and anti-aliasing
        g.setStroke(new BasicStroke(outlineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Create glyph vector and transform shape
        GlyphVector glyphVector = font.createGlyphVector(g.getFontRenderContext(), text);
        Shape textShape = glyphVector.getOutline();
        AffineTransform at = new AffineTransform();
        at.setToTranslation(Math.round(x), Math.round(y));
        textShape = at.createTransformedShape(textShape);

        // Draw outline
        g.setColor(outlineColor);
        g.draw(textShape);

        // Fill shape with text color
        g.setColor(textColor);
        g.fill(textShape);

        // Restore original settings
        g.setColor(originalColor);
        g.setStroke(originalStroke);
        g.setRenderingHints(originalHints);
    }

    public int getHeight() {
        // Placeholder for future implementation
        throw new UnsupportedOperationException("Unimplemented method 'getHeight'");
    }

    public Object getFontMetrics() {
        // Placeholder for future implementation
        throw new UnsupportedOperationException("Unimplemented method 'getFontMetrics'");
    }
}
