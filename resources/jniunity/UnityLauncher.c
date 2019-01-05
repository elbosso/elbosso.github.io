//rm UnityLauncher.o
//rm de_elbosso_ui_unity_UnityLauncher.h
///usr/java/jdk7/bin/javah -jni de.elbosso.ui.unity.UnityLauncher


//gcc `pkg-config --cflags --libs glib-2.0` `pkg-config --cflags --libs dee-1.0` `pkg-config --cflags --libs dbusmenu-glib-0.4` -I/usr/lib/jvm/java-7-openjdk-amd64/include/ -Wall -Werror -fpic -c UnityLauncher.c
//gcc -shared -o libebunity.so UnityLauncher.o -lunity -lunity-misc -lunity-extras -lunity-core-6.0
//cp  libebunity.so /tmp/dll/
/*
Copyright (c) 2012-2019.

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
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND. */
#include <unity/unity/unity.h>
#include <stdio.h>
#include "de_elbosso_ui_unity_UnityLauncher.h"
static GMainLoop *ml;
static jmethodID mid;
//static JNIEnv *callbackjnienv;
static jobject cbi;
static JavaVM * g_vm;

void* mainLoop(void *threadid)
{
    long tid;
    tid = (long) threadid;
    printf("Hello World! It's me, thread #%ld!\n", tid);
    g_main_loop_run(ml);
    pthread_exit(NULL);
	return NULL;
}

int setupMainLoop()
{
    pthread_t thread;
    int rc;
    ml = g_main_loop_new(NULL, TRUE);
    rc = pthread_create(&thread, NULL, mainLoop, NULL);
    return rc;
}

JNIEXPORT jint JNICALL Java_de_elbosso_ui_unity_UnityLauncher_callTest
  (JNIEnv *env, jobject obj, jint param)
{
    return param*2;
}
/*
 * Class:     org_eclipse_ubuntu_UnityLauncher
 * Method:    unity_launcher_entry_get_for_desktop_id
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_de_elbosso_ui_unity_UnityLauncher_getForDesktopId
(JNIEnv * env, jobject obj, jstring desktopId)
{
    const char *nativeDesktopId = (*env)->GetStringUTFChars(env, desktopId, 0);
    setupMainLoop();
    long pointer = (long) unity_launcher_entry_get_for_desktop_id(nativeDesktopId);
    (*env)->ReleaseStringUTFChars(env, desktopId, nativeDesktopId);
    return pointer;
}

/*
 * Class:     org_eclipse_ubuntu_UnityLauncher
 * Method:    unity_launcher_entry_set_progress_visible
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_setProgressVisible
(JNIEnv * env, jobject obj, jlong pointer, jboolean show)
{
    unity_launcher_entry_set_progress_visible((UnityLauncherEntry*) pointer, show);
    return;
}

/*
 * Class:     org_eclipse_ubuntu_UnityLauncher
 * Method:    unity_launcher_entry_set_progress
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_setProgress
(JNIEnv * env, jobject obj, jlong pointer, jdouble progress)
{
    unity_launcher_entry_set_progress((UnityLauncherEntry*) pointer, progress);
    return;
}

/*
 * Class:     de_elbosso_ui_unity_UnityLauncher
 * Method:    setCountVisible
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_setCountVisible
  (JNIEnv *env, jobject obj, jlong pointer, jboolean show)
{
    unity_launcher_entry_set_count_visible((UnityLauncherEntry*) pointer, show);
    return;
}

/*
 * Class:     de_elbosso_ui_unity_UnityLauncher
 * Method:    setCount
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_setCount
  (JNIEnv *env, jobject obj, jlong pointer, jlong count)
{
    unity_launcher_entry_set_count((UnityLauncherEntry*) pointer, count);
    return;
}

/*
 * Class:     de_elbosso_ui_unity_UnityLauncher
 * Method:    setUrgent
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_setUrgent
  (JNIEnv *env, jobject obj, jlong pointer, jboolean urgent)
{
    unity_launcher_entry_set_urgent((UnityLauncherEntry*) pointer, urgent);
    return;
}
/*
 * Class:     de_elbosso_ui_unity_UnityLauncher
 * Method:    getRootMenu
 * Signature: (J)J
 */

JNIEXPORT jlong JNICALL Java_de_elbosso_ui_unity_UnityLauncher_getRootMenu
  (JNIEnv *env, jobject obj, jlong pointer)
{
	DbusmenuMenuitem *Unity_Menu = dbusmenu_menuitem_new();
	dbusmenu_menuitem_property_set_bool (Unity_Menu, DBUSMENU_MENUITEM_PROP_VISIBLE, FALSE);

	unity_launcher_entry_set_quicklist((UnityLauncherEntry *)pointer, Unity_Menu);
	return (long)Unity_Menu;
}
void callbackNative(DbusmenuMenuitem *menuItem, guint whatever, gpointer data)
{
	JNIEnv * g_env;
	// double check it's all ok
	int getEnvStat = (*g_vm)->GetEnv(g_vm,(void **)&g_env, JNI_VERSION_1_6);
	if (getEnvStat == JNI_EDETACHED) {
		printf( "GetEnv: not attached\n");
		if ((*g_vm)->AttachCurrentThread(g_vm,(void **) &g_env, NULL) != 0) {
			printf("Failed to attach\n");
		}
	} else if (getEnvStat == JNI_OK) {
		//
	} else if (getEnvStat == JNI_EVERSION) {
		printf("GetEnv: version not supported\n");
	}
	const gchar *title=dbusmenu_menuitem_property_get(menuItem,DBUSMENU_MENUITEM_PROP_LABEL);

	(*g_env)->CallVoidMethod(g_env,cbi, mid,(jstring) (*g_env)->NewStringUTF(g_env, title));

	if ((*g_env)->ExceptionCheck(g_env)) {
		(*g_env)->ExceptionDescribe(g_env);
	}

	(*g_vm)->DetachCurrentThread(g_vm);
}
/*
 * Class:     de_elbosso_ui_unity_UnityLauncher
 * Method:    addToMenu
 * Signature: (JLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_de_elbosso_ui_unity_UnityLauncher_addToMenu
  (JNIEnv *env, jobject obj, jlong menu, jstring _labelText, jobject callbackInstance)
{	
	(*env)->GetJavaVM(env,&g_vm);
	jclass jc = (*env)->GetObjectClass(env,callbackInstance);
	mid = (*env)->GetMethodID(env,jc, "callback","(Ljava/lang/String;)V");
	cbi=(*env)->NewGlobalRef(env,callbackInstance);
	const gchar* labelText;

	// convert parameter labelText
	labelText = (const gchar*) (*env)->GetStringUTFChars(env, _labelText, NULL);


	DbusmenuMenuitem *Unity_Menu_Item = dbusmenu_menuitem_new();
	dbusmenu_menuitem_property_set(Unity_Menu_Item, DBUSMENU_MENUITEM_PROP_LABEL, labelText);

	dbusmenu_menuitem_child_append ((DbusmenuMenuitem *)menu, Unity_Menu_Item);
	dbusmenu_menuitem_property_set_bool((DbusmenuMenuitem *)menu, DBUSMENU_MENUITEM_PROP_VISIBLE, TRUE);
	dbusmenu_menuitem_property_set_bool(Unity_Menu_Item, DBUSMENU_MENUITEM_PROP_VISIBLE, TRUE);

	g_signal_connect(G_OBJECT(Unity_Menu_Item), DBUSMENU_MENUITEM_SIGNAL_ITEM_ACTIVATED, G_CALLBACK(callbackNative), NULL);

	// cleanup parameter labelText
	(*env)->ReleaseStringUTFChars(env, _labelText, labelText);

}

