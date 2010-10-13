package com.fray.evo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EcActionBuildInfestor extends EcAction implements Serializable
{
	private static final int	time		= 50;
	private static final int	supply		= 2;
	private static final int	gas			= 150;
	private static final int	minerals	= 100;

	public void execute(final EcBuildOrder s, final EcEvolver e)
	{
		s.minerals -= minerals;
		s.gas -= gas;
		s.consumeLarva(e);
		s.supplyUsed += supply;
		s.addFutureAction(time, new Runnable()
		{
			@Override
			public void run()
			{
				if (e.debug)
					System.out.println("@" + s.seconds() + " Infestor+1");
				s.infestors += 1;
			}
		});
	}

	public boolean isInvalid(EcBuildOrder s)
	{
		if (s.infestationPit == 0)
			return true;
		return false;
	}

	public boolean isPossible(EcBuildOrder s)
	{
		if (s.minerals < minerals)
			return false;
		if (s.gas < gas)
			return false;
		if (s.larva < 1)
			return false;
		if (!s.hasSupply(supply))
			return false;
		return true;
	}

	@Override
	public List<EcAction> requirements()
	{
		ArrayList<EcAction> l = new ArrayList<EcAction>();
		l.add(new EcActionBuildInfestationPit());
		return l;
	}
}
