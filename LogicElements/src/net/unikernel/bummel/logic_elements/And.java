package net.unikernel.bummel.logic_elements;

import java.util.Map;
import net.unikernel.bummel.project_model.api.BasicElement;

/**
 * <b>Pinout:</b>
 * <ol start='0'>
 * <li>Input</li>
 * <li>Input</li>
 * <li>Output</li>
 * </ol>
 * @author mcangel
 */
public class And extends BasicElement
{

	public And()
	{
		super(new String[]{"input1","input2","output"}); //input1,inpu2,output
	}
	
	 /**
	 * Calculates the logical "and" value
	 * <b>Pinout:</b>
	 * <ol start='0'>
	 * <li>Input</li>
	 * <li>Input</li>
	 * <li>Output</li>
	 * </ol>
	 */
	@Override
	public Map<String, Double> process(Map<String, Double> valuesOnPorts)
	{
		valuesOnPorts.put(getPorts().get(2), 
				new Double(valuesOnPorts.get(getPorts().get(0)).doubleValue()
				* valuesOnPorts.get(getPorts().get(1)).doubleValue()));
		valuesOnPorts.put(getPorts().get(0), 0.);
		valuesOnPorts.put(getPorts().get(1), 0.);
		return valuesOnPorts;
	}
}