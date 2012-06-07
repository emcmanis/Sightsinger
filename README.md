Sightsinger
===========

a program to practice sightsinging / digital pitchpipe

The goal: to create a program that helps you practice sight-singing.

Intermediate steps: Recognize pitches, record pitches and display pitch over time, recognize discrete notes,
compare with previously recorded values.

Current status: The first step (creating a working fft) works! The program will display a power spectrum for you to see the pitches of your voice and/or anything else in the area.
To compile, run (in the Sightsinger directory):   

     javac -cp ./:jtransforms-2.4.jar DataFrame.java    
     java -cp ./:jtransforms-2.4.jar DataFrame
 
I wrote and checked this on Ubuntu. It may or may not work on Windows. The microphone does not (appear to) work on OS X.

Known issues:

* The "start" button cannot be pressed after it has been stopped once
* Pressing any button twice throws exceptions.