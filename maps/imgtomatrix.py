from PIL import Image
#######################
#######################
MAPNUMBER = 1  ########
#######################
#######################


imgfile = Image.open("map" + str(MAPNUMBER) + ".png")

pic = imgfile.load()

xSize, ySize = imgfile.size
colorls = []

out = open("file" + str(MAPNUMBER) + ".txt", 'w')

alphabet = ['AAA', 'BAA', 'CAA', 'DAA', 'EAA', 'FAA', 'GAA', 'HAA', 'IAA', 'JAA', 'KAA', 'LAA', 'MAA', 'NAA', 'OAA', 'PAA', 'QAA', 'RAA', 'SAA', 'TAA', 'UAA', 'VAA', 'WAA', 'XAA', 'YAA', 'ZAA', '[AA', '\\AA', ']AA', '^AA', '_AA', '`AA', 'aAA', 'bAA', 'cAA', 'dAA', 'eAA', 'fAA', 'gAA', 'hAA', 'iAA', 'jAA', 'kAA', 'lAA', 'mAA', 'nAA', 'oAA', 'pAA', 'qAA', 'rAA', 'sAA', 'tAA', 'uAA', 'vAA', 'wAA', 'xAA', 'yAA', 'zAA']

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
