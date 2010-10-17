package com.fray.evo.ui.swingx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXGraph;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jgap.InvalidConfigurationException;

import com.fray.evo.EcState;
import com.fray.evo.EvolutionChamber;
import com.fray.evo.action.EcAction;

public class EcSwingX extends JXPanel
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				JFrame frame = new JFrame();

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(new EcSwingX());
				frame.setPreferredSize(new Dimension(800, 600));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private JTextArea	outputList;

	public EcSwingX()
	{
		try
		{
			destination = (EcState) ec.getInternalDestination().clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		setName("Evolution Chamber");
		setLayout(new BorderLayout());

		JSplitPane outside = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel leftbottom = new JPanel(new GridBagLayout());
		JScrollPane stuffPanel = new JScrollPane(leftbottom);
		outside.setLeftComponent(stuffPanel);
		JPanel right = new JPanel(new FlowLayout());
		outside.setRightComponent(new JScrollPane(right));

		addInputContainer(leftbottom);
		addOutputContainer(right);

		add(outside);
	}

	private void addOutputContainer(JPanel component)
	{
		component.add(outputList = new JTextArea());
		outputList.setAlignmentX(0);
		outputList.setAlignmentY(0);
	}

	EvolutionChamber	ec	= new EvolutionChamber();
	EcState				destination;
	private JButton		goButton;

	private void addInputContainer(JPanel component)
	{
		addButton(component, "Stop", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ec.stop();
				goButton.setText("Start");
			}
		});
		goButton = addButton(component, "Start", new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ec.onNewBuild = new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent e)
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							@Override
							public void run()
							{
								EcState destination = (EcState) e.getSource();
								outputList.setText(e.getActionCommand());
							}
						});
					}
				};
				restartChamber();
				goButton.setText("Restart");
			}
		});
		gridy++;
		addInput(component, "Target number of seconds", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.targetSeconds = getDigit(e);
			}
		}).setText("600");
		addInput(component, "Threads", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ec.setThreads(getDigit(e));
			}
		}).setText("4");
		gridy++;
		addInput(component, "Population Size", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ec.POPULATION_SIZE = getDigit(e);
			}
		}).setText("30");
		addInput(component, "Chromosome Length", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ec.CHROMOSOME_LENGTH = getDigit(e);
			}
		}).setText("120");
		gridy++;
		addInput(component, "Drones", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.drones = getDigit(e);
			}
		}).setText("6");
		addCheck(component, "Burrow", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.burrow = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Overlords", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.overlords = getDigit(e);
			}
		}).setText("1");
		addInput(component, "Overseers", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.overseers = getDigit(e);
			}
		});
		gridy++;
		addCheck(component, "Pneumatized Carapace", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.pneumatizedCarapace = getTrue(e);
			}
		});
		addCheck(component, "Ventral Sacs", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.ventralSacs = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Queens", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.queens = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Zerglings", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.zerglings = getDigit(e);
			}
		});
		gridy++;
		addCheck(component, "Metabolic Boost", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.metabolicBoost = getTrue(e);
			}
		});
		addCheck(component, "Adrenal Glands", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.adrenalGlands = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Banelings", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.banelings = getDigit(e);
			}
		});
		addCheck(component, "Centrifugal Hooks", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.centrifugalHooks = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Roaches", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.roaches = getDigit(e);

			}
		});
		gridy++;
		addCheck(component, "Glial Reconstitution", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.glialReconstitution = getTrue(e);
			}
		});
		addCheck(component, "Tunneling Claws", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.tunnelingClaws = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Hydralisks", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.hydralisks = getDigit(e);
			}
		});
		addCheck(component, "Grooved Spines", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.groovedSpines = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Infestors", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.infestors = getDigit(e);
			}
		});
		gridy++;
		addCheck(component, "Neural Parasite", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.neuralParasite = getTrue(e);
			}
		});
		addCheck(component, "Pathogen Glands", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.pathogenGlands = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Mutalisks", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.mutalisks = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Ultralisks", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.ultralisks = getDigit(e);
			}
		});
		addCheck(component, "Chitinous Plating", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.chitinousPlating = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Corruptors", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.corruptors = getDigit(e);
			}
		});
		addInput(component, "Broodlords", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.broodlords = getDigit(e);
			}
		});
		gridy++;
		addCheck(component, "Melee +1", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.melee1 = getTrue(e);
			}
		});
		addCheck(component, "+2", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.melee2 = getTrue(e);
			}
		});
		addCheck(component, "+3", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.melee3 = getTrue(e);
			}
		});
		gridy++;
		addCheck(component, "Missile +1", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.missile1 = getTrue(e);
			}
		});
		addCheck(component, "+2", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.missile2 = getTrue(e);
			}
		});
		addCheck(component, "+3", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.missile3 = getTrue(e);
			}
		});
		gridy++;
		addCheck(component, "Carapace +1", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.armor1 = getTrue(e);
			}
		});
		addCheck(component, "+2", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.armor2 = getTrue(e);
			}
		});
		addCheck(component, "+3", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.armor3 = getTrue(e);
			}
		});
		gridy++;
		addCheck(component, "Flyer Attack +1", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerAttack1 = getTrue(e);
			}
		});
		addCheck(component, "+2", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerAttack2 = getTrue(e);
			}
		});
		addCheck(component, "+3", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerAttack3 = getTrue(e);
			}
		});
		gridy++;
		addCheck(component, "Flyer Armor +1", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerArmor1 = getTrue(e);
			}
		});
		addCheck(component, "+2", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerArmor2 = getTrue(e);
			}
		});
		addCheck(component, "+3", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.flyerArmor3 = getTrue(e);
			}
		});
		gridy++;
		addInput(component, "Hatcheries", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.hatcheries = getDigit(e);
			}
		});
		addInput(component, "Lairs", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.lairs = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Hives", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.hives = getDigit(e);
			}
		});
		addInput(component, "Gas Extractors", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.gasExtractors = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Evolution Chambers", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.evolutionChambers = getDigit(e);
			}
		});
		addInput(component, "Spine Crawlers", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.spineCrawlers = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Spawning Pools", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.spawningPools = getDigit(e);
			}
		});
		addInput(component, "Baneling Nests", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.banelingNest = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Roach Warrens", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.roachWarrens = getDigit(e);
			}
		});
		addInput(component, "Hydralisk Dens", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.hydraliskDen = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Infestation Pits", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.infestationPit = getDigit(e);
			}
		});
		addInput(component, "Spires", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.spire = getDigit(e);
			}
		});
		gridy++;
		addInput(component, "Ultralisk Caverns", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.ultraliskCavern = getDigit(e);
			}
		});
		addInput(component, "Greater Spires", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				destination.greaterSpire = getDigit(e);
			}
		});
	}

	private JButton addButton(JPanel container, String string, ActionListener actionListener)
	{
		final JButton button = new JButton();

		button.setText(string);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = .25;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		container.add(button, gridBagConstraints);
		button.addActionListener(actionListener);
		return button;
	}

	protected int getDigit(ActionEvent e)
	{
		JTextField tf = (JTextField) e.getSource();
		String text = tf.getText();
		try
		{
			Integer i = Integer.parseInt(text);
			return i;
		}
		catch (NumberFormatException ex)
		{
			return 0;
		}
	}

	private void restartChamber()
	{
		if (ec.threads.size() > 0)
			ec.stop();
		try
		{
			ec.setDestination((EcState) destination.clone());
			ec.go();
		}
		catch (InvalidConfigurationException e1)
		{
			e1.printStackTrace();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}

	protected boolean getTrue(ActionEvent e)
	{
		JCheckBox tf = (JCheckBox) e.getSource();
		this.ec.bestScore = new Double(0);
		return tf.isSelected();
	}

	int	gridy	= 0;

	private JTextField addInput(JPanel container, String name, final ActionListener a)
	{
		GridBagConstraints gridBagConstraints;

		JXLabel strictTextFieldLabel = new JXLabel("  " + name);
		strictTextFieldLabel.setAlignmentX(.5f);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = .25;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		container.add(strictTextFieldLabel, gridBagConstraints);

		final JTextField nonStrictTextField = new JTextField();
		nonStrictTextField.setColumns(5);
		nonStrictTextField.setText("0");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = .25;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		container.add(nonStrictTextField, gridBagConstraints);
		nonStrictTextField.addFocusListener(new FocusListener()
		{

			@Override
			public void focusLost(FocusEvent e)
			{
				a.actionPerformed(new ActionEvent(e.getSource(), 0, "changed"));
			}

			@Override
			public void focusGained(FocusEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		return nonStrictTextField;
	}

	private void addCheck(JPanel container, String name, final ActionListener a)
	{
		GridBagConstraints gridBagConstraints;

		final JCheckBox nonStrictTextField = new JCheckBox();
		nonStrictTextField.setText(name);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = .5;
		if (name.length() == 2)
			gridBagConstraints.gridwidth = 1;
		else
			gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		container.add(nonStrictTextField, gridBagConstraints);
		nonStrictTextField.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				a.actionPerformed(new ActionEvent(nonStrictTextField, 0, "changed"));
			}
		});
	}

}