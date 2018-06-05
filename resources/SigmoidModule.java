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
package de.elbosso.dataflowframework.modules.convert.algorithms.functions;

@javax.annotation.Generated(value="de.elbosso.dataflowframework.processors.OneDFunctionProcessor", date="2018-05-23T15:13:50.414Z")
public class SigmoidModule extends de.netsysit.dataflowframework.modules.ThreadingBeanContextChildModuleBase
{
	static
	{
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociationForEventDispatchThread(SigmoidModule.class, de.netsysit.dataflowframework.modules.ThreadingBeanContextChildModuleBase.class);
	}
	private double[] output;
	private de.elbosso.algorithms.functions.Sigmoid function;
	private de.elbosso.util.validator.rules.MeasurementRule measure;
	private de.elbosso.util.validator.rules.MeasurementWrapper wrapper;
	private java.lang.Number[] lastInput;

	public SigmoidModule()
	{
		super(SigmoidModule.class.getName());
		this.function=new de.elbosso.algorithms.functions.Sigmoid();
	}
    @Override
    protected de.netsysit.util.threads.CubbyHole createCubbyHole()
    {
        return new de.netsysit.util.threads.SimpleNonBlockingCubbyHole();
    }

	@de.elbosso.dataflowframework.ui.annotations.AutoConnectAllowed
	public synchronized double[] getOutput()
	{
		return output;
	}
    private synchronized void setOutput(double[] output)
    {
        this.output=output;
    }
    private synchronized java.lang.Number[] getLastInput()
    {
        return (lastInput!=null?java.util.Arrays.copyOf(lastInput,lastInput.length):null);
    }
    private synchronized void setLastInput(java.lang.Number[] lastInput)
    {
        this.lastInput = lastInput;
    }
	@de.elbosso.dataflowframework.ui.annotations.AutoConnectAllowed
	public void input(java.lang.Number[] data)
	{
        setLastInput(data);
        processData(data);
    }
    protected void doWork(Object ref) throws InterruptedException
    {
        java.lang.Number[] in=getLastInput();
		if(in!=null)
		{
			double[] old=getOutput();
			try
			{
                output=new double[in.length];
                for(int i=0;i<in.length;++i)
                    output[i]=function.compute(in[i].doubleValue());
                setOutput(output);
                send("output", old, getOutput());
            }
            catch(java.lang.Throwable t)
            {
                error(null,t.getMessage());
            }
		}
	}
	public de.elbosso.algorithms.functions.Sigmoid getFunction()
	{
		return function;
	}

	public void setFunction(de.elbosso.algorithms.functions.Sigmoid function)
	{
		this.function = function;
	}
}

