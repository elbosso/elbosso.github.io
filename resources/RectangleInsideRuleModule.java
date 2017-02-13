/*
Copyright (c) 2012-2017.

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
package de.elbosso.dataflowframework.modules.filter.rules.util.validator.rules;

public class RectangleInsideRuleModule extends de.netsysit.dataflowframework.modules.BeanContextChildModuleBase
{
	static
	{
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociationForEventDispatchThread(RectangleInsideRuleModule.class, de.netsysit.dataflowframework.modules.BeanContextChildModuleBase.class);
	}
	private java.awt.geom.Point2D failedPoint2D;
	private java.awt.geom.Point2D passedPoint2D;
	private java.awt.Shape failedShape;
	private java.awt.Shape passedShape;
	private de.elbosso.util.validator.rules.RectangleInsideRule rule;
	private de.elbosso.util.validator.rules.MeasurementRule measure;
	private de.elbosso.util.validator.rules.MeasurementWrapper wrapper;

	public RectangleInsideRuleModule()
	{
		super();
		this.rule=new de.elbosso.util.validator.rules.RectangleInsideRule();
		wrapper=new de.elbosso.util.validator.rules.MeasurementWrapper(rule);
		measure=new de.elbosso.util.validator.rules.MeasurementRule(null);
	}
//datatypes.size: 2
	public java.awt.geom.Point2D getFailedPoint2D()
	{
		return failedPoint2D;
	}
	public java.awt.geom.Point2D getPassedPoint2D()
	{
		return passedPoint2D;
	}
	
	public void inputPoint2D(java.awt.geom.Point2D data)
	{
		boolean valid=wrapper.validate(data).isEmpty();
		if(measure.isTransparent()==false)
		{
			valid=measure.validate(wrapper).isEmpty();
		}
		if(valid==false)
		{
			java.awt.geom.Point2D old=getFailedPoint2D();
			failedPoint2D=data;
			send("failedPoint2D", old, getFailedPoint2D());
		}
		else
		{
			java.awt.geom.Point2D old=getPassedPoint2D();
			passedPoint2D=data;
			send("passedPoint2D", old, getPassedPoint2D());
		}
	}
	public java.awt.Shape getFailedShape()
	{
		return failedShape;
	}
	public java.awt.Shape getPassedShape()
	{
		return passedShape;
	}
	
	public void inputShape(java.awt.Shape data)
	{
		boolean valid=wrapper.validate(data).isEmpty();
		if(measure.isTransparent()==false)
		{
			valid=measure.validate(wrapper).isEmpty();
		}
		if(valid==false)
		{
			java.awt.Shape old=getFailedShape();
			failedShape=data;
			send("failedShape", old, getFailedShape());
		}
		else
		{
			java.awt.Shape old=getPassedShape();
			passedShape=data;
			send("passedShape", old, getPassedShape());
		}
	}
	public de.elbosso.util.validator.rules.RectangleInsideRule getRule()
	{
		return rule;
	}

	public void setRule(de.elbosso.util.validator.rules.RectangleInsideRule rule)
	{
		this.rule = rule;
	}
	public de.elbosso.util.validator.rules.MeasurementRule getMeasurement()
	{
		return measure;
	}

	public void setMeasurement(de.elbosso.util.validator.rules.MeasurementRule measure)
	{
		this.measure = measure;
	}
}

