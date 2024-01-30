# Java Image Sharpening Application
## Project for the Java course
In this application I have made use of all object oriented programming concepts (inheritance, polymorphism, abstractization, encapsulation), threads, threads communication and image/file manipulation concepts in order to apply a convolution kernel on an image, resulting in a well structured application.

It is structured in two packages:
 - packWork - in which the mainclass is found, there the components which are tasked with the image processing are created
 - packTest - which contains all the other classes.

The main concept of the application is : 3 threads are started ( 1 is tasked with reading the image file, 2 is tasked with processing the said image and 3 is tasked with writing the result into a new file ). The first thread communicates the information to the second using a buffer object with syncronized methods. The second thread applies the convolution kernel in a primitive fashion and gives the information to the third thread through Pipes. Finally the third thread will write the result to a new image file. The whole process is timed using a time interface to get the respective running time of each task.

A more in depth information please check the [Documentation](https://github.com/zuch3e/javaImgSharp/blob/main/Executable/Documentatie.pdf) and the [code comments](https://github.com/zuch3e/javaImgSharp/tree/main/src).
