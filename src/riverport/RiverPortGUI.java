package riverport;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

import process.Dispatcher;
import process.IModelFactory;
import rnd.Erlang;
import rnd.Norm;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;
import widgets.experiments.ExperimentManager;
import widgets.stat.StatisticsManager;
import widgets.trans.TransProcessManager;
import javax.swing.JTextArea;

import javax.imageio.ImageIO;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class RiverPortGUI extends JFrame{
  
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Diagram diagramBarge = null;

	private Diagram diagramCars = null;

	private Diagram diagramCarsonRoad = null;

	private JButton jButtonStart = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanelTest = null;

	private JScrollPane jScrollPaneTz = null;

	private JPanel jPanelTransient = null;

	private TransProcessManager transProcessManager = null;

	private JPanel jPanelModelParameters = null;

	private Dispatcher dispatcher = null;

	private JCheckBox jCheckBox = null;

	private JPanel jPanelStat = null;

	private ChooseRandom rndLoader = null;

	private ChooseData chooseDataCarsNumber = null;

	private ChooseData chooseDataBargeNumber = null;

	private ChooseRandom rndCars = null;

	private ChooseData chooseDataLoaderNumber = null;

	private ChooseData chooseDataFinishTime = null;

	private JTextPane jTextPane = null;
	
	private StatisticsManager statisticsManager;
	
	private JPanel jPanelRegres;
	
	private JPanel panelInfo;
	
	private JPanel panelPhoto;
	
	private JTextArea textArea;
	
	private ChooseData chooseDataCapacityContainer;
	
	private ChooseRandom rndBarge = null;

	public RiverPortGUI() throws HeadlessException {
		super();
		initialize();
	}


	/**
	 * This method initializes jButtonStart
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setText("Старт");
			jButtonStart.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					startTest();
				}
			});
		}
		return jButtonStart;
	}

	private void startTest() {
		getJButtonStart().setEnabled(false);
		getDiagramBarge().clear();
		getDiagramCars().clear();
		getDiagramCarsonRoad().clear();
		Dispatcher dispatcher = new Dispatcher();		
		dispatcher.addDispatcherFinishListener(
				()->getJButtonStart().setEnabled(true));
		IModelFactory factory = (d)-> new RiverModel(d, this);
		RiverModel model =(RiverModel) factory.createModel(dispatcher);
		model.initForTest();    
		dispatcher.start();
	}

	
	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setName("");
			jTabbedPane.setVisible(true);
			jTabbedPane.setFont(new java.awt.Font("Courier New",
					java.awt.Font.PLAIN, 14));
			//
			//

			jTabbedPane.addTab("Tz", null, getJScrollPaneTz(), null);
			jTabbedPane.addTab("Test", null, getJPanelTest(), null);
			jTabbedPane.addTab("Stat", null, getJPanelStat(),"Статистичні характеристики");
			jTabbedPane.addTab("Regres", null, getJPanelRegres(), null);
			jTabbedPane.addTab("Transient", null, getJPanelTransient(), null);
			jTabbedPane.addTab("Info", null, getPanelInfo(), null);
			
			//jTabbedPane.addTab("Info", null, getJPanelTransient(), null);

		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanelTest
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTest() {
		if (jPanelTest == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.insets = new java.awt.Insets(4, 8, 3, 2);
			gridBagConstraints13.gridy = 4;
			gridBagConstraints13.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.insets = new Insets(2, 3, 0, 4);
			gridBagConstraints12.gridy = 4;
			gridBagConstraints12.ipadx = 10;
			gridBagConstraints12.ipady = -1;
			gridBagConstraints12.anchor = GridBagConstraints.EAST;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.gridx = 2;
			GridBagConstraints gbc_diagramCarsonRoad = new GridBagConstraints();
			gbc_diagramCarsonRoad.insets = new Insets(1, 4, 1, 4);
			gbc_diagramCarsonRoad.gridx = 0;
			gbc_diagramCarsonRoad.gridy = 2;
			gbc_diagramCarsonRoad.ipadx = 183;
			gbc_diagramCarsonRoad.ipady = -86;
			gbc_diagramCarsonRoad.fill = java.awt.GridBagConstraints.BOTH;
			gbc_diagramCarsonRoad.weightx = 1.0D;
			gbc_diagramCarsonRoad.weighty = 1.0D;
			gbc_diagramCarsonRoad.gridwidth = 4;
			GridBagConstraints gbc_diagramCars = new GridBagConstraints();
			gbc_diagramCars.insets = new Insets(0, 3, 0, 4);
			gbc_diagramCars.gridx = 0;
			gbc_diagramCars.gridy = 1;
			gbc_diagramCars.ipadx = 183;
			gbc_diagramCars.ipady = -86;
			gbc_diagramCars.weightx = 1.0D;
			gbc_diagramCars.weighty = 1.0D;
			gbc_diagramCars.fill = java.awt.GridBagConstraints.BOTH;
			gbc_diagramCars.gridwidth = 4;
			GridBagConstraints gbc_diagramBarge = new GridBagConstraints();
			gbc_diagramBarge.insets = new Insets(4, 3, 0, 4);
			gbc_diagramBarge.gridx = 0;
			gbc_diagramBarge.gridy = 0;
			gbc_diagramBarge.ipadx = 183;
			gbc_diagramBarge.ipady = -86;
			gbc_diagramBarge.fill = java.awt.GridBagConstraints.BOTH;
			gbc_diagramBarge.weightx = 1.0D;
			gbc_diagramBarge.weighty = 2.0D;
			gbc_diagramBarge.gridwidth = 4;
			jPanelTest = new JPanel();
			jPanelTest.setLayout(new GridBagLayout());
			jPanelTest.add(getDiagramBarge(), gbc_diagramBarge);
			jPanelTest.add(getDiagramCars(), gbc_diagramCars);
			jPanelTest.add(getDiagramCarsonRoad(), gbc_diagramCarsonRoad);
			jPanelTest.add(getJButtonStart(), gridBagConstraints12);
			jPanelTest.add(getJCheckBox(), gridBagConstraints13);
			jPanelTest
			.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentShown(
						java.awt.event.ComponentEvent e) {
					// Штучно формуємо подію CaretUpdate,
					// щоб ооновити налаштування діаграм
					getChooseDataCarsNumber().select(0,0);
					getChooseDataFinishTime().select(0,0);
					getChooseDataLoaderNumber().select(0,0);
				}
			});
		}
		return jPanelTest;
	}

	/**
	 * This method initializes jScrollPaneTz
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneTz() {
		if (jScrollPaneTz == null) {
			jScrollPaneTz = new JScrollPane();
			jScrollPaneTz.setName("jScrollPaneTz");
			jScrollPaneTz.setViewportView(getJTextPane());
			jScrollPaneTz
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jScrollPaneTz;
	}

	/**
	 * This method initializes jPanelTransient
	 * 
	 * @return javax.swing.JPanel
	 */
	
	
	
	private JPanel getJPanelTransient() {
		if (jPanelTransient == null) {
			jPanelTransient = new JPanel();
			jPanelTransient.setLayout(new CardLayout(0, 0));
			jPanelTransient.add(getTransProcessManager(), "name_34492577955736");

		}
		return jPanelTransient;
	}

	/**
	 * This method initializes transMonitorView
	 * 
	 * @return transProcess.TransMonitorView
	 */
	private TransProcessManager getTransProcessManager() {
		if (transProcessManager == null) {
			transProcessManager = new TransProcessManager();
			transProcessManager.setNumberOfInterval("20");
			transProcessManager.setTextInterval("5");
			transProcessManager.setFactory((d)-> new RiverModel(d, this));

		}
		return transProcessManager;
	}

	/**
	 * This method initializes jPanelModelParameters
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelModelParameters() {
		if (jPanelModelParameters == null) {
			jPanelModelParameters = new JPanel();
			jPanelModelParameters.setLayout(null);
			jPanelModelParameters
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									javax.swing.BorderFactory
											.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
									"Параметри системи що досліджується",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12),
									new java.awt.Color(51, 51, 51)));
			jPanelModelParameters.setPreferredSize(new Dimension(262, 436));
			jPanelModelParameters.setMinimumSize(new Dimension(262, 600));
			jPanelModelParameters.add(getRndLoader(), null);
			jPanelModelParameters.add(getChooseDataCarsNumber(), null);
			jPanelModelParameters.add(getChooseDataBargeNumber(), null);
			jPanelModelParameters.add(getRndCars(), null);
			jPanelModelParameters.add(getChooseDataLoaderNumber(), null);
			jPanelModelParameters.add(getChooseDataFinishTime(), null);
			jPanelModelParameters.add(getChooseDataCapacityContainer(), null);
			jPanelModelParameters.add(getRndBarge());
		}
		return jPanelModelParameters;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox
					.setActionCommand("\u0412\u044b\u0432\u043e\u0434 \u043f\u0440\u043e\u0442\u043e\u043a\u043e\u043b\u0430 \u043d\u0430 \u043a\u043e\u043d\u0441\u043e\u043b\u044c");
			jCheckBox.setText("Протокол на консоль");
			jCheckBox.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelStat() {
		if (jPanelStat == null) {
			jPanelStat = new JPanel();
			jPanelStat.setLayout(new CardLayout(0, 0));
			jPanelStat.add(getStatisticsManager(), "name_131147950583608");
		}
		return jPanelStat;
	}

	/**
	 * This method initializes rndBuldo	

	/**
	 * This method initializes rndLoader	
	 * 	
	 * @return widgets.ChooseRandom	
	 */
	public ChooseRandom getRndLoader() {
		if (rndLoader == null) {
			rndLoader = new ChooseRandom();
			rndLoader.setRandom(new Norm(1,0.2));
			rndLoader.setTitle("Продуктивність прогрузчиків");
			rndLoader.setBounds(new Rectangle(3, 24, 231, 52));
		}
		return rndLoader;
	}


	/**
	 * This method initializes chooseDataNLorry	
	 * 	
	 * @return widgets.ChooseData	
	 */
	public ChooseData getChooseDataCarsNumber() {
		if (chooseDataCarsNumber == null) {
			chooseDataCarsNumber = new ChooseData();
			chooseDataCarsNumber.setBounds(new Rectangle(3, 331, 231, 53));
			chooseDataCarsNumber.setTitle("Кількість авто");
			chooseDataCarsNumber.setText("2");
			chooseDataCarsNumber
			.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					if (getJPanelTest().isShowing()){
						getDiagramCarsonRoad().setVerticalMaxText(
								chooseDataCarsNumber.getText());
						getDiagramCars().setVerticalMaxText(
								chooseDataCarsNumber.getText());
					}
				}
			});

		}
		return chooseDataCarsNumber;
	}


	/**
	 * This method initializes chooseDataBodySize	
	 * 	
	 * @return widgets.ChooseData	
	 */
	public ChooseData getChooseDataBargeNumber() {
		if (chooseDataBargeNumber == null) {
			chooseDataBargeNumber = new ChooseData();
			chooseDataBargeNumber.setBounds(new Rectangle(3, 203, 231, 53));
			chooseDataBargeNumber.setTitle("Кількість барж");
			chooseDataBargeNumber.setText("4");
			chooseDataBargeNumber.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					if (getJPanelTest().isShowing()){
						getDiagramBarge().setVerticalMaxText(chooseDataBargeNumber.getText());
						
					}
				}
			});
		}
		return chooseDataBargeNumber;
	}


	/**
	 * This method initializes rndLorry	
	 * 	
	 * @return widgets.ChooseRandom	
	 */
	public ChooseRandom getRndCars() {
		if (rndCars == null) {
			rndCars = new ChooseRandom();
			rndCars.setBounds(new Rectangle(3, 76, 231, 52));
			rndCars.setRandom(new Erlang(20, 4));
			rndCars.setTitle("Перебування автомобіля у дорозі");
		}
		return rndCars;
	}


	/**
	 * This method initializes chooseDataHeapMaxSize	
	 * 	
	 * @return widgets.ChooseData	
	 */
	public ChooseData getChooseDataLoaderNumber() {
		if (chooseDataLoaderNumber == null) {
			chooseDataLoaderNumber = new ChooseData();
			chooseDataLoaderNumber.setBounds(new Rectangle(3, 267, 231, 53));
			chooseDataLoaderNumber.setTitle("Кількість бригад погрузчиків");
			chooseDataLoaderNumber.setText("1");
			chooseDataLoaderNumber.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					//if (getJPanelTest().isShowing())
						//getDiagramBarge().setVerticalMaxText(chooseDataLoaderNumber.getText());
				}
			});

		}
		return chooseDataLoaderNumber;
	}


	/**
	 * This method initializes chooseDataFinishTime	
	 * 	
	 * @return widgets.ChooseData	
	 */
	public ChooseData getChooseDataFinishTime() {
		if (chooseDataFinishTime == null) {
			chooseDataFinishTime = new ChooseData();
			chooseDataFinishTime.setBounds(new Rectangle(3, 447, 231, 53));
			chooseDataFinishTime.setTitle("Час моделювання");
			chooseDataFinishTime.setText("500");
			chooseDataFinishTime
			.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					if (getJPanelTest().isShowing()) {
						getDiagramBarge().setHorizontalMaxText(
								chooseDataFinishTime.getText());
						getDiagramCars().setHorizontalMaxText(
								chooseDataFinishTime.getText());
						getDiagramCarsonRoad().setHorizontalMaxText(
								chooseDataFinishTime.getText());
					}
				}
			});

		}
		return chooseDataFinishTime;
	}


	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */ 
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			String str="/riverport/tz.htm";
			URL url = getClass().getResource(str);
			try {
				jTextPane.setPage(url);
			} catch (IOException e33) {
				System.err
						.println("Problems with file "+str);
			}

		}
		return jTextPane;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RiverPortGUI application = new RiverPortGUI();
		application.setVisible(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(957, 622);
		this.setContentPane(getJContentPane());
		this.setTitle("Дослідження розвантаження та доставки контейнерів шляхом імітаційного моделювання");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.NORTH;
			gridBagConstraints7.insets = new Insets(9, 10, 7, 2);
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.ipadx = -21;
			gridBagConstraints7.ipady = 13;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.ipadx = -96;
			gridBagConstraints6.ipady = -294;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(8, 3, 6, 7);
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJTabbedPane(), gridBagConstraints6);
			jContentPane.add(getJPanelModelParameters(), gridBagConstraints7);
		}
		return jContentPane;
	}

	// /////////////////////////////////////////////////
	// ///Реалізація інтерфейсу IBuldoGUI
	// //////////////////////////////////////////////////


	/*
	 * @see buldo2009.IBuldoGUI Метод повертає стан перемикача управління
	 *      виведенням протоколу на консоль
	 */
	public Boolean getProtocolToConcole() {
		return getJCheckBox().isSelected();
	}


	/**
	 * This method initializes diagramHeepSize
	 * 
	 * @see buldo2011.IBuldoGUI
	 * @return paint.Diagram
	 */
	public Diagram getDiagramBarge() {
		if (diagramBarge == null) {
			diagramBarge = new Diagram();
			diagramBarge.setHorizontalMaxText("500");
			diagramBarge.setTitleText("Розмір черги барж до погрузчиків");
			diagramBarge.setVerticalMaxText("4");
			diagramBarge.setPainterColor(new java.awt.Color(204, 102, 0));
		}
		return diagramBarge;
	}

	/**
	 * This method initializes diagramQueueToLoader
	 * 
	 * @see buldo2011.IBuldoGUI
	 * @return paint.Diagram
	 */
	public Diagram getDiagramCars() {
		if (diagramCars == null) {
			diagramCars = new Diagram();
			diagramCars.setHorizontalMaxText("500");
			diagramCars.setTitleText("Черга авто до погрузчиків");
			diagramCars.setVerticalMaxText("6");
			diagramCars.setPainterColor(java.awt.Color.magenta);
		}
		return diagramCars;
	}

	/**
	 * This method initializes diagram
	 * 
	 * @see buldo2011.IBuldoGUI
	 * @return paint.Diagram
	 */
	public Diagram getDiagramCarsonRoad() {
		if (diagramCarsonRoad == null) {
			diagramCarsonRoad = new Diagram();
			diagramCarsonRoad.setHorizontalMaxText("500");
			diagramCarsonRoad
					.setTitleText("Автомобілі у дорозі");
			diagramCarsonRoad.setVerticalMaxText("6");
			diagramCarsonRoad.setPainterColor(java.awt.SystemColor.activeCaption);
		}
		return diagramCarsonRoad;
	}

	public Dispatcher getDispatcher() {
		if (dispatcher == null)
			dispatcher = new Dispatcher();
		return dispatcher;
	}


	private StatisticsManager getStatisticsManager() {
		if (statisticsManager == null) {
			statisticsManager = new StatisticsManager();
			statisticsManager.setFactory((d)-> new RiverModel(d, this));
		}
		return statisticsManager;
	}
	
	
	
	private JPanel getJPanelRegres() {
		if (jPanelRegres == null) {
			jPanelRegres = new JPanel();
			jPanelRegres.setLayout(new CardLayout(0, 0));		
			ExperimentManager em = new ExperimentManager();
			em.getChooseDataFactors().setTitle("Кількість барж");
			em.getComboBox().setPreferredSize(new Dimension(328, 20));
			em.getComboBox().setMinimumSize(new Dimension(328, 20));
			em.getDiagram().setHorizontalMaxText("15");
			em.getDiagram().setVerticalMaxText("500");
			em.getChooseDataFactors().setText("1 4 6 8 10 14");
			em.setFactory((d)-> new RiverModel(d, this));
			jPanelRegres.add(em, "name_111625814938837");
		}
		return jPanelRegres;
	}
	
	private JPanel getPanelInfo() {
		if (panelInfo == null) {
			panelInfo = new JPanel();
			panelInfo.setToolTipText("");
			GridBagLayout gbl_panelInfo = new GridBagLayout();
			gbl_panelInfo.columnWidths = new int[]{0, 0};
			gbl_panelInfo.rowHeights = new int[]{0, 0, 0};
			gbl_panelInfo.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panelInfo.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			panelInfo.setLayout(gbl_panelInfo);
			GridBagConstraints gbc_panelPhoto = new GridBagConstraints();
			gbc_panelPhoto.insets = new Insets(0, 0, 5, 0);
			gbc_panelPhoto.fill = GridBagConstraints.BOTH;
			gbc_panelPhoto.gridx = 0;
			gbc_panelPhoto.gridy = 0;
			panelInfo.add(getPanelPhoto(), gbc_panelPhoto);
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.gridx = 0;
			gbc_textArea.gridy = 1;
			panelInfo.add(getTextArea_1(), gbc_textArea);
		}
		return panelInfo;
	}
	@SuppressWarnings("serial")
	private JPanel getPanelPhoto() {
		if (panelPhoto == null) {
			panelPhoto = new JPanel() {
				public void paintComponent(Graphics g){
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					BufferedImage img;
					URL url = getClass().getResource("/resource/2.png");
					try {
						img = ImageIO.read(url);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					double k = (double)img.getHeight() / img.getWidth();
					int width = getWidth();
					int height = getHeight();
					if((double)height / width > k)
						height = (int) (width *k);
					else
						width = (int) (height /k);
					Image scaledImg = img.getScaledInstance(
	                            width, height, Image.SCALE_SMOOTH);
					g2d.drawImage(scaledImg,0,0,null);
				};
			};
		}
		return panelPhoto;
	}

	private JTextArea getTextArea_1() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setText("Розробник програми:\r\nВасюк Дмитро Олександрович\r\nСтудент групи Кіт-211\r\nТел: 0967630427\r\nMail: vasyuk.dima27@gmail.com\r\n");
		}
		return textArea;
	}
	
	public ChooseData getChooseDataCapacityContainer() {
		
		if (chooseDataCapacityContainer == null) {
			chooseDataCapacityContainer = new ChooseData();
			chooseDataCapacityContainer.setBounds(new Rectangle(3, 395, 231, 49));
			chooseDataCapacityContainer.setTitle("Місткість контейнера");
			chooseDataCapacityContainer.setText("20");
			
		}
		
		return chooseDataCapacityContainer;
	}
	public ChooseRandom getRndBarge() {
		if(rndBarge == null) {
			rndBarge = new ChooseRandom();
			rndBarge.setBounds(3, 139, 231, 52);
			rndBarge.setRandom(new Erlang(20, 4));
			rndBarge.setTitle("Перебування баржі у морі");
		}
		return rndBarge;
	}
}  //  @jve:decl-index=0:visual-constraint="42,-8"
// @jve:decl-index=0:visual-constraint="10,8"
