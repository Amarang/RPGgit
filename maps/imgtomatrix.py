from PIL import Image
#######################
#######################
MAPNUMBER = 3  ########
#######################
#######################


imgfile = Image.open("map" + str(MAPNUMBER) + ".png")

pic = imgfile.load()

xSize, ySize = imgfile.size
colorls = []

out = open("file" + str(MAPNUMBER) + ".txt", 'w')

alphabet = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

def removeDuplicates(seq):
   checked = []
   for e in seq:
       if e not in checked:
           checked.append(e)
   return checked

for y in range(ySize):
	for x in range(xSize):
		colorls.append(pic[x,y])
		
colorls = removeDuplicates(colorls)

for y in range(ySize):
	for x in range(xSize):
		if (x == xSize-1):
			#print colorls.index(pic[x,y])
			out.write(str(alphabet[colorls.index(pic[x,y])]))
			if(not (y == xSize-1)): out.write("\n")
		else:
			#print colorls.index(pic[x,y]),
			out.write(str(alphabet[colorls.index(pic[x,y])]) + " ")
