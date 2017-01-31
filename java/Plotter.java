import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Plotter extends Applet{

	Plot3DCanvas plotpanel;

	JButton clearButton;
	JPanel inputPanel;
	JPanel removePanel;
	JPanel nodePanel;
	JPanel valuePanel;
	JToggleButton plotButton;
	JToggleButton legendButton;
	JToggleButton axesButton;
	JButton plot2DButton;
	JButton moreFineButton;
	JLabel fineLabel;
	JButton lessFineButton;
	JButton saveButton;
	JPanel autoNodesPanel;
	JToggleButton entButton;
	JToggleButton mlsButton;
	JToggleButton tri3Button;
	JToggleButton wachButton;
	JToggleButton mvcButton;
	JToggleButton laplaceButton;
	JToggleButton labelsButton;
	JToggleButton nodeNumbersButton;
	JToggleButton noLabelsButton;
	JToggleButton noNothingButton;
	JToggleButton clickButton;
	JSlider fineSlider;
	JSlider fontSlider;
	ButtonGroup buttonGroup;
	ButtonGroup labelsGroup;
	String[] levels;
	int newNode1;
	int newNode2;
	int pos = 0;
	int pos1 = pos;
	int pos2 = pos;
	int pos3 = pos;

	public final int COORDS = 1;
	public final int NUMBERS = 2;
	public final int DOTS = 3;

	private ShapeFunction shapeFunction;


	public void init() {
		buttonGroup = new ButtonGroup();
		labelsGroup = new ButtonGroup();
        this.setLayout(new BorderLayout());
		plotpanel = new Plot3DCanvas();
		this.add(plotpanel, BorderLayout.CENTER);
		fineLabel = new JLabel("Fine", JLabel.CENTER);



		clearButton = new JButton("Remove all nodes");
        clearButton.addActionListener(new ActionListener() {
       			public void actionPerformed(ActionEvent e) {
				plotpanel.initPlots();
				tri3Button.setEnabled(true);
				wachButton.setEnabled(true);
				laplaceButton.setEnabled(true);
				mvcButton.setEnabled(true);
				plotpanel.node = 0;
				plotpanel.reset();
            }
        });



        inputPanel = new JPanel();
        inputPanel.setToolTipText("Add a node by inputting its coordinates");
		final JTextField xField1 = new JTextField(3);
		final JTextField yField1 = new JTextField(3);
		yField1.setText("");
		xField1.setText("");
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
       			public void actionPerformed(ActionEvent e) {
				double x = Double.parseDouble(xField1.getText());
				double y = Double.parseDouble(yField1.getText());
				plotpanel.nodeSet.add(x, y);
				yField1.setText("");
				xField1.setText("");
				check();
				plotpanel.mlsRadius = plotpanel.nodeSet.thirdRadius();
				pos = 0;
				plotpanel.reset();
            }
        });


        inputPanel.setLayout(new GridLayout(1,3));
        inputPanel.add(xField1);
        inputPanel.add(yField1);
        inputPanel.add(addButton);





        autoNodesPanel = new JPanel();
        autoNodesPanel.setToolTipText("Create nodes in a regular or random configuration");
		final JTextField xField2 = new JTextField(2);
		JButton polyButton = new JButton("P");
		polyButton.setToolTipText("Create a polygon");
		polyButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				int x = Integer.parseInt(xField2.getText());
				for (int k = 0; k < x; k++)
					plotpanel.nodeSet.add(Math.cos(2.0*k*Math.PI / x), Math.sin(2.0*k*Math.PI / x));
				check();
				xField2.setText("");
				plotpanel.mlsRadius = plotpanel.nodeSet.thirdRadius();
				pos = 0;
				plotpanel.reset();
		    }
		});


		JButton randButton = new JButton("R");
        randButton.setToolTipText("Create a unit square with randomly-located interior nodes");
		randButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				int x = Integer.parseInt(xField2.getText());
				plotpanel.nodeSet.add(0,1);
				plotpanel.nodeSet.add(0,0);
				plotpanel.nodeSet.add(1,1);
				plotpanel.nodeSet.add(1,0);
				for (int k = 0; k < x; k++)
					plotpanel.nodeSet.add(Math.random(), Math.random());
				check();
				plotpanel.mlsRadius = plotpanel.nodeSet.thirdRadius();
				pos = 0;
				plotpanel.reset();
				xField2.setText("");
		   }
		});

		JButton quadButton = new JButton("Q");
		quadButton.setToolTipText("Create a quadtree-type element");
		quadButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				int x = Integer.parseInt(xField2.getText());
				if (x < 4)
					return;
				x = x - 4;
				plotpanel.nodeSet.add(0,1);
				plotpanel.nodeSet.add(0,0);
				plotpanel.nodeSet.add(1,1);
				plotpanel.nodeSet.add(1,0);
				int sideNodes = x / 4;
				int remainder = x - sideNodes*4;

				if (remainder > 0){
					for (int i = 1; i <= (sideNodes + 1); i++)
						plotpanel.nodeSet.add(0, (double)i/(sideNodes + 2));
				}
				else {
					for (int i = 1; i <= sideNodes; i++)
						plotpanel.nodeSet.add(0, (double)i/(sideNodes + 1));
				}
				if (remainder > 1){
					for (int i = 1; i <= (sideNodes + 1); i++)
						plotpanel.nodeSet.add((double)i/(sideNodes + 2), 1);
				}
				else {
					for (int i = 1; i <= sideNodes; i++)
						plotpanel.nodeSet.add((double)i/(sideNodes + 1), 1);
				}
				if (remainder > 2){
					for (int i = 1; i <= (sideNodes + 1); i++)
						plotpanel.nodeSet.add(1, (double)i/(sideNodes + 2));
				}
				else {
					for (int i = 1; i <= sideNodes; i++)
						plotpanel.nodeSet.add(1, (double)i/(sideNodes + 1));
				}
				if (remainder > 3){
					for (int i = 1; i <= (sideNodes + 1); i++)
						plotpanel.nodeSet.add((double)i/(sideNodes + 2), 0);
				}
				else {
					for (int i = 1; i <= sideNodes; i++)
						plotpanel.nodeSet.add((double)i/(sideNodes + 1), 0);
				}
				xField2.setText("");
				check();
				plotpanel.mlsRadius = plotpanel.nodeSet.thirdRadius();
				pos = 0;
				plotpanel.reset();
		    }
		});




        autoNodesPanel.setLayout(new GridLayout(1,4));
        autoNodesPanel.add(xField2);
        autoNodesPanel.add(polyButton);
        autoNodesPanel.add(quadButton);
        autoNodesPanel.add(randButton);




        nodePanel = new JPanel();
        nodePanel.setToolTipText("Select the node for which to plot the shape function");
		final JButton nLabel1 = new JButton("");
		newNode1 = -1;
		if (plotpanel.nodeSet.count > 0){
			nLabel1.setText(Integer.toString(newNode1+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode1])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode1])).concat(")"));
		}
		JButton prevButton1 = new JButton("<");
		prevButton1.setToolTipText("Previous node");
		prevButton1.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0)
					return;
				newNode1 = newNode1 - 1;
				if (newNode1 <= -1)
					newNode1 = plotpanel.nodeSet.count - 1;
				nLabel1.setText(Integer.toString(newNode1+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode1])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode1])).concat(")"));
            }
        });

		JButton nextButton1 = new JButton(">");
		nextButton1.setToolTipText("Next node");
		nextButton1.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0)
					return;
				newNode1 = newNode1 + 1;
				if (newNode1 >= plotpanel.nodeSet.count)
					newNode1 = 0;
				nLabel1.setText(Integer.toString(newNode1+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode1])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode1])).concat(")"));
            }
        });


		nLabel1.setToolTipText("Set this as the current node");
		nLabel1.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0 || newNode1 >= plotpanel.nodeSet.count || newNode1 < 0){
					return;
				}
				plotpanel.node = newNode1;
				plotpanel.reset();
            }
        });


		nodePanel.setLayout(new BorderLayout());
        nodePanel.add(prevButton1, BorderLayout.WEST);
        nodePanel.add(nLabel1);
        nodePanel.add(nextButton1, BorderLayout.EAST);





        removePanel = new JPanel();
        removePanel.setToolTipText("Remove a single node");
		final JButton nLabel2 = new JButton("");
		newNode2 = -1;
		if (plotpanel.nodeSet.count > 0){
			nLabel2.setText(Integer.toString(newNode2+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode2])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode2])).concat(")"));
		}
		JButton prevButton2 = new JButton("<");
		prevButton2.setToolTipText("Previous node");
		prevButton2.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0)
					return;
				newNode2 = newNode2 - 1;
				if (newNode2 <= -1)
					newNode2 = plotpanel.nodeSet.count - 1;
				nLabel2.setText(Integer.toString(newNode2+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode2])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode2])).concat(")"));
            }
        });

		JButton nextButton2 = new JButton(">");
		nextButton2.setToolTipText("Next node");
		nextButton2.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0)
					return;
				newNode2 = newNode2 + 1;
				if (newNode2 >= plotpanel.nodeSet.count)
					newNode2 = 0;
				nLabel2.setText(Integer.toString(newNode2+1).concat(":  (").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeX[newNode2])).concat(", ").concat(Format.leftAlign(0,1,plotpanel.nodeSet.nodeY[newNode2])).concat(")"));
            }
        });


		nLabel2.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count == 0 || newNode2 >= plotpanel.nodeSet.count || newNode2 < 0){
					return;
				}
				plotpanel.nodeSet.remove(newNode2);
				if (plotpanel.node == newNode2)
					plotpanel.node = 0;
				plotpanel.reset();
				if (newNode2 == newNode1)
					nLabel1.setText("                     ");
				newNode2 = 0;
				check();
				nLabel2.setText("                     ");
				plotpanel.mlsRadius = plotpanel.nodeSet.thirdRadius();
				pos = 0;
            }
        });


		removePanel.setLayout(new BorderLayout());
        removePanel.add(prevButton2, BorderLayout.WEST);
        removePanel.add(nLabel2);
        removePanel.add(nextButton2, BorderLayout.EAST);









		final JTextField xField3 = new JTextField(6);
		final JTextField yField3 = new JTextField(6);
		final JTextField zField = new JTextField(10);
		JButton getButton = new JButton("=");
		getButton.setToolTipText("Get the value of the current shape function at these coordinates");
		getButton.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
				double x = Double.parseDouble(xField3.getText());
				double y = Double.parseDouble(yField3.getText());
				shapeFunction = new ShapeFunction(plotpanel.nodeSet, plotpanel.type, plotpanel.node);
				shapeFunction.epsilon = 1.0E-7;
				shapeFunction.mlsRadius = plotpanel.mlsRadius;
				shapeFunction.beta = plotpanel.beta;
				if (plotpanel.nodeSet.isInside(x,y))
					zField.setText(Format.leftAlign(8,8,shapeFunction.f(x, y)));
				else
					zField.setText("Not in element!");
            }
        });


        JLabel xLabel = new JLabel("x: ");
        JLabel yLabel = new JLabel(" y: ");



        JPanel valuePanelal = new JPanel();
		valuePanelal.setLayout(new BorderLayout());
		valuePanelal.add(xLabel, BorderLayout.WEST);
		valuePanelal.add(xField3);

        JPanel valuePanelar = new JPanel();
		valuePanelar.setLayout(new BorderLayout());
		valuePanelar.add(yLabel, BorderLayout.WEST);
		valuePanelar.add(yField3);

		JPanel valuePanela = new JPanel();
		valuePanela.setLayout(new GridLayout(1,2));
		valuePanela.add(valuePanelal);
		valuePanela.add(valuePanelar);


		JPanel valuePanelb = new JPanel();
		valuePanelb.setLayout(new BorderLayout(2,0));
		valuePanelb.add(getButton, BorderLayout.WEST);
		valuePanelb.add(zField);








		plot2DButton = new JButton("2D View");
        plot2DButton.setToolTipText("View the x-y plane");
        plot2DButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					plotpanel.set2D();
            }
        });





		levels = new String[]{	"",
								"Level 1: No detail, unlimited plotting speed.",
								"Level 2: Some detail, very fast plotting speed.",
								"Level 3: Acceptable detail, fast plotting speed.",
								"Level 4: Good detail, reasonable plotting speed.",
								"Level 5: Great detail, sluggish plotting speed.",
								"Level 6: Excellent detail, very slow plotting speed.",
								"Level 7: Perfect detail, unreasonably slow plotting speed."
							};
		fineSlider = new JSlider(JSlider.HORIZONTAL, 1, 6, 4);
		fineSlider.setToolTipText(levels[4]);
		fineSlider.setPaintTicks(true);
		fineSlider.setMajorTickSpacing(1);
		fineSlider.setSnapToTicks(true);

		fineSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
				if (!(fineSlider.getValueIsAdjusting())){
					plotpanel.fineLevel = fineSlider.getValue();
					fineSlider.setToolTipText(levels[fineSlider.getValue()]);
					plotpanel.reset();
				}
            }
        });



		fontSlider = new JSlider(JSlider.HORIZONTAL, 10, 20, 14);
		fontSlider.setPaintTicks(true);
		fontSlider.setMajorTickSpacing(1);
		fontSlider.setSnapToTicks(true);

		fontSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
				if (!(fontSlider.getValueIsAdjusting())){
					plotpanel.font = new Font("BitStream Vera Sans", Font.PLAIN, fontSlider.getValue());
					plotpanel.reset();
				}
            }
        });
		fontSlider.setToolTipText("Adjust font size");






		labelsButton = new JToggleButton(". (#,#)");
        labelsButton.setToolTipText("Show the coordinates of each node on the plot");
        labelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					if (labelsButton.isSelected()){
						plotpanel.labels = COORDS;
						plotpanel.reset();
					}
            }
        });


		nodeNumbersButton = new JToggleButton(". #");
		nodeNumbersButton.setSelected(true);
        nodeNumbersButton.setToolTipText("Show the node number of each node on the plot");
        nodeNumbersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					if (nodeNumbersButton.isSelected()){
						plotpanel.labels = NUMBERS;
						plotpanel.reset();
					}
            }
        });


		noLabelsButton = new JToggleButton(". ");
        noLabelsButton.setToolTipText("Do not label nodes");
        noLabelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					if (noLabelsButton.isSelected()){
						plotpanel.labels = DOTS;
						plotpanel.reset();
					}
            }
        });


		noNothingButton = new JToggleButton("");
        noNothingButton.setToolTipText("Do not mark or label nodes");
        noNothingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					if (noNothingButton.isSelected()){
						plotpanel.labels = 0;
						plotpanel.reset();
					}
            }
        });






		clickButton = new JToggleButton("Click Nodes");
		clickButton.setSelected(true);
        clickButton.setToolTipText("Use the mouse to create (left button) and delete (right button) nodes");
        clickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					plotpanel.clickNodes = clickButton.isSelected();
            }
        });




		plotButton = new JToggleButton("Plot");
		plotButton.setSelected(false);
        plotButton.setToolTipText("Turn plotting on or off");
        plotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					plotpanel.plotting = plotButton.isSelected();
					plotpanel.reset();
            }
        });


		legendButton = new JToggleButton("Legend");
		legendButton.setSelected(true);
        legendButton.setToolTipText("Turn colorbar on or off");
        legendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					plotpanel.legend = legendButton.isSelected();
					plotpanel.reset();
            }
        });


		axesButton = new JToggleButton("Axes");
		axesButton.setSelected(true);
        axesButton.setToolTipText("Turn axes on or off");
        axesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
					plotpanel.axes = axesButton.isSelected();
					plotpanel.reset();
            }
        });



        tri3Button = new JToggleButton("DELAUNAY");
        tri3Button.setToolTipText("Use linear triangular shape functions");
        tri3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.count > 3){
					tri3Button.setEnabled(false);
					entButton.setSelected(true);
					plotpanel.type = ShapeFunction.MAXENT;
					plotpanel.reset();
				}
				else
					plotpanel.type = ShapeFunction.TRI3;
					plotpanel.reset();
            }
        });

        entButton = new JToggleButton("ENTROPY");
		entButton.setSelected(true);
        entButton.setToolTipText("Use entropy shape functions");
        entButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

					ButtonGroup boxes = new ButtonGroup();
			        final JFrame frame = new JFrame();
					JPanel panel = new JPanel();
					panel.setLayout(new GridLayout(0,2));
					JPanel panelL = new JPanel();
					panelL.setLayout(new GridLayout(0,1));
					JPanel panelR = new JPanel();
					panelR.setLayout(new GridLayout(0,1));

					final JCheckBox maxentBox = new JCheckBox();
					if (plotpanel.type != ShapeFunction.LMAXENT
					 && plotpanel.type != ShapeFunction.RELENT
					 && plotpanel.type != ShapeFunction.RELENTG){
						maxentBox.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
					maxentBox.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
							plotpanel.type = ShapeFunction.MAXENT;
					    }
					});

					JPanel maxentPanel = new JPanel();
					maxentPanel.setLayout(new BorderLayout());
			        maxentPanel.add(maxentBox,BorderLayout.WEST);
			        maxentPanel.add(new JLabel("Uniform prior"),BorderLayout.CENTER);
			        panelL.add(maxentPanel);
					panelR.add(new JPanel());



/*

					final JCheckBox relentgBox = new JCheckBox();
					if (plotpanel.type == ShapeFunction.RELENTG)
						relentgBox.setSelected(true);
					final JSlider relentgSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, pos1);
					relentgSlider.setPaintTicks(false);
					relentgSlider.setSnapToTicks(false);
					relentgSlider.setToolTipText("Slide to increase or decrease the size of the nodes' domains of influence");
					relentgSlider.addChangeListener(new ChangeListener() {
					    public void stateChanged(ChangeEvent e) {
							if (!(relentgSlider.getValueIsAdjusting())){
								pos = relentgSlider.getValue();
								pos1 = pos;
								double[] temparr = plotpanel.nodeSet.thirdRadius();
								for (int i = 0; i < plotpanel.nodeSet.count; i++)
									plotpanel.mlsRadius[i] = (1 + (double)pos/25)*temparr[i];
								plotpanel.type = ShapeFunction.RELENTG;
								relentgBox.setSelected(true);
							}
					    }
					});
					relentgBox.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
							if (relentgBox.isSelected()){
								pos = relentgSlider.getValue();
								double[] temparr = plotpanel.nodeSet.thirdRadius();
								for (int i = 0; i < plotpanel.nodeSet.count; i++)
									plotpanel.mlsRadius[i] = (1 + (double)pos/25)*temparr[i];
								plotpanel.type = ShapeFunction.RELENTG;
							}
					    }
					});
					relentgBox.setToolTipText("Use Gaussian minimum relative entropy shape functions");



					JPanel relentgPanel = new JPanel();
					relentgPanel.setLayout(new BorderLayout());
			        relentgPanel.add(relentgBox,BorderLayout.WEST);
			        relentgPanel.add(new JLabel("Minimum relative entropy (Gaussian prior)"),BorderLayout.CENTER);
			        panelL.add(relentgPanel);
			        panelR.add(relentgSlider);

*/


					final JCheckBox relentBox = new JCheckBox();
					if (plotpanel.type == ShapeFunction.RELENT)
						relentBox.setSelected(true);
					final JSlider relentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, pos2);
					relentSlider.setPaintTicks(false);
					relentSlider.setSnapToTicks(false);
					relentSlider.setToolTipText(("[alpha = ").concat(Format.leftAlign(0,2,plotpanel.nodeSet.mlsFudge * (1 + (double)pos2/25))).concat("]"));
					relentSlider.addChangeListener(new ChangeListener() {
					    public void stateChanged(ChangeEvent e) {
							if (!(relentSlider.getValueIsAdjusting())){
								pos = relentSlider.getValue();
								pos2 = pos;
								plotpanel.mlsRadius = new double[plotpanel.nodeSet.count];
								double[] temparr = plotpanel.nodeSet.thirdRadius();
								for (int i = 0; i < plotpanel.nodeSet.count; i++)
									plotpanel.mlsRadius[i] = (1 + (double)pos2/25)*temparr[i];
								plotpanel.type = ShapeFunction.RELENT;
								relentBox.setSelected(true);
								relentSlider.setToolTipText(("[alpha = ").concat(Format.leftAlign(0,2,plotpanel.nodeSet.mlsFudge * (1 + (double)pos2/25))).concat("]"));
							}
					    }
					});
					relentBox.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
							if (relentBox.isSelected()){
								pos = relentSlider.getValue();
								plotpanel.mlsRadius = new double[plotpanel.nodeSet.count];
								double[] temparr = plotpanel.nodeSet.thirdRadius();
								for (int i = 0; i < plotpanel.nodeSet.count; i++)
									plotpanel.mlsRadius[i] = (1 + (double)pos/25)*temparr[i];
								plotpanel.type = ShapeFunction.RELENT;
							}
					    }
					});


					JPanel relentPanel = new JPanel();
					relentPanel.setLayout(new BorderLayout());
			        relentPanel.add(relentBox,BorderLayout.WEST);
			        relentPanel.add(new JLabel("Compact cubic spline prior"),BorderLayout.CENTER);
			        panelL.add(relentPanel);
			        panelR.add(relentSlider);




					final JCheckBox lmaxentBox = new JCheckBox();
					if (plotpanel.type == ShapeFunction.LMAXENT)
						lmaxentBox.setSelected(true);
					final JSlider lmaxentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, pos3);
					lmaxentSlider.setPaintTicks(false);
					lmaxentSlider.setSnapToTicks(false);
					lmaxentSlider.setToolTipText(("[beta = ").concat(Format.leftAlign(0,2,pos3)).concat("]"));
					lmaxentSlider.addChangeListener(new ChangeListener() {
			            public void stateChanged(ChangeEvent e) {
							if (!(lmaxentSlider.getValueIsAdjusting())){
								pos = lmaxentSlider.getValue();
								pos3 = pos;
								plotpanel.beta = pos;
								plotpanel.type = ShapeFunction.LMAXENT;
								lmaxentBox.setSelected(true);
								lmaxentSlider.setToolTipText(("[beta = ").concat(Format.leftAlign(0,2,pos3)).concat("]"));
							}
			            }
			        });
					lmaxentBox.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
							if (lmaxentBox.isSelected()){
								pos = lmaxentSlider.getValue();
								plotpanel.beta = pos;
								plotpanel.type = ShapeFunction.LMAXENT;
							}
					    }
					});

					JPanel lmaxentPanel = new JPanel();
					lmaxentPanel.setLayout(new BorderLayout());
			        lmaxentPanel.add(lmaxentBox,BorderLayout.WEST);
			        lmaxentPanel.add(new JLabel("Gaussian prior  "),BorderLayout.CENTER);
			        panelL.add(lmaxentPanel);
			        panelR.add(lmaxentSlider);






					JButton doneButton = new JButton("Done");
					doneButton.addActionListener(new ActionListener() {
			        	public void actionPerformed(ActionEvent e) {
							plotpanel.reset();
							frame.setVisible(false);
					    }
					});
					panelL.add(new JPanel());
					panelR.add(doneButton);

					panel.add(panelL);
					panel.add(panelR);
			        frame.setContentPane(panel);
			        frame.pack();
			        frame.setVisible(true);


					boxes.add(maxentBox);
					boxes.add(relentBox);
					boxes.add(lmaxentBox);
            }
        });




        mlsButton = new JToggleButton("MLS");
        mlsButton.setToolTipText("Use moving least-square shape functions");
        mlsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

			        final JFrame frame = new JFrame();
					JPanel panel = new JPanel();
					final JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, pos);
					radiusSlider.setPaintTicks(false);
					radiusSlider.setSnapToTicks(false);
					radiusSlider.setToolTipText(("[alpha = ").concat(Format.leftAlign(0,2,plotpanel.nodeSet.mlsFudge * (1 + (double)pos/25))).concat("]"));
					radiusSlider.addChangeListener(new ChangeListener() {
			            public void stateChanged(ChangeEvent e) {
							if (!(radiusSlider.getValueIsAdjusting())){
								pos = radiusSlider.getValue();
								double[] temparr = plotpanel.nodeSet.thirdRadius();
								plotpanel.mlsRadius = new double[plotpanel.nodeSet.count];
								for (int i = 0; i < plotpanel.nodeSet.count; i++)
									plotpanel.mlsRadius[i] = (1 + (double)pos/25)*temparr[i];
								plotpanel.type = ShapeFunction.MLS;
								plotpanel.reset();
								frame.setVisible(false);
							}
			            }
			        });



					JButton doneButton = new JButton("Done");
					doneButton.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
							pos = radiusSlider.getValue();
							double[] temparr = plotpanel.nodeSet.thirdRadius();
							plotpanel.mlsRadius = new double[plotpanel.nodeSet.count];
							for (int i = 0; i < plotpanel.nodeSet.count; i++)
								plotpanel.mlsRadius[i] = (1 + (double)pos/25)*temparr[i];
							plotpanel.type = ShapeFunction.MLS;
							plotpanel.reset();
							frame.setVisible(false);
			            }
			        });
			        panel.add(new JLabel("Domain of influence: "));
			        panel.add(radiusSlider);
			        panel.add(doneButton);
			        frame.setContentPane(panel);
			        frame.pack();
			        frame.setVisible(true);



            }
        });





        mvcButton = new JToggleButton("MVC");
        mvcButton.setToolTipText("Use mean value coordinate shape functions");
        mvcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.isSimple() || plotpanel.nodeSet.isLooselyConvex()){
					plotpanel.type = ShapeFunction.MVC;
					plotpanel.reset();
				}
				else {
					mvcButton.setEnabled(false);
					entButton.setSelected(true);
					plotpanel.type = ShapeFunction.MAXENT;
					plotpanel.reset();
	            }
            }
        });


        wachButton = new JToggleButton("WSP");
        wachButton.setToolTipText("Use Wachspress shape functions");
        wachButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.isConvex()){
					plotpanel.type = ShapeFunction.WACH;
					plotpanel.reset();
				}
				else {
					wachButton.setEnabled(false);
					entButton.setSelected(true);
					plotpanel.type = ShapeFunction.MAXENT;
					plotpanel.reset();
	            }
            }
        });




        laplaceButton = new JToggleButton("LAPLACE");
        laplaceButton.setToolTipText("Use Laplace shape functions");
        laplaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (plotpanel.nodeSet.isLooselyConvex()){
					plotpanel.type = ShapeFunction.LAPLACE;
					plotpanel.reset();
				}
				else {
					laplaceButton.setEnabled(false);
					entButton.setSelected(true);
					plotpanel.type = ShapeFunction.MAXENT;
					plotpanel.reset();
	            }
            }
        });




        saveButton = new JButton("Generate EPS");
        saveButton.setToolTipText("Generate code for an EPS graphics file");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				String epsString = plotpanel.showEPS();
		        final JFrame frame = new JFrame();



				JScrollPane pane = new JScrollPane(new JTextArea(epsString, 20, 80));
				frame.add(pane);

		        frame.pack();
		        frame.setVisible(true);




			}
        });






//		Check for important changes due to clicked nodes
		plotpanel.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
		    }
		    public void mouseReleased(MouseEvent e) {
		    }
		    public void mouseEntered(MouseEvent e) {
		    }
		    public void mouseExited(MouseEvent e) {
		    }
		    public void mouseClicked(MouseEvent e) {

				check();
				pos = 0;
				plotpanel.reset();

		    }
		});



        JPanel input = new JPanel();
        input.setLayout(new GridLayout(0,1));
		input.setBorder(BorderFactory.createTitledBorder("Menu"));

		input.add(new JLabel("Auto nodes:"));
		input.add(autoNodesPanel);
		input.add(new JLabel("Input a node:"));
		input.add(inputPanel);
		input.add(new JLabel("Remove a node:"));
		input.add(removePanel);
		input.add(clearButton);

		labelsGroup.add(labelsButton);
		labelsGroup.add(nodeNumbersButton);
		labelsGroup.add(noLabelsButton);
		labelsGroup.add(noNothingButton);
        input.add(new JLabel("Node labeling:"));
        JPanel input1a = new JPanel();
        JPanel input1aa = new JPanel();
        input1a.setLayout(new GridLayout(1,2));
        input1aa.setLayout(new GridLayout(1,2));
		input1a.add(labelsButton);
		input1a.add(nodeNumbersButton);
		input1aa.add(noLabelsButton);
		input1aa.add(noNothingButton);
		input.add(input1a);
		input.add(input1aa);

		input.add(new JLabel("Font:"));
		input.add(fontSlider);

		input.add(new JPanel());

		JPanel input2c = new JPanel();
		input2c.setLayout(new GridLayout(1,2));
        input2c.add(plotButton);
        input2c.add(axesButton);
		JPanel input2cc = new JPanel();
		input2cc.setLayout(new GridLayout(1,2));
        input2cc.add(legendButton);
        input2cc.add(clickButton);
		input.add(input2c);
		input.add(input2cc);
        input.add(plot2DButton);

		input.add(new JLabel("Formulation:"));


		buttonGroup.add(tri3Button);
		buttonGroup.add(wachButton);
		buttonGroup.add(laplaceButton);
		buttonGroup.add(entButton);
		buttonGroup.add(mlsButton);
		buttonGroup.add(mvcButton);
        JPanel input2a = new JPanel();
        input2a.setLayout(new GridLayout(1,2));
        JPanel input2aa = new JPanel();
        input2aa.setLayout(new GridLayout(1,2));
        JPanel input2d = new JPanel();
        input2d.setLayout(new GridLayout(1,2));
		input2a.add(entButton);
		input2a.add(mlsButton);

		input2aa.add(tri3Button);
		input2aa.add(laplaceButton);
		input2d.add(mvcButton);
		input2d.add(wachButton);

		input.add(input2a);
		input.add(input2aa);
		input.add(input2d);



		input.add(new JLabel("Set a node:"));
        input.add(nodePanel);


		input.add(new JLabel("Detail:"));
		input.add(fineSlider);


		input.add(new JPanel());



		input.add(saveButton);



		input.add(new JLabel("Find a value:"));
		input.add(valuePanela);
		input.add(valuePanelb);



        this.add(input, BorderLayout.WEST);




	}



		private void check(){
				if (plotpanel.nodeSet.count > 3){
					tri3Button.setEnabled(false);
					if (plotpanel.type == ShapeFunction.TRI3){
						entButton.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
				}
				if (!(plotpanel.nodeSet.isConvex())){
					wachButton.setEnabled(false);
					laplaceButton.setEnabled(false);
					if (plotpanel.type == ShapeFunction.WACH || plotpanel.type == ShapeFunction.LAPLACE){
						entButton.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
				}
				if (!(plotpanel.nodeSet.isConvex())){
					wachButton.setEnabled(false);
					if (plotpanel.type == ShapeFunction.WACH){
						entButton.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
				}
				if (!(plotpanel.nodeSet.isLooselyConvex())){
					laplaceButton.setEnabled(false);
					if (plotpanel.type == ShapeFunction.LAPLACE){
						entButton.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
				}
				if (!(plotpanel.nodeSet.isSimple() || plotpanel.nodeSet.isLooselyConvex())){
					mvcButton.setEnabled(false);
					if (plotpanel.type == ShapeFunction.MVC){
						entButton.setSelected(true);
						plotpanel.type = ShapeFunction.MAXENT;
					}
				}
				if (plotpanel.nodeSet.count <= 3)
					tri3Button.setEnabled(true);
				if (plotpanel.nodeSet.isConvex()){
					wachButton.setEnabled(true);
				}
				if (plotpanel.nodeSet.isLooselyConvex()){
					laplaceButton.setEnabled(true);
					mlsButton.setEnabled(true);
				}
				if (plotpanel.nodeSet.isSimple() || plotpanel.nodeSet.isLooselyConvex())
					mvcButton.setEnabled(true);



				inputPanel.repaint();
		}








}