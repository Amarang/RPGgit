from PIL import Image
import pygame, sys, time, math, random
from pygame.locals import *

imgCount = 0
currTile = 0
tileImgSize = 20 #physical size-don't change
tileSize = 10 #calculational size
brushSize = 1
maxBrushSize = 20
fps = 50
tiles = []
mapls = []
width, height = 1024, 768
xtiles, ytiles = width//tileSize, height//tileSize
print "initiating", xtiles, "by", ytiles, "array"
	
def roundCoords(c, step):
	return (c[0]//step * step, c[1]//step * step)
	
def addSteps(c, i, j, step):
	return (c[0] + i*step, c[1] + j*step)

def updateCurrTile(dir):
	global currTile
	currTile += dir
	if(currTile < 0): currTile = imgCount-1
	if(currTile >= imgCount): currTile = 0
	print currTile
	
def updateBrushSize(dir):
	global brushSize
	brushSize += dir
	if(brushSize < 1): brushSize = 1
	if(brushSize >= maxBrushSize): brushSize = maxBrushSize
	print brushSize
	
def writeToFile(ls):
	alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '.']
	xSize = len(ls[0])
	ySize = len(ls)
	xMin, yMin = xSize, ySize
	print "found", xSize, "by", ySize, "array"
	
	#pass 1 -- check for -1 and cut array
	#essentially this only retains what you drew on
	for y in range(ySize):
		for x in range(xSize):
			val = ls[y][x]
			x0 = ls[y][0]
			if(val == -1):
				if(x < xMin and not(x0==-1)): xMin = x
			if(x0 == -1 and y < yMin): yMin = y
				
	print "clipped to", xMin, "by", yMin
	if(xMin == 0 or yMin == 0):
		print "Failed due to clipping to zero"
		return -1
	
	#pass 2 -- write to file
	filename = str(xMin)+'x'+str(yMin)+'.txt'
	out = open(filename, 'w')
	for y in range(yMin):
		for x in range(xMin):
			if (x == xMin-1):
				out.write(str(alphabet[ls[y][x]]))
				if(not (y == yMin-1)): out.write("\n")
			else:
				out.write(str(alphabet[ls[y][x]]) + " ")
	print "SAVED", filename

basedir = "tiles/"
try:
	imgfile = Image.open(basedir+"tiles.png")
except:
	basedir = "../images/tiles/"
	imgfile = Image.open(basedir+"tiles.png")
#pic = imgfile.load()
xSize, ySize = imgfile.size
for yt in range(ySize/tileImgSize):
	for xt in range(xSize/tileImgSize):
		x,y = tileImgSize*xt,tileImgSize*yt
		#imgfile.crop( (x,y, x+tileImgSize,y+tileImgSize) ) .save("tiles/" + str(imgCount) + ".png")
		#timg = pygame.image.load("tiles/"+str(imgCount)+".png")
		timg = pygame.image.load(basedir+str(imgCount)+".png")
		timg = pygame.transform.scale(timg, (tileSize, tileSize) )
		tiles.append(timg)
		imgCount += 1


for j in range(ytiles):
	mapls.append([])
	for i in range(xtiles):
		mapls[j].append(-1)
		

black = (0,0,0)
white = (255,255,255)

pygame.init()
screen = pygame.display.set_mode((width, height))
screen.fill(black)
clock=pygame.time.Clock()
font = pygame.font.Font(None, 24)

mousedownl = False	
mousedownr = False	

mpos = (-100,-100)



while 1:
	update = False
	
	for event in pygame.event.get():
		if event.type == QUIT:
			pygame.quit()
			writeToFile(mapls)
			sys.exit()
		if event.type == pygame.MOUSEBUTTONUP:
			update = True
			if(event.button==1):
				mousedownl = False
			if(event.button==3):
				mousedownr = False
		if event.type == pygame.MOUSEBUTTONDOWN:
			update = True
			mpos = (-100,-100)
			if(event.button==1):
				print "leftclick"
				mousedownl = True
			if(event.button==3):
				print "rightclick"
				mousedownr = True
			if(event.button==4):
				print "scrollup"
				updateCurrTile(1)
				#if(mousedownr): updateBrushSize(1)
			if(event.button==5):
				print "scrolldown"
				updateCurrTile(-1)
				#if(mousedownr): updateBrushSize(-1)
		if(event.type == pygame.KEYUP):
			update = True
			if(event.key == pygame.K_UP):
				print "up key"
				updateBrushSize(1)
			if(event.key == pygame.K_DOWN):
				print "down key"
				updateBrushSize(-1)
			if(event.key == pygame.K_s):
				print "saving"
				writeToFile(mapls)
				
				
		if event.type == MOUSEMOTION:
			#print "test"
			update = True
			if(mousedownl): mpos = pygame.mouse.get_pos()

	currTileImg  = tiles[currTile]
	
	#print mpos
	#repaint over bottom right corner
	pygame.draw.rect(screen, black, (width-7*tileImgSize, height-2*tileImgSize, 7*tileImgSize, 2*tileImgSize))
	
	
	if(mousedownl):
		for y in range(brushSize):
			for x in range(brushSize):
				coords = addSteps(roundCoords(mpos,tileSize),x,y, tileSize)
				#print coords[0]//tileSize,coords[1]//tileSize
				screen.blit(currTileImg, coords)
				if(not (mpos == (-100,-100))):
					try:
						mapls[coords[1]//tileSize][coords[0]//tileSize] = currTile
					except: pass
				
		

	screen.blit(pygame.transform.scale(currTileImg,(tileImgSize,tileImgSize)), (width-tileImgSize-2, height-tileImgSize-2))

	
	screen.blit(font.render(str(currTile), True, white), (width-tileImgSize*2-6, height-tileImgSize+2))
	
	screen.blit(font.render(str(brushSize), True, white), (width-tileImgSize*5-6, height-tileImgSize+2))
	
	clock.tick(fps)
	if(update):
		pygame.display.update()