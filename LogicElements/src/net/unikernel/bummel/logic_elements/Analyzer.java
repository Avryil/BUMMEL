package net.unikernel.bummel.logic_elements;

import java.util.Map;
import net.unikernel.bummel.project_model.api.BasicElement;

/**
 * <b>Pinout:</b>
 * <ol start='0'>
 * <li>Input</li>
 * </ol>
 * @author mcangel
 */
public class Analyzer extends BasicElement
{
	/**
	 * Creates analyzer with one port
	 */
	public Analyzer(Integer[] availablePorts)
	{
		super(new Integer[]{0});
		setState(0);
	}
	/**Sets analyzers state same as on the input port
	 * <b>Pinout:</b>
	 * <ol start='0'>
	 * <li>Input</li>
	 * </ol>
	 * @return
	 */
	@Override
	public Map<Integer, Double> process(Map<Integer, Double> valuesOnPorts)
	{
		if(valuesOnPorts.get(getPorts().get(0)).compareTo(new Double(0)) != 0);
		setState(1);	//light it
		return valuesOnPorts;
	}
}
