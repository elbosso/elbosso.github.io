/*
Copyright (c) 2012-2024.

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
package de.elbosso.util.validator.rules;

import de.elbosso.util.lang.annotations.ValidatorModule;
import de.netsysit.util.validator.rules.*;
import java.awt.geom.Rectangle2D;

@ValidatorModule(datatypes={java.awt.geom.Point2D.class,java.awt.Shape.class}, cubbyholetype = de.netsysit.util.threads.SimpleNonBlockingCubbyHole.class)
public class RectangleInsideRule extends AbstractRule
{
	private java.awt.geom.Rectangle2D rect;

	public RectangleInsideRule()
	{
		this(new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10));
	}
	public RectangleInsideRule(java.awt.geom.Rectangle2D rect)
	{
		this(null,rect);
	}
	public RectangleInsideRule(de.netsysit.util.validator.Rule pc,java.awt.geom.Rectangle2D rect)
	{
		super(pc);
		setRect(rect);
	}

	public Rectangle2D getRect()
	{
		return rect;
	}

	public void setRect(Rectangle2D rect)
	{
		this.rect = rect;
	}
	@Override
	public java.util.Collection validate(java.lang.Object value, java.util.Map context)
	{
		java.util.Collection reasons=new java.util.LinkedList();
		if(rect!=null)
		{
			if(reasons.isEmpty())
			{
				if(precondition!=null)
					reasons=precondition.validate(value,context);
			}
			if(reasons.isEmpty())
			{
				try
				{
					if(value instanceof java.awt.geom.Point2D)
					{
						java.awt.geom.Point2D p=(java.awt.geom.Point2D)value;
						if(rect.contains(p)==false)
							reasons.add(java.text.MessageFormat.format(getMsgViaI18n("de.netsysit.util.i18n",extractLocaleFromContext(context),"RectangleInsideRule.notinside.reason"),rect.toString()));
					}
					else if(value instanceof java.awt.Shape)
					{
						java.awt.geom.Rectangle2D r=((java.awt.Shape)value).getBounds2D();
						if(rect.contains(r)==false)
							reasons.add(java.text.MessageFormat.format(getMsgViaI18n("de.netsysit.util.i18n",extractLocaleFromContext(context),"RectangleInsideRule.notinside.reason"),rect.toString()));
					}
					else
					{
						reasons.add(getMsgViaI18n("de.netsysit.util.i18n",extractLocaleFromContext(context),"RectangleInsideRule.wrongtype.reason"));
					}
				}
				catch(java.lang.ClassCastException exp)
				{
					reasons.add(getMsgViaI18n("de.netsysit.util.i18n",extractLocaleFromContext(context),"RectangleInsideRule.wrongtype.reason"));
				}
			}
		}
		return reasons;
	}
	public java.lang.String toString()
	{
		return java.text.MessageFormat.format(getMsgViaI18n("de.netsysit.util.i18n",null,"RectangleInsideRule.notinside.reason"),new java.lang.Object[]{rect.toString()});
	}
}