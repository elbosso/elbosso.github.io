#!/usr/bin/env python
# http://shallowsky.com/blog/linux/open-selection-in-browser.html
import gtk
import re

primary = gtk.clipboard_get(gtk.gdk.SELECTION_PRIMARY)

if not primary.wait_is_text_available() :
    sys.exit(0)
s = primary.wait_for_text()

# eliminate newlines, and any spaces immediately following a newline:
print re.sub(r'[\r\n]+ *', '', s)
