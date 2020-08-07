/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.util.generator.generalpurpose;

import de.elbosso.util.lang.annotations.*;

/**
 *
 * @author elbosso
 */
@GeneratorScript
@GeneratorModule(datatype=java.lang.Number.class)
@GeneratorRestHandler(datatype = java.lang.Number.class,
		path="normalDistributedRandom",
//		operationId = "operationId",
//		description = "description",
//		summary = "summary",
				parameters = {
						@KeyValueMapStore(key="Mue", valueMap ={
								@KeyValueStore(key="type", value="double"),
								@KeyValueStore(key="default", value="0.0")}),
						@KeyValueMapStore(key="WantedSigma", valueMap ={
								@KeyValueStore(key="type", value="double"),
								@KeyValueStore(key="default", value="0.5")})
				})
public class NormalDistributedRandom extends de.elbosso.util.beans.EventHandlingSupport implements de.netsysit.util.generator.Sequence<java.lang.Number>
{
	private java.util.Random random;
	private de.elbosso.algorithms.functions.InverseErrorFunctionAbramowitzStegun func=new de.elbosso.algorithms.functions.InverseErrorFunctionAbramowitzStegun();
	private double mue=0.0;
	private double wantedSigma=0.5;

	public NormalDistributedRandom()
	{
		super();
		try
		{
			random = java.security.SecureRandom.getInstance("SHA1PRNG");
		}
		catch (java.security.NoSuchAlgorithmException ex)
		{
			random=new java.util.Random(System.currentTimeMillis());
		}
		func.setArgScale(2.0);
		func.setArgOffset(-1.0);
//		func.setOffset(15);
//		double wantedSigma=5;
//		func.setScale(java.lang.Math.sqrt(2*wantedSigma));
	}

	public NormalDistributedRandom(double mue, double wantedSigma)
	{
		this();
		setMue(mue);
		setWantedSigma(wantedSigma);
	}

	public void setMue(double mue)
	{
		double old=getMue();
		this.mue = mue;
		func.setOffset(mue);
		send("mue", old, getMue());
	}

	public void setWantedSigma(double wantedSigma)
	{
		double old=getWantedSigma();
		this.wantedSigma = wantedSigma;
		func.setScale(/*java.lang.Math.sqrt(2**/wantedSigma/*)*/);
		send("wantedSigma", old, getWantedSigma());
	}

	public double getMue()
	{
		return mue;
	}

	public double getWantedSigma()
	{
		return wantedSigma;
	}
	
	public boolean hasNext()
	{
		return true;
	}

	public Number next()
	{
		double rv=random.nextDouble();
		rv=func.compute(rv);
		return java.lang.Double.valueOf(rv);
	}

	public void remove()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String toString()
	{
		java.lang.String rv=this.getClass().getSimpleName();
//		rv=i18n.getString(".name");
		return rv;
	}

}
