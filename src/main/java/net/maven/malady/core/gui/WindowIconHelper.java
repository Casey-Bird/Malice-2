package net.maven.malady.core.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class WindowIconHelper {

    public static void setWindowIcon(long windowHandle, String iconPath) {
        try (InputStream is = WindowIconHelper.class.getResourceAsStream(iconPath)) {
            if (is == null) {
                System.err.println("Icon not found at: " + iconPath);
                return;
            }

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                System.err.println("Failed to load icon: " + iconPath);
                return;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            // Convert BufferedImage to RGBA byte buffer
            int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
            ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                    buffer.put((byte) (pixel & 0xFF));         // B
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
            }
            buffer.flip();

            // Set the icon
            GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
            GLFWImage icon = iconBuffer.get(0);
            icon.set(width, height, buffer);
            GLFW.glfwSetWindowIcon(windowHandle, iconBuffer);

            // Clean up
            MemoryUtil.memFree(buffer);
            iconBuffer.free();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}