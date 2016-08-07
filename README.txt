This program must be run from the command line.

TO COMPILE:
Run the following command from within the src file:
javac JOCLMandelbrotJulia.java -classpath .:../JOCL-0.1.9.jar

TO RUN:
Run the following command from within the source file, with "x" replaced with the number
 of Mandelbrot/Julia sets to display (preferably a power of 2, but if not it will
 be rounded up to the nearest power of 2):
java -cp .:../JOCL-0.1.9.jar JOCLMandelbrotJulia x