# Final-Project-2017s
Features:
- Takes a text file with rsID, p value, chromosome, and base pair position and makes a graph
- Can be implemented as a Processing library (calling public functions from the package I made)
- Can color different points (points with same chromosome)
- Can draw vertical lines along ticks
- Can draw horizontal lines
- Can resize points
- Scales the image based on max x & y values

Bugs:
- Labeling ticks (& labels in general) don't work
- Ran into some problems unifying x & y axes into one Axis type
- Problems with scaling vs keeping literal x/y values
- Can't use a text file with only rsIDs and p values
