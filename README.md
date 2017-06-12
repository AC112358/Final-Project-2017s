# Final-Project-2017s
Features:
- Takes a text file with rsID, p value, chromosome, and base pair position and makes a graph
- Can be implemented as a Processing library (calling public functions from the package I made)
- Can color different points (points with same chromosome)
- Can draw vertical lines along ticks
- Can draw horizontal lines
- Can resize points
- Scales the image based on max x & y values
- The labels lower if there's not enough space, draws a line to the axis if it doesn't go through neighboring text
- Can display the y-value (-log base 10 of p) next to a horizontal line made by the user
- Can mark specific points with a triangle (default color black but the user can change the triangle color)
- Can reject p-values with a specified probability
- The program will automatically remove data that contains impossible information (e.g., negative p value) and the user can specify if they want to be told by the program which data was incorrect


Bugs/Issues:
- Ran into some problems unifying x & y axes into one Axis type
- Problems with scaling vs keeping literal x/y values
- Can't use a text file with only rsIDs and p values
- Basically only can display a specific type of inverted Manhattan plot
- I should've added more public methods, like change the size of the title
- Title is displayed strangely (not in the middle)
- Exceptions (FileNotFoundException) can be displayed; however, I think this is OK since it's a Processing library & the user would want to see that output

Potential added features:
- Make more user-friendly


General description: This project is a Processing library that allows the user to create inverted Manhattan plots. It was made specifically for genetic studies. 
A Manhattan Plot displays the location of a gene and the -log base 10 of its p value. The p value is the probability that the null hypothesis (the gene & the trait being studied are not correlated) is true. The smaller it is, the more correlated the gene & trait are. 
I was influenced/inspired by the Grafica Processing library (I based some of my class names/structure on it) & Assocplots (a Python library) (I tried to include features & style in Assocplots) and used a GitHub Processing library setup tutorial.

HOW TO USE (NOTE: I used Java 8): 
1. Get the invertedmanhattan-1 folder: invertedmanhattan --> distribution --> invertedmanhattan-1 (copy the entire folder)

2. Move the folder to your Processing library folder: Documents --> Processing --> libraries: in the libraries folder, copy the  invertedmanhattan-1 folder

3. Open Processing (restart it if it was already open). 
Sketch (in the top bar next to "File" & "Edit") --> Import Library --> Contributed (in gray near the bottom) --> invertedmanhattan-1 --> 
 Inverted Manhattan Plot
  
4. Call my methods (see TestingPlot.pde in the folder TestingPlot in Final-Project-2017s for an example)

IMPORTANT: You can go to invertedmanhattan --> src --> library to see all the Java files used in the library. There are others I made (HelloExample is not one of them; it came with the tutorial project I downloaded and I kept it in case I needed reference). The other ones I made were going to be used to get the location of a given gene using its rsID, but that didn't end up working out. 
I tried using an API and uploading the reference file to a HashTable and later to a database. None of these approaches worked. I researched a bit on using Entrez API, but it seems easier to use a pre-existing program to first get the locations of genes. I think there are easier tools on Python.

I was away for a family event over the weekend, and I only really had free time on the train back (I studied for Regents on the way there). That's why I unevenly worked over the weekend.
