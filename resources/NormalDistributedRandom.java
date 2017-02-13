/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.util.generator.generalpurpose;

import java.util.Random;

/**
 *
 * @author elbosso
 */
@de.elbosso.db.processors.GeneratorScript
@de.elbosso.dataflowframework.processors.GeneratorModule(datatype=java.lang.Number.class)
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
		return new java.lang.Double(rv);
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
