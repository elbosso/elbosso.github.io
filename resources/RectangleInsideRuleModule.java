/*
Copyright (c) 2012-2018.

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

@javax.annotation.Generated(value="de.elbosso.dataflowframework.processors.ValidatorProcessor", date="2018-01-21T08:47:55.148Z")
public class RectangleInsideRuleModule extends de.netsysit.dataflowframework.modules.ThreadingBeanContextChildModuleBase
{
	static
	{
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociationForEventDispatchThread(RectangleInsideRuleModule.class, de.netsysit.dataflowframework.modules.ThreadingBeanContextChildModuleBase.class);
	}
	private java.awt.geom.Point2D failedPoint2D;
	private java.awt.geom.Point2D passedPoint2D;
	private java.awt.geom.Point2D lastInputPoint2D;
	private java.awt.Shape failedShape;
	private java.awt.Shape passedShape;
	private java.awt.Shape lastInputShape;
	private de.elbosso.util.validator.rules.RectangleInsideRule rule;
	private de.elbosso.util.validator.rules.MeasurementRule measure;
	private de.elbosso.util.validator.rules.MeasurementWrapper wrapper;

	public RectangleInsideRuleModule()
	{
		super(RectangleInsideRuleModule.class.getName());
		this.rule=new de.elbosso.util.validator.rules.RectangleInsideRule();
		wrapper=new de.elbosso.util.validator.rules.MeasurementWrapper(rule);
		measure=new de.elbosso.util.validator.rules.MeasurementRule(null);
	}
    @Override
    protected de.netsysit.util.threads.CubbyHole createCubbyHole()
    {
        return new de.netsysit.util.threads.SimpleNonBlockingCubbyHole();
    }
//datatypes.size: 2
	public synchronized java.awt.geom.Point2D getFailedPoint2D()
	{
		return failedPoint2D;
	}
    private synchronized void setFailedPoint2D(java.awt.geom.Point2D failedPoint2D)
    {
        this.failedPoint2D=failedPoint2D;
    }
	public synchronized java.awt.geom.Point2D getPassedPoint2D()
	{
		return passedPoint2D;
	}
    private synchronized void setPassedPoint2D(java.awt.geom.Point2D passedPoint2D)
    {
        this.passedPoint2D=passedPoint2D;
    }

    private synchronized java.awt.geom.Point2D getLastInputPoint2D()
    {
        return lastInputPoint2D;
    }
    private synchronized void setLastInputPoint2D(java.awt.geom.Point2D lastInputPoint2D)
    {
        this.lastInputPoint2D = lastInputPoint2D;
    }
	public void inputPoint2D(java.awt.geom.Point2D data)
	{
        setLastInputPoint2D(data);
        processData(java.awt.geom.Point2D.class);
    }
	public synchronized java.awt.Shape getFailedShape()
	{
		return failedShape;
	}
    private synchronized void setFailedShape(java.awt.Shape failedShape)
    {
        this.failedShape=failedShape;
    }
	public synchronized java.awt.Shape getPassedShape()
	{
		return passedShape;
	}
    private synchronized void setPassedShape(java.awt.Shape passedShape)
    {
        this.passedShape=passedShape;
    }

    private synchronized java.awt.Shape getLastInputShape()
    {
        return lastInputShape;
    }
    private synchronized void setLastInputShape(java.awt.Shape lastInputShape)
    {
        this.lastInputShape = lastInputShape;
    }
	public void inputShape(java.awt.Shape data)
	{
        setLastInputShape(data);
        processData(java.awt.Shape.class);
    }
    protected void doWork(Object ref) throws InterruptedException
    {
        if(((java.lang.Class)ref).isAssignableFrom(java.awt.geom.Point2D.class))
        {
            java.awt.geom.Point2D data=getLastInputPoint2D();
            try
            {
                boolean valid=wrapper.validate(data).isEmpty();
                if(measure.isTransparent()==false)
                {
                    valid=measure.validate(wrapper).isEmpty();
                }
                if(valid==false)
                {
                    java.awt.geom.Point2D old=getFailedPoint2D();
                    setFailedPoint2D(data);
                    send("failedPoint2D", old, getFailedPoint2D());
                }
                else
                {
                    java.awt.geom.Point2D old=getPassedPoint2D();
                    setPassedPoint2D(data);
                    send("passedPoint2D", old, getPassedPoint2D());
                }
            }
            catch(java.lang.Throwable t)
            {
                error(null,t.getMessage());
            }
        }
        if(((java.lang.Class)ref).isAssignableFrom(java.awt.Shape.class))
        {
            java.awt.Shape data=getLastInputShape();
            try
            {
                boolean valid=wrapper.validate(data).isEmpty();
                if(measure.isTransparent()==false)
                {
                    valid=measure.validate(wrapper).isEmpty();
                }
                if(valid==false)
                {
                    java.awt.Shape old=getFailedShape();
                    setFailedShape(data);
                    send("failedShape", old, getFailedShape());
                }
                else
                {
                    java.awt.Shape old=getPassedShape();
                    setPassedShape(data);
                    send("passedShape", old, getPassedShape());
                }
            }
            catch(java.lang.Throwable t)
            {
                error(null,t.getMessage());
            }
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

