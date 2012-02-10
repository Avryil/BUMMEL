package net.unikernel.bummel.logic_elements.Not;

import java.util.Map;
import net.unikernel.bummel.project_model.api.BasicElement;

/**
 * <b>Pinout:</b>
 * <ol start='0'>
 * <li>Input</li>
 * <li>Output</li>
 * </ol>
 * @author mcangel
 */
public class Element extends BasicElement
{

	public Element()
	{
		super(new String[]{"input","output"});//input, output
	}
	
	/**
	 * Calculates the logical "not" value
	 * <b>Pinout:</b>
	 * <ol start='0'>
	 * <li>Input</li>
	 * <li>Output</li>
	 * </ol>
	 */
	@Override
	public Map<String, Double> process(Map<String, Double> valuesOnPorts)
	{
		if(valuesOnPorts.get(getPorts().get(0)).compareTo(new Double(0)) == 0)
		{
			valuesOnPorts.put(getPorts().get(1), new Double(1));
		}
		else
		{
			valuesOnPorts.put(getPorts().get(1), new Double(0));
		}
		valuesOnPorts.put(getPorts().get(0), 0.);
		return valuesOnPorts;
	}
}
