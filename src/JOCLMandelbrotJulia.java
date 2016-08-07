/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 */

import static java.lang.Math.sqrt;
import static org.jocl.CL.*;

import java.awt.*;

import javax.swing.*;

import org.jocl.*;

/**
 * A class that uses a simple OpenCL kernel to compute the
 * Mandelbrot set and displays it in an image
 */
public class JOCLMandelbrotJulia {
    /**
     * Entry point for this sample.
     *
     * @param args is an integer number of Mandelbrot/Julia sets to display
     */
    public static void main(String args[]) {
        // Argument taken as the number of sets to produce
        int numberOfSets = Integer.parseInt(args[0]);
        // If number given is not a power of two, round it up to the nearest power of two
        for (int i = 1; ; ) {
            if (i == numberOfSets) {
                break;
            }
            i *= 2;
            if (i > numberOfSets) {
                numberOfSets = i;
                break;
            }
        }
        int finalNumberOfSets = numberOfSets;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JOCLMandelbrotJulia(300, 300, finalNumberOfSets);
            }
        });
    }

    /**
     * The component which is used for rendering the image
     */
    private JComponent imageComponent;

    /**
     * The OpenCL context
     */
    private cl_context context;

    /**
     * The OpenCL command queue
     */
    private cl_command_queue commandQueue;


    /**
     * Creates the JOCLMandelbrotJulia sample with the given
     * width and height
     */
    public JOCLMandelbrotJulia(int width, int height, int numberOfSets) {

        // Create the context and command queue
        initCL();

        // Calculate the number of sets displayed horizontally and vertically
        int heightCount = (int) sqrt(numberOfSets);
        // If widthCount is not a power of two, round it up to the nearest power of two
        for (int i = 1; ; ) {
            if (i == heightCount) {
                break;
            }
            i *= 2;
            if (i > heightCount) {
                heightCount = i;
                break;
            }
        }
        int widthCount = numberOfSets / heightCount;

        // Create the main frame
        JFrame frame = new JFrame("Assignment - Mandelbrot & Julia Sets");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(heightCount, widthCount));

        // Create new Madelbrot/Julia sets and add them to the main frame
        for (int i = 0; i < numberOfSets; i++) {
            Fractal nextSet = new Fractal(width, height, context,
                    commandQueue, i);
            imageComponent = nextSet.CreateImageComponent();
            imageComponent.setPreferredSize(new Dimension(width, height));
            frame.add(imageComponent);
        }

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initialize OpenCL: Create the context, the command queue
     * and the kernel.
     */
    private void initCL() {
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        commandQueue =
                clCreateCommandQueue(context, device, 0, null);
    }


}


// mpi, map reduce, scatter gather
// types of locks, monitors, semaphores, principles not syntax, read-write locks, synchronisation, reentrant lock
// processes vs threads
// threadpools