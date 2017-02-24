/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.algorithms.functions;

import de.elbosso.util.lang.OneDFunction;
import de.elbosso.util.lang.annotations.OneDFunctionModule;

/**
 *
 * @author elbosso
 */
@OneDFunctionModule(cubbyholetype = de.netsysit.util.threads.SimpleNonBlockingCubbyHole.class)
public class Sigmoid extends OneDFunction
{
	static
	{
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociation(Sigmoid.class,java.lang.Object.class);
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociationForEventDispatchThread(Sigmoid.class,java.lang.Object.class);
	}

	public double compute(double input)
	{
		return finish(1/(1+java.lang.Math.exp(-prepare(input))));
	}

}
