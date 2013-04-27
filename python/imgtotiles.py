from PIL import Image
imgfile = Image.open("all.png")

pic = imgfile.load()

xSize, ySize = imgfile.size
colorls = []

out = open('file.txt', 'w')

alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

imgCount = 1
size = 16
for yt in range(ySize/size):
	for xt in range(xSize/size):
		x,y = size*xt,size*yt
		print (x,y), (x+size,y+size)
		imgfile.crop( (x,y, x+size,y+size) ).save(str(imgCount) + ".png")
		imgCount += 1
