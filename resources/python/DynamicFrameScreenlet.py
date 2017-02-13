#!/usr/bin/env python

# This application is released under the GNU General Public License 
# v3 (or, at your option, any later version). You can find the full 
# text of the license under http://www.gnu.org/licenses/gpl.txt. 
# By using, editing and/or distributing this software you agree to 
# the terms and conditions of this license. 
# Thank you for using free software!

#Copyright (c) 2013.

#Juergen Key. Alle Rechte vorbehalten.


#DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE 
#JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE 
#UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER 
#SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR 
#ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, 
#ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM 
#VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER 
#NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER 
#GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER 
#VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER 
#UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM 
#WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, 
#WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.


import screenlets
from screenlets.options import ImageOption, IntOption, FloatOption, ColorOption, StringOption, BoolOption
import gtk
import cairo
import math
try:
	import Image
except:
	print 'Error - Please install python image module'
import os
import commands
import random
import gobject

class DynamicFrameScreenlet (screenlets.Screenlet):
	"""A Screenlet that displays an image within a themeable frame. Currently
	this only displays PNG-images. You can add new images by drag&amp;drop them
	into the screenlet's window area."""
	
	# --------------------------------------------------------------------------
	# meta-info, options
	# --------------------------------------------------------------------------
	
	__name__		= 'DynamicFrameScreenlet'
	__version__		= '0.1'
	__author__		= 'ELBOSSO'
	__desc__		= __doc__
	
	# attributes
	__image = None
	__timeout = None
	__imgs = []
	# editable options
	image_filename 	= ''
	image_scale		= 0.18
	image_offset_x	= 16
	image_offset_y	= 24
	curve = 15
	screen_width = gtk.gdk.screen_width() /10
	screen_height = gtk.gdk.screen_height()/10
	rotate = 0
	min_rotate=-20
	max_rotate=20
	random_rotate=False
	maxwidth = 320
	maxheight = 256
	color_back = (0.5,0.5,0.5,0.7)
	home = commands.getoutput("echo $HOME")
	folders = '/home/elbosso/Pictures'
	use_types = ['.jpg', '.gif', '.png']
	update_interval = 60
	x_padding=0
	y_padding=0
	adjust_position= False
	slide=True
	recursive=True
	shown=False
	# --------------------------------------------------------------------------
	# constructor and internals
	# --------------------------------------------------------------------------
	
	def __init__ (self, **keyword_args):
		# call super (and enable drag/drop)
		screenlets.Screenlet.__init__(self, width=320, height=256,
			uses_theme=True, drag_drop=True, **keyword_args)
		# set theme
		self.theme_name = "default"
		# initially apply default image (for newly created instances)
		#self.image_filename = screenlets.PATH + '/DynamicFrame/dali.png'
		# add option group to properties-dialog
		self.add_options_group('DynamicFrame', 'DynamicFrame-related settings ...')
		# add editable options
		self.add_option(StringOption('DynamicFrame', 'folders', self.folders,'Select Folders', 'The folder where pictures are',))
		#self.add_option(ImageOption('DynamicFrame', 'image_filename', 
		#	self.image_filename, 'Filename',
		#	'Filename of image to be shown in this DynamicFrame ...')) 
		self.add_option(ColorOption('DynamicFrame','color_back', 
			self.color_back, 'Frame color', ''))
		self.add_option(IntOption('DynamicFrame','maxwidth', self.maxwidth,'Maximum Width','',min=100, max=640,increment=10))
		self.add_option(IntOption('DynamicFrame','maxheight', self.maxheight,'Maximum Height','',min=100, max=480,increment=10)) 
		self.add_option(IntOption('DynamicFrame','curve', 
			self.curve, 'Rounded corners angle', 
			'curve', min=0, max=45))
		self.add_option(IntOption('DynamicFrame','rotate', 
			self.rotate, 'Rotate angle', 
			'rotate', min=0, max=360))
		self.add_option(IntOption('DynamicFrame', 'update_interval', 
			self.update_interval, 'Update interval', 
			'The interval for updating info (in seconds ,3660 = 1 day, 25620 = 1 week)', min=0, max=25620))
		self.add_option(BoolOption('DynamicFrame', 'adjust_position', bool(self.adjust_position),'Adjust Position', 'Adjusts position to keep the center of consecutive images at the same position'))
		self.add_option(BoolOption('DynamicFrame', 'slide', bool(self.slide),'Run Slideshow', 'Runs a slideshow with the images inside the selected directory, changing the picture displayed every time Update Interval is elapsed'))
		self.add_option(IntOption('DynamicFrame','min_rotate', self.min_rotate,'Random rotation Min','',min=-20, max=20))
		self.add_option(IntOption('DynamicFrame','max_rotate', self.max_rotate,'Random rotation Max','',min=-20, max=20))
		self.add_option(BoolOption('DynamicFrame', 'random_rotate', bool(self.random_rotate),'Random Rotation', 'Rotates each newly loaded image by an amount randonly choosen between Random rotate Max and Random rotate Min'))
		self.add_option(BoolOption('DynamicFrame', 'recursive', bool(self.recursive),'Read directory recursively', 'Scan the chosen directory and all its subdirectories for images'))
		self.add_menuitem('update','NextImage')
		self.add_menuitem('open','Open')
		self.update_interval = self.update_interval
		
	def on_init (self):
		print "Screenlet has been initialized."
		# add default menuitems
		self.add_default_menuitems()

	def read_dir(self, folder):
		if os.path.exists(folder) and os.path.isdir(folder): #is it a valid folder?
			for f in os.listdir(folder):                #get all files in that folder
				if os.path.isdir(folder+ os.sep + f) and self.recursive==True:
					self.read_dir(folder+ os.sep + f)
				else:
			      		try:  #splitext[1] may fail
						if os.path.splitext(f)[1].lower() in self.use_types and self.image_filename != folder + os.sep + f: #is that file of proper type?
				                 	self.__imgs.append(folder + os.sep + f)         #if so, add it to our list
							#print f
				   	except:
				              	pass
		return True

	def fetch_image(self):
		
		#print self.folders
		#screenlets.show_message(self, self.folders)
		if len(self.__imgs)==0:
			self.read_dir(self.folders)
			if len(self.__imgs) == 0:
				self.__imgs.append(self.image_filename)
		#print imgs
		try:
			forecast = random.choice(self.__imgs)  #get a random entry from our list
			self.image_filename = forecast
			#os.system("gconftool-2 -t string -s /desktop/gnome/background/picture_filename " + chr(34) + forecast + chr(34))
			#print forecast
		except:
			pass

#		try:
#			return forecast	
#		except:
#			pass
#		return self.image_filename

	def __setattr__ (self, name, value):
		if name == "folders":
			#print "SET IMAGEFILENAME"
			# set self.__image to new image, if value is set and != current
			ret = True
			if value != '' and value != self.folders:	# ok?
				self.__imgs=[]
				screenlets.Screenlet.__setattr__(self, name, value)
				#self.image_filename=
				self.set_image()

			# update view

				self.redraw_canvas()
		if name == "recursive":
			screenlets.Screenlet.__setattr__(self, name, value)
			self.__imgs=[]
			self.set_image()

		if name == 'rotate' or name == 'curve':
			screenlets.Screenlet.__setattr__(self, name, value)
			self.set_image()
			self.redraw_canvas()
			#self.update_shape()
		elif name in ('image_scale', 'image_offset_x', 'image_offset_y'):
			# update view
			screenlets.Screenlet.__setattr__(self, name, value)
			self.redraw_canvas()
		if name == "update_interval":
			if value > 0:
				screenlets.Screenlet.__setattr__(self, name, value)
				self.__dict__['update_interval'] = value
				if self.__timeout:
					gobject.source_remove(self.__timeout)
				self.__timeout = gobject.timeout_add(int(value * 1000), self.update)
			else:
				self.__dict__['update_interval'] = 0

				if self.__timeout:
					gobject.source_remove(self.__timeout)
				pass
		#elif name == 's_width':
		#	screenlets.Screenlet.__setattr__(self, name, value)
		#	self.width = value + 200 
		#elif name == 's_height':
		#	screenlets.Screenlet.__setattr__(self, name, value)
		#	self.height = value + 200
		else:
			# else, just call super
			screenlets.Screenlet.__setattr__(self, name, value)
		
	def set_image(self):
		if self.shown==True:
			self.fetch_image()
			try:
				old_x=self.x
				old_y=self.y
				old_width=self.width
				old_height=self.height
				image = Image.open(self.image_filename)
				iw,ih = image.size
				factor=1.0
				hfactor=1.0
				if iw > self.maxwidth:
					factor = float(self.maxwidth)/float(iw)
				if ih > self.maxheight:
					hfactor= float(self.maxheight)/float(ih)
				if hfactor<factor:
					factor=hfactor
				ww=int(iw*factor)
				hh=int(ih*factor)
				if self.rotate != 0:
					self.x_padding=100
					self.y_padding=100
				else:
					self.x_padding=0
					self.y_padding=0
				
				wwh=float(ww)*0.5
				hhh=float(hh)*0.5
				edge=math.sqrt(wwh*wwh+hhh*hhh)
				nominal_angle_rad=math.asin(hhh/edge)
				nominal_angle=nominal_angle_rad/3.14*180
				co=math.cos(nominal_angle_rad)
				si=math.sin(nominal_angle_rad)
				angle=nominal_angle-self.rotate
				if self.rotate>0:
					angle=nominal_angle-self.rotate
				if self.rotate<0:
					angle=-nominal_angle-self.rotate
				offset=math.fabs(int(math.cos(angle*3.14/180)*edge))
				self.x_padding=offset-wwh
				#self.x_padding=edge
				w=int(ww+self.x_padding*2)
				angle=nominal_angle-self.rotate
				if self.rotate<0:
					angle=nominal_angle-self.rotate
				if self.rotate>0:
					angle=-nominal_angle-self.rotate
				offset=math.fabs(int(math.sin(angle*3.14/180)*edge))
				self.y_padding=offset-hhh
				#self.y_padding=edge
				h=int(hh+self.y_padding*2)
				if self.adjust_position == True:
					x=old_x+(old_width-w)/2
					y=old_y+(old_height-h)/2
					self.x=x
					self.y=y
				self.width=w
				self.height=h
			except Exception, ex:
				print 'Failed to load image '
		
		
	# --------------------------------------------------------------------------
	# Screenlet handlers
	# --------------------------------------------------------------------------
	def do_update(self):
		if self.random_rotate == True:
			randmin=self.min_rotate
			randmax=self.max_rotate
			if randmin>randmax:
				randmin=self.max_rotate
				randmax=self.min_rotate
				
			self.rotate=random.randint(randmin,randmax)
		else:
			self.set_image()
			self.redraw_canvas()
		return True

	def update(self):
		#screenlets.show_error(self, 
		#		'Failed to load image "%s": %s (only PNG images supported yet)' )# % (filename, ex))
		if self.slide == True:	
			self.do_update()
		return True
	
	def on_drag_enter (self, drag_context, x, y, timestamp):
		self.redraw_canvas()
	
	def on_drag_leave (self, drag_context, timestamp):
		self.redraw_canvas()
	
	def on_drop (self, x, y, sel_data, timestamp):
		print "Data dropped ..."
		filename = ''
		# get text-elements in selection data
		txt = sel_data.get_text()
		if txt:
			if txt[-1] == '\n':
				txt = txt[:-1]
			txt.replace('\n', '\\n')
			# if it is a filename, use it
			if txt.startswith('file://'):
				filename = txt[7:]
			else:
				screenlets.show_error(self, 'Invalid string: %s.' % txt)
		#else:
			# else get uri-part of selection
			#uris = sel_data.get_uris()
			#if uris and len(uris)>0:
				#print "URIS: "+str(uris	)
				#filename = uris[0][7:]
		if filename != '':
			#self.set_image(filename)
			if os.path.exists(filename) and os.path.isdir(filename):
				self.__imgs=[]
				self.folders=filename
				self.fetch_image()

	def on_menuitem_select (self, id):
		"""handle MenuItem-events in right-click menu"""
		if id == "update":
			self.do_update()
		if id == "open":
			os.system('xdg-open '+self.image_filename);
			
	def on_draw (self, ctx):
		ctx.set_operator(cairo.OPERATOR_OVER)
		ctx.scale(self.scale, self.scale)
		if self.theme:
	        	w = self.width-self.x_padding*2-8
			h = self.height-self.y_padding*2-8
			#except:pass
	        	# Fill in the shape.
			# if something is dragged over, lighten up the whole thing
			if self.dragging_over:
				ctx.set_operator(cairo.OPERATOR_XOR)
			#self.width = self.width*2
			#self.height = self.height*2
			
			ctx.translate(self.width/2,self.height/2)
			ctx.rotate((self.rotate)*3.14/180)
			ctx.translate(-self.width/2,-self.height/2)
			ctx.translate(self.x_padding,self.y_padding)
			#self.width = self.width/2
			#self.height = self.height/2
			# render bg
			#self.theme['DynamicFrame-bg.svg'].render_cairo(ctx)
			ctx.set_source_rgba(self.color_back[0],self.color_back[1],self.color_back[2],self.color_back[3])
			self.theme.draw_rounded_rectangle(ctx,0,0,self.curve,self.width-self.x_padding*2,self.height-self.y_padding*2)	
			ctx.translate(4,4)
			padding=0 # Padding from the edges of the window
	        	rounded=self.curve # How round to make the edges 20 is ok

	        	# Move to top corner
	        	ctx.move_to(0+padding+rounded, 0+padding)
	        	
	        	# Top right corner and round the edge
	        	ctx.line_to(w-padding-rounded, 0+padding)
	        	ctx.arc(w-padding-rounded, 0+padding+rounded, rounded, math.pi/2, 0)
	
	        	# Bottom right corner and round the edge
	        	ctx.line_to(w-padding, h-padding-rounded)
	        	ctx.arc(w-padding-rounded, h-padding-rounded, rounded, 0, math.pi/2)
	       	
	        	# Bottom left corner and round the edge.
	        	ctx.line_to(0+padding+rounded, h-padding)
	        	ctx.arc(0+padding+rounded, h-padding-rounded, rounded, math.pi+math.pi/2, math.pi)
		
	        	# Top left corner and round the edge
	        	ctx.line_to(0+padding, 0+padding+rounded)
	        	ctx.arc(0+padding+rounded, 0+padding+rounded, rounded, math.pi/2, 0)

			if self.image_filename != '': 
				pixbuf = gtk.gdk.pixbuf_new_from_file(self.image_filename).scale_simple(int(w),int(h),gtk.gdk.INTERP_HYPER)
				format = cairo.FORMAT_RGB24
				if pixbuf.get_has_alpha():
					format = cairo.FORMAT_ARGB32

				iw = pixbuf.get_width()
				ih = pixbuf.get_height()
				image = cairo.ImageSurface(format, iw, ih)

				

				#iw = float(image.get_width()) 
				#ih = float(image.get_height()) 

				matrix = cairo.Matrix(xx=iw/w, yy=ih/h)
				image = ctx.set_source_pixbuf(pixbuf, 0, 0)
				if image != None :image.set_matrix(matrix)
				
				#ctx.scale( min(((self.width-8)/iw),, ((self.height-8)/ih))
				
				#image = ctx.set_source_pixbuf(pixbuf, 0, 0)
        		
				#self.draw_scaled_image(ctx,self.image_filename,self.width,self.height)
			

			ctx.fill()
			#ctx.paint()

			image = None
			puxbuf = None
			#self.theme.render(ctx, 'DynamicFrame-frame')
			# render glass
			#self.theme['DynamicFrame-glass.svg'].render_cairo(ctx)
			#self.theme.render(ctx, 'DynamicFrame-glass')

	def draw_scaled_image(self,ctx, pix, w, h):
		"""Draws a png or svg from specified path with a certain width and height"""

		ctx.save()
		
		if pix.lower().endswith('svg'):
			image = rsvg.Handle(pix)
			size=image.get_dimension_data()
			try:ctx.scale( w/size[0], h/size[1])
			except:pass
			image.render_cairo(ctx)
		elif pix.lower().endswith('png'):

			image = cairo.ImageSurface.create_from_png(pix)
			iw = float(image.get_width())
			ih = float(image.get_height())
			ctx.scale( w/iw, h/ih)
			ctx.set_source_surface(image, 0, 0)
			
		image = None
		ctx.restore()
	
	def on_draw_shape (self, ctx):
		self.on_draw(ctx)

	def on_show(self):
		#print(self,self.image_filename)
		#print(self,self.width)
		#print(self,self.height)
		self.shown=True
		self.set_image()

	
# If the program is run directly or passed as an argument to the python
# interpreter then launch as new application
if __name__ == "__main__":
	# create session object here, the rest is done automagically
	import screenlets.session
	screenlets.session.create_session(DynamicFrameScreenlet)

