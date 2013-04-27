from PIL import Image
imgfile = Image.open("tiles.png")

pic = imgfile.load()

xSize, ySize = imgfile.size
colorls = []

imgCount = 0
size = 20
for yt in range(ySize/size):
	for xt in range(xSize/size):
		x,y = size*xt,size*yt

		print (x,y), (x+size,y+size)
		imgfile.crop( (x,y, x+size,y+size) ).save(str(imgCount) + ".png")
		imgCount += 1
