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
- Exceptions (FileNotFoundException) can be displayed; hower, I think this is OK since it's a Processing library & the user would want to see that output

Potential added features:
- Make more user-friendly


General description: This project is a Processing library that allows the user to create inverted Manhattan plots. It was made specifically for genetic studies. 
A Manhattan Plot displays the location of a gene and the -log base 10 of its p value. The p value is the probability that the null hypothesis (the gene & the trait being studied are not correlated) is true. The smaller it is, the more correlated the gene & trait are. 
I was influenced/inspired by the Grafica Processing library & Assocplots (a Python library) and used a GitHub Processing library setup tutorial.

HOW TO USE: 
1. Get the invertedmanhattan-1 folder: invertedmanhattandistribution --> invertedmanhattan-1
  The file invertedmanhattan.jar should be there.

2. Move the JAR to your Processing library folder: Documents --> Processing --> libraries
  (Documents\Processing\libraries)
  
  
