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


Bugs/Issues:
- Doesn't display x-axis name (not sure if bug)
- Ran into some problems unifying x & y axes into one Axis type
- Problems with scaling vs keeping literal x/y values
- Can't use a text file with only rsIDs and p values
- Basically only can display a specific type of inverted Manhattan plot

Potential added features:
- Make more user-friendly
- Option to display height of horizontal line on x-axis
