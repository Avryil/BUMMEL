/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unikernel.bummel.logic_elements;

import net.unikernel.bummel.basic_elements.BasicElement;

/**
 *
 * @author uko
 */
public class Or implements BasicElement
	{
		@Override
		public double[] touch(double[] acdc)
		{
			double[] result = {0};
			for (double pin : acdc)
			{
				if((result[0] = ((pin == 0) ? 0 : 1)) == 1)
					return result;
			}
			return result;
		}
	}
