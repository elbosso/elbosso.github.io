/*
Copyright (c) 2012-2015.

Juergen Key. Alle Rechte vorbehalten.

Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form, 
mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:

   1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext 
enthalten.
   2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss in der 
Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet 
werden, enthalten.
   3. Weder der Name des Autors noch die Namen der Beitragsleistenden 
duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software 
abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet 
werden.

DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE 
JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE 
UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER 
SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR 
ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, 
ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM 
VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER 
NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER 
GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER 
VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER 
UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM 
WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, 
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.
 */
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <linux/input.h>
#include <linux/uinput.h>
#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>

//http://thiemonge.org/getting-started-with-uinput

/* baudrate settings are defined in <asm/termbits.h>, which is
        included by <termios.h> */
#define BAUDRATE B115200           
/* change this definition for the correct port */
#define MODEMDEVICE "/dev/ttyUSB0"
#define _POSIX_SOURCE 1 /* POSIX compliant source */

#define FALSE 0
#define TRUE 1

volatile int STOP = FALSE;
struct termios oldtio;
int fd;
static int uinp_fd = -1;

main() {
    int c, res;
    struct termios newtio;
    char buf[255];
    if (setup_uinput_device() < 0) {
        printf("Unable to find uinput device\n");
        return -1;
    }
    /* 
      Open modem device for reading and writing and not as controlling tty
      because we don't want to get killed if linenoise sends CTRL-C.
     */
    fd = open(MODEMDEVICE, O_RDWR | O_NOCTTY);
    if (fd < 0) {
        perror(MODEMDEVICE);
        exit(-1);
    }

    tcgetattr(fd, &oldtio); /* save current serial port settings */
    bzero(&newtio, sizeof (newtio)); /* clear struct for new port settings */

    /* 
      BAUDRATE: Set bps rate. You could also use cfsetispeed and cfsetospeed.
      CRTSCTS : output hardware flow control (only used if the cable has
                all necessary lines. See sect. 7 of Serial-HOWTO)
      CS8     : 8n1 (8bit,no parity,1 stopbit)
      CLOCAL  : local connection, no modem contol
      CREAD   : enable receiving characters
     */
    newtio.c_cflag = BAUDRATE | CRTSCTS | CS8 | CLOCAL | CREAD;

    /*
      IGNPAR  : ignore bytes with parity errors
      ICRNL   : map CR to NL (otherwise a CR input on the other computer
                will not terminate input)
      otherwise make device raw (no other input processing)
     */
    newtio.c_iflag = IGNPAR | ICRNL;

    /*
     Raw output.
     */
    newtio.c_oflag = 0;

    /*
      ICANON  : enable canonical input
      disable all echo functionality, and don't send signals to calling program
     */
    newtio.c_lflag = ICANON;

    /* 
      initialize all control characters 
      default values can be found in /usr/include/termios.h, and are given
      in the comments, but we don't need them here
     */
    newtio.c_cc[VINTR] = 0; /* Ctrl-c */
    newtio.c_cc[VQUIT] = 0; /* Ctrl-\ */
    newtio.c_cc[VERASE] = 0; /* del */
    newtio.c_cc[VKILL] = 0; /* @ */
    newtio.c_cc[VEOF] = 4; /* Ctrl-d */
    newtio.c_cc[VTIME] = 0; /* inter-character timer unused */
    newtio.c_cc[VMIN] = 1; /* blocking read until 1 character arrives */
    newtio.c_cc[VSWTC] = 0; /* '\0' */
    newtio.c_cc[VSTART] = 0; /* Ctrl-q */
    newtio.c_cc[VSTOP] = 0; /* Ctrl-s */
    newtio.c_cc[VSUSP] = 0; /* Ctrl-z */
    newtio.c_cc[VEOL] = 0; /* '\0' */
    newtio.c_cc[VREPRINT] = 0; /* Ctrl-r */
    newtio.c_cc[VDISCARD] = 0; /* Ctrl-u */
    newtio.c_cc[VWERASE] = 0; /* Ctrl-w */
    newtio.c_cc[VLNEXT] = 0; /* Ctrl-v */
    newtio.c_cc[VEOL2] = 0; /* '\0' */

    /* 
      now clean the modem line and activate the settings for the port
     */
    tcflush(fd, TCIFLUSH);
    tcsetattr(fd, TCSANOW, &newtio);

    /*
      terminal settings done, now handle input
      In this example, inputting a 'z' at the beginning of a line will 
      exit the program.
     */
    //printf("start reading\n");
    struct input_event ev;

    memset(&ev, 0, sizeof (ev));

    while (STOP == FALSE) { /* loop until we have a terminating condition */
        //printf("read iteration\n");
        /* read blocks program execution until a line terminating character is 
           input, even if more than 255 chars are input. If the number
           of characters read is smaller than the number of chars available,
           subsequent reads will return the remaining chars. res will be set
           to the actual number of characters actually read */
        res = read(fd, buf, 255);
        buf[res] = 0; /* set end of string, so we can printf */
        if (res > 1) {
            //            printf(":%s:%d\n", buf, res);
            int x, y, btns;
            sscanf(buf, "%d %d %d", &x, &y, &btns);
            //printf("%d %d\n",x,y);
            ev.type = EV_ABS;
            ev.code = ABS_Y;
            ev.value = y;
            write(uinp_fd, &ev, sizeof (ev));

            ev.type = EV_ABS;
            ev.code = ABS_X;
            ev.value = x;

            write(uinp_fd, &ev, sizeof (ev));
            ev.type = EV_SYN;
            ev.code = SYN_REPORT;
            ev.value = 0;
            write(uinp_fd, &ev, sizeof (ev));
            ev.type = EV_KEY;
            ev.code = BTN_MIDDLE;
            ev.value = btns != 0;

            write(uinp_fd, &ev, sizeof (ev));
        }
        if (buf[0] == 'z') STOP = TRUE;
    }
    destroy();
}
struct uinput_user_dev uinp;
// uInput device structure
struct input_event event; // Input device structure

/* Setup the uinput device */
int setup_uinput_device() {
    // Temporary variable
    int i = 0;
    // Open the input device
    uinp_fd = open("/dev/uinput", O_WRONLY | O_NDELAY);
    if (uinp_fd == NULL) {

        printf("Unable to open /dev/uinput\n");
        return -1;
    }
    memset(&uinp, 0, sizeof (uinp)); // Intialize the uInput device to NULL
    strncpy(uinp.name, "Arduino Analog Stick", UINPUT_MAX_NAME_SIZE);
    uinp.id.version = 4;
    uinp.id.bustype = BUS_USB;
    // Setup the uinput device
    ioctl(uinp_fd, UI_SET_EVBIT, EV_KEY);
    //ioctl(uinp_fd, UI_SET_EVBIT, EV_REL);
    //ioctl(uinp_fd, UI_SET_RELBIT, REL_X);
    //ioctl(uinp_fd, UI_SET_RELBIT, REL_Y);
    ioctl(uinp_fd, UI_SET_EVBIT, EV_ABS);
    ioctl(uinp_fd, UI_SET_ABSBIT, ABS_X);
    ioctl(uinp_fd, UI_SET_ABSBIT, ABS_Y);
    //for (i=0; i < 256; i++) {
    //ioctl(uinp_fd, UI_SET_KEYBIT, i);
    //}
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_MOUSE);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_TOUCH);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_MOUSE);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_LEFT);
    ioctl(uinp_fd, UI_SET_KEYBIT, BTN_MIDDLE);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_RIGHT);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_FORWARD);
    //ioctl(uinp_fd, UI_SET_KEYBIT, BTN_BACK);
    /* Create input device into input sub-system */
    uinp.absmax[ABS_X] = 1023;
    uinp.absmin[ABS_X] = 0;
    uinp.absmax[ABS_Y] = 1023;
    uinp.absmin[ABS_Y] = 0;
    write(uinp_fd, &uinp, sizeof (uinp));

    if (ioctl(uinp_fd, UI_DEV_CREATE)) {
        printf("Unable to create UINPUT device.");
        return -1;
    }
    return 1;
}

destroy() {
    /* Destroy the input device */
    ioctl(uinp_fd, UI_DEV_DESTROY);

    /* Close the UINPUT device */
    close(uinp_fd);

    /* restore the old port settings */
    tcsetattr(fd, TCSANOW, &oldtio);
}
